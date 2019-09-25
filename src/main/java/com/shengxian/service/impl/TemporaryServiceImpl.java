package com.shengxian.service.impl;

import com.shengxian.common.util.GroupNumber;
import com.shengxian.common.util.OrderCodeFactory;
import com.shengxian.common.util.Page;
import com.shengxian.entity.*;
import com.shengxian.mapper.*;
import com.shengxian.service.TemporaryService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Date: 2019-05-11
 * @Version: 1.0
 */
@Service
public class TemporaryServiceImpl  implements TemporaryService {


    @Resource
    private TemporaryMapper temporaryMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ClerkMapper clerkMapper;


    //查询店铺类别
    @Override
    public List<GoodsCategory> businessCategory(Integer business_id, Integer level, Integer icode)throws NullPointerException {

        HashMap business = temporaryMapper.selectBusinessIcode(business_id);
        if (business == null ){
            throw new NullPointerException("店铺不存在");
        }else if (business.get("icode") == null || !Integer.valueOf(business.get("icode").toString()).equals(icode) ){
            throw new NullPointerException("您输入的产品二维码验证码不正确");
        }else if (Integer.valueOf(business.get("is_disable").toString()) == 1){
            throw new NullPointerException("抱歉，店铺已被禁用了，不能购买了");
        }else if (business.get("duration") == null || Integer.valueOf(business.get("duration").toString()) <= -1){
            throw new NullPointerException("抱歉，店铺使用权限过期了，不能购买了");
        }
        return temporaryMapper.businessCategory(business_id,level);
    }

    //店铺类别下的产品
    @Override
    public Page businessGoods( Integer pageNo, Integer business_id, Integer category_id, String name) throws Exception {
        int pageNum = 1;
        if(pageNo != null && pageNo != 0){
            pageNum=pageNo;
        }
        Integer cid = null;
        Integer level = null;
        //判断是大类别id还是小类别id
        Integer le = temporaryMapper.largeClassOrSmalClass(category_id);
        if (le == null || le == 0){ //判断le如果等于null，就代表是大类下的产品
            level = category_id;
        }else {
            cid = category_id;
        }
        Integer totalCount = temporaryMapper.businessGoodsCount(new Search(business_id,cid,name,level));
        Page page = new Page(pageNum,totalCount);
        List<IntegrGoods> hashMaps = temporaryMapper.businessGoods(new Search(business_id,cid,name,level,page.getStartIndex(),page.getPageSize()));
        page.setRecords(hashMaps);
        return page;
    }

    //加入购物车
    @Override
    @Transactional
    public Integer addShoppingCart(String tic , Integer goods_id, Double num ,Integer type) throws NullPointerException ,Exception {


        //通过识别码查询临时客户购物车id
        Integer tscId = temporaryMapper.selectTShoppongcart(tic); //scid 购物车id

        //没有购物车
        if (tscId == null){
            //添加购物车
            Shoppongcart shoppongcart = new Shoppongcart(tic,new Date());
            temporaryMapper.addTShoppongcart(shoppongcart);
            tscId = shoppongcart.getId();
        }
        //通过购物车id和产品id查询临时客户购物车详情里是否该产品
        Integer tscdId = temporaryMapper.selectTShoppingcartDetailIsGoodsId(tscId , goods_id); //临时客户购物车详情id
        if (tscdId != null){

            //通过购物车详情id修改购买数量
            temporaryMapper.updateTShoppingcartDetailNum(tscdId,num ,type);

        }else {
            //通过产品id查询临时售价

            Double temporaryPrice = temporaryMapper.selectTemporaryPriceByGoodsId(goods_id);

            //添加购物车详情的产品数量
            ShoppongcartDetail shoppongcartDetail = new ShoppongcartDetail(tscId ,goods_id ,num , temporaryPrice ,new Date());
            temporaryMapper.addTShoppingcartDetailNum(shoppongcartDetail);
            tscdId = shoppongcartDetail.getId();
        }

        //计算店铺加入购物车的总金额
        Double money = temporaryMapper.calculationTScAllGoodsTotalMoney( tscId);
        //修改购物车总金额
        return temporaryMapper.updateTShoppingcartTotalMoney(tscId, money);
    }

