/*
* <p>文件名称: UserInfo.java</p>
* <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
* <p>公司名称: 深圳市金证科技股份有限公司</p>
* <p>版权所有: (C)2019-2020</p>
*/

package com.szkingdom.rfkj.oauth.model;



/**
*
* 对应表：user_info 实体类.
* 用户表 
* @author admin by koca-lcp
* @version V0.4 2022-11-30
*/
public class UserInfo {
    /**
    * 主键编号
    */
    private String id;

    /**
    * 密码
    */
    private String password;

    /**
    * 用户名称
    */
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
