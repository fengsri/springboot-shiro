package com.feng.shiro.api.controller;

import com.feng.shiro.api.util.PermissionUtil;
import com.feng.shiro.api.util.SessionUtil;
import com.feng.shiro.api.vo.LoginVo;
import com.feng.shiro.api.vo.UpdateVo;
import com.feng.shiro.common.ResultUtil;
import com.feng.shiro.common.ResultVo;
import com.feng.shiro.constant.SessionConstant;
import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.exception.BusinessException;
import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;
import com.feng.shiro.service.UserRoleService;
import com.feng.shiro.service.UserService;
import com.feng.shiro.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Description 用户基本信息
 * @Author chenlinghong
 * @Date 2019/4/21 19:59
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用于用户角色，权限的判断
     */
    @Autowired
    private UserRoleService userRoleService;


    /**
     * 用户未登录操作返回提示
     */
    @RequestMapping(value = "/authc")
    public ResultVo authc() {
        throw new BusinessException(ErrorCodeEnum.USER_NOT_LOGIN);
    }



    /**
     * 登录
     *
     * @param telephone 电话号码
     * @param password  密码
     * @param imageCode 图形验证码
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public ResultVo login(@RequestParam(value = "telephone") String telephone,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "imageCode") String imageCode,
                          HttpServletRequest request) {
        if (StringUtils.isBlank(telephone) || StringUtils.isBlank(password) || StringUtils.isBlank(imageCode)) {
            // 参数为空
            log.error("UserController#login: param is null. telephone={}, password={}, imageCode={}, request={}. ",
                    telephone, password, imageCode, request);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        /**
         * 校验短信验证码
         * TODO 后期可改为分布式session
         */
        boolean checkCode = SessionUtil.checkImageCode(imageCode, request);
        if (checkCode == false) {
            // 校验失败
            log.error("UserController#login: image code is error. telephone={}, password={}, imageCode={}, request={}. ",
                    telephone, password, imageCode, request);
            throw new BusinessException(ErrorCodeEnum.IMAGE_CODE_ERROR);
        }

        LoginVo result = new LoginVo();

        //初始化
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(telephone, password);
        User user =null;
        //登录，即身份校验，由通过Spring注入的UserRealm会自动校验输入的用户名和密码在数据库中是否有对应的值
        try {
            //4、登录，即身份验证
            subject.login(token);
            if(subject.isAuthenticated()){
                user = (User)subject.getPrincipal();
                request.getSession().setAttribute(SessionConstant.USER,user);
            }
        }catch (UnknownAccountException e) {
            throw new BusinessException(ErrorCodeEnum.NO_USER);
        } catch (IncorrectCredentialsException e) {
            throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
        } catch (AuthenticationException e) {
            //其他错误，比如锁定，如果想单独处理请单独catch处理
            throw new BusinessException(ErrorCodeEnum.NO_USER);
        }
        result.setUser(user);
        request.getSession().setAttribute("user", user);
        return ResultUtil.success(result);
    }

    /**
     * 用户通过id进行查询自己的信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    @RequiresRoles(value = {"administrator","user","collector"},logical = Logical.OR)
    public ResultVo getById(@PathVariable(value = "id") Long id, HttpSession session) {

        /**
         * 2、数据查询
         */
        User user = userService.getById(id);
        ResultVo resultVo = ResultUtil.success(user);
        return resultVo;
    }


    /**
     * 修改用户名，密码
     *
     * @param session
     * @return
     */
    @PutMapping(value = "/password")
    @RequiresRoles(value = {"administrator","user","collector"},logical = Logical.OR)
    public ResultVo updatePassword(@RequestBody Map<String,Object> map,
                                   HttpSession session) {
        String sId = map.get("id")+"";
        if(sId==null){
            // 参数为空
            log.error("UserController#updatePassword: param is null.  id={}. ", sId);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        Long id =Long.parseLong(sId);
        String password = (String)map.get("password");
        String modifiedPassword = (String)map.get("modifiedPassword");
        String reModifiedPassword = (String)map.get("reModifiedPassword");

        /**
         * 1、参数检验
         */
        if (StringUtils.isBlank(password) || StringUtils.isBlank(modifiedPassword)|| StringUtils.isBlank(reModifiedPassword)) {
            // 参数为空
            log.error("UserController#updatePassword: param is null. password={}, modifiedPassword={},reModifiedPassword={}, id={}. ", password, modifiedPassword,reModifiedPassword, id);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        //判断两次修改的密码是否一致
        if(!modifiedPassword.equals(reModifiedPassword)){
            log.error("UserController#updatePassword: modifiedPassword not equals reModifiedPassword ",modifiedPassword,reModifiedPassword);
            throw new BusinessException(ErrorCodeEnum.PASSWORD_AGREEMENT);
        }

        /**
         * 3、通过id进行查询用户
         */
        User user = userService.getById(id);
        if (user == null) {
            log.error("UserController#updatePassword: not have this user:id={}", id);
            throw new BusinessException(ErrorCodeEnum.NO_USER);
        }

        /**
         * 4、比对用户输入的密码，和数据库的密码是否相同
         */
        String salt  =user.getSalt();
        String pwd = user.getPassword();
        if (!EncryptionUtil.md5Hash(password,salt).equals(pwd)) {
            log.error("UserController#updatePassword: password is error");
            throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
        }

        /**
         * 5、修改密码
         */
        boolean result = userService.updatePassword(id, EncryptionUtil.md5Hash(modifiedPassword,salt));
        if (!result) {
            log.error("UserController#updatePassword: modified password error");
            throw new BusinessException(ErrorCodeEnum.DATA_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }


    /**
     * 用户修改自己基本信息
     *
     * @param updateVo
     * @param bindingResult
     * @return
     */
    @PutMapping(value = "/info")
    @RequiresRoles(value = {"administrator","user","collector"},logical = Logical.OR)
    public ResultVo update(@Valid @RequestBody UpdateVo updateVo, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            log.error("UserController#update: param is error. updateVo={}, request={}. ", updateVo);
            throw new BusinessException(
                    ErrorCodeEnum.PARAM_ILLEGAL.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        //设置用户信息
        User user = new User(updateVo);
        user.setId(updateVo.getId());
        //修改用户基本信息
        int result = userService.update(user);
        if (result != 0) {
            //设置用户角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            if (updateVo.getType() == 1) {
                userRole.setRoleId(1L);
                userRole.setDescription("Administrators");
            } else if (updateVo.getType() == 2) {
                userRole.setRoleId(2L);
                userRole.setDescription("Collector");
            } else if (updateVo.getType() == 3) {
                userRole.setRoleId(3L);
                userRole.setDescription("user");
            } else {
                userRole.setRoleId(3L);
                userRole.setDescription("user");
            }
            boolean roleResult = userRoleService.update(userRole);
            if (roleResult) {
                return ResultUtil.success();
            }
        }
        /**
         * TODO 修改失败
         */
        return ResultUtil.error(ErrorCodeEnum.DATA_UPDATE_ERROR);
    }


}
