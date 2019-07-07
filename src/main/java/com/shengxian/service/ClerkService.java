package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.Goods;
import com.shengxian.entity.WageSettlement;
import com.shengxian.entity.clerkApp.Calculator;
import com.shengxian.entity.clerkApp.ShoppingMall;
import com.shengxian.entity.clerkApp.ShoppingMallDateil;
import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 员工APP接口业务接口层
 *
 * @Author: yang
 * @Date: 2019-04-12
 * @Version: 1.0
 */
public interface ClerkService {

    /**
     * 查询员工APP版本号
     * @return
     * @throws Exception
     */
    String version()throws Exception;

    /**
     * 员工app中判断在是否有退出登录过
     * @param phone 手机号
     * @param model 手机型号
     * @param system  操作系统版本
     * @param version F: 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion  客户端基础库版本
     * @return
     */
    boolean appIsLogin(String token ,String phone ,String model,String system,String version ,String platform ,String SDKVersion)throws Exception;

    /**
     * 修改手机设备
     * @param token
     * @param model 手机型号
     * @param system 操作系统版本
     * @param version 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion 客户端基础库版本
     * @return
     */
    Integer updateEquipment(String token,String model,String system,String version,String platform ,String SDKVersion)throws Exception;

    /**
     * 共享订单
     * @param token
     * @param pageNo
     * @return
     * @throws Exception
     */
    Page sharingOrder(String token ,Integer role , Integer pageNo ,String name , String number)throws NullPointerException, Exception;



    /**
     * 订单详情
     * @param order_id 订单ID
     * @return
     * @throws Exception
     */
    HashMap orderDetail( Integer order_id )throws Exception ;

    /**
     * 未到货的订单
     * @param token
     * @param pageNo
     * @return
     * @throws Exception
     */
    Page noArrivedOrder(String token ,Integer role ,Integer pageNo ,String name)throws Exception;



    /**
     * 未收款的订单
     * @param token
     * @param pageNo
     * @param name 搜索条件，订单编号 ，客户名称，客户编号
     * @throws Exception
     */
    Page uncollectedOrderList(String token ,Integer role ,Integer pageNo,String name )throws NullPointerException, Exception;

    /**
     * 欠款的订单
     * @param token
     * @param pageNo
     * @param name 搜索条件，订单编号 ，客户名称，客户编号
     * @throws Exception
     */
    Page arrearsOrderList(String token ,Integer role ,Integer pageNo,String name )throws NullPointerException, Exception;

    /**
     * 完成的订单
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    Page completeOrderList(String token ,Integer role ,Integer pageNo,String name,String startTime ,String endTime )throws NullPointerException, Exception;


    /**
     * 员工app功能列表
     * @param token
     * @return
     * @throws Exception
     */
    List<HashMap> staffFunctionList(String token)throws  Exception;

    /**
     * 客户列表（分页）
     * @param token
     * @param name
     * @return
     * @throws Exception
     */
    Page bindingUserList(String token  ,Integer role , Integer pageNo, Integer category_id ,String name)throws Exception;

    /**
     * 供应商列表（分页）
     * @param token
     * @param name
     * @return
     * @throws Exception
     */
    Page suppliersList(String token  ,Integer role , Integer pageNo, Integer category_id ,String name)throws Exception;

    /**
     * 已录入的订单
     * @param token
     * @param role
     * @param mold
     * @param type
     * @return
     */
    Page haveJoinedShoppingcart(String token , Integer role ,Integer pageNo ,Integer mold ,Integer type);




    /**
     * 通过客户ID查询客户信息
     * @param id
     * @return
     * @throws Exception
     */
    HashMap bindingUserById(Integer id)throws Exception;

    /**
     * 店铺产品类别
     * @param token
     * @param level
     * @return
     * @throws Exception
     */
    List<HashMap> busienssGoodsCategory(String token  ,Integer role,Integer level)throws Exception;

    /**
     * app店铺类别下的产品（客户）
     * @param token
     * @param pageNo
     * @param binding_id 绑定客户ID
     * @param category_id 类别ID
     * @param name 客户名称或编号
     * @return
     * @throws Exception
     */
    Page businessGoodsList( String token  ,Integer role,Integer pageNo,Integer binding_id ,Integer category_id,String name)throws Exception;

    /**
     * app店铺类别下的产品（供应商）
     * @param token
     * @param pageNo
     * @param suppliersId 供应商id
     * @param categoryId 类别ID
     * @param name 客户名称或编号
     * @return
     * @throws Exception
     */
    Page businessGoodsListSuppliers( String token  ,Integer role,Integer pageNo,Integer suppliersId,Integer categoryId,String name)throws Exception;


    /**
     * 加入购物车
     * @param token
     * @param binding_id 绑定客户ID
     * @param goods_id 产品ID
     * @param type 0销售产品，1赠送产品
     * @param price 产品单价
     * @param num 数量
     * @param mold  0销售 ，1采购
     * @return
     * @throws Exception
     */
    Integer addShoppingMall(String token , Integer role ,Integer binding_id,Integer goods_id ,Integer type ,Integer mold, Double price ,Double num)throws Exception;

    /**
     * 减掉购物车
     * @param sm_id 购物车ID
     * @param smd_id 购物车详情ID
     * @return
     * @throws Exception
     */
    Integer reduceShoppingMall(Integer sm_id ,Integer smd_id)throws NullPointerException, Exception;

    /**
     * 删除购物车产品
     * @param sm_id
     * @param smd_id
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer deleteShoppingMallDateil(Integer sm_id ,Integer smd_id  )throws NullPointerException, Exception;

    /**
     * 客户的购物车总数和总金额
     * @param token
     * @param binding_id 绑定客户id
     * @param mold 0销售，1采购
     * @return
     */
    HashMap currentBindingShoppingMallCountAndMoney(String token ,Integer role ,Integer binding_id ,Integer mold)throws Exception;

    /**
     * 客户的购物车产品列表
     * @param sm_id 购物车ID
     * @return
     */
    List<ShoppingMallDateil> currentBindingShoppingMallGoodsList(Integer sm_id)throws Exception;

    /**
     * 结算
     * @param sm_id 购物车ID
     * @return
     * @throws Exception
     */
    ShoppingMall settlement(Integer sm_id)throws Exception;

    /**
     * 销售下订单
     * @param token
     * @param sm_id 购物车ID
     * @param freight 运费
     * @param difference_price 差价
     * @param beizhu 备注
     * @param print 0未打印，1打印
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer addOrder(String token ,Integer role ,Integer sm_id ,double freight ,double difference_price ,String beizhu  ,Integer print, Integer mold)throws NullPointerException, Exception;


    /**
     * 客户收藏的产品
     * @param token
     * @param pageNo
     * @param bindingId 客户id
     * @param name 产品名称
     * @return
     * @throws Exception
     */
    Page bindingCollectionBindingGoodsList(String token ,Integer role ,Integer pageNo,Integer bindingId ,String name) throws Exception;

    /**
     * 店铺收藏供应商的产品
     * @param token
     * @param pageNo
     * @param suppliersId 供应商ID
     * @param name 产品名称
     * @return
     * @throws Exception
     */
    Page bindingCollectionSuppliersGoodsList(String token ,Integer role ,Integer pageNo,Integer suppliersId ,String name) throws Exception;

    /**
     * 采购下订单
     * @param token
     * @param sm_id 购物车ID
     * @param freight 运费
     * @param difference_price 差价
     * @param beizhu 备注
     * @param print 0未打印，1打印
     * @return
     */
    Integer addPurchase(String token ,Integer role ,Integer sm_id,double freight ,double difference_price,String beizhu ,Integer print ,Integer mold)throws NullPointerException ,Exception;

    /**
     * 销售收款(默认进来数据不加载，通过扫一扫或搜索)
     * @param token
     * @param pageNo
     * @param name 客户名称或客户编号或标识码
     * @param number 订单编号
     * @return
     * @throws Exception
     */
    Page saleReceivables(String token , Integer role, Integer pageNo , String name  , String number)throws Exception;


    /**
     * 获取员工信息
     * @param token
     * @return
     * @throws Exception
     */
    HashMap myInfo(String token  ,Integer role)throws Exception;

    /**
     * 我的工资今日结算
     * @param token
     * @param role
     * @return
     * @throws Exception
     */
    HashMap mySalary(String token ,Integer role  ) throws Exception;

    /**
     * 我的工资本月结算
     * @param token
     * @param role
     * @return
     * @throws Exception
     */
    List<WageSettlement> wageSettlement(String token , Integer role ) throws Exception;

    /**
     * 提成分类
     * @param token
     * @param role
     * @param pageNo
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Page percentageClassification(String token ,Integer role ,Integer pageNo  ,Integer type ,String startTime ,String endTime);

    /**
     * 工作记录
     * @param token
     * @param role
     * @param pageNo
     * @param startTime
     * @param endTime
     * @return
     */
    Page payrollRecords(String token ,Integer role ,Integer pageNo ,String startTime ,String endTime);


    /**
     * 员工APP修改密码
     * @param token
     * @param role
     * @param password 旧密码
     * @param password2 新密码
     * @return
     */
    Integer updatePassword(String token ,Integer role ,String password ,String password2)throws NullPointerException ,Exception;

    /**
     * 客户账单
     * @param token
     * @param role
     * @param type
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    Page shareUserOrder(String token , Integer role ,Integer pageNo ,Integer type ,Integer bindingID,String name ,String goodsName ,String startTime , String endTime);


    /**
     * 客户账单分享到微信上
     * @param type
     * @param bindingID
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    String shareWXRecord(String token, Integer role ,Integer type ,Integer bindingID ,String name ,String goodsName ,String startTime , String endTime )throws NullPointerException;



    /**
     * 客户账单的微信分享
     * @param shareID
     * @return
     */
    HashMap userWXShareRecord(String shareID );


    /**
     * 订单的微信分享
     * @param orderID
     * @return
     */
    HashMap orderWXShareRecord(Integer orderID );

    /**
     * 员工APP提成标题提醒
     * @param token
     * @param role
     * @return
     */
    List ritleReminder(String token ,Integer role );

    /**
     * 采购产品汇总
     * @param token
     * @param role
     * @param pageNo
     * @param suppliersName
     * @param goodsName
     * @param startTime
     * @param endTime
     * @return
     */
    Page purchaseGoodsSummary(String token ,Integer role ,Integer pageNo,String suppliersName,String goodsName ,String startTime ,String endTime);

    /**
     * 采购产品明细
     * @param token
     * @param role
     * @param pageNo
     * @param suppliersName
     * @param goodsName
     * @param startTime
     * @param endTime
     * @return
     */
    Page purchaseGoodsDetails(String token ,Integer role ,Integer pageNo ,String suppliersName,String goodsName ,String startTime ,String endTime);

    /**
     * 添加计算器
     * @param calculator
     * @return
     */
    Integer addCalculator(String token , Integer role , Calculator calculator);

    /**
     * 查询计算器
     * @param token
     * @param role
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    Page selectCalculator(String token , Integer role ,Integer pageNo ,String name , String startTime ,String endTime);

    /**
     * 查询计算器详情
     * @param calculatorId
     * @return
     */
    List<HashMap> selectCalculatorDateilById(Integer calculatorId);

}
