package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.PurchaseOrder;
import com.shengxian.entity.PurchaseOrderDetail;
import com.shengxian.entity.Template;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 采购service接口层
 *
 * @Author: yang
 * @Date: 2018/9/18
 * @Version: 1.0
 */
public interface PurchaseService {

    /**
     * 生产采购订单号
     * @param token
     * @return
     */
    String createPurchaseOrder(String token ,Integer role) throws NullPointerException;

    /**
     * 采购和销售时搜索产品
     * @param token
     * @param name
     * @return
     */
    List<HashMap> selectBusinessGoods(String token ,Integer role, String name)throws NullPointerException;

    /**
     *查询商家的产品收藏
     * @param token
     * @return
     */
    List<HashMap> findBusinessGoodsCollection(String token ,Integer role , Integer suppliers_id, String name, String startTime, String endTime)throws NullPointerException;

    /**
     * 添加商家收藏产品
     * @param token
     * @param goods_id
     * @return
     */
    Integer addBusinessGoodsCollection(String token  ,Integer role, Integer goods_id, Integer suppliers_id)throws NullPointerException;

    /**
     * 取消商家收藏的产品
     * @param id
     * @return
     */
    Integer deleteBusinessGoodsCollection(String id);

    /**
     * 通过产品id搜索产品（采购和销售）
     * @param goods_id 产品id
     * @param suppliers_id 供应商id
     * @return
     */
    List<HashMap> selectBusinessGoodsById(String token, String goods_id, Integer suppliers_id);

    /**
     * 查询商家采购的订单
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    List<HashMap> selectPurchaseOrder(String token ,Integer role , Integer suppliers_id, String startTime, String endTime)throws NullPointerException;

    /**
     * 通过订单id查询采购产品详情
     * @param order_id
     * @return
     */
    List<HashMap> selectPurchaseOrderDetailById(String order_id)throws NullPointerException ;

    /**
     * 添加采购产品单
     * @param token
     * @param purchaseOrder
     * @return
     */
    Integer addPurchaseOrder(String token  ,Integer role, PurchaseOrder purchaseOrder) throws NullPointerException;

    /**
     * 采购退货产品
     * @param token
     * @param purchaseOrder
     * @return
     */
    Integer addPurchaseReturnOrder(String token  ,Integer role , PurchaseOrder purchaseOrder)throws NullPointerException;

    /**
     * 代采购报表总数
     * @param token
     * @param role
     * @return
     */
    Integer PurchasereportCount(String token  ,Integer role);

    /**
     * 代采购报表
     * @param token
     * @return
     */
    Page Purchasereport(String token  ,Integer role , Integer pageNo, Integer id)throws NullPointerException;

    /**
     * 待审核，未付款，欠款总数
     * @param token
     * @param status 0待审核 ，1确认到货
     * @param state 0未付款，1欠款
     * @return
     */
    Integer stayAuditedCount(String token , Integer role, Integer status, Integer state)throws NullPointerException;

    /**
     * 待审核(采购未到货的订单)
     * @param token
     * @return
     */
   Page stayAudited(String token , Integer role, Integer pageNo, String name) throws  NullPointerException;

    /**
     * 采购确认
     * @param token
     * @param purchase_id 产品id
     * @param mold 0采购单，1采购退货单
     * @return
     * @throws IndexOutOfBoundsException
     */
    Integer updatePurchaseConfirmArrival(String token ,Integer role , Integer purchase_id, Integer mold, Integer status)throws IndexOutOfBoundsException,NullPointerException;


    /**
     * 未审核的采购订单直接收款
     * @param token
     * @param role
     * @param id 订单id
     * @param state
     * @param mold
     * @paran price
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer updateOrderReceivables(String token ,Integer role, Integer id , Integer state, Integer mold , Integer type, Double money )throws NullPointerException ,Exception;

    /**
     * 未审核的采购订单取消
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
     * 查询采购未付款订单
     * @param token
     * @param pageNo
     * @param name
     * @param
     * @return
     */
    Page purchaseUnpaidOrder(String token  ,Integer role , Integer pageNo,  String name ,String number, String staffName)throws NullPointerException;

    /**
     * 查询采购欠款订单
     * @param token
     * @param pageNo
     * @param name
     * @param
     * @return
     */
    Page purchaseArrearsOrder(String token  ,Integer role , Integer pageNo, String name ,String number ,String staffName)throws NullPointerException;


