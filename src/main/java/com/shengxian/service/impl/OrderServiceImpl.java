package com.shengxian.service.impl;

import com.shengxian.common.util.*;
import com.shengxian.controller.OrderController;
import com.shengxian.entity.*;
import com.shengxian.mapper.*;
import com.shengxian.service.OrderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Date: 2018-11-20
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DistributeMapper distributeMapper;

    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private PurchaseMapper purchaseMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    //创建订单编号
    @Override
    public HashMap createOrder(String token ,Integer role ) throws NullPointerException{
        HashMap hashMap = new HashMap();

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询商家的运费
        Integer freight = shopMapper.findFreight(bid);
        String onlineOrderCode = OrderCodeFactory.getOnlineOrderCode("5" ,(long) bid, 5);

        //判断生成的订单号是否出现重复的了
        List<Integer> id = orderMapper.orderNumber(onlineOrderCode);
        if (id.size() > 0){
            onlineOrderCode = OrderCodeFactory.getOnlineOrderCode("8" ,(long) bid, 5);
        }

        hashMap.put("freight",freight);
        hashMap.put("orderNumber",onlineOrderCode);
        return hashMap;
    }

    //搜索商家用户
    @Override
    public List<HashMap> selectBindingUser(String token ,Integer role, String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.selectBindingUser(bid,name);
    }

    //通过用户id查询用户信息
    @Override
    public HashMap selectUserInfoById(Integer id) {
        return orderMapper.selectUserInfoById(id);
    }

    //查询绑定用户的收藏产品
    @Override
    public List<HashMap>    selectBandingUserGoodsCollection(String token ,Integer role, Integer binding_id,String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.selectBandingUserGoodsCollection(bid,binding_id ,name);
    }

    //添加用户的产品收藏
    @Override
    @Transactional
    public Integer addBandingUserGoodsCollection(Integer binding, Integer goods_id) throws NullPointerException{
        Collect collect = new Collect(binding, goods_id, new Date());

        Integer isExist = orderMapper.selectBindingIsExist(binding, goods_id);
        if (isExist != null ){
            throw new NullPointerException("已收藏过，不能再收藏了哟");
        }
        orderMapper.addBandingUserGoodsCollection(collect);
        return collect.getId();
    }

    //取消用户收藏产品
    @Override
    @Transactional
    public Integer deleteBandingUserGoodsCollection(String collection_id)throws Exception {
        Integer count = null;
        String[] split = collection_id.split(",");
        for (String cid: split ) {
            count = orderMapper.deleteBandingUserGoodsCollection(Integer.valueOf(cid));
        }
        return count;
    }

    //通过用户方案id和产品id查询查询产品信息
    @Override
    public List<HashMap> findGoodsInfoBySidAndGid( String token ,Integer role,String goods_id, Integer scheme_id, Integer binding_id) {
        //判断当前登录的账号是否是员工的
        HashMap shield = purchaseMapper.selectShield(token);

        List<HashMap> hashMaps = new ArrayList<HashMap>();
        String[] split = goods_id.split(",");
        for (String gid: split ) {
            HashMap hashMap = orderMapper.findGoodsInfoBySidAndGid(Integer.valueOf(gid), scheme_id,binding_id);
            if (shield != null && Integer.valueOf(shield.get("inv").toString()) == 1){
                hashMap.put("inv","库存不可见");
            }else {
                hashMap.put("inv","库存可见");
            }
            if (shield != null && Integer.valueOf(shield.get("min").toString()) == 1){
                hashMap.put("min","最低进价不可见");
            }else {
                hashMap.put("min","最低进价可见");
            }
            hashMaps.add(hashMap);
        }
        return hashMaps;
    }

    //销售订单
    @Override
    @Transactional
    public synchronized Integer addOrder(String token ,Integer role, Order order)throws IndexOutOfBoundsException ,NullPointerException,Exception{
        HashMap hashMap = null;
        //以防订单号出现重复
        List<Integer> id = orderMapper.orderNumber(order.getOrder_number());
        if (id.size() > 0){
            throw new NullPointerException("您提交的订单重复了");
        }

        //登录人id ，店铺默认0
        int operate = 0;

        //通过角色判断是店铺登录还是员工登录
        if (role == 1 ){

            //通过token查询店铺信息
            hashMap = shopMapper.findSellerBySellerToken(token);

        }else if (role == 2){

            //通过token查询员工信息
            hashMap = shopMapper.findClerkByClerkToken(token);
            operate = Integer.valueOf(hashMap.get("staff_id").toString());
        }
        if (hashMap == null ){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        //制单人
        order.setMaking(hashMap.get("name").toString());
        //销售的是6位二维码标识
        order.setNo(OrderCodeFactory.getStringRandom(3,3));
        //店铺id
        order.setBusiness_id(Integer.valueOf(hashMap.get("id").toString()));

        //判断是否指定员工派送订单吗
        //状态：1商城待接单，2待送货，3派送中，4确认到货，5拒绝接单，6取消订单
        if (order.getStaff_id() == 0){
            order.setStatus(2);
        }else {
            order.setStatus(3);
        }
        order.setCreate_time(new Date());//创建时间

        //添加订单
        orderMapper.addOrder(order);

        double price = 0;//订单总金额
        Give give = null;
        double costPrice = 0 ;
                //添加订单详情
        OrderDetail[] orderDetails = order.getOrderDetails();

        for (OrderDetail orderDetail: orderDetails ) {

            //根据产品id查询产品进价
            costPrice = shopMapper.findGoodsCostPrice(orderDetail.getGoods_id());
            double profit = 0 ;
            //type == 0 是销售产品
            if (orderDetail.getType() == 0){
                //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以销售数量等于纯盈利润

                profit = (orderDetail.getOrder_price() - costPrice) * orderDetail.getOrder_number();
                //计算当前订单的总金额，数量乘以产品销售价格
                price += orderDetail.getOrder_number() * orderDetail.getOrder_price();

             //type == 1 是报损赠送用户的产品
            }else if (orderDetail.getType() == 1){

                //报损产品
                give = new Give(orderDetail.getGoods_id(),order.getBinding_id(),orderDetail.getOrder_number(),new Date(),operate,1,orderDetail.getId());
                //添加报损记录
                inventoryMapper.addLossGoods(give);
                profit =  0 - ( costPrice * orderDetail.getOrder_number());
                orderDetail.setOrder_price(0.0); //报损售价归零
            }


            //销售先减少产品的虚拟库存，到货时根据订单id减少实际库存
            shopMapper.reduceGoodsFictitiousInventory(orderDetail.getGoods_id(),orderDetail.getOrder_number());
            orderDetail.setProfit(profit );
            //订单id
            orderDetail.setOrder_id(order.getId());
            //进价
            orderDetail.setCost_price(costPrice);
            //提交订单详情
            orderMapper.addOrderDetail(orderDetail);

            //判断是否是添加报损单的订单详情
            if (orderDetail.getType() == 1){
                //修改关联报损单的订单详情id
                orderMapper.updateGive(give.getId(),orderDetail.getId());
            }

        }

        //总金额 = 产品所有销售总金额  + 运费 + 差价
        double money = price + order.getFreight() + order.getDifference_price();

        //修改订单的总金额
        orderMapper.updateOrderPrice(order.getId(),new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP));

        //（operate不等于0）说明是员工账号登录的开单
        if (operate != 0){

            //通过员工id查询是否有销售开单提成比例
            HashMap staffPercentage = distributeMapper.findStaffPercentage(operate, 8);

            if (staffPercentage != null ){
                if (staffPercentage.get("a") != null && !staffPercentage.get("a").equals("") && staffPercentage.get("b") != null  && !staffPercentage.get("b").equals("")){

                    //添加员工的开单次数 type=7是开单
                    orderMapper.addStaffFrequency(operate,order.getId(),7,money,new Date());
                }
            }
        }

        //判断下订单是否调用飞蛾打印机打印订单 ， 条件是：print_frequ== 1 && type ==1
        if (order.getPrint_frequ() == 1 && Integer.valueOf(hashMap.get("type").toString()) == 1){

            //通过店铺id查询店铺飞蛾打印机
            List<String> snsList = shopMapper.businessPrinter(order.getBusiness_id());
            String[] sns = snsList.toArray(new String[snsList.size()]);
            

        }

        return order.getId();
    }


    //销售退货订单
    @Override
    @Transactional
    public Integer addReturnOrder(String token ,Integer role , Order order)throws Exception {

        //以防订单号出现重复
        List<Integer> id = orderMapper.orderNumber(order.getOrder_number());
        if (id.size() > 0){
            throw new NullPointerException("您提交的订单重复了");
        }

        HashMap hashMap = null;

        //通过角色和token查询服务商信息
        if (role == 1 ){
            hashMap = shopMapper.findSellerBySellerToken(token);

        }else if (role == 2){
            hashMap = shopMapper.findClerkByClerkToken(token);//通过token查询员工信息
        }
        if (hashMap == null ){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }

        order.setMaking(hashMap.get("name").toString());//制单人
        order.setNo(OrderCodeFactory.getStringRandom(3,2));//销售退货的是5位二维码标识
        order.setBusiness_id(Integer.valueOf(hashMap.get("id").toString())); //商家id

        //判断是否指定员工派送订单吗
        if (order.getStaff_id() != 0){
            order.setStatus(3);
        }else {
            order.setStatus(2); //线下开单直接是待送货，状态：1商城待接单，2待送货，3派送中，4确认到货，5拒绝接单，6取消订单
        }
        order.setMold(1);//1销售退货单
        order.setCreate_time(new Date());//创建时间
        //添加退货订单
        orderMapper.addOrder(order);

        //添加订单详情
        OrderDetail[] orderDetails = order.getOrderDetails();
        double price = 0;//订单总金额

        double costPrice = 0 ;

        for (OrderDetail orderDetail: orderDetails ) {

            //根据产品id查询产品进价
            costPrice = shopMapper.findGoodsCostPrice(orderDetail.getGoods_id());
            if (orderDetail.getType() == 0) {
                price += orderDetail.getOrder_number() * orderDetail.getOrder_price();//计算当前订单的总金额，数量乘以产品销售价格
            }
            //销售退货先增加产品的虚拟库存，到货时根据订单id增加实际库存
            shopMapper.increaseGoodsFictitiousInventory(orderDetail.getGoods_id(),orderDetail.getOrder_number());
            orderDetail.setOrder_id(order.getId());
            orderDetail.setCost_price(costPrice);
            //提交订单详情
            orderMapper.addOrderDetail(orderDetail);
        }

        //员工的开单提成不包括销售退货
        //总金额 = 销售所有产品价格 - 运费 - 差价
        double money = price - order.getFreight() - order.getDifference_price();

        //修改订单的总金额
        orderMapper.updateOrderPrice(order.getId(),new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP));

        return order.getId();
    }

    //（临时订单）通过订单编号查询历史订单
    @Override
    public HashMap historyOrder(String number)throws NullPointerException {
        //通过订单编号查询所有的订单信息
        HashMap hashMap = orderMapper.historyOrder(number);
        if (hashMap == null){
            throw new NullPointerException("订单编号不存在");
        }
        //通过订单id查询订单详情
        List<HashMap> orderDetail = orderMapper.findOrderInfoById(Integer.valueOf(hashMap.get("id").toString()));
        hashMap.put("orderDetail",orderDetail);
        return hashMap;
    }

    //查询用户的历史销售订单
    @Override
    public List<HashMap> findHistoryOrder(Integer binding_id, String startTime, String endTime) {
        return orderMapper.findHistoryOrder(binding_id,startTime,endTime);
    }

    //通过订单id查询订单详情
    @Override
    public List<HashMap> findOrderInfoById(String order_id)throws Exception {
        List<HashMap> hashMaps = new ArrayList<HashMap>();
        String[] split = order_id.split(",");
        for (String id: split ) {
            List<HashMap> order = orderMapper.findOrderInfoById(Integer.valueOf(id));
            hashMaps.addAll(order);
        }
        return hashMaps;
    }

    //临时订单
    @Override
    @Transactional
    public Integer addFalseOrder(String token ,Integer role , Order order)throws NullPointerException, Exception {
        HashMap hashMap = null;
        //通过角色和token查询服务商信息
        if (role == 1 ){
            hashMap = shopMapper.findSellerBySellerToken(token);
        }

        //通过角色和token查询员工信息
        if (role == 2){
            hashMap = shopMapper.findClerkByClerkToken(token);
        }

        if (hashMap == null ){
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }

        //通过订单id查询
        order.setNo(OrderCodeFactory.getStringRandom(3,3));//销售退货的是5位二维码标识
        order.setMaking(hashMap.get("name").toString());//制单人
        order.setBusiness_id(Integer.valueOf(hashMap.get("id").toString())); //商家id
        order.setStatus(2); //线下开单直接是待送货，状态：1商城待接单，2待送货，3派送中，4确认到货，5拒绝接单，6取消订单
        order.setCreate_time(new Date());//创建时间

        //添加临时订单
        orderMapper.addFalseOrder(order);

        //添加临时订单详情
        OrderDetail[] orderDetails = order.getOrderDetails();
        double price = 0;//订单总金额
        for (OrderDetail orderDetail: orderDetails ) {
            if (orderDetail.getType() == 0) {
                price += orderDetail.getOrder_number() * orderDetail.getOrder_price();//计算当前订单的总金额，数量乘以产品销售价格
            }
            orderDetail.setOrder_id(order.getId());
            orderMapper.addFalseOrderDetail(orderDetail);
            //临时订单不需要做库存的操作
        }

        //总金额 = 临时订单产品价格+运费+差价
        double money = price + order.getFreight() + order.getDifference_price();
        //修改临时订单的总金额
        orderMapper.updateFalseOrderPrice(order.getId(),money);

        return order.getId();
    }


    //添加费用支出记录
    @Override
    @Transactional
    public Integer addExpense(String token ,Integer role, Expense[] expense)throws NullPointerException, Exception {

        //默认是店铺操作
        int staff_id = 0;

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //role== 2 是员工登录
        if (role == 2){

            Integer sid = shopMapper.findStaffIdByToken(token);
            staff_id = sid;
        }

        Integer count = null;
        for (Expense e: expense ) {
            e.setStaff_id(staff_id);
            e.setBusiness_id(bid);
            e.setCreate_time(new Date());
            count = orderMapper.addExpense(e);
        }
        return  count;
    }


    //费用列表
    @Override
    public Page expenseList(String token ,Integer role, Integer pageNo, Integer type, String startTime, String endTime) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum= pageNo;
        }
        //费用列表总数
        Integer tatolCount = orderMapper.expenseListCount(bid,type,startTime,endTime);
        Page page = new Page(pageNum,tatolCount);

        //费用列表
        List<HashMap> hashMaps = orderMapper.expenseList(bid, type,startTime,endTime,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        HashMap hashMap = new HashMap();
        Double expense = null;
        Double income = null;
        if (type != null && type.equals(0)){
            //支出总额
            expense = orderMapper.expenseTotalMoney(bid, 0, startTime, endTime);
        }else if (type != null && type.equals(1)){
            //收入总额
            income = orderMapper.expenseTotalMoney(bid, 1, startTime, endTime);
        }else {
            //支出总额
            expense = orderMapper.expenseTotalMoney(bid, 0, startTime, endTime);
            //收入总额
            income = orderMapper.expenseTotalMoney(bid, 1, startTime, endTime);
        }
        hashMap.put("expense",expense);
        hashMap.put("income",income);
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //删除费用信息
    @Override
    @Transactional
    public Integer deleteExpense(String token, Integer role, Integer id)throws NullPointerException ,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //通过店铺id查询店铺销售财务结算时间
        Settlement sett = orderMapper.financialSettlement(bid,0);

        //通过费用id查询费用创建时间
        String expenseTime = orderMapper.expenseInfo(id);

        if (sett != null &&   DateUtil.compareDate1( expenseTime ,sett.getTime()) == true ){
            throw new NullPointerException("费用记录在结算之前创建的，因此不能删除");
        }

        return orderMapper.deleteExpense(id);
    }

    //超期进货的用户总数
    @Override
    public Integer overduePurchaseUserCount(String token, Integer role) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        return orderMapper.overdueUserCount(bid);
    }

    //超期进货的用户
    @Override
    public Page overduePurchaseUser(String token ,Integer role, Integer pageNo, String name, Integer cycle)throws NullPointerException {

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum= pageNo;
        }
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

       //超期进货的用户总数
        Integer tatolCount = orderMapper.overduePurchaseUserCount(bid,name,cycle);
        Page page = new Page(pageNum,tatolCount);

        //超期进货的用户
        List<HashMap> hashMaps = orderMapper.overduePurchaseUser(bid, name, cycle,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //标记超期进货为已读
    @Override
    @Transactional
    public Integer markReaded(Integer orderId) {
        return orderMapper.markReaded(orderId);
    }

    //没有销售的用户总数
    @Override
    public Integer noSalesUserCount(String token, Integer role) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        return orderMapper.noSalesUserCount(bid,null);
    }

    //没有销售的用户
    @Override
    public Page noSalesUser(String token ,Integer role, Integer pageNo, String name) throws NullPointerException {
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum= pageNo;
        }
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //没有销售的用户总数
        Integer tatolCount = orderMapper.noSalesUserCount(bid,name);
        Page page = new Page(pageNum,tatolCount);

        //没有销售的用户
        List<HashMap> hashMaps = orderMapper.noSalesUser(bid, name,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //待接单总数
    @Override
    public Integer waitingOrderCount(String token ,Integer role) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.waitingOrderCount(bid);
    }

    //待接单
    @Override
    public Page waitingOrder(String token ,Integer role ,Integer pageNo){
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum= pageNo;
        }

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //待接单总数
        Integer totalCount = orderMapper.waitingOrderCount(bid);
        Page page = new Page(pageNum ,totalCount) ;
        //待接单
        List<HashMap> hashMaps = orderMapper.waitingOrder(bid ,page.getStartIndex() ,page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //订单详情
    @Override
    public HashMap orderDetail(Integer id)throws NullPointerException {

        //通过订单id查询订单运费和差价
        HashMap hashMap = orderMapper.freightAndDifferencePrice(id);
        if (hashMap == null){
            throw new NullPointerException("订单id不存在");
        }
        //通过订单id查询订单详情
        List<HashMap> hashMaps = orderMapper.orderDetail(id);
        hashMap.put("detail",hashMaps);
        return hashMap;
    }

    //删除订单详情产品
    @Override
    @Transactional
    public Integer deleteOrderDetail(Integer id ,Integer mold )throws NullPointerException ,Exception {
        //通过订单详情id查询原来的销售数量
        HashMap hashMap = orderMapper.orderDetailNum(id);
        if (hashMap == null){
            throw new NullPointerException("订单详情id不存在");
        }

        Integer status = orderMapper.findOrderStatus(Integer.valueOf(hashMap.get("order_id").toString()));
        if (status == 4 ){
            throw new NullPointerException("订单已到货不能删除");
        }

        if (mold == 0){
            //增加产品虚拟库存
            shopMapper.increaseGoodsFictitiousInventory(Integer.valueOf(hashMap.get("goods_id").toString()),Double.valueOf(hashMap.get("order_number").toString()));
        }else {
            //减少产品虚拟库存
            shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(hashMap.get("goods_id").toString()),Double.valueOf(hashMap.get("order_number").toString()));
        }


        //删除订单详情产品时通过订单id减少订单总金额
        orderMapper.updateOrderTotalPrice(Integer.valueOf(hashMap.get("order_id").toString()) ,Double.valueOf(hashMap.get("price").toString()));

        //删除订单详情
        Integer count = orderMapper.deleteOrderDetail(id);

        //通过订单id查询是否还有订单详情
        List<Integer> dateilId = orderMapper.selectOrderDateilId(Integer.valueOf(hashMap.get("order_id").toString()));
        if (dateilId.size() <= 0 ){
            //说明此订单已经没有产品了，删除订单
            orderMapper.deleteOrder(Integer.valueOf(hashMap.get("order_id").toString()));
        }
        return count;
    }

    //修改订单销售产品金额和运费差价
    @Override
    @Transactional
    public synchronized  Integer updateOrderPrice(String token ,Integer role, Order order)throws NullPointerException, Exception {
        double  price = 0;
        Integer status = orderMapper.findOrderStatus(order.getId());
        if (status != null && status == 4 ){
            throw new NullPointerException("订单已到货不能修改");
        }

        OrderDetail[] orderDetails = order.getOrderDetails();
        //判断订单是否还有产品存在，没有则删除该订单
        if (orderDetails.length <= 0 ){
            //删除订单
           return orderMapper.deleteOrder(order.getId());
        }

        for (OrderDetail detail:orderDetails  ) {

            //根据产品id查询产品进价
            double costPrice = shopMapper.findGoodsCostPrice(detail.getGoods_id());

            //判断是销售单还是销售退货单

            //*******销售单并且是销售产品***********
            if (order.getMold() == 0  && detail.getType() == 0 ){

                //判断是否是新增的产品
                if (detail.getId()== null){ //新增的产品

                    //销售先减少产品的虚拟库存，到货时根据订单id减少实际库存
                    shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),detail.getOrder_number());

                    //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润
                    detail.setProfit((detail.getOrder_price() - costPrice) * detail.getOrder_number());
                    //
                    detail.setOrder_id(order.getId());//订单id
                    detail.setCost_price(costPrice);
                    orderMapper.addOrderDetail(detail);

                }else { //修改的产品信息

                    //通过订单详情id查询原来的销售数量
                    HashMap hashMap = orderMapper.orderDetailNum(detail.getId());
                    if (hashMap == null ){
                        throw new NullPointerException("订单详情id不存在");
                    }

                    //判断开单时销售数量和修改后的销售数量是否一致

                    //原来的销售数量小于修改后的数量
                    if (Double.valueOf(hashMap.get("order_number").toString()) < detail.getOrder_number()){

                        //用修改的数量 - 原来的数量 = 还减多少库存
                        double order_number = detail.getOrder_number() - Double.valueOf(hashMap.get("order_number").toString());
                        //减少产品虚拟库存
                        shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),order_number);

                        //原来的销售数量大于修改后的数量
                    }else if (Double.valueOf(hashMap.get("order_number").toString()) > detail.getOrder_number()){

                        //用原来的数量 - 修改后的数量 = 还需要补回多少库存
                        double order_number = Double.valueOf(hashMap.get("order_number").toString()) -detail.getOrder_number() ;
                        //增加产品虚拟库存
                        shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),order_number);
                    }

                    //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润
                    double profit = (detail.getOrder_price() - costPrice) * detail.getOrder_number();

                    //通过订单详情id修改产品数量和价格
                    orderMapper.updateOrderDetailPrice(detail.getId(),detail.getOrder_number(),detail.getOrder_price(),profit ,costPrice );
                }
                //统计每一条产品的销售金额
                price += detail.getOrder_number() * detail.getOrder_price();


             //******销售单并且是赠送给客户的产品 ***********
            }else if (order.getMold() == 0 && detail.getType() == 1 ){

                double profit = 0 - (  costPrice * detail.getOrder_number()) ;

                //新增的报损产品
                if (detail.getId() == null){
                    //报损产品
                    Give give = new Give();
                    give.setGoods_id(detail.getGoods_id());//产品id
                    give.setBinding_id(order.getBinding_id());//报损给用户id
                    give.setLoss_time(new Date());
                    give.setOperate_id(orderMapper.findOperateId(token));//操作人 0是商家 账号
                    give.setNum(detail.getOrder_number());//报损数量
                    give.setStatus(1);//0默认在报损单里报损，1在销售订单里报损
                    inventoryMapper.addLossGoods(give);//添加报损记录

                    //销售先减少产品的虚拟库存，到货时根据订单id减少实际库存
                    shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),detail.getOrder_number());
                    //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润

                    //销售价归零
                    detail.setOrder_price(0.0);
                    detail.setCost_price(costPrice); //进价
                    detail.setProfit(profit);
                    detail.setOrder_id(order.getId());//订单id


                    //提交订单详情
                    orderMapper.addOrderDetail(detail);
                    //修改报损单的订单详情id
                    orderMapper.updateGive(give.getId(),detail.getId());

                }else { // 修改的报损产品

                    HashMap hashMap = orderMapper.orderDetailNum(detail.getId());
                    if (hashMap == null ){
                        throw new NullPointerException("订单详情id不存在");
                    }
                    //判断开单时报损的数量和修改后的报损的数量是否一致

                    //原来的报损数量大于修改后的数量
                    if  (Double.valueOf(hashMap.get("order_number").toString()) > detail.getOrder_number()){

                        //用原来的数量 - 修改后的数量 = 还需要补回多少库存
                        double order_number = Double.valueOf(hashMap.get("order_number").toString()) - detail.getOrder_number() ;

                        //增加产品虚拟库存
                        shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),order_number);

                        ////原来的销售数量小于修改后的数量
                    }else if (Double.valueOf(hashMap.get("order_number").toString()) < detail.getOrder_number()){

                        //用修改的数量 - 原来的数量 = 还减多少库存
                        double order_number = detail.getOrder_number() - Double.valueOf(hashMap.get("order_number").toString());
                        //减少产品虚拟库存
                        shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),order_number);
                    }

                    //通过订单详情id修改报损表里的报损数量
                    orderMapper.updateGiveNum(detail.getId(),detail.getOrder_number(),new Date());

                    //通过订单详情id修改产品数量和价格
                    orderMapper.updateOrderDetailPrice(detail.getId(),detail.getOrder_number(),detail.getOrder_price(),profit ,costPrice);
                }

             //********销售退货单并且是退货产品************
            }else if (order.getMold() == 1 && detail.getType() == 0 ){

                //判断是否是新增的产品
                if (detail.getId()== null){ //新增的产品

                    //销售先增加产品的虚拟库存，到货时根据订单id增加实际库存
                    shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),detail.getOrder_number());

                    detail.setOrder_id(order.getId());//订单id
                    detail.setCost_price(costPrice);
                    orderMapper.addOrderDetail(detail);

                }else { //修改的产品信息

                    //通过订单详情id查询原来的退货数量
                    HashMap hashMap = orderMapper.orderDetailNum(detail.getId());
                    if (hashMap == null ){
                        throw new NullPointerException("订单详情id不存在");
                    }

                    //判断开单时退货数量和修改后的退货数量是否一致

                    //原来的销售数量小于修改后的数量
                    if (Double.valueOf(hashMap.get("order_number").toString()) < detail.getOrder_number()){

                        //用修改的数量 - 原来的数量 = 还增多少库存
                        double order_number = detail.getOrder_number() - Double.valueOf(hashMap.get("order_number").toString());
                        //增加产品虚拟库存
                        shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id(),order_number);

                        //原来的退货数量大于修改后的退货数量
                    }else if (Double.valueOf(hashMap.get("order_number").toString()) > detail.getOrder_number()){

                        //用原来的数量 - 修改后的数量 = 还需要补回多少库存
                        double order_number = Double.valueOf(hashMap.get("order_number").toString()) -detail.getOrder_number() ;
                        //减少产品虚拟库存
                        shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id(),order_number);
                    }


                    //通过订单详情id修改产品数量和价格
                    orderMapper.updateOrderDetailPrice(detail.getId(),detail.getOrder_number(),detail.getOrder_price(),0.0 ,costPrice );
                }
                //统计每一条产品的销售金额
                price += detail.getOrder_number() * detail.getOrder_price();

            }

        }//遍历

        double money = 0 ;

        if (order.getMold() == 0 ){
            //查询订单是否使用了优惠券
            Double couponMoney = orderMapper.selectConponMoney(order.getId());

            //总金额 = 销售所有产品价格 + 运费 +差价
            money = ( price + order.getFreight() + order.getDifference_price() ) - couponMoney ;

        }else if (order.getMold() == 1 ){

            //总金额 = 退货所有产品价格 - 运费 - 差价
            money = ( price - order.getFreight() - order.getDifference_price() )  ;
        }

        //通过订单id修改订单运费和差价
        return orderMapper.updatePurchaseOrderPriceid(order.getId(),money ,order.getFreight(),order.getDifference_price());
    }

    //修改订单打印状态
    @Override
    @Transactional
    public Integer updatePrintFrequ(Integer id) {
        return orderMapper.updatePrintFrequ(id);
    }

    //修改订单状态（2 接受）（5 拒绝）
    @Override
    @Transactional
    public synchronized Integer updateOrderAcceptOrRejectionStatus(String token, Integer role,Integer id , Integer status ,String reason) throws NullPointerException, Exception{
        Integer count = null;

        //通过订单id查询订单status状态和part区分
        HashMap SAP = orderMapper.statusAndPartById(id);
        if (SAP == null ){
            throw new NullPointerException("订单不存在");
        }
        Integer oStatus = Integer.valueOf(SAP.get("status").toString());
        if (oStatus == status ||  ( oStatus == 4 && status == 6  ) || (oStatus == 6 && status == 4 ) ||
                (oStatus == 4 && status == 2 ) || (oStatus == 6 && status == 2) || (oStatus == 5 && status == 3 )){
            throw new NullPointerException("订单已被操作过");
        }

        int staff_id = 0;

        if (status != 5 && status != 6 ){ // 不是拒绝订单

            //判断是线上的订单还是线下的订单

            //线上的订单需要减库存 条件是 :part == 1 && 查询订单原来的状态：Integer.valueOf(statusAndPart.get("status").toString()) == 1
            if (SAP.get("part").toString().equals("1") && Integer.valueOf(SAP.get("status").toString()) == 1){

                //通过订单id查询订单详情，
                List<HashMap> hashMaps = orderMapper.detail(id);
                for (HashMap detail:hashMaps ) {
                    //减少产品虚拟库存
                    shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(detail.get("goods_id").toString()),Double.valueOf(detail.get("num").toString()));
                }
            }

            if (role == 2 && status == 3 ){ // 员工登录
                //查询员工id
                staff_id = shopMapper.findStaffIdByToken(token);
            }

        }else if (status == 6){
            //店铺取消订单了，判断此订单是否使用个人优惠券
            if (SAP.get("coupon_id") != null  && !SAP.get("coupon_id").toString().equals("0")){
                //修改用户优惠券使用的状态为无使用中
                orderMapper.updateUserCouponState(Integer.valueOf(SAP.get("coupon_id").toString() ) , 0);
            }
        }

        //修改订单状态（接受或拒绝）print_frequ
        return orderMapper.updateAcceptOrRejectionStatus(id,staff_id ,status,new Date(),reason);
    }




    //一键接单
    @Override
    @Transactional
    public synchronized Integer onekeyAcceptOrder(String token ,Integer role) throws NullPointerException, Exception {
        Integer count = null;

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        if (bid == null ){
            throw new NullPointerException("登录已失效");
        }

        //通过店铺ID查询待接单中的订单
        List<HashMap> order_id = orderMapper.selectAcceptOrder(bid);
        if (order_id.size() <= 0){
            throw new NullPointerException("没有待接单的订单");
        }
        for (HashMap order :order_id  ) {
            //通过订单ID查询订单status状态
            Integer orderStatus = orderMapper.findOrderStatus(Integer.valueOf(order.get("id").toString()));
            //判断当前订单是否已被接单了
            if (!orderStatus.equals(1) ){
                throw new NullPointerException("其中一条订单号为:"+order.get("order_number").toString() +"已被接单了");
            }

            //通过订单id查询订单详情，
            List<HashMap> hashMaps = orderMapper.detail(Integer.valueOf(order.get("id").toString()));
            for (HashMap detail:hashMaps ) {
                //减少产品虚拟库存
                shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(detail.get("goods_id").toString()),Double.valueOf(detail.get("num").toString()));
            }
            // //修改订单状态（接受或拒绝）
            count = orderMapper.updateAcceptOrRejectionStatus(Integer.valueOf(order.get("id").toString()),null,2,new Date(),null);
        }
        return count;
    }

    //未打印的订单总数
    @Override
    public Integer notPrintedOrderCount(String token ,Integer role) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.notPrintedOrderCount(bid,null,null);
    }

    //未打印的订单
    @Override
    public Page notPrintedOrder(String token ,Integer role, Integer pageNo, String name , String number)throws NullPointerException ,Exception {

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //未打印订单的总数
        Integer tatolCount = orderMapper.notPrintedOrderCount(bid,name,number);
        Page page = new Page(pageNum,tatolCount);
        //未打印订单列表
        List<HashMap> hashMaps = orderMapper.notPrintedOrder(bid,name ,number,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //未打印订单汇总
    @Override
    public Page notPrintedOrderSummary(String token, Integer role, Integer pageNo, String name, String number) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = orderMapper.notPrintedOrderSummaryCount(new Paramt(bid,name ,number ));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = orderMapper.notPrintedOrderSummary(new Paramt(bid, name, number, page.getStartIndex(), page.getPageSize()));
        HashMap hashMap = orderMapper.notPrintedOrderSummaryTatolMoney(new Paramt(bid, name,number));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    ///待送货订单总数
    @Override
    public Integer stayDeliveredCount(String token ,Integer role) throws NullPointerException{
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.stayDeliveredCount(new Paramt(bid,"",""));
    }

    //待送货订单
    @Override
    public Page stayDelivered(String token ,Integer role, Integer pageNo, String name , String number ,String staffName)throws NullPointerException ,Exception{

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        Integer tatolCount = orderMapper.stayDeliveredCount(new Paramt(staffName , bid,name,number));
        Page page = new Page(pageNum,tatolCount);
        //待送货订单
        List<HashMap> hashMaps = orderMapper.stayDelivered(new Paramt( staffName ,bid,name,number,page.getStartIndex(),page.getPageSize()));

        //待送货订单总金额
        HashMap hashMap = new HashMap();
        Double sale = orderMapper.stayDeliveredTotalMoney(new Paramt(staffName ,bid, name, number,0));//销售单总金额
        Double Return = orderMapper.stayDeliveredTotalMoney(new Paramt( staffName ,bid, name, number,1));//退货单总金额
        hashMap.put("sale",sale);
        hashMap.put("return",Return);
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //待送货订单汇总
    @Override
    public Page stayDeliveredSummary(String token, Integer role, Integer pageNo, String name, String number, Integer mold) throws NullPointerException, Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCount = orderMapper.stayDeliveredSummaryCount(new Paramt( bid,name ,number ,mold ));
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = orderMapper.stayDeliveredSummary(new Paramt(bid, name, number ,mold , page.getStartIndex(), page.getPageSize()));
        HashMap hashMap = orderMapper.stayDeliveredSummaryTatolMoney(new Paramt(bid, name,number ,mold ));
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //确认到货或取消订单状态（4 确认已送达）（6 取消订单）
    @Override
    @Transactional
    public synchronized Integer updateOrderConfirmOrCancelStatus(String token ,Integer role,Integer id, Integer status, Integer mold)throws NullPointerException ,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid,0);
        if (sett != null && DateUtil.compareDate1(DateUtil.getDay(), sett.getTime()) == true ){
            throw new NullPointerException("今天的账单已经结算了，等待明天再结算");
        }

        //处理是否有同时操作订单的隐患
        //通过订单id查询订单到货状态
        Integer oStatus = orderMapper.findOrderStatus(id);
        //判断status是否一致
        if (oStatus == null || oStatus == status || (oStatus == 4 && status == 6) || (oStatus == 6 && status == 4 ) ){
            throw new NullPointerException("订单已被操作过");
        }

        //通过订单id查询订单信息（销售单才有提成）
        HashMap order = orderMapper.findOrderIfStaffIdPercentage(id);
        if (order == null){
            throw new NullPointerException("订单不存在");
        }

        double price = Double.valueOf(order.get("price").toString());//订单总金额

        //判断商家是否设置了库存设置
        Integer set = shopMapper.amountSet(bid);

        //订单详情
        List<HashMap> hashMaps = orderMapper.detail(id);

        //产品详情
        orderDetailTraversal(hashMaps ,id , bid , status , mold ,set);


        // 计算客户积分  销售单 status ==0
        if (status == 4 ){

            //通过订单id查询用户订单是线上的还是线下的
            HashMap bindingOrder = orderMapper.selectBindingOrder(id);
            if (bindingOrder == null){
                throw new NullPointerException("订单不存在");
            }
            //用户绑定id
            Integer binding_id = Integer.valueOf(bindingOrder.get("binding_id").toString());
            //订单归类 part == 0 线下订单 ，part == 1 商城订单
            Integer part = Integer.valueOf(bindingOrder.get("part").toString());


            //订单编号
            String order_number = bindingOrder.get("order_number").toString();
            //订单总金额
            Double totalPrice = Double.valueOf(bindingOrder.get("price").toString());


            //通过用户id查询用户积分id
            Integer integar_id = shopMapper.selectBindingIntegraId(binding_id);
            if (integar_id == null){
                throw new NullPointerException("用户积分id不存在 ");
            }

            double value1 = 0;
            //通过用户id获取对应的积分比例
            HashMap integraParam = shopMapper.selectBindingIntegra(binding_id); //积分比例
            if (integraParam != null ){

                if(mold == 0 && part == 0 && integraParam.get("c") != null && integraParam.get("d") != null && !integraParam.get("c").equals("") && !integraParam.get("d").equals("")){

                    value1 = GroupNumber.getValue( totalPrice , Double.valueOf(integraParam.get("c").toString()), Double.valueOf(integraParam.get("d").toString()));

                }else if(mold == 0 && part == 1 && integraParam.get("a") != null && integraParam.get("b") != null && !integraParam.get("a").equals("") && !integraParam.get("b").equals("")){

                    value1 = GroupNumber.getValue( totalPrice , Double.valueOf(integraParam.get("a").toString()), Double.valueOf(integraParam.get("b").toString()));


                }else if(mold == 1  && integraParam.get("c") != null && integraParam.get("d") != null && !integraParam.get("c").equals("") && !integraParam.get("d").equals("")){

                    value1 = GroupNumber.getValue( totalPrice , Double.valueOf(integraParam.get("c").toString()), Double.valueOf(integraParam.get("d").toString()));

                }

                if( mold == 0 && value1 > 0.0 ) {

                    //修改用户总积分（赠）
                    shopMapper.updateBindingIntegra(integar_id,value1);
                    //添加用户积分明细
                    shopMapper.addGiveBindingIntegra(new Integra(integar_id , value1 , part ,new Date(),order_number) );

                }else if( mold == 1 && value1 > 0.0 ){

                    //修改用户总积分（减）
                    shopMapper.reduceBindingIntegra(integar_id,value1);
                    //添加用户积分明细
                    shopMapper.addGiveBindingIntegra(new Integra(integar_id , value1 , 6 ,new Date(),order_number) );
                }

            }
        }

        //计算提成   销售单mold==0并且状态status==4
        if (status == 4 && mold == 0){
            //通过店铺id和下订单的绑定客户id查询店铺所有有客户提成比例的员工
            List<Percentage> bindings = orderMapper.selectBusinessAndBindingAllStaffPercentage(bid, Integer.valueOf(order.get("binding_id").toString()));
            for (Percentage pt : bindings  ) {
                if (pt.getA() != null &&  pt.getB() != null && pt.getA() != 0.0 && pt.getB() != 0.0 ){
                    double value = GroupNumber.getValue(price, pt.getA() , pt.getB()) ;
                    if (value != 0.0){
                        //添加员工提成金额记录
                        orderMapper.addStaffOrderStatis(id , pt.getStaff_id(),value,2,new Date());
                    }
                }
            }

            //通过店铺id查询店铺所有有金额提成比例的员工
            List<Percentage> moneys = orderMapper.selectBusinessAllMoneyStaffPercentage(bid);
            for (Percentage pt : moneys  ) {
                if (pt.getA() != null && pt.getB() != null && pt.getA() != 0.0  && pt.getB() != 0.0){
                    //添加员工到货提成次数 type=6店铺总销售
                    orderMapper.addStaffFrequency( pt.getStaff_id() ,id,6 ,price , new Date());
                }
            }

            //判断订单是否有员工接单
            if (order.get("staff_id") != null && !order.get("staff_id").toString().equals("0")){
                log.warn("【员工接单】时间"+DateUtil.getTime()+",订单为：" +id+",提成员工是：" +order.get("staff_id"));
                // 3，重量提成
                //通过员工id和类型查询吨位提成比例
                HashMap tonPercentage = distributeMapper.findStaffPercentage(Integer.valueOf(order.get("staff_id").toString()),4);
                if (tonPercentage != null ){
                    if (tonPercentage.get("a") != null && !tonPercentage.get("a").equals("") && tonPercentage.get("b") != null && !tonPercentage.get("b").equals("") ){
                        //计算当前销售产品的总斤
                        //通过订单id查询订单详情的销售产品总重量（斤）
                        Double weight = orderMapper.goodsTotalWeight(id ,bid);
                        if (weight != null ){
                            //添加员工重量提成记录 type=3
                            orderMapper.addStaffFrequency(Integer.valueOf(order.get("staff_id").toString()),id,3 ,weight ,new Date());
                        }
                    }
                }

                //5到达提成
                //通过员工id查询员工是否有到货提成比例
                HashMap arrive = distributeMapper.findStaffPercentage(Integer.valueOf(order.get("staff_id").toString()), 6);
                if (arrive != null ){

                    if (arrive.get("a") != null && !arrive.get("a").equals("") && arrive.get("b") != null && !arrive.get("b").equals("")){
                        log.warn("员工有到货提成，提成金额："+price);
                        //添加员工到货提成次数 type=5送达
                        orderMapper.addStaffFrequency(Integer.valueOf(order.get("staff_id").toString()),id,5 ,price , new Date());
                    }else {
                        log.warn("员工没有提成，订单金额为：" + price);
                    }
                }else {
                    log.warn("员工没有到货提成比例！订单金额为：" + price);
                }

            }else {
                //进入这里说明该订单没有任何人接单

                //获取当前登录的账号id
                Integer staffId = shopMapper.registerIdByTokenAndRole(token, role);
                if (staffId == null){
                    throw new NullPointerException("失败，请刷新重试！！！");
                }
                log.warn("【没有任何人接单】时间"+DateUtil.getTime()+",订单为：" +id+",提成员工是：" +staffId);
                if(staffId != null){
                    //5到达提成
                    //通过员工id查询员工是否有到货提成比例
                    HashMap staffArrive = distributeMapper.findStaffPercentage(staffId, 6);
                    if (staffArrive != null ){
                        if (staffArrive.get("a") != null && staffArrive.get("b") != null && !staffArrive.get("a").equals("")  && !staffArrive.get("b").equals("")){
                            log.warn("重新指派订单给当前员工，提成金额："+price);
                            //添加员工到货提成次数 type=5送达
                            orderMapper.addStaffFrequency(staffId ,id,5 ,price , new Date());

                            //重新指派订单给当前员工
                            orderMapper.updateStaffIdByOrderId(id , staffId);
                        }else {
                            log.warn("员工没有提成，订单金额为：" + price);
                        }
                    }else {
                        log.warn("员工没有到货提成比例！订单金额为：" + price);
                    }
                }


            }
        }else if ( status == 6 ){

            // 店铺取消订单了，判断此订单是否使用个人优惠券
            if (order.get("coupon_id") != null &&  !order.get("coupon_id").toString().equals("0")){

                orderMapper.updateUserCouponState(Integer.valueOf(order.get("coupon_id").toString() ) , 0);
            }
        }

        //获取登录人名称
        String name = shopMapper.findName(token);
        if (name == null || name.equals("")){
            throw new NullPointerException("登录失效，请重新登录");
        }

        //修改订单确认或取消状态（确认或取消）
        return orderMapper.updateOrderConfirmOrCancelStatus(id,status,new Date(),name);
    }

    /**
     * 订单详情遍历方法
     * @param detailTraversal
     * @param orderId 订单id
     * @param businessId 店铺id
     * @param status 订单状态
     * @param mold 订单类型
     * @param set 库存判断
     */
    public synchronized void orderDetailTraversal(List<HashMap> detailTraversal ,Integer orderId ,Integer businessId ,Integer status ,Integer mold ,Integer set  ){

        for (HashMap detail:detailTraversal ) {

            if( status == 4 && mold == 0 ){ //销售单确认到货

                if (set == 1 ){
                    //判断销售订单数量是否小于实际库存
                    Integer num = orderMapper.isLessthanActualInventory(Integer.valueOf(detail.get("goods_id").toString()), Double.valueOf(detail.get("num").toString()));
                    if (num == null){
                        throw new NullPointerException("产品["+detail.get("name")+"]销售数量低于实际库存，因此不能到货。如要到货，则到商家设置里进行修改");
                    }
                }

                //销售单，减少产品实际库存
                shopMapper.reduceGoodsActualInventory(Integer.valueOf(detail.get("goods_id").toString()) , Double.valueOf(detail.get("num").toString()) );

                //通过店铺id和产品id查询店铺所有有产品提成比例的员工
                List<Percentage> percentages = orderMapper.selectBusienssAndGoodsAllStaffPercentage( businessId ,Integer.valueOf(detail.get("goods_id").toString()) );
                for (Percentage pt : percentages  ) {

                    if (pt.getA() != null  && pt.getB() != null && pt.getA() != 0.0 && pt.getB() != 0.0 ){

                        double value = GroupNumber.getValue(Double.valueOf(detail.get("num").toString()) , pt.getA(), pt.getB() );
                        if (value != 0.0){
                            //添加员工产品提成金额记录
                            orderMapper.addStaffOrderStatis( orderId , pt.getStaff_id(),value,1,new Date());
                        }
                    }
                }

            }else if(status == 4 && mold == 1){ //销售退货单确认到货

                //销售退货单，要增加产品实际库存
                shopMapper.increaseGoodsActualInventory(Integer.valueOf(detail.get("goods_id").toString()),Double.valueOf(detail.get("num").toString()));

            }else if( status == 6 && mold == 0 ){ //销售单取消订单

                //销售单取消订单则增加产品虚拟库存
                shopMapper.increaseGoodsFictitiousInventory(Integer.valueOf(detail.get("goods_id").toString()),Double.valueOf(detail.get("num").toString()));

            }else if(status == 6 && mold == 1){ //销售退货单取消订单

                //销售退货单取消订单则减少产品虚拟库存
                shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(detail.get("goods_id").toString()),Double.valueOf(detail.get("num").toString()));
            }

            //确认到货的订单需要 记录每一件的产品库存
            if(status == 4  ){
                //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
                Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate(Integer.valueOf(detail.get("goods_id").toString()));
                Double goodsInventory = null ;
                if (statisId == null ){
                    //通过产品id查询每天产品的初始库存
                    goodsInventory = shopMapper.selectGoodsInventory(Integer.valueOf(detail.get("goods_id").toString()));

                }
                if (statisId == null && mold == 0 ){

                    //当前这件销售产品没有产品库存统计记录 则添加
                    shopMapper.addSaleGoodsInventoryStatis( businessId ,Integer.valueOf(detail.get("goods_id").toString()) , new BigDecimal(goodsInventory) ,new BigDecimal( Double.valueOf(detail.get("num").toString())) , new Date()  );

                }else if(statisId != null && mold == 0 ){

                    //增加销售产品是否在每天产品库存统计记录
                    shopMapper.increaseSaleGoodsInventoryStatis(statisId , new BigDecimal( Double.valueOf(detail.get("num").toString())));

                }else if(statisId == null && mold == 1 ){

                    //当前这件销售产品没有产品库存统计记录 则添加数据
                    shopMapper.addReduceSaleGoodsInventoryStatis( businessId ,Integer.valueOf(detail.get("goods_id").toString()) , new BigDecimal(goodsInventory) ,new BigDecimal( Double.valueOf(detail.get("num").toString())) , new Date()  );

                }else if(statisId != null && mold == 1 ){

                    //减少销售产品是否在每天产品库存统计记录
                    shopMapper.reduceSaleGoodsInventoryStatis(statisId , new BigDecimal( Double.valueOf(detail.get("num").toString())));
                }
            }
        }
    }



    //未到货的订单直接收款
    @Override
    @Transactional
    public Integer updateOrderReceivables(String token, Integer role, Integer id , Integer state, Integer mold , Integer type, Double money ) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid,0);
        if ( sett != null && DateUtil.compareDate1(DateUtil.getDay(), sett.getTime() ) == true ){
            throw new NullPointerException("今天的账单已经结算了 ");
        }


        //处理是否有同时操作订单的隐患
        //通过订单id查询订单到货状态
        Integer orderStatus = orderMapper.findOrderStatus(id);
        //判断status是否一致
        if (orderStatus == null ){
            throw new NullPointerException("订单不存在");
        }else if( orderStatus == 4 ){
            throw new NullPointerException("该订单已被操作了哟");
        }

        //通过订单id查询订单信息（销售单才有提成）
        Order order = orderMapper.selectOrderInfo(id);


        //判断商家是否设置了库存设置
        Integer set = shopMapper.amountSet(bid);

        //订单详情
        List<OrderDetail> orderDetails = orderMapper.orderDetailInfo(id);
        for (OrderDetail detail:orderDetails ) {

            //status == 4 && mold 销售单确认到货收款
            if ( mold == 0 ){

                if (set == 1 ){
                    //判断销售订单数量是否小于实际库存
                    Integer num = orderMapper.isLessthanActualInventory( detail.getGoods_id() , detail.getOrder_number());
                    if (num == null){
                        throw new NullPointerException("产品["+ detail.getName() +"]销售数量低于实际库存，因此不能到货。如要到货，则到商家设置里进行修改");
                    }
                }

                //销售单，减少产品实际库存
                shopMapper.reduceGoodsActualInventory(detail.getGoods_id() , detail.getOrder_number()  );

                //通过店铺id和产品id查询店铺所有有产品提成比例的员工
                List<Percentage> percentages = orderMapper.selectBusienssAndGoodsAllStaffPercentage( bid ,detail.getGoods_id() );
                for (Percentage pt : percentages  ) {

                    if (pt.getA() != null  && pt.getB() != null ){
                        double value = GroupNumber.getValue(detail.getOrder_number() , pt.getA(), pt.getB() );
                        if (value != 0.0){
                            //添加员工产品提成金额记录
                            orderMapper.addStaffOrderStatis( id , pt.getStaff_id(),value,1,new Date());
                        }
                    }
                }

            }else if( mold == 1 ){

                //销售退货单，要增加产品实际库存
                shopMapper.increaseGoodsActualInventory(detail.getGoods_id() ,detail.getOrder_number() );

            }


            //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
            Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate( detail.getGoods_id() );
            Double goodsInventory = null ;
            if (statisId == null ){
                //通过产品id查询每天产品的初始库存
                goodsInventory = shopMapper.selectGoodsInventory( detail.getGoods_id() );

            }
            if (statisId == null && mold == 0 ){

                //当前这件销售产品没有产品库存统计记录 则添加
                shopMapper.addSaleGoodsInventoryStatis( bid ,detail.getGoods_id() , new BigDecimal(goodsInventory) ,new BigDecimal( detail.getOrder_number().toString() ) , new Date()  );

            }else if(statisId != null && mold == 0 ){

                //增加销售产品是否在每天产品库存统计记录
                shopMapper.increaseSaleGoodsInventoryStatis(statisId , new BigDecimal(  detail.getOrder_number().toString()) );

            }else if(statisId == null && mold == 1 ){

                //当前这件销售产品没有产品库存统计记录 则添加数据
                shopMapper.addReduceSaleGoodsInventoryStatis( bid ,detail.getGoods_id(), new BigDecimal(goodsInventory) ,new BigDecimal(detail.getOrder_number().toString() ) , new Date()  );

            }else if(statisId != null && mold == 1 ){

                //减少销售产品是否在每天产品库存统计记录
                shopMapper.reduceSaleGoodsInventoryStatis(statisId , new BigDecimal( detail.getOrder_number().toString() ) );
            }


        }//订单详情 foreach 结束

        // 计算客户积分

        //通过订单id查询用户订单是线上的还是线下的
        Order bindingOrder = orderMapper.selectBindingOrderInfo(id);
        if (bindingOrder == null){
            throw new NullPointerException("订单不存在");
        }

        //用户绑定id
        Integer binding_id = Integer.valueOf( bindingOrder.getBinding_id() );

        //通过用户id查询用户积分id
        Integer integar_id = shopMapper.selectBindingIntegraId(binding_id);
        if (integar_id == null){
            throw new NullPointerException("用户积分id不存在");
        }

        double value1 = 0;
        //通过用户id获取对应的积分比例
        HashMap integraParam = shopMapper.selectBindingIntegra(binding_id); //积分比例
        if (integraParam != null ){

            if (mold == 0 && bindingOrder.getPart() == 0 ){ //销售单线下订单

                if (integraParam.get("c") != null && !integraParam.get("c").equals("") && integraParam.get("d") != null && !integraParam.get("d").equals("")){
                    value1 += GroupNumber.getValue( bindingOrder.getPrice() , Double.valueOf(integraParam.get("c").toString()), Double.valueOf(integraParam.get("d").toString()));

                    if (value1 > 0.0 ){
                        //修改用户总积分（赠）
                        shopMapper.updateBindingIntegra(integar_id,value1);
                        //添加用户积分明细
                        shopMapper.addGiveBindingIntegra(new Integra(integar_id , value1 , bindingOrder.getPart() ,new Date(),bindingOrder.getOrder_number()) );
                    }

                }
            } else if(mold == 0 && bindingOrder.getPart() == 1 ){//销售单商城订单
                if (integraParam.get("a") != null && !integraParam.get("a").equals("") && integraParam.get("b") != null && !integraParam.get("b").equals("")){
                    value1 += GroupNumber.getValue(bindingOrder.getPrice() , Double.valueOf(integraParam.get("a").toString()), Double.valueOf(integraParam.get("b").toString()));

                    if (value1 > 0.0 ){
                        //修改用户总积分（赠）
                        shopMapper.updateBindingIntegra(integar_id,value1);
                        //添加用户积分明细
                        shopMapper.addGiveBindingIntegra(new Integra(integar_id,value1, bindingOrder.getPart() ,new Date(), bindingOrder.getOrder_number() ));
                    }

                }
            }else if(mold == 1 ){//退货单

                if (integraParam.get("c") != null && !integraParam.get("c").equals("") && integraParam.get("d") != null && !integraParam.get("d").equals("")){
                    value1 += GroupNumber.getValue( bindingOrder.getPrice() , Double.valueOf(integraParam.get("c").toString()), Double.valueOf(integraParam.get("d").toString()));

                    if (value1 > 0.0 ){
                        //修改用户总积分（减）
                        shopMapper.reduceBindingIntegra(integar_id,value1);
                        //添加用户积分明细
                        shopMapper.addGiveBindingIntegra(new Integra(integar_id , value1 , 6 ,new Date(),bindingOrder.getOrder_number()) );
                    }
                }

            }
        }


        //获取登录人名称
        String name = shopMapper.findName(token);

        //计算提成   销售单mold==0 ,销售退货单没有提成
        if (mold == 0 ){

            //通过店铺id和下订单的绑定客户id查询店铺所有有客户提成比例的员工
            List<Percentage> bindings = orderMapper.selectBusinessAndBindingAllStaffPercentage(bid, order.getBinding_id());
            for (Percentage pt : bindings  ) {
                if (pt.getA() != null &&  pt.getB() != null ){
                    double value = GroupNumber.getValue(order.getPrice() , pt.getA() , pt.getB()) ;
                    if (value != 0.0){
                        //添加员工提成金额记录
                        orderMapper.addStaffOrderStatis(id , pt.getStaff_id(),value,2,new Date());
                    }
                }
            }

            //通过店铺id查询店铺所有有金额提成比例的员工
            List<Percentage> moneys = orderMapper.selectBusinessAllMoneyStaffPercentage(bid);
            for (Percentage pt : moneys  ) {
                if (pt.getA() != null && pt.getB() != null ){
                    //通过订单id查询到达的销售金额
                    Double tatol =orderMapper.saleOrderTotalPrice(id);
                    //添加员工到货提成次数 type=6到货
                    orderMapper.addStaffFrequency( pt.getStaff_id() ,id,6 ,tatol,new Date());
                }
            }

            //判断订单是否有员工接单
            if ( order.getStaff_id() != 0 ){

                // 3，重量提成
                //通过员工id和类型查询吨位提成比例
                HashMap tonPercentage = distributeMapper.findStaffPercentage(order.getStaff_id()  ,4);
                if (tonPercentage != null ){
                    if (tonPercentage.get("a") != null && !tonPercentage.get("a").equals("") && tonPercentage.get("b") != null && !tonPercentage.get("b").equals("") ){
                        //计算当前销售产品的总斤
                        //通过订单id查询订单详情的销售产品总重量（斤）
                        Double weight = orderMapper.goodsTotalWeight(id ,bid);
                        if (weight != null ){
                            //添加员工重量提成记录 type=3
                            orderMapper.addStaffFrequency(order.getStaff_id()  ,id,3 ,weight,new Date());
                        }
                    }
                }

                //5到达提成
                //通过员工id查询员工是否有到货提成比例
                HashMap arrive = distributeMapper.findStaffPercentage(order.getStaff_id() , 6);
                if (arrive != null ){
                    if (arrive.get("a") != null && !arrive.get("a").equals("") && arrive.get("b") != null && !arrive.get("b").equals("")){
                        //通过订单id查询到达的销售金额
                        Double tatol =orderMapper.saleOrderTotalPrice(id );
                        //添加员工到货提成次数 type=5送达
                        orderMapper.addStaffFrequency(order.getStaff_id()  ,id,5 ,tatol,new Date());
                    }
                }

            }//判断订单是否有员工接单..

        }

        //通过订单id查询是否是给员工配送的订单
        Integer distribute = orderMapper.isStaffDistributeOrder(id);//得到配送id
        if (distribute != null){
            //修改配送订单的操作记录
            orderMapper.updateDistributeDetailOpen(id);
            //通过配送id查询配送详情的所有订单是否都操作过了
            List<Integer> isPay =  orderMapper.isAllOrderPay(distribute);
            if (isPay.size() == 0){ //等于0，就说明配送的所有订单都操作过了
                //通过配送id修改员工分配一次记录完成的状态
                orderMapper.updateDistributeStatus(distribute);
            }
        }
        Date date = new Date();

        //未到货的订单直接收款

        if (state != 0 ){
            //添加收款分类记录
            orderMapper.addreceiptClass(bid ,id ,type , money ,date );
        }

        return orderMapper.updateOrderStatusState(id,state , type , money ,name , date );
    }

    //未到货的订单取消
    @Override
    @Transactional
    public Integer updateOrderCancel(String token, Integer role, Integer id,  Integer mold) throws NullPointerException, Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid,0);
        if (sett != null && DateUtil.compareDate1(DateUtil.getDay(), sett.getTime()) == true ){
            throw new NullPointerException("今天的账单已经结算了");
        }

        //处理是否有同时操作订单的隐患
        //通过订单id查询订单到货状态
        Integer orderStatus = orderMapper.findOrderStatus(id);
        //判断status是否一致
        if (orderStatus == null ){
            throw new NullPointerException("订单不存在");
        }else if( orderStatus == 6 ){
            throw new NullPointerException("该订单已被操作了哟");
        }

        //订单详情
        List<OrderDetail> orderDetails = orderMapper.orderDetailInfo(id);
        for (OrderDetail detail:orderDetails ) {

            if ( mold == 0 ){

                //销售单取消订单则增加产品虚拟库存
                shopMapper.increaseGoodsFictitiousInventory(detail.getGoods_id()  ,detail.getOrder_number());

            }else if ( mold == 1 ){

                //销售退货单取消订单则减少产品虚拟库存
                shopMapper.reduceGoodsFictitiousInventory(detail.getGoods_id()  ,detail.getOrder_number());
            }

        }
        //通过订单id查询订单信息（销售单才有提成）
        Order order = orderMapper.selectOrderInfo(id);

        //店铺取消订单了，判断此订单是否使用个人优惠券
        if (order.getCoupon_id() != null && order.getCoupon_id()  != 0 ){

            orderMapper.updateUserCouponState(order.getCoupon_id() , 0);
        }


        //获取登录人名称
        String name = shopMapper.findName(token);

        //未到货的订单取消
        return orderMapper.updateOrderConfirmOrCancelStatus(id,6 ,new Date(),name);
    }

    //查询未付款,欠款订单总数
    @Override
    public Integer arrivalOrderCount(String token ,Integer role, Integer state) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.arrivalOrderCount(bid,state);
    }

    //查询未付款的订单
    @Override
    public Page unpaidOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime ,String staffName ) throws NullPointerException, Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //查询未付款订单总数
        Integer tatolCount = orderMapper.unpaidOrderCount(new Paramt( staffName ,bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);
        //查询未付款订单
        List<HashMap> hashMaps = orderMapper.unpaidOrder(new Paramt( staffName ,bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));

        //查询未付款订单总金额
        HashMap hashMap =orderMapper.unpaidOrderTotalMoney(new Paramt(staffName ,bid,name,number,startTime,endTime));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }

    //查询欠款的订单
    @Override
    public Page arrearsOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime ,String staffName ) throws NullPointerException, Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //查询欠款订单总数
        Integer tatolCount = orderMapper.arrearsOrderCount(new Paramt(staffName ,bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);

        //查询欠款订单
        List<HashMap> hashMaps = orderMapper.arrearsOrder(new Paramt(staffName ,bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        //查询欠款订单总金额
        HashMap hashMap =orderMapper.arrearsOrderTotalMoney(new Paramt( staffName ,bid,name,number,startTime,endTime));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }

    //查询已完成订单
    @Override
    public Page arrivalOrder(String token ,Integer role, Integer pageNo, String name, String number , String startTime, String endTime,String staffName )throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //查询已完成订单总数
        Integer tatolCount = orderMapper.orderInfoCount(new Paramt(staffName ,bid,name,number,startTime,endTime));
        Page page = new Page(pageNum,tatolCount);
        //查询已完成订单
        List<HashMap> hashMaps = orderMapper.orderInfo(new Paramt(staffName ,bid,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize()));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        //根据状态分别统计对应的总金额
        HashMap hashMap = orderMapper.orderInfoTatolMoney(new Paramt(staffName ,bid,name,number,startTime,endTime));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }


    //收款或欠款
    @Override
    @Transactional
    public Integer updateArrivalOrder(String token ,Integer role,Integer id, Integer state , Integer type, Double money)throws NullPointerException, Exception  {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //查询当天是否有结算过
        Settlement sett = orderMapper.financialSettlement(bid ,0);
        if (sett != null &&  DateUtil.compareDate1(DateUtil.getDay(), sett.getTime()) == true){
            throw new NullPointerException("今天的账单已经结算了，不能收款了");
        }

        Integer count = null;



        if (state != 0 ){ //次判断是因为配送的订单要操作未付款按钮的事件的

            //通过订单id查询订单收款状态
            Integer orderState = orderMapper.findOrderState(id);
            if (orderState == null || orderState == state){ //处理是否有同时操作订单的隐患
                throw new NullPointerException("该订单已被收款或欠款了");
            }

            //获取登录人名称
            String name = shopMapper.findName(token);

            if (name == null || name.equals("")){
                throw new NullPointerException("登录失效，请重新登录");
            }

            Date date = new Date();

            //判断订单是欠款后第二次收款，还是一次性收的款，一次性收的款是没有收款时间的
            String receivableTime = orderMapper.receivableTime(id);
            if (state == 2){ //欠款 预付款

                count = orderMapper.updateArrivalOrder(id, state, type, money, date , name);

            //（state == 3 && 有收款时间 && 有结算记录 && 欠款时间大于结算时间） || （state == 3 && 有收款时间 && 没有结算记录 ） money2也要相加
            }else if( ( state == 3 && receivableTime != null &&  !receivableTime.equals("") && sett != null && receivableTime.compareTo(sett.getTime()) > 0 ) || ( state == 3 && receivableTime != null &&  !receivableTime.equals("") && sett == null  ) ){

                //未结算订单的收款
                count = orderMapper.updateNotReceivablesOrder(id, state, type, money, date, name);

            //（state == 3 && 有收款时间 && 有结算记录 && 欠款时间小于或等于结算时间） || （state == 3 && 没有收款时间 ） 清空money2第一次收款的钱
            }else if(( state == 3 && receivableTime != null &&  !receivableTime.equals("") && sett != null && receivableTime.compareTo(sett.getTime()) <= 0 ) || ( state == 3 && (receivableTime == null ||  receivableTime.equals(""))  ) ){

                //结算的订单的收款
                count = orderMapper.updateReceivablesOrder(id, state, type, money, date , name);
            }

            //添加收款分类记录
            orderMapper.addreceiptClass(bid ,id ,type , money ,date );

        }

        //通过订单id查询是否是给员工配送的订单
        Integer distribute = orderMapper.isStaffDistributeOrder(id);//得到配送id
        if (distribute != null){
            //修改配送订单的操作记录
            count = orderMapper.updateDistributeDetailOpen(id);
            //通过配送id查询配送详情的所有订单是否都操作过了
            List<Integer> isPay =  orderMapper.isAllOrderPay(distribute);
            if (isPay.size() == 0){ //等于0，就说明配送的所有订单都操作过了
                //通过配送id修改员工分配一次记录完成的状态
                count = orderMapper.updateDistributeStatus(distribute);
            }
        }
        return count;

    }

    //拒绝
    @Override
    public Page refuseOrder(String token ,Integer role, Integer pageNo) throws NullPointerException ,Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //拒绝订单总数
        Integer totalCount = orderMapper.refuseOrderCount(bid);
        Page page = new Page(pageNum,totalCount);

        //拒绝订单
        List<HashMap> hashMaps = orderMapper.refuseOrder(bid,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //所有销售订单
    @Override
    public Page allSaleOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime , Integer mold ,Integer type) throws NullPointerException,Exception {

        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //所有销售订单总数
        Integer totalCount = orderMapper.allSaleOrderCount(new Paramt(bid ,type ,name,number,startTime,endTime,mold ));
        Page page = new Page(pageNum,totalCount);
        //所有销售订单
        List<HashMap> hashMaps = orderMapper.allSaleOrder(new Paramt(bid , type ,name,number,startTime,endTime,page.getStartIndex(),page.getPageSize(),mold ));
        for (HashMap hash : hashMaps ) {
            String tName = shopMapper.salesMoneyRecordsGroupConcat(Integer.valueOf(hash.get("id").toString()));
            hash.put("tName" ,tName);
        }
        //所有销售订单总金额
        HashMap hashMap = orderMapper.allSaleOrderTatolMoney(new Paramt(bid , type,name,number,startTime,endTime,mold));

        //查询所有销售的未到货总金额
        Double notArrival = orderMapper.allNotArrivalTatolMoney(new Paramt(bid , type,name,number,startTime,endTime,mold ));
        //查询所有销售的到货总金额
        Double arrival = orderMapper.allArrivalTatolMoney(new Paramt(bid , type,name,number,startTime,endTime,mold ));
        hashMap.put("notArrival",notArrival);
        hashMap.put("arrival" ,arrival);

        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }


    @Override
    public HashMap yearSalePirce(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.yearSalePirce(bid);
    }

    @Override
    public HashMap quarterSalePrice(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.quarterSalePrice(bid);
    }

    @Override
    public HashMap monthSalePrice(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.monthSalePrice(bid);
    }

    @Override
    public HashMap weekSalePrice(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.weekSalePrice(bid);
    }

    @Override
    public HashMap daysSalePrice(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.daysSalePrice(bid);
    }

    @Override
    public HashMap definitionSalePrice(String token ,Integer role, String startTime, String endTime) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.definitionSalePrice(bid,startTime,endTime);
    }

    @Override
    public Integer userCancelOrderCount(String token ,Integer role, String startTime, String endTime)throws NullPointerException, Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.userCancelOrderCount(bid ,startTime,endTime);
    }

    //用户取消订单
    @Override
    public Page userCancelOrder(String token ,Integer role, Integer pageNo , String startTime, String endTime) {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        int pageNum =1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer totalCount = orderMapper.userCancelOrderCount(bid ,startTime,endTime);
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = orderMapper.userCancelOrder(bid,startTime,endTime,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //申请审核用户总数
    @Override
    public Integer auditUserCount(String token ,Integer role) throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.auditUserCount(bid);
    }

    //申请审核用户信息
    @Override
    public List<HashMap> auditUser(String token ,Integer role)throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        return orderMapper.auditUser(bid);
    }

    //审核用户
    @Override
    @Transactional
    public HashMap updateAuditUser(String token ,Integer role,Integer id,String phone, Integer state, String reason)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        //通过审核id查询是否操作过了
        Integer operationState = orderMapper.isOperation(id);
        if (operationState == null || operationState == state){
            throw new NullPointerException("该用户已被审核了");
        }
        HashMap hashMap = new HashMap();
        if (state == 2){
            orderMapper.updateAuditUser(id,state,reason);
            //1,通过手机号查询该手机号是否有备录在线下的电话号码中
            Integer uid = null;
            uid = orderMapper.findPhoneEqualTelephone(phone,bid);

            if (uid == null){
                //判断是否是取消后再次绑定商家
                uid = orderMapper.findPhoneEqualPhone(phone,bid);

            }
            hashMap.put("uid",uid);
        }else {
            orderMapper.updateAuditUser(id,state,reason);
            hashMap.put("no",1);
        }
        return hashMap;
    }

    //申请欠款审核总数
    @Override
    public Integer arrearsAuditCount(String token ,Integer role)throws NullPointerException {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );
        return orderMapper.arrearsAuditCount(bid);
    }

    //申请欠款审核列表
    @Override
    public List<HashMap> arrearsAudit(String token ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.arrearsAudit(bid);
    }

    //审核欠款
    @Override
    @Transactional
    public Integer updateArrearsAudit(Integer id, Integer state) throws NullPointerException ,Exception{
        //处理是否有同时操作订单的隐患
        //通过订单id查询订单收款状态
        Integer orderState = orderMapper.findOrderState(id);

        if (orderState == null || orderState == state){
            throw new NullPointerException("订单已被审核过了");
        }
        return orderMapper.updateArrearsAudit(id,state);
    }

    //打印销售订单
    @Override
    public HashMap orderPrint( Integer order_id)throws NullPointerException ,Exception{

        //通过订单id查询订单信息
        HashMap hashMap = orderMapper.orderPrintInfo(order_id);
        if (hashMap == null){
            throw new NullPointerException("订单id不存在");
        }

        String money = NumberToCN.number2CNMontrayUnit(new BigDecimal(Double.valueOf(hashMap.get("price").toString())));
        //订单详情总数
        Integer tatolCount = orderMapper.orderPrintCount(order_id);//
        hashMap.put("tatolCount",tatolCount);
        hashMap.put("money",money);
        return hashMap;
    }

    //打印销售订单详情
    @Override
    public List<HashMap> orderPrintDetail(Integer pageNo, Integer order_id ,Integer pageSize) {
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer tatolCont = orderMapper.orderPrintCount(order_id);
        Page page = new Page(pageNum,tatolCont,pageSize);
        //通过订单id查询订单详情 1
        return orderMapper.orderPrintDetail(order_id,page.getStartIndex(),page.getPageSize());
    }
    //打印销售订单详情


    //打印临时订单
    @Override
    public HashMap printTemporaryOrder(Integer order_id)throws NullPointerException {
        //通过订单id查询订单信息
        HashMap hashMap = orderMapper.printTemporaryOrder(order_id);
        if (hashMap == null){
            throw new NullPointerException("订单id不存在");
        }
        Integer tatolCount = orderMapper.printTemporaryOrderCount(order_id);//订单详情总数
        hashMap.put("tatolCount",tatolCount);
        String money = NumberToCN.number2CNMontrayUnit(new BigDecimal(Double.valueOf(hashMap.get("price").toString())));
        hashMap.put("money",money);
        return hashMap;
    }

    //打印临时订单详情
    @Override
    public List<HashMap> printTemporaryOrderDetail(Integer pageNo, Integer order_id  ,Integer pageSize) {

        //通过订单id查询销售订单详情的总数
        Integer tatolCont = orderMapper.orderPrintCount(order_id);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }

        Page page = new Page(pageNum,tatolCont,pageSize);
        //通过订单id查询临时订单详情
        return orderMapper.printTemporaryOrderDetail(order_id,page.getStartIndex(),page.getPageSize());
    }

    //超期用户导出
    @Override
    public HSSFWorkbook overduePurchaseUserDownload(String token ,Integer role) {
        Integer bid = null;
        bid = shopMapper.findIdByBusinessToken(token);
        if (bid == null){
            bid = shopMapper.findIdByStaffToken(token);
        }
        String seetName = "超期用户";//sheet名
        String[] title =new String[]{"类别", "手机号", "编号","客户","专员","进货周期","说明","绑定时间"};//标题
        List<HashMap> hashMaps = orderMapper.overduePurchaseUserDownload(bid);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("phone").toString();
            values[i][1] = hashMap.get("user_name").toString();
            values[i][2] = hashMap.get("number").toString();
            values[i][3] = hashMap.get("name").toString();

            if (hashMap.get("staff_name")==null){
                values[i][4] ="";
            }else {
                values[i][4] =hashMap.get("staff_name").toString();
            }
            values[i][5] = hashMap.get("cycle").toString()+"天";
            values[i][6] = hashMap.get("illustrated1").toString();
            values[i][7] = hashMap.get("create_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //没有销售用户导出
    @Override
    public HSSFWorkbook noSalesUserDownload(String token ,Integer role) {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        String seetName = "没有销售用户";//sheet名
        String[] title =new String[]{"类别", "客户", "编号","手机号","专员","说明","绑定时间"};//标题
        List<HashMap> hashMaps = orderMapper.noSalesUserDownload(bid);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("name").toString();
            values[i][1] = hashMap.get("user_name").toString();
            values[i][2] = hashMap.get("number").toString();
            values[i][3] = hashMap.get("phone").toString();
            if (hashMap.get("staff_name")==null){
                values[i][4] ="";
            }else {
                values[i][4] =hashMap.get("staff_name").toString();
            }
            values[i][5] = hashMap.get("illustrated1").toString();
            values[i][6] = hashMap.get("binding_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //回退取消订单
    @Override
    @Transactional
    public Integer updateOrderStatus(Integer id ,Integer mold)throws NullPointerException {

        //通过订单id查询订单是否使用了个人优惠券
        HashMap order = orderMapper.selectOrderCoupon(id);
        if (order == null){
            throw new NullPointerException("订单不存在");
        }

        if (mold == 0 ){
            //判断此订单是否使用个人优惠券
            if (order.get("coupon_id") != null && !order.get("coupon_id").toString().equals("0")){
                //通过优惠券id查询这张用户优惠券是否在别的订单使用了
                Coupon coupon = orderMapper.selectUserCouponState(Integer.valueOf(order.get("coupon_id").toString()));

                if (coupon.getState() == 1){ //state== 1 说明此优惠券已经在别的订单中使用了

                    //使用了，在回退之前得要加订单的总金额
                    orderMapper.plusOrderTotalPrice(id , coupon.getReduce());

                }else {
                    orderMapper.updateUserCouponState(Integer.valueOf(order.get("coupon_id").toString()) , 1);
                }

            }else if (order.get("coupon_id") != null && order.get("coupon_id").toString().equals("0")){

                //通过店铺优惠券id查询订单使用店铺满减优惠券是否过期了
                Coupon reduce = orderMapper.selectFullReduct(Integer.valueOf(order.get("activity").toString()));
                String day = DateUtil.getDay();
                if ( !(reduce.getStartTime().compareTo(day) <= 0 && reduce.getEndTime().compareTo(day) >= 0 ) ){
                    //店铺满减过期了，在回退之前得要加订单的总金额
                    orderMapper.plusOrderTotalPrice(id , reduce.getReduce());
                }

            }
        }

        //订单详情
        List<OrderDetail> orderDetails = orderMapper.orderDetailInfo(id);

        for (OrderDetail dateil: orderDetails ) {

            if (mold == 0 ){
                //减少产品虚拟库存
                shopMapper.reduceGoodsFictitiousInventory(dateil.getGoods_id()  ,dateil.getOrder_number() );

            }else if (mold == 1 ){
                //增加产品虚拟库存
                shopMapper.increaseGoodsFictitiousInventory(dateil.getGoods_id() , dateil.getOrder_number() );
            }

        }
        return orderMapper.updateOrderStatus(id);
    }


    //验单
    @Override
    @Transactional
    public Integer updateCheck(Integer id) throws Exception {

        return orderMapper.updateCheck(id);
    }

    //一键取消验单
    @Override
    @Transactional
    public Integer updateCancelCheck(String token ,Integer role ,Integer state, String startTime, String endTime) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.updateCancelCheck(bid,state,startTime,endTime);
    }

    //一键取消待送货验单
    @Override
    @Transactional
    public Integer updateStayDeliveredCancelCheck(String token ,Integer role) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.updateStayDeliveredCancelCheck(bid);
    }

    //积分兑换订单总数
    @Override
    public Integer selectIntegraOrderCount(String token, Integer role,  Integer state) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.selectIntegraOrderCount(bid ,state);
    }


    //积分兑换订单
    @Override
    public Page selectIntegraOrder(String token, Integer role, Integer pageNo, Integer state) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo ;
        }
        //积分兑换订单总数
        Integer totalCount = orderMapper.selectIntegraOrderCount(bid ,state);
        Page page = new Page(pageNum ,totalCount);
        List<HashMap> hashMaps = orderMapper.selectIntegraOrder(bid ,state , page.getStartIndex() ,page.getPageSize() );
        page.setRecords(hashMaps);
        return page;
    }

    //积分兑换订单确认
    @Override
    public Integer updateIntegraOrder(String token, Integer role, Integer id, Integer state) throws NullPointerException, Exception {

        //获取登录人名称
        String name = shopMapper.findName(token);
        if (name == null || name.equals("")){
            throw new NullPointerException("登录失效，请重新登录");
        }
        if (state == 2 ){//取消兑换，需要加回当前兑换用户的积分

            //通过订单id查询兑换用户的id和兑换的积分数
           HashMap order =  orderMapper.findIntegraOrderBDidAndNum(id);
           if (order == null ){
               throw new NullPointerException("兑换订单不存在");
           }
           //通过绑定用户id查询用户积分id
            Integer integraId = shopMapper.selectBindingIntegraId(Integer.valueOf(order.get("binding_id").toString()));

           //取消兑换，退回用户积分
           shopMapper.updateBindingIntegra(integraId ,Double.valueOf(order.get("price").toString()));
           //添加用户积分明细
            Integra integra = new Integra(integraId ,Double.valueOf(order.get("price").toString()) ,2 ,new Date() ,order.get("order_number").toString());
            shopMapper.addGiveBindingIntegra(integra);
        }

        //修改订单状态
        return orderMapper.updateIntegraOrder(id ,state , name ,new Date());
    }

    //售后服务总数
    @Override
    public Integer salesServiceTCount(String token, Integer role,Integer state) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        return orderMapper.salesServiceCount(bid ,state);
    }

    //售后服务
    @Override
    public Page salesService(String token, Integer role, Integer pageNo ,Integer state) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role );

        int pageNum=1;
        if (pageNo !=null && pageNo != 0){
            pageNum=pageNo;
        }
        Integer totalCount = orderMapper.salesServiceCount(bid ,state);
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = orderMapper.salesService(bid ,state , page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //处理售货服务
    @Override
    public Integer updateSalesService(Integer id, Integer state) {
        return orderMapper.updateSalesService(id ,state ,new Date() );
    }

    //收款记录
    @Override
    public List<HashMap> selectOrderMoneyRecords(Integer id) {
        return orderMapper.selectOrderMoneyRecords(id);
    }
}
