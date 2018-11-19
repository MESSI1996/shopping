package com.tz.service;

import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;

public interface IUserService {

    /**
     * 登录接口
     */
    ServerResponse login(String username,String password);

    /**
     * 注册接口
     */
    ServerResponse register(UserInfo userInfo);

    /**
     * 忘记密码
     */
    ServerResponse forgot_get_question(String username);

    /**
     *提交问题答案
     */
    ServerResponse forgot_check_answer(String username,String question,String answer);

    /**
     *忘记密码重置密码
     */
    ServerResponse forgot_reset_password(String username,String passwordNEW,String forgettoken);

    /**
     * 校验用户名和邮箱是否有效
     */
    ServerResponse check_valid (String str,String type);

    /**
     *登录状态下改密码
     */
    ServerResponse reset_password (String username,String passwordOLD,String passwordNEW);


    /**
     *登录状态下改信息
     */
    ServerResponse update_information (UserInfo user);

    /**
     * 解决无法获取更后用户信息的问题根据用户id查询用户信息
     *
     */
    UserInfo findUserInfoByUserid(Integer id);


    /**
     * 保存用户的token信息
     */
    int updateTokenByUserId(Integer userId,String token);

    /**
     *
     * 根据token查询用户信息
     */
    UserInfo findUserInfoByToken(String token);

}
