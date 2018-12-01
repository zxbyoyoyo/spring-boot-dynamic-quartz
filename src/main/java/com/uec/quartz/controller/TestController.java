package com.uec.quartz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.uec.common.dubboapi.dbservice.UserDbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@RestController
@RequestMapping("/")
public class TestController {

    @Reference(version = "${dubbo.service.version}")
    private UserDbService userDbService;

    @GetMapping("hello")
    public String hello() {
         userDbService.findById("1");
        return "HHH";
    }

}