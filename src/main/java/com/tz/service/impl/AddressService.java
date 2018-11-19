package com.tz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.tz.common.ServerResponse;
import com.tz.dao.ShippingMapper;
import com.tz.pojo.Shipping;
import com.tz.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressService implements IAddressService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //1：参数的非空校验

        if (shipping==null){
            return  ServerResponse.serverResponseByError("参数错误");
        }
        //2：添加
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);

        //3：返回结果
        Map<String,Integer> map= Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.serverResponseBySuccess(map);
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        //1：参数非空校验
        if (shippingId==null){
            return  ServerResponse.serverResponseByError("参数错误");
        }

        //2：删除
        int result=shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);
        //3：返回结果
        if (result>0){
            return  ServerResponse.serverResponseBySuccess("删除成功");
        }
        return ServerResponse.serverResponseByError("删除失败");
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        //1:非空判断
        if (shipping==null){
            return  ServerResponse.serverResponseByError("参数不正确");
        }


        //2：更新
       int result= shippingMapper.updateBySelectiveKey(shipping);
        if (result>0){
            return ServerResponse.serverResponseBySuccess();
        }


        //3：返回结果


        return ServerResponse.serverResponseByError("更新失败");
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        //1：参数非空校验
        if (shippingId==null){
            return  ServerResponse.serverResponseByError("参数错误");
        }
        //2：
        Shipping shipping=shippingMapper.selectByPrimaryKey(shippingId);
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectAll();
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
