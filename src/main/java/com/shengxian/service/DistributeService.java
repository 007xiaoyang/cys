package com.shengxian.service;

import com.shengxian.common.util.Page;

import java.util.HashMap;
import java.util.List;

public interface DistributeService {

    /**
     * 所有未到货的产品汇总
     * @param token
     * @param pageNo
     * @param wid
     * @return
     */
    Page allNotArrivalGoodsSummary(String token ,Integer role , Integer pageNo, Integer wid)throws NullPointerException;

    /**
     * 所有未到货的产品明细
     * @param token
     * @param pageNo
     * @param wid
     * @return
     */
    Page allNotArrivalGoodsDetail(String token ,Integer role , Integer pageNo, Integer wid)throws NullPointerException;


    /**
     * 查询未分配给员工派送的订单
     * @param token
     * @param name
     * @param number
     * @return
     */
    List<HashMap> notDistributeOrder(String token ,Integer role , String name, String number)throws NullPointerException ,Exception;

    /**
     * 分配订单给员工派送
     * @param order_id
     * @param staff_id
     * @return
     */
    Integer distributeOrderToStaff(String token ,Integer role , String order_id, Integer staff_id)throws NullPointerException, Exception;

    /**
     * 已经分配给员工配送的订单
     * @param token
     * @param pageNo
     * @param staff_id
     * @return
     */
    Page alreadyDistributeOrder(String token ,Integer role, Integer pageNo, Integer status, Integer staff_id)throws NullPointerException ,Exception;


    /**
     *通过配送id查询配送订单详情
     * @param id
     * @return
     */
    List<HashMap> findDistributeOrderDetail(Integer id, Integer is_del, Integer status, String number);

    /**
     * 通过配送id查询配送订单详情不同状态的总金额
     * @param id
     * @return
     */
    HashMap findDistributeOrderDetailTotalMoney(Integer id, Integer is_del, Integer status);



    /**
     * 员工配送完成的订单汇总
     * @param token
     * @param pageNo
     * @param staff_id
     * @param type
     * @return
     */
    Page saleDistributeCompletOrderSummary(String token  ,Integer role, Integer pageNo, Integer staff_id, Integer type, Integer status, String startTime, String endTime)throws NullPointerException ,Exception;

    /**
     * 员工配送完成的订单明细
     * @param token
     * @param pageNo
     * @param staff_id
     * @param name
     * @return
     */
    Page saleDistributeCompletOrderDetail(String token ,Integer role, Integer pageNo, Integer staff_id, String name, String startTime, String endTime)throws NullPointerException;







    /**
     * 员工总明细
     * @param token
     * @param staff_id
     * @return
     */
    Page staffTatolDetail(String token ,Integer role , Integer pageNo, Integer staff_id)throws NullPointerException ,Exception;

    /**
     * 员工月明细
     * @param token
     * @param pageNo
     * @param staff_id
     * @return
     * @throws NullPointerException
     */
    Page staffMonthOrder(String token ,Integer role , Integer pageNo, Integer staff_id)throws NullPointerException ,Exception ;

    /**
     * 员工工资结算
     * @param steff_id
     * @param time
     * @return
     */
    Integer addStaffWageSettlement(Integer steff_id, Double money, String time)throws Exception;

    /**
     * 员工提成订单明细
     * @param staff_id
     * @param time
     * @return
     * @throws Exception
     */
    Page staffDayDetail(Integer pageNo, Integer staff_id, String time,String name ,Integer type ,String startTime ,String endTime)throws Exception;

    /**
     * 员工订单
     * @param pageNo
     * @param staff_id
     * @param time
     * @return
     */
    Page salePurchaseOrder(Integer pageNo, Integer staff_id, String time,String name ,Integer type);

}
