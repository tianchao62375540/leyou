package com.leyou.order.service;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.enums.PayState;
import com.leyou.order.pojo.Order;

import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:18
 * @Description:
 */
public interface OrderService {
    /**
     * 创建订单
     * @param orderDTO
     * @return 订单id
     */
    Long createOrder(OrderDTO orderDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    Order queryOrderById(Long id);

    /**
     * 生成预下单url
     * @param orderId
     * @return
     */
    String createPayUrl(Long orderId);

    /**
     * h回调处理
     * @param result
     */
    void handleNotify(Map<String, String> result);

    PayState queryOrderState(Long orderId);
}
