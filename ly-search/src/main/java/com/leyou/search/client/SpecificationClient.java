package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:22
 * @Description:
 */
@FeignClient(name = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
