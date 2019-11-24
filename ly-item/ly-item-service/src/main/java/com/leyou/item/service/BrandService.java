package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/10/29 21:21
 * @Description:
 */
@Service
public class BrandService  {

    @Autowired
    private BrandMapper brandMapper;
    /**
     * 查詢品牌列表
     * @param page 頁碼
     * @param rows 每頁條數
     * @param sortBy 是否排序
     * @param desc 聖墟降序
     * @param key 查詢條件
     * @return
     */
    public PageResult<Brand> queryBrandPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分頁
        PageHelper.startPage(page,rows);
        //過濾
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            //過濾條件
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByCause = sortBy + (desc?" DESC":" ASC");
            example.setOrderByClause(orderByCause);
        }
        //查詢
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分頁結果
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getList());
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insertSelective(brand);
        if(count != 1){
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增中间表
        for(Long cid : cids){
             count = brandMapper.insertCategoryBrand(cid, brand.getId());
             if(count != 1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
             }
        }
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * 根据cid查询品牌
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }

    /**
     * 根据id集合查询品牌
     * @param ids
     * @return
     */
    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
