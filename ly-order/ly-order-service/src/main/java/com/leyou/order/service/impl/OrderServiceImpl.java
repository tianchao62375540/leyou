package com.leyou.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.leyou.common.context.UserInfoContext;
import com.leyou.common.dto.CartDTO;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.item.pojo.Sku;
import com.leyou.order.client.AddressClient;
import com.leyou.order.client.GoodsClient;
import com.leyou.order.dto.AddressDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.enums.OrderStatusEnums;
import com.leyou.order.enums.PayState;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.service.OrderService;
import com.leyou.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: tianchao
 * @Date: 2019/12/17 20:21
 * @Description:
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private PayHelper payHelper;

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return 订单id
     */
    @Override
    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        //1新增订单
        Order order = new Order();
        //1.1订单编号
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());
        //1.2用户信息
        UserInfo userInfo = UserInfoContext.getUser();
        order.setUserId(userInfo.getId());
        order.setBuyerNick(userInfo.getUsername());
        order.setBuyerRate(false);
        //1.3收货人地址
        //获取收货人信息
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());
        //1.4金额
        @NotNull List<CartDTO> cartDTOS = orderDTO.getCarts();
        Map<Long, Integer> skuIdNumMap = cartDTOS.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        List<Sku> skus = goodsClient.querySkuBySkuIds(new ArrayList<>(skuIdNumMap.keySet()));
        long totalPay = 0;

        //准备orderDetail集合
        List<OrderDetail> details = new ArrayList<>();

        for (Sku sku : skus) {
            //计算总价
            totalPay+=sku.getPrice()*skuIdNumMap.get(sku.getId());
            //封装orderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            orderDetail.setNum(skuIdNumMap.get(sku.getId()));
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setOrderId(orderId);
            details.add(orderDetail);
        }
        order.setTotalPay(totalPay);
        //实付金额 = 总金额 + 邮费 - 实付金额
        order.setActualPay(totalPay + order.getPostFee() - 0);
        //1.5写入数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1){
            log.error("[订单服务] 创建订单失败 新增订单 orderId{}",orderId);
            new LyException(ExceptionEnum.CREATE_ORDER_EXCEPTION);
        }
        //2新增订单详情
        count = orderDetailMapper.insertList(details);
        if (count != details.size()){
            log.error("[订单服务] 创建订单失败 新增订单详情 orderId{}",orderId);
            new LyException(ExceptionEnum.CREATE_ORDER_EXCEPTION);
        }
        //3新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnums.UNPAY.value());
        orderStatusMapper.insertSelective(orderStatus);
        //减库存
        goodsClient.decrease(cartDTOS);
        return orderId;
    }

    /**
     * 根据id查询订单
     *
     * @param id
     * @return
     */
    @Override
    public Order queryOrderById(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null){
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = orderDetailMapper.select(detail);
        if (CollectionUtils.isEmpty(details)){
            throw new LyException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(details);
        //查询订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(id);
        if (orderStatus == null){
            throw new LyException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    /**
     * 生成预下单url
     *
     * @param orderId
     * @return
     */
    @Override
    public String createPayUrl(Long orderId) {
        Order order = queryOrderById(orderId);
        //订单状态
        Integer status = order.getOrderStatus().getStatus();
        if (status!=OrderStatusEnums.UNPAY.value()){
            throw new LyException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
        //支付金额order.getActualPay()
        @NotNull Long actualPay = 1L;
        //商品描述
        List<OrderDetail> orderDetails = order.getOrderDetails();
        String desc = orderDetails.get(0).getTitle();
        return payHelper.createOrder(orderId, actualPay, desc);
    }

    /**
     * h回调处理
     *
     * @param result
     */
    @Override
    public void handleNotify(Map<String, String> result) {
        payHelper.isSuccess(result);
        //校验签名
        payHelper.isValidSign(result);

        //校验金额
        String totalFeeStr = result.get("total_fee");
        String tradeNo = result.get("out_trade_no");
        if (StringUtils.isEmpty(totalFeeStr)){
            throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        Long totalFee = Long.valueOf(totalFeeStr);
        Long orderId = Long.valueOf(tradeNo);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (totalFee != /*order.getActualPay()*/1){
            throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
        }

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(OrderStatusEnums.PAYED.value());
        orderStatus.setOrderId(orderId);
        orderStatus.setPaymentTime(new Date());
        int count = orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
        if (count!=1){
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        log.info("[订单回调] 微信支付回调成功，{}",orderId);
    }

    @Override
    public PayState queryOrderState(Long orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        if (status!=OrderStatusEnums.UNPAY.value()){
            return PayState.SUCCESS;
        }
        //如果是未支付，但其实不一定是未支付,必须去微信查询支付
        return payHelper.queryPayState(orderId);
    }
}
