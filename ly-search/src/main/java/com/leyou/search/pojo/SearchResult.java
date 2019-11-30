package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/11/24 19:38
 * @Description:
 */
@Data
public class SearchResult extends PageResult {
    /**
     * 分类集合
     */
    private List<Category> categories;
    /**
     * 品牌集合
     */
    private List<Brand> brands;
    /**
     * 过滤项
     */
    private Map<String,String> filter;


    private List<Map<String,Object>> specs;
    public SearchResult(){
    }

    public SearchResult(Long total,
                        Integer totalPage,
                        List<Goods> items,
                        List<Category> categories,
                        List<Brand> brands,
                        List<Map<String,Object>> specs){
        super(total,totalPage,items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
