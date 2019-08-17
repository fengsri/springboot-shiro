package com.feng.shiro.util;

import com.feng.shiro.common.redis.RedisUtil;
import com.feng.shiro.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description 该项目专用redis 工具类
 * @Author chenlinghong
 * @Date 2019/4/6 17:35
 * @Version V1.0
 */
@Slf4j
@Service
public class MyRedisUtil extends RedisUtil {

    @Autowired
    private RedisKeyUtil redisKeyUtil;


    /**
     * 短信验证码写入redis
     *
     * @param telephone 电话号码
     * @param smsCode   短信验证码
     * @return
     */
    @Async("asyncServiceExecutor")
    public String putSmsCode(String telephone, String smsCode) {
        log.info("MyRedisUtil#putSmsCode: beginning. telephone={}, smsCode={}", telephone, smsCode);
        // 生成redis key
        String redisKey = redisKeyUtil.generateKeyForSms(telephone);
        // 写入redis
        set(redisKey, smsCode);
        // 设置key 存活时间   10 * 60s
        expire(redisKey, RedisConstant.SMS_TTL, TimeUnit.MILLISECONDS);
        log.info("MyRedisUtil#putSmsCode: ended. telephone={}, smsCode={}", telephone, smsCode);
        return redisKey;
    }

    /**
     * 获取短信验证码
     *
     * @param telephone
     * @return
     */
    public String getSmsCode(String telephone) {
        // 生成redis key
        String redisKey = redisKeyUtil.generateKeyForSms(telephone);
        // 获取短信验证码
        return get(redisKey);
    }

}
