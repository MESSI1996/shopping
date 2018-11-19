package com.tz.controller.backend;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.Product;
import com.tz.pojo.UserInfo;
import com.tz.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value ="/manage/product" )
public class ProductManagerController {


    @Autowired
    IProductService productService;

    /**
     * 新增or更新商品
     */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate(HttpSession session, Product product) {


       /* UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(), Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(), Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/

        //
        return productService.saveOrUpdate(product);

    }


    //产品的上下架
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session, Integer productId, Integer status) {


       /* UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(), Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(), Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/

        //
        return productService.set_sale_status(productId, status);

    }



    //产品的详情
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session, Integer productId) {


      /*  UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(), Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(), Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/

        //
        return productService.detail(productId);
    }



    //查看商品列表
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum ,
                               @RequestParam(value = "pageSize",required = false,defaultValue ="10" ) Integer pageSize)  {


       /* UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(), Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(), Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/

        //
        return productService.list(pageNum,pageSize);
    }



    //产品搜索
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum ,
                               @RequestParam(value = "pageSize",required = false,defaultValue ="10" ) Integer pageSize,
                               @RequestParam(value = "productId",required = false )Integer productId,
                               @RequestParam(value = "productName" ,required = false)String productName
                                    )  {


       /* UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(), Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(), Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/

        //
        return productService.search(pageNum,pageSize,productId,productName);
    }


}