package com.tz.service.impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.dao.OrderItemMapper;
import com.tz.dao.OrderMapper;
import com.tz.pojo.Order;
import com.tz.service.IOrderSendService;
import com.tz.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderSendService  implements IOrderSendService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    IProductService productService;

    @Autowired
    OrderItemMapper orderItemMapper;
    @Override
    public ServerResponse send_goods(Long orderMo) {
        if (orderMo==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }


         Order order =orderMapper.findOrderByOrderNo(orderMo);
        int result=order.getStatus();

        if (result==Const.OrderStatusdEnum.ORDER_PAYED.getCode()){



            return  ServerResponse.serverResponseBySuccess(Const.OrderSendEnum.SEND_SUCCESS.getCode(),Const.OrderSendEnum.SEND_SUCCESS.getDesc());
        }


        return ServerResponse.serverResponseByError(Const.OrderSendEnum.SEND_FAIL.getCode(),Const.OrderSendEnum.SEND_FAIL.getDesc());
    }


}
