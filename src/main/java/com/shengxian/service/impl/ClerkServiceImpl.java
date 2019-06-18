package com.shengxian.service.impl;

import com.shengxian.common.MothPrinter;
import com.shengxian.common.util.*;
import com.shengxian.entity.*;
import com.shengxian.entity.clerkApp.Clerk;
import com.shengxian.entity.clerkApp.ShoppingMall;
import com.shengxian.entity.clerkApp.ShoppingMallDateil;
import com.shengxian.mapper.*;
import com.shengxian.service.ClerkService;
import io.swagger.models.auth.In;
import org.apache.http.cookie.SM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Description: 员工APP接口业务实现层
 *
 * @Author: yang
 * @Date: 2019-04-12
 * @Version: 1.0
 */
@Service
public class ClerkServiceImpl implements ClerkService {

    @Resource
    private ClerkMapper clerkMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private DistributeMapper distributeMapper;

    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private PurchaseMapper purchaseMapper;

    @Resource
    private MenuMapper menuMapper;

    //查询员工APP版本号
    @Override
    public String version() throws Exception {
        return clerkMapper.version();
    }

    //app中判断在是否有退出登录过
    @Override
    public boolean appIsLogin(String token ,String phone, String model, String system, String version, String platform, String SDKVersion) throws Exception{
        Integer bool = clerkMapper.appIsLogin(token , phone , model, system, version, platform, SDKVersion);
        if (bool != null){
            return true;
        }
        return false;
    }

    //修改手机设备
    @Override
    @Transactional
    public Integer updateEquipment(String token, String model, String system, String version, String platform, String SDKVersion) throws Exception{
        return clerkMapper.updateEquipment(token,model,system,version,platform,SDKVersion);
    }

    //共享订单
    @Override
    public Page sharingOrder(String token ,Integer role , Integer pageNo) throws NullPointerException, Exception {
        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        //查询共享订单总数
        Integer totalCount = clerkMapper.sharingOrderCount(business_id);
        Page page = new Page(pageNum,totalCount);
        //查询共享订单集合
        List<HashMap> hashMaps = clerkMapper.sharingOrder(business_id ,page.getStartIndex() ,page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }


    //订单详情
    @Override
    public HashMap orderDetail( Integer order_id) throws Exception {
        //通过订单ID查询客户和产品信息
        HashMap hashMap = clerkMapper.orderInfo(order_id);
        if (hashMap == null ){
            throw new NullPointerException("订单不存在");
        }

        //通过订单ID查询订单详情的产品信息
        List<HashMap> dateil = clerkMapper.orderDateilGoods(order_id);
        hashMap.put("dateil" ,dateil);
        return hashMap;
    }

    //未到货的订单
    @Override
    public Page noArrivedOrder(String token ,Integer role , Integer pageNo ,String name) throws Exception {
        HashMap hashMap = new HashMap();
        //通过token查询商家ID和员工ID
        Clerk clerk = clerkMapper.bidAndSidByToken(token);
        int pageNum = 1 ;
        if (pageNo != null && pageNo != 0){
            pageNum = pageNo;
        }
        //未到货订单总数
        Integer totalCount = clerkMapper.noArrivedOrderCount(new AppParameter(clerk.getBusiness_id() ,clerk.getId() ,name));
        Page page = new Page(pageNum,totalCount);
        //未到货订单列表
        List<HashMap> hashMaps = clerkMapper.noArrivedOrder(new AppParameter( clerk.getBusiness_id() ,clerk.getId() ,name,page.getStartIndex() ,page.getPageSize()));

        //销售未到货的订单总金额
        Double price = clerkMapper.noArrivedOrderTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name ,0));

