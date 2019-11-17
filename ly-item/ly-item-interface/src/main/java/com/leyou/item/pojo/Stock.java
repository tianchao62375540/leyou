package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 库存
 * @Auther: tianchao
 * @Date: 2019/11/10 13:51
 * @Description:
 */
@Data
@Table(name="tb_stock")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Stock {
    @Id
    private Long skuId;
    /**
     * 可秒杀库存
     */
    private Integer seckillStock;
    /**
     * 秒杀总数量
     */
    private Integer seckillTotal;
    /**
     * 库存数量
     */
    private Integer stock;
}
