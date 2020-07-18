package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

/**
 * @Auther: tianchao
 * @Date: 2020/3/4 23:07
 * @Description:
 */
@Data
public class PayConfig implements WXPayConfig {
    /**
     * 公众账号id
     */
    private String appID;
    /**
     *商户号id
     */
    private String mchID;
    /**
     * 生成签名的秘钥
     */
    private String key;
    /**
     * 连接超时时长
     */
    private int httpConnectTimeoutMs;
    /**
     * 读取超时时长
     */
    private int HttpReadTimeoutMs;

    /**
     * 回调地址
     */
    private String notifyUrl;


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    @Override
    public InputStream getCertStream() {
        return null;
    }
}
