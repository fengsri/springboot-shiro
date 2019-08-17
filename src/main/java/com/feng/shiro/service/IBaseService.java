package com.feng.shiro.service;

import com.feng.shiro.common.PageDto;

/**
 * @Description 基本Service接口, 基本业务对象均需继承该接口
 * @Author chenlinghong
 * @Date 2019/4/12 13:56
 * @Version V1.0
 */
public interface IBaseService<T> {

    /**
     * 新增
     *
     * @param t 具体对象
     * @return 数据库表操作行数
     */
    int insert(T t);

    /**
     * 根据ID删除
     *
     * @param id ID
     * @return 数据库表操作行数
     */
    int deleteById(long id);

    /**
     * 根据ID获取domain对象
     *
     * @param id ID
     * @return 查询获取Domain对象，对象不存在时返回null
     */
    T getById(long id);

    /**
     * 分页获取所有对象
     *
     * @param pageNo   第几页
     * @param pageSize 每页行数
     * @return Domain的分页对象
     */
    PageDto<T> listAll(long pageNo, long pageSize);

    /**
     * 根据对象基本属性，对象中每个字段必填，否则出现更新为NULL，会引发错误
     *
     * @param t 对象
     * @return 数据库表更新行数
     */
    int update(T t);
}
