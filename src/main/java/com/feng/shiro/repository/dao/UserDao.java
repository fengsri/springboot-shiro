package com.feng.shiro.repository.dao;

import com.feng.shiro.repository.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description 用户基本信息
 * @Author chenlinghong
 * @Date 2019/4/18 22:48
 * @Version V1.0
 */
public interface UserDao extends IBaseDao<User> {

    /**
     * 根据电话号码和密码获取用户信息
     *
     * @param telephone
     * @param password
     * @return
     */
    User getByTelephoneAndPassword(@Param("telephone") String telephone, @Param("password") String password);

    /**
     * 根据电话号码获取行数
     *
     * @param telephone
     * @return
     */
    int countByTelephone(String telephone);

    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    int updatePassword(@Param(value = "id") Long id, @Param(value = "password") String password);

    /**
     * 电话号码对应的user
     *
     * @param telephone
     * @return
     */
    User getByTelephone(String telephone);

    /**
     * 如果插入的认识已经删除的，就通过修改的方式
     *
     * @param user
     * @return
     */
    int insertUpdate(User user);

    /**
     * 同用户名模糊查询
     * @param name
     * @param offset
     * @param rows
     * @return
     */
    List<User> getByName(@Param("name") String name, @Param("offset") long offset, @Param("rows") long rows);

    /**
     * 模糊查询用户名个数
     * @param name
     * @return
     */
    long getCountByName(@Param("name") String name);
}
