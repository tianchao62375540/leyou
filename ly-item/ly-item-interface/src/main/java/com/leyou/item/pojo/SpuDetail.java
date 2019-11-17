package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: tianchao
 * @Date: 2019/11/3 21:52
 * @Description:
 */
@Data
@Accessors(chain = true)
@ToString
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;
    //商品描述
    private String description;
    //通用规格参数数据
    private String genericSpec;
    //特有规格参数及可选值信息，json格式
    private String specialSpec;
    //包装清单
    private String packingList;
    //售后服务
    private String afterService;
}
