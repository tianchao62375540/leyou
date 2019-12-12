package com.leyou.sms.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 19:31
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsListenerTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void listenInsertOrUpdate() {
        Map<String,String> map = new HashMap<>();
        map.put("phone","18940295748");
        map.put("code","1523");
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",map);
    }
}