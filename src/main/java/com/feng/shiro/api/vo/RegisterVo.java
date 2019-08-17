package com.feng.shiro.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Description 用户注册VO
 * @Author chenlinghong
 * @Date 2019/4/21 20:12
 * @Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVo implements Serializable {

    private static final long serialVersionUID = 5801701615619987323L;

    /**
     * 电话号码
     */
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String telephone;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Length(min = 6, message = "密码不能少于6位")
    private String password;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 性别: 0未填写 1男 2女
     */
    private Integer gender;

    /**
     * 描述
     */
    private String description;

    /**
     * 邮箱
     */
    private String email;

    /**
     * TODO: 添加
     * 用户的角色类型  1：超级用户   2：数据采集员  3：普通用户
     */
    @NotNull(message = "用户类型不能为空")
    private Integer type;

    /**
     * TODO: 添加
     * 用户可以导出案例的个数
     */
    @NotNull(message = "用户导出个数不能为空")
    private Integer derivedNumber;
}
