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
     * @param spu
     * @return
     */
    Goods buildGoods(Spu spu);

    /**
     * 搜索商品列表
     * @param searchRequest
     * @return
     */
    PageResult<Goods> search(SearchRequest searchRequest);
}
