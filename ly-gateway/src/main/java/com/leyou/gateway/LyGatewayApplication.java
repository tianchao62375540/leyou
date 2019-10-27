package com.leyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Auther: tianchao
 * @Date: 2019/10/27 12:59
 * @Description:
 */
//zuul
@EnableZuulProxy
@SpringCloudApplication
/**
 * @SpringBootApplication boot应用
 * @EnableDiscoveryClient 服务发现
 * @EnableCircuitBreaker 服务熔断
 */
public class LyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGatewayApplication.class,args);
    }
}
