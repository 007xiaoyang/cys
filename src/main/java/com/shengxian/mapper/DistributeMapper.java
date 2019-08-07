package com.shengxian.mapper;

import com.shengxian.entity.Dispatc;
import com.shengxian.entity.Paramt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface DistributeMapper {

    /**
     * 所有未到货的产品汇总总数
     * @param bid 商家id
     * @param wid 仓库id
     * @return
     */
    Integer allNotArrivalGoodsSummaryCount(@Param("business_id") Integer bid, @Param("wid") Integer wid);

    /**
     * 所有未到货的产品汇总
     * @param bid 商家id
     * @param wid 仓库id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> allNotArrivalGoodsSummary(@Param("business_id") Integer bid, @Param("wid") Integer wid, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 所有未到货的产品汇总总金额
     * @param bid 商家id
     * @param wid 仓库id
     * @return
     */
    Double allNotArrivalGoodsSummaryTatolMoney(@Param("business_id") Integer bid, @Param("wid") Integer wid);


    /**
     * 所有未到货的产品明细总数
     * @param bid 商家id
     * @param wid 仓库id
     * @return
     */
    Integer allNotArrivalGoodsDetailCount(@Param("business_id") Integer bid, @Param("wid") Integer wid);

    /**
     * 所有未到货的产品明细
     * @param bid 商家id
     * @param wid 仓库id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> allNotArrivalGoodsDetail(@Param("business_id") Integer bid, @Param("wid") Integer wid, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 所有未到货的产品明细总数总金额
     * @param bid 商家id
     * @param wid 仓库id
     * @return
     */
    Double allNotArrivalGoodsDetailTatolMoney(@Param("business_id") Integer bid, @Param("wid") Integer wid);


    /**
     * 查询未分配给员工派送的订单
     * @param bid
     * @param name
     * @param number
     * @return
     */
    List<HashMap> notDistributeOrder(@Param("business_id") Integer bid, @Param("name") String name, @Param("number") String number);

    /**
     * 添加员工配送
     * @param dispatc
     * @return
     */
    Integer addDispatc(Dispatc dispatc);

    /**
     * 通过订单id查询员工配送详情是否有分配过了，如果有则删除
     * @param order_id
     * @return
     */
    HashMap selectDispatcDetailByOrder_id(@Param("order_id") Integer order_id);

    /**
     * 删除配送详情记录
     * @param id 配送详情id
     * @return
     */
    Integer deleteDispatcDetail(@Param("id") Integer id);

    /**
     * 通过订单id修改订单属于谁配送的
     * @param id
     * @param staff_id
     * @return
     */
    Integer updateDistribyteOrderStaff(@Param("id") Integer id, @Param("staff_id") Integer staff_id);

    /**
     * 添加员工配送详情
     * @param dispatch_id
     * @param order_id
     * @return
     */
    Integer addDispatcDetail(@Param("dispatch_id") Integer dispatch_id, @Param("order_id") Integer order_id);

    /**
     * 查询是否是到货的订单了
     * @param order_id
     * @return
     */
    Integer findOrderStatus(@Param("id") Integer order_id);

    /**
     * 分配订单给员工派送
     * @param order_id 订单id
     * @param staff_id 员工id
     * @return
     */
    Integer updateDistributeOrderToStaff(@Param("id") Integer order_id, @Param("staff_id") Integer staff_id);


    /**
     * 添加指定员工派送的车次提成
     * @param staff_id
     * @param create_time
     * @return
     */
    Integer addOncePercentage(@Param("staff_id") Integer staff_id, @Param("create_time") Date create_time, @Param("order_id") String order_id);

    /**
     * 已经分配给员工配送订单总数
     * @param paramt
     * @return
     */
    Integer alreadyDistributeOrderCount(Paramt paramt);

    /**
     * 已经分配给员工配送订单
     * @param paramt
     * @return
     */
    List<HashMap> alreadyDistributeOrder(Paramt paramt);

    /**
     * 已经分配给员工配送订单总金额
     * @param paramt
     * @return
     */
    Double alreadyDistributeOrderTatolMoney(Paramt paramt);


    /**
     * 通过配送id查询配送订单详情
     * @param id 配送id
     * @param is_del 0未删除，1删除
     * @return
     */
    List<HashMap> findDistributeOrderDetail(@Param("id") Integer id, @Param("is_del") Integer is_del, @Param("status") Integer status, @Param("number") String number);

    /**
     * 通过配送id查询配送订单详情不同状态的总金额
     * @param id 配送id
     * @param state 收款状态
     * @param is_del 0未删除，1删除
     * @return
     */
    HashMap findDistributeOrderDetailTotalMoney(@Param("id") Integer id, @Param("state") Integer state, @Param("is_del") Integer is_del, @Param("status") Integer status);

    /**
     * 员工配送完成的订单汇总总数
     * @param paramt
     * @return
     */
    Integer saleDistributeCompletOrderSummaryCount(Paramt paramt);

    /**
     * 员工配送完成的订单汇总
     * @param paramt
     * @return
     */
    List<HashMap> saleDistributeCompletOrderSummary(Paramt paramt);

    /**
     * 员工配送完成的订单汇总总金额合计
     * @param paramt
     * @return
     */
    HashMap saleDistributeCompletOrderSummaryTatolMoney(Paramt paramt);


    /**
     * 员工配送完成的订单明细总数
     * @param paramt
     * @return
     */
    Integer saleDistributeCompletOrderDetailCount(Paramt paramt);

    /**
     * 员工配送完成的订单明细
     * @param paramt
     * @return
     */
    List<HashMap> saleDistributeCompletOrderDetail(Paramt paramt);

    /**
     * 员工配送完成的订单明细总金额合计
     * @param paramt
     * @return
     */
    HashMap saleDistributeCompletOrderDetailTatolMoney(Paramt paramt);







    /**
     * 员工总明细总数
     * @param bid
     * @param staff_id
     * @return
     */
    Integer staffTatolDetailCount(@Param("business_id") Integer bid, @Param("staff_id") Integer staff_id);

    /**
     * 员工总明细
     * @param bid
     * @param staff_id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> staffTatolDetail(@Param("business_id") Integer bid, @Param("staff_id") Integer staff_id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 统计员工每月订单总数
     * @param staff_id
     * @return
     */
    Integer staffMonthOrderCount(@Param("staff_id") Integer staff_id);

    /**
     * 统计员工每月销售订单
     * @return
     */
    List<HashMap> staffMonthOrder(@Param("staff_id") Integer staff_id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 统计员工每月提成
     * @param staff_id
     * @param time
     * @return
     */
    Double staffMonthStatis(@Param("staff_id") Integer staff_id, @Param("time") String time);

    /**
     * 根据月查询员工结算金额
     * @param staff_id
     * @param time
     * @return
     */
    Double totalTaffWage(@Param("staff_id") Integer staff_id, @Param("time") String time);

    /**
     * 查询员工每月是否结算了
     * @param staff_id
     * @param time
     * @return
     */
    Integer staffWageSettlement(@Param("staff_id") Integer staff_id, @Param("time") String time);

    /**
     * 判断当前月是否结算了
     * @param time
     * @return
     */
    Integer finTime(@Param("time") String time);

    /**
     * 员工工资结算
     * @param steff_id
     * @param time
     * @return
     */
    Integer addStaffWageSettlement(@Param("staff_id") Integer steff_id, @Param("money") Double money, @Param("time") String time);

    /**
     * 员工订单明细总数
     * @param staff_id
     * @param time
     * @return
     */
    Integer staffDayDetailCount(@Param("staff_id") Integer staff_id, @Param("time") String time  ,@Param("name") String name,@Param("type") Integer type ,@Param("startTime") String startTime ,@Param("endTime") String endTime);

    /**
     * 员工订单明细
     * @param staff_id
     * @param time
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> staffDayDetail(@Param("staff_id") Integer staff_id, @Param("time") String time  ,@Param("name") String name,@Param("type") Integer type ,@Param("startTime") String startTime ,@Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


    Double staffDayDetailTotalMoney(@Param("staff_id") Integer staff_id, @Param("time") String time  ,@Param("name") String name,@Param("type") Integer type ,@Param("startTime") String startTime ,@Param("endTime") String endTime);



    /**
     * 员工的采购订单明细总数
      * @param staff_id
     * @param time
     * @return
     */
    //Integer purchaseDetailCount(@Param("staff_id") Integer staff_id, @Param("time") String time ,@Param("name") String name,@Param("type") Integer type);

    /**
     * 员工的采购订单明细
     * @param staff_id
     * @param time
     * @param startIndex
     * @param pageSize
     * @return
     */
    //List<HashMap> purchaseDetail(@Param("staff_id") Integer staff_id, @Param("time") String time  ,@Param("name") String name,@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);



    /**
     * 根据类型查询员工的各种提成比例
     * @param staff_id
     * @param type
     * @return
     */
    HashMap findStaffPercentage(@Param("staff_id") Integer staff_id, @Param("type") Integer type);


    /**
     * 销售结算的未付款欠款订单
     * @return
     */
    List<Integer> saleSettlementUnpaidArrearsOrder();

    /**
     * 采购结算的未付款欠款订单
     * @return
     */
    List<Integer> purchaseSettlementUnpaidArrearsOrder();

    /**
     * 销售订单明细总数
     * @param staff_id
     * @param time
     * @return
     */
    Integer saleOrderCount(@Param("staff_id") Integer staff_id, @Param("time") String time,@Param("name") String name);

    /**
     * 销售订单明细
     * @param staff_id
     * @param time
     * @return
     */
    List<HashMap> saleOrder(@Param("staff_id") Integer staff_id, @Param("time") String time ,@Param("name") String name, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 采购订单明细总数
     * @param staff_id
     * @param time
     * @return
     */
    Integer purchaseOrderCount(@Param("staff_id") Integer staff_id, @Param("time") String time,@Param("name") String name);

    /**
     * 采购订单明细
     * @param staff_id
     * @param time
     * @return
     */
    List<HashMap> purchaseOrder(@Param("staff_id") Integer staff_id, @Param("time") String time ,@Param("name") String name, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


}
