package com.feng.shiro.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 用户-角色
 * @Author chenlinghong
 * @Date 2019/4/18 23:40
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseDomain {

    private static final long serialVersionUID = -290815126347104603L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 描述
     */
    private String description;

    /**
     * 角色
     */
    private Role role;
}
