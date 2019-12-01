package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:21
 * @Description:
 */
public interface SpecificationApi {
    @GetMapping("spec/params")
    public List<SpecParam> querySpecParamList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching);

    /**
     * 商品分叉查询规格组 包括下面的规格属性
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    List<SpecGroup> queryGroupsByid(@RequestParam("cid") Long cid);
}
