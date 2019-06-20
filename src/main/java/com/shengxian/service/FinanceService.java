package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.IntegrGoods;
import com.shengxian.entity.Lous;
import com.shengxian.entity.Settlement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;

public interface FinanceService {

    /**
     * 商城订单
     * @param token
     * @param pageNo
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page mallMoney(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 线下订单
     * @param token
     * @param pageNo
     * @param staff_if 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page underLineMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 退货订单
     * @param token
     * @param pageNo
     * @param staff_if 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page returnMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime)throws NullPointerException;


    /**
     * 总销售订单
     * @param token
     * @param pageNo
     * @param staff_id
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    Page tatolSaleMoney(String token ,Integer role , Integer pageNo, Integer staff_id, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 销售财务结算
     * @param token
     * @return
     */
    Integer addFinanceSettlement(String token ,Integer role , Settlement settlement)throws NullPointerException, Exception;

    /**
     * 采购产品金额
     * @param token
     * @param pageNo
     * @param staff_if 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page purchaseMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 费用记录
     * @param token
     * @param pageNo
     * @param name 费用名称
     * @param startTime
     * @param endTime
     * @return
     */
    Page expenseMoney(String token ,Integer role , Integer pageNo, Integer staff_id, String name, String startTime, String endTime)throws NullPointerException;

    /**
     * 采购退货订单总金额金额
     * @param token
     * @param pageNo
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page purchaseReturnMoney(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 总采购订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    Page tatolPurchaseOrder(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime)throws NullPointerException;

    /**
     * 采购财务结算
     * @param token
     * @return
     */
    Integer aadPurchaseFinanceSettlement(String token ,Integer role , Settlement settlement)throws NullPointerException,Exception;

    /**
     * 商家财务结算记录
     * @param token
     * @return
     */
    Page settlementInfo(String token ,Integer role , Integer pageNo, String startTime, String endTime, Integer type)throws  NullPointerException;

    /**
     * 通过结算id查询结算信息
     * @param id
     * @return
     */
    HashMap settlementByid(Integer id);

    /**
     * 用户销售汇总
     * @param token
     * @return
     */
    Page userSaleProfitSummary(String token ,Integer role , Integer pageNo, String name,String goodsName, String startTime, String endTime,Integer bindindId)throws NullPointerException;

    /**
     * 用户销售明细
     * @param token
     * @return
     */
    Page userSaleProfitDetails(String token ,Integer role , Integer pageNo, String name ,String goodsName, String startTime, String endTime,Integer bindindId)throws NullPointerException;


    /**
     * 产品销售汇总
     * @param token
     * @return
     */
    Page goodsSaleProfitSummary(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime, Integer is)throws NullPointerException;

    /**
     * 产品销售明细
     * @param token
     * @return
     */
    Page goodsSaleProfitDetails(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime, Integer is) throws NullPointerException;


    /**
     * 添加欠条
     * @param token
     * @param
     * @return
     */
    Integer addIous(String token ,Integer role , Lous lous);

    /**
     * 欠条id查询欠条内容
     * @param id
     * @return
     */
    HashMap findLous(Integer id)throws NullPointerException;

    /**
     *销售风险订单记录总数
     * @param token
     * @param role
     * @param startTime
     * @param endTime
     * @return
     */
    Integer riskOrderCount(String token ,Integer role ,  String startTime, String endTime);

    /**
     * 销售风险订单记录
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    Page riskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime)throws NullPointerException ,Exception;

    /**
     * 采购风险订单记录总数
     * @param token
     * @param role
     * @param startTime
     * @param endTime
     * @return
     */
    Integer purchaseRiskOrderCount(String token ,Integer role ,  String startTime, String endTime);
    /**
     * 采购风险订单记录
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Page purchaseRiskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime)throws NullPointerException ,Exception;

    /**
     * 修改销售风险订单的实收款金额
     * @param id
     * @param money
     * @return
     */
    Integer updateRiskOrderMoney(Integer id, Double money );


    /**
     * 修改采购风险订单的实收款金额
     * @param id
     * @param money
     * @return
     */
    Integer updatePurchaseRiskOrderMoney(Integer id, Double money);

    /**
     * 销售收款分类
     * @param token
     * @param mold 0销售单，1退货单
     * @param startTime
     * @param endTime
     * @return
     */
    HashMap saleReceivablesType(String token ,Integer role , Integer mold, String startTime, String endTime)throws NullPointerException ,Exception;


    /**
     * 采购收款分类
     * @param token
     * @param mold 0销售单，1退货单
     * @param startTime
     * @param endTime
     * @return
     */
    HashMap purchaseReceivablesType(String token ,Integer role , Integer mold, String startTime, String endTime)throws NullPointerException ,Exception;

    /**
     * 最后结算时间
     * @param token
     * @param type
     * @return
     */
    HashMap finalSettlementTime(String token ,Integer role , Integer type)throws NullPointerException,Exception;

    /**
     * 每天产品库存情况总数
     * @param token
     * @param role
     * @param startTime
     * @param endTime
     * @return
     */
    Integer dayGoodsInventorySituationCount(String token ,Integer role  ,String startTime ,String endTime);

    /**
     * 每天产品库存情况
     * @param token
     * @param role
     * @param pageNo
     * @param name 产品名称或编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page dayGoodsInventorySituation(String token ,Integer role ,Integer pageNo,String name ,String startTime ,String endTime )throws Exception;


    /**
     * 产品风控总数
     * @param token
     * @param role
     * @param startTime
     * @param endTime
     * @return
     */
    Integer goodsWindControlCount(String token ,Integer role ,String startTime ,String endTime);

    /**
     * 产品风控
     * @param token
     * @param role
     * @param pageNo
     * @param name 产品名称或编号
     * @return
     * @throws Exception
     */
    Page goodsWindControl(String token ,Integer role ,Integer pageNo,String name ,String startTime ,String endTime )throws Exception;

    /**
     * 用户销售明细导出
     * @param token
     * @param role
     * @param name
     * @param startTime
     * @param endTime
     * @param bindindId
     * @return
     */
    HSSFWorkbook userSaleDetailDownload(String token ,Integer role ,   String name , String startTime, String endTime ,Integer bindindId );

}
