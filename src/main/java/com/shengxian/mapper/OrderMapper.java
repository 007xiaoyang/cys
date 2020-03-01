package com.shengxian.mapper;

import com.shengxian.entity.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 查询订单号
     * @return
     */
    List<Integer> orderNumber(@Param("number") String number);

    /**
     * 搜索商家用户
     * @param bid 商家id
     * @param name 搜索条件，名字或编号
     * @return
     */
    List<HashMap> selectBindingUser(@Param("business_id") Integer bid, @Param("name") String name);

    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    HashMap selectUserInfoById(@Param("id") Integer id);

    /**
     * 查询绑定用户的收藏产品
     * @param bid
     * @param bindind_id
     * @return
     */
    List<HashMap> selectBandingUserGoodsCollection(@Param("business_id") Integer bid, @Param("binding_id") Integer bindind_id, @Param("name") String name);

    /**
     * 判断用户是否收藏过该产品
     * @param bindingId
     * @param goodsId
     * @return
     */
    Integer selectBindingIsExist(@Param("bindingId") Integer bindingId , @Param("goodsId") Integer goodsId);

    /**
     * 添加用户的产品收藏
     * @return
     */
    Integer addBandingUserGoodsCollection(Collect collect);

    /**
     * 取消用户收藏产品
     * @param id 收藏id
     * @return
     */
    Integer deleteBandingUserGoodsCollection(@Param("id") Integer id);

    /**
     * 通过用户方案id和产品id查询查询产品信息
     * @param goods_id
     * @param scheme_id
     * @return
     */
    HashMap findGoodsInfoBySidAndGid(@Param("goods_id") Integer goods_id, @Param("scheme_id") Integer scheme_id, @Param("binding_id") Integer binding_id);

    /**
     * 提交订单
     * @param order
     * @return
     */
    Integer addOrder(Order order);

    /**
     * 提交订单详情
     * @param orderDetail
     * @return
     */
    Integer addOrderDetail(OrderDetail orderDetail);

    /**
     * 修改报损单的订单详情id
     * @param gid 报损id
     * @param detail_id 订单详情id
     * @return
     */
    Integer updateGive(@Param("id") Integer gid, @Param("order_id") Integer detail_id);

    /**
     * 通过订单详情id修改报损表里的报损数量
     * @param order_id 订单详情id
     * @param num 报损数量
     * @return
     */
    Integer updateGiveNum(@Param("order_id") Integer order_id, @Param("num") Double num, @Param("loss_time") Date loss_time);

    /**
     * 通过员工id查询员工的销售提成
     * @param staff_id
     * @param type
     * @return
     */
    String findStaffIsOpenOrderPercent(@Param("staff_id") Integer staff_id, @Param("type") Integer type);

    /**
     * 添加员工的提成记录
     * @param staff_id 员工id
     * @param order_id 订单id
     * @param type  3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
     * @param create_time
     * @return
     */
    Integer  addStaffFrequency(@Param("staff_id") Integer staff_id, @Param("order_id") Integer order_id, @Param("type") Integer type, @Param("statis") Double statis, @Param("create_time") Date create_time);

    /**
     * 添加员工（产品或客户）提成金额记录
     * @param order_id 订单id
     * @param staff_id 员工id
     * @param statis 提成金额
     * @param type 标识不同提成:1产品，2客户
     * @return
     */
    Integer addStaffOrderStatis(@Param("order_id") Integer order_id, @Param("staff_id") Integer staff_id, @Param("statis") Double statis, @Param("type") Integer type, @Param("create_time") Date create_time);

    /**
     * 通过订单id查询订单详情的销售产品总重量（斤）
     * @param goods_id 订单id
     * @return
     */
    Double goodsTotalWeight(@Param("order_id") Integer goods_id ,@Param("business_id") Integer business_id);

    /**
     * 通过订单id查询到达的销售金额
     * @param goods_id
     * @return
     */
    Double saleOrderTotalPrice(@Param("id") Integer goods_id);

    /**
     * 修改当前订单员工的提成
     * @param order_id 订单id
     * @param statis 提成金额
     * @return
     */
    Integer updateOrderStatis(@Param("id") Integer order_id, @Param("statis") double statis);

    /**
     * 修改订单的总金额
     * @param order_id
     * @param price
     * @return
     */
    Integer updateOrderPrice(@Param("id") Integer order_id, @Param("price") BigDecimal price);

    /**
     * 通过订单编号查询所有的订单信息(临时订单调用)
     * @param number
     * @return
     */
    HashMap historyOrder(@Param("number") String number);

    /**
     * 查询用户的历史订单
     * @param binding_id
     * @param startTime
     * @param endTime
     * @return
     */
    List<HashMap> findHistoryOrder(@Param("binding_id") Integer binding_id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 通过订单id查询订单详情
     * @param id
     * @return
     */
    List<HashMap> findOrderInfoById(@Param("id") Integer id);

    /**
     * 临时订单
     * @param order
     * @return
     */
    Integer addFalseOrder(Order order);

    /**
     * 临时订单详情
     * @param orderDetail
     * @return
     */
    Integer addFalseOrderDetail(OrderDetail orderDetail);

    /**
     * 修改临时订单的总金额
     * @param order_id
     * @param price
     * @return
     */
    Integer updateFalseOrderPrice(@Param("id") Integer order_id, @Param("price") Double price);

    /**
     * 添加支出费用和收入
     * @param expense
     * @return
     */
    Integer addExpense(Expense expense);

    /**
     * 费用列表总数
     * @param business_id
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Integer expenseListCount(@Param("business_id") Integer business_id, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 费用列表
     * @param business_id
     * @param type
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> expenseList(@Param("business_id") Integer business_id, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 费用总额
     * @param business_id
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Double expenseTotalMoney(@Param("business_id") Integer business_id, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 通过费用id查询费用创建时间
     * @param id
     * @return
     */
    String expenseInfo(@Param("id") Integer id);

    /**
     * 删除费用信息
     * @param id
     * @return
     */
    Integer deleteExpense(@Param("id") Integer id);

    /**
     * 超期进货的用户总数
     * @param bid
     * @return
     */
    Integer overdueUserCount(@Param("business_id") Integer bid);

    /**
     * 超期进货的用户总数
     * @param bid
     * @param name
     * @param cycle
     * @return
     */
    Integer overduePurchaseUserCount(@Param("business_id") Integer bid, @Param("name") String name, @Param("cycle") Integer cycle);
    /**
     * 超期进货的用户
     * @param bid
     * @return
     */
    List<HashMap> overduePurchaseUser(@Param("business_id") Integer bid, @Param("name") String name, @Param("cycle") Integer cycle, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 标记超期进货为已读
     * @param orderId
     * @return
     */
    Integer markReaded(@Param("id") Integer orderId);


    /**
     * 没有销售的用户总数
     * @param bid
     * @param name
     * @return
     */
    Integer noSalesUserCount(@Param("business_id") Integer bid, @Param("name") String name);

    /**
     * 没有销售的用户
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> noSalesUser(@Param("business_id") Integer bid, @Param("name") String name, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 待接单总数
     * @param bid
     * @return
     */
    Integer waitingOrderCount(@Param("business_id") Integer bid);
    /**
     * 待接单
     * @param bid
     * @return
     */
    List<HashMap> waitingOrder(@Param("business_id") Integer bid ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);

    /**
     * 通过订单id查询订单运费和差价
     * @param id
     * @return
     */
    HashMap freightAndDifferencePrice(Integer id);

    /**
     * 订单详情
     * @param id 订单id
     * @return
     */
    List<HashMap> orderDetail(@Param("id") Integer id);

    /**
     * 通过订单详情id查询原来的销售数量
     * @param id
     * @return
     */
    HashMap orderDetailNum(@Param("id") Integer id);

    /**
     * 删除订单详情产品时通过订单id减少订单总金额
     * @param order_id 订单id
     * @param price 要减少的金额
     * @return
     */
    Integer updateOrderTotalPrice(@Param("id") Integer order_id ,@Param("price") Double price);

    /**
     * 通过订单id加订单总金额
     * @param order_id 订单id
     * @param price 要减少的金额
     * @return
     */
    Integer plusOrderTotalPrice(@Param("id") Integer order_id ,@Param("price") Double price);

    /**
     * 通过店铺优惠券id查询订单使用店铺满减优惠券是否过期了
     * @param id
     * @return
     */
    Coupon selectFullReduct(Integer id );


    /**
     * 通过订单id查询是否还有订单详情id
     * @param order_id
     * @return
     */
    List<Integer> selectOrderDateilId(@Param("order_id") Integer order_id);

    /**
     * 删除订单详情
     * @param id
     * @return
     */
    Integer deleteOrderDetail(@Param("id") Integer id);

    /**
     * 删除订单
     * @param id
     * @return
     */
    Integer deleteOrder(@Param("id") Integer id );

    /**
     * 通过token查询商家id或员工id
     * @param token
     * @return
     */
    Integer findOperateId(String token);


    /**
     * 通过订单详情id修改产品数量和价格和利润
     * @param id 详情id
     * @param order_number
     * @param order_price
     * @return
     */
    Integer updateOrderDetailPrice(@Param("id") Integer id, @Param("order_number") double order_number, @Param("order_price") double order_price, @Param("profit") Double profit ,@Param("cost_price") double cost_price);

    /**
     * 查询订单是否使用优惠券
     * @param orderId
     * @return
     */
    Double selectConponMoney(@Param("id") Integer orderId);

    /**
     * 通过订单id修改订单运费和差价
     * @param id
     * @param freight
     * @param difference_price
     * @return
     */
    Integer updatePurchaseOrderPriceid(@Param("id") Integer id, @Param("price") double price, @Param("freight") double freight, @Param("difference_price") double difference_price);

    /**
     * 通过订单id查询用户订单是线上的还是线下的
     * @param id
     * @return
     */
    HashMap selectBindingOrder(@Param("id") Integer id);


    /**
     * 销售订单详情
     * @param orderId
     * @return
     */
    List<OrderDetail> orderDetailInfo(@Param("id") Integer orderId);


    /**
     * 通过订单id查询用户订单信息
     * @param id
     * @return
     */
    Order selectBindingOrderInfo(@Param("id") Integer id);

    /**
     * 订单详情（产品id和销售数量）
     * @param id
     * @return
     */
    List<HashMap> detail(@Param("id") Integer id);



    /**
     * 销售数量是否小于实际库存
     * @param goods_id 产品id
     * @param num 销售数量
     * @return
     */
    Integer isLessthanActualInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     * 修改订单打印状态
     * @param id
     * @return
     */
    Integer updatePrintFrequ(@Param("id") Integer id);

    /**
     * 通过用户优惠券id查询优惠券状态
     * @param coupon_id
     * @return
     */
    Coupon selectUserCouponState(@Param("id") Integer coupon_id );


    /**
     * 重新指派订单给当前员工
     * @param id
     * @param staffId
     * @return
     */
    Integer updateStaffIdByOrderId(@Param("id") Integer id ,@Param("staffId") Integer staffId);


    /**
     * 修改用户优惠券使用的状态
     * @param coupon_id
     * @return
     */
    Integer updateUserCouponState(@Param("id") Integer coupon_id ,@Param("state") Integer state);

    /**
     * 修改订单状态（接受或拒绝）
     * @param id
     * @param status 2接受，5拒绝
     * @param accept_time 接受或拒绝时间
     * @return
     */
    Integer updateAcceptOrRejectionStatus(@Param("id") Integer id,@Param("staffId") Integer staff_id, @Param("status") Integer status, @Param("accept_time") Date accept_time, @Param("reason") String reason);


    /**
     * 通过店铺ID查询待接单中的订单
     * @param business_id 店铺ID
     * @return
     */
    List<HashMap> selectAcceptOrder(Integer business_id);

    /**
     * 未打印订单的总数
     * @param bid
     * @param name
     * @return
     */
    Integer notPrintedOrderCount(@Param("business_id") Integer bid, @Param("name") String name, @Param("number") String number);

    /**
     * 未打印订单的
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> notPrintedOrder(@Param("business_id") Integer bid, @Param("name") String name, @Param("number") String number, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 未打印订单汇总总数
     * @param
     * @return
     */
    Integer notPrintedOrderSummaryCount(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number, @Param("startTime") String startTime,@Param("endTime") String endTime );

    /**
     * 未打印订单汇总
     * @param
     * @return
     */
    List<HashMap> notPrintedOrderSummary(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number, @Param("startTime") String startTime,@Param("endTime") String endTime ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);

    /**
     * 未打印订单汇总统计金额
     * @param
     * @return
     */
    HashMap notPrintedOrderSummaryTatolMoney(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number, @Param("startTime") String startTime,@Param("endTime") String endTime );

    /**
     * 未打印订单明细总数
     * @param
     * @return
     */
    Integer notPrintedOrderDetailCount(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number,@Param("userName") String userName,@Param("wid") Integer warehouseId , @Param("startTime") String startTime,@Param("endTime") String endTime );

    /**
     * 未打印订单明细
     * @param
     * @return
     */
    List<HashMap> notPrintedOrderDetail(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number,@Param("userName") String userName,@Param("wid") Integer warehouseId , @Param("startTime") String startTime,@Param("endTime") String endTime ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);

    /**
     * 未打印订单明细统计金额
     * @param
     * @return
     */
    HashMap notPrintedOrderDetailTatolMoney(@Param("bid") Integer bid ,@Param("name") String name,@Param("number") String number,@Param("userName") String userName,@Param("wid") Integer warehouseId , @Param("startTime") String startTime,@Param("endTime") String endTime );


    /**
     * 待送货订单总数
     * @param paramt
     * @return
     */
    Integer stayDeliveredCount(Paramt paramt);

    /**
     * 待送货订单
     * @param
     * @return
     */
    List<HashMap> stayDelivered(Paramt paramt);

    /**
     * 待送货订单总金额
     * @param paramt
     * @return
     */
    Double stayDeliveredTotalMoney(Paramt paramt);


    /**
     * 待送货订单汇总总数
     * @param paramt
     * @return
     */
    Integer stayDeliveredSummaryCount(Paramt paramt);

    /**
     * 待送货订单汇总
     * @param paramt
     * @return
     */
    List<HashMap> stayDeliveredSummary(Paramt paramt);

    /**
     * 待送货订单汇总统计金额
     * @param paramt
     * @return
     */
    HashMap stayDeliveredSummaryTatolMoney(Paramt paramt);

    /**
     * 待送货订单明细总数
     * @param paramt
     * @return
     */
    Integer stayDeliveredDetailCount(Paramt paramt);

    /**
     * 待送货订单明细
     * @param paramt
     * @return
     */
    List<HashMap> stayDeliveredDetail(Paramt paramt);

    /**
     * 待送货订单明细统计金额
     * @param paramt
     * @return
     */
    HashMap stayDeliveredDetailTatolMoney(Paramt paramt);



    /**
     * 通过订单id查询订单信息
     * @param id
     * @return
     */
    Order selectOrderInfo(Integer id );

    /**
     * 通过订单id查询订单是否有员工提成
     * @param id
     * @return
     */
    HashMap findOrderIfStaffIdPercentage(@Param("id") Integer id);


    /**
     * 未到货的订单直接收款
     * @param id
     * @param state 收款状态
     * @param type 收款类型
     * @param money 收款金额
     * @param name 操作人名称
     * @param time 操作时间
     * @return
     */
    Integer updateOrderStatusState(@Param("id") Integer id, @Param("state") Integer state,@Param("type") Integer type ,@Param("money") Double money , @Param("name") String name , @Param("time") Date time);

    /**
     *未到货的订单取消
     * @param id 订单id
     * @param status 4确认，6取消
     * @param audit_time 确认或取消时间
     * @return
     */
    Integer updateOrderConfirmOrCancelStatus(@Param("id") Integer id, @Param("status") Integer status, @Param("audit_time") Date audit_time, @Param("deliver") String deliver);




    /**
     * 查询未付款,欠款订单总数
     * @param bid
     * @param state
     * @return
     */
    Integer arrivalOrderCount(@Param("business_id") Integer bid, @Param("state") Integer state);


    /**
     * 查询未付款订单总数
     * @param paramt
     * @return
     */
    Integer unpaidOrderCount(Paramt paramt);

    /**
     * 查询未付款订单
     * @param paramt
     * @return
     */
    List<HashMap> unpaidOrder(Paramt paramt);

    /**
     * 查询未付款订单总金额
     * @param paramt
     * @return
     */
    HashMap unpaidOrderTotalMoney(Paramt paramt);



    /**
     * 查询欠款订单总数
     * @param paramt
     * @return
     */
    Integer arrearsOrderCount(Paramt paramt);

    /**
     * 查询欠款订单
     * @param paramt
     * @return
     */
    List<HashMap> arrearsOrder(Paramt paramt);

    /**
     * 查询欠款订单总金额
     * @param paramt
     * @return
     */
    HashMap arrearsOrderTotalMoney(Paramt paramt);


    /**
     * 查询已完成订单总数
     * @return
     */
    Integer orderInfoCount(Paramt paramt);

    /**
     * 查询已完成订单
     * @return
     */
    List<HashMap> orderInfo(Paramt paramt);

    /**
     * 查询已完成订单总金额
     * @return
     */
    HashMap orderInfoTatolMoney(Paramt paramt);


    /**
     * 通过订单id查询订单status状态和part是商城订单还是线下订单
     * @param id
     * @return
     */
    HashMap statusAndPartById(@Param("id") Integer id);



    /**
     * 通过订单id查询订单到货状态
     * @param id
     * @return
     */
    Integer findOrderStatus(@Param("id") Integer id);

    /**
     * 通过订单id查询订单收款状态
     * @param id
     * @return
     */
    Integer findOrderState(@Param("id") Integer id);




    /**
     * 查询商家当前是否结算过
     * @param business_id 店铺id
     * @param type 0销售结算，1采购结算
     * @return
     */
    Settlement financialSettlement(@Param("id") Integer business_id, @Param("type") Integer type);

    /**
     * 通过订单id查询是否是给员工配送的订单
     * @param id 订单id
     * @return
     */
    Integer isStaffDistributeOrder(@Param("id") Integer id);


    /**
     * 修改配送订单的操作记录
     * @param order_id
     * @return
     */
    Integer updateDistributeDetailOpen(@Param("order_id") Integer order_id);

    /**
     * 通过配送id查询配送详情的所有订单是否都付款了
     * @param id 配送id
     * @return
     */
    List<Integer> isAllOrderPay(@Param("id") Integer id);

    /**
     * 通过配送id修改员工分配一次记录完成的状态
     * @param id 配送id
     * @return
     */
    Integer updateDistributeStatus(@Param("id") Integer id);


    /**
     * 欠款收款
     * @param id 订单id
     * @param state 2欠款
     * @param type 收款方式
     * @param money 金额
     * @param receivable_time 欠款时间
     * @param payee 操作人
     * @return
     */
    Integer updateArrivalOrder(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") Double money, @Param("receivable_time") Date receivable_time, @Param("payee") String payee);

    /**
     * 结算的订单的收款
     * @param id 订单id
     * @param state 2欠款
     * @param type 收款方式
     * @param money 金额
     * @param receivable_time 欠款时间
     * @param payee 操作人
     * @return
     */
    Integer updateReceivablesOrder(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") Double money, @Param("receivable_time") Date receivable_time, @Param("payee") String payee);

    /**
     * 查询订单是欠款后第二次收款，还是一次性收的款，一次性收的款是没有收款时间的
     * @param order_id
     * @return
     */
    String receivableTime(@Param("id") Integer order_id);

    /**
     * 添加每次销售收款类型记录
     * @param business_id
     * @param order_id
     * @param type
     * @param money
     * @param date
     * @return
     */
    Integer addreceiptClass(@Param("business_id") Integer business_id ,@Param("order_id") Integer order_id,@Param("type") Integer type ,@Param("money") Double money ,@Param("receivable_time") Date date);

    /**
     * 订单结算状态
     * @param order_id
     * @return
     */
    Integer orderIfSettlement(@Param("id") Integer order_id);

    /**
     * 未结算订单的收款
     * @param id 订单id
     * @param state 2欠款
     * @param type 收款方式
     * @param money 金额
     * @param receivable_time 欠款时间
     * @param payee 操作人
     * @return
     */
    Integer updateNotReceivablesOrder(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") Double money, @Param("receivable_time") Date receivable_time, @Param("payee") String payee);


    /**
     * 拒绝订单总数
     * @param bid
     * @return
     */
    Integer refuseOrderCount(Integer bid);

    /**
     * 拒绝订单
     * @param bid
     * @return
     */
    List<HashMap> refuseOrder(@Param("business_id") Integer bid, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 所有销售订单总数
     * @return
     */
    Integer allSaleOrderCount(Paramt paramt);

    /**
     * 所有销售订单
     * @return
     */
    List<HashMap>  allSaleOrder(Paramt paramt);

    /**
     * 所有销售订单总金额
     * @param paramt
     * @return
     */
    HashMap allSaleOrderTatolMoney(Paramt paramt);

    /**
     * 查询所有销售的未到货总金额
     * @param paramt
     * @return
     */
    Double allNotArrivalTatolMoney(Paramt paramt);

    /**
     * 查询所有销售的到货总金额
     * @param paramt
     * @return
     */
    Double allArrivalTatolMoney(Paramt paramt);

    /**
     * 计算这一年总销售金额
     * @param bid
     * @return
     */
    HashMap yearSalePirce(@Param("bid") Integer bid);

    /**
     * 计算这季度总销售金额
     * @param bid
     * @return
     */
    HashMap quarterSalePrice(@Param("bid") Integer bid);

    /**
     * 计算这月总销售金额
     * @param bid
     * @return
     */
    HashMap monthSalePrice(@Param("bid") Integer bid);

    /**
     * 计算这周总销售金额
     * @param bid
     * @return
     */
    HashMap weekSalePrice(@Param("bid") Integer bid);

    /**
     * 计算当天总销售金额
     * @param bid
     * @return
     */
    HashMap daysSalePrice(@Param("bid") Integer bid);

    /**
     * 自定义时间段总销售金额
     * @param bid
     * @return
     */
    HashMap definitionSalePrice(@Param("bid") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 用户取消订单总数
     * @param bid
     * @return
     */
    Integer userCancelOrderCount(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 用户取消订单
     * @param bid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> userCancelOrder(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 申请审核用户总数
     * @param bid
     * @return
     */
    Integer auditUserCount(@Param("business_id") Integer bid);
    /**
     * 申请审核用户信息
     * @param bid
     * @return
     */
    List<HashMap> auditUser(@Param("business_id") Integer bid);

    /**
     * 通过审核id查询是否操作过了
     * @param id
     * @return
     */
    Integer isOperation(@Param("id") Integer id);

    /**
     * 审核用户
     * @param id
     * @param state 1不通过，2通过
     * @param reason 不通过原因
     * @return
     */
    Integer updateAuditUser(@Param("id") Integer id, @Param("state") Integer state, @Param("reason") String reason);

    /**
     * 通过手机号查询该手机号是否有备录在线下的电话号码中
     * @param phone
     * @return
     */
    Integer findPhoneEqualTelephone(@Param("phone") String phone, @Param("business_id") Integer business_id);

    /**
     * 通过商家ID和注册手机号查询是否是二次绑定
     * @param phone
     * @param business_id
     * @return
     */
    Integer findPhoneEqualPhone(@Param("phone") String phone, @Param("business_id") Integer business_id);

    /**
     * 申请欠款审核总数
     * @param bid
     * @return
     */
    Integer arrearsAuditCount(@Param("busienss_id") Integer bid);

    /**
     * 申请欠款审核列表
     * @param bid
     * @return
     */
    List<HashMap> arrearsAudit(@Param("busienss_id") Integer bid);

    /**
     * 审核欠款
     * @param id
     * @param state
     * @return
     */
    Integer updateArrearsAudit(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 通过销售订单id查询订单信息
     * @param id
     * @return
     */
    HashMap orderPrintInfo(@Param("id") Integer id);

    /**
     * 通过订单id查询销售订单详情的总数
     * @param id
     * @return
     */
    Integer orderPrintCount(@Param("order_id") Integer id);

    /**
     * 打印销售订单详情
     * @param id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> orderPrintDetail(@Param("order_id") Integer id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


    /**
     * 打印临时订单
     * @param id
     * @return
     */
    HashMap printTemporaryOrder(@Param("id") Integer id);

    /**
     * 打印临时订单详情的总数
     * @param id
     * @return
     */
    Integer printTemporaryOrderCount(@Param("order_id") Integer id);

    /**
     * 打印临时订单详情
     * @param id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> printTemporaryOrderDetail(@Param("order_id") Integer id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


    /**
     * 超期用户导出
     * @param bid
     * @return
     */
    List<HashMap> overduePurchaseUserDownload(@Param("business_id") Integer bid);

    /**
     * 没有销售用户导出
     * @param bid
     * @return
     */
    List<HashMap> noSalesUserDownload(@Param("business_id") Integer bid);


    /**
     * 通过订单id查询订单优惠券id
     * @param order_id
     * @return
     */
    HashMap selectOrderCoupon(@Param("id") Integer order_id);

    /**
     * 回退取消订单
     * @param id
     * @return
     */
    Integer updateOrderStatus(@Param("id") Integer id);

    /**
     * 验单
     * @param id 订单id
     * @return
     */
    Integer updateCheck(@Param("id") Integer id);

    /**
     * 一键取消验单
     * @param state
     * @param startTime
     * @param endTime
     * @return
     */
    Integer updateCancelCheck(@Param("business_id") Integer bid, @Param("state") Integer state, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 一键取消待送货验单
     * @param bid
     * @return
     */
    Integer updateStayDeliveredCancelCheck(@Param("business_id") Integer bid);


    /**
     * 积分兑换订单总数
     * @param business_id
     * @param state
     * @return
     */
    Integer selectIntegraOrderCount(@Param("business_id") Integer business_id ,@Param("state") Integer state );

    /**
     * 积分兑换订单
     * @param business_id
     * @param state
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> selectIntegraOrder(@Param("business_id") Integer business_id , @Param("state") Integer state , @Param("startIndex") Integer startIndex , @Param("pageSize") Integer pageSize);


    /**
     * 通过订单id查询兑换用户的id和兑换的积分数
     * @param order_id 积分兑换订单id
     * @return
     */
    HashMap findIntegraOrderBDidAndNum(Integer order_id);

    /**
     * 积分兑换订单确认
     * @param id 订单id
     * @param state
     * @param deliver 操作人
     * @param audit_time 操作时间
     * @return
     */
    Integer updateIntegraOrder(@Param("id") Integer id ,@Param("state") Integer state,@Param("deliver") String deliver ,@Param("audit_time") Date audit_time);


    /**
     * 售后服务总数(不包括完成的)
     * @param business_id 店铺id
     * @return
     */
    Integer salesServiceTCount(@Param("business_id") Integer business_id );


    /**
     * 售后服务总数
     * @param business_id 店铺id
     * @return
     */
    Integer salesServiceCount(@Param("business_id") Integer business_id ,@Param("state") Integer state);

    /**
     * 售后服务
     * @param business_id
     * @param pageSize
     * @return
     */
    List<HashMap> salesService(@Param("business_id") Integer business_id ,@Param("state") Integer state,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize );

    /**
     * 处理售货服务
     * @param id
     * @param state
     * @return
     */
    Integer updateSalesService(@Param("id") Integer id ,@Param("state") Integer state ,@Param("audit_time") Date audit_time);



    /**
     * 收款记录
     * @param id
     * @return
     */
    List<HashMap> selectOrderMoneyRecords(@Param("id") Integer id);



    /**
     * 通过店铺id和产品id查询店铺所有有产品提成的员工
     * @param business_id
     * @return
     */
    List<Percentage> selectBusienssAndGoodsAllStaffPercentage(@Param("business_id") Integer business_id ,@Param("goods_id") Integer goods_id);

    /**
     * 通过店铺id和下订单的绑定客户id查询店铺所有有客户提成比例的员工
     * @param business_id
     * @param binding_id
     * @return
     */
    List<Percentage> selectBusinessAndBindingAllStaffPercentage(@Param("business_id")Integer business_id , @Param("binding_id") Integer binding_id);

    /**
     * 通过店铺id查询店铺所有有金额提成比例的员工
     * @param business_id
     * @return
     */
    List<Percentage> selectBusinessAllMoneyStaffPercentage(@Param("business_id")Integer business_id );

}


