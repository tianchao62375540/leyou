package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Auther: tianchao
 * @Date: 2019/11/10 14:21
 * @Description:
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, InsertListMapper<T>, IdListMapper<T,Long> {
}
