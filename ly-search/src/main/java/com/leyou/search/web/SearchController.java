package com.leyou.search.web;

import com.leyou.common.vo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/**
 * @Auther: tianchao
 * @Date: 2019/11/24 13:24
 * @Description:
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;
    /**
     * 搜素
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(searchService.search(searchRequest));
    }
}
