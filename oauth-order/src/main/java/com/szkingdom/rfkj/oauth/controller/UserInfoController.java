/*
 * <p>文件名称: UserInfoController.java</p>
 * <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C)2019-2020</p>
 */

package com.szkingdom.rfkj.oauth.controller;

import com.szkingdom.rfkj.oauth.service.UserInfoService;
import com.szkingdom.rfkj.oauth.util.CookieTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 对应表：user_info 接口.
 * 用户表
 *
 * @author admin by koca-lcp
 * @version V0.4 2022-11-30
 */
@RestController("用户表接口")
@RequestMapping("/order")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    //
    @GetMapping("/getCookie")
//    @PreAuthorize(value = "hasAnyAuthority(admin)") // 只有order能访问
    public String getCookie(HttpServletRequest request) {
        String authorization = CookieTools.getCookieValue(request, "Authorization");
        return authorization;
    }

}
