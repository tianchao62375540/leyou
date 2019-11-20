package com.leyou.search.client;

import com.leyou.item.pojo.SpuDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:09
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsClientTest {
    @Autowired
    private GoodsClient goodsClient;
    @Test
    public void test(){
        SpuDetail spuDetailResponseEntity = goodsClient.queryDetailById(2L);
        System.out.println(spuDetailResponseEntity);
    }
}