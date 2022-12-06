/*
* <p>文件名称: UserInfoController.java</p>
* <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
* <p>公司名称: 深圳市金证科技股份有限公司</p>
* <p>版权所有: (C)2019-2020</p>
*/

package com.szkingdom.rfkj.oauth.controller;

import com.szkingdom.rfkj.oauth.model.User;
import com.szkingdom.rfkj.oauth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
*
* 对应表：user_info 接口.
* 用户表 
* @author admin by koca-lcp
* @version V0.4 2022-11-30
*/
@RestController("用户表接口")
@RequestMapping("/user")
public class UserInfoController {

@Autowired
private UserInfoService userInfoService;

@Autowired
private PasswordEncoder passwordEncoder;
   // 获取当前登录用户
    @GetMapping("/getUser")
   public Map<String,String> get(){
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      // details里面存放了当前登录用户的详细信息
       User userDetails = (User) authenticationToken.getPrincipal();
        Map<String,String> map = new HashMap<>();
        if (userDetails!=null) {
            map.put("userName",userDetails.getUsername());
        }
        return map;
   }

}
