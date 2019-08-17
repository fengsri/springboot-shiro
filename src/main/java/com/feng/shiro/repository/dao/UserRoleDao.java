package com.feng.shiro.repository.dao;

import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 用户-角色
 * @Author chenlinghong
 * @Date 2019/4/18 23:41
 * @Version V1.0
 */
public interface UserRoleDao extends IBaseDao<UserRole> {

    /**
     * 通过当前的用户，查询当前的用户-角色表
     * @param user
     * @return
     */
    UserRole getByUser(User user);

    /**
     * 通过当前的用户Id，查询当前的用户-角色表
     * @param userId
     * @return
     */
    UserRole getByUserId(@Param("userId") long userId);

    /**
     * 根据userId删除
     *
     * @param userId
     * @return
     */
    int deleteByUserId(long userId);
}
