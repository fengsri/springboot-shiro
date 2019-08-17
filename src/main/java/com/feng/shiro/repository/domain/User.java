package com.feng.shiro.repository.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feng.shiro.api.vo.RegisterVo;
import com.feng.shiro.api.vo.UpdateVo;
import com.feng.shiro.util.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 用户基本信息
 * @Author chenlinghong
 * @Date 2019/4/18 22:45
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseDomain {

    private static final long serialVersionUID = 1743615449161992939L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    @JsonIgnore
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户导出案例的个数
     * TODO: 添加
     */
    private Integer derivedNumber;


    /**
     * 盐值

     * TODO: 添加
     */
    @JsonIgnore
    private String salt;

    /**
     * 用户角色
     * TODO: 添加
     */
    private UserRole userRole;



    /**
     * 构造函数 ， password为密文
     * @param registerVo
     */
    public User(RegisterVo registerVo){
        this.username = registerVo.getUsername().trim();
        this.salt = EncryptionUtil.getMd5HashSalt(32);
        this.password = EncryptionUtil.md5Hash(registerVo.getPassword(),salt);
        this.telephone = registerVo.getTelephone().trim();
        this.email = registerVo.getEmail();
        this.gender = registerVo.getGender();
        this.description = registerVo.getDescription();
        this.derivedNumber = registerVo.getDerivedNumber();
    }

    /**
     * 通过修改的Vo构造函数
     * @param updateVo
     */
    public User(UpdateVo updateVo){
        this.username = updateVo.getUsername().trim();
        this.telephone = updateVo.getTelephone().trim();
        this.email = updateVo.getEmail();
        this.gender = updateVo.getGender();
        this.description = updateVo.getDescription();
        this.derivedNumber = updateVo.getDerivedNumber();
    }

}
