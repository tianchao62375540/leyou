package com.leyou.common.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import feign.Feign;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 12:22
 * @Description:
 */
@Configuration
@Slf4j
public class MyErrDecoder extends ErrorDecoder.Default{

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                Map<String, String> stringStringMap = JsonUtils.parseMap(Util.toString(response.body().asReader()), String.class
                        , String.class);
                if ("400".equals(stringStringMap.get("status"))){
                   /* Class clazz = Class.forName(exceptionInfo.getException());
                    return (Exception) clazz.getDeclaredConstructor(String.class).newInstance(exceptionInfo.getMessage());*/
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.decode(methodKey, response);
    }

    @Data
    public class ExceptionInfo extends Exception{

        private Long timestamp;

        private Integer status;
        //异常包结构-"com.crazy.cloud.common.exception.DataConflictException"
        private String exception;

        private String path;

        private String error;
    }
}
