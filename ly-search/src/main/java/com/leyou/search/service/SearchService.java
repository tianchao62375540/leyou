package com.leyou.search.service;

import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:42
 * @Description:
 */
public interface SearchService {
    Goods buildGoods(Spu spu);
}
