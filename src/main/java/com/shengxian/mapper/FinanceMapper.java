package com.shengxian.mapper;




import com.shengxian.entity.Lous;
import com.shengxian.entity.Paramt;
import com.shengxian.entity.Settlement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface FinanceMapper {

    /**
     * 商城订单总数
     * @return
     */
    Integer mallMoneyCount(Paramt param);

    /**
     * 商城订单金额
     * @return
     */
    List<HashMap> mallMoney(Paramt param);

    /**
     * 条件搜索商城的应收、实收的总金额，结算不清零，留数据对比
     * @param paramt
     * @return
     */
    HashMap tatolMallMoney(Paramt paramt);

    /**
     * 线下订单总数
     * @return
     */
    Integer underLineMoneyCount(Paramt paramt);

    /**
     * 线下订单金额
     * @return
     */
    List<HashMap> underLineMoney(Paramt paramt);

    /**
     * 条件搜索线下的应收、实收的总金额，结算不清零，留数据对比
     * @param paramt
     * @return
     */
    HashMap tatolUnderLineMoney(Paramt paramt);

    /**
     * 退货产品总数
     * @return
     */
    Integer returnMoneyCount(Paramt paramt);

    /**
     * 退货产品金额
     * @return
     */
    List<HashMap> returnMoney(Paramt paramt);

    /**
     * 条件搜索退货单的应收、实收的总金额
     * @param paramt
     * @return
     */
    HashMap tatolreturnMoney(Paramt paramt);


    /**
     * 查询商家最后结算时间
     * @param busienss_id
     * @return
     */
    String lastSettlementTime(@Param("business_id") Integer busienss_id, @Param("type") Integer type);

    /**
     * 最后一次未结算的账单
     * @param busienss_id
     * @return
     */
    Double lastNotSettlementBills(@Param("business_id") Integer busienss_id, @Param("type") Integer type);



    /**
     * 总销售产品金额总数
     * @return
     */
    Integer tatolSaleMoneyCount(Paramt paramt);

    /**
     * 总销售产品金额
     * @return
     */
    List<HashMap> tatolSaleMoney(Paramt paramt);

    /**
     * 条件搜索总销售产品订单总金额
     * @param paramt
     * @return
     */
    HashMap tatolSalePrice(Paramt paramt);


    /**
     * 条件搜索总销售产品订单总金额 (当天销售的订单)
     * @param paramt
     * @return
     */
    HashMap notTatolSalePrice(Paramt paramt);

    /**
     * 当天所有订单的应收/实收/未付款/欠款订单的总金额
     * @param bid
     * @param time 结算时间 用来判断欠款的
     * @return
     */
    HashMap dayAllSaleOrderTatolMoney(@Param("business_id") Integer bid, @Param("time") String time);

    /**
     *查询商家未结算的完成的订单
     * @param bid 商家id
     * @return
     */
    List<Integer> findNotSettlementOrder(@Param("business_id") Integer bid);

    /**
     * 通过订单id修改未结算的订单状态Settlement
     * @param id 订单id
     * @return
     */
    Integer updateNotSettlementOrder(@Param("id") Integer id, @Param("is_settlement") Integer is_settlement);


    /**
     * 采购产品金额总数
     * @param paramt
     * @return
     */
    Integer purchaseMoneyCount(Paramt paramt);

    /**
     * 采购产品金额
     * @param paramt
     * @return
     */
    List<HashMap> purchaseMoney(Paramt paramt);

    /**
     * 条件搜索采购订单总价金额（默认当天时间）
     * @param paramt
     * @return
     */
    HashMap tatolPurchaseMoney(Paramt paramt);

    /**
     * 条件搜索采购订单总价金额（默认当天时间）
     * @param paramt
     * @return
     */
    HashMap notTatolPurchaseMoney(Paramt paramt);


    /**
     * 费用支出总数
     * @param paramt
     * @return
     */
    Integer expenseMoneyCount(Paramt paramt);

    /**
     *费用支出
     * @param paramt
     * @return
     */
    List<HashMap> expenseMoney(Paramt paramt);

    /**
     * 费用支出统计
     * @param paramt
     * @return
     */
    Double expenseMoneyTotalCount(Paramt paramt);

    /**
     * 费用支出总金额
     * @param paramt
     * @return
     */
    //HashMap tatolExpenseMoney(Paramt paramt);

    /**
     * 当天费用支出总金额
     * @param bid
     * @param dayTime
     * @return
     */
    //HashMap dayExpenseTatolMoney(@Param("business_id") Integer bid,@Param("dayTime") String dayTime);


    /**
     * 采购退货产品总数
     * @param paramt
     * @return
     */
    Integer purchaseReturnMoneyCount(Paramt paramt);

    /**
     * 采购退货产品
     * @param paramt
     * @return
     */
    List<HashMap> purchaseReturnMoney(Paramt paramt);

    /**
     * 条件搜索采购退货产品总金额1
     * @param paramt
     * @return
     */
    HashMap tatolPurchaseReturnMoney(Paramt paramt);

    /**
     * 总采购订单总数
     * @param paramt
     * @return
     */
    Integer tatolPurchaseOrderCount(Paramt paramt);

    /**
     * 总采购订单
     * @param paramt
     * @return
     */
    List<HashMap> tatolPurchaseOrder(Paramt paramt);

    /**
     * 条件搜索总采购订单总金额
     * @param paramt
     * @return
     */
  // HashMap tatolPurchaseOrderMoney(Paramt paramt);

    /**
     * 当天总采购订单总金额
     * @param bid
     * @param time
     * @return
     */
    HashMap dayPurchaseOrderTatolMoney(@Param("business_id") Integer bid, @Param("time") String time);

    /**
     * 通过商家id查询采购未结算的完成订单
     * @param bid 商家id
     * @return
     */
    List<Integer> purchaseNotSettlement(@Param("business_id") Integer bid);

    /**
     * 修改采购未结算的订单
     * @param order_id
     * @return
     */
    Integer updatePurchaseNotSettlement(@Param("id") Integer order_id, @Param("is_settlement") Integer is_settlement);

    /**
     * 通过商家id查询未结算费用支出
     * @param bid
     * @return
     */
    List<Integer> notSettlementExpense(@Param("business_id") Integer bid);

    /**
     * 修改未结算的费用支出
     * @param id
     * @return
     */
    Integer updateNotSettlementExpense(@Param("id") Integer id);


    /**
     * 财务结算时查询五种收款分类
     * @param bid
     * @param type 1 销售 ，2 采购
     * @param time
     * @return
     */
    Settlement fiveMoneyType(@Param("bid") Integer bid ,@Param("type") Integer type, @Param("time") String time);




    /**
     * 添加财务结算记录
     * @param settlement
     * @return
     */
    Integer addSettlement(Settlement settlement);

    /**
     * 财务结算记录总数
     * @param bid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer settlementInfoCount(@Param("business_id") Integer bid, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 财务结算记录
     * @param bid
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> settlementInfo(@Param("business_id") Integer bid, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 财务结算总金额（分销售和采购）
     * @param bid
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    Double settlemenTotalMoney(@Param("business_id") Integer bid, @Param("type") Integer type, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 通过结算id查询结算信息
     * @param id
     * @return
     */
    HashMap settlementByid(@Param("id") Integer id);

    /**
     * 用户销售汇总总数
     * @param paramt
     * @return
     */
    Integer userSaleProfitSummaryCount(Paramt paramt);

    /**
     * 用户销售汇总
     * @param paramt
     * @return
     */
    List<HashMap> userSaleProfitSummary(Paramt paramt);

    /**
     * 用户销售汇总总金额
     * @param paramt
     * @return
     */
    HashMap userSaleProfitSummaryTatolMoney(Paramt paramt);

    /**
     * 用户销售明细总数
     * @param paramt
     * @return
     */
    Integer userSaleProfitDetailsCount(Paramt paramt);

    /**
     * 用户销售明细
     * @param paramt
     * @return
     */
    List<HashMap> userSaleProfitDetails(Paramt paramt);

    /**
     * 用户销售明细总金额
     * @param paramt
     * @return
     */
    HashMap userSaleProfitDetailsTatolMoney(Paramt paramt);

    /**
     * 产品销售汇总总数
     * @param paramt
     * @return
     */
    Integer goodsSaleProfitSummaryCount(Paramt paramt);

    /**
     * 产品销售汇总
     * @param paramt
     * @return
     */
    List<HashMap> goodsSaleProfitSummary(Paramt paramt);

    /**
     * 产品销售汇总总金额
     * @param paramt
     * @return
     */
    HashMap goodsSaleProfitSummaryTatolMoney(Paramt paramt);

    /**
     * 产品销售明细总数
     * @param paramt
     * @return
     */
    Integer goodsSaleProfitDetailsCount(Paramt paramt);

    /**
     * 产品销售明细
     * @param paramt
     * @return
     */
    List<HashMap> goodsSaleProfitDetails(Paramt paramt);

    /**
     * 产品销售明细总金额
     * @param paramt
     * @return
     */
    HashMap goodsSaleProfitDetailsTatolMoney(Paramt paramt);

    /**
     * 添加欠条
     * @return
     */
    Integer addIous(Lous lous);

    /**
     * 欠条id查询欠条内容
     * @param id
     * @return
     */
    HashMap findIous(@Param("id") Integer id);

    /**
     * 销售风险订单总数
     * @param paramt
     * @return
     */
    Integer riskOrderCount(Paramt paramt);

    /**
     * 销售风险订单
     * @param paramt
     * @return
     */
    List<HashMap> riskOrder(Paramt paramt);

    /**
     * 销售风险总金额
     * @param paramt
     * @return
     */
    HashMap riskOrderTatolMoney(Paramt paramt);

    /**
     * 采购风险订单总数
     * @param paramt
     * @return
     */
    Integer purchaseRiskOrderCount(Paramt paramt);

    /**
     * 采购风险订单
     * @param paramt
     * @return
     */
    List<HashMap> purchaseRiskOrder(Paramt paramt);

    /**
     * 采购风险总金额
     * @param paramt
     * @return
     */
    HashMap purchaseRiskOrderTatolMoney(Paramt paramt);


    /**
     * 通过订单id查询订单第一次收款分类记录
     * @param id
     * @return
     */
    HashMap selectOrderOneMoneyType(@Param("id") Integer id);

    /**
     * 通过订单id查询订单第二次收款分类记录
     * @param id
     * @return
     */
    HashMap selectOrderTwoMoneyType(@Param("id") Integer id);



    /**
     * 通过分类记录id修改订单收款分类金额
     * @param id
     * @param money
     * @return
     */
    Integer updateOrderMoneyType(@Param("id") Integer id ,@Param("money") Double money ,@Param("time") Date time );

    /**
     * 修改销售风险订单的实收款金额
     * @param id 订单id
     * @param money 实收款金额
     * @return
     */
    Integer updateRiskOrderMoney(@Param("id") Integer id, @Param("money") Double money );



    /**
     * 通过采购id查询订单第一次收款分类金额
     * @param id
     * @return
     */
    HashMap selectPurchaseOneMoneyType(@Param("id") Integer id);

    /**
     * 通过订单id查询订单第二次收款分类记录
     * @param id
     * @return
     */
    HashMap selectPurchaseTwoMoneyType(@Param("id") Integer id);

    /**
     * 通过分类记录id修改采购收款分类金额
     * @param id
     * @param money
     * @return
     */
    Integer updateOPurchaseMoneyType(@Param("id") Integer id ,@Param("money") Double money ,@Param("time") Date time);

    /**
     * 修改采购风险订单的实收款金额
     * @param id 订单id
     * @param money 实收款金额
     * @return
     */
    Integer updatePurchaseRiskOrderMoney(@Param("id") Integer id, @Param("money") Double money);

    /**
     * 销售收款分类
     * @param bid
     * @param mold 0销售单，1退货单
     * @param startTime 开始时间
     * @param endTime 结算时间
     * @return
     */
    HashMap saleReceivablesType(@Param("bid") Integer bid, @Param("mold") Integer mold, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 采购收款分类
     * @param bid
     * @param mold 0销售单，1退货单
     * @param startTime 开始时间
     * @param endTime 结算时间
     * @return
     */
    HashMap purchaseReceivablesType(@Param("bid") Integer bid, @Param("mold") Integer mold, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 销售欠款订单收的款
     * @return
     */
    Double aefundsOrderCollectMoney(Paramt paramt);

    /**
     * 采购欠款订单收的款
     * @param paramt
     * @return
     */
    Double aefundsPurchaseCollectMoney(Paramt paramt);


    /**
     * 最后结算时间
     * @param bid
     * @param type
     * @return
     */
    HashMap finalSettlementTime(@Param("bid") Integer bid, @Param("type") Integer type);

    /**
     * 每天产品库存情况总数
     * @param paramt
     * @return
     */
    Integer dayGoodsInventorySituationCount(Paramt paramt);

    /**
     * 每天产品库存情况
     * @param paramt
     * @return
     */
    List<HashMap> dayGoodsInventorySituation(Paramt paramt);

    /**
     * 每天产品库存情况总数量
     * @param paramt
     * @return
     */
    HashMap dayGoodsInventorySituationTotalNum(Paramt paramt);


    /**
     * 产品风控总数
     * @param paramt
     * @return
     */
    Integer goodsWindControlCount(Paramt paramt);

    /**
     * 产品风控
     * @param paramt
     * @return
     */
    List<HashMap> goodsWindControl(Paramt paramt);


    /**
     * 用户销售明细导出
     * @param paramt
     * @return
     */
    List<HashMap> userSaleDetailDownload(Paramt paramt);

    /**
     * 用户销售明细导出总金额
     * @param paramt
     * @return
     */
    Double userSaleDetailDownloadTatolMoney(Paramt paramt);

}


