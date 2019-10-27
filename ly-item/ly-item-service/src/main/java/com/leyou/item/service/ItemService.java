package com.leyou.item.service;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @Auther: tianchao
 * @Date: 2019/10/27 16:29
 * @Description:
 */
@Service
public class ItemService {

    public Item saveItem(Item item){
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
