package com.szkingdom.rfkj.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 授权服务器（认证服务器）配置类
 * 配置客户端信息
 * 配置令牌服务和端点
 * 配置端点的完成访问策略
 */
@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //SpringSecurity 用户自定义授权认证类
    @Autowired
    UserDetailsService userDetailsService; // 用户信息


    //授权认证管理器
    @Autowired
    AuthenticationManager authenticationManager; //密码模式

    @Autowired
    AuthorizationCodeServices authorizationCodeServices; // 授权码模式

    @Autowired
    TokenStore tokenStore; // 令牌存储方式

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService; // 客户端详情服务-内存方式

    /***
     * 认证服务支持的客户端详情配置-哪些客户端可以到授权服务器来申请令牌
     * 1.支持哪些客户端请求
     * 2.客户端的认证方式
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 内存模式-写死
        clients.inMemory() // 使用内存方式
                .withClient("changgou")          //客户端id
                .secret(passwordEncoder.encode("changgou")) // 客户端秘钥                   //秘钥
                .redirectUris("http://www.baidu.com")       //客户端回调地址
                .accessTokenValiditySeconds(3600)          //访问令牌有效期
                .refreshTokenValiditySeconds(3600)         //刷新令牌有效期
                .authorizedGrantTypes( // 客户端可以以哪种模式来申请令牌
                        "authorization_code",          //根据授权码生成令牌
                        "client_credentials",          // 客户端认证
                        "refresh_token",                //刷新令牌
                        "password")                     //密码方式认证
                .scopes("all")            // 允许客户端的访问范围
                .autoApprove(false);      // 跳转到授权页面
        //动态获取客户端信息配置（数据库里获取，表格是官方给的，不能修改）clientDetails：查询客户端信息
        // clients.jdbc(dataSource).clients(clientDetails());
    }

    /**
     * 授权服务器令牌服务配置
     * 令牌产生方式
     * 令牌存储策略-内存模式
     * 有效时长
     *
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);//客户端详情-内存模式
        services.setSupportRefreshToken(true);// 是否产生刷新令牌
        services.setTokenStore(tokenStore); // 令牌服务-令牌存储策略-JWT、数据库、内存
        services.setAccessTokenValiditySeconds(7200); // 有效时长
        services.setRefreshTokenValiditySeconds(259200); // 有效天数
        return services;
    }


    /**
     * 端点：oauth提供的url
     * 授权服务器令牌相关的访问端点配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.accessTokenConverter(jwtAccessTokenConverter)
//                .authenticationManager(authenticationManager)//认证管理器
//                .tokenStore(tokenStore)                       //令牌存储
//                .userDetailsService(userDetailsService);     // 用户信息service，密码模式需要配置

        endpoints.authenticationManager(authenticationManager) // 密码模式需要-认证管理器
                .authorizationCodeServices(authorizationCodeServices) // 授权码模式需要
                .tokenServices(tokenServices()) // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)  // 端点运行POST提交
                 .userDetailsService(userDetailsService);     // 用户信息service，密码模式需要配置，获取用户信息
        ;
    }


    /***
     * 授权服务器的安全配置-令牌端点的安全约束
     *
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients() // 允许表单认证-表单提交申请令牌-认证请求-刷新令牌
                .tokenKeyAccess("permitAll()") // 使用JWT时，提供公钥的端点公开
                .checkTokenAccess("permitAll()"); // /oauth/check_token 公开
    }


}

