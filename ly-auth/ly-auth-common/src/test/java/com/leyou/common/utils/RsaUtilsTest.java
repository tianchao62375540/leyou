package com.leyou.common.utils;

import com.leyou.common.entity.UserInfo;
import io.jsonwebtoken.Jwt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * @Auther: tianchao
 * @Date: 2019/12/14 22:16
 * @Description:
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class RsaUtilsTest {

    private static final String pubKeyPath = "G:\\leyou-server-zongjie\\rsa\\rsa.pub";
    private static final String priKeyPath = "G:\\leyou-server-zongjie\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void getPublicKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"234");
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(15L, "田超"), privateKey, 50);
        System.out.println("token:"+token);
        //eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTUsInVzZXJuYW1lIjoi55Sw6LaFIiwiZXhwIjoxNTgwNjUzNzI4fQ.glquHCoZTyBb5-_ExOaj7DUMT0PfXuwifZNXhWzreMP2PTmNVey3dpkGrFsdmrKFDEn9euf7pqM5vPiBgJWswOzHwkbqmu7MHznfvBEBjbRvIXVQ4SKLY3q3pnKDOVmvaNsRVn7W4Ry9sU-G6lvmlgYCJWqzx9588ioe6yEuywI
    }
    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTUsInVzZXJuYW1lIjoi55Sw6LaFIiwiZXhwIjoxNTgwNjUzNzI4fQ.glquHCoZTyBb5-_ExOaj7DUMT0PfXuwifZNXhWzreMP2PTmNVey3dpkGrFsdmrKFDEn9euf7pqM5vPiBgJWswOzHwkbqmu7MHznfvBEBjbRvIXVQ4SKLY3q3pnKDOVmvaNsRVn7W4Ry9sU-G6lvmlgYCJWqzx9588ioe6yEuywI";
        UserInfo userInfo = JwtUtils.getInfoFromToken(token,publicKey);
        System.out.println(userInfo);
    }
}