package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @Auther: tianchao
 * @Date: 2019/10/27 17:19
 * @Description:
 */
@Data
public class ExceptionResult {
    private int status;
    private String msg;
    private Long timestamp;
    public ExceptionResult(ExceptionEnum em){
        this.status = em.getCode();
        this.msg = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
