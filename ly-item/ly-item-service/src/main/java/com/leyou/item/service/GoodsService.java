package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: tianchao
 * @Date: 2019/11/3 21:58
 * @Description:
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //上下架过滤
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //默认排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        //判断
        if(CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //解析分类和品牌的名称
        loadCategoryAndBrandName(spus);
        //解析
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 加载分类名 和 品牌的名称
     * @param spus
     */
    private void loadCategoryAndBrandName(List<Spu> spus) {
        for(Spu spu: spus){
            //处理分类名称
            List<Category> categories = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));
            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }

    /**
     * 保存商品
     * @param spu
     */
    @Transactional
    public void saveGoods(Spu spu) {
        //1新增spu
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spu.setSaleable(true);
            //是否有效
        spu.setValid(true);
        int count = spuMapper.insertSelective(spu);
        if(count != 1 ){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //2新增spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insertSelective(spuDetail);
        if(count != 1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //3新增sku和stock
        SaveSkuAndStock(spu);
        //4发送mq消息
        amqpTemplate.convertAndSend("item.insert",spu.getId());
    }

    private void SaveSkuAndStock(Spu spu) {
        //定义库存集合
        List<Stock> stockList = new ArrayList<>();
        int count;
        List<Sku> skus = spu.getSkus();
        for(Sku sku:skus){
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            sku.setSpuId(spu.getId());
            count = skuMapper.insertSelective(sku);
            if(count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            //4新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
            /*count = stockMapper.insertSelective(stock);
            if(count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }*/
        }
        count = stockMapper.insertList(stockList);
        if( count != stockList.size()){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    /**
     * 修改商品
     * @param spu
     */
    public void editGoods(Spu spu) {
        if(spu==null){
            throw new LyException(ExceptionEnum.GOODS_ID_CANNOT_NULL);
        }
        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOODS_EDIT_ERROR);
        }
        int count = skuMapper.delete(sku);
        if(count != skuList.size()){
            throw new LyException(ExceptionEnum.GOODS_EDIT_ERROR);
        }
        //删除stock
        List<Long> skuIds = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        count = stockMapper.deleteByIdList(skuIds);
        if(count != skuIds.size()){
            throw new LyException(ExceptionEnum.GOODS_EDIT_ERROR);
        }
        //修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count !=1 ){
            throw new LyException(ExceptionEnum.GOODS_EDIT_ERROR);
        }
        //修改spuDetail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count !=1 ){
            throw new LyException(ExceptionEnum.GOODS_EDIT_ERROR);
        }
        //新增sku  //新增stock
        SaveSkuAndStock(spu);
        //发送消息
        amqpTemplate.convertAndSend("item.update",spu.getId());
    }
    /**
     * 根据spuid获取spu详情
     * @param spuId
     * @return
     */
    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(spuDetail == null){
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 根据spuId查询下面所有sku
     * @param spuId
     * @return
     */
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //查询库存
       /* for(Sku s: skuList){
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock==null){
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }*/
        setStockNum(skuList);
        return skuList;
    }

    /**
     *根据sku集合补充库存量字段
     * @param skuList
     */
    private void setStockNum(List<Sku> skuList) {
        List<Long> skuIds = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(skuIds);
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.stream().forEach(e->e.setStock(stockMap.get(e.getId())));
    }

    /**
     * 根据id查询spu
     * @param id
     * @return
     */
    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询sku
        spu.setSkus(querySkuBySpuId(id));
        //查询detail
        spu.setSpuDetail(queryDetailById(id));
        return spu;
    }

    /**
     *
     * @param ids
     * @return
     */
    public List<Sku> querySkuBySkuIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //根据sku集合补充库存量字段
        setStockNum(skus);
        return skus;
    }
}
