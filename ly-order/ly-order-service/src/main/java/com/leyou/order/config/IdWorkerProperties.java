package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:51
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {
    /**
     * 当前机器id
     */
    private long workerId;
    /**
     * 序列号
     */
    private long dataCenterId;
}
