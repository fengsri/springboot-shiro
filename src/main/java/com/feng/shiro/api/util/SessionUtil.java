package com.feng.shiro.api.util;

import com.feng.shiro.constant.SessionConstant;
import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description session 工具类
 * @Author chenlinghong
 * @Date 2019/4/22 9:42
 * @Version V1.0
 */
@Slf4j
@Service
public class SessionUtil {


    /**
     * 校验图形验证码
     * @param imageCode
     * @param request
     * @return
     */
    public static boolean checkImageCode(String imageCode, HttpServletRequest request) {
        if (StringUtils.isBlank(imageCode)) {
            log.error("SessionUtil#checkImageCode: param is null. imageCode={}, request={}", imageCode, request);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        String sessionImageCode = getImageCode(request);
        if (imageCode.equalsIgnoreCase(sessionImageCode)){
            return true;
        }
        return false;
    }

    /**
     * 获取图像验证码
     * @param request
     * @return
     */
    public static String getImageCode(HttpServletRequest request){
        HttpSession session = request.getSession();
        return (String) session.getAttribute(SessionConstant.IMAGE_CODE);
    }
}
