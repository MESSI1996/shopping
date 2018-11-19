package com.tz.controller.portal;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.Shipping;
import com.tz.pojo.UserInfo;
import com.tz.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "/shipping")
public class AddressController {

    @Autowired
    IAddressService addressService;

    /**
     * 添加地址
     */
    @RequestMapping(value = "/add.do")
     public  ServerResponse add(HttpSession session, Shipping shipping){
         UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        /*if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }*/

         return  addressService.add(userInfo.getId(),shipping);


     }

    /**
     * 删除地址
     */
    @RequestMapping(value = "/delete.do")
    public  ServerResponse delete(HttpSession session, Integer shippingId){
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
       /* if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }*/

        return  addressService.delete(userInfo.getId(),shippingId);


    }



    /**
     * 登录状态更新地址
     */
    @RequestMapping(value = "/update.do")
    public  ServerResponse update(HttpSession session, Shipping shipping){
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
       /* if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }*/
        shipping.setUserId(userInfo.getId());
        return  addressService.update(shipping);


    }



    /**
     * 选中查看具体的地址
     */
    @RequestMapping(value = "/select.do")
    public  ServerResponse select(HttpSession session, Integer shippingId){
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  addressService.select(shippingId);


    }

    /**
     * 分页地址列表
     */
    @RequestMapping(value = "/list.do")
    public  ServerResponse list(HttpSession session,
                                @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                @RequestParam(required = false,defaultValue = "10")Integer  pageSize){
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  addressService.list(pageNum,pageSize);


    }
}
