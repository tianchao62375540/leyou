package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 22:25
 * @Description:
 */
@Table(name = "tb_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 4,max = 32,message = "用户名长度必须在4~32位")
    private String username;
    /**
     * 密码
     */
    @JsonIgnore
    @Length(min = 4,max = 32,message = "密码长度必须在4~32位")
    private String password;//
    /**
     * 电话
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$",
        message = "手机号不正确")
    private String phone;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;
}
