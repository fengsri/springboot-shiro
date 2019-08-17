package com.feng.shiro.service;

import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;

/**
 * @Description  用户查看当前的用户角色
 * @Author fengwen
 * @Date 2019/5/22 0:55
 * @Version V1.0
 */
public interface UserRoleService {

    /**
     * 通过当前的用户，查询当前的用户-角色表
     * @param user
     * @return
     */
    UserRole getByUser(User user);

    /**
     * 超级管理员通过用户的userId进行删除，用户角色表
     * @param userId
     * @return
     */
    boolean deleteByUserId(long userId);

    /**
     * 超级管理员添加用户角色
     */
    boolean insert(UserRole userRole);

    /**
     * 超级管理员修改用户基本信息
     */
    boolean update(UserRole userRole);

}
