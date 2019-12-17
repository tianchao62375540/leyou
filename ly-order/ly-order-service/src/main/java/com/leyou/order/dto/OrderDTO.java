package com.leyou.order.dto;

import com.leyou.common.dto.CartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:10
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    /**
     * 收货人地址
     */
    @NotNull
    private Long addressId;
    /**
     * 付款类型
    */
    private Integer paymentType;
    /**
     * 订单详情
     */
    @NotNull
    private List<CartDTO> carts;

}
