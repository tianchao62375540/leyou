package com.leyou.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: tianchao
 * @Date: 2020/3/4 23:19
 * @Description:
 */
@Configuration
public class WXPAYConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ly.pay")
    public PayConfig payConfig(){
        return new PayConfig();
    }

    @Bean
    public WXPay wxPay(PayConfig payConfig){
        return new WXPay(payConfig, WXPayConstants.SignType.HMACSHA256);
    }
}
