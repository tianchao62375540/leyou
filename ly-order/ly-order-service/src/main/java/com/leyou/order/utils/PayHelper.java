package com.leyou.order.utils;

import com.github.wxpay.sdk.WXPay;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import com.leyou.order.enums.OrderStatusEnums;
import com.leyou.order.enums.PayState;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.wxpay.sdk.WXPayConstants.*;

/**
 * @Auther: tianchao
 * @Date: 2020/3/4 23:23
 * @Description:
 */
@Component
@Slf4j
public class PayHelper {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    public String createOrder(Long orderId,Long totalPay,String desc){
        Map<String,String> data = new HashMap<>();
        //商品描述
        data.put("body", desc);
        //订单号
        data.put("out_trade_no", orderId.toString());
        //金额 单位是分
        data.put("total_fee", totalPay.toString());
        //调用微信支付终端ip
        data.put("spbill_create_ip", "127.0.0.1");
        //回调地址
        data.put("notify_url",payConfig.getNotifyUrl());
        //交易类型为扫码支付
        data.put("trade_type", "NATIVE");
        Map<String,String> result;
        try {
            result = wxPay.unifiedOrder(data);

            for (Map.Entry<String, String> entry : result.entrySet()) {
                String key = entry.getKey();
                System.out.println(key+(key.length()>=8?"\t:":"\t\t: ") + entry.getValue());
            }
            isSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[微信支付]创建预交易订单异常失败",e);
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
        //下单成功
        return result.get("code_url");
    }

    public void isSuccess(Map<String, String> result) {
        //判断通信标识
        String return_code = result.get("return_code");
        if (FAIL.equals(return_code)){
            log.error("[微信下单] 微信下单通讯失败 失败原因{}",result.get("return_msg"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
        //判断业务标识
        String resultCode = result.get("result_code");
        if (FAIL.equals(resultCode)){
            log.error("[微信下单] 微信下单业务失败 错误码{}，失败原因{}",result.get("err_code"),result.get("err_code_des"));
            throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }


    public void isValidSign(Map<String, String> result) {
        try {
            String sign1 = WXPayUtil.generateSignature(result, payConfig.getKey(), SignType.HMACSHA256);
            String sign2 = WXPayUtil.generateSignature(result, payConfig.getKey(), SignType.HMACSHA256);
            String sign = result.get("sign");
            if (!StringUtils.equals(sign, sign1)&&!StringUtils.equals(sign, sign2)) {
                throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
            }
        }catch (Exception e){
            throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
        }

    }

    public PayState queryPayState(Long orderId) {
        try {
            Map<String,String> data = new HashMap<>();
            data.put("out_trade_no", orderId.toString());
            Map<String, String> result = wxPay.orderQuery(data);
            //校验状态
            isSuccess(result);
            //校验签名
            isValidSign(result);
            //校验金额
            //校验金额
            String totalFeeStr = result.get("total_fee");
            String tradeNo = result.get("out_trade_no");
            if (org.apache.commons.lang.StringUtils.isEmpty(totalFeeStr)){
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            Long totalFee = Long.valueOf(totalFeeStr);
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (totalFee != /*order.getActualPay()*/1){
                throw new LyException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            String state = result.get("trade_state");
            /**
             * SUCCESS—支付成功
             *
             * REFUND—转入退款
             *
             * NOTPAY—未支付
             *
             * CLOSED—已关闭
             *
             * REVOKED—已撤销（付款码支付）
             *
             * USERPAYING--用户支付中（付款码支付）
             *
             * PAYERROR--支付失败(其他原因，如银行返回失败)
             *
             * 支付状态机请见下单API页面
             */
            if (SUCCESS.equals(state)){
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setStatus(OrderStatusEnums.PAYED.value());
                orderStatus.setOrderId(orderId);
                orderStatus.setPaymentTime(new Date());
                int count = orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
                if (count!=1){
                    throw new LyException(ExceptionEnum.CART_NOT_FOUND);
                }
                return PayState.SUCCESS;
            }
            if ("NOTPAY".equals(state)||"USERPAYING".equals(state)){
                return PayState.NOT_PAY;
            }
            return PayState.FAIL;


        }catch (Exception e){
            return PayState.NOT_PAY;
        }


    }
}
