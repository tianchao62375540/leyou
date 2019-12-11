package com.leyou.page.mq;

import com.leyou.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: tianchao
 * @Date: 2019/12/4 20:52
 * @Description:
 */
@Component
@Slf4j
public class ItemListener {

    @Autowired
    private PageService pageService;

    /**
     * 监听商品的新增和修改
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.insert.queue",durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId){
        if (spuId == null){
            log.error("[静态页微服务]-新增和修改商品监听，spuId 为NULL");
            return;
        }
        log.info("[静态页微服务]-新增和修改商品监听，spuId " + spuId);
        //处理消息
        pageService.createGoodsHtml(spuId);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "page.item.delete.queue",durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if (spuId == null){
            log.error("[静态页微服务]-删除商品监听，spuId 为NULL");
            return;
        }
        log.info("[静态页微服务]-删除商品监听，spuId " + spuId);
        //处理消息
        pageService.deleteHtml(spuId);
    }

}
