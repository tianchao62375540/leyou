package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

import java.sql.DriverManager;

/**
 * @Auther: tianchao
 * @Date: 2019/12/16 23:15
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.leyou.order.mapper")
public class LyOrderApplication {
    public static void main(String[] args) {
        DriverManager.getConnection()
        SpringApplication.run(LyOrderApplication.class,args);
    }
}
