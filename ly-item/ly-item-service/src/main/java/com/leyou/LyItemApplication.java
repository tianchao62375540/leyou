package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 商品微服务的服务提供者
 * @Auther: tianchao
 * @Date: 2019/10/27 13:56
 * @Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value={"com.leyou.item.mapper"})
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class,args);
    }
}
