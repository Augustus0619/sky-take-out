package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService
{
    /**
     * 微信登录,根据用户code请求到openId,为用户生成jwt令牌,为新用户数据库内注册
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
