package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.GoodsCategory;
import com.shengxian.entity.Order;
import com.shengxian.entity.ShoppingHashMap;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-11
 * @Version: 1.0
 */
public interface TemporaryService {

    /**
     * 查询店铺类别
     * @param business_id
     * @param level
     * @param icode
     * @return
     */
    List<GoodsCategory> businessCategory(Integer business_id, Integer level ,Integer icode)throws NullPointerException;

    /**
     * 店铺类别下的产品
     * @param pageNo
     * @param business_id
     * @param category_id
     * @param name
     * @return
     */
    Page businessGoods( Integer pageNo, Integer business_id, Integer category_id, String name)throws Exception;


    /**
     * 加入购物车
     * @param tic
     * @param goods_id
     * @param num
     * @param type
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer addShoppingCart(String tic , Integer goods_id, Double num, Integer type)throws NullPointerException ,Exception;

    /**
     * 减掉购物车
     * @param tscdId 购物车详情ID
     * @return
     */
    Integer reduceShoppingCart( Integer tscdId)throws NullPointerException ,Exception;

    /**
     * 获取购物车总数
     * @param tic
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer temporaryShoppingcartMoneyAndCount( String tic)throws NullPointerException ,Exception;



    /**
     * 当前临时客户的购物车
     * @param business_id 店铺id
     * @param tic 临时客户识别码
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    HashMap temporaryShoppingcart(Integer business_id , String tic)throws NullPointerException ,Exception;

    /**
     * 删除购物车产品
     * @param tscdId
     * @return
     */
    Integer deleteShoppingcartDateil( Integer tscId ,Integer tscdId);

    /**
     * 结算
     * @param tic
     * @param tscdId 购物车详情id
     * @param business_id 店铺ID
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    HashMap settlement(String tic, String tscdId,  Integer business_id)throws NullPointerException ,Exception;

    /**
     * 下订单
     * @param
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer addOrder(String tic , Integer business_id , Integer tscId)throws NullPointerException ,Exception;

}
