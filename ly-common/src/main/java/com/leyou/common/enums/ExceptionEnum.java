package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: tianchao
 * @Date: 2019/10/27 17:10
 * @Description:
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    SYSTEM_ERROR(500,"网络系统异常"),
    PRICE_CANNOT_BE_NULL(400,"价格不能为空！"),
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),
    GOODS_ID_CANNOT_NULL(400,"商品id不能为空"),
    BRAND_NOT_FOUND(404,"品牌没查到"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组不存在！"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在！"),
    GOODS_NOT_FOUND(404,"商品不存在！"),
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在！"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在！"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在！"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    GOODS_SAVE_ERROR(500,"新增商品失败"),
    GOODS_EDIT_ERROR(500,"修改商品失败！"),
    INVALID_FILE_TYPE(500,"无效的文件类型"),
    INVALID_USER_DATA_TYPE(400,"用户数据类型无效"),
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    INVALID_USERNAME_PASSWORD(400,"无效的用户名或密码"),
    CREATE_TOKEN_ERROR(500,"创建用户token信息失败"),
    UNAUTHORIZED(403,"未授权"),
    ;
    private int code;
    private String msg;


}
