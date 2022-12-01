package com.szkingdom.rfkj.oauth.config;

import com.szkingdom.rfkj.oauth.model.User;
import com.szkingdom.rfkj.oauth.model.UserInfo;
import com.szkingdom.rfkj.oauth.service.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

/*****
 * 自定义授权认证类:设置用户信息（角色和权限）
 * spring security 会调用这个接口，完成当前用户信息的认证
 * 无论是哪种授权模式都会调用这个服务进行用户信息认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoService userInfoService;

    /****
     * 登录认证：自定义授权认证-用户信息认证-用户名称检验-区分自定义登录接口
     * 获取授权码和账号密码登录时会通过这个检验登录用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            // 客户端认证-从内存获取客户端id和客户端秘钥进行认证
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //数据库查找方式
                return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
         // 密码模式从数据库获取登录用户的信息进行认证
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 获取用户信息
        UserInfo userInfo = userInfoService.loadUserByUsernameAndPassword(username);
        // oauth通过输入用户信息和数据库用户信息进行比较判断用户是否存在
        return new User(userInfo.getUserName(), passwordEncoder.encode(userInfo.getPassword()), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
    }
}
