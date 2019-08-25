package com.shengxian.mapper;

import com.shengxian.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-11
 * @Version: 1.0
 */
@Mapper
public interface TemporaryMapper {

    /**
     * 通过店铺id查询店铺产品二维码标识码icode
     * @param business_id
     * @return
     */
    HashMap selectBusinessIcode(@Param("id") Integer business_id);

    /**
     * 查询店铺类别
     * @param business_id 店铺id
     * @param level 二级类别时传一级类别id
     * @return
     */
    List<GoodsCategory> businessCategory(@Param("business_id") Integer business_id, @Param("level") Integer level);

    /**
     * 判断是大类别还是小类别
     * @param category_id 类别id
     * @return
     */
    Integer largeClassOrSmalClass(@Param("id") Integer category_id);

    /**
     * 店铺类别下的产品总数
     * @param search
     * @return
     */
    Integer businessGoodsCount(Search search);

    /**
     * 店铺类别下的产品列表
     * @param search
     * @return
     */
    List<IntegrGoods> businessGoods(Search search);

    /**
     * 通过识别码查询临时客户购物车id
     * @param tic
     * @return
     */
    Integer selectTShoppongcart(@Param("tic") String tic );

    /**
     * 添加临时客户购物车
     * @return
     */
    Integer addTShoppongcart(Shoppongcart shoppongcart);

    /**
     * 通过购物车id和产品id查询临时客户购物车详情里是否该产品
     * @param goods_id
     * @return
     */
    Integer selectTShoppingcartDetailIsGoodsId(@Param("tscId") Integer tscId  , @Param("goods_id") Integer goods_id);


    /**
     * 修改临时客户购物车详情的产品数量
     * @param id 购物车详情id
     * @param num 购买数量
     * @return
     */
    Integer updateTShoppingcartDetailNum(@Param("id") Integer id, @Param("num") Double num, @Param("type") Integer type);

    /**
     * 通过产品id查询临时售价
     * @param goodsId
     * @return
     */
    Double selectTemporaryPriceByGoodsId(@Param("id") Integer goodsId);

    /**
     * 添加临时客户购物车详情的产品数量
     * @return
     */
    Integer addTShoppingcartDetailNum(ShoppongcartDetail shoppongcartDetail);

    /**
     * 计算临时客户购物车所有产品的总金额
     * @param tscId 临时客户购物车id
     * @return
     */
    Double calculationTScAllGoodsTotalMoney(@Param("tscId") Integer tscId );

    /**
     * 修改临时客户购物车总金额
     * @param id 购物车id
     * @param money
     * @return
     */
    Integer updateTShoppingcartTotalMoney(@Param("id") Integer id, @Param("money") Double money);

    /**
     * 减少临时客户购物车详情里的产品数量
     * @param dateil_id
     * @return
     */
    Integer reduceTShoppingcartDetailGoodsNum(@Param("id") Integer dateil_id);

    /**
     * 通过临时客户购物车详情ID查询原来的产品数量
     * @param id 购物车详情id
     * @return
     */
    HashMap selectTShoppingcartDetailGoodsNum(@Param("id") Integer id);

    /**
     * 删除临时客户购物车详情id
     * @param id
     * @return
     */
    Integer deleteTShoppingcartDetailId(@Param("id") Integer id);

    /**
     * 通过购物车id查询临时客户购物车下是否还有产品
     * @param tscId
     * @return
     */
    List<Integer> findTShoppingcartIsGoods(@Param("tscId") Integer tscId);

    /**
     * 删除临时客户购物车
     * @param id
     * @return
     */
    Integer deleteTShoppingcart(@Param("id") Integer id);

    /**
     * 通过店铺id查询店铺名称
     * @param business_id
     * @return
     */
    String findStartingPriceAndStoreName(@Param("id") Integer business_id);

    /**
     * 通过临时客户识别码查询购物车id
     * @param tic
     * @return
     */
    Integer selectTShoppingcart(@Param("tic") String tic);

    /**
     * 通过临时客户购物车id查询购物车下的产品总数
     * @param tscId
     * @return
     */
    Integer selectTShoppingcartGoodsDetailGoodsCount(@Param("tscId") Integer tscId);

    /**\
     * 根据购物车id和客户识别码查询总金额
     * @param tscId
     * @param tic
     * @return
     */
    Double selectShoppingcartMoney(@Param("id") Integer tscId ,@Param("tic") String tic);

    /**
     * 通过临时客户购物车id查询购物车下的产品信息
     * @param tscId
     * @return
     */
    List<HashMap> selectTShoppingcartGoodsDetail(@Param("tscId") Integer tscId ,@Param("business_id") Integer business_id);




    /**
     * 结算
     * @param tscdId 购物车详情id
     * @param business_id 店铺id
     * @return
     */
    HashMap settlement(@Param("id") Integer tscdId,@Param("business_id") Integer business_id);


    /**
     * 通过店铺id查询店铺是否有扫码客户账号名称
     * @param business_id
     * @return
     */
    Integer selectScanBinding(Integer business_id);


    /**
     * 根据临时客户购物车id和产品id删除购物车详情
     * @param tscId 购物车id
     * @param goods_id 产品id
     * @return
     */
    Integer deteleTShoppingCartDetail(@Param("tscId") Integer tscId, @Param("goods_id") Integer goods_id);


}
