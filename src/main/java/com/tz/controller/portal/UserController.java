package com.tz.controller.portal;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * 登陆
     */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, String username, String password) {
        ServerResponse serverResponse = userService.login(username, password);

        if (serverResponse.isSuccess()) {//登陆成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER, userInfo);
        }
        return serverResponse;
    }

    /**
     * 注册
     *
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(UserInfo userInfo) {
        ServerResponse serverResponse = userService.register(userInfo);
        return  serverResponse;
    }

    /**
     * 根据用户名查询密保
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/forgot_get_question.do")
    public ServerResponse forgot_get_question(String username) {
        ServerResponse serverResponse = userService.forgot_get_question(username);
        return  serverResponse;
    }

    /**
     * 提交问题答案
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/forgot_check_answer.do")
    public ServerResponse forgot_check_answer(String username,String question,String answer) {
        ServerResponse serverResponse = userService.forgot_check_answer(username,question,answer);
        return  serverResponse;
    }

    /**
     * 忘记密码的重置密码
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/forgot_reset_password.do")
    public ServerResponse forgot_reset_password(String username,String passwordNEW,String forgettoken) {
        ServerResponse serverResponse = userService.forgot_reset_password(username,passwordNEW,forgettoken);
        return  serverResponse;
    }

    /**
     * 检查用户名和邮箱是否youxiao
     *
     */
    @RequestMapping(value = "/check_valid.do")
    public ServerResponse check_valid(String str,String type) {
        ServerResponse serverResponse = userService.check_valid(str,type);
        return  serverResponse;
    }

    /**
     * 获取登陆用户详细信息
     *
     */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
       UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);



       if (userInfo==null){
           return  ServerResponse.serverResponseByError("用户未登陆");
       }
       userInfo.setPassword("");
       userInfo.setQuestion("");
       userInfo.setAnswer("");
       userInfo.setRole(null);
        return  ServerResponse.serverResponseBySuccess(userInfo);
    }

    /**
     * 登录状态修改密码
     */
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordNEW,String passwordOLD,String username) {
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("用户未登陆");
        }
    return  userService.reset_password(userInfo.getUsername(),passwordOLD,passwordNEW);

    }


    /**
     * 登录状态更新个人信息
     */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user) {
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("用户未登陆");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse=userService.update_information(user);
        if (serverResponse.isSuccess()){
            UserInfo userInfo1=userService.findUserInfoByUserid(userInfo.getId());
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return  serverResponse;

    }


    /**
     * 获取登录用户的详细信息
     *
     */
    @RequestMapping(value = "/get_information.do")
    public ServerResponse get_information(HttpSession session) {
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("用户未登陆");
        }
        userInfo.setPassword("");
        return  ServerResponse.serverResponseBySuccess(userInfo);
    }

    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENTUSER);
                return  ServerResponse.serverResponseBySuccess("退出成功");
                }

                }
