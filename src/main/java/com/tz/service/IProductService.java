package com.tz.service;

import com.tz.common.ServerResponse;
import com.tz.pojo.Product;

import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    /**
     * 保存或者更新产品
     *
     * @param product
     * @return
     */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 商品的上下架
     */
    ServerResponse set_sale_status(Integer productId, Integer status);


    /**
     * 商品的详情
     */
    ServerResponse detail(Integer productId);


    /**
     * 后台查询商品列表
     */
    ServerResponse list(Integer pageNum, Integer pageSize);

    /**
     * 后台搜索商品
     */
    ServerResponse search(Integer pageNum, Integer pageSize, Integer productId, String productName);

    /**
     * 图片上传
     */
    ServerResponse upload(MultipartFile file, String path);

    /**
     * 前台商品列表
     */
    ServerResponse detail_portal(Integer productId);

    /**
     * 前台商品搜索
     */
    ServerResponse list_potal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize,String orderby);

}