package com.leyou.cart.web;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 21:41
 * @Description:
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cart cart对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCartList(){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.queryCartList());
    }
}
