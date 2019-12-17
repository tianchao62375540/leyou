package com.leyou.order.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 21:15
 * @Description:
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
