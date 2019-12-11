package com.leyou.sms.utils;


import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: tianchao
 * @Date: 2019/12/11 23:19
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SmsUtilsTest {

    @Autowired
    private SmsUtils smsUtils;

    @org.junit.Test
    public void sendSms() throws ClientException {
        smsUtils.sendSms("18940295748","ly商城","SMS_180058287","{\"code\":\"123\"}");
    }
    @Test
    public void testLog(){
        log.error("我是大神{},原因{},wawa{}","tianchao","牛逼","haha");
    }
}