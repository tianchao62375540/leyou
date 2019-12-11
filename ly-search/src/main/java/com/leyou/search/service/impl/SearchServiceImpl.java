package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:43
 * @Description:
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        String categoryNames = categories.stream().map(Category::getName).collect(Collectors.joining(","));
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        String brandName = brand.getName();

        //搜索字段
        String all = spu.getTitle() + categoryNames + brandName;
        //查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuId);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        Set<Long> prices = new HashSet<>();
        //所有sku的json集合
        //对skus进行处理
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            maps.add(map);
            prices.add(sku.getPrice());
        }
        //查询规格参数
        List<SpecParam> params = specificationClient.querySpecParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        //通用规格参数
        String genericSpecJson = spuDetail.getGenericSpec();
        Map<Long, String> genericSpec = JsonUtils.parseMap(genericSpecJson, Long.class, String.class);
        //特有规格参数 特有是 规格id:可选项集合 逗号分开
        String specialSpecJson = spuDetail.getSpecialSpec();
        Map<Long, List<String>> specialSpec = JsonUtils
                .nativeRead(specialSpecJson, new TypeReference<Map<Long, List<String>>>() {
                });

        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            String key = param.getName();
            Object value = "";
            //通用
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId());
                //处理段
                if (param.getNumeric()) {
                    value = chooseSegment(value.toString(), param);
                }
            //特有
            } else {
                value = specialSpec.get(param.getId());
            }
            specs.put(key, value);
        }

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spuId);
        //搜索字段，包含标题，分类，品牌，规格等
        goods.setAll(all);
        //sku价格集合
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(maps));
        //规格参数中可搜索的
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    /**
     * 搜索商品
     * @param searchRequest
     * @return
     */
    @Override
    public PageResult<Goods> search(SearchRequest searchRequest) {
        int page = searchRequest.getPage() - 1;
        int size = searchRequest.getSize();
        //原生查询构建起
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //0结果过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //1分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page,size));
        //2搜索条件 这里改为过滤条件和查询条件分开
        //MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all", searchRequest.getKey());
        QueryBuilder basicQuery = buildBasicQuery(searchRequest);
        nativeSearchQueryBuilder.withQuery(basicQuery);
        //3聚合分类和品牌
        //3.1聚合分类
        String categoryAggName = "category_agg";
        String brandAggName = "brand_agg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //4查询
        //Page<Goods> result = goodsRepository.search(nativeSearchQueryBuilder.build());
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        //5解析
        //5.1解析分页结果
        long total = result.getTotalElements();
        Integer totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        //5.2解析聚合结果
        Aggregations aggregations = result.getAggregations();
        List<Category> categories = parseCategory(aggregations.get(categoryAggName));
        List<Brand> brands = parseBrand(aggregations.get(brandAggName));
        //6 规格参数聚合
        List<Map<String,Object>> specs = null;
        if(categories!=null && categories.size() ==1){
            //商品分类存在并且为1,才完成聚合分类规格参数
            specs = buildSpecoficationAgg(categories.get(0).getId(),basicQuery);
        }
        return new SearchResult(total,totalPage,goodsList,categories,brands,specs);
    }

    /**
     * 创建和修改索引
     *
     * @param spuId
     */
    @Override
    public void createOrUpdateIndex(Long spuId) {
        //构建spu
        Spu spu = goodsClient.querySpuById(spuId);
        //构建goods
        Goods goods = buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);
    }

    /**
     * 删除索引 根据商品id
     *
     * @param spuId 商品id
     */
    @Override
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }

    /**
     * 构建基本查询
     * @param searchRequest
     * @return
     */
    private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
        //创建布尔条件
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        //过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //处理key 分类和品牌
            if(!"cid3".equals(key)&&!"brandId".equals(key)){
                key = "specs."+key+".keyword";
            }
            queryBuilder.filter(QueryBuilders.termsQuery(key,value));
        }
        return queryBuilder;
    }

    /**
     *
     * @param cid 商品分类id
     * @param basicQuery 查询条件
     * @return
     */
    private List<Map<String, Object>> buildSpecoficationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //1查询需要聚合的规格参数
        List<SpecParam> params = specificationClient.querySpecParamList(null,cid,true);
        //2聚合
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //2.1带上查询条件
        nativeSearchQueryBuilder.withQuery(basicQuery);
        //2.2聚合
        for (SpecParam param : params) {
            String name = param.getName();
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
        }
        //3获取结果
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        //4解析结果
        Aggregations aggs = result.getAggregations();
        for (SpecParam param : params) {
            String name = param.getName();
            StringTerms aggregation = aggs.get(name);
            List<StringTerms.Bucket> buckets = aggregation.getBuckets();
            List<String> options = buckets.stream().map(e -> e.getKeyAsString()).collect(Collectors.toList());
            //准备map
            Map<String,Object> map = new HashMap<>();
            map.put("k",name);
            map.put("options",options);
            specs.add(map);
        }
        return specs;
    }

    /**
     * 解析品牌
     * @param longTerms
     * @return
     */
    private List<Brand> parseBrand(LongTerms longTerms) {
        try {
            List<LongTerms.Bucket> buckets = longTerms.getBuckets();
            List<Long> ids = buckets.stream().map(e -> e.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return brandClient.queryBrandByIds(ids);
        }catch (Exception e){
            log.error("[搜索服務]查询品牌异常:"+e);
            return null;
        }


    }

    /**
     * 解析分类聚合
     * @param longTerms
     * @return
     */
    private List<Category> parseCategory(LongTerms longTerms) {
        try {
            List<LongTerms.Bucket> buckets = longTerms.getBuckets();
            List<Long> ids = buckets.stream().map(e -> e.getKeyAsNumber().longValue()).collect(Collectors.toList());
            return categoryClient.queryCategoryByIds(ids);
        }catch (Exception e){
            log.error("[搜索服務]查询分类异常:"+e);
            return null;
        }
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


}