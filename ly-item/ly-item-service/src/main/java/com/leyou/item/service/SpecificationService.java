package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 规格组和规格参数service
 *
 * @Auther: tianchao
 * @Date: 2019/11/3 13:09
 * @Description:
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询规格组列表
     * @param cid 分类id
     * @return 规格组集合
     */
    public List<SpecGroup> queryGroupByCid(Long cid) {
        List<SpecGroup> list = specGroupMapper.select(new SpecGroup().setCid(cid));
        if(CollectionUtils.isEmpty(list)){
            //没查到
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    /**
     * 新增规格组
     * @param specGroup
     */
    public void saveSpecGroup(SpecGroup specGroup) {
        specGroup.setId(null);
        specGroupMapper.insertSelective(specGroup);
    }

    /**
     * 修改规格组
     * @param specGroup
     */
    public void editSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 根据id删除规格组
     * @param id
     */
    public void deleteSpecGroup(Long id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询指定规格组下面的规格参数集合
     * 或者查询指定分类下的规格参数集合
     * 或者是否搜索查询规格参数集合
     * @param gid 规格组id
     * @param cid 分类id
     * @param searching 是否搜索
     * @return
     */
    public List<SpecParam> querySpecParamByGid(Long gid, Long cid, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid)
                .setSearching(searching)
                .setCid(cid);
        List<SpecParam> list = specParamMapper.select(param);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    /**
     * 添加规格参数
     * @param specParam
     * @return
     */
    public void saveSpecParam(SpecParam specParam) {
        specParamMapper.insertSelective(specParam);
    }

    /**
     * 修改规格参数
     * @param specParam
     */
    public void editSpecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    /**
     *根据id删除规格参数
     * @param id
     */
    public void deleteSpecParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }
}
