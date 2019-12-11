package com.leyou.page.service;

import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 10:47
 * @Description:
 */
public interface PageService {
    /**
     * 查询商品详情页相关内容
     * @param spuId
     * @return
     */
    Map<String,Object> loadModel(Long spuId);

    /**
     * 生成商品详情html
     * @param spuId 商品id
     */
    void createGoodsHtml(Long spuId);

    /**
     * 删除静态页
     * @param spuId
     */
    void deleteHtml(Long spuId);
}
