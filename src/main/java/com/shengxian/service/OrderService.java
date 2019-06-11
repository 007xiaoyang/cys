package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.Expense;
import com.shengxian.entity.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.List;

public interface OrderService {

    /**
     * 创建订单编号
     * @param token
     * @return
     */
    HashMap createOrder(String token ,Integer role );

    /**
     * 搜索商家用户
     * @param token
     * @param name
     * @return
     * @throws NullPointerException
     */
    List<HashMap> selectBindingUser(String token ,Integer role, String name)throws NullPointerException;

    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    HashMap selectUserInfoById(Integer id);

    /**
     * 查询绑定用户的收藏产品
     * @param token
     * @param binding_id
     * @return
     */
    List<HashMap> selectBandingUserGoodsCollection(String token ,Integer role, Integer binding_id, String name)throws NullPointerException;

    /**
     * 添加用户的产品收藏
     * @param binding_id 商家用户id
     * @param goods_id 产品id
     * @return
     */
    Integer addBandingUserGoodsCollection(Integer binding_id, Integer goods_id);

    /**
     * 取消用户收藏产品
     * @param collection_id
     * @return
     */
    Integer deleteBandingUserGoodsCollection(String collection_id)throws Exception;

    /**
     * 通过用户方案id和产品id查询查询产品信息
     * @param goods_id
     * @param scheme_id
     * @return
     */
    List<HashMap> findGoodsInfoBySidAndGid(String token ,Integer role, String goods_id, Integer scheme_id, Integer binding_id);

    /**
     * 销售订单
     * @param order
     * @return
     */
    Integer addOrder(String token ,Integer role, Order order)throws IndexOutOfBoundsException ,NullPointerException ,Exception;

    /**
     * 销售退货订单
     * @param token
     * @param order
     * @return
     */
    Integer addReturnOrder(String token ,Integer role, Order order)throws Exception;

    /**
     * （临时订单）通过订单编号查询历史订单
     * @param number
     * @return
     */
    HashMap historyOrder(String number)throws NullPointerException;

    /**
     * 查询用户的历史销售订单
     * @param binding_id
     * @param startTime
     * @param endTime
     * @return
     */
    List<HashMap> findHistoryOrder(Integer binding_id, String startTime, String endTime);

    /**
     * 通过订单id查询订单详情
     * @param order_id
     * @return
     */
    List<HashMap> findOrderInfoById(String order_id)throws Exception;

    /**
     * 临时订单
     * @param token
     * @param order
     * @return
     */
    Integer addFalseOrder(String token ,Integer role, Order order)throws NullPointerException, Exception;

    /**
     * 添加费用支出记录
     * @param token
     * @param expense
     * @return
     */
    Integer addExpense(String token ,Integer role , Expense[] expense)throws NullPointerException, Exception;

    /**
     * 费用列表
     * @param token
     * @param pageNo
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Page expenseList(String token ,Integer role , Integer pageNo, Integer type, String startTime, String endTime);

    /**
     * 删除费用信息
     * @param token
     * @param role 1店铺，2员工
     * @param id 费用id
     * @return
     */
    Integer deleteExpense(String token ,Integer role ,Integer id)throws NullPointerException ,Exception;

    /**
     * 超期进货的用户
     * @param token
     * @return
     */
    Page overduePurchaseUser(String token ,Integer role , Integer pageNo, String name, Integer cycle)throws NullPointerException;

    /**
     * 没有销售的用户
     * @param token
     * @param name
     * @return
     * @throws NullPointerException
     */
    Page noSalesUser(String token ,Integer role , Integer pageNo, String name)throws NullPointerException;

    /**
     * 待接单总数
     * @param token
     * @return
     */
    Integer waitingOrderCount(String token ,Integer role);

    /**
     * 待接单
     * @param token
     * @return
     */
    Page waitingOrder(String token ,Integer role ,Integer pageNo);

    /**
     * 订单详情
     * @param id
     * @return
     */
    HashMap orderDetail(Integer id)throws NullPointerException;

    /**
     * 删除订单详情产品
     * @param id 详情id
     * @return
     */
    Integer deleteOrderDetail(Integer id ,Integer mold )throws NullPointerException ,Exception;

    /**
     * 修改订单销售产品金额和运费差价
     * @param order
     * @return
     */
    Integer updateOrderPrice(String token ,Integer role, Order order)throws NullPointerException, Exception;

