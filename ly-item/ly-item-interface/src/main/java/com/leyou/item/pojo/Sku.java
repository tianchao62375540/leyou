package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Auther: tianchao
 * @Date: 2019/11/10 13:43
 * @Description:
 */

@Table(name="tb_sku")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    /**
     * 商品特殊规格的键值对
     */
    private String ownSpec;
    /**
     * 商品特殊规格的下表
     */
    private String indexes;
    /**
     * 是否有效，逻辑删除用
     */
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;


    /**
     * 库存
     */
    @Transient
    private Integer stock;


}
