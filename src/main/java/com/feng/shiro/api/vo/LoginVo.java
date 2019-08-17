package com.feng.shiro.api.vo;

import com.feng.shiro.repository.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 登录返回的Vo
 * @Author chenlinghong
 * @Date 2019/4/21 20:03
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo implements Serializable {

    private static final long serialVersionUID = -4387138932459443667L;

    /**
     * 用户基本信息
     */
    private User user;

}
