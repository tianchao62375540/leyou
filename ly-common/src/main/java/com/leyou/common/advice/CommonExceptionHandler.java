package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

/**
 * 通用异常处理器
 * @Auther: tianchao
 * @Date: 2019/10/27 16:55
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        log.error("[系统自定义运行时异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> handleException(RuntimeException e){
        ExceptionEnum exceptionEnum = ExceptionEnum.SYSTEM_ERROR;
        log.error("[系统运行时异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResult> handleException(Exception e){
        ExceptionEnum exceptionEnum = ExceptionEnum.SYSTEM_ERROR;
        log.error("[系统异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
}