    /**
     * 修改订单打印状态
     * @param id
     * @return
     */
    Integer updatePrintFrequ(Integer id);

    /**
     * 修改订单状态（2 接受）（5 拒绝）
     * @param id
     * @param status
     * @return
     */
    Integer updateOrderAcceptOrRejectionStatus(String token,Integer role ,Integer id, Integer status, String reason)throws NullPointerException, Exception;

    /**
     * 一键接单
     * @param token
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer onekeyAcceptOrder(String token ,Integer role)throws NullPointerException ,Exception;

    /**
     * 未打印的订单总数
     * @param token
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer notPrintedOrderCount(String token ,Integer role)throws NullPointerException ,Exception;

    /**
     * 未打印的订单
     * @param token
     * @param pageNo
     * @return
     */
    Page notPrintedOrder(String token ,Integer role, Integer pageNo, String name, String number)throws NullPointerException ,Exception;

    /**
     * 待送货订单总数
     * @param token
     * @return
     */
    Integer stayDeliveredCount(String token ,Integer role)throws NullPointerException;

    /**
     * 待送货订单
     * @param token
     * @param pageNo
     * @param name
     * @return
     */
    Page stayDelivered(String token ,Integer role, Integer pageNo, String name, String number ,String staffName)throws NullPointerException ,Exception;

    /**
     * 确认到货或取消订单状态（4 确认已送达）（6 取消订单）
     * @param id 订单id
     * @param status
     * @param mold
     * @return
     */
    Integer updateOrderConfirmOrCancelStatus(String token ,Integer role, Integer id, Integer status, Integer mold)throws NullPointerException ,Exception;


    /**
     * 未到货的订单直接收款
     * @param token
     * @param role
     * @param id
     * @param state
     * @param mold
     * @paran price
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer updateOrderReceivables(String token ,Integer role, Integer id , Integer state, Integer mold , Integer type, Double money )throws NullPointerException ,Exception;

    /**
     * 未到货的订单取消
     * @param token
     * @param role
     * @param id
     * @param mold
     * @paran price
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer updateOrderCancel(String token ,Integer role, Integer id ,  Integer mold  )throws NullPointerException ,Exception;


    /**
     * 查询未付款,欠款订单总数
     * @param token
     * @param state
     * @return
     */
    Integer arrivalOrderCount(String token ,Integer role, Integer state)throws NullPointerException;

