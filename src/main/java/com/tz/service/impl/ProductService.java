package com.tz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.dao.CategoryMapper;
import com.tz.dao.ProductMapper;
import com.tz.pojo.Category;
import com.tz.pojo.Product;
import com.tz.service.IProductService;
import com.tz.utils.DateUtils;
import com.tz.utils.FTPUtil;
import com.tz.utils.PropertiesUtils;
import com.tz.vo.ProductDetailVO;
import com.tz.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductService implements IProductService
{

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryService categoryService;
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //1:参数的非空校验
        if (product==null){
            ServerResponse.serverResponseByError("参数为空");
        }

        //2：是指商品的主图
        String subImages=product.getSubImages();
        if (subImages!=null&&!subImages.equals("")){
            String[] subImagearry=subImages.split(",");
            if (subImagearry.length>0){
                product.setMainImage(subImagearry[0]);
            }
        }

        //3：根据id是否存在判断时添加还是更新
        if (product.getId()==null) {//添加
            int result = productMapper.insert(product);
            if (result > 0) {
                ServerResponse.serverResponseBySuccess();
            } else {
                ServerResponse.serverResponseByError("添加失败");
            }
        } else{
            int result = productMapper.updateByPrimaryKey(product);
            if (result > 0) {
                ServerResponse.serverResponseBySuccess();
            } else {
                ServerResponse.serverResponseByError("更新失败");
            }
        }

        return null;
    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        // 1:参数的非空校验
        if (productId==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }
        if (status==null){
            ServerResponse.serverResponseByError("商品状态不能为空");
        }

        //2：更新商品的状态
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int result =productMapper.updateProductKeySelective(product);

        //3：返回结果
        if(result>0){
           return ServerResponse.serverResponseBySuccess("更新成功");
        }else {
            return  ServerResponse.serverResponseByError("更新失败");
        }


    }

    @Override
    public ServerResponse detail(Integer productId) {
        //1:参数校验
        if (productId==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }

        //2：根据id查详情product
        Product product= productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return  ServerResponse.serverResponseByError("商品不存在");
        }
        //3：将product转为productdetailvo
        ProductDetailVO productDetailVO=assenbleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }


    private ProductDetailVO assenbleProductDetailVO(Product product){

        ProductDetailVO  productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getId());
        productDetailVO.setCreateTime(DateUtils.dateTostr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readKey("imageKey"));//图片都放在服务器上 为了避免修改服务器造成的麻烦，吧地址放在配置文件中，这样只需要修改配置文件
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImages(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateTostr(product.getUpdateTime()));

        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            productDetailVO.setParentCategoryId(0);
        }
        return  productDetailVO;
    }


    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //1：有默认值不要非空判断
        PageHelper.startPage(pageNum,pageSize);//利用springaop，必须放在查询语句之前
        // /2:查询商品数据,使用分页插件，加上后sql语句相当于：select * from product limit(pageNum-1)*pageSize,pageSize
        List<Product>  productlist=productMapper.selectAll();
        List<ProductListVO> productListVOList= Lists.newArrayList();
        if (productlist!=null&&productlist.size()>0){
            for (Product product:productlist){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //分页对象插件提供
        PageInfo pageInfo=new PageInfo(productListVOList);
        return  ServerResponse.serverResponseBySuccess(pageInfo);
    }



    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImages(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setSubtitle(product.getSubtitle());
        return  productListVO;
    }



    @Override
    public ServerResponse search(Integer pageNum, Integer pageSize, Integer productId, String productName) {
        PageHelper.startPage(pageNum,pageSize);
        //还是分页查询
        if (productName!=null&&!productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product>  productList=productMapper.findPorductByProductNameAndProductId(productId,productName);
        List<ProductListVO> productListVOList= Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //分页对象插件提供
        PageInfo pageInfo=new PageInfo(productListVOList);
        return  ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if (file==null){
            return  ServerResponse.serverResponseByError("文件为空");
        }
        //1：获取图片名称,因为不同的用户可能上传相同文件名的图片，所以要生成为一个文件名，防止相同图片文件被覆盖
        String originFileName=file.getOriginalFilename();
        //2:获取图片扩展名
        String exName = originFileName.substring(originFileName.lastIndexOf("."));//截取扩展名
        //3：为图片生成新的唯一的名字
        String newFileName=UUID.randomUUID().toString()+exName;
        File pathFile=new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName);

        try {
            file.transferTo(file1);

            FTPUtil.uploadFile(Lists.<File>newArrayList(file1));

            //上传图片服务器
            Map<String,String> map= Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readKey("imageHost")+"/"+newFileName);


            //删除应用服务器的图片
            //file1.delete();
            return  ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ServerResponse detail_portal(Integer productId) {

        //1:参数校验
        if (productId==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }

        //2：根据id查详情product
        Product product= productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return  ServerResponse.serverResponseByError("商品不存在");
        }
       //3：校验商品状态
        if (product.getStatus()!=Const.ProductStatusEnm.PRODUCT_ONLINE.getCode()){
            return  ServerResponse.serverResponseByError("商品下架");
        }
        //4:获取商品详情的VO
        ProductDetailVO productDetailVO=assenbleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse list_potal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize,String orderby) {
        //1：参数校验，categoryid和keyword不能铜时空
            if (categoryId==null&&(categoryId==null||keyword.equals(""))){
                return  ServerResponse.serverResponseByError("参数错误");
            }

        //2：根据categoryid查询
            if (categoryId!=null){
                Category category=categoryMapper.selectByPrimaryKey(categoryId);
                if (category==null&&(keyword==null||keyword.equals(""))){
                   //说明商品不存在
                   PageHelper.startPage(pageNum,pageSize);
                   List<ProductListVO> productListVOList=Lists.newArrayList();
                   PageInfo pageInfo=new PageInfo(productListVOList);
                   return  ServerResponse.serverResponseBySuccess(pageInfo);
                }
               ServerResponse serverResponse= categoryService.get_deep_category(categoryId);
                Set<Integer> integerSet= Sets.newHashSet();
                if (serverResponse.isSuccess()){
                   integerSet= (Set<Integer>) serverResponse.getData();

                }
                if (keyword!=null&&keyword.equals("")){
                    keyword="%"+keyword+"%";
                }
                if (orderby==null){
                    PageHelper.startPage(pageNum,pageSize);
                }else{
                    String[] orderByArr=orderby.split("_");
                    if (orderByArr.length>1){
                        PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);

                    }else{
                        PageHelper.startPage(pageNum,pageSize);

                    }
                }//
                //4:


                List<Product> productList=productMapper.searchProduct(integerSet,keyword);
                List<ProductListVO> productListVOList=Lists.newArrayList();
                if (productList!=null&&productList.size()>0){
                    for (Product product:productList){
                        ProductListVO productListVO=assembleProductListVO(product);
                        productListVOList.add(productListVO);
                    }
                }
                PageInfo pageInfo=new PageInfo();
                pageInfo.setList(productListVOList);


                return ServerResponse.serverResponseBySuccess(pageInfo);
        }



        return null;
    }

}
