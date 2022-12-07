/*
 * <p>文件名称: UserInfoServiceImpl.java</p>
 * <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C)2019-2020</p>
 */

package com.szkingdom.rfkj.oauth.service.impl;

import com.szkingdom.rfkj.oauth.dao.UserInfoDao;
import com.szkingdom.rfkj.oauth.model.UserInfo;
import com.szkingdom.rfkj.oauth.service.UserInfoService;
import com.szkingdom.rfkj.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.Map;

/**
 * 对应表：user_info service接口实现类.
 * 用户表
 *
 * @author admin by koca-lcp
 * @version V0.4 2022-11-30
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public UserInfo loadUserByUsernameAndPassword(String userName) {
        UserInfo userInfo = userInfoDao.selectByUserName(userName);

        return userInfo;
    }

    /**
     * 授权认证方法
     *
     * @param username     ：用户名
     * @param password     ：用户密码
     * @param clientId     ：客户端id
     * @param clientSecret :客户端密钥
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if (authToken == null) {
            //认证失败
            throw new RuntimeException("申请令牌失败");
        }
        //authToken封装了令牌信息的对象
        return authToken;
    }

    /**
     * 若用户真实存在，发送请求根据用户密码申请令牌
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */

    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        ServiceInstance choose = loadBalancerClient.choose("user-auth");
        String s = choose.getUri().toString();
        //定义请求的登录的地址:硬编码
        String path = s + "/oauth/token";
        //构造生成令牌的请求参数
        //定义body
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        //授权方式-密码模式
        multiValueMap.add("grant_type", "password");
        //用户名
        multiValueMap.add("username", username);
        //用户密码
        multiValueMap.add("password", password);
        //定义头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        //用户请求生成令牌的id:密钥 Basic Y2hhbmdnb3U6Y2hhbmdnb3U= 需要base64加密
        header.add("Authorization", httpbasic(clientId, clientSecret));
        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        // 调用 CustomUserAuthenticationConverter
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }

        });
        Map map = null;
        //发送http请求，生成令牌
        try {
            ResponseEntity<Map> exchange = restTemplate.exchange(path, HttpMethod.POST,
                    new HttpEntity<MultiValueMap<String, String>>(multiValueMap, header),
                    Map.class);
            //获取令牌
            map = exchange.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //判断令牌是否成功生成
        if (map == null || map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //jti是jwt令牌的唯一标识作为用户身份令牌
            throw new RuntimeException("创建令牌失败！");
        }
        //将相应数据存到AuthToken对象
        AuthToken authToken = new AuthToken();
        //访问令牌
        String accessToken = (String) map.get("access_token");
        //刷新令牌
        String refreshToken = (String) map.get("refresh_token");
        //jti，作为用户的身份标识
        String jtiToken = (String) map.get("jti");
        authToken.setAccessToken(accessToken);
        authToken.setJti(jtiToken);
        authToken.setRefreshToken(refreshToken);
        return authToken;
    }

    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
