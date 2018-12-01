package com.uec.quartz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.uec.common.dubboapi.dbservice.UserDbService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Controller
@RequestMapping("/")
public class FreemarkerController {

    @Reference(version = "${dubbo.service.version}")
    private UserDbService userDbService;

    @GetMapping("freemark")
    public String hello() {
        return "hello";
    }

}