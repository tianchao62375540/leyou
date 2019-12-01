package com.leyou.page.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:18
 * @Description:
 */
@FeignClient(value="item-service")
public interface BrandClient extends BrandApi {
}
