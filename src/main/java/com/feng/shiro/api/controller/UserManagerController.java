package com.feng.shiro.api.controller;

import com.feng.shiro.api.vo.RegisterVo;
import com.feng.shiro.api.vo.UpdateVo;
import com.feng.shiro.common.PageDto;
import com.feng.shiro.common.ResultUtil;
import com.feng.shiro.common.ResultVo;
import com.feng.shiro.enums.ErrorCodeEnum;
import com.feng.shiro.exception.BusinessException;
import com.feng.shiro.repository.domain.User;
import com.feng.shiro.repository.domain.UserRole;
import com.feng.shiro.service.UserRoleService;
import com.feng.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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

/**
 * @Description 超级管理员对用户的管理
 * @Author fengwen
 * @Date 2019/5/24 23:03
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user/um")
@Slf4j
public class UserManagerController {
    @Autowired
    private UserService userService;

    /**
     * 用于用户角色，权限的判断
     */
    @Autowired
    private UserRoleService userRoleService;


    /**
     * 超级管理员注册用户
     *
     * @param registerVo
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/register")
    @RequiresRoles(value = {"administrator"})
    public ResultVo register(@Valid RegisterVo registerVo, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error("UserController#register: param is error. registerVo={}, request={}. ", registerVo, request);
            throw new BusinessException(
                    ErrorCodeEnum.PARAM_ILLEGAL.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        User user = new User(registerVo);
        boolean result = userService.register(user, registerVo.getType());
        if (result == true) {
            return ResultUtil.success();
        }
        return ResultUtil.error(ErrorCodeEnum.UNKOWN_ERROR);
    }



    /**
     * 超级管理员通过分页查询所有用户的基本信息
     */
    @GetMapping(value = "/user")
    @RequiresRoles(value = {"administrator"})
    public ResultVo listUser(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
                             HttpSession session) {
        return ResultUtil.success(userService.listAll(pageNo, pageSize));
    }



    /**
     * 超级管理员通过电话号码,或姓名查询用户基本信息
     */
    @GetMapping(value = "/param")
    @RequiresRoles(value = {"administrator"})
    public ResultVo getByPhoneOrName(@RequestParam(value = "param", required = false,defaultValue = "")String param,
                                    @RequestParam(value = "pageNo", required = false, defaultValue = "1") Long pageNo,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize
                                    ,HttpSession session) {

        if (StringUtils.isBlank(param)) {
            log.error("UserService#findByTelephone: telephone is null", param);
            throw new BusinessException(ErrorCodeEnum.PARAM_IS_NULL);
        }
        if(param.matches("^1(3|4|5|7|8)\\d{9}")){
            PageDto pageDto = userService.findByTelephone(param);
            return ResultUtil.success(pageDto);
        }else{
            PageDto pageDto = userService.findByName(param,pageNo,pageSize);
            return ResultUtil.success(pageDto);
        }
    }



    /**
     * 超级管理员通过id查询信息
     */
    @GetMapping(value = "/user/{id}")
    @RequiresRoles(value = {"administrator"})
    public ResultVo getById(@PathVariable(value = "id") Long id, HttpSession session) {
        User user = userService.getById(id);
        ResultVo resultVo = ResultUtil.success(user);
        return resultVo;
    }


    /**
     * 超级管理员通过id进行删除用户
     *
     * @param id
     * @param session
     * @return
     */
    @DeleteMapping(value = "/user/{id}")
    @RequiresRoles(value = {"administrator"})
    public ResultVo delete(@PathVariable(value = "id") Long id, HttpSession session) {
        int result = userService.deleteById(id);
        if (result == 1) {
            //删除成功
            return ResultUtil.success();
        } else {
            log.error("UserController#delete： delete error");
            throw new BusinessException(ErrorCodeEnum.DATA_DELETE_ERROR);
        }
    }


    /**
     * 超级管理员修改用户基本信息
     *
     * @param updateVo
     * @param bindingResult
     * @param request
     * @return
     */
    @PutMapping(value = "/user")
    @RequiresRoles(value = {"administrator"})
    public ResultVo update(@Valid @RequestBody UpdateVo updateVo, BindingResult bindingResult,
                           HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error("UserController#register: param is error. updateVo={}, request={}. ", updateVo, request);
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
