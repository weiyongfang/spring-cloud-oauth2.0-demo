package com.szkingdom.rfkj.oauth.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 令牌的存储方式-最简单的
 * @author Lenovo
 * @date 2022/11/30 14:14
 */
@Configuration
public class TokenConfig {
    //令牌存储策略-JWT、数据库、内存
    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore(); // 最原始的令牌存储方式-先不用JWT
    }
}
