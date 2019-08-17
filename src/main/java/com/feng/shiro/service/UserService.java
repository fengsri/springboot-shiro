package com.feng.shiro.service;


import com.feng.shiro.common.PageDto;
import com.feng.shiro.repository.domain.User;


/**
 * @Description 用户基本信息
 * @Author chenlinghong
 * @Date 2019/4/19 0:30
 * @Version V1.0
 */
public interface UserService extends IBaseService<User> {

    /**
     * 登录接口
     *
     * @param telephone 电话号码
     * @param password  密码（明文）
     * @return
     */
    User login(String telephone, String password);

    /**
     * 注册用户
     *
     * @param username    用户名
     * @param telephone   电话号码
     * @param password    密码（明文）
     * @param email       邮箱
     * @param avatarUrl   头像URL
     * @param gender      性别：0未填写 1男 2女
     * @param description 描述
     * @return
     */
    boolean register(String username, String telephone, String password, String email, String avatarUrl, int gender, String description, int derivedNumber, int type);

    /**
     * 注册用户
     *
     * @param user 用户对象，注意密码需为密文
     * @return
     */
    boolean register(User user, int type);


    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    boolean updatePassword(Long id, String password);


    /**
     *  通过电话号码进行查询是否存在当前用户
     * @param tel
     * @return
     */
    boolean isExist(String tel);


    /**
     * 通过电话号码查询
     * @param telephone
     * @return
     */
    PageDto<User> findByTelephone(String telephone);

    /**
     * 通过姓名模糊查询
     */
    PageDto<User> findByName(String username, long pageNo, long pageSize);
}
