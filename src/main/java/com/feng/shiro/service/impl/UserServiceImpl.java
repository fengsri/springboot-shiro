package com.feng.shiro.service.impl;

import com.feng.shiro.common.PageDto;
import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.enums.NumericEnum;
import com.feng.shiro.exception.BusinessException;
import com.feng.shiro.repository.dao.UserDao;
import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;
import com.feng.shiro.service.UserRoleService;
import com.feng.shiro.service.UserService;
import com.feng.shiro.util.EncryptionUtil;
import com.feng.shiro.util.TelephoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 用户基本信息
 * @Author chenlinghong
 * @Date 2019/4/19 0:30
 * @Version V1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 对user操作
     */
    @Autowired
    private UserDao userDao;

    /**
     * 用户注册保存用户角色,userId进行删除UserRole
     */
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public int insert(User user) {
        if (user == null) {
            // 参数为空
            log.error("UserService#insert: user={}.", user);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        int result = userDao.insert(user);
        /**
         * TODO 后期校验返回结果
         */
        return result;
    }

    /**
     * 通过id进行删除用户，及用户角色UserRole
     *
     * @param id ID
     * @return
     */
    @Override
    public int deleteById(long id) {
        int userResult = userDao.deleteById(id);
        //判断用户user删除的情况
        if (userResult != 0) {
            //判断用户角色删除的情况
            boolean result = userRoleService.deleteByUserId(id);
            if (result) {
                return 1;
            }
        }
        return 0;
    }


    /**
     * 通过id进行查用户信息
     *
     * @param id ID
     * @return
     */
    @Override
    public User getById(long id) {
        return userDao.getById(id);
    }

    /**
     * 通过分页查询所有的用户
     *
     * @param pageNo   第几页
     * @param pageSize 每页行数
     * @return
     */
    @Override
    public PageDto<User> listAll(long pageNo, long pageSize) {
        if (pageNo <= 0 || pageSize < 0) {
            pageNo = NumericEnum.ONE.getNumeric();
            pageSize = NumericEnum.TEN.getNumeric();
        }
        List<User> userList = userDao.listAll((pageNo - 1) * pageSize, pageSize);
        long total = userDao.count();
        return new PageDto<>(userList, pageNo, pageSize, total);
    }

    /**
     * 修改用户基本信息
     *
     * @param user
     * @return
     */
    @Override
    public int update(User user) {
        if (user == null) {
            // 参数为空
            log.error("UserService#update: user={}.", user);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }

        // 校验电话号码合法性
        if (TelephoneUtil.isNotPhoneLegal(user.getTelephone())) {
            log.error("UserService#register: telephone is illegal", user.getTelephone());
            throw new BusinessException(ErrorCodeEnum.TELEPHONE_ILLEGAL);
        }

        int result = userDao.update(user);
        /**
         * TODO 后期校验结果
         */
        return result;
    }

    /**
     * 用户登录
     *
     * @param telephone 电话号码
     * @param password  密码（明文）
     * @return
     */
    @Override
    public User login(String telephone, String password) {
        if (StringUtils.isBlank(telephone) || StringUtils.isBlank(password)) {
            log.error("UserService#login: param is null. telephone={}, password={}. ", telephone, password);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        // 校验电话号码合法性
        if (TelephoneUtil.isNotPhoneLegal(telephone)) {
            log.error("UserService#login: telephone is illegal. telephone={}, password={}. ", telephone, password);
            throw new BusinessException(ErrorCodeEnum.TELEPHONE_ILLEGAL);
        }
        int countUser = userDao.countByTelephone(telephone);
        if (countUser <= 0) {
            // 用户不存在
            log.error("UserService#login: no user. telephone={}, password={}. ", telephone, password);
            throw new BusinessException(ErrorCodeEnum.NO_USER);
        } else if (countUser == 1) {
            // 存在用户
            //String encodePassword = EncryptionUtil.ccMD5(password);
            //User user = userDao.getByTelephoneAndPassword(telephone, encodePassword);
            User user = userDao.getByTelephoneAndPassword(telephone, password);
            if (user == null) {
                // 密码错误
                log.error("UserService#login: password is error. telephone={}, password={}. ", telephone, password);
                throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
            }
            /**
             * TODO 后期可能封装更多信息
             */
            return user;
        } else {
            /**
             * TODO 数据存在问题
             */
        }
        return null;
    }

    /**
     * 用户注册
     *
     * @param username      用户名
     * @param telephone     电话号码
     * @param password      密码（明文）
     * @param email         邮箱
     * @param avatarUrl     头像URL
     * @param gender        性别：0未填写 1男 2女
     * @param description   描述
     * @param derivedNumber
     * @param type
     * @return
     */
    @Override
    public boolean register(String username, String telephone, String password, String email, String avatarUrl,
                            int gender, String description, int derivedNumber, int type) {
        if (StringUtils.isBlank(telephone) || StringUtils.isBlank(password)) {
            log.error("UserService#register: param is null. username={}, telephone={}, password={}, " +
                            "email={}, avatarUrl={}, gender={}, description={}. derivedNumber={}",
                    username, telephone, password, email,
                    avatarUrl, gender, description, derivedNumber);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        // 校验电话号码合法性
        if (TelephoneUtil.isNotPhoneLegal(telephone)) {
            log.error("UserService#register: telephone is illegal. username={}, telephone={}, password={}, " +
                            "email={}, avatarUrl={}, gender={}, description={}. ",
                    username, telephone, password, email, avatarUrl, gender, description);
            throw new BusinessException(ErrorCodeEnum.TELEPHONE_ILLEGAL);
        }

        //查询当前电话号码是否已经注册
        if (isExist(telephone)) {
            log.error("UserService#register: telephone has register", telephone);
            throw new BusinessException(ErrorCodeEnum.TELEPHONE_HAS_REGISTER);
        }

        String salt = EncryptionUtil.getMd5HashSalt(32);
        String md5Password = EncryptionUtil.md5Hash(password,salt);
        /**
         * 封装对象
         */
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setTelephone(telephone);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);
        user.setGender(gender);
        user.setDescription(description);
        user.setDerivedNumber(derivedNumber);
        user.setSalt(salt);
        return register(user, type);

    }

    /**
     * 用户注册
     *
     * @param user 用户对象，注意密码需为密文
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user, int type) {
        if (user == null) {
            log.error("UserService#register: param is null. user={}. ", user);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }

        String telephone = user.getTelephone();
        User us = userDao.getByTelephone(telephone);
        //us为空，则没有人注册。
        //us不为空，那么先看看deleted的状态，1为已删除，2为有
        int result = 0;
        if (us != null) {
            Integer deleted = us.getDeleted();
            if (deleted == 0) {
                log.error("UserService#register: telephone has register", telephone);
                throw new BusinessException(ErrorCodeEnum.TELEPHONE_HAS_REGISTER);
            } else {
                user.setDeleted(0);
                user.setId(us.getId());
                result = userDao.insertUpdate(user);
            }
        } else {
            result = userDao.insert(user);
        }

        if (result == 1) {
            // 新增成功,添加用户角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            if (type == 1) {
                userRole.setRoleId(1L);
                userRole.setDescription("Administrators");
            } else if (type == 2) {
                userRole.setRoleId(2L);
                userRole.setDescription("Collector");
            } else if (type == 3) {
                userRole.setRoleId(3L);
                userRole.setDescription("user");
            } else {
                userRole.setRoleId(3L);
                userRole.setDescription("user");
            }
            boolean roleResult = userRoleService.insert(userRole);
            if (roleResult) {
                return true;
            }
        }
        /**
         * TODO 数据存在问题
         */
        return false;
    }


    /**
     * 修改用户密码
     *
     * @param id
     * @param password
     * @return
     */
    @Override
    public boolean updatePassword(Long id, String password) {
        if (id == null || id <= 0) {
            log.error("UserService#updatePassword: id is null", id);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        if (id < 0) {
            log.error("UserService#updatePassword: id is error", id);
            throw new BusinessException(ErrorCodeEnum.PARAM_ILLEGAL);
        }
        if (StringUtils.isBlank(password)) {
            log.error("UserService#updatePassword: password is null", password);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        int result = userDao.updatePassword(id, password);
        if (result == 0) {
            log.error("UserService#updatePassword: update password is error", password);
            throw new BusinessException(ErrorCodeEnum.DATA_DELETE_ERROR);
        }
        return true;
    }


    /**
     * 通过电话号码进行查询，当前用户是否存在
     *
     * @param tel
     * @return
     */
    @Override
    public boolean isExist(String tel) {
        if (StringUtils.isBlank(tel)) {
            log.error("UserService#register: telephone is null", tel);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        int count = userDao.countByTelephone(tel);
        if (count != 0) {
            return true;
        }
        return false;
    }


    /**
     * 通过电话号码查询
     * @param tel
     * @return
     */
    @Override
    public PageDto<User> findByTelephone(String tel) {
        if(!tel.matches("^1(3|4|5|7|8)\\d{9}")){
            log.error("UserService#findByTelephone: telephone is error", tel);
            throw new BusinessException(ErrorCodeEnum.TELEPHONE_ILLEGAL);
        }

        User user = userDao.getByTelephone(tel);
        if(user==null){
            log.error("UserService#findByTelephone: telephone find user is null", tel);
            throw new BusinessException(ErrorCodeEnum.NO_USER);
        }
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return new PageDto<User>(userList,1L,1L,1L);
    }


    /**
     * 通过用户名模糊查询
     * @param name
     * @return
     */
    @Override
    public PageDto<User> findByName(String name,long pageNo, long pageSize) {
        if (pageNo <= 0 || pageSize < 0) {
            pageNo = NumericEnum.ONE.getNumeric();
            pageSize = NumericEnum.TEN.getNumeric();
        }
        List<User> userList = userDao.getByName(name,(pageNo - 1) * pageSize, pageSize);
        long total = userDao.getCountByName(name);
        return new PageDto<>(userList, pageNo, pageSize, total);
    }


}
