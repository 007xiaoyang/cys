package com.shengxian.service.impl;

import com.shengxian.common.util.*;
import com.shengxian.entity.*;
import com.shengxian.mapper.*;
import com.shengxian.service.PurchaseService;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/9/18
 * @Version: 1.0
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private DistributeMapper distributeMapper;

    //生产采购订单号
    @Override
    public String createPurchaseOrder(String token ,Integer role) throws NullPointerException{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //生产采购订单编码
        return OrderCodeFactory.getPurchaseOrder((long) bid, 5);
    }

    //采购和销售时搜索产品
    @Override
    public List<HashMap> selectBusinessGoods(String token ,Integer role, String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.selectBusinessGoods(bid,name);
    }

    //查询商家的产品收藏
    @Override
    public List<HashMap> findBusinessGoodsCollection(String token ,Integer role ,Integer suppliers_id,String name,String startTime,String endTime)throws  NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.findBusinessGoodsCollection(bid,suppliers_id,name ,startTime,endTime);
    }

    //添加商家收藏产品
    @Override
    @Transactional
    public Integer addBusinessGoodsCollection(String token  ,Integer role, Integer goods_id ,Integer suppliers_id) throws NullPointerException{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //通过产品id查询产品是否收藏过了
        Integer collection_id = purchaseMapper.goods_idIfCollection(goods_id,suppliers_id);
        if (collection_id != null){
            throw new NullPointerException("该产品已经收藏过了");
        }

        Collect collect = new Collect(goods_id, new Date(), suppliers_id, bid);

        //添加商家收藏产品
        purchaseMapper.addBusinessGoodsCollection(collect);
        return collect.getId();
    }

    //取消商家收藏的产品
    @Override
    @Transactional
    public Integer deleteBusinessGoodsCollection(String id) {
        Integer count =null;
        String[] split = id.split(",");
        for (String cid: split ) {
            count = purchaseMapper.deleteBusinessGoodsCollection(Integer.valueOf(cid));
        }
        return count;
    }

    // 通过产品id搜索产品（采购和销售）
    @Override
    public List<HashMap> selectBusinessGoodsById(String token,String goods_id ,Integer suppliers_id) {
        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        List<HashMap> hashMaps = new ArrayList<HashMap>();
        String[] splitGoodsId = goods_id.split(",");
        for (String id:splitGoodsId ) {

            HashMap hashMap = purchaseMapper.selectBusinessGoodsById(Integer.parseInt(id),suppliers_id);

            if(shield != null && Integer.valueOf(shield.get("inv").toString()) == 1){
                hashMap.put("inv","库存不可见");
            }else {
                hashMap.put("inv","库存可见");
            }

            if (shield == null || Integer.valueOf(shield.get("shield").toString()) == 0){
                hashMap.put("cost","进价可见");
            }else {
                hashMap.put("cost","进价不可见");
            }
            hashMaps.add(hashMap);
        }
        return hashMaps;
    }

    //查询商家采购的订单
    @Override
    public List<HashMap> selectPurchaseOrder(String token ,Integer role ,Integer suppliers_id, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.selectPurchaseOrder(bid,suppliers_id,startTime,endTime);
    }

    //通过订单id查询采购产品详情
    @Override
    public List<HashMap> selectPurchaseOrderDetailById(String order_id) throws NullPointerException {
        List<HashMap> hashMaps = new ArrayList<HashMap>();
        String[] split = order_id.split(",");
        for (String oid:split) {

            //通过订单id查询采购产品详情
            List<HashMap> hashMaps1 = purchaseMapper.selectPurchaseOrderDetailById(Integer.valueOf(oid));
            hashMaps.addAll(hashMaps1);
        }
        return hashMaps;
    }


    //添加采购产品单
    @Override
    @Transactional
    public Integer addPurchaseOrder(String token  ,Integer role, PurchaseOrder purchaseOrder ) throws NullPointerException {
        HashMap hashMap = null;

        int operate = 0;
        //查询店铺信息
        hashMap = shopMapper.findSellerBySellerToken(token);

        if (role == 2 ){
            //查询店铺员工信息
            hashMap = shopMapper.findClerkByClerkToken(token);

            operate = Integer.valueOf(hashMap.get("staff_id").toString());
        }

        if (hashMap == null){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        purchaseOrder.setBusiness_id(Integer.valueOf(hashMap.get("id").toString()));//商家id
        purchaseOrder.setMaking(hashMap.get("name").toString());//制单人姓名
        purchaseOrder.setNo(OrderCodeFactory.getStringRandom(3,3));//销售的是6位二维码标识
        purchaseOrder.setCreate_time(new Date());

        //添加采购产品订单
        Integer count = purchaseMapper.addPurchaseOrder(purchaseOrder);

        double price =0;
        Give give = new Give();
        PurchaseOrderDetail[] purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();
        for (PurchaseOrderDetail detail: purchaseOrderDetail ) {
            if (detail.getType() == 0){
                price += detail.getPurchase_number() * detail.getPurchase_price();
            }else if(detail.getType() == 1) {
                //采购时的赠送产品只添加数据，等到确认到货时在加库存
                give.setGoods_id(detail.getGoods_id());//产品id
                give.setSuppliers_id(purchaseOrder.getSuppliers_id());//供应商id
                give.setGive_time(new Date());
                give.setOperate_id(operate);//操作人
                give.setNum(detail.getPurchase_number());//赠送数量
                give.setStatus(1);//0默认在赠送单里赠送，1在采购单里赠送
                count = inventoryMapper.addGiveGoods(give);
            }
            //采购入库开单，先增加产品的虚拟库存
            shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id() , detail.getPurchase_number() );

            detail.setPurchase_id(purchaseOrder.getId());
            count = purchaseMapper.addPurchaseOrderDetail(detail);

            if (give.getId() != null){
                //通过赠送id，修改订单详情id
               count =  purchaseMapper.updateGive(give.getId(),detail.getId());
            }


        }
        //总金额 = 采购所以产品价格+运费+差价
        double money = price +purchaseOrder.getFreight() + purchaseOrder.getDifference_price();
        //修改采购产品的总金额
         count = purchaseMapper.updatePurchase(purchaseOrder.getId(),new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP));

       return purchaseOrder.getId();
    }


    //添加采购退货产品
    @Override
    @Transactional
    public Integer addPurchaseReturnOrder(String token ,Integer role , PurchaseOrder purchaseOrder)throws NullPointerException {

        //查询店铺信息
        HashMap hashMap = shopMapper.findSellerBySellerToken(token);

        if (role == 2 ){
            //查询店铺员工信息
            hashMap = shopMapper.findClerkByClerkToken(token);
        }

        if (hashMap == null){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }

        purchaseOrder.setMaking(hashMap.get("name").toString());//制单人姓名
        purchaseOrder.setBusiness_id(Integer.valueOf(hashMap.get("id").toString()));//商家id
        purchaseOrder.setNo(OrderCodeFactory.getStringRandom(3,2));//销售退货的是5位二维码标识
        purchaseOrder.setCreate_time(new Date());
        purchaseOrder.setMold(1);//0采购单，1采购退货单
        //添加采购退货产品
        Integer count = purchaseMapper.addPurchaseOrder(purchaseOrder);
        double price =0;
        PurchaseOrderDetail[] purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();
        for (PurchaseOrderDetail detail: purchaseOrderDetail ) {
            price += detail.getPurchase_number() * detail.getPurchase_price();
            detail.setPurchase_id(purchaseOrder.getId());

            //采购退货开单，先减少产品的虚拟库存
            shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id() , detail.getPurchase_number() );

            detail.setMold(1);//1采购退货详情单
            //添加采购退货产品详情
            count = purchaseMapper.addPurchaseOrderDetail(detail);

        }

        //总金额 = 采购所以产品价格+运费+差价
        double money = price +purchaseOrder.getFreight() + purchaseOrder.getDifference_price();
        //修改采购退货产品的总金额
        purchaseMapper.updatePurchase(purchaseOrder.getId(),new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP));
        return purchaseOrder.getId();
    }

    //代采购报表
    @Override
    public Page Purchasereport(String token  ,Integer role , Integer pageNo, Integer id)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //代采购报表总数
        Integer tatolCount = purchaseMapper.PurchasereportCount(bid,id);
        Page page = new Page(pageNum,tatolCount,20);

        //代采购报表
        List<HashMap> purchasereport = purchaseMapper.Purchasereport(bid,id, page.getStartIndex(), page.getPageSize());
        page.setRecords(purchasereport);
        return page;
    }

    //待审核，未付款，欠款总数
    @Override
    public Integer stayAuditedCount(String token , Integer role, Integer status, Integer state)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.purchaseCount(bid,status,state);
    }

    //待审核(采购未到货的订单)
    @Override
    public Page stayAudited(String token , Integer role , Integer  pageNo, String name) throws  NullPointerException{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //待审核总数
        Integer tatolCount = purchaseMapper.stayAuditedCount(new Paramt(bid,name));
        Page page = new Page(pageNum,tatolCount);

        //待审核
        List<HashMap> hashMaps = purchaseMapper.stayAudited(new Paramt(bid, name, page.getStartIndex(), page.getPageSize()));

        //待审核总金额
        HashMap hashMap = new HashMap();
        Double price= purchaseMapper.stayAuditedTatolMoney(new Paramt(bid,name,0));//采购总金额

        Double Return = purchaseMapper.stayAuditedTatolMoney(new Paramt(bid,name,1));//退货总金额

        hashMap.put("price",price);
        hashMap.put("return",Return);
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //确认到货
    @Override
    @Transactional
    public synchronized Integer updatePurchaseConfirmArrival(String token ,Integer role,Integer purchase_id,Integer mold ,Integer status)throws IndexOutOfBoundsException ,NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid ,1);
        if (sett != null && DateUtil.compareDate1(DateUtil.getDay(), sett.getTime()) == true ){
            throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
        }

        //处理是否有同时操作订单的隐患
        //通过订单id查询订单确认状态
        Integer purchaseStatus = purchaseMapper.findPurchaseStatus(purchase_id);
        if (purchaseStatus == null || purchaseStatus == status){
            throw new NullPointerException("订单id不存在或已被别的账号操作了");
        }

        //判断商家是否设置了库存设置
        Integer set = shopMapper.amountSet(bid);

        //通过订单id查询订单详情
        List<PurchaseOrderDetail> orderDetail = purchaseMapper.orderDetail(purchase_id);
        for (PurchaseOrderDetail detail: orderDetail ) {

            if (status == 1 && mold == 0 ){

                //采购入库单到货增加产品的实际库存
                shopMapper.increaseGoodsActualInventory(detail.getGoods_id() ,detail.getPurchase_number() );

            }else if( status == 1 && mold == 1 ){

                if (set == 1 ){

                    //采购退货订单数量报损数量是否大于实际库存
                    Integer num = purchaseMapper.isLessthanActualInventory(detail.getGoods_id() , detail.getPurchase_number() );
                    if (num == null){
                        throw new NullPointerException("产品["+detail.getName()+"]采购退货数量大于实际库存，因此不能到货。如要到货，则到商家设置里进行修改");
                    }
                }
                //采购退货单减少产品实际库存
                shopMapper.reduceGoodsActualInventory( detail.getGoods_id() ,detail.getPurchase_number());

            }else if (status == 2 &&  mold == 0){ //等于0是减库存

                //采购入库单取消减少产品的虚拟库存
                shopMapper.reduceGoodsFictitiousInventory( detail.getGoods_id() ,detail.getPurchase_number());


            }else if (status == 2 && mold == 1){ //退货，减库存

                //采购退货单增加产品虚拟库存
                shopMapper.increaseGoodsFictitiousInventory( detail.getGoods_id() ,detail.getPurchase_number() );
            }

            //确认到货的订单需要 记录每一件的产品库存
            if (status == 1 ){

                //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
                Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate(detail.getGoods_id()  );
                Double goodsInventory = null ;

                if (statisId == null ){
                    //通过产品id查询每天产品的初始库存
                    goodsInventory = shopMapper.selectGoodsInventory(detail.getGoods_id() );

                }else if (statisId == null && mold == 0 ){

                    //添加采购产品是否在每天产品库存统计记录(采购）
                    shopMapper.addPurchaseGoodsInventoryStatis( bid ,detail.getGoods_id()  ,new BigDecimal(goodsInventory) ,new BigDecimal(detail.getPurchase_number().toString() ) , new Date()  );

                }else if (statisId != null && mold == 0 ){

                    //增加采购产品是否在每天产品库存统计记录（采购）
                    shopMapper.increasePurchaseGoodsInventoryStatis(statisId , new BigDecimal( detail.getPurchase_number() ));

                }else if (statisId == null && mold == 1 ){

                    //添加数据减采购产品是否在每天产品库存统计记录(采购）
                    shopMapper.addReducePurchaseGoodsInventoryStatis( bid ,detail.getGoods_id() , new BigDecimal(goodsInventory) ,new BigDecimal(detail.getPurchase_number().toString() ) , new Date()  );

                }else if(statisId != null && mold == 1){

                    //减少采购产品是否在每天产品库存统计记录（采购）
                    shopMapper.reducePurchaseGoodsInventoryStatis(statisId , new BigDecimal(detail.getPurchase_number().toString() ));
                }
            }

        }

        double value = 0;
        //通过订单id查询是否是员工提成的订单
        Integer staff_id = purchaseMapper.selectPurchaseStaff(purchase_id);
        if (!staff_id.equals(0) && staff_id != null){

            //查询员工的采购9产品数量提成比例
            HashMap num = distributeMapper.findStaffPercentage(staff_id, 9);
            if (num != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购产品总数量
                    Double tatol = purchaseMapper.pGoodsTotalNumber(purchase_id);
                    //添加员工采购产品数量提成次数 type=9到货
                    orderMapper.addStaffFrequency(staff_id,purchase_id,8, tatol,new Date());                    }
            }

            //查询员工的10采购吨位提成比例
            HashMap ton = distributeMapper.findStaffPercentage(staff_id, 10);
            if (ton != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购产品吨位（斤）
                    Double weight = purchaseMapper.pTatolWeight(purchase_id);
                    //添加员工采购产品数量提成次数 type=10到货
                    orderMapper.addStaffFrequency(staff_id,purchase_id,9, weight,new Date());
                }
            }

            //查询员工的采购金额提成比例
            HashMap money = distributeMapper.findStaffPercentage(staff_id, 12);
            if (money != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购金额提成记录
                    Double tatol = purchaseMapper.purchaseTotalPrice(purchase_id);
                    //添加员工采购产品数量提成次数 type=10到货
                    orderMapper.addStaffFrequency(staff_id,purchase_id,10, tatol,new Date());
                }
            }

        }

        //获取登录人名称
        String name = shopMapper.findName(token);

        //采购确认
        return purchaseMapper.updatePurchaseConfirmArrival(purchase_id ,name ,status,new Date());
    }

    //未审核的采购订单直接收款
    @Override
    @Transactional
    public synchronized Integer updateOrderReceivables(String token, Integer role, Integer id, Integer state, Integer mold, Integer type, Double money) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        if (id == null ){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid ,1);
        if (sett != null && DateUtil.compareDate1(DateUtil.getDay(), sett.getTime()) == true ){
            throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
        }

        //处理是否有同时操作订单的隐患
        //通过订单id查询订单确认状态
        Integer purchaseStatus = purchaseMapper.findPurchaseStatus(id);
        if (purchaseStatus == null || purchaseStatus != 0 ){
            throw new NullPointerException("订单已被操作了");
        }

        String name = shopMapper.findName(token);

        //判断商家是否设置了库存设置
        Integer set = shopMapper.amountSet(bid);

        //通过订单id查询订单详情
        List<PurchaseOrderDetail> orderDetail = purchaseMapper.orderDetail(id);
        for (PurchaseOrderDetail detail: orderDetail ) {

            if (mold == 0 ){ //采购入库

                //采购入库单到货增加产品的实际库存
                shopMapper.increaseGoodsActualInventory(detail.getGoods_id() , detail.getPurchase_number() );

            }else if (mold == 1 ) {//采购退货

                if (set == 1 ){

                    //采购退货订单数量报损数量是否大于实际库存
                    Integer num = purchaseMapper.isLessthanActualInventory(detail.getGoods_id() ,detail.getPurchase_number() );
                    if (num == null){
                        throw new NullPointerException("产品["+detail.getName() +"]采购退货数量大于实际库存，因此不能到货。如要到货，则到商家设置里进行修改");
                    }
                }

                //采购退货单减少产品实际库存
                shopMapper.reduceGoodsActualInventory(detail.getGoods_id() ,detail.getPurchase_number() );
            }


            //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
            Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate(detail.getGoods_id()  );
            Double goodsInventory = null ;

            if (statisId == null ){
                //通过产品id查询每天产品的初始库存
                goodsInventory = shopMapper.selectGoodsInventory(detail.getGoods_id() );

            }else if (statisId == null && mold == 0 ){

                //添加采购产品是否在每天产品库存统计记录(采购）
                shopMapper.addPurchaseGoodsInventoryStatis( bid ,detail.getGoods_id()  ,new BigDecimal(goodsInventory) ,new BigDecimal(detail.getPurchase_number().toString() ) , new Date()  );

            }else if (statisId != null && mold == 0 ){

                //增加采购产品是否在每天产品库存统计记录（采购）
                shopMapper.increasePurchaseGoodsInventoryStatis(statisId , new BigDecimal( detail.getPurchase_number() ));

            }else if (statisId == null && mold == 1 ){

                //添加数据减采购产品是否在每天产品库存统计记录(采购）
                shopMapper.addReducePurchaseGoodsInventoryStatis( bid ,detail.getGoods_id() , new BigDecimal(goodsInventory) ,new BigDecimal(detail.getPurchase_number().toString() ) , new Date()  );

            }else if(statisId != null && mold == 1){

                //减少采购产品是否在每天产品库存统计记录（采购）
                shopMapper.reducePurchaseGoodsInventoryStatis(statisId , new BigDecimal(detail.getPurchase_number().toString() ));
            }


        }


        double value = 0;
        //通过订单id查询是否是员工提成的订单
        Integer staff_id = purchaseMapper.selectPurchaseStaff(id );
        if (!staff_id.equals(0) && staff_id != null){

            //查询员工的采购9产品数量提成比例
            HashMap num = distributeMapper.findStaffPercentage(staff_id, 9);
            if (num != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购产品总数量
                    Double tatol = purchaseMapper.pGoodsTotalNumber( id );
                    //添加员工采购产品数量提成次数 type=9到货
                    orderMapper.addStaffFrequency(staff_id,id ,8, tatol,new Date());                    }
            }

            //查询员工的10采购吨位提成比例
            HashMap ton = distributeMapper.findStaffPercentage(staff_id, 10);
            if (ton != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购产品吨位（斤）
                    Double weight = purchaseMapper.pTatolWeight( id );
                    //添加员工采购产品数量提成次数 type=10到货
                    orderMapper.addStaffFrequency(staff_id, id ,9, weight,new Date());
                }
            }

            //查询员工的采购金额提成比例
            HashMap money3 = distributeMapper.findStaffPercentage(staff_id, 12);
            if (money3 != null ){
                if (num.get("a") != null && !num.get("a").equals("") && num.get("b") != null && !num.get("b").equals("")){
                    //采购金额提成记录
                    Double tatol = purchaseMapper.purchaseTotalPrice( id );
                    //添加员工采购产品数量提成次数 type=10到货
                    orderMapper.addStaffFrequency(staff_id, id ,10, tatol,new Date());
                }
            }

        }

        Date date = new Date();
        if (state != 0 ){

            //添加采购收款分类记录
            purchaseMapper.addreceiptClass(bid ,id ,type , money ,date );
        }

        //未审核的采购订单直接收款
        return purchaseMapper.updateOrderStatusState(id ,state ,type  , money ,name,date );
    }

    //未审核的采购订单取消
    @Override
    @Transactional
    public Integer updateOrderCancel(String token, Integer role, Integer id, Integer mold) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        if (id == null ){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid ,1);
        if (sett.getTime() != null && !sett.getTime().equals("")){
            boolean b = DateUtil.compareDate1(DateUtil.getDay(), sett.getTime());
            if (b){
                throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
            }
        }

        //处理是否有同时操作订单的隐患
        //通过订单id查询订单确认状态
        Integer purchaseStatus = purchaseMapper.findPurchaseStatus(id);
        if (purchaseStatus == null || purchaseStatus != 0 ){
            throw new NullPointerException("订单已被操作了");
        }

        //通过订单id查询订单详情
        List<PurchaseOrderDetail> orderDetail = purchaseMapper.orderDetail(id);
        for (PurchaseOrderDetail detail: orderDetail ) {

            //判断mold是否是采购单还是采购退货单 ，进行修改对应的库存
            if (mold == 0){ //等于0是减库存

                //采购入库单取消减少产品的虚拟库存
                shopMapper.reduceGoodsFictitiousInventory( detail.getGoods_id() ,detail.getPurchase_number() );

            }else if (mold == 1){ //退货，减库存

                //采购退货单增加产品虚拟库存
                shopMapper.increaseGoodsFictitiousInventory( detail.getGoods_id() ,detail.getPurchase_number() );
            }

        }

        String name = shopMapper.findName(token);

        //未审核的采购订单取消
        return purchaseMapper.updatePurchaseConfirmArrival(id ,name ,2 ,new Date());
    }

    //查询采购未付款订单
    @Override
    public Page purchaseUnpaidOrder(String token, Integer role, Integer pageNo, String name ,String number, String staffName) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum =pageNo;
        }
        //查询采购未付款，欠款，已完成的订单总数
        Integer tatolCount = purchaseMapper.purchaseUnpaidOrderCount(bid ,name ,number ,staffName);
        Page page = new Page(pageNum,tatolCount);
        //查询采购未付款，欠款，已完成的订单总数
        List<HashMap> purchaseOrder = purchaseMapper.purchaseUnpaidOrder(bid,  name ,number  ,staffName , page.getStartIndex(), page.getPageSize());
        //查询采购未付款，欠款，已完成的订单总金额
        HashMap hashMap = purchaseMapper.purchaseUnpaidOrderTatolMoney(bid ,name ,number ,staffName);
        page.setHashMap(hashMap);
        page.setRecords(purchaseOrder);
        return page;
    }

    //查询采购欠款订单
    @Override
    public Page purchaseArrearsOrder(String token, Integer role, Integer pageNo, String name ,String number , String staffName) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum =pageNo;
        }
        //查询采购欠款订单总数
        Integer tatolCount = purchaseMapper.purchaseArrearsOrderCount(bid ,name ,number  ,staffName);
        Page page = new Page(pageNum,tatolCount);
        //查询采购欠款订单
        List<HashMap> purchaseOrder = purchaseMapper.purchaseArrearsOrder(bid,  name ,number ,staffName , page.getStartIndex(), page.getPageSize());
        //查询采购欠款订单总金额
        HashMap hashMap = purchaseMapper.purchaseArrearsOrderTatolMoney(bid ,name ,number ,staffName);
        page.setHashMap(hashMap);
        page.setRecords(purchaseOrder);
        return page;
    }

    // 查询采购已完成的订单
    @Override
    public Page findPurchaseOrder(String token ,Integer role , Integer pageNo , String name ,String number , String startTime, String endTime,String staffName)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum =pageNo;
        }
        //查询采购已完成的订单总数
        Integer tatolCount = purchaseMapper.findPurchaseOrderCount(bid ,name ,number ,startTime,endTime ,staffName);
        Page page = new Page(pageNum,tatolCount);
        //查询采购已完成的订单
        List<HashMap> purchaseOrder = purchaseMapper.findPurchaseOrder(bid , name ,number ,startTime,endTime ,staffName , page.getStartIndex(), page.getPageSize());
        for (HashMap hash : purchaseOrder ) {
            String tName = shopMapper.purchaseMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        //查询采购已完成的订单总金额
        HashMap hashMap = purchaseMapper.findPurchaseOrderTatolMoney(bid ,name ,number ,startTime,endTime ,staffName);
        page.setHashMap(hashMap);
        page.setRecords(purchaseOrder);
        return page;
    }

    //确认收款和确认欠款
    @Override
    @Transactional
    public Integer updatePurchaseOrderState(String token  ,Integer role,Integer id, Integer state, Integer type ,double money)throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid ,1);
        if (sett != null &&  DateUtil.compareDate1(DateUtil.getDay(), sett.getTime() ) == true ){
            throw new NullPointerException("今天的账单已经结算了，等待明天再操作了");
        }
        //处理是否有同时操作订单的隐患
        //通过订单id查询订单确认状态
        Integer purchaseState = purchaseMapper.findPurchaseState(id);
        if (purchaseState == null || purchaseState == state){
            throw new NullPointerException("订单id不存在或已被别的账号操作了");
        }

        //获取登录人名称
        String name = shopMapper.findName(token);
        if (!IntegerUtils.isEmpty(name)){
            throw new NullPointerException("登录失效，请重新登录");
        }
        Integer count = null;
        //判断订单是欠款后第二次收款，还是一次性收的款，一次性收的款是没有收款时间的
        String receivableTime = purchaseMapper.receivableTime(id);

        Date date = new Date();
        if (state == 1){//欠款单收款

            count = purchaseMapper.updateArrearPurchase(id,state,type,money,name,date);

         //（state == 2 && 有收款时间 && 有结算记录 && 欠款时间大于结算时间） || （state == 2 && 有收款时间 && 没有结算记录 ） money2也要相加
        }else if ( (state == 2 && receivableTime != null &&  !receivableTime.equals("") && sett != null && receivableTime.compareTo(sett.getTime()) > 0 ) ||  (state == 2 && receivableTime != null &&  !receivableTime.equals("") && sett == null ) ){

            count = purchaseMapper.updateNotReceivablesPurchase(id,state,type,money,name,date);

         //（state == 2 && 有收款时间 && 有结算记录 && 欠款时间小于或等于结算时间） || （state == 2 && 没有收款时间 ） 清空money2第一次收款的钱
        }else if ((state == 2 && receivableTime != null &&  !receivableTime.equals("") && sett != null && receivableTime.compareTo(sett.getTime()) <= 0 ) ||  (state == 2 && (receivableTime == null ||  receivableTime.equals("")) ) ){

            count = purchaseMapper.updateReceivablesPurchase(id,state,type,money,name ,date);
        }

        //添加采购收款分类记录
        purchaseMapper.addreceiptClass(bid ,id ,type , money ,date );

        return count;
    }

    //订单详情
    @Override
    public HashMap findPurchaseOrderDetail(Integer id)throws Exception {
        //查询订单的运费和差价
        HashMap hashMap =  purchaseMapper.freightAndDifferencePrice(id);
        //查询订单详情
        List<HashMap> purchaseOrderDetail = purchaseMapper.findPurchaseOrderDetail(id);
        hashMap.put("detail",purchaseOrderDetail);
        return hashMap;
    }

    //删除采购详情产品
    @Override
    @Transactional
    public Integer deletePurchaseDetail(Integer id ,Integer mold)throws Exception {

        //通过详情id查询采购产品的数量
        HashMap hashMap = purchaseMapper.purchaseGoodsNum(id);
        if (hashMap == null){
            return -1;
        }

        if (mold == 0 ){
            //减少产品虚拟库存
            shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(hashMap.get("goods_id").toString()),Double.valueOf(hashMap.get("purchase_number").toString()));
        }else {
            //增加产品虚拟库存
            shopMapper.increaseGoodsFictitiousInventory(Integer.valueOf(hashMap.get("goods_id").toString()),Double.valueOf(hashMap.get("purchase_number").toString()));
        }

        //通过采购订单id修改订单总金额
        purchaseMapper.updatePurchasePrice(Integer.valueOf(hashMap.get("purchase_id").toString()) , Double.valueOf(hashMap.get("price").toString()));

        Integer count = purchaseMapper.deletePurchaseDetail(id);

        //通过订单id查询是否还有订单详情
        List<Integer> dateilId = purchaseMapper.selectPurchaseDateilId(Integer.valueOf(hashMap.get("purchase_id").toString()));
        if (dateilId.size() <= 0 ){
            //说明此订单已经没有产品了，删除订单
            purchaseMapper.deletePurchase(Integer.valueOf(hashMap.get("purchase_id").toString()));
        }

        return count;
    }

    //修改采购订单产品
    @Override
    public Integer updatePurchaseOrderPrice(String token ,Integer role, PurchaseOrder purchaseOrder)throws Exception {
        double  price = 0;
        Give give = new Give();
        PurchaseOrderDetail[] purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();

        if (purchaseOrderDetail.length <= 0){
            //删除订单
            return purchaseMapper.deletePurchase(purchaseOrder.getId());
        }

        for (PurchaseOrderDetail detail:purchaseOrderDetail  ) {
            if (detail.getType() == 0){ //采购产品
                //判断是新增的还是修改的
                if (detail.getId() == null){
                    detail.setPurchase_id(purchaseOrder.getId());
                    purchaseMapper.addPurchaseOrderDetail(detail);

                    //销售先增加产品的虚拟库存，到货时根据订单id增加实际库存
                    shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),detail.getPurchase_number());

                }else {
                    //通过采购订单详情id查询原来的采购数量
                    HashMap hashMap = purchaseMapper.purchaseGoodsNum(detail.getId());
                    if (hashMap == null ){
                        throw new NullPointerException("订单不存在");
                    }

                    //判断开单时采购数量和修改后的采购数量是否一致

                    //原来的采购数量小于修改后的数量
                    if (Double.valueOf(hashMap.get("purchase_number").toString()) < detail.getPurchase_number()){

                        //用修改的数量 - 原来的数量 = 还剩多少库存
                        double order_number = detail.getPurchase_number() - Double.valueOf(hashMap.get("purchase_number").toString());
                        //增加产品虚拟库存
                        shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),order_number);

                        //原来的销售数量大于修改后的数量
                    }else if (Double.valueOf(hashMap.get("purchase_number").toString()) > detail.getPurchase_number()){

                        //用原来的数量 - 修改后的数量 = 还需要补回多少库存
                        double order_number = Double.valueOf(hashMap.get("purchase_number").toString()) - detail.getPurchase_number() ;
                        //减少产品虚拟库存
                        shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),order_number);
                    }

                    //修改采购订单的数量和产品价格
                    purchaseMapper.updatePurchaseOrderDetail(detail.getId(),detail.getPurchase_number(),detail.getPurchase_price());
                }
                //统计总采购金额
                price += detail.getPurchase_number()*detail.getPurchase_price();

            }else { //赠送产品
                //判断是新增的赠送还是修改赠送的
                if (detail.getId() == null){ //新增的
                    //采购时的赠送产品只添加数据，等到确认到货时在加库存
                    give.setGoods_id(detail.getGoods_id());//产品id
                    give.setSuppliers_id(purchaseOrder.getSuppliers_id());//供应商id
                    give.setGive_time(new Date());
                    give.setOperate_id(orderMapper.findOperateId(token));//操作人
                    give.setNum(detail.getPurchase_number());//赠送数量
                    give.setStatus(1);//0默认在赠送单里赠送，1在采购单里赠送
                    inventoryMapper.addGiveGoods(give);

                    detail.setPurchase_id(purchaseOrder.getId());
                    //新增的产品订单详情
                    purchaseMapper.addPurchaseOrderDetail(detail);
                    //修改赠送表的订单详情id
                    purchaseMapper.updateGive(give.getId(),detail.getId());
                    //销售先增加产品的虚拟库存，到货时根据订单id增加实际库存
                    shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),detail.getPurchase_number());

                }else { //修改的
                    //通过采购订单详情id查询原来的采购数量
                    HashMap hashMap = purchaseMapper.purchaseGoodsNum(detail.getId());
                    if (hashMap == null ){
                        throw new NullPointerException("订单不存在");
                    }

                    //判断开单时采购数量和修改后的采购数量是否一致

                    //原来的采购数量小于修改后的数量
                    if (Double.valueOf(hashMap.get("purchase_number").toString()) < detail.getPurchase_number()){

                        //用修改的数量 - 原来的数量 = 还剩多少库存
                        double order_number = detail.getPurchase_number() - Double.valueOf(hashMap.get("purchase_number").toString());
                        //增加产品虚拟库存
                        shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),order_number);

                        //原来的销售数量大于修改后的数量
                    }else if (Double.valueOf(hashMap.get("purchase_number").toString()) > detail.getPurchase_number()){

                        //用原来的数量 - 修改后的数量 = 还需要补回多少库存
                        double order_number = Double.valueOf(hashMap.get("purchase_number").toString()) - detail.getPurchase_number() ;
                        //减少产品虚拟库存
                        shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),order_number);
                    }

                    //通过订单详情id修改赠送表里的赠送数量
                    purchaseMapper.updateGiveNum(detail.getId(),detail.getPurchase_number(),new Date());
                    purchaseMapper.updatePurchaseOrderDetail(detail.getId(),detail.getPurchase_number(),detail.getPurchase_price());
                }
            }
        }
        //总金额 = 采购所以产品价格+运费+差价
        double money = price +purchaseOrder.getFreight() + purchaseOrder.getDifference_price();
        return purchaseMapper.updatePurchaseOrderPriceid(purchaseOrder.getId(),money ,purchaseOrder.getFreight(),purchaseOrder.getDifference_price());
    }

    //搜索商家供应商
    @Override
    public List<HashMap> findBusinessSupplies(String token ,Integer role ,String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.findBusinessSupplies(bid,name);
    }


    //打印模板
    @Override
    public Template printTemplate(String token ,Integer role , Integer type) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        if (bid == null){
            throw new NullPointerException("您的账号在另一台设备登录");
        }
        //查询商家的采购模板
        return  purchaseMapper.selectsTemplate(bid, type);
    }

    //打印采购订单
    @Override
    public PurchaseOrder purchasePrint(String token  ,Integer role , Integer order_id)throws Exception {
        //通过订单id查询订单信息
        PurchaseOrder purchase = purchaseMapper.findPurchaseInfoById(order_id);
        //查询订单详情总数
        purchase.setTatolCount(purchaseMapper.purchaseOrderDetailCount(order_id));
        purchase.setMoney(NumberToCN.number2CNMontrayUnit(new BigDecimal(purchase.getPrice())));//中文人民币大写
        return purchase;
    }

    //打印采购订单详情
    @Override
    public List<PurchaseOrderDetail> purchasePrintDetail(Integer pageNo, Integer order_id ,Integer pageSize) {
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //查询采购订单详情总数
        Integer tatolCont = purchaseMapper.purchaseOrderDetailCount(order_id);
        Page page = new Page(pageNum,tatolCont,pageSize);

        //通过订单id查询订单详情 1
        return purchaseMapper.purchaseOrderDetail(order_id,page.getStartIndex(),page.getPageSize());
    }

    //修改订单打印的次数
    @Override
    @Transactional
    public Integer updatePrintFrequ(Integer order_id) {
        //修改订单打印的次数
        return purchaseMapper.updatePrintFrequ(order_id,1);
    }


    //所有采购单据
    @Override
    public Page allPurchaseOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime , Integer mold)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        //所有采购单据总数
        Integer tatolCont = purchaseMapper.allPurchaseOrderCount(new Paramt(bid,name ,number,startTime,endTime,mold));
        Page page = new Page(pageNum,tatolCont);
        //所有采购单据(包括退货单据)
        List<HashMap> hashMaps = purchaseMapper.allPurchaseOrder(new Paramt(bid,name ,number,startTime,endTime,page.getStartIndex(),page.getPageSize(),mold));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.purchaseMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        //所有采购单据总金额
        HashMap hashMap = purchaseMapper.allPurchaseOrderTatolMoney(new Paramt(bid,name ,number,startTime,endTime,mold));

        //查询所有销售的到货总金额
        Double arrival = purchaseMapper.allArrivalTatolMoney(new Paramt(bid,name,number,startTime,endTime,mold));

        //查询所有销售的未到货总金额
        Double notArrival = purchaseMapper.allNotArrivalTatolMoney(new Paramt(bid,name,number,startTime,endTime,mold));

        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        hashMap.put("notArrival",notArrival);
        hashMap.put("arrival" ,arrival);

        return page;
    }

    //计算这一年总采购金额
    @Override
    public HashMap yearPurchassPirce(String token  ,Integer role)throws NullPointerException {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return purchaseMapper.yearPurchassPirce(bid);
    }

    //计算这季度总采购金额
    @Override
    public HashMap quarterPurchassPrice(String token  ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.quarterPurchassPrice(bid);
    }

    //计算这月总采购金额
    @Override
    public HashMap monthPurchassPrice(String token  ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.monthPurchassPrice(bid);
    }

    //计算这周总采购金额
    @Override
    public HashMap weekPurchassPrice(String token  ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.weekPurchassPrice(bid);
    }

    //计算当天总采购金额
    @Override
    public HashMap daysPurchassPrice(String token  ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.daysPurchassPrice(bid);
    }

    //自定义时间段总采购金额
    @Override
    public HashMap definitionPurchassPrice(String token  ,Integer role, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return purchaseMapper.definitionPurchassPrice(bid,startTime,endTime);
    }


    //导出待采购订单信息
    @Override
    public HSSFWorkbook excelPurchasereport(String token  ,Integer role, Integer id) {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        String sheetName = "供应商资料";//sheet名
        String[] title = new String[]{"仓库名称", "产品类别", "产品编号","产品名称","采购数量", "单位", "单价(元)","供应商","实际库存", "虚拟库存", "采购时间"};//标题
        List<HashMap> hashMaps = purchaseMapper.excelPurchaserepor(bid,id);
        String[][] values = new String[hashMaps.size()][];
        for (int i = 0; i < hashMaps.size(); i++) {
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("wname").toString();
            values[i][1] = hashMap.get("categoryName").toString();
            values[i][2] = hashMap.get("number").toString();
            values[i][3] = hashMap.get("name").toString();
            values[i][4] = hashMap.get("purchase_number").toString();
            values[i][5] = hashMap.get("units").toString();
            values[i][6] = hashMap.get("purchase_price").toString();
            values[i][7] = hashMap.get("suppliersName").toString();
            values[i][8] = hashMap.get("actual").toString();
            values[i][9] = hashMap.get("fictitious").toString();
            values[i][10] = hashMap.get("create_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(sheetName, title, values, null);
        return workbook;
    }

    //修改产品进价
    @Override
    @Transactional
    public Integer updateGoodsPrice(Integer goods_id, Double cost_price)throws Exception {
        return purchaseMapper.updateGoodsPrice(goods_id,cost_price);
    }

    //采购取消订单总数
    @Override
    public Integer cancelOrderCount(String token  ,Integer role, String startTime, String endTime) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //采购取消订单总数
        return  purchaseMapper.cancelOrderCount(bid ,startTime,endTime);
    }

    //采购取消订单
    @Override
    public Page cancelOrder(String token ,Integer role , Integer pageNo, String startTime, String endTime) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //采购取消订单总数
        Integer totalCount = purchaseMapper.cancelOrderCount(bid ,startTime,endTime);
        Page page = new Page(pageNum,totalCount);

        //采购取消订单
        List<HashMap> hashMaps = purchaseMapper.cancelOrder(bid,startTime,endTime,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //回退取消订单
    @Override
    @Transactional
    public Integer updatePurchaseStatus(Integer id) {

        List<PurchaseOrderDetail> orderDetail = purchaseMapper.orderDetail(id);

        for (PurchaseOrderDetail dateil: orderDetail ) {
            //增加产品虚拟库存
            shopMapper.increaseGoodsFictitiousInventory(dateil.getGoods_id()  ,dateil.getPurchase_number() );
        }

        return purchaseMapper.updatePurchaseStatus(id);
    }


    //收款记录
    @Override
    public List<HashMap> selectOrderMoneyRecords(Integer id) {
        return purchaseMapper.selectOrderMoneyRecords(id);
    }
}