    /**
     * 查询未付款的订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Page unpaidOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime ,String staffName )throws NullPointerException ,Exception;


    /**
     * 查询欠款的订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Page arrearsOrder(String token ,Integer role  , Integer pageNo, String name, String number, String startTime, String endTime ,String staffName )throws NullPointerException ,Exception;



    /**
     * 查询已完成订单
     * @param token
     * @param pageNo
     * @param name 用户名称或编号
     * @param startTime
     * @param endTime
     * @return
     */
    Page arrivalOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime,String staffName )throws NullPointerException;


    /**
     *收款或欠款
     * @param id 订单id，
     * @param state 2欠款，3已完成
     * @param money
     * @return
     */
    Integer updateArrivalOrder(String token ,Integer role, Integer id, Integer state, Integer type, Double money)throws NullPointerException, Exception;

    /**
     * 拒绝
     * @param token
     * @return
     */
    Page refuseOrder(String token ,Integer role, Integer pageNo)throws NullPointerException ,Exception;

    /**
     * 所有销售订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @return
     */
    Page allSaleOrder(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime, Integer mold)throws NullPointerException,Exception;

    /**
     * 计算这一年总销售金额
     * @param token
     * @return
     */
    HashMap yearSalePirce(String token ,Integer role)throws NullPointerException;

    /**
     * 计算这季度总销售金额
     * @param token
     * @return
     */
    HashMap quarterSalePrice(String token ,Integer role)throws NullPointerException;

    /**
     * 计算这月总销售金额
     * @param token
     * @return
     */
    HashMap monthSalePrice(String token ,Integer role)throws NullPointerException;

    /**
     * 计算这周总销售金额
     * @param token
     * @return
     */
    HashMap weekSalePrice(String token ,Integer role )throws NullPointerException;

    /**
     * 计算当天总销售金额
     * @param token
     * @return
     */
    HashMap daysSalePrice(String token ,Integer role)throws NullPointerException;

    /**
     * 自定义时间段总销售金额
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    HashMap definitionSalePrice(String token ,Integer role, String startTime, String endTime)throws NullPointerException;

    /**
     * 用户取消订单总数
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    Integer userCancelOrderCount(String token ,Integer role , String startTime, String endTime)throws NullPointerException, Exception;

    /**
     *用户取消订单
     * @param token
     * @param pageNo
     * @return
     */
    Page userCancelOrder(String token ,Integer role , Integer pageNo, String startTime, String endTime);

    /**
     * 申请审核用户总数
     * @param token
     * @return
     * @throws NullPointerException
     */
    Integer auditUserCount(String token ,Integer role )throws NullPointerException;

    /**
     * 申请审核用户信息
     * @param token
     * @return
     */
    List<HashMap> auditUser(String token ,Integer role)throws NullPointerException;

    /**
     * 审核用户
     * @param id
     * @param state
     * @param reason
     * @return
     */
    HashMap updateAuditUser(String token ,Integer role, Integer id, String phone, Integer state, String reason)throws NullPointerException;

    /**
     * 申请欠款审核总数
     * @param token
     * @return
     */
    Integer arrearsAuditCount(String token ,Integer role)throws NullPointerException;

    /**
     * 申请欠款审核列表
     * @param token
     * @return
     */
    List<HashMap> arrearsAudit(String token ,Integer role)throws NullPointerException;

    /**
     * 审核欠款
     * @param id
     * @param state
     * @return
     */
    Integer updateArrearsAudit(Integer id, Integer state) throws NullPointerException ,Exception;

    /**
     * 打印销售订单
     * @param order_id
     * @return
     */
    HashMap orderPrint(Integer order_id)throws NullPointerException ,Exception;

    /**
     * 打印销售订单详情
     * @param pageNo
     * @param order_id
     * @return
     */
    List<HashMap> orderPrintDetail(Integer pageNo, Integer order_id ,Integer pageSize);

    /**
     * 打印临时订单
     * @param order_id
     * @return
     */
    HashMap printTemporaryOrder(Integer order_id)throws NullPointerException;

    /**
     * 打印临时订单详情
     * @param pageNo
     * @param order_id
     * @return
     */
    List<HashMap> printTemporaryOrderDetail(Integer pageNo, Integer order_id  ,Integer pageSize);

    /**
     * 超期用户导出
     * @param token
     * @return
     */
    HSSFWorkbook overduePurchaseUserDownload(String token ,Integer role);

    /**
     * 没有销售用户导出
     * @param token
     * @return
     */
    HSSFWorkbook noSalesUserDownload(String token ,Integer role);

    /**
     * 回退取消订单
     * @param id
     * @return
     */
    Integer updateOrderStatus(Integer id)throws NullPointerException;

    /**
     * 验单
     * @param id
     * @return
     */
    Integer  updateCheck(Integer id)throws Exception;

    /**
     * 一键取消验单
     * @param state
     * @return
     */
    Integer updateCancelCheck(String token ,Integer role, Integer state, String startTime, String endTime)throws NullPointerException, Exception;

    /**
     * 一键取消待送货验单
     * @return
     */
    Integer updateStayDeliveredCancelCheck(String token ,Integer role)throws NullPointerException, Exception;


    /**
     * 积分兑换订单总数
     * @param token
     * @param role
     * @param state
     * @return
     */
    Integer selectIntegraOrderCount(String token , Integer role ,Integer state );

    /**
     * 积分兑换订单
     * @param token
     * @param role
     * @param pageNo
     * @param state
     * @return
     */
    Page selectIntegraOrder(String token , Integer role ,Integer pageNo ,Integer state );

    /**
     * 积分兑换订单确认
     * @param token
     * @param role
     * @param id
     * @param state
     * @return
     */
    Integer updateIntegraOrder(String token , Integer role ,Integer id ,Integer state)throws NullPointerException ,Exception;

    /**
     * 售后服务总数
     * @param token
     * @param role
     * @return
     */
    Integer salesServiceTCount(String token ,Integer role,Integer state);

    /**
     * 售后服务
     * @param token
     * @param role
     * @param pageNo
     * @return
     */
    Page salesService( String token , Integer role , Integer pageNo,Integer state );

    /**
     * 处理售货服务
     * @param id
     * @param state
     * @return
     */
    Integer updateSalesService(Integer id ,Integer state);

    /**
     * 收款记录
     * @param id
     * @return
     */
    List<HashMap> selectOrderMoneyRecords(Integer id);
}
