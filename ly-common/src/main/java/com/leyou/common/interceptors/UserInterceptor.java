package com.leyou.common.interceptors;

import com.leyou.common.config.JwtProperties;
import com.leyou.common.context.UserInfoContext;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: tianchao
 * @Date: 2019/12/15 21:09
 * @Description:
 */

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @NonNull
    private JwtProperties jwtProperties;

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        try {
            //解析userInfo
            UserInfo userinfo = JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
            //传递用户
            UserInfoContext.setUser(userinfo);
        }catch (Exception e){
            log.error("[购物车服务] 解析用户身份失败.",e);
            return false;
        }
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfoContext.removeUser();
    }


}
