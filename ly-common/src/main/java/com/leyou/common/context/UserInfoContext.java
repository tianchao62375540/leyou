package com.leyou.common.context;

import com.leyou.common.entity.UserInfo;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 21:49
 * @Description:
 */

public class UserInfoContext {

    private static final ThreadLocal<UserInfo> userContext = new ThreadLocal<>();

    private UserInfoContext(){

    }

    /**
     * 设置userInfo
     * @param userInfo
     */
    public static void setUser(UserInfo userInfo){
        userContext.set(userInfo);
    }

    /**
     * 获取userInfo
     * @return
     */
    public static UserInfo getUser(){
        return userContext.get();
    }

    public static void removeUser(){
        userContext.remove();
    }
}
