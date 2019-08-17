package com.feng.shiro.repository.dao;

import com.feng.shiro.repository.domain.TestDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 测试
 * @Author chenlinghong
 * @Date 2019/3/29 18:14
 **/
public interface TestDomainDao {

    int insert(TestDomain testDomain);

    int deleteById(int id);

    TestDomain getById(int id);

    List<TestDomain> listAll(@Param("offset") int offset, @Param("rows") int rows);

    int count();

    int update(TestDomain testDomain);

}
