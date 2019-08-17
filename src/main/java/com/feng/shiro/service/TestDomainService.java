package com.feng.shiro.service;

import com.feng.shiro.common.PageDto;
import com.feng.shiro.repository.domain.TestDomain;

/**
 * @Description 测试
 * @Author chenlinghong
 * @Date 2019/3/29 18:42
 **/
public interface TestDomainService {

    /**
     * 分页获取所有列表
     * @param pageNo    第几页
     * @param pageSize  每页条数
     * @return
     */
    PageDto<TestDomain> listAll(int pageNo, int pageSize);

}
