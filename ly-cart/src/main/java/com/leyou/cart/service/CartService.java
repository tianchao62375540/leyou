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

    /**
     *  修改购物车数量
     * @param id
     * @param num
     */
    void updateCartNum(Long id, Integer num);

    /**
     * 根据skuId删除购物车
     * @param skuId
     */
    void deleteCart(Long skuId);
}
