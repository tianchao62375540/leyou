package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.entity.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: tianchao
 * @Date: 2019/12/14 22:54
 * @Description:
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    JwtProperties properties;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String token = authService.accredit(username,password);
        if (StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getExpire()*60);
        return ResponseEntity.ok(null);
    }

    /**
     * 校验用户名登录
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verity(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        if (StringUtils.isBlank(token)){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        //解析token
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            //刷新token
            String newToken = JwtUtils.generateToken(userInfo,properties.getPrivateKey(),properties.getExpire());
            //写会cookie
            CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getExpire()*60);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            log.error("[鉴权服务] token解析失败");
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }

}
