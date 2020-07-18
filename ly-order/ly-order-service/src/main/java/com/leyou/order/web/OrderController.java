package com.leyou.order.web;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:15
 * @Description:
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
        //创建订单
        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(orderDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.queryOrderById(id));
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<String> createOrder(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.createPayUrl(orderId));
    }
    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderState(orderId).getValue());
    }
}
