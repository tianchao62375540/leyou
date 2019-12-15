package com.leyou.auth.service;

/**
 * @Auther: tianchao
 * @Date: 2019/12/14 22:56
 * @Description:
 */
public interface AuthService {
    /**
     * 授权
     * @param username
     * @param password
     * @return
     */
    String accredit(String username, String password);
}
