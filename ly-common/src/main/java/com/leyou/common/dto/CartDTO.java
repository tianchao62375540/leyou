package com.leyou.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:12
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 购买数量
     */
    private Integer num;
}
