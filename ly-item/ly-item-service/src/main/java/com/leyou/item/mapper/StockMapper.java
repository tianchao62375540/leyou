package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: tianchao
 * @Date: 2019/11/10 14:00
 * @Description:
 */
public interface StockMapper  extends Mapper<Stock>, InsertListMapper<Stock>, IdListMapper<Stock,Long> {
}
