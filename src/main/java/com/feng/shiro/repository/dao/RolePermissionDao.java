package com.feng.shiro.repository.dao;

import com.feng.shiro.repository.domain.RolePermission;

import java.util.List;

/**
 * @Description 角色-权限
 * @Author chenlinghong
 * @Date 2019/4/18 23:57
 * @Version V1.0
 */
public interface RolePermissionDao extends IBaseDao<RolePermission> {

    List<RolePermission> getByRoleId(Long roleId);
}
