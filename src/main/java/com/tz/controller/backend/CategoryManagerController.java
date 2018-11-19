package com.tz.controller.backend;

import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {
    /**
     * 获取品类 子节点
     */

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId){
      /*UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
            }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/



        return  categoryService.get_category(categoryId);
    }


    /**
     * 增加节点
     *
     *@RequestParam(required = false,defaultValue ="0"表示改参数可传课不传
     *
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,@RequestParam(required = false,defaultValue ="0" ) Integer parentId,String categoryName){
       /* UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/



        return  categoryService.add_category(parentId,categoryName);
    }


    /**
     * 修改节点
     *
     *@RequestParam(required = false,defaultValue ="0"表示改参数可传课不传
     *
     */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,Integer categoryId,String categoryName){
      /*  UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }*/



        return  categoryService.set_category_name(categoryId,categoryName);
    }



    /**
     * 获取当前分类的id即递归子节点categiryId
     *
     *@RequestParam(required = false,defaultValue ="0"表示改参数可传课不传
     *
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,Integer categoryId){
      /*  UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
*/


        return  categoryService.get_deep_category(categoryId);
    }


}
