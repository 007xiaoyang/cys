package com.shengxian.service.impl;

import com.shengxian.common.util.*;
import com.shengxian.entity.Lous;
import com.shengxian.entity.Paramt;
import com.shengxian.entity.Settlement;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.FinanceMapper;
import com.shengxian.mapper.OrderMapper;
import com.shengxian.service.FinanceService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-12-02
 * @Version: 1.0
 */
@Service
public class FinanceServiceImpl implements FinanceService {

    @Resource
    private FinanceMapper financeMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private OrderMapper orderMapper;

    //商城订单
    @Override
    public Page mallMoney(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime)throws NullPointerException  {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //商城订单总数
        Integer totalCount = financeMapper.mallMoneyCount(new Paramt(bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //商城订单
        List<HashMap> hashMaps = financeMapper.mallMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }

        HashMap hashMap = new HashMap();

        //总销售金额（销售商城订单）
        HashMap tatolHashMap = financeMapper.notTatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"1",1,0));
        hashMap.put("tatol",tatolHashMap.get("receivable").toString());

        //销售单欠款收的钱（销售商城订单）
        Double money = financeMapper.aefundsOrderCollectMoney(new Paramt(bid, 0, 1, name, number, startTime, endTime));
        hashMap.put("money2",money);

        //完成订单应收金额、实收（销售商城订单）
        HashMap payHashMap = financeMapper.tatolMallMoney(new Paramt(bid,name,number,startTime,endTime,"3",1));
        hashMap.put("tatol_receivable",(Double.valueOf(payHashMap.get("receivable").toString()) + money));//完成的订单应收款 + 欠款单收的款
        hashMap.put("tatol_pay",(Double.valueOf(payHashMap.get("pay").toString()) + money)); //完成的订单实收款 + 欠款单收的款

        //欠款订单（销售商城订单）
        HashMap arrearsHashMap = financeMapper.tatolMallMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("tatol_arrears",(Double.valueOf(arrearsHashMap.get("receivable").toString())-money));//欠款单的应收款 - 欠款单收的款

