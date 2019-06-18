package com.shengxian.mapper;

import com.shengxian.entity.*;
import com.shengxian.entity.clerkApp.Clerk;
import com.shengxian.entity.clerkApp.ShoppingMall;
import com.shengxian.entity.clerkApp.ShoppingMallDateil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-04-12
 * @Version: 1.0
 */
@Mapper
public interface ClerkMapper {

    /**
     * 查询员工APP版本号
     * @return
     */
    String version();

    /**
     * app中判断在是否有退出登录过
     * @param phone
     * @param model
     * @param system
     * @param version
     * @param platform
     * @param SDKVersion
     * @return
     */
    Integer appIsLogin(@Param("token") String token , @Param("phone") String phone , @Param("model") String model, @Param("system") String system, @Param("version") String version , @Param("platform") String platform , @Param("SDKVersion") String SDKVersion);


    /**
     * 修改手机设备
     * @param token
     * @param model
     * @param system
     * @param version
     * @param platform
     * @param SDKVersion
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer  updateEquipment(@Param("token") String token,@Param("model") String model,@Param("system") String system,@Param("version") String version,@Param("platform") String platform ,@Param("SDKVersion") String SDKVersion);


    /**
     * 查询共享订单总数
     * @param business_id
     * @return
     */
    Integer sharingOrderCount(@Param("business_id") Integer business_id);

    /**
     * 查询共享订单集合
     * @param business_id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> sharingOrder(@Param("business_id") Integer business_id , @Param("startIndex") Integer startIndex , @Param("pageSize") Integer pageSize);

    /**
     * 通过订单ID查询订单信息
     * @param order_id 订单ID
     * @return
     */
    HashMap orderInfo(@Param("id") Integer order_id);

    /**
     *通过订单ID查询订单详情的产品信息
     * @param order_id 订单ID
     * @return
     */
    List<HashMap> orderDateilGoods(@Param("order_id") Integer order_id);

    /**
     * 通过token查询商家ID和员工ID
     * @param token
     * @return
     */
    Clerk bidAndSidByToken(@Param("token") String token);

    /**
     * 未到货的订单总数
     * @return
     */
    Integer noArrivedOrderCount(AppParameter appParameter);

    /**
     * 未到货的订单
     * @return
     */
    List<HashMap> noArrivedOrder(AppParameter appParameter);

    /**
     * 未到货的订单总金额
     * @param appParameter
     * @return
     */
    Double noArrivedOrderTotalMoney(AppParameter appParameter);


    /**
     * 未收款的订单总数
     * @param appParameter
     * @return
     */
    Integer uncollectedOrderListCount(AppParameter appParameter);

    /**
     * 未收款的订单
     * @param appParameter
     * @return
     */
    List<HashMap> uncollectedOrderList(AppParameter appParameter);

    /**
     * 未收款的订单总金额
     * @param appParameter
     * @return
     */
    Double uncollectedOrderListTotalMoney(AppParameter appParameter);

    /**
     * 欠款的订单总数
     * @param appParameter
     * @return
     */
    Integer arrearsOrderListCount(AppParameter appParameter);

    /**
     * 欠款的订单
     * @param appParameter
     * @return
     */
    List<HashMap> arrearsOrderList(AppParameter appParameter);

    /**
     * 欠款的订单总金额
     * @param appParameter
     * @return
     */
    Money arrearsOrderListTotalMoney(AppParameter appParameter);


    /**
     * 完成的订单总数
     * @param appParameter
     * @return
     */
    Integer completeOrderListCount(AppParameter appParameter);

    /**
     * 完成款的订单
     * @param appParameter
     * @return
     */
    List<HashMap> completeOrderList(AppParameter appParameter);

    /**
     * 完成的订单总金额
     * @param appParameter
     * @return
     */
    Money completeOrderListTotalMoney(AppParameter appParameter);

    /**
     * 通过员工ID查询员工APP功能菜单列表
     * @param staff_id
     * @return
     */
    List<HashMap> staffFunctionList(@Param("staff_id") Integer staff_id);


