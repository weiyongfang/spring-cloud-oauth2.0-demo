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

}
