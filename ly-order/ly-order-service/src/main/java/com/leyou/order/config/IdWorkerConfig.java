package com.leyou.order.config;

import com.leyou.common.utils.IdWorker;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:54
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {
    /**
     * 注册idWorker
     * @param prop
     * @return
     */
    @Bean
    public IdWorker idWorker(IdWorkerProperties prop){
        return new IdWorker(prop.getWorkerId(),prop.getDataCenterId());
    }

}
