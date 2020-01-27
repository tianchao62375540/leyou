package com.leyou.cart.service.impl;

import com.leyou.cart.context.UserInfoContext;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 21:47
 * @Description:
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";


    /**
     * 添加购物车
     *
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //获取登录用户
        UserInfo user = UserInfoContext.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //hashKey
        //记录num
        Integer num = cart.getNum();
        String hashKey= cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        //判断当前购物车商品是否存在
        if (operations.hasKey(hashKey)) {
            //存在，修改数量
            String json = operations.get(hashKey).toString();
            cart = JsonUtils.parseBean(json,Cart.class);
            cart.setNum(cart.getNum()+num);
        }
        //写回redis
        operations.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 获取用户购物车数据
     *
     * @return
     */
    @Override
    public List<Cart> queryCartList() {
        //获取登录用户
        UserInfo user = UserInfoContext.getUser();
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        return operations.values().stream().map(e -> JsonUtils.parseBean(e.toString(), Cart.class)).collect(Collectors.toList());
    }

    /**
     * 修改购物车数量
     *
     * @param skuId 商品id
     * @param num 修改到的数量
     */
    @Override
    public void updateCartNum(Long skuId, Integer num) {
        //获取登录用户
        UserInfo user = UserInfoContext.getUser();
        //Key
        String key = KEY_PREFIX + user.getId();
        //hashKey
        String hashKey = skuId.toString();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        //判断hashkey是否存在
        if (!operations.hasKey(hashKey)) {
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        Cart cart = JsonUtils.parseBean(operations.get(hashKey).toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operations.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 根据skuId删除购物车
     *
     * @param skuId
     */
    @Override
    public void deleteCart(Long skuId) {
        //获取登录用户
        UserInfo user = UserInfoContext.getUser();
        //Key
        String key = KEY_PREFIX + user.getId();
        //HashKey
        String hashKey = skuId.toString();
        //删除
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.delete(key,hashKey);
    }
}
