package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
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

    @Update("UPDATE tb_stock SET stock = stock - #{num} where sku_id = #{id} AND stock >= #{num}")
    int decreaseStock(@Param("id") Long id,@Param("num") Integer num);
}
