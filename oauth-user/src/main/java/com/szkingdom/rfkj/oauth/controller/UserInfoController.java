/*
* <p>文件名称: UserInfoController.java</p>
* <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
* <p>公司名称: 深圳市金证科技股份有限公司</p>
* <p>版权所有: (C)2019-2020</p>
*/

package com.szkingdom.rfkj.oauth.controller;

import com.szkingdom.rfkj.oauth.model.Result;
import com.szkingdom.rfkj.oauth.model.User;
import com.szkingdom.rfkj.oauth.service.UserInfoService;
import com.szkingdom.rfkj.oauth.util.AuthToken;
import com.szkingdom.rfkj.oauth.util.CookieTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

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

    /**
     * 使用oauth2.0完成登录认证
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam(value = "username") String username,@RequestParam(value = "password")  String password, HttpServletResponse response, HttpServletRequest request){
        //1.判断用户名和密码是否为空
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        //2.根据用户名和密码以及客户端id、密钥生成令牌
        AuthToken authToken = userInfoService.login(username, password, clientId, clientSecret);
        //3.将令牌存到cookie，返回客户端
        String accessToken = authToken.getAccessToken();
        //登录成功后将令牌保存到cookie中
        CookieTools.setCookie(request,response,"Authorization",accessToken);
        return new Result(true, 200,"登录成功！");
    }

    //
    @GetMapping("/getCookie")

    public String getCookie(HttpServletRequest request){
        String authorization = CookieTools.getCookieValue(request, "Authorization");
        return authorization;
    }

}
