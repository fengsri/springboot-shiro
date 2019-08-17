package com.feng.shiro.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 角色
 * @Author chenlinghong
 * @Date 2019/4/18 23:32
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseDomain {

    private static final long serialVersionUID = -8376456689380240157L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 一个角色 对应 角色权限
     */
    private List<RolePermission> rolePermissionList;
}