    /**
     * 查询采购已完成的订单
     * @param token
     * @param pageNo
     * @param name
     * @param
     * @return
     */
    Page findPurchaseOrder(String token  ,Integer role , Integer pageNo , String name ,String number , String startTime, String endTime,String staffName)throws NullPointerException;

    /**
     * 确认收款和确认欠款
     * @param id
     * @param state
     * @param type
     * @return
     */
    Integer updatePurchaseOrderState(String token ,Integer role , Integer id, Integer state, Integer type, double money)throws NullPointerException, Exception;

    /**
     * 订单详情
     * @param id
     * @return
     */
    HashMap  findPurchaseOrderDetail(Integer id)throws Exception;

    /**
     * 删除采购详情产品
     * @param id 详情id
     * @return
     */
    Integer deletePurchaseDetail(Integer id ,Integer mold)throws NullPointerException , Exception;

    /**
     * 修改采购订单金额
     * @param purchaseOrder
     * @param
     * @return
     */
    Integer updatePurchaseOrderPrice(String token ,Integer role, PurchaseOrder purchaseOrder)throws NullPointerException, Exception;


    /**
     * 搜索商家供应商
     * @param token
     * @return
     */
    List<HashMap> findBusinessSupplies(String token ,Integer role, String name)throws NullPointerException;

    /**
     * 打印模板
     * @param token
     * @param type 0采购订单,1采购退货订单，2销售订单，3临时销售订单，4退货销售订单
     * @return
     */
    Template printTemplate(String token  ,Integer role , Integer type) throws NullPointerException;


    /**
     * 打印采购订单
     * @param token
     * @param order_id 订单id
     * @return
     */
    PurchaseOrder purchasePrint(String token  ,Integer role , Integer order_id)throws Exception;

    /**
     * 打印采购订单详情
     * @param pageNo
     * @param order_id 订单id
     * @return
     */
    List<PurchaseOrderDetail> purchasePrintDetail(Integer pageNo, Integer order_id ,Integer pageSize);

    /**
     * 修改订单打印的次数
     * @param order_id
     * @return
     */
    Integer updatePrintFrequ(Integer order_id);

    /**
     * 所有采购单据
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    Page allPurchaseOrder(String token  ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime, Integer mold);


    /**
     * 计算这一年总采购金额
     * @param token
     * @return
     */
    HashMap yearPurchassPirce(String token  ,Integer role ) throws NullPointerException;

    /**
     * 计算这季度总采购金额
     * @param token
     * @return
     */
    HashMap quarterPurchassPrice(String token  ,Integer role )throws NullPointerException;

    /**
     * 计算这月总采购金额
     * @param token
     * @return
     */
    HashMap monthPurchassPrice(String token  ,Integer role )throws NullPointerException;


    /**
     * 计算这周总采购金额
     * @param token
     * @return
     */
    HashMap weekPurchassPrice(String token  ,Integer role )throws NullPointerException;

    /**
     * 计算当天总采购金额
     * @param token
     * @return
     */
    HashMap daysPurchassPrice(String token  ,Integer role )throws NullPointerException;

    /**
     * 自定义时间段总采购金额
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    HashMap definitionPurchassPrice(String token  ,Integer role , String startTime, String endTime)throws NullPointerException;

    /**
     * 导出待采购订单信息
     * @param token
     * @return
     */
    HSSFWorkbook excelPurchasereport(String token  ,Integer role , Integer id);

    /**
     * 修改产品进价
     * @param goods_id
     * @param cost_price
     * @return
     */
    Integer  updateGoodsPrice(Integer goods_id, Double cost_price)throws Exception;

    /**
     * 采购取消订单总数
     * @param token
     * @param role
     * @param startTime
     * @param endTime
     * @return
     */
    Integer cancelOrderCount(String token  ,Integer role , String startTime, String endTime);

    /**
     * 采购取消订单
     * @param token
     * @param pageNo
     * @param startTime
     * @param endTime
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Page cancelOrder(String token  ,Integer role , Integer pageNo, String startTime, String endTime)throws NullPointerException ,Exception;


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
    List<HashMap> selectOrderMoneyRecords(Integer id);
}
