package com.tz.controller.portal;

import com.tz.common.ServerResponse;
import com.tz.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    /**
     * 商品详情
     */
    @Autowired
    IProductService productService;
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId){



        return  productService.detail_portal(productId);
    }


    /**
     *resetful风格的url路径;http://localhost:8080/shop/product/detail/productId/1把所有的请求定义为网络资源，当请求参数过多不适合resetful风格
     */
    @RequestMapping(value = "/detail/productId/{productId}")
    public ServerResponse detailRestful(@PathVariable("productId") Integer productId){



        return  productService.detail_portal(productId);
    }


    /**
     *查询商品列表
     * @return
     */


    @RequestMapping(value = "/list")
    public ServerResponse list(
            @RequestParam(required = false)  Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize,
            @RequestParam(required = false,defaultValue = "") String orderby
    ){
        return  productService.list_potal(categoryId,keyword,pageNum,pageSize,orderby);
    }

    /***
     * resetful风格的前台-搜索商品并且排序，通过categoryId查询，由于不管通过categoryId查询还是
     * 通过关键字查询，两者传的参数格式相似，所以会形成模糊的查询格式造成服务器不知道调用哪个方法
     * 所以要在各自的方法前加相关的  自定义资源占位  来区分模棱两可的方法
     */

    /**resetful风格的前台-搜索商品并且排序,通过关键字查询
     */
    @RequestMapping(value = "/list/categoryId/{categoryId}/{pageNum}/{pageSize}/{orderby}")
    public ServerResponse listresetfulBycategoryId(
            @PathVariable("categoryId") Integer categoryId,

            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("orderby") String orderby
    ){
        return  productService.list_potal(categoryId,null,pageNum,pageSize,orderby);
    }




    /***
     * resetful风格的前台-搜索商品并且排序,通过关键字查询
     */
    @RequestMapping(value = "/list/keyword/{keyword}/{pageNum}/{pageSize}/{orderby}")
    public ServerResponse listBykeyword(@PathVariable("categoryId") Integer categoryId,
                                        @PathVariable("keyword") String keyword,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize,
                                        @PathVariable("orderby") String orderby
    ){
        return  productService.list_potal(categoryId,keyword,pageNum,pageSize,orderby);
    }


}
