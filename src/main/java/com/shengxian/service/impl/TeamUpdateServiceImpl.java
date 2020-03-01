package com.shengxian.service.impl;

import com.shengxian.common.util.GroupNumber;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.DistributeMapper;
import com.shengxian.mapper.FinanceMapper;
import com.shengxian.mapper.StaffMapper;
import com.shengxian.service.TeamUpdateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/6/2
 * @Version: 1.0
 */
@Service
public class TeamUpdateServiceImpl extends HttpServlet implements TeamUpdateService {

    @Resource
    private StaffMapper staffMapper;
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private DistributeMapper distributeMapper;

    @Resource
    private FinanceMapper financeMapper;

    private Logger logger = Logger.getLogger(TeamUpdateServiceImpl.class);

    /**
     * 统计每月商家的员工定时提成统计
     */
    @Override
    @Transactional
    public void teamUpdate() {

        try {
            //每个月的1号定时统计上个月之内的员工提成（3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成）
            List<Integer> business_id = shopMapper.businessId();
            if (business_id.size() > 0) {
                for (Integer bid : business_id) { //商家id

                    //通过商家id获取商家所有的员工id
                    List<Integer> staff_id = staffMapper.staffList(bid);
                    if (staff_id.size() > 0){
                        logger.warn("---店铺id"+bid+"---");
                        for (Integer sid : staff_id) {    //员工id
                            logger.info("---员工"+sid+",统计开始---");
                            
                            //统计员工吨位提成
                            Double tonnage = staffMapper.staffFrequency(sid, 3);
                            if (tonnage != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap TP = distributeMapper.findStaffPercentage(sid, 4);
                                if (TP != null) {
                                    if (TP.get("a") != null && !TP.get("a").equals("") && TP.get("b") != null && !TP.get("b").equals("")) {
                                        double value = GroupNumber.ton(tonnage, Double.valueOf(TP.get("a").toString()), Double.valueOf(TP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,3);
                                            logger.info("员工是"+sid+"添加员工重量提成");
                                        }
                                    }
                                }
                            }


                            //统计员工车次提成
                            Integer tatol = staffMapper.staffOnceFrequency(sid);
                            if (tatol != null && !tatol.equals(0)){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap AP = distributeMapper.findStaffPercentage(sid, 5);
                                if (AP != null) {
                                    if (AP.get("a") != null && !AP.get("a").equals("") && AP.get("b") != null && !AP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(tatol, Integer.valueOf(AP.get("a").toString()), Double.valueOf(AP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,4车次，5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,4);
                                            logger.info("员工是"+sid+"添加员工车次提成");
                                        }
                                    }
                                }
                            }


                            //统计员工到达提成
                            Double arrival = staffMapper.staffFrequency(sid, 5);
                            if (arrival != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap AP = distributeMapper.findStaffPercentage(sid, 6);
                                if (AP != null) {
                                    if (AP.get("a") != null && !AP.get("a").equals("") && AP.get("b") != null && !AP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(arrival, Double.valueOf(AP.get("a").toString()), Double.valueOf(AP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,5);
                                            logger.info("员工是"+sid+"添加员工到达提成");
                                        }
                                    }
                                }
                            }

                            //统计员工店总销售提成
                            Double tatolSale = staffMapper.staffFrequency(sid, 6);
                            if (tatolSale != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap SP = distributeMapper.findStaffPercentage(sid, 7);
                                if (SP != null) {
                                    if (SP.get("a") != null && !SP.get("a").equals("") && SP.get("b") != null && !SP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(tatolSale, Double.valueOf(SP.get("a").toString()), Double.valueOf(SP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,6);
                                            logger.info("员工是"+sid+"添加店总销售提成");
                                        }
                                    }
                                }
                            }


                            //统计员工开单提成
                            Double openOrder = staffMapper.staffFrequency(sid, 7);
                            if (openOrder != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap SP = distributeMapper.findStaffPercentage(sid, 8);
                                if (SP != null) {
                                    if (SP.get("a") != null && !SP.get("a").equals("") && SP.get("b") != null && !SP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(openOrder, Double.valueOf(SP.get("a").toString()), Double.valueOf(SP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,7);
                                            logger.info("员工是"+sid+"添加员工开单提成");
                                        }
                                    }
                                }
                            }

                            //统计员工采购数量提成
                            Double purchaseNum = staffMapper.staffFrequency(sid, 8);
                            if (purchaseNum != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap SP = distributeMapper.findStaffPercentage(sid, 9);
                                if (SP != null) {
                                    if (SP.get("a") != null && !SP.get("a").equals("") && SP.get("b") != null && !SP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(purchaseNum, Double.valueOf(SP.get("a").toString()), Double.valueOf(SP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,8);
                                            logger.info("员工是"+sid+"添加员工采购数量提成");
                                        }
                                    }
                                }
                            }

                            //统计员工采购重量提成
                            Double purchaseTonnage = staffMapper.staffFrequency(sid, 9);
                            if (purchaseTonnage != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap SP = distributeMapper.findStaffPercentage(sid, 10);
                                if (SP != null) {
                                    if (SP.get("a") != null && !SP.get("a").equals("") && SP.get("b") != null && !SP.get("b").equals("")) {
                                        double value = GroupNumber.ton(purchaseTonnage, Double.valueOf(SP.get("a").toString()), Double.valueOf(SP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,9);
                                            logger.info("员工是"+sid+"添加员工采购重量提成");
                                        }
                                    }
                                }
                            }

                            //统计总采购金额
                            Double tatolPurchase = staffMapper.staffFrequency(sid, 10);
                            if (tatolPurchase != null ){
                                //获取员工的吨位提成比例 4销售吨位，5销售车次，6到货，7总销售，8销售开单，9采购产品数量，10采购吨位，12采购金额
                                HashMap SP = distributeMapper.findStaffPercentage(sid, 12);
                                if (SP != null) {
                                    if (SP.get("a") != null && !SP.get("a").equals("") && SP.get("b") != null && !SP.get("b").equals("")) {
                                        double value = GroupNumber.getValue(tatolPurchase, Double.valueOf(SP.get("a").toString()), Double.valueOf(SP.get("b").toString()));
                                        if (value != 0.0){
                                            //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
                                            staffMapper.addStaffTimingStatis(sid, value,10);
                                            logger.info("员工是"+sid+"添加员工总采购金额");
                                        }
                                    }
                                }
                            }


                        }
                    }else {
                        logger.warn("没有员工");
                    }
                }
            } else {
                logger.warn("没有商家");
            }
            logger.warn("---------------------------结束（每月定时员工提成统计）-------------------------------------------");

        }catch (Exception e){
            logger.error("异常");
            logger.error(e);
        }
    }


    //清除结算状态
    @Override
    @Transactional
    public void settlement() {

        //查询销售已结算的未付款和欠款的订单
        List<Integer> order_id = distributeMapper.saleSettlementUnpaidArrearsOrder();
        if (order_id != null){
            logger.warn("---------------------------销售订单"+order_id.size()+"-------------------------------------------");
            for (Integer id: order_id ) {
                try {
                    //修改结算状态
                    financeMapper.updateNotSettlementOrder(id, 0);
                }catch (Exception e){
                    logger.error("修改销售订单结算状态异常");
                }

            }
        }

        //查询采购已结算的未付款和欠款的订单
        List<Integer> purchase_id = distributeMapper.purchaseSettlementUnpaidArrearsOrder();
        if (purchase_id != null){
            logger.warn("---------------------------采购订单"+purchase_id.size()+"-------------------------------------------");
            for (Integer id: purchase_id ) {
                try {
                    //修改结算状态
                    financeMapper.updatePurchaseNotSettlement(id, 0);
                }catch (Exception e){
                    logger.error("修改采购订单结算状态异常");
                }

            }
        }
        logger.warn("---------------------------结束（每晚清除订单结算状态）-------------------------------------------");

    }

    @Override
    @CacheEvict(cacheNames = "demoCache" , allEntries = true)
    public void cacheEvict() {
        System.out.println("清除拦截器查询店铺信息" );
    }
}
