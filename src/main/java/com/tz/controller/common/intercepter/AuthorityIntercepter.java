package com.tz.controller.common.intercepter;

import com.google.gson.Gson;
import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 权限拦截器
 */
public class AuthorityIntercepter implements HandlerInterceptor {

    @Autowired
    IUserService userService;
    /**
     * 请求到达controller响应之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        HttpSession session=request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);

        if (userInfo==null){//从cookie中获取用户信息
            Cookie cookie[]=request.getCookies();
            for (int i=0;i<cookie.length;i++){
               String  CookieName=cookie[i].getName();
               if (CookieName.equals(Const.AUTOLOGINCTOKEN)){

                   String autologintoken=cookie[i].getValue();
                   //根据token查询用户信息
                userInfo =   userService.findUserInfoByToken(autologintoken);
                    if (userInfo!=null){
                        session.setAttribute(Const.CURRENTUSER,userInfo);
                    }
                   break;
               }
            }

        }




        if (userInfo==null||userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter=response.getWriter();
            if (userInfo == null) {
                ServerResponse serverResponse=ServerResponse.serverResponseByError("用户未登录");
                Gson gson=new Gson();
                String string=gson.toJson(serverResponse);
                printWriter.write(string);


            }else {
                ServerResponse serverResponse=ServerResponse.serverResponseByError("用户无权限"); 
                Gson gson=new Gson();
                String string=gson.toJson(serverResponse);
                printWriter.write(string);
            }
            printWriter.flush();
            printWriter.close();
            return  false;
        }

        return true;//返回true才可以进入controller执行之后的操作









       /*
      通过如下配置完成指定方法的拦截

       HandlerMethod handlerMethod=(HandlerMethod) o;
        String classname=handlerMethod.getBean().getClass().getSimpleName();
        String methodname=handlerMethod.getMethod().getName();
        if (classname.equals("ProductManagerController")&&methodname.equals("set-sale-status")){

        }


        */


    }
    /**
     *  controller响应返回前台之前调用
     */

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    /**
     * 响应到达客户端
     */
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