    /**
     * 客户列表总数
     * @param appParameter
     * @return
     */
    Integer bindingUserListCount(AppParameter appParameter);

    /**
     * 客户列表
     * @param appParameter
     * @return
     */
    List<HashMap> bindingUserList(AppParameter appParameter);


    /**
     * 供应商列表总数
     * @param appParameter
     * @return
     */
    Integer suppliersListCount(AppParameter appParameter);

    /**
     *供应商列表
     * @param appParameter
     * @return
     */
    List<HashMap> suppliersList(AppParameter appParameter);


    /**
     * 已录入的订单总数
     * @param id
     * @param mold
     * @param type
     * @return
     */
    Integer haveJoinedShoppingcartCount(@Param("id") Integer id , @Param("mold") Integer mold,@Param("type") Integer type);

    /**
     * 已录入的订单
     * @param id
     * @param mold
     * @param type
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> haveJoinedShoppingcart(@Param("id") Integer id , @Param("mold") Integer mold,@Param("type") Integer type ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);


    /**
     * 通过客户ID查询客户信息
     * @param id
     * @return
     * @throws Exception
     */
    HashMap bindingUserById(Integer id)throws Exception;

    /**
     * 店铺产品类别
     * @param business_id 店铺ID
     * @param level 级别
     * @return
     */
    List<HashMap> busienssGoodsCategory(@Param("business_id") Integer business_id ,@Param("level") Integer level);

    /**
     * 判断是大类别还是小类别
     * @param category_id 类别id
     * @return
     */
    Integer largeClassOrSmalClass(@Param("id") Integer category_id);

    /**
     * app店铺类别下的产品总数（客户）
     * @param appParameter
     * @return
     */
    Integer businessGoodsListCount(AppParameter appParameter);

    /**
     *app店铺类别下的产品（客户）
     * @param appParameter
     * @return
     */
    List<HashMap> businessGoodsList(AppParameter appParameter);

    /**
     * app店铺类别下的产品总数（供应商）
     * @param appParameter
     * @return
     */
    Integer businessGoodsListSuppliersCount(AppParameter appParameter);

    /**
     *app店铺类别下的产品（供应商）
     * @param appParameter
     * @return
     */
    List<HashMap> businessGoodsListSuppliers(AppParameter appParameter);


    /**
     * 通过员工ID和绑定客户ID查询客户是否有购物车id
     * @param op_id 根据type来判断是员工ID还是店铺ID
     * @param binding_id 绑定客户ID
     * @param mold 0录入订单，1采购入库
     * @return
     */
    Integer selectShoppingMall(@Param("role") Integer role ,@Param("op_id") Integer op_id, @Param("binding_id") Integer binding_id , @Param("mold") Integer mold );

    /**
     * 添加购物车
     * @param shoppingMall
     * @return
     */
    Integer addShoppingMall(ShoppingMall shoppingMall);

    /**
     * 通过shoppingMall_id和goods_id和type和mold查询购物车详情里是否存在
     * @param sm_id 购物车ID
     * @param goods_id 产品ID
     * @param type 0销售产品，1赠送产品
     * @param mold 0录入订单，1采购入库
     * @return
     */
    Integer selectShoppingMallDateil(@Param("sm_id") Integer sm_id ,@Param("goods_id") Integer goods_id ,@Param("type") Integer type,@Param("mold") Integer mold);

    /**
     * 添加购物车详情
     * @param shoppingMallDateil
     * @return
     */
    Integer addShoppingMallDateil(ShoppingMallDateil shoppingMallDateil);

    /**
     * 通过购物车详情ID加购物车产品数量
     * @param smd_id 购物车详情id
     * @return
     */
    Integer updateShoppingMallDateilPlusNum(@Param("id") Integer smd_id ,@Param("num") Double num);

    /**
     * 计算加入购物车里产品的总金额（数量乘以单价）
     * @param sm_id 购物车ID
     * @return
     */
    Double selectShoppingMallTotalMoney(@Param("sm_id") Integer sm_id  );

