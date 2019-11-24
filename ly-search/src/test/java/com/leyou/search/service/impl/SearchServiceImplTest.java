package com.leyou.search.service.impl;

import com.leyou.common.vo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/11/24 14:49
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceImplTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void buildGoods() {
    }

    @Test
    public void search() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKey("手机");
        PageResult<Goods> search = searchService.search(searchRequest);
        System.out.println(search);

    }
}