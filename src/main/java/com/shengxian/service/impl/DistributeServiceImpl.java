package com.shengxian.service.impl;

import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.mapper.*;
import com.shengxian.entity.Dispatc;
import com.shengxian.entity.Paramt;
import com.shengxian.service.DistributeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-12-07
 * @Version: 1.0
 */
@Service
public class DistributeServiceImpl implements DistributeService {

    @Resource
    private ShopMapper shopMapper;
    @Resource
    private DistributeMapper distributeMapper;

    @Resource
    private PurchaseMapper purchaseMapper;

    @Resource
    private OrderMapper orderMapper;

    private Logger logger =  Logger.getLogger(DistributeServiceImpl.class);


    //所有未到货的产品汇总
    @Override
    public Page allNotArrivalGoodsSummary(String token ,Integer role , Integer pageNo, Integer wid)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        //所有未到货的产品汇总总数
        Integer tatolCount = distributeMapper.allNotArrivalGoodsSummaryCount(bid,wid);
        Page page = new Page(pageNum,tatolCount,14);
        //所有未到货的产品汇总
        List<HashMap> hashMaps = distributeMapper.allNotArrivalGoodsSummary(bid,wid,page.getStartIndex(),page.getPageSize());
        for (HashMap hashMap: hashMaps ) {

            if (shield == null || Integer.valueOf(shield.get("inv").toString()) == 0){
                hashMap.put("inv","库存可见");
            }else {
                hashMap.put("inv","库存不可见");
            }
        }
        //所有未到货的产品汇总总金额
        Double money = distributeMapper.allNotArrivalGoodsSummaryTatolMoney(bid,wid);
        page.setPrice(new BigDecimal(money));
        page.setRecords(hashMaps);
        return page;
    }

    //所有未到货的产品明细
    @Override
    public Page allNotArrivalGoodsDetail(String token ,Integer role , Integer pageNo, Integer wid)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        //所有未到货的产品明细总数
        Integer tatolCount = distributeMapper.allNotArrivalGoodsDetailCount(bid,wid);
        Page page = new Page(pageNum,tatolCount,14);
        //所有未到货的产品明细
        List<HashMap> hashMaps = distributeMapper.allNotArrivalGoodsDetail(bid,wid,page.getStartIndex(),page.getPageSize());
        for (HashMap hashMap: hashMaps ) {

            if (shield == null || Integer.valueOf(shield.get("inv").toString()) == 0){
                hashMap.put("inv","库存可见");
            }else {
                hashMap.put("inv","库存不可见");
            }
        }
        //所有未到货的产品明细总金额
        Double money = distributeMapper.allNotArrivalGoodsDetailTatolMoney(bid,wid);
        page.setPrice(new BigDecimal(money));
        page.setRecords(hashMaps);
        return page;
    }

    //查询未分配给员工派送的订单
    @Override
    public List<HashMap>  notDistributeOrder(String token ,Integer role , String name, String number) throws NullPointerException,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return distributeMapper.notDistributeOrder(bid,name,number);
    }

    //分配订单给员工派送
    @Override
    @Transactional
    public Integer distributeOrderToStaff(String token ,Integer role ,String order_id, Integer staff_id)throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //添加员工配送表
        Dispatc dispatc = new Dispatc(bid,staff_id, new Date());
        Integer count = distributeMapper.addDispatc(dispatc);

        //分割订单id
        String[] split = order_id.split(",");
        for (String id: split) { //订单id

            //通过订单id查询员工配送详情是否有分配过了，如果有则删除 ，重新分配
            HashMap hashMap = distributeMapper.selectDispatcDetailByOrder_id(Integer.valueOf(id));//id订单id

            if (hashMap != null){
                if (Integer.valueOf(hashMap.get("status").toString()) == 0){
                    throw new NullPointerException("该订单还在配送结账表中，不能再分配");
                }
                //删除配送详情记录
                distributeMapper.deleteDispatcDetail(Integer.valueOf(hashMap.get("id").toString()));

                //通过订单id修改订单属于谁配送的
                distributeMapper.updateDistribyteOrderStaff(Integer.valueOf(id),staff_id);
            }else {
                //判断订单是否从未分配的订单但是状态已是到货的了
                Integer status = distributeMapper.findOrderStatus(Integer.valueOf(id));
                if (status == 4){
                    //通过订单id修改订单属于谁配送的
                    distributeMapper.updateDistribyteOrderStaff(Integer.valueOf(id),staff_id);
                }else {
                    //通过订单id修改该订单属于配送员工的
                    distributeMapper.updateDistributeOrderToStaff(Integer.valueOf(id),staff_id);
                }
            }
            //添加员工配送详情表（一次配送多少个订单）
            distributeMapper.addDispatcDetail(dispatc.getId(),Integer.valueOf(id));

        }//分割订单id

        //查询员工是否有车次提成比例
        HashMap oncePercentage = distributeMapper.findStaffPercentage(staff_id, 5);
        if (oncePercentage.get("a")!= null && !oncePercentage.get("a").equals("") && oncePercentage.get("b") != null && !oncePercentage.get("b").equals("")){
            //添加员工车次次数记录
            distributeMapper.addOncePercentage(staff_id,new Date(),order_id);
        }
        return count;
    }

    //已经分配给员工配送的订单
    @Override
    public Page alreadyDistributeOrder(String token ,Integer role , Integer pageNo, Integer status , Integer staff_id)throws NullPointerException ,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //已经分配给员工配送订单总数
        Integer tatolCount = distributeMapper.alreadyDistributeOrderCount(new Paramt(bid,staff_id));
        Page page = new Page(pageNum,tatolCount);
        //已经分配给员工配送订单
        List<HashMap> hashMaps = distributeMapper.alreadyDistributeOrder(new Paramt(bid,staff_id,page.getStartIndex(),page.getPageSize()));
        //已经分配给员工配送订单总金额
        Double tatolMoney = distributeMapper.alreadyDistributeOrderTatolMoney(new Paramt(bid, staff_id));
        page.setPrice(new BigDecimal(tatolMoney));
        page.setRecords(hashMaps);
        return page;
    }

    //通过配送id查询配送订单详情
    @Override
    public List<HashMap> findDistributeOrderDetail(Integer id ,Integer is_del ,Integer status,String number) {
        return distributeMapper.findDistributeOrderDetail(id,is_del,status,number);//配送订单详情
    }

    //通过配送id查询配送订单详情不同状态的总金额
    @Override
    public HashMap findDistributeOrderDetailTotalMoney(Integer id ,Integer is_del,Integer status) {
        HashMap hashMap = new HashMap();
        HashMap unpaid = distributeMapper.findDistributeOrderDetailTotalMoney(id,0,is_del,status);//未收款
        HashMap arrears = distributeMapper.findDistributeOrderDetailTotalMoney(id, 2,is_del,status); //欠款
        HashMap receivables = distributeMapper.findDistributeOrderDetailTotalMoney(id, 3,is_del,status);//收款
        hashMap.put("unpaid",unpaid);
        hashMap.put("arrears",arrears);
        hashMap.put("receivables",receivables);
        return hashMap;
    }

    //员工配送完成的订单汇总
    @Override
    public Page saleDistributeCompletOrderSummary(String token  ,Integer role, Integer pageNo, Integer staff_id, Integer type, Integer status , String startTime, String endTime)throws NullPointerException ,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //员工配送完成的订单汇总总数
        Integer tatolCount = distributeMapper.saleDistributeCompletOrderSummaryCount(new Paramt(bid,staff_id,status,startTime,endTime,type));
        Page page = new Page(pageNum,tatolCount);
        //员工配送完成的订单汇总
        List<HashMap> hashMaps = distributeMapper.saleDistributeCompletOrderSummary(new Paramt(bid,staff_id,status ,startTime,endTime,page.getStartIndex(),page.getPageSize(),type));
        //员工配送完成的订单汇总总金额合计
        HashMap hashMap = distributeMapper.saleDistributeCompletOrderSummaryTatolMoney(new Paramt(bid, staff_id,status,startTime,endTime,type));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //员工配送完成的订单明细
    @Override
    public Page saleDistributeCompletOrderDetail(String token ,Integer role , Integer pageNo, Integer staff_id, String name , String startTime, String endTime) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //员工配送完成的订单明细总数
        Integer tatolCount = distributeMapper.saleDistributeCompletOrderDetailCount(new Paramt(bid,staff_id,name,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);
        //员工配送完成的订单明细
        List<HashMap> hashMaps = distributeMapper.saleDistributeCompletOrderDetail(new Paramt(bid,staff_id,name,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        //员工配送完成的订单明细总金额合计
        HashMap hashMap = distributeMapper.saleDistributeCompletOrderDetailTatolMoney(new Paramt(bid, staff_id, name,startTime,endTime));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }



    //员工总明细
    @Override
    public Page staffTatolDetail(String token ,Integer role , Integer pageNo, Integer staff_id)throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = distributeMapper.staffTatolDetailCount(bid,staff_id);
        Page page = new Page(pageNum,tatolCount);
        //查询员工明细(接单总数，完成订单总金额
        List<HashMap> hashMaps = distributeMapper.staffTatolDetail(bid,staff_id,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //员工月明细
    @Override
    public Page staffMonthOrder(String token ,Integer role , Integer pageNo, Integer staff_id) throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //统计员工每月订单总数
        Integer tatolCount = distributeMapper.staffMonthOrderCount(staff_id);
        Page page = new Page(pageNum,tatolCount);

        //统计员工每月订单
        List<HashMap> hashMaps = distributeMapper.staffMonthOrder(staff_id,page.getStartIndex(),page.getPageSize());
        if (hashMaps != null){
            for (HashMap hashMap: hashMaps  ) {

                //统计员工每月提成
                Double statis = distributeMapper.staffMonthStatis(staff_id, hashMap.get("month").toString());
                hashMap.put("statis",statis);

                //根据月查询员工结算金额
                Double state = distributeMapper.totalTaffWage(staff_id, hashMap.get("month").toString());
                if (state == null){
                    hashMap.put("state",0);
                }else {
                    hashMap.put("state",state);
                }
            }
        }
        page.setRecords(hashMaps);
        return page;
    }

    //员工工资结算
    @Override
    @Transactional
    public Integer addStaffWageSettlement(Integer steff_id,Double money, String time) throws Exception {

        //判断当前月是否结算了
        Integer count = distributeMapper.staffWageSettlement(steff_id,time);
        if (count != null){
            throw new NullPointerException("这个月已经结算了");
        }
        return distributeMapper.addStaffWageSettlement(steff_id,money,time);
    }


    //员工提成订单明细
    @Override
    public Page staffDayDetail(Integer pageNo, Integer staff_id, String time,String name ,Integer type ,String startTime ,String endTime) throws Exception {
        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //通过员工id查询店铺id
        Integer businessId = shopMapper.selectBusinessIdByStaffId(staff_id);
        if (startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("")){
            startTime = startTime.trim();
            endTime = endTime.trim();
        }
        //员工销售订单明细和采购订单明细总数
        Integer saleSatolCount = distributeMapper.staffDayDetailCount(businessId , staff_id,time ,name ,type ,startTime ,endTime);

        Page page = new Page(pageNum,saleSatolCount,10);

        //员工销售订单明细和采购订单明细
        List<HashMap> sale = distributeMapper.staffDayDetail(businessId , staff_id,time ,name ,type ,startTime , endTime ,page.getStartIndex(),page.getPageSize());

        HashMap hashMap = new HashMap();
        hashMap.put("totalStatis",0);
        if (type != null && type != 0 ){
            Double hash = distributeMapper.staffDayDetailTotalMoney(businessId ,staff_id, time, name, type, startTime, endTime);
            hashMap.put("totalStatis",hash);
        }


        page.setRecords(sale);
        page.setHashMap(hashMap);
        return page;
    }


    //员工订单
    @Override
    public Page salePurchaseOrder(Integer pageNo, Integer staff_id, String time ,String name ,Integer type) {
        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        Page page = null;
        if (type != null && type == 0 ){
            Integer tatolCount = tatolCount = distributeMapper.saleOrderCount(staff_id,time,name);
            page = new Page(pageNum,tatolCount);
            List<HashMap> hashMaps = distributeMapper.saleOrder(staff_id, time ,name, page.getStartIndex(), page.getPageSize());
            page.setRecords(hashMaps);
            return page;

        }else if (type != null && type == 1){
            Integer tatolCount = tatolCount = distributeMapper.purchaseOrderCount(staff_id,time,name);
            page = new Page(pageNum,tatolCount);
            List<HashMap> hashMaps = distributeMapper.purchaseOrder(staff_id, time ,name, page.getStartIndex(), page.getPageSize());
            page.setRecords(hashMaps);
            return page;

        }else {
            Integer tatolCount1 = distributeMapper.saleOrderCount(staff_id,time,name);
            Integer tatolCount2 = distributeMapper.purchaseOrderCount(staff_id,time,name);
            page = new Page(pageNum,(tatolCount1 + tatolCount2));

            List<HashMap> saleOrder = distributeMapper.saleOrder(staff_id, time ,name, page.getStartIndex(), page.getPageSize());

            List<HashMap> purchaseOrder = distributeMapper.purchaseOrder(staff_id, time ,name, page.getStartIndex(), page.getPageSize());
            List<HashMap> hashMaps = new ArrayList<HashMap>();
            hashMaps.addAll(saleOrder);
            hashMaps.addAll(purchaseOrder);
            class ReverseSort implements Comparator{
                @Override
                public int compare(Object o1, Object o2) {
                    HashMap h1 = (HashMap) o1;
                    HashMap h2 = (HashMap) o2;
                    return -h1.get("audit_time").toString().compareTo(h2.get("audit_time").toString());
                }
            }
            Collections.sort(hashMaps,new ReverseSort());
            page.setRecords(hashMaps);
            return page;
        }

    }


}

