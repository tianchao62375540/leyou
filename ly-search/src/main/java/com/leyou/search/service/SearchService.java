package com.leyou.search.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:42
 * @Description:
 */
public interface SearchService {
    /**
     * 构建商品
     * @param spu 商品对象
     * @return
     */
    Goods buildGoods(Spu spu);

    /**
     * 搜索商品列表
     * @param searchRequest 搜索
     * @return
     */
    PageResult<Goods> search(SearchRequest searchRequest);

    /**
     * 创建和修改索引
     * @param spuId 商品id
     */
    void createOrUpdateIndex(Long spuId);

    /**
     * 删除索引 根据商品id
     * @param spuId 商品id
     */
    void deleteIndex(Long spuId);
}
