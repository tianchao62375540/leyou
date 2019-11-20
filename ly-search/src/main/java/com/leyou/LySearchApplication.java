package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: tianchao
 * @Date: 2019/11/17 22:36
 * @Description:
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class,args);
    }
}
