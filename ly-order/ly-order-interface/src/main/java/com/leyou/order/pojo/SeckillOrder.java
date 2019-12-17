package com.leyou.order.pojo;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 19:44
 * @Description:秒杀订单
 */
//@Table(name = "tb_seckill_order")
public class SeckillOrder {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long skuId;

    private Long orderId;

}
