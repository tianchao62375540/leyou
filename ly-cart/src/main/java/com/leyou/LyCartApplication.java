package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 20:50
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LyCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyCartApplication.class,args);
    }
}
