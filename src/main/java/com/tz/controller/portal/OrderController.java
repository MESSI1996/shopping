package com.tz.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.pojo.UserInfo;
import com.tz.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     */
    @RequestMapping(value = "/createOrder.do")
    public ServerResponse createOrder(HttpSession session,Integer shippingId){

        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.createOrder(userInfo.getId(),shippingId);
    }


    /**
     * 取消订单
     *
     */
    @RequestMapping(value = "/cancle.do")
    public ServerResponse cancle(HttpSession session,Long orderNo){

        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.cancle(userInfo.getId(),orderNo);
    }



    /**
     * 获取创建订单后购物车订单商品的信息
     *
     */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){

        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.get_order_cart_product(userInfo.getId());
    }




    /**
     *订单列表
     *
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize    ){

        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.list(userInfo.getId(),pageNum,pageSize);
    }




    /**
     *订单详情
     *
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){

        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.detail(orderNo);
    }


    /**
     * 支付接口
     */
    @RequestMapping(value = "/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }

        return  orderService.pay(userInfo.getId(),orderNo);
    }


    /**
     * 支付宝服务器回调应用服务器
     */
    @RequestMapping(value = "/alipay_callback.do")
    public ServerResponse callback(HttpServletRequest request){
        System.out.println("======支付宝回调应用接口=======");
        Map<String,String[]> params=request.getParameterMap();
        Map<String,String> requestparams= Maps.newHashMap();
        Iterator<String>   it=params.keySet().iterator();
        while (it.hasNext()){
            String key=it.next();
            String[] strArr=params.get(key);
            String value="";
            for (int i=0;i<strArr.length;i++){
                value=(i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";

            }
            requestparams.put(key,value);
        }
        //1:支付宝验签
        try {
            requestparams.remove("sign_type");
            boolean result=AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (result==false){
                return  ServerResponse.serverResponseByError("非法请求，验证失败");
            }


        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //处理业务逻辑
        return  orderService.alipay_callback(requestparams);
    }


    /**
     * 查询订单的支付状态
     */
    @RequestMapping(value = "/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("请登录");
        }
        return orderService.query_order_pay_status(orderNo);
    }
}