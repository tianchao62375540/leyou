package com.leyou.search.pojo;

import lombok.Data;

import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/11/24 13:15
 * @Description:
 */
public class SearchRequest {
    //搜索字段
    private String key;
    /**
     * 当前页
     */
    private Integer page;

    /**
     * 过滤项
     */
    private Map<String,String> filter;

    private static final int DEFAULT_ROWS = 20;

    private static final int DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(page,DEFAULT_PAGE);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getSize(){
        return DEFAULT_ROWS;
    }
}
