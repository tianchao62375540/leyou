package com.leyou.user.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: tianchao
 * @Date: 2019/12/12 22:38
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String KEY_PREFIX = "user:verify:phone:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 校验数据（注册）
     *
     * @param data
     * @param type 1用户名 2手机号
     * @return
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        //判断数据类型
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        return userMapper.selectCount(user) == 0;
    }

    /**
     * 发送短信
     *
     * @param phone 手机号
     */
    @Override
    public void sendPhone(String phone) {
        //生成key
        String key = KEY_PREFIX+phone;
        //生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
        //发送验证码
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",map);
        //保存验证码
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     *
     * @param user username password phone
     * @param code 验证码
     */
    @Override
    public void register(User user, String code) {
        //1校验验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code,cacheCode)) {
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        //2对密码加密
        //2.1生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //3写入数据库
        user.setCreated(new Date());
        user.setId(null);
        userMapper.insert(user);
    }
}
