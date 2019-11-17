package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 商品spu
 *
 * @Auther: tianchao
 * @Date: 2019/11/3 21:47
 * @Description:
 */
@Data
@ToString
@Accessors(chain = true)
@Table(name = "tb_spu")
public class Spu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private String title;//标题
    private String subTitle;//子标题
    private Boolean saleable; //上下架
    //@JsonIgnore
    private Boolean valid;//是否有效 逻辑删除
    private Date createTime;
    //@JsonIgnore
    private Date lastUpdateTime;

    //以下是扩展属性
    /**
     * 下面两个是管理spu的时候的品牌名字和分类名字
     */
    @Transient
    private String cname;
    @Transient
    private String bname;

    /**
     *下面两个是商品新增用的
     */
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;
}
