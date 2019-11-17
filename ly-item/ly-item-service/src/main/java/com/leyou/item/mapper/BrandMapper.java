package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/10/29 21:20
 * @Description:
 */
public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid")Long cid,@Param("bid") Long bid);

    /**
     * 根据三级分类id查询品牌集合
     * @param cid
     * @return
     */
    @Select("select b.id,b.name,b.image,b.letter from tb_brand b inner join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}
