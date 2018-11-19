package com.tz.controller.backend;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.IOrderSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/order")
public class OrderManagerController {

    @Autowired
    IOrderSendService orderSendService;

    @RequestMapping(value ="/send_goods.do" )
    public ServerResponse send_goods(HttpSession session, Long orderNo){
        //1:判断用户权限
       /* UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return  ServerResponse.serverResponseByError("没有权限");
        }*/


        return orderSendService.send_goods(orderNo);
    }
}
