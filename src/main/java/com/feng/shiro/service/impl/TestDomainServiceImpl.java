package com.feng.shiro.service.impl;

import com.feng.shiro.common.PageDto;
import com.feng.shiro.repository.dao.TestDomainDao;
import com.feng.shiro.repository.domain.TestDomain;
import com.feng.shiro.service.TestDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 测试
 * @Author chenlinghong
 * @Date 2019/3/29 18:43
 **/
@Service
public class TestDomainServiceImpl implements TestDomainService {

    @Autowired
    private TestDomainDao testDomainDao;

    @Override
    public PageDto<TestDomain> listAll(int pageNo, int pageSize) {
        int offset = (pageNo - 1) * pageSize;
        int rows = pageSize;
        List<TestDomain> beanList = testDomainDao.listAll(offset, rows);
        int totalCount = testDomainDao.count();
        return new PageDto<>(beanList, pageNo, pageSize, totalCount);
    }
}
