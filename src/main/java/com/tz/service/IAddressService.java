package com.tz.service;

import com.tz.common.ServerResponse;
import com.tz.pojo.Shipping;

public interface IAddressService {


    /**
     * 添加用户地址
     */
    public ServerResponse add(Integer userId, Shipping shipping);


    /**
     * 删除用户地址
     */
    public ServerResponse delete(Integer userId, Integer shippingId);

    /**
     *
     * 更新
     */
    public ServerResponse update(Shipping shipping);

    /**
     * 查看
     */
    ServerResponse select(Integer shippingId);


    /**
     * 分页查询
     */
    ServerResponse list(Integer pageNum,Integer pageSize);
}
