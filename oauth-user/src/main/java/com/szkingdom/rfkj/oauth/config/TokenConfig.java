package com.szkingdom.rfkj.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 令牌的存储方式-JWT令牌
 *
 * @author Lenovo
 * @date 2022/11/30 14:14
 */
@Configuration
public class TokenConfig {
    private String SIGNING_KYE = "uua123"; // 对称秘钥--对称算法
//    //令牌存储策略-JWT、数据库、内存
//    @Bean
//    public TokenStore tokenStore(){
//        return new InMemoryTokenStore(); // 最原始的令牌存储方式-先不用JWT
//    }

    /**
     * JWT令牌方式存储用户信息
     * @return
     */
    @Bean
    @Autowired
    public TokenStore tokenStore() {
        // JWT 令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    /****
     * JWT令牌转换器-不是生成令牌，是生成令牌的依据
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KYE); // 对称秘钥
        return jwtAccessTokenConverter;
    }

}
