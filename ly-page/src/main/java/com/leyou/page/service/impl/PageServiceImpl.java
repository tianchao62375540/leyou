package com.leyou.page.service.impl;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 10:50
 * @Description:
 */
@Service
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;
    /**
     * 查询商品详情页相关内容
     *
     * @param spuId
     * @return
     */
    @Override
    public Map<String, Object> loadModel(Long spuId) {
        Map<String,Object> model = new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        //详情
        SpuDetail detail = spu.getSpuDetail();
        //品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //分类
        List<Category> categories = categoryClient.
                queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        //规格组 包含规格参数
        List<SpecGroup> specs = specificationClient.queryGroupsByid(spu.getCid3());
        //model.put("spu",spu);
        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specs);
        return model;
    }

    /**
     * 删除静态页
     *
     * @param spuId
     */
    @Override
    public void deleteHtml(Long spuId) {
        File dest = new File("G:\\leyou-server-zongjie\\upload", spuId + ".html");
        if (dest.exists()){
            dest.delete();
        }
    }

    /**
     * 生成商品详情html
     *
     * @param spuId 商品id
     */
    @Override
    public void createGoodsHtml(Long spuId) {
        //准备上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //准备输出流
        File dest = new File("G:\\leyou-server-zongjie\\upload", spuId + ".html");
        if (dest.exists()){
            dest.delete();
        }
        try (PrintWriter writer = new PrintWriter(dest,"UTF-8")){
            //生成html
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            log.error("[静态页服务]生成静态页异常！",e);
        }

    }
}