    /**
     * 修改购物车里的总金额
     * @param sm_id 购物车ID
     * @param money 购物车总金额
     * @return
     */
    Integer updateShoppingMallMoney(@Param("id") Integer sm_id,@Param("money") Double money);


    /**
     * 通过购物车详情ID减掉购物车产品数量
     * @param smd_id 购物车详情ID
     * @return
     */
    Integer updateShoppingMallDateilNum(@Param("id") Integer smd_id);

    /**
     * 通过购物车详情ID查询购物车产品数量
     * @param smd_id 购物车详情ID
     * @return
     */
    Double selectShoppingMallDateilNum(@Param("id") Integer smd_id);

    /**
     * 删除购物车详情ID
     * @param smd_id 购物车详情ID
     * @return
     */
    Integer deleteShoppingMallDateil(@Param("id") Integer smd_id);

    /**
     * 通过购物车id查询购物车详情下是否还有产品
     * @param sm_id 购物车ID
     * @return
     */
    List<Integer> selectShoppingMallDateilIsGoods(@Param("sm_id") Integer sm_id);



    /**
     * 删除购物车
     * @param sm_id 购物车ID
     * @return
     */
    Integer deleteShoppingMall(@Param("id") Integer sm_id);

    /**
     * 通过员工ID和客户绑定ID查询购物车信息
     * @param op_id 员工ID
     * @param binding_id 客户绑定ID
     * @param mold 0入库，1采购
     * @return
     */
    HashMap selectShoppingMallInfo(@Param("role") Integer role , @Param("op_id") Integer op_id, @Param("binding_id") Integer binding_id , @Param("mold") Integer mold );

    /**
     * 通过购物车ID删除购物车详情
     * @param sm_id 购物车ID
     * @return
     */
    Integer deleteShoppingMallDateilBySmId(@Param("sm_id") Integer sm_id);

    /**
     * 通过购物车ID查询购物车详情总数
     * @param sm_id 购物车ID
     * @return
     */
    Integer selectShoppingMallDateCount(@Param("sm_id") Integer sm_id);

    /**
     * 通过购物车ID查询购物车总金额
     * @param sm_id 购物车ID
     * @return
     */
    ShoppingMall findShoppingMallTotalMoney(@Param("id") Integer sm_id);

    /**
     * 通过购物车ID查询购物车详情产品列表
     * @param sm_id 购物车ID
     * @return
     */
    List<ShoppingMallDateil> currentBindingShoppingMallGoodsList(@Param("sm_id") Integer sm_id);

    /**
     * 通过购物车ID查询购物车详情
     * @param sm_id 购物车ID
     * @param // 0录入订单，1采购入库
     * @return
     */
    List<HashMap> ShoppingMallGoodsListBySMID(@Param("sm_id") Integer sm_id );

    /**
     * 通过token查询员工姓名店铺ID
     * @param token
     * @return
     */
    HashMap staffNameAndBusiness_id(@Param("token") String token);


    /**
     * 根据产品id查询产品进价
     * @param goods_id
     * @return
     */
    double findGoodsCostPrice(Integer goods_id);




    /**
     * 修改报损单的订单详情id
     * @param gid 报损id
     * @param detail_id 订单详情id
     * @return
     */
    Integer updateLoss(@Param("id") Integer gid,@Param("order_id") Integer detail_id);

    /**
     * 客户收藏的产品总数
     * @param businessId 店铺ID
     * @param bindingId 客户id
     * @return
     */
    Integer bindingCollectionBindingGoodsListCount(@Param("businessId") Integer businessId ,@Param("bindingId") Integer bindingId ,@Param("schemeId") Integer schemeId, @Param("name") String name  ,@Param("inv") byte inv );

