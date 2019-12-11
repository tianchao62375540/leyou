package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: tianchao
 * @Date: 2019/12/11 23:06
 * @Description:
 */
@ConfigurationProperties(prefix = "ly.sms")
@Data
public class SmsProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String verifyCodeTemplate;
}
