package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 22:38
 * @Description:
 */
public interface UserService {
    /**
     * 校验数据（注册）
     * @param data
     * @param type 1用户名 2电话号
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送短信
     * @param phone 手机号
     */
    void sendPhone(String phone);

    /**
     * 用户注册
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUserByUsernameAndPassword(String username, String password);
}
