package com.leyou.order.enums;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 21:37
 * @Description:
 */
public enum OrderStatusEnums {

    UNPAY(1,"未付款"),
    PAY(2,"已付款,未发货"),
    DELIVERED(3,"已发货,未确认"),
    SUCCESS(4,"已确认,未评价"),
    CLOSED(5,"交易失败,已关闭"),
    RATED(6,"已评价"),




    ;
    private int code;
    private String desc;

    OrderStatusEnums(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int value(){
        return this.code;
    }
}
