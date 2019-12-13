package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

import java.util.stream.Collectors;

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
    public ExceptionResult(BindException ex){
        this.status = HttpStatus.BAD_REQUEST.value();
        this.msg = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("|"));
        this.timestamp = System.currentTimeMillis();
    }
}
