package com.leyou.sms.mq;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/11 23:23
 * @Description:
 */
@Component
@Slf4j
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsProperties prop;

    @Autowired
    private SmsUtils smsUtils;

    /**
     * 发送短信验证码的监听
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms.verify.code.queue",durable = "true"),
            exchange = @Exchange(name = "ly.sms.exchange",type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}
    ))
    public void listenInsertOrUpdate(Map<String,String> msg){
        if (CollectionUtils.isEmpty(msg)){
            log.error("[短信服务] 发送验证码监听收到的消息为null");
            return;
        }
        String phone = msg.remove("phone");
        if(StringUtils.isBlank(phone)){
            log.error("[短信服务] 发送验证码监听收到的消息的电话号为null");
            return;
        }
        //处理消息
        smsUtils.sendSms(phone,prop.getSignName(),prop.getVerifyCodeTemplate(), JsonUtils.serialize(msg));
    }
}
