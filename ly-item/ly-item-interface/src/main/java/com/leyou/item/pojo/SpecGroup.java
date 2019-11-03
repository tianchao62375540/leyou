package com.leyou.item.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: tianchao
 * @Date: 2019/11/3 13:07
 * @Description:
 */
@Data
@Accessors(chain = true)
@Table(name="tb_spec_group")
@ToString
public class SpecGroup {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long cid;

    private String name;
}
