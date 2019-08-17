package com.feng.shiro.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 角色-权限
 * @Author chenlinghong
 * @Date 2019/4/18 23:56
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends BaseDomain {

    private static final long serialVersionUID = -7358666723248456788L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 描述
     */
    private String description;

    /**
     * 权限
     */
    private Permission permission;
}
