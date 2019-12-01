package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: tianchao
 * @Date: 2019/11/19 23:41
 * @Description:
 */
@FeignClient(value="item-service")
public interface CategoryClient extends CategoryApi {


}