        //未付款订单（销售商城订单）
        HashMap unpaidHashMap = financeMapper.tatolMallMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("tatol_unpaid",unpaidHashMap.get("receivable").toString());

        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //线下订单
    @Override
    public Page underLineMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime)throws NullPointerException  {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //线下订单总数
        Integer totalCount = financeMapper.underLineMoneyCount(new Paramt(bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //线下订单总数
        List<HashMap> hashMaps = financeMapper.underLineMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }

        HashMap hashMap = new HashMap();

        //总销售金额(销售线下订单)
        HashMap tatolHashMap = financeMapper.notTatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"1",0,0));
        hashMap.put("tatol",tatolHashMap.get("receivable").toString());

        //欠款单收的钱(销售线下订单)
        Double money = financeMapper.aefundsOrderCollectMoney(new Paramt(bid, 0, 0, name, number, startTime, endTime));
        hashMap.put("money2",money);

        //应收金额、实收(销售线下订单)
        HashMap payHashMap = financeMapper.tatolUnderLineMoney(new Paramt(bid,name,number,startTime,endTime,"3",1));
        hashMap.put("tatol_receivable",(Double.valueOf(payHashMap.get("receivable").toString())+ money));//完成的订单应收款 + 欠款单收的款
        hashMap.put("tatol_pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单实收款 + 欠款单收的款

        //欠款(销售线下订单)
        HashMap arrearsHashMap = financeMapper.tatolUnderLineMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("tatol_arrears",(Double.valueOf(arrearsHashMap.get("receivable").toString()) - money));//欠款单的应收款 - 欠款单收的款

        //未付款(销售线下订单)
        HashMap unpaidHashMap = financeMapper.tatolUnderLineMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("tatol_unpaid",unpaidHashMap.get("receivable").toString());

        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //销售退货订单
    @Override
    public Page returnMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime)throws NullPointerException  {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //销售退货订单总数
        Integer totalCount = financeMapper.returnMoneyCount(new Paramt( bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //销售退货订单
        List<HashMap> hashMaps = financeMapper.returnMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }

        HashMap hash = new HashMap();

        //（退货单没有商城和线下区分）

        //欠款单收的钱
        Double money = financeMapper.aefundsOrderCollectMoney(new Paramt(bid, 1, null, name, number, startTime, endTime));
        hash.put("money2",money);

        //总退货金额
        HashMap tatolHashMap = financeMapper.notTatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"1",null,1));
        hash.put("tatol",tatolHashMap.get("receivable").toString());


        //退货欠款
        HashMap arrearsHashMap = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hash.put("tatol_arrears",(Double.valueOf(arrearsHashMap.get("receivable").toString()) - money));//欠款单的应收款 - 欠款单收的款

        //退货应收金额、实收
        HashMap payHashMap = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"3",1));
        hash.put("tatol_pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单实收款 + 欠款单收的款
        hash.put("tatol_receivable",(Double.valueOf(payHashMap.get("receivable").toString()) + money));//完成的订单应收款 + 欠款单收的款


        //退货未付款
        HashMap unpaidHashMap = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hash.put("tatol_unpaid",unpaidHashMap.get("receivable").toString());

        page.setHashMap(hash);
        page.setRecords(hashMaps);
        return page;
    }


    //总销售订单
    @Override
    public Page tatolSaleMoney(String token ,Integer role , Integer pageNo, Integer staff_id , String name, String number, String startTime, String endTime) throws NullPointerException {

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

      //查询商家最后结算时间
        String time = financeMapper.lastSettlementTime(bid,0);
       /*   if (IntegerUtils.isEmpty(time)){
            int i = startTime.compareTo(time);
            if (i <= 0){
                throw new NullPointerException("开始时间不能小于结算时间哟");
            }
        }*/

        //总销售订单总数
        Integer totalCount = financeMapper.tatolSaleMoneyCount(new Paramt( bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //总销售订单
        List<HashMap> hashMaps = financeMapper.tatolSaleMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        HashMap hashMap = new HashMap();

        //总销售不区分商城线下
        //总销售金额
        HashMap tatolHashMap = financeMapper.notTatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"1",null,0));
        hashMap.put("tatol",tatolHashMap.get("receivable").toString());

        //欠款单收的钱
        Double money = financeMapper.aefundsOrderCollectMoney(new Paramt(bid, 0, null, name, number, startTime, endTime));
        hashMap.put("money2",money);

        //销售应收金额、实收
        HashMap payHashMap = financeMapper.tatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"3",1));
        hashMap.put("tatol_receivable",(Double.valueOf(payHashMap.get("receivable").toString()) + money));//完成的订单应收款 + 欠款单收的款
        hashMap.put("tatol_pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单实收款 + 欠款单收的款

        //销售欠款
        HashMap arrearsHashMap = financeMapper.tatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("tatol_arrears",(Double.valueOf(arrearsHashMap.get("receivable").toString()) - money));//欠款单的应收款 - 欠款单收的款

        //销售未付款
        HashMap unpaidHashMap = financeMapper.tatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("tatol_unpaid",unpaidHashMap.get("receivable").toString());


        HashMap hashMap1 = new HashMap();
        //总退货金额
        HashMap tatolHashMap1 = financeMapper.notTatolSalePrice(new Paramt(bid,name,number,startTime,endTime,"1",null,1));
        hashMap1.put("tatol",tatolHashMap1.get("receivable").toString());

        //欠款单收的钱
        Double money2 = financeMapper.aefundsOrderCollectMoney(new Paramt(bid, 1, null, name, number, startTime, endTime));
        hashMap1.put("money2",money2);

        //退货应收金额、实收
        HashMap payHashMap1 = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"3",1));
        hashMap1.put("tatol_receivable",(Double.valueOf(payHashMap1.get("receivable").toString()) + money2));//完成的订单应收款 + 欠款单收的款
        hashMap1.put("tatol_pay",(Double.valueOf(payHashMap1.get("pay").toString()) + money2));//完成的订单实收款 + 欠款单收的款

        //退货欠款
        HashMap arrearsHashMap1 = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap1.put("tatol_arrears",Double.valueOf(arrearsHashMap1.get("receivable").toString()) - money2);//欠款单的应收款 - 欠款单收的款
        //退货未付款
        HashMap unpaidHashMap1 = financeMapper.tatolreturnMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap1.put("tatol_unpaid",unpaidHashMap1.get("receivable").toString());

        //当天所有订单的应收/实收/未付款/欠款订单的总金额
        HashMap day = financeMapper.dayAllSaleOrderTatolMoney(bid,time);

        //获取销售结算记录未结清的账单
        Double bills = financeMapper.lastNotSettlementBills(bid,0);
        hashMap.put("bills",bills); //未结清的账单

        hashMap.put("day",day);
        page.setHashMap(hashMap);
        page.setHashMap1(hashMap1);
        page.setRecords(hashMaps);
        return page;
    }

    //销售财务结算
    @Override
    @Transactional
    public Integer addFinanceSettlement(String token ,Integer role , Settlement settlement)throws NullPointerException, Exception {

        //店铺登录默认0
        Integer staffId = 0;

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //判断商家当天是否结算过
        Settlement set = orderMapper.financialSettlement(bid,0);
        if (set != null && DateUtil.compareDate( set.getTime() ,DateUtil.getDay()) == true){
            throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
        }

        //通过商家id查询销售未结算的订单
        List<Integer> order_id = financeMapper.findNotSettlementOrder(bid);
        if (order_id == null || order_id.size() == 0){
            throw new NullPointerException("您已经没有未结算的订单");
        }
        for (Integer id:order_id ) {
            //通过订单id修改未结算的订单状态Settlement
            financeMapper.updateNotSettlementOrder(id,1);//结算
        }


        if (role == 2){
            staffId = shopMapper.findStaffIdByToken(token);
        }
        //通过商家id查询未结算费用支出
        List<Integer> eid = financeMapper.notSettlementExpense(bid);
        for (Integer id:eid ) {
            financeMapper.updateNotSettlementExpense(id);
        }

        settlement.setStaff_id(staffId);
        settlement.setBusiness_id(bid);
        settlement.setCreate_time(new Date());
        settlement.setOldTime(  set == null ? null : set.getCreate_time() );

        //通过上次结算时间查询5种收款分类的金额
        Settlement settl = financeMapper.fiveMoneyType(bid, 1, set == null ? null : set.getTime() );
        settlement.setWx(settl.getWx());
        settlement.setAlipay(settl.getAlipay());
        settlement.setCash(settl.getCash());
        settlement.setBankcard(settl.getBankcard());
        settlement.setOther(settl.getOther());

        //财务结算时查询五种（退货的）收款分类
        Settlement sett2 = financeMapper.fiveRetreatMoneyType(bid, 1, set == null ? null : set.getTime() );
        settlement.setrWx(sett2.getrWx());
        settlement.setrAlipay(sett2.getrAlipay());
        settlement.setrCash(sett2.getrCash());
        settlement.setrBankcard(sett2.getrBankcard());
        settlement.setrOther(sett2.getrOther());
       //添加销售财务结算记录
        return financeMapper.addSettlement(settlement);
    }

    //采购产品金额
    @Override
    public Page purchaseMoney(String token ,Integer role , Integer pageNo, Integer staff_if, String name, String number, String startTime, String endTime) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        HashMap hashMap = new HashMap();

        //采购欠款订单收的款
        Double money = financeMapper.aefundsPurchaseCollectMoney(new Paramt(bid,0,name,number,startTime,endTime));
        hashMap.put("money2",money);

        //总采购
        HashMap tatolHashMap = financeMapper.notTatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,0));
        hashMap.put("tatol",tatolHashMap.get("price").toString());

        //采购欠款
        HashMap arrearsHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"1",1));
        hashMap.put("arrears",(Double.valueOf(arrearsHashMap.get("price").toString()) - money));//欠款的订单：欠款 - 欠款收的款

        //采购应收、实收
        HashMap payHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单：实收款款+ 欠款收的款
        hashMap.put("price",(Double.valueOf(payHashMap.get("price").toString()) + money));//完成的订单：应收款+ 欠款收的款

        //采购未付款
        HashMap unpaidHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("unpaid",unpaidHashMap.get("price").toString());


        //采购产品金额总数
        Integer totalCount = financeMapper.purchaseMoneyCount(new Paramt(bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //采购产品金额
        List<HashMap> hashMaps = financeMapper.purchaseMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.purchaseMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);

        return page;
    }

    //费用记录
    @Override
    public Page expenseMoney(String token ,Integer role , Integer pageNo, Integer staff_id, String name, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        HashMap hash = new HashMap();

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //费用支出总数
        Integer totalCount = financeMapper.expenseMoneyCount(new Paramt(bid,staff_id,name,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //费用支出
        List<HashMap> hashMaps = financeMapper.expenseMoney(new Paramt(bid,staff_id ,name,startTime,endTime,page.getStartIndex(),page.getPageSize()));

        //分类统计费用金额
        Double out = financeMapper.expenseMoneyTotalCount(new Paramt(bid, staff_id, name, startTime, endTime, 0));
        Double enter = financeMapper.expenseMoneyTotalCount(new Paramt(bid, staff_id, name, startTime, endTime, 1));
        hash.put("out" ,out);
        hash.put("enter" ,enter);


        page.setRecords(hashMaps);
        page.setHashMap(hash);
        return page;
    }

    //采购退货订单总金额金额
    @Override
    public Page purchaseReturnMoney(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //采购退货产品总数
        Integer totalCount = financeMapper.purchaseReturnMoneyCount(new Paramt(bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,totalCount);

        //采购退货产品
        List<HashMap> hashMaps = financeMapper.purchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.purchaseMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        HashMap hashMap = new HashMap();

        //采购欠款订单收的款
        Double money = financeMapper.aefundsPurchaseCollectMoney(new Paramt(bid,1,name,number,startTime,endTime));
        hashMap.put("money2",money);

        //总采购退货
        HashMap tatolHashMap = financeMapper.notTatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,1));
        hashMap.put("tatol",tatolHashMap.get("price").toString());

        //采购退货应收、实收
        HashMap payHashMap = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单：实收款款+ 欠款收的款
        hashMap.put("price",(Double.valueOf(payHashMap.get("price").toString()) + money));//完成的订单：应收款+ 欠款收的款

        //采购退货未付款
        HashMap unpaidHashMap = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("unpaid",unpaidHashMap.get("price").toString());


        //采购退货欠款
        HashMap arrearsHashMap = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"1",1));
        hashMap.put("arrears",(Double.valueOf(arrearsHashMap.get("price").toString()) - money));//欠款的订单：欠款 - 欠款收的款

        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //总采购订单
    @Override
    public Page tatolPurchaseOrder(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

      //查询商家最后结算时间
        String time = financeMapper.lastSettlementTime(bid,1);
       /*   if (time != null && !time.equals("")){
            int i = startTime.compareTo(time);
            if (i <= 0){
                throw new NullPointerException("开始时间不能小于结算时间哟");
            }
        }
*/
        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //总采购订单总数
        Integer tatolCount = financeMapper.tatolPurchaseOrderCount(new Paramt(bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);

        //总采购订单
        List<HashMap> hashMaps = financeMapper.tatolPurchaseOrder(new Paramt(bid, name, number, startTime, endTime, page.getStartIndex(), page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.purchaseMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }

        HashMap hashMap = new HashMap();
        //总采购
        HashMap tatolHashMap = financeMapper.notTatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,0));
        hashMap.put("tatol",tatolHashMap.get("price").toString());

        //采购欠款订单收的款
        Double money = financeMapper.aefundsPurchaseCollectMoney(new Paramt(bid,0,name,number,startTime,endTime));
        hashMap.put("money2",money);

        //采购应收、实收
        HashMap payHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap.put("price",(Double.valueOf(payHashMap.get("price").toString()) + money));//完成的订单：应收款+ 欠款收的款
        hashMap.put("pay",(Double.valueOf(payHashMap.get("pay").toString()) + money));//完成的订单：实收款款+ 欠款收的款
        //采购欠款
        HashMap arrearsHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"1",1));
        hashMap.put("arrears",(Double.valueOf(arrearsHashMap.get("price").toString()) - money));//欠款的订单：欠款 - 欠款收的款
        //采购未付款
        HashMap unpaidHashMap = financeMapper.tatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap.put("unpaid",unpaidHashMap.get("price").toString());



        HashMap hashMap1 = new HashMap();
        //总采购退货
        HashMap tatolHashMap1 = financeMapper.notTatolPurchaseMoney(new Paramt(bid,name,number,startTime,endTime,1));
        hashMap1.put("tatol",tatolHashMap1.get("price").toString());

        //采购欠款订单收的款
        Double money2 = financeMapper.aefundsPurchaseCollectMoney(new Paramt(bid,1,name,number,startTime,endTime));
        hashMap1.put("money2",money2);
        //采购退货应收、实收
        HashMap payHashMap1 = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"2",1));
        hashMap1.put("price",(Double.valueOf(payHashMap1.get("price").toString()) + money2));//完成的订单：应收款+ 欠款收的款
        hashMap1.put("pay",(Double.valueOf(payHashMap1.get("pay").toString()) + money2));//完成的订单：实收款款+ 欠款收的款
        //采购退货欠款
        HashMap arrearsHashMap1 = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"1",1));
        hashMap1.put("arrears",(Double.valueOf(arrearsHashMap1.get("price").toString()) - money2));//欠款的订单：欠款 - 欠款收的款
        //采购退货未付款
        HashMap unpaidHashMap1 = financeMapper.tatolPurchaseReturnMoney(new Paramt(bid,name,number,startTime,endTime,"0",null));
        hashMap1.put("unpaid",unpaidHashMap1.get("price").toString());


        //当天采购订单总金额
        HashMap day = financeMapper.dayPurchaseOrderTatolMoney(bid, time);
        hashMap.put("day",day);

        //获取采购结算记录未结清的账单
        Double bills = financeMapper.lastNotSettlementBills(bid,1);
        hashMap.put("bills",bills); //未结清的账单
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        page.setHashMap1(hashMap1);
        return page;
    }

    //采购财务结算
    @Override
    @Transactional
    public Integer aadPurchaseFinanceSettlement(String token ,Integer role , Settlement settlement)throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //判断商家当天是否结算过
        Settlement set = orderMapper.financialSettlement(bid,1);
        if (set != null && DateUtil.compareDate1(DateUtil.getDay(), set.getTime()) == true){
            throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
        }

        Integer staffId = 0;
        if (role == 2){
            staffId = shopMapper.findStaffIdByToken(token);
        }

        //通过商家id查询采购未结算的完成订单
        List<Integer> pid = financeMapper.purchaseNotSettlement(bid);
        if (pid == null || pid.size() <= 0){
            throw new NullPointerException("没有未结算的订单");
        }
        for (Integer id:pid ) {
            financeMapper.updatePurchaseNotSettlement(id,1);
        }
        settlement.setBusiness_id(bid);
        settlement.setStaff_id(staffId);
        settlement.setOldTime(  set == null ? null : set.getCreate_time() );

        //通过上次结算时间查询5种收款分类的金额
        Settlement settl = financeMapper.fiveMoneyType(bid, 2, set == null ? null : set.getTime());
        settlement.setAlipay(settl.getAlipay());
        settlement.setCreate_time(new Date());
        settlement.setWx(settl.getWx());
        settlement.setOther(settl.getOther());
        settlement.setCash(settl.getCash());
        settlement.setBankcard(settl.getBankcard());

        //财务结算时查询五种（退货的）收款分类
        Settlement sett2 = financeMapper.fiveRetreatMoneyType(bid, 2, set == null ? null : set.getTime() );
        settlement.setrAlipay(sett2.getrAlipay());
        settlement.setrWx(sett2.getrWx());
        settlement.setrCash(sett2.getrCash());
        settlement.setrBankcard(sett2.getrBankcard());
        settlement.setrOther(sett2.getrOther());

        //添加采购财务结算记录
        return financeMapper.addSettlement(settlement);

    }


    //商家财务结算记录
    @Override
    public Page settlementInfo(String token ,Integer role , Integer pageNo, String startTime, String endTime , Integer type) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //财务结算记录总数
        Integer tatolCount = financeMapper.settlementInfoCount(bid,type,startTime,endTime);
        Page page = new Page(pageNum,tatolCount);
        //财务结算记录
        List<HashMap> hashMaps = financeMapper.settlementInfo(bid,type,startTime,endTime,page.getStartIndex(),page.getPageSize());
        HashMap hashMap = new HashMap();
        Double sale_money= null;
        Double purchase_money = null;
        if (type != null && type.equals(0)){
            //销售总金额
            sale_money = financeMapper.settlemenTotalMoney(bid,0,startTime,endTime);
        }else if (type != null && type.equals(1)){
            //采购总金额
            purchase_money = financeMapper.settlemenTotalMoney(bid,1,startTime,endTime);
        }else {
            //销售总金额
            sale_money = financeMapper.settlemenTotalMoney(bid,0,startTime,endTime);
            //采购总金额
            purchase_money = financeMapper.settlemenTotalMoney(bid,1,startTime,endTime);
        }

        hashMap.put("sale_money",sale_money);
        hashMap.put("purchase_money",purchase_money);
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //通过结算id查询结算信息
    @Override
    public HashMap settlementByid(Integer id) {
        return financeMapper.settlementByid(id);
    }

    //用户销售汇总
    @Override
    public Page userSaleProfitSummary(String token ,Integer role , Integer pageNo, String name ,String goodsName, String startTime, String endTime,Integer bindindId)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //用户销售汇总总数
        Integer tatolCount = financeMapper.userSaleProfitSummaryCount(new Paramt(bid,name,startTime,endTime ,bindindId ,goodsName));
        Page page = new Page(pageNum,tatolCount);

        //用户销售汇总
        List<HashMap> hashMaps = financeMapper.userSaleProfitSummary(new Paramt(bid, name, startTime, endTime, page.getStartIndex(), page.getPageSize() ,bindindId ,goodsName ));

        //用户销售汇总总金额
        HashMap hashMap = financeMapper.userSaleProfitSummaryTatolMoney(new Paramt(bid,name,startTime,endTime ,bindindId ,goodsName));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //用户销售明细
    @Override
    public Page userSaleProfitDetails(String token ,Integer role , Integer pageNo, String name ,String goodsName, String startTime, String endTime,Integer bindindId)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = financeMapper.userSaleProfitDetailsCount(new Paramt(bid,name,startTime,endTime ,bindindId ,goodsName));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = financeMapper.userSaleProfitDetails(new Paramt(bid, name, startTime, endTime, page.getStartIndex(), page.getPageSize() ,bindindId ,goodsName));
        HashMap hashMap = financeMapper.userSaleProfitDetailsTatolMoney(new Paramt(bid,name,startTime,endTime ,bindindId ,goodsName));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }


    //产品销售汇总
    @Override
    public Page goodsSaleProfitSummary(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime, Integer is) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = financeMapper.goodsSaleProfitSummaryCount(new Paramt(bid,name,startTime,endTime,is));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = financeMapper.goodsSaleProfitSummary(new Paramt(bid, name, startTime, endTime, page.getStartIndex(), page.getPageSize(),is));
        HashMap hashMap = financeMapper.goodsSaleProfitSummaryTatolMoney(new Paramt(bid, name, startTime, endTime,is));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //产品销售明细
    @Override
    public Page goodsSaleProfitDetails(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime , Integer is) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = financeMapper.goodsSaleProfitDetailsCount(new Paramt(bid,name,startTime,endTime,is));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = financeMapper.goodsSaleProfitDetails(new Paramt(bid, name, startTime, endTime, page.getStartIndex(), page.getPageSize(),is));
        HashMap hashMap = financeMapper.goodsSaleProfitDetailsTatolMoney(new Paramt(bid, name, startTime, endTime,is));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //添加欠条
    @Override
    @Transactional
    public Integer addIous(String token ,Integer role , Lous lous) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        lous.setBusiness_id(bid);
        //添加欠条
        return financeMapper.addIous(lous);
    }

    //欠条id查询欠条内容
    @Override
    public HashMap findLous(Integer id) throws NullPointerException{

        //欠条id查询欠条内容
        HashMap hashMap = financeMapper.findIous(id);
        if (hashMap== null){
            throw  new  NullPointerException("欠条id不存在");
        }
        Double money = Double.valueOf(hashMap.get("money").toString());

        hashMap.put("numberOfMoney",NumberToCN.number2CNMontrayUnit(new BigDecimal(money)));
        return hashMap;
    }

    //销售风险订单记录总数
    @Override
    public Integer riskOrderCount(String token, Integer role, String startTime, String endTime) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        return financeMapper.riskOrderCount(new Paramt(bid,"",startTime,endTime));
    }

    //销售风险订单记录
    @Override
    public Page riskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount =  financeMapper.riskOrderCount(new Paramt(bid,name,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = financeMapper.riskOrder(new Paramt(bid,name,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        //销售风险总金额
        HashMap hashMap = financeMapper.riskOrderTatolMoney(new Paramt(bid, name, startTime, endTime));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //采购风险订单记录总数
    @Override
    public Integer purchaseRiskOrderCount(String token, Integer role, String startTime, String endTime) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return  financeMapper.purchaseRiskOrderCount(new Paramt(bid,"",startTime,endTime));
    }

    //采购风险订单记录
    @Override
    public Page purchaseRiskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount =  financeMapper.purchaseRiskOrderCount(new Paramt(bid,name,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = financeMapper.purchaseRiskOrder(new Paramt(bid,name,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        //采购风险总金额
        HashMap hashMap = financeMapper.purchaseRiskOrderTatolMoney(new Paramt(bid, name, startTime, endTime));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //修改销售风险订单的实收款金额
    @Override
    @Transactional
    public Integer updateRiskOrderMoney(Integer id, Double money) {

        //收款分类id
        Integer typeId = null;
        double v = 0;

        //通过订单id查询订单第一次收款分类记录id
        HashMap   oneTypeMoney = financeMapper.selectOrderOneMoneyType(id);


        //通过订单id查询订单第二次收款分类记录
        HashMap twoTypeMoney = financeMapper.selectOrderTwoMoneyType(id);

        if (  twoTypeMoney == null ){

            typeId = Integer.valueOf(oneTypeMoney.get("id").toString());
            v = money;

        }else  if (twoTypeMoney != null){ //有第二次收款记录

            typeId = Integer.valueOf(twoTypeMoney.get("id").toString());
            v = money - Double.valueOf(oneTypeMoney.get("money").toString());

        }

        //修改收款分类
        financeMapper.updateOrderMoneyType( typeId , v , new Date());

        return financeMapper.updateRiskOrderMoney(id,money);
    }

    //修改采购风险订单的实收款金额
    @Override
    @Transactional
    public Integer updatePurchaseRiskOrderMoney(Integer id, Double money ) {

        //收款分类id
        Integer typeId = null;
        double v = 0;

        //通过订单id查询订单第一次收款分类记录id
        HashMap   oneTypeMoney = financeMapper.selectPurchaseOneMoneyType(id);


        //通过订单id查询订单第二次收款分类记录
        HashMap twoTypeMoney = financeMapper.selectPurchaseTwoMoneyType(id);

        if (  twoTypeMoney == null ){

            typeId = Integer.valueOf(oneTypeMoney.get("id").toString());
            v = money;

        }else  if (twoTypeMoney != null){ //有第二次收款记录

            typeId = Integer.valueOf(twoTypeMoney.get("id").toString());
            v = money - Double.valueOf(oneTypeMoney.get("money").toString());

        }

        //修改采购收款分类
        financeMapper.updateOPurchaseMoneyType( typeId , v , new Date());

        return financeMapper.updatePurchaseRiskOrderMoney(id,money);
    }

    //销售收款分类
    @Override
    public HashMap saleReceivablesType(String token ,Integer role , Integer mold, String startTime, String endTime) throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        //销售收款分类
        return financeMapper.saleReceivablesType(bid,mold,startTime,endTime);
    }

    //采购收款分类
    @Override
    public HashMap purchaseReceivablesType(String token ,Integer role , Integer mold, String startTime, String endTime) throws NullPointerException ,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        //采购收款分类
        return financeMapper.purchaseReceivablesType(bid,mold,startTime,endTime);
    }

    //最后结算时间
    @Override
    public HashMap finalSettlementTime(String token ,Integer role , Integer type)throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        //最后结算时间
        return financeMapper.finalSettlementTime(bid,type);
    }

    //每天产品库存情况总数
    @Override
    public Integer dayGoodsInventorySituationCount(String token, Integer role, String startTime, String endTime) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        return financeMapper.dayGoodsInventorySituationCount(new Paramt(bid , "" , startTime ,endTime) );
    }

    //每天产品库存情况
    @Override
    public Page dayGoodsInventorySituation(String token, Integer role, Integer pageNo, String name, String startTime, String endTime)throws Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //每天产品库存情况总数
        Integer totalCount = financeMapper.dayGoodsInventorySituationCount(new Paramt(bid , name , startTime ,endTime) );
        Page page = new Page(pageNum , totalCount);
        //每天产品库存情况
        List<HashMap> hashMaps = financeMapper.dayGoodsInventorySituation(new Paramt(bid , name , startTime ,endTime , page.getStartIndex() , page.getPageSize()));

        //每天产品库存情况总数量()
        HashMap hashMap = financeMapper.dayGoodsInventorySituationTotalNum(new Paramt(bid, name, startTime, endTime));

        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }

    //产品风控总数
    @Override
    public Integer goodsWindControlCount(String token, Integer role, String startTime, String endTime) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        return financeMapper.goodsWindControlCount(new Paramt(bid , "" , startTime ,endTime));
    }

    //产品风控
    @Override
    public Page goodsWindControl(String token ,Integer role ,Integer pageNo,String name ,String startTime ,String endTime) throws Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        Integer totalCount = financeMapper.goodsWindControlCount(new Paramt(bid , name , startTime ,endTime));
        Page page = new Page(pageNum , totalCount);
        List<HashMap> hashMaps = financeMapper.goodsWindControl(new Paramt(bid , name , startTime ,endTime , page.getStartIndex() , page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //用户销售明细导出
    @Override
    public HSSFWorkbook userSaleDetailDownload(String token, Integer role, String name, String startTime, String endTime, Integer bindindId) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        String seetName = "用户销售明细";//sheet名
        String[] title =new String[]{"客户", "品名","数量", "单价(元)","合计(元)","创建时间"};//标题
        List<HashMap> hashMaps = financeMapper.userSaleDetailDownload(new Paramt(bid ,name ,startTime,endTime ,bindindId));

        Double tatol = financeMapper.userSaleDetailDownloadTatolMoney(new Paramt(bid, name, startTime, endTime, bindindId));

        String[][] values = new String[hashMaps.size()+1][];
        for (int i =0;i<= hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];

            if (hashMaps.size() == i){
                values[i][0] = "合计：";
                values[i][1] = String.valueOf(tatol);
                values[i][3] = "导出时间段";
                values[i][4] = startTime+" — "+endTime;
            }else {
                HashMap hashMap = hashMaps.get(i);
                values[i][0] = hashMap.get("user_name").toString();
                values[i][1] = hashMap.get("name").toString();
                values[i][2] = hashMap.get("order_number").toString();
                values[i][3] = hashMap.get("order_price").toString();
                values[i][4] = hashMap.get("price").toString();
                values[i][5] = hashMap.get("create_time").toString();
            }

        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }
}
