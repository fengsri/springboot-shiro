package com.feng.shiro.api.controller;

import com.feng.shiro.common.ResultUtil;
import com.feng.shiro.common.ResultVo;
import com.feng.shiro.service.TestDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试
 * @Author chenlinghong
 * @Date 2019/3/29 19:43
 **/
@RestController
@RequestMapping(value = "/test")
public class TestDomainController {

    @Autowired
    private TestDomainService testDomainService;

    @GetMapping("/list")
    public ResultVo listAll(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return ResultUtil.success(testDomainService.listAll(pageNo, pageSize));
    }
}
