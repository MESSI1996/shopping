package com.tz.service.impl;

import com.google.common.collect.Lists;
import com.tz.common.Const;
import com.tz.common.ServerResponse;
import com.tz.dao.CartMapper;
import com.tz.dao.ProductMapper;
import com.tz.pojo.Cart;
import com.tz.pojo.Product;
import com.tz.service.ICartService;
import com.tz.utils.BigDecimalUtils;
import com.tz.vo.CartProductVO;
import com.tz.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService implements ICartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //1:参数的非空校验
            if (productId==null||count==null){
                return  ServerResponse.serverResponseByError("参数不能为空");
            }
            Product product=productMapper.selectByPrimaryKey(productId);
            if (product==null){
               return ServerResponse.serverResponseByError("添加的商品不存在");
            }

        //2：根据productid和userid查询购物信息
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart == null) {
            //添加
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1=new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked() );
            cartMapper.updateByPrimaryKey(cart1);


        }

        CartVO cartVO=getCartVOLimit(userId);


        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO=getCartVOLimit(userId);
        return  ServerResponse.serverResponseBySuccess(cartVO);



    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //1:参数的判断
        if (productId==null||count==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }

        //2：查询购物车商品
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            //3:更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        //4：返回cartvo
        return  ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));




    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //1:参数的非空校验
        if (productIds==null||productIds.equals("")){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }

        //2：productids转为list集合
        List<Integer> productIdList=Lists.newArrayList();
        String[] productIdsArr=productIds.split(",");
        if (productIds!=null&&productIdsArr.length>0){
            for (String productIdstr:productIdsArr){
                Integer productId=Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }

        //3：调用dao层删除
        cartMapper.deleteByUseridAndProductIds(userId,productIdList);
        //4：返回结果
        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //1：非空校验
      /*  if (productId==null||productId.equals("")){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }*/

        //2：调用dao接口
            cartMapper.selectOrsUnelectProduct(userId,productId,check);


        //3：返回结果
        return  ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));

    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int result=cartMapper.get_cart_product_count(userId);


        return ServerResponse.serverResponseBySuccess(result);
    }

    private  CartVO getCartVOLimit(Integer userId){
        CartVO cartVO=new CartVO();
        //1：根据userid查询购物信息
        List <Cart> cartList=cartMapper.selectCartByUserid(userId);
        //2：将List<Cart> 转为 List<CartProductVO>
        List<CartProductVO> cartProductVOList=Lists.newArrayList();

        //购物车总价格
        BigDecimal carttotalprice=new BigDecimal("0");

        if (cartList!=null&&cartList.size()>0){
            for (Cart cart:cartList){
                    CartProductVO cartProductVO=new CartProductVO();
                    cartProductVO.setId(cart.getId());
                    cartProductVO.setQuantity(cart.getQuantity());
                    cartProductVO.setUserId(userId);
                    cartProductVO.setProductChecked(cart.getChecked());
                  //查询商品,因为商品的信息都在商品表中，所以要先查商品
                 Product product= productMapper.selectByPrimaryKey(cart.getProductId());
                 if (product!=null){
                     cartProductVO.setProductId(cart.getProductId());
                     cartProductVO.setProductMainImage(product.getMainImage());
                     cartProductVO.setProductName(product.getName());
                     cartProductVO.setProductPrice(product.getPrice());
                     cartProductVO.setProductStatus(product.getStatus());
                     cartProductVO.setProductStoock(product.getStock());
                     cartProductVO.setProductSubtitle(product.getSubtitle());


                     int stock=product.getStock();
                     int limitProductCount=0;
                     if (stock>=cart.getQuantity()){//库存大于要买的数量
                         limitProductCount=cart.getQuantity();//要多少买多少
                         cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                     }else {
                         //库存小于购买数量
                         limitProductCount=stock;//最多买到和库存一样的
                         //此时需要更新购物车中商品的数量


                         Cart cart1=new Cart();
                         cart1.setId(cart.getId());
                         cart1.setQuantity(stock);
                         cart1.setProductId(cart.getProductId());
                         cart1.setChecked(cart.getChecked());
                         cart1.setUserId(userId);
                            cartMapper.updateByPrimaryKey(cart1);

                         cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                     }
                     cartProductVO.setQuantity(limitProductCount);
                     cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));

                }
             carttotalprice=BigDecimalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());


                 cartProductVOList.add(cartProductVO);
            }
        }

        cartVO.setCartProductVOList(cartProductVOList);
            //3：计算总价格
        cartVO.setCarttotalprice(carttotalprice);
            //4：判断购物车是否全选
            int count= cartMapper.isCheckedAll(userId);
            if (count>0){
                cartVO.setIsallchecked(false);
            }else {
                cartVO.setIsallchecked(true);
            }

            //5:返回结果
            return  cartVO;
    }
}
