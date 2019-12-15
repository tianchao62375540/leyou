package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        e.printStackTrace();
        log.error("[系统自定义运行时异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionResult> handlerFeignException(FeignException e){
        log.error("[通用feign异常]",e);
        return ResponseEntity.status(e.status()).body(new ExceptionResult(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> handleException(RuntimeException e){
        ExceptionEnum exceptionEnum = ExceptionEnum.SYSTEM_ERROR;
        e.printStackTrace();
        log.error("[系统运行时异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
    /**
     * 捕获@Validate校验抛出的异常
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResult> validExceptionHandler(BindException ex, HttpServletRequest request, HttpServletResponse response) {
        String msg = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("|"));
        log.error("[通用异常处理-BindException-{} -exception:{}]",msg,ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResult(ex));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResult> handleException(Exception e){
        ExceptionEnum exceptionEnum = ExceptionEnum.SYSTEM_ERROR;
        e.printStackTrace();
        log.error("[系统异常]");
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
}
