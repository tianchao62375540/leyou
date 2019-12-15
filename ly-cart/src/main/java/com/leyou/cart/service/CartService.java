package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 21:45
 * @Description:
 */
public interface CartService {
    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 获取用户购物车数据
     * @return
     */
    List<Cart> queryCartList();
}
