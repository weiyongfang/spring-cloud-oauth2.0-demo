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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
*
* 对应表：user_info service接口实现类.
* 用户表 
* @author admin by koca-lcp
* @version V0.4 2022-11-30
*/
@Service
public class UserInfoServiceImpl implements UserInfoService {
@Autowired
private UserInfoDao userInfoDao;
    @Override
    public UserInfo loadUserByUsernameAndPassword(String userName) {
        UserInfo userInfo = userInfoDao.selectByUserName(userName);

        return userInfo;
    }
}
