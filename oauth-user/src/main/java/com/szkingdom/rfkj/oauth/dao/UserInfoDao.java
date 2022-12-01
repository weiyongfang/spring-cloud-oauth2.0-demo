/*
* <p>文件名称: UserInfoDao.java</p>
* <p>项目描述: KOCA 金证云原生平台 V1.0.0</p>
* <p>公司名称: 深圳市金证科技股份有限公司</p>
* <p>版权所有: (C)2019-2020</p>
*/

package com.szkingdom.rfkj.oauth.dao;

import com.szkingdom.rfkj.oauth.model.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
*
* 对应表：user_info DAO接口.
* 用户表 
* @author admin by koca-lcp
* @version V0.4 2022-11-30
*/
public interface UserInfoDao {

/**
* 新增.
* @param userInfo 实体
* @return 是否操作成功
*/
boolean insert(UserInfo userInfo);

/**
* 修改.
* @param userInfo 实体
* @return 是否操作成功
*/
boolean update(UserInfo userInfo);

    /**
    * 根据主键删除.
    * @param id 主键编号.
    * @return 是否操作成功
    */
    boolean deleteById(@Param("id")String id);

    /**
    * 根据主键查询.
    * @param id 主键编号.
    * @return 映射实体
    */
    UserInfo selectById(@Param("id")String id);

    UserInfo selectByUserName(@Param("userName") String userName);
}
