package com.shengxian.mapper;

import com.shengxian.entity.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 采购mapper接口层
 *
 * @Author: yang
 * @Date: 2018/9/18
 * @Version: 1.0
 */
@Mapper
public interface PurchaseMapper {



    /**
     * 采购和销售时搜索产品
     * @param business_id
     * @param name
     * @return
     */
    List<HashMap> selectBusinessGoods(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 查询商家产品的收藏
     * @param business_id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<HashMap> findBusinessGoodsCollection(@Param("business_id") Integer business_id, @Param("suppliers_id") Integer suppliers_id, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询产品是否收藏过了
     * @param goods_id
     * @return
     */
    Integer goods_idIfCollection(@Param("goods_id") Integer goods_id, @Param("suppliers_id") Integer suppliers_id);

    /**
     * 添加商家收藏产品
     * @return
     */
    Integer addBusinessGoodsCollection(Collect collect);

    /**
     * 取消商家收藏的产品
     * @param id 收藏id
     * @return
     */
    Integer deleteBusinessGoodsCollection(Integer id);

    /**
     * 查询员工进价屏蔽和库存屏蔽状态
     * @param token
     * @return
     */
    HashMap selectShield(@Param("token") String token);

    /**
     * 通过产品id查询产品信息（销售和采购的数据)
     * @param goods_id
     * @return
     */
   HashMap selectBusinessGoodsById(@Param("goods_id") Integer goods_id, @Param("suppliers_id") Integer suppliers_id);

    /**
     * 查询商家采购的订单
     * @param bid 商家id
     * @param suppliers_id 供应商id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
   List<HashMap> selectPurchaseOrder(@Param("business_id") Integer bid, @Param("suppliers_id") Integer suppliers_id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 通过订单id查询采购产品详情
     * @param order_id
     * @return
     */
   List<HashMap> selectPurchaseOrderDetailById(@Param("id") Integer order_id);

    /**
     * 查询商家的采购模板
     * @param bid
     * @param type 0采购 1销售
     * @return
     */
   Template selectsTemplate(@Param("business_id") Integer bid, @Param("type") Integer type);


    /**
     * 提交采购订单
     * @param purchaseOrder
     * @return
     */
   Integer addPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 提交采购详情订单
     * @param purchaseOrderDetail
     * @return
     */
   Integer addPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 修改赠送订单详情id
     * @param gid 赠送id
     * @param detail 详情id
     * @return
     */
   Integer updateGive(@Param("id") Integer gid, @Param("order_id") Integer detail);

    /**
     * 通过订单详情id修改赠送表里的赠送数量
     * @param detail_id 采购详情id
     * @param num 赠送数量
     * @return
     */
   Integer updateGiveNum(@Param("order_id") Integer detail_id, @Param("num") Double num, @Param("give_time") Date give_time);

    /**
     * 修改采购产品的总金额
     * @param id 采购订单id
     * @param price 总金额
     * @return
     */
   Integer updatePurchase(@Param("id") Integer id, @Param("price") BigDecimal price);

    /**
     * 订单的打印次数
     * @param id
     * @return
     */
   Integer updatePrintFrequ(@Param("id") Integer id, @Param("print_frequ") Integer print_frequ);



    /**
     * 代采购报表
     * @param business_id
     * @return
     */
   Integer PurchasereportCount(@Param("business_id") Integer business_id, @Param("id") Integer id);

    /**
     * 代采购报表
     * @param business_id
     * @param startIndex
     * @param pageSize
     * @return
     */
   List<HashMap> Purchasereport(@Param("business_id") Integer business_id, @Param("id") Integer id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


    /**
     * 待审核，未付款，欠款总数
     * @param bid
     * @param status 0待审核 ，1确认到货
     * @param state 0未付款，1欠款
     * @return
     */
    Integer purchaseCount(@Param("business_id") Integer bid, @Param("status") Integer status, @Param("state") Integer state);

    /**
     * 待审核总数
     * @param paramt
     * @return
     */
   Integer stayAuditedCount(Paramt paramt);
    /**
     * 待审核(采购未到货的订单)
     * @param paramt
     * @return
     */
   List<HashMap> stayAudited(Paramt paramt);

    /**
     * 待审核总金额
     * @param paramt
     * @return
     */
    Double stayAuditedTatolMoney(Paramt paramt);

    /**
     * 通过订单id查询业务员是否是员工
     * @param purchase_id
     * @return
     */
   Integer selectPurchaseStaff(@Param("id") Integer purchase_id);

    /**
     * 采购产品总数量提成记录
     * @param purchase_id 采购订单id
     * @return
     */
   Double pGoodsTotalNumber(@Param("id") Integer purchase_id);

    /**
     * 采购产品吨位提成记录（斤）
     * @param purchase_id
     * @return
     */
   Double pTatolWeight(@Param("id") Integer purchase_id);

    /**
     *  采购金额提成记录
     * @param purchase_id
     * @return
     */
   Double purchaseTotalPrice(@Param("id") Integer purchase_id);



    /**
     * 通过id查询采购确认状态
     * @param id
     * @return
     */
   Integer findPurchaseStatus(@Param("id") Integer id);

    /**
     * 通过id查询采购收款状态
     * @param id
     * @return
     */
    Integer findPurchaseState(@Param("id") Integer id);


    /**
     * 未审核的采购订单直接收款
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
     * 未审核的采购订单取消
     * @param purchase_id 采购订单id
     * @param status 状态，0默认待审核，1采购到货
     * @return
     */
   Integer updatePurchaseConfirmArrival(@Param("id") Integer purchase_id, @Param("auditor") String auditor, @Param("status") Integer status, @Param("audit_time") Date audit_time);

    /**
     * 订单详情
     * @param purchase_id
     * @return
     */
   List<PurchaseOrderDetail> orderDetail(@Param("purchase_id") Integer purchase_id);


    /**
     * 采购退货订单数量报损数量是否大于实际库存
     * @param goods_id
     * @param num
     * @return
     */
   Integer isLessthanActualInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     * 查询采购未付款订单总数
     * @param bid
     * @param name
     * @return
     */
    Integer purchaseUnpaidOrderCount(@Param("business_id") Integer bid , @Param("name") String name ,@Param("number") String number ,@Param("staffName") String staffName);

    /**
     * 查询采购未付款订单
     * @param bid
     * @param name
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> purchaseUnpaidOrder(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number ,@Param("staffName") String staffName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询采购未付款订单总金额
     * @param bid
     * @param name
     * @return
     */
    HashMap purchaseUnpaidOrderTatolMoney(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number ,@Param("staffName") String staffName);


    /**
     * 查询采购欠款的订单总数
     * @param bid
     * @param name
     * @return
     */
    Integer purchaseArrearsOrderCount(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number,@Param("staffName") String staffName);

    /**
     * 查询采购欠款订单
     * @param bid
     * @param name
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> purchaseArrearsOrder(@Param("business_id") Integer bid , @Param("name") String name ,@Param("number") String number ,@Param("staffName") String staffName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询采购欠款订单总金额
     * @param bid
     * @param name
     * @return
     */
    HashMap purchaseArrearsOrderTatolMoney(@Param("business_id") Integer bid , @Param("name") String name ,@Param("number") String number,@Param("staffName") String staffName);


    /**
     * 查询采购已完成的订单总数
     * @param bid
     * @param name
     * @return
     */
    Integer findPurchaseOrderCount(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number, @Param("startTime") String startTime, @Param("endTime") String endTime ,@Param("staffName") String staffName);

    /**
     * 查询采购已完成的订单
     * @param bid
     * @param name
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> findPurchaseOrder(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number, @Param("startTime") String startTime, @Param("endTime") String endTime  ,@Param("staffName") String staffName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询采购已完成的订单总金额
     * @param bid
     * @param name
     * @return
     */
    HashMap findPurchaseOrderTatolMoney(@Param("business_id") Integer bid , @Param("name") String name  ,@Param("number") String number, @Param("startTime") String startTime, @Param("endTime") String endTime  ,@Param("staffName") String staffName);

    /**
     * 确认欠款
     * @param id
     * @param state
     * @param type
     * @return
     */
    Integer updateArrearPurchase(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") double money, @Param("payee") String payee, @Param("receivable") Date receivable);


    /**
     * 结算的收款
     * @param id
     * @param state
     * @param type
     * @return
     */
    Integer updateReceivablesPurchase(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") double money, @Param("payee") String payee, @Param("receivable") Date receivable);

    /**
     * 查询订单是欠款后第二次收款，还是一次性收的款，一次性收的款是没有收款时间的
     * @param order_id
     * @return
     */
    String receivableTime(@Param("id") Integer order_id);

    /**
     * 添加每次采购收款类型记录
     * @param business_id
     * @param order_id
     * @param type
     * @param money
     * @param date
     * @return
     */
    Integer addreceiptClass(@Param("business_id") Integer business_id ,@Param("order_id") Integer order_id,@Param("type") Integer type ,@Param("money") Double money ,@Param("receivable_time") Date date);



    /**
     * 欠款单未结算的收款
     * @param id
     * @param state
     * @param type
     * @return
     */
    Integer updateNotReceivablesPurchase(@Param("id") Integer id, @Param("state") Integer state, @Param("type") Integer type, @Param("money") double money, @Param("payee") String payee, @Param("receivable") Date receivable);


    /**
     * 查询订单的运费和差价
     * @param id
     * @return
     */
    HashMap freightAndDifferencePrice(@Param("id") Integer id);


    /**
     * 查询订单详情
     * @param purchase_id 订单id
     * @return
     */
    List<HashMap> findPurchaseOrderDetail(@Param("id") Integer purchase_id);

    /**
     * 通过详情id查询采购产品的数量
     * @param id
     * @return
     */
    HashMap purchaseGoodsNum(@Param("id") Integer id);

    /**
     * 通过采购订单id修改订单总金额
     * @param id
     * @param price
     * @return
     */
    Integer updatePurchasePrice(@Param("id") Integer id ,@Param("price") Double price );

    /**
     * 删除采购详情产品
     * @param id
     * @return
     */
    Integer deletePurchaseDetail(@Param("id") Integer id);

    /**
     * 通过订单id查询是否还有订单详情id
     * @param id
     * @return
     */
    List<Integer> selectPurchaseDateilId(@Param("purchase_id") Integer id);

    /**
     * 删除采购订单
     * @param id
     * @return
     */
    Integer deletePurchase(@Param("id") Integer id);

    /**
     * 修改采购订单的数量和产品价格
     * @param id
     * @param purchase_number
     * @param purchase_price
     * @return
     */
    Integer updatePurchaseOrderDetail(@Param("id") Integer id, @Param("purchase_number") double purchase_number, @Param("purchase_price") double purchase_price);

    /**
     * 修改采购订单总金额
     * @param id
     * @param price
     * @return
     */
    Integer updatePurchaseOrderPriceid(@Param("id") Integer id, @Param("price") Double price, @Param("freight") double freight, @Param("difference_price") double difference_price);

    /**
     * 搜索供应商
     * @param business_id
     * @return
     */
    List<HashMap> findBusinessSupplies(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 通过订单id查询订单信息和供应商名称电话
     * @param id
     * @return
     */
    PurchaseOrder findPurchaseInfoById(@Param("id") Integer id);

    /**
     * 查询采购订单详情总数(打印)
     * @param purchase_id
     * @return
     */
    Integer purchaseOrderDetailCount(@Param("purchase_id") Integer purchase_id);
    /**
     * 查询采购订单详情(打印)
     * @param purchase_id
     * @return
     */
    List<PurchaseOrderDetail> purchaseOrderDetail(@Param("purchase_id") Integer purchase_id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);


    /**
     * 所有采购单据总数
     * @return
     */
    Integer allPurchaseOrderCount(Paramt paramt);

    /**
     * 所有采购单据
     * @return
     */
    List<HashMap> allPurchaseOrder(Paramt paramt);

    /**
     * 所有采购单据总金额
     * @return
     */
    HashMap allPurchaseOrderTatolMoney(Paramt paramt);


    /**
     * 查询所有采购未到货总金额
     * @param paramt
     * @return
     */
    Double allNotArrivalTatolMoney(Paramt paramt);

    /**
     * 查询所有采购到货总金额
     * @param paramt
     * @return
     */
    Double allArrivalTatolMoney(Paramt paramt);

    /**
     * 计算这一年总采购金额
     * @param business_id
     * @return
     */
    HashMap yearPurchassPirce(@Param("bid") Integer business_id);

    /**
     * 计算这季度总采购金额
     * @param business_id
     * @return
     */
    HashMap quarterPurchassPrice(@Param("bid") Integer business_id);

    /**
     * 计算这月总采购金额
     * @param business_id
     * @return
     */
    HashMap monthPurchassPrice(@Param("bid") Integer business_id);

    /**
     * 计算 这周总采购金额
     * @param business_id
     * @return
     */
    HashMap weekPurchassPrice(@Param("bid") Integer business_id);

    /**
     * 计算 当天总采购金额
     * @param business_id
     * @return
     */
    HashMap daysPurchassPrice(@Param("bid") Integer business_id);

    /**
     * 自定义时间段总采购金额
     * @param business_id
     * @param startTime
     * @param endTime
     * @return
     */
    HashMap definitionPurchassPrice(@Param("bid") Integer business_id, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 导出待采购订单信息
     * @param bid
     * @param id 仓库id
     * @return
     */
    List<HashMap> excelPurchaserepor(@Param("business_id") Integer bid, @Param("id") Integer id);


    /**
     * 修改产品进价
     * @param goods_id
     * @param cost_price
     * @return
     */
    Integer  updateGoodsPrice(@Param("id") Integer goods_id, @Param("cost_price") Double cost_price);


    /**
     * 采购取消订单总数
     * @param bid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer cancelOrderCount(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 采购取消订单
     * @param bid
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> cancelOrder(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 回退取消订单
     * @param id
     * @return
     */
    Integer updatePurchaseStatus(@Param("id") Integer id);


    /**
     * 收款记录
     * @param id
     * @return
     */
    List<HashMap> selectOrderMoneyRecords(@Param("id") Integer id);

}
