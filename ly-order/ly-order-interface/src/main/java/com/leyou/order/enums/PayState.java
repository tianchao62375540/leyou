package com.leyou.order.enums;

/**
 * @Auther: tianchao
 * @Date: 2020/3/15 18:23
 * @Description:
 */
public enum PayState {
    NOT_PAY(0),
    SUCCESS(1),
    FAIL(2),
    ;
    PayState(int value){
        this.value = value;
    }
    int value;
    public int getValue(){
        return value;
    }
}
