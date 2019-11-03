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
    BRAND_NOT_FOUND(404,"品牌没查到"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组不存在！"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在！"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(500,"无效的文件类型"),

    ;
    private int code;
    private String msg;


}
