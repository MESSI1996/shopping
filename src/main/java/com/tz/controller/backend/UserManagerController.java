package com.tz.controller.backend;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.IUserService;
import com.tz.utils.IpUtils;
import com.tz.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 后台用户控制器类
 */
@RestController
@RequestMapping(value = "/manager/user")
public class UserManagerController {

    @Autowired
    IUserService userService;
    /**
     * 管理员登陆
     */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, HttpServletRequest request, HttpServletResponse response, String username, String password) {



        ServerResponse serverResponse = userService.login(username, password);

        if (serverResponse.isSuccess()) {//登陆成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if (userInfo.getRole()==Const.RoleEnum.ROLE_CUSTOMER.getCode()){
                return  ServerResponse.serverResponseByError("无权登陆");
            }

            session.setAttribute(Const.CURRENTUSER, userInfo);
            //生成token
            String ip= IpUtils.getRemoteAddress(request);
            try {
               String mac= IpUtils.getMACAddress(ip);
                String token=MD5Utils.getMD5Code(mac);
                //token保存到数据库
                userService.updateTokenByUserId(userInfo.getId(),token);
                //token作为cookie响应客户端
                Cookie cookie=new Cookie(Const.AUTOLOGINCTOKEN,token);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*7);
                response.addCookie(cookie);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return serverResponse;
    }
}
