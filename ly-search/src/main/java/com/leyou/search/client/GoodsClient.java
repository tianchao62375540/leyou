package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:08
 * @Description:
 */
@FeignClient(name = "item-service")
public interface GoodsClient extends GoodsApi {
}
