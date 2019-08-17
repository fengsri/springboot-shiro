package com.feng.shiro.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 权限
 * @Author chenlinghong
 * @Date 2019/4/18 23:48
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseDomain {

    private static final long serialVersionUID = 8076189469063060802L;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
