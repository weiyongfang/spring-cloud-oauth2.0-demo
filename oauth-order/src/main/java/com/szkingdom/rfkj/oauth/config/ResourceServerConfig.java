package com.szkingdom.rfkj.oauth.config;
/**
 * 微服组资源服务-登录权限认证和权限控制：自动通过秘钥校验请求令牌是否合法，完成授权
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer//标识当前的微服务是一个资源服务器,需要进行令牌的校验
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//激活方法上的PreAuthorize注解--方法的权限控制
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //id
    private static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;


    @Override
    public void configure(ResourceServerSecurityConfigurer configurer){
        configurer.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore) // 生成秘钥时的秘钥
                .stateless(true);
    }

    /***
     * Http安全配置，对每个到达系统的http请求链接进行校验
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests()
                //下边的路径放行:需要给登录认证服务放行获取用户信息
                .antMatchers().permitAll()//配置地址放行
                .anyRequest().authenticated()// 其他的需要认证
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}