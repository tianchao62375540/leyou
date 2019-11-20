package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/11/19 23:50
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {
    @Autowired
    private CategoryClient categoryClient;
    @Test
    public void queryCategoryByIds(){
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(2L,3L));
        Assert.assertEquals(2,categories.size());
        categories.stream().forEach(System.out::println);
    }
}