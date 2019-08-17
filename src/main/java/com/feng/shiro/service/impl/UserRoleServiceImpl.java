package com.feng.shiro.service.impl;

import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.exception.BusinessException;
import com.feng.shiro.repository.dao.UserRoleDao;
import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;
import com.feng.shiro.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 查询用户的当前的角色
 * @Author fengwen
 * @Date 2019/5/22 0:56
 * @Version V1.0
 */
@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 通过当前的用户，查询当前的用户-角色表
     *
     * @param user
     * @return
     */
    @Override
    public UserRole getByUser(User user) {
        if (user == null) {
            // 参数为空
            log.error("UserRole#insert: user={}.", user);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_LOGIN);
        }
        return userRoleDao.getByUser(user);
    }

    /**
     * 通过userId进行删除用户角色
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteByUserId(long userId) {
        int result = userRoleDao.deleteByUserId(userId);
        if (result != 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存用户角色
     *
     * @param userRole
     * @return
     */
    @Override
    public boolean insert(UserRole userRole) {
        if (userRole == null) {
            // 参数为空
            log.error("UserRole#insert: userRole={}.", userRole);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_LOGIN);
        }
        int result = userRoleDao.insert(userRole);
        if (result != 0) {
            return true;
        }
        return false;
    }

    /**
     * 超级管理员修改用户基本信息
     *
     * @param userRole
     * @return
     */
    @Override
    public boolean update(UserRole userRole) {
        if (userRole == null) {
            // 参数为空
            log.error("UserRole#insert: userRole={}.", userRole);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_LOGIN);
        }
        int result = userRoleDao.update(userRole);
        if (result != 0) {
            return true;
        }
        return false;
    }
}