        //退货未到货的订单总金额
        Double retreat_price = clerkMapper.noArrivedOrderTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name ,1));

        hashMap.put("retreat_price" ,new BigDecimal(retreat_price).setScale(2,BigDecimal.ROUND_CEILING));
        hashMap.put("price" ,new BigDecimal(price).setScale(2,BigDecimal.ROUND_CEILING));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }


    //未收款的订单
    @Override
    public Page uncollectedOrderList(String token ,Integer role , Integer pageNo, String name) throws NullPointerException, Exception {

        HashMap hashMap = new HashMap();
        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo) ){
            pageNum =  pageNo;
        }
        //通过token查询商家ID和员工ID
        Clerk clerk = clerkMapper.bidAndSidByToken(token);

        //未收款的订单总数
        Integer totalCount = clerkMapper.uncollectedOrderListCount(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name));
        Page page = new Page(pageNum,totalCount);
        //未收款的订单
        List<HashMap> hashMaps = clerkMapper.uncollectedOrderList(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name ,page.getStartIndex(),page.getPageSize()) );
        //未付款订单总金额(销售单)
        Double price = clerkMapper.uncollectedOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name,  0));
        //未付款订单总金额(销售退货单)retreat
        Double retreat_price = clerkMapper.uncollectedOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name , 1));
        hashMap.put("price" ,new BigDecimal(price).setScale(2,BigDecimal.ROUND_CEILING));
        hashMap.put("retreat_price" ,new BigDecimal(retreat_price).setScale(2,BigDecimal.ROUND_CEILING));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }

    //欠款的订单
    @Override
    public Page arrearsOrderList(String token ,Integer role , Integer pageNo, String name) throws NullPointerException, Exception {
        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        HashMap hashMap = new HashMap();
        Clerk clerk = clerkMapper.bidAndSidByToken(token);

        Integer totalCount = clerkMapper.arrearsOrderListCount(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name));
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = clerkMapper.arrearsOrderList(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name ,page.getStartIndex(),page.getPageSize()) );
        //欠款订单总金额(销售单)
        Money money = clerkMapper.arrearsOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name, 0));
        //欠款订单总金额(销售退货单)
        Money retreat = clerkMapper.arrearsOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name, 1));
        hashMap.put("price", new BigDecimal(money.getPrice()).setScale(2 ,BigDecimal.ROUND_CEILING)); //欠款金额
        hashMap.put("money", new BigDecimal(money.getMoney2()).setScale(2 ,BigDecimal.ROUND_CEILING)); //已收金额
        hashMap.put("retreat_price", new BigDecimal(retreat.getPrice()).setScale(2 ,BigDecimal.ROUND_CEILING)); //退货欠款金额
        hashMap.put("retreat_money", new BigDecimal(retreat.getMoney2()).setScale(2 ,BigDecimal.ROUND_CEILING)); //退货已付金额
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //完成的订单
    @Override
    public Page completeOrderList(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime) throws NullPointerException, Exception {
        HashMap hashMap = new HashMap();
        Clerk clerk = clerkMapper.bidAndSidByToken(token);
        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo) ){
            pageNum = pageNo;
        }
        Integer totalCount = clerkMapper.completeOrderListCount(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name,startTime ,endTime));
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = clerkMapper.completeOrderList(new AppParameter(clerk.getBusiness_id() ,clerk.getId()  ,name ,startTime ,endTime ,page.getStartIndex(),page.getPageSize()) );
        //完成的订单总金额(销售单)
        Money money = clerkMapper.completeOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name,startTime,endTime, 0));
        //完成的订单总金额(销售退货单)
        Money retreat = clerkMapper.completeOrderListTotalMoney(new AppParameter(clerk.getBusiness_id() ,clerk.getId() , name,startTime,endTime, 1));
        hashMap.put("price", new BigDecimal(money.getPrice()).setScale(2 ,BigDecimal.ROUND_CEILING)); //欠款金额
        hashMap.put("money", new BigDecimal(money.getMoney()).setScale(2 ,BigDecimal.ROUND_CEILING)); //已收金额
        hashMap.put("retreat_price", new BigDecimal(retreat.getPrice()).setScale(2 ,BigDecimal.ROUND_CEILING)); //退货欠款金额
        hashMap.put("retreat_money", new BigDecimal(retreat.getMoney()).setScale(2 ,BigDecimal.ROUND_CEILING)); //退货已付金额
        page.setHashMap(hashMap);
        page.setRecords(hashMaps);
        return page;
    }

    //员工功能列表
    @Override
    public List<HashMap> staffFunctionList(String token) throws  Exception {
        //获取员工ID
        Integer staff_id = shopMapper.findStaffIdByToken(token);

        //通过员工ID查询APP功能菜单列表
        return clerkMapper.staffFunctionList(staff_id);
    }

    //客户列表
    @Override
    public Page bindingUserList(String token ,Integer role ,Integer pageNo ,Integer category_id , String name) throws Exception {
        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        Integer totalCount = clerkMapper.bindingUserListCount(new AppParameter(business_id,name,category_id));
        Page page = new Page(pageNum ,totalCount);
        List<HashMap>  hashMaps = clerkMapper.bindingUserList(new AppParameter(business_id ,name,category_id,page.getStartIndex() ,page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //供应商列表（分页）
    @Override
    public Page suppliersList(String token, Integer role, Integer pageNo, Integer category_id, String name) throws Exception {
        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        Integer totalCount = clerkMapper.suppliersListCount(new AppParameter(business_id,name,category_id));
        Page page = new Page(pageNum ,totalCount);
        List<HashMap>  hashMaps = clerkMapper.suppliersList(new AppParameter(business_id ,name,category_id,page.getStartIndex() ,page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //已录入的订单
    @Override
    public Page haveJoinedShoppingcart(String token, Integer role, Integer pageNo, Integer mold, Integer type) {
        int pageNum = 1 ;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }

        //根据token和角色查询注册ID
        Integer id = shopMapper.registerIdByTokenAndRole(token, role);

        //已录入的订单总数
        Integer totalCount = clerkMapper.haveJoinedShoppingcartCount(id , mold , type );
        Page page = new Page(pageNum ,totalCount);
        //已录入的订单
        List<HashMap>  hashMaps = clerkMapper.haveJoinedShoppingcart(id , mold ,type ,page.getStartIndex() ,page.getPageSize());
        String time = DateUtil.getTime();
        for (HashMap hash: hashMaps ) {
            //判断加入的购物车时间是否超过24小时了
            boolean b = DateUtil.compareDateTime(hash.get("time").toString() , time );
            if (b){
                //进来这里代表员工加入这个客户的购物车已经超过24小时了
                //通过购物车ID删除购物车详情
                clerkMapper.deleteShoppingMallDateilBySmId(Integer.valueOf(hash.get("sm_id").toString()) );
                //删除购物车
                clerkMapper.deleteShoppingMall(Integer.valueOf(hash.get("sm_id").toString()));
            }
        }
        page.setRecords(hashMaps);
        return page;
    }

    //通过客户ID查询客户信息
    @Override
    public HashMap bindingUserById(Integer id) throws Exception {
        return clerkMapper.bindingUserById(id);
    }

    //店铺产品类别
    @Override
    public List<HashMap> busienssGoodsCategory(String token  ,Integer role, Integer level) throws Exception {

        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        return clerkMapper.busienssGoodsCategory(business_id,level);
    }

    //app店铺类别下的产品（客户）
    @Override
    public Page businessGoodsList(String token  ,Integer role, Integer pageNo,Integer binding_id , Integer category_id, String name) throws Exception {

        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        byte inv = 0 ; // 1屏蔽库存
        if (role == 2 ){ //员工登录

            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap hash = purchaseMapper.selectShield(token);
            inv = Byte.parseByte(hash.get("inv").toString());
        }

        int pageNum = 1;
        if( IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer level = null ,cid = null;

        //判断是大类别id还是小类别id
        Integer le = clerkMapper.largeClassOrSmalClass(category_id);
        if (le == null || le == 0){ //判断le如果等于null，就代表是大类下的产品
            level = category_id;
        }else {
            cid = category_id;
        }

        //通过绑定用户id查询方案id
        Integer schemeId = shopMapper.selectBindingSchemeId(binding_id);

        Integer totalCount = clerkMapper.businessGoodsListCount(new AppParameter(business_id , binding_id ,cid,level ,name , schemeId ,inv));
        Page page = new Page(pageNum, totalCount);
        List<HashMap> hashMaps = clerkMapper.businessGoodsList(new AppParameter(business_id , binding_id ,cid ,level ,name ,schemeId ,inv,page.getStartIndex() ,page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //pp店铺类别下的产品（供应商）
    @Override
    public Page businessGoodsListSuppliers(String token, Integer role, Integer pageNo, Integer suppliersId,Integer categoryId, String name) throws Exception {
        int pageNum = 1;
        if(IntegerUtils.isNotEmpty(pageNo) ){
            pageNum=pageNo;
        }
        //通过员工登录token员工所在的商家ID
        Integer business_id =  shopMapper.shopipByTokenAndRole(token , role);

        byte shield = 0 ; // 1屏蔽进价
        byte inv = 0 ; // 1屏蔽库存

        if (role == 2 ){ //员工登录

            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap hash = purchaseMapper.selectShield(token);
            shield = Byte.parseByte(hash.get("shield").toString());
            inv = Byte.parseByte(hash.get("inv").toString());
        }
        Integer cid = null ,level = null;
        //判断是大类别id还是小类别id
        Integer le = clerkMapper.largeClassOrSmalClass(categoryId);
        if (le != null && le != 0 ){ //判断le如果等于null，就代表是大类下的产品
            cid = categoryId;

        }else {
            level = categoryId;
        }

        //app店铺类别下的产品总数（供应商）
        Integer totalCount = clerkMapper.businessGoodsListSuppliersCount(new AppParameter(business_id , suppliersId ,cid ,level ,name ,shield , inv));
        Page page = new Page(pageNum, totalCount);
        //app店铺类别下的产品（供应商）
        List<HashMap> hashMaps = clerkMapper.businessGoodsListSuppliers(new AppParameter(business_id , suppliersId ,cid ,level ,name ,shield , inv ,page.getStartIndex() ,page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //加入购物车(店铺APP和员工APP共用)
    @Override
    @Transactional
    public Integer addShoppingMall(String token , Integer role, Integer binding_id, Integer goods_id, Integer type ,Integer mold, Double price  ,Double num) throws Exception {

        Integer op_id = shopMapper.registerIdByTokenAndRole(token, role); //获取注册id


        //通过登录的注册id和绑定客户ID查询是否有商城购物车id
        Integer sm_id = clerkMapper.selectShoppingMall( role ,op_id , binding_id, mold); // sm_id购物车ID

        //判断购物车ID是否存在 ，等于null则添加购物车
        if (sm_id == null ){
            ShoppingMall shoppingMall = null;
            if (role == 1){
                shoppingMall = new ShoppingMall(op_id, binding_id, 0, mold, new Date());
            }else {
                shoppingMall = new ShoppingMall(op_id, binding_id, 1, mold, new Date());
            }
            //添加购物车
            clerkMapper.addShoppingMall(shoppingMall);
            sm_id = shoppingMall.getId();
        }

        //通过sm_id、goods_id、type和mold查询购物车详情里是存在
        Integer smd_id = clerkMapper.selectShoppingMallDateil(sm_id ,goods_id, type, mold); //购物车详情ID smd_id

        //判断购物车详情ID是否存在
        if (smd_id == null){
            ShoppingMallDateil shoppingMallDateil = new ShoppingMallDateil(sm_id, goods_id, price , num ,type ,mold ,new Date() );

            //添加购物车详情
            clerkMapper.addShoppingMallDateil(shoppingMallDateil);
            smd_id = shoppingMallDateil.getId();
        }else {

            //通过购物车详情ID修改加数量
            clerkMapper.updateShoppingMallDateilPlusNum(smd_id , num );
        }

        //计算加入购物车里产品的总金额（数量乘以单价）
        Double money = clerkMapper.selectShoppingMallTotalMoney(sm_id );
        //修改购物车里的总金额
        return clerkMapper.updateShoppingMallMoney(sm_id,money);
    }

    //减掉购物车
    @Override
    @Transactional
    public Integer reduceShoppingMall(Integer sm_id, Integer smd_id) throws NullPointerException, Exception {

        //通过购物车详情ID减掉购物车产品数量
        Integer count = clerkMapper.updateShoppingMallDateilNum(smd_id);
        if (count == null || count == 0){
            throw new NullPointerException("执行失败");
        }

        //通过购物车详情ID查询购物车产品数量
        Double num = clerkMapper.selectShoppingMallDateilNum(smd_id);
        //判断如果购物车里的数量等于0.0或小于0.0 ，则删除购物车详情
        if(num <=  0.0){
            //删除购物车详情ID
            clerkMapper.deleteShoppingMallDateil(smd_id);

            //通过购物车id查询购物车详情下是否还有产品
            List<Integer> isGoods = clerkMapper.selectShoppingMallDateilIsGoods(sm_id);
            if (isGoods.size() <= 0 ){
                //因购物车下没有产品则删除购物车
                return clerkMapper.deleteShoppingMall(sm_id);
            }
        }
        //计算加入购物车里产品的总金额（数量乘以单价）
        Double money = clerkMapper.selectShoppingMallTotalMoney(sm_id );
        //修改购物车里的总金额
        return clerkMapper.updateShoppingMallMoney(sm_id,money);
    }

    //删除购物车产品
    @Override
    @Transactional
    public Integer deleteShoppingMallDateil(Integer sm_id, Integer smd_id ) throws NullPointerException, Exception {

        //删除购物车详情ID
        clerkMapper.deleteShoppingMallDateil(smd_id);
        //通过购物车id查询购物车详情下是否还有产品
        List<Integer> isGoods = clerkMapper.selectShoppingMallDateilIsGoods(sm_id);
        if (isGoods.size() <= 0 ){
            //因购物车下没有产品则删除购物车
            return clerkMapper.deleteShoppingMall(sm_id);
        }
        //计算加入购物车里产品的总金额（数量乘以单价）
        Double money = clerkMapper.selectShoppingMallTotalMoney(sm_id );
        //修改购物车里的总金额
        return clerkMapper.updateShoppingMallMoney(sm_id,money);
    }

    //客户的购物车总数和总金额
    @Override
    @Transactional
    public HashMap currentBindingShoppingMallCountAndMoney(String token ,Integer role , Integer binding_id ,Integer mold)throws Exception {
        HashMap hashMap = new HashMap();

        Integer op_id = shopMapper.registerIdByTokenAndRole(token, role); //获取注册id

        //通过员工ID和客户绑定ID查询购物车信息
        HashMap hash = clerkMapper.selectShoppingMallInfo(role , op_id, binding_id, mold);

        Integer smId =null; //购物车id
        if (hash != null){
            smId = Integer.valueOf(hash.get("id").toString());
        }

        //通过购物车ID查询购物车详情总数
        Integer count = clerkMapper.selectShoppingMallDateCount(smId);
        //通过购物车ID查询购物车总金额
        ShoppingMall mall = clerkMapper.findShoppingMallTotalMoney(smId);
        hashMap.put("count",count); //订单总数
        if (mall == null){
            hashMap.put("sm_id" ,0);
            hashMap.put("money",0);
        }else {
            hashMap.put("sm_id" ,mall.getId());
            hashMap.put("money",new BigDecimal(mall.getMoney()).setScale(BigDecimal.ROUND_CEILING,2));
        }
        return hashMap;
    }

    //客户的购物车产品列表
    @Override
    public List<ShoppingMallDateil> currentBindingShoppingMallGoodsList(Integer sm_id) throws Exception {
        //通过购物车ID查询购物车详情产品列表
        return clerkMapper.currentBindingShoppingMallGoodsList(sm_id);
    }

    //结算
    @Override
    public ShoppingMall settlement(Integer sm_id) throws Exception {
        ShoppingMall mall = clerkMapper.findShoppingMallTotalMoney(sm_id);
        if (mall != null){
            List<ShoppingMallDateil> smd = clerkMapper.currentBindingShoppingMallGoodsList(sm_id);
            mall.setMallDateil(smd);
        }
        return mall;
    }

    //销售下订单
    @Override
    @Transactional
    public synchronized Integer addOrder(String token  ,Integer role, Integer sm_id ,double freight ,double difference_price , String beizhu ,Integer print, Integer mold) throws NullPointerException, Exception {

        int staff_id = 0 ;

        //APP点打印只能调用飞蛾打印
        List<Printer> printers = null;
        if (print != null && print == 1 ){
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);
            printers = menuMapper.queryPrinter(bid , 0);
            if (printers.size() <=  0 ){
                throw new NullPointerException("飞蛾打印机未启用或未添加,不能使用此模块打印 ");
            }
        }
        //通过购物车ID查询购物车总金额
        ShoppingMall SM = clerkMapper.findShoppingMallTotalMoney(sm_id);
        if(SM == null ){
            throw new NullPointerException("购物车不存在");
        }

        if (SM.getType() == 1){  //SM.getType() 0店铺APP购物车，1员工APP购物车
            staff_id = SM.getOp_id();
        }
        //购物车金额加运费+差价等于订单总金额
        double price = SM.getMoney() + freight + difference_price;

        //生成订单号
        String orderNumber = OrderCodeFactory.getOnlineOrderCode("5" ,(long) SM.getOp_id(), 5);
        Order order = new Order();
        order.setStaff_id(staff_id);
        order.setOrder_number(orderNumber); //订单号
        order.setNo(OrderCodeFactory.getStringRandom(3,3)); //销售的是6位二维码标识
        order.setBinding_id(SM.getConsume_id()); //客户ID
        order.setBeizhu(beizhu); //备注
        order.setFreight(freight); // 运费
        order.setDifference_price(difference_price); //差价
        order.setPrice(price); //订单总金额
        order.setStatus(3);
        order.setCreate_time(new Date());
        order.setPrint_frequ(print); //0未打印
        order.setMold(mold); //销售单
        HashMap staff = clerkMapper.staffNameAndBusiness_id(token);
        if (staff == null ){
            throw new NullPointerException("登录失效");
        }
        order.setMaking(staff.get("name").toString()); //制单人
        order.setBusiness_id(Integer.valueOf(staff.get("business_id").toString())); //店铺ID
        //添加订单
        orderMapper.addOrder(order);

        Give give = null;
        double profit = 0;
        OrderDetail orderDetail = new OrderDetail();
        //通过购物车ID查询购物车详情
        List<HashMap> mallDetail = clerkMapper.ShoppingMallGoodsListBySMID(sm_id );
        if (mallDetail.size() <= 0 ){
            throw new NullPointerException("没有商品不能下单哟～");
        }

        double costPrice = 0;
        for (HashMap mall: mallDetail ) {
            //根据type来判断是销售的还是报损的
            if (Integer.valueOf(mall.get("type").toString()) == 0){

                //根据产品id查询产品进价
                 costPrice = clerkMapper.findGoodsCostPrice(Integer.valueOf(mall.get("goods_id").toString()));
                //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润
                profit = (Double.valueOf(mall.get("price").toString()) - costPrice) * Double.valueOf(mall.get("num").toString());

            }else if (Integer.valueOf(mall.get("type").toString()) == 1){
                //报损产品
                give = new Give(Integer.valueOf(mall.get("goods_id").toString()),SM.getConsume_id(),Double.valueOf(mall.get("num").toString()),new Date(),SM.getOp_id() ,1);
                //添加报损记录
                inventoryMapper.addLossGoods(give);
            }

           if(mold != null && mold == 0 ){
               //销售先减少产品的虚拟库存，到货时根据订单id减少实际库存
               shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(mall.get("goods_id").toString()) , Double.valueOf(mall.get("num").toString()));

           }else if(mold != null && mold == 1){
               //销售退货先增加产品的虚拟库存，到货时根据订单id增加实际库存
               shopMapper.increaseGoodsFictitiousInventory(Integer.valueOf(mall.get("goods_id").toString()) , Double.valueOf(mall.get("num").toString()));

           }
            orderDetail.setGoods_id(Integer.valueOf(mall.get("goods_id").toString())); //产品ID
            orderDetail.setOrder_number(Double.valueOf(mall.get("num").toString())); //购买数量
            orderDetail.setOrder_price(Double.valueOf(mall.get("price").toString())); //购买单价
            orderDetail.setProfit(profit); //盈利
            orderDetail.setOrder_id(order.getId()); //订单ID
            orderDetail.setType(Integer.valueOf(mall.get("type").toString()));
            orderDetail.setCost_price(costPrice);
            //添加购物车里的产品详情
            orderMapper.addOrderDetail(orderDetail);

            //判断是否是添加报损单的订单详情
            if (Integer.valueOf(mall.get("type").toString()) == 1){
                //修改关联报损单的订单详情id
                clerkMapper.updateLoss(give.getId(),orderDetail.getId());
            }
        }
        //通过员工id查询是否有销售开单提成比例
        HashMap sp = distributeMapper.findStaffPercentage( staff_id, 8);
        if (sp != null ){
            if (sp.get("a") != null && sp.get("b") != null && !sp.get("a").equals("")   && !sp.get("b").equals("")){
                //添加员工的开单次数 type=7是开单
                orderMapper.addStaffFrequency(staff_id,order.getId(),7,price,new Date());
            }
        }
        //通过购物车ID删除购物车
        clerkMapper.deleteShoppingMall(sm_id);
        clerkMapper.deleteShoppingMallDateilBySmId(sm_id);


        if (print == 1 ){
            //通过订单id查询订单信息
            Order OP = menuMapper.queryOrder(order.getId());
            if (order == null ){
                throw new NullPointerException("订单不存在");
            }
            //通过订单id查询订单详情
            List<MothPrinterClass> mothPrinters = menuMapper.queryOrderDateil(order.getId());

            for (int i = 0 ; i < printers.size() ; i ++){
                for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                    MothPrinter.salePrint(printers.get(i).getSn1() ,mold == 0 ? "销售单":"销售退货单" ,OP.getBeizhu(), new BigDecimal(OP.getPrice().toString())
                            ,OP.getAddress(),OP.getPhone(),OP.getName(),OP.getOrder_number(),OP.getNo() ,OP.getFreight()
                            ,OP.getDifference_price(),OP.getReduce() ,mothPrinters);

                }
            }
        }


        return order.getId();
    }


    //客户收藏的产品
    @Override
    public Page bindingCollectionBindingGoodsList(String token, Integer role, Integer pageNo, Integer bindingId, String name) throws Exception {
        byte inv = 0 ; // 1屏蔽库存
        int pageNum = 1;
        if( IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer business_id = shopMapper.shopipByTokenAndRole(token ,role );

        if (role == 2 ){ //员工登录
            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap hash = purchaseMapper.selectShield(token);
            inv = Byte.parseByte(hash.get("inv").toString());
        }


        //通过绑定用户id查询方案id
        Integer schemeId = shopMapper.selectBindingSchemeId(bindingId);

        Integer totalCount = clerkMapper.bindingCollectionBindingGoodsListCount(business_id,bindingId ,schemeId ,name , inv );
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = clerkMapper.bindingCollectionBindingGoodsList(business_id , bindingId ,schemeId ,name , inv ,page.getStartIndex() ,page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //店铺收藏供应商的产品
    @Override
    public Page bindingCollectionSuppliersGoodsList(String token ,Integer role , Integer pageNo, Integer suppliersId, String name) throws Exception {

        int pageNum = 1;
        if(IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer business_id = shopMapper.shopipByTokenAndRole(token ,role );
        byte shield = 0 , inv = 0; // 1屏蔽进价 \ 1屏蔽库存

        if (role == 2 ){ //员工登录

            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap hash = purchaseMapper.selectShield(token);
            inv = Byte.parseByte(hash.get("inv").toString());
            shield = Byte.parseByte(hash.get("shield").toString());

        }


        Integer totalCount = clerkMapper.bindingCollectionSuppliersGoodsListCount(business_id,suppliersId ,name ,shield, inv  );
        Page page = new Page(pageNum,totalCount);
        List<HashMap> hashMaps = clerkMapper.bindingCollectionSuppliersGoodsList(business_id , suppliersId ,name ,  shield ,inv ,page.getStartIndex() ,page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //采购下订单
    @Override
    @Transactional
    public synchronized Integer addPurchase(String token ,Integer role , Integer sm_id, double freight, double difference_price, String beizhu, Integer print ,Integer mold)throws NullPointerException ,Exception {

        //APP点打印只能调用飞蛾打印
        List<Printer> printers = null;
        if (print != null && print == 1 ){
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);
            printers = menuMapper.queryPrinter(bid , 0);
            if (printers.size() <=  0 ){
                throw new NullPointerException("飞蛾打印机未启用或未添加,不能使用此模块打印");
            }
        }

        //通过购物车ID查询购物车总金额
        ShoppingMall SM = clerkMapper.findShoppingMallTotalMoney(sm_id);
        if(SM == null ){
            throw new NullPointerException("购物车不存在");
        }
        //购物车金额加运费+差价等于订单总金额
        double price = SM.getMoney() + freight + difference_price;
        int staff_id = 0;
        if (SM.getType() == 1){  //SM.getType() 0店铺APP购物车，1员工APP购物车
            staff_id = SM.getOp_id();
        }
        //通过员工登录token查询员工所在店铺ID和员工姓名
        HashMap staff = clerkMapper.staffNameAndBusiness_id(token);
        if (staff == null ){
            throw new NullPointerException("登录失效");
        }
        //生成订单号
        String orderNumber = OrderCodeFactory.getPurchaseOrder((long) SM.getOp_id(), 5);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSuppliers_id(SM.getConsume_id()); //供应商ID
        purchaseOrder.setOrder_number(orderNumber); //订单号
        purchaseOrder.setFreight(freight); // 运费
        purchaseOrder.setDifference_price(difference_price); //差价
        purchaseOrder.setNo(OrderCodeFactory.getStringRandom(3,3));
        purchaseOrder.setBeizhu(beizhu); //备注
        purchaseOrder.setPrice(price); //订单总金额
        purchaseOrder.setStatus(0);
        purchaseOrder.setCreate_time(new Date());
        purchaseOrder.setPrint_frequ(print); //0未打印
        purchaseOrder.setMold(mold); //销售单
        purchaseOrder.setStaff_id(staff_id);

        purchaseOrder.setMaking(staff.get("name").toString()); //制单人
        purchaseOrder.setBusiness_id(Integer.valueOf(staff.get("business_id").toString())); //店铺ID
        //添加采购订单
        purchaseMapper.addPurchaseOrder(purchaseOrder);

        Give give = new Give();
        double profit = 0;
        PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
        //通过购物车ID查询购物车详情
        List<HashMap> mallDetail = clerkMapper.ShoppingMallGoodsListBySMID(sm_id );
        if (mallDetail.size() <= 0 ){
            throw new NullPointerException("没有商品不能下单哟～");
        }
        for (HashMap mall: mallDetail ) {
            //根据type来判断是采购的还是赠送的
            if (Integer.valueOf(mall.get("type").toString()) == 0){

                //根据产品id查询产品进价
                double costPrice = shopMapper.findGoodsCostPrice(Integer.valueOf(mall.get("goods_id").toString()));
                //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润
                profit = (Double.valueOf(mall.get("price").toString()) - costPrice) * Double.valueOf(mall.get("num").toString());

            }else if (Integer.valueOf(mall.get("type").toString()) == 1){
                //采购时的赠送产品只添加数据，等到确认到货时在加库存
                give.setGoods_id(Integer.valueOf(mall.get("goods_id").toString()));//产品id
                give.setSuppliers_id(SM.getConsume_id());//供应商id
                give.setGive_time(new Date());
                give.setOperate_id(SM.getOp_id());//操作人
                give.setNum(Double.valueOf(mall.get("num").toString()));//赠送数量
                give.setStatus(1);//0默认在赠送单里赠送，1在采购单里赠送
                inventoryMapper.addGiveGoods(give);
            }
            if(mold != null && mold == 0 ){
                //采购入库开单，先增加产品的虚拟库存
                shopMapper.increaseGoodsFictitiousInventory(Integer.valueOf(mall.get("goods_id").toString()) , Double.valueOf(mall.get("num").toString()) );

            }else if(mold != null && mold == 1){
                //采购退货开单，先减少产品的虚拟库存
                shopMapper.reduceGoodsFictitiousInventory(Integer.valueOf(mall.get("goods_id").toString()) , Double.valueOf(mall.get("num").toString()) );

            }

            purchaseOrderDetail.setGoods_id(Integer.valueOf(mall.get("goods_id").toString())); //产品ID
            purchaseOrderDetail.setPurchase_number(Double.valueOf(mall.get("num").toString())); //采购数量
            purchaseOrderDetail.setPurchase_price(Double.valueOf(mall.get("price").toString())); //采购单价
            purchaseOrderDetail.setPurchase_id(purchaseOrder.getId()); //订单ID
            purchaseOrderDetail.setType(Integer.valueOf(mall.get("type").toString()));
            //添加购物车里的产品详情
            purchaseMapper.addPurchaseOrderDetail(purchaseOrderDetail);

            //判断是否是添加报损单的订单详情
            if (Integer.valueOf(mall.get("type").toString()) == 1){
                //通过赠送id，修改订单详情id
                orderMapper.updateGive(give.getId(),purchaseOrderDetail.getId());
            }
        }
        //通过购物车ID删除购物车
        clerkMapper.deleteShoppingMall(sm_id);
        clerkMapper.deleteShoppingMallDateilBySmId(sm_id);

        if (print == 1 ){

            //通过订单id查询订单信息
            PurchaseOrder PP = menuMapper.queryPurchaseOrder(purchaseOrder.getId());
            if (purchaseOrder == null ){
                throw new NullPointerException("订单不存在");
            }

            //通过订单id查询订单详情
            List<MothPrinterClass> mothPrinters = menuMapper.queryPurchaseOrderDateil(purchaseOrder.getId());

            for (int i = 0 ; i < printers.size() ; i ++){

                for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                    MothPrinter.puchasePrint(printers.get(i).getSn1() ,mold == 0? "采购单":"采购退货单" ,PP.getBeizhu(), new BigDecimal(PP.getPrice().toString())
                            ,PP.getAddress(),PP.getPhone(),PP.getName(),PP.getOrder_number(),PP.getNo()
                            ,PP.getFreight() ,PP.getDifference_price(),mothPrinters);

                }
            }
        }

        return purchaseOrder.getId();
    }

    //销售收款(默认进来数据不加载，通过扫一扫或搜索)
    @Override
    public Page saleReceivables(String token ,Integer role, Integer pageNo, String name  ,String number) throws Exception {

        int pageNum = 1 ;
        if( IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        Integer business_id = shopMapper.shopipByTokenAndRole(token ,role);

        Integer totalCount = clerkMapper.saleReceivablesCount(new Paramt( business_id,name ,number));
        Page page = new Page(pageNum ,totalCount) ;
        List<HashMap> hashMaps = clerkMapper.saleReceivables(new Paramt( business_id ,name ,number ,page.getStartIndex() ,page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //获取员工信息
    @Override
    public HashMap myInfo(String token  ,Integer role) throws Exception {
        return clerkMapper.myInfo(token );
    }

    //我的工资今日结算
    @Override
    public HashMap mySalary(String token, Integer role) throws Exception {

        HashMap hashMap = new HashMap();

        Integer staffId = shopMapper.findStaffIdByToken(token);
        //销售单总数
        Integer  count = clerkMapper.saleAndPurchaseOrderCount(staffId);
        //本月总提成
        Double tatol = clerkMapper.statisMoneyCount(staffId);
        hashMap.put("count" ,count);
        hashMap.put("tatol" , count);
        return hashMap;
    }


    //我的工资本月结算
    @Override
    public List<WageSettlement> wageSettlement(String token, Integer role  ) throws Exception {

        Integer staffId = shopMapper.findStaffIdByToken(token);
        String ym = DateUtil.getYM();

        List<WageSettlement> wageSettlements = clerkMapper.wageSettlement(staffId);
        Iterator<WageSettlement> it = wageSettlements.iterator();
        while (it.hasNext()){
            WageSettlement wage = it.next();
            if (wage.getMoney() != null ){
                if (ym.compareTo(wage.getTime()) > 0 ){
                    it.remove();
                }
            }
        }
        return wageSettlements;
    }

    //提成分类
    @Override
    public Page percentageClassification(String token, Integer role, Integer pageNo, Integer type, String startTime, String endTime) {

        Integer id = shopMapper.registerIdByTokenAndRole(token ,role);
        int pageNum = 1 ;
        if(pageNo != null && pageNo != 0 ){
            pageNum = pageNo;
        }
        Integer totalCount = clerkMapper.percentageClassificationCount(id , type , startTime , endTime );
        Page page = new Page(pageNum,totalCount );
        List<HashMap> hashMaps = clerkMapper.percentageClassification(id , type , startTime , endTime ,page.getStartIndex() ,page.getPageSize());
        Double statis = clerkMapper.percentageClassificationTotalMoney(id, type, startTime, endTime);
        page.setRecords(hashMaps);
        page.setPrice(new BigDecimal(statis).setScale(2,BigDecimal.ROUND_UP));
        return page;
    }

    //工作记录
    @Override
    public Page payrollRecords(String token, Integer role, Integer pageNo, String startTime, String endTime) {
        Integer business_id = shopMapper.shopipByTokenAndRole(token ,role);
        int pageNum = 1 ;
        if( IntegerUtils.isNotEmpty(pageNo) ){
            pageNum = pageNo;
        }

        return null;
    }

    //员工APP修改密码
    @Override
    @Transactional
    public Integer updatePassword(String token, Integer role, String password, String password2) throws NullPointerException ,Exception{
        //获取员工ID
        Integer staff_id = shopMapper.findStaffIdByToken(token);
        //通过员工id查询原来密码
        String pwd = staffMapper.findStaffPassword(staff_id);

        String pwd1 = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
        if (!pwd.equals(pwd1)){
           throw new NullPointerException("旧密码不匹配");
        }
        String pwd2 = PasswordMD5.EncoderByMd5(password2 + Global.passwordKey);
        return staffMapper.updateStaffPwd(staff_id ,pwd2 );
    }


    //客户账单
    @Override
    public Page shareUserOrder(String token, Integer role ,Integer pageNo, Integer type,Integer bindingID , String name, String startTime, String endTime) {
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);
        int pageNum = 1 ;
        if(IntegerUtils.isNotEmpty(pageNo) ){
            pageNum = pageNo;
        }
        Integer tatolCount = clerkMapper.shareUserOrderCount(new Paramt(bid , type , bindingID , name , startTime , endTime ));
        Page page = new Page(pageNum , tatolCount);
        List<HashMap> hashMaps = clerkMapper.shareUserOrder(new Paramt(bid , type , bindingID , name , startTime , endTime , page.getStartIndex() ,page.getPageSize()));
        HashMap hashMap = clerkMapper.shareUserOrderTotalMoeny(new Paramt(bid, type, bindingID, name, startTime, endTime));
        page.setRecords(hashMaps);
        page.setHashMap(hashMap);
        return page;
    }

    //客户账单分享到微信上
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String shareWXRecord(String token, Integer role ,Integer type, Integer bindingID, String name, String startTime, String endTime) throws NullPointerException{

        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);
        StringBuffer sb = new StringBuffer();

        List<Integer> shareWxRecord = clerkMapper.shareWXRecord(new Paramt(bid, type, bindingID, name, startTime, endTime));
        if (shareWxRecord.size() <= 0 ){
            throw new NullPointerException("没有要分享的订单");
        }
        for (Integer  shareId : shareWxRecord){
            sb.append(shareId + ",");
        }
        Double sharePrice = clerkMapper.userSaleDetailDownloadTatolMoney(new Paramt(bid, type, bindingID, name, startTime, endTime));
        System.out.println(sb.toString());
        //添加到微信分享记录表里
        String uuid = GroupNumber.getUUID();
        Integer count = clerkMapper.addWXShareRecord(uuid , bindingID , sb.toString() , sharePrice , startTime ,endTime );
        if (count == null || count == 0){
            throw new NullPointerException("分享失败");
        }
        return uuid;
    }




    //客户账单的微信分享
    @Override
    public HashMap userWXShareRecord(String shareID) {

        HashMap hashMap = clerkMapper.selectUserWXShareRecord(shareID);
        if (hashMap != null ){
            //客户账单的微信分享商品明细
            List<HashMap> wxShareDetail = clerkMapper.selectWXShareGoodDetail(shareID);
            hashMap.put("shareDetail" , wxShareDetail);
        }
        return hashMap;
    }


    //订单的微信分享
    @Override
    public HashMap orderWXShareRecord(Integer orderID) {

        HashMap hashMap = clerkMapper.orderWXShareRecord(orderID);
        if (hashMap != null ){
            List<HashMap> hashMaps = orderMapper.orderDetail(orderID);
            hashMap.put("orderDetail" ,hashMaps);
        }
        return hashMap;
    }
}
