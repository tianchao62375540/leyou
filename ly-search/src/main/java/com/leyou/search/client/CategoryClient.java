package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/11/19 23:41
 * @Description:
 */
@FeignClient(value="item-service")
public interface CategoryClient extends CategoryApi {


}