    //减掉购物车
    @Override
    @Transactional
    public Integer reduceShoppingCart( Integer tscdId) throws NullPointerException, Exception {


        //通过购物车详情ID减数量
        Integer count = temporaryMapper.reduceTShoppingcartDetailGoodsNum(tscdId);
        if (count == null || count == 0){
            throw new NullPointerException("执行失败");
        }
        //通过临时客户购物车详情ID查询原来的产品数量
        HashMap tscd = temporaryMapper.selectTShoppingcartDetailGoodsNum(tscdId);
        if (tscd == null ){
            throw new NullPointerException("购物车详情产品不存在");
        }
        Integer tscId = Integer.valueOf(tscd.get("tsc_id").toString());

        //判断如果购物车里的数量等于0.0或小于0.0 ，则删除购物车详情
        if (Double.valueOf(tscd.get("num").toString()) <= 0.0){

            //删除临时客户购物车详情id
            temporaryMapper.deleteTShoppingcartDetailId(tscdId);

            //通过购物车id查询临时客户购物车下是否还有产品
            List<Integer> shoppingcartIsGoods = temporaryMapper.findTShoppingcartIsGoods(tscId);
            if (shoppingcartIsGoods.size() <= 0){
                //因购物车下没有产品则删除购物车
                return  temporaryMapper.deleteTShoppingcart(tscId);
            }
        }
        //计算店铺加入购物车的总金额
        Double money = temporaryMapper.calculationTScAllGoodsTotalMoney( tscId);
        //修改购物车总金额
        return temporaryMapper.updateTShoppingcartTotalMoney(tscId , money);
    }

    //获取购物车金额和总数
    @Override
    public Integer temporaryShoppingcartMoneyAndCount( String tic) throws  Exception {
        //通过临时客户识别码查询购物车id
        Integer tscId = temporaryMapper.selectTShoppingcart(tic);
        return  temporaryMapper.selectTShoppingcartGoodsDetailGoodsCount(tscId);
    }


    //当前临时客户的购物车
    @Override
    @Transactional
    public  HashMap temporaryShoppingcart(Integer business_id ,String tic)throws NullPointerException ,Exception {

        HashMap hash = new HashMap();
        //通过店铺id查询店铺名称
        String storeName = temporaryMapper.findStartingPriceAndStoreName(business_id);
        if (storeName == null || storeName.equals("") ){
            throw new NullPointerException("店铺信息不存在");
        }

        //通过临时客户识别码查询购物车id
        Integer tscId = temporaryMapper.selectTShoppingcart(tic);
        hash.put("tscId" , tscId);
        //通过购物车id查询临时客户购物车下是否还有产品
        List<Integer> shoppingcartIsGoods = temporaryMapper.findTShoppingcartIsGoods(tscId );
        if (shoppingcartIsGoods.size() <= 0){
            //因购物车下没有产品则删除购物车
            temporaryMapper.deleteTShoppingcart(tscId);
        }
        //查询购物车下所有产品的总金额
        Double tatolMoney = temporaryMapper.selectShoppingcartMoney( tscId , tic );
        hash.put("money" , new BigDecimal(tatolMoney).setScale(2,BigDecimal.ROUND_CEILING));

        //通过临时客户购物车id查询购物车下的产品信息
        List<HashMap> hashMaps = temporaryMapper.selectTShoppingcartGoodsDetail( tscId  ,business_id);
        hash.put("goodsDateil" , hashMaps);



        return hash;
    }


    //删除购物车产品
    @Override
    @Transactional
    public Integer deleteShoppingcartDateil(Integer tscId , Integer tscdId) {
        //删除临时客户购物车详情id
        temporaryMapper.deleteTShoppingcartDetailId(tscdId);

        //通过购物车id查询临时客户购物车下是否还有产品
        List<Integer> shoppingcartIsGoods = temporaryMapper.findTShoppingcartIsGoods(tscId);
        if (shoppingcartIsGoods.size() <= 0){
            //因购物车下没有产品则删除购物车
            return  temporaryMapper.deleteTShoppingcart(tscId);
        }
        //计算店铺加入购物车的总金额
        Double money = temporaryMapper.calculationTScAllGoodsTotalMoney( tscId);
        //修改购物车总金额
        return temporaryMapper.updateTShoppingcartTotalMoney(tscId , money);
    }

