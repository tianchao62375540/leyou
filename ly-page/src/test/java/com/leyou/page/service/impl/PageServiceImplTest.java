package com.leyou.page.service.impl;

import com.leyou.page.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 13:08
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PageServiceImplTest {
    @Autowired
    private PageService pageService;
    @Test
    public void loadModel() {
    }

    @Test
    public void createGoodsHtml() {
        pageService.createGoodsHtml(141L);
    }
}