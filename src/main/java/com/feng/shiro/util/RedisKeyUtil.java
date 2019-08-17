package com.feng.shiro.util;

import com.feng.shiro.constant.RedisConstant;
import com.feng.shiro.repository.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @Description redis key设计
 * @Author chenlinghong
 * @Date 2019/4/5 13:35
 * @Version V1.0
 */
@Service
public class RedisKeyUtil<T> {

    /**
     * 生成redis key
     * redisKey = {nais}:{className}:{caseNumber}   比如：nais:accident:cdx123
     *
     * @param caseNumber
     * @return
     */
    public String naisAccidentKey(String caseNumber) {
        StringBuffer redisKey = new StringBuffer("nais");
        redisKey.append(RedisConstant.SEPARATOR);
        redisKey.append("accident");
        redisKey.append(RedisConstant.SEPARATOR);
        redisKey.append(caseNumber);
        return redisKey.toString();
    }



    /**
     * 生成redis key，通过domain对象
     * redisKey = {className}:{id}
     *
     * @param data
     * @return
     */
    public String generateKey(T data) {
        if (data == null) {
            return null;
        }
        String className = getSimpleNameForDomain(data);
        if (className == null) {
            return null;
        }
        StringBuffer redisKey = new StringBuffer(className);
        redisKey.append(RedisConstant.SEPARATOR);
        // 将data强转为BaseDomain
        BaseDomain dataDomain = (BaseDomain) data;
        redisKey.append(dataDomain.getId());
        return redisKey.toString();
    }

    /**
     * 生成redis key，通过domain对象
     * redisKey = {className}:{telephone}
     *
     * @param data
     * @param telephone
     * @return
     */
    public String generateKey(T data, String telephone) {
        if (data == null || StringUtils.isBlank(telephone)) {
            return null;
        }
        String className = getSimpleNameForDomain(data);
        if (className == null) {
            return null;
        }
        StringBuffer redisKey = new StringBuffer(className);
        redisKey.append(RedisConstant.SEPARATOR).append(telephone);
        return redisKey.toString();
    }


    /**
     * 生成redis key。主要针对电话号码进行存储短信验证码
     * redisKey = telephone:{telephone}
     *
     * @param telephone
     * @return
     */
    public String generateKeyForSms(String telephone) {
        StringBuffer redisKey = new StringBuffer();
        redisKey.append(RedisConstant.TELEPHONE).append(RedisConstant.SEPARATOR).append(telephone);
        return redisKey.toString();
    }

    /**
     * 获取domain对象的类名
     *
     * @param data
     * @return
     */
    private String getSimpleNameForDomain(T data) {
        // data 是 domain领域对象
        if (data instanceof BaseDomain) {
            /**
             * 通过反射，获取到data的类名，实际key的设计为：className:id
             */
            Class clazz = data.getClass();
            String className = clazz.getSimpleName();
            return className;
        }
        /**
         * 暂不提供其它类的redis key设计
         */
        return null;
    }

}