    //结算
    @Override
    public HashMap settlement(String tic, String tscdId, Integer business_id) throws NullPointerException, Exception {

        HashMap hashMap = new HashMap();
        List<HashMap> list = new ArrayList<HashMap>();
        double tatolMoney = 0; //统计总金额
        String[] tscdid = tscdId.split(",");
        for (String id: tscdid  ) {

            //结算
            HashMap detail = temporaryMapper.settlement( Integer.valueOf(id)  ,business_id);

            tatolMoney += Double.valueOf(detail.get("num").toString()) * Double.valueOf(detail.get("price").toString());

            list.add(detail);
        }
        hashMap.put("detail",list);
        hashMap.put("money",tatolMoney);
        return hashMap;
    }

    //下订单
    @Override
    @Transactional
    public Integer addOrder(String tic , Integer business_id ,Integer tscId) throws NullPointerException, Exception {

        //通过店铺id查询店铺是否有扫码客户账号名称
        Integer biding_id = temporaryMapper.selectScanBinding(business_id);
        if (biding_id == null ){ //
            //扫码客户账号没注册，帮忙注册

            //通过店铺id查询店铺第一个类别id
            Integer categoryId =  userMapper.findOneCategoryId(business_id);
            if (categoryId == null ){
                throw new NullPointerException("店铺没添加客户类别，不能下单");
            }
            //根据用户类别id查询最后一条用户编号
            String num = userMapper.findNumberByCategoryId(categoryId);
            if (num == null){
                num = userMapper.findCodeById(categoryId) + "001";
            }else {
                num = GroupNumber.getNumber(Integer.parseInt(num));
            }
            BindUser bindUser = new BindUser();
            bindUser.setBusiness_id(business_id);//设值商家ID
            bindUser.setCategory_id(categoryId); //类别id
            bindUser.setNumber(num);
            bindUser.setScheme_id(16);
            bindUser.setMarket_integral("/");
            bindUser.setUnderline_integral("/");
            bindUser.setSource(1);
            bindUser.setUser_name("扫码客户");
            bindUser.setBinding_time(new Date());//绑定时间
            userMapper.addBinding(bindUser);
            biding_id = bindUser.getId();
            //添加用户积分
            userMapper.addBindingIntegra(bindUser.getId(),new Date());
        }

        //通过购物车id查询购物车金额
        Double price = temporaryMapper.selectShoppingcartMoney(tscId, tic);

        Order order = new Order();

        order.setOrder_number(OrderCodeFactory.getOnlineOrderCode("6" , (long)business_id ,5));//订单号
        order.setNo(OrderCodeFactory.getStringRandom(3,3));//标识码
        order.setMaking("扫码客户"); //制单人
        order.setBusiness_id(business_id); //店铺id
        order.setBinding_id(biding_id);  //绑定用户id
        order.setStaff_id(0);
        order.setStatus(2);
        order.setPrice(price);
        order.setCreate_time(new Date());
        orderMapper.addOrder(order);


        //通过购物车id查询购物车产品详情
        List<HashMap> hashs = temporaryMapper.selectTShoppingcartGoodsDetail(tscId, business_id);
        double costPrice = 0;
        double money = 0 ;
        double profit = 0;
        OrderDetail orderDetail = new OrderDetail();
        for (HashMap hash: hashs ) {
            //根据产品id查询产品进价
            costPrice = clerkMapper.findGoodsCostPrice(Integer.valueOf(hash.get("goodsId").toString()));
            Integer goodsId = Integer.valueOf(hash.get("goodsId").toString());
            Double num = Double.valueOf(hash.get("num").toString());

            //计算每销售一件产品的纯盈利 //用销售价格减去产品进价乘以数量等于纯盈利润
            profit = (Double.valueOf(hash.get("price").toString()) - costPrice) * num ;
            money = Double.valueOf(hash.get("price").toString());
            //销售先减少产品的虚拟库存，到货时根据订单id减少实际库存
            shopMapper.reduceGoodsFictitiousInventory(goodsId , num );



            orderDetail.setGoods_id(goodsId); //产品ID
            orderDetail.setOrder_number(num); //购买数量
            orderDetail.setOrder_price(money); //购买单价
            orderDetail.setProfit(profit); //盈利
            orderDetail.setOrder_id(order.getId()); //订单ID
            orderDetail.setCost_price(costPrice);
            //添加购物车里的产品详情
            orderMapper.addOrderDetail(orderDetail);

            //删除购物车详情
            temporaryMapper.deleteTShoppingcartDetailId(Integer.valueOf(hash.get("id").toString()));

        }

        temporaryMapper.deleteTShoppingcart(tscId);


        return order.getId();
    }

}