    /**
     * 客户收藏的产品
     * @param businessId 店铺ID
     * @param bindingId  供应商ID
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> bindingCollectionBindingGoodsList(@Param("businessId") Integer businessId ,@Param("bindingId") Integer bindingId ,@Param("schemeId") Integer schemeId,@Param("name") String name  ,@Param("inv") byte inv  ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);


    /**
     * 店铺收藏供应商的产品总数
     * @param businessId 店铺ID
     * @param suppliersId 供应商ID
     * @return
     */
    Integer bindingCollectionSuppliersGoodsListCount(@Param("businessId") Integer businessId ,@Param("suppliersId") Integer suppliersId,@Param("name") String name ,@Param("shield") byte shield, @Param("inv") byte inv );

    /**
     * 店铺收藏供应商的产品
     * @param businessId 店铺ID
     * @param suppliersId  供应商ID
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> bindingCollectionSuppliersGoodsList(@Param("businessId") Integer businessId ,@Param("suppliersId") Integer suppliersId,@Param("name") String name  ,@Param("shield") byte shield, @Param("inv") byte inv,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);


    /**
     * 销售收款总数(默认进来数据不加载，通过扫一扫或搜索)
     * @param paramt
     * @return
     */
    Integer saleReceivablesCount(Paramt paramt);

    /**
     * 销售收款(默认进来数据不加载，通过扫一扫或搜索)
     * @param paramt
     * @return
     */
    List<HashMap> saleReceivables(Paramt paramt);

    /**
     * 获取我的信息
     * @param token
     * @return
     */
    HashMap myInfo(@Param("token") String token );

    /**
     * 销售单和采购单总数
     * @param staff_id
     * @return
     */
    Integer saleAndPurchaseOrderCount(@Param("staff_id") Integer staff_id);


    /**
     * 10种总提成统计
     * @param staff_id
     * @return
     */
    Double statisMoneyCount(@Param("staff_id") Integer staff_id);

    /**
     * 工资结算
     * @param staff_id
     * @return
     */
    List<WageSettlement> wageSettlement(@Param("staff_id") Integer staff_id);

    /**
     * 提成分类总数
     * @param id
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Integer percentageClassificationCount(@Param("id") Integer id , @Param("type") Integer type, @Param("startTime") String startTime,@Param("endTime") String endTime );

    /**
     * 提成分类
     * @param id
     * @param type
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> percentageClassification(@Param("id") Integer id , @Param("type") Integer type, @Param("startTime") String startTime,@Param("endTime") String endTime ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);

    /**
     * 提成分类总金额
     * @param id
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Double percentageClassificationTotalMoney(@Param("id") Integer id , @Param("type") Integer type, @Param("startTime") String startTime,@Param("endTime") String endTime );



    /**
     * 客户账单总数
     * @param paramt
     * @return
     */
    Integer shareUserOrderCount(Paramt paramt);

    /**
     * 客户账单
     * @param paramt
     * @return
     */
    List<HashMap> shareUserOrder(Paramt paramt);

    /**
     * 客户账单统计
     * @param paramt
     * @return
     */
    HashMap shareUserOrderTotalMoeny(Paramt paramt);


    /**
     * 分享到微信上的订单
     * @param paramt
     * @return
     */
    List<Integer> shareWXRecord(Paramt paramt);

    /**
     * 客户账单分享到微信上总金额
     * @param paramt
     * @return
     */
    Double userSaleDetailDownloadTatolMoney(Paramt paramt);


    /**
     * 添加到微信分享记录表里
     * @param id
     * @param shareId
     * @param endTime
     * @return
     */
    Integer addWXShareRecord(@Param("id") String id ,@Param("bindingId") Integer bindingId , @Param("shareId") String shareId  , @Param("price") Double price , @Param("startTime") String startTime,  @Param("endTime") String endTime);


    /**
     * 客户账单的微信分享
     * @param id
     * @return
     */
    HashMap selectUserWXShareRecord(@Param("id") String id);

    /**
     * 客户账单的微信分享商品明细
     * @param id
     * @return
     */
    List<HashMap> selectWXShareGoodDetail(@Param("id") String id);

    /**
     * 订单的微信分享
     * @param id
     * @return
     */
    HashMap orderWXShareRecord(Integer id);




}
