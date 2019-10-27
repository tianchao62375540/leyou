package com.leyou.item.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: tianchao
 * @Date: 2019/10/27 16:26
 * @Description:
 */
@Data
@Accessors(chain = true)
public class Item {
    private Integer id;
    private String name;
    private Long price;
}
