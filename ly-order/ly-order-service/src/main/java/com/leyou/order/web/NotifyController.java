package com.leyou.order.web;

import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2020/3/5 22:48
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "notify",produces = "application/xml")
public class NotifyController {

    @Autowired
    private OrderService orderService;

    public Map<String,String> pay(@RequestBody Map<String,String> result){
        orderService.handleNotify(result);
        log.info("[支付回调] 接收微信支付回调,结果{}",result);
        Map<String,String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        return msg;
    }


}
