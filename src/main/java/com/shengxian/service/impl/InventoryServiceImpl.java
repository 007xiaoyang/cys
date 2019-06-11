package com.shengxian.service.impl;

import com.shengxian.common.util.*;
import com.shengxian.entity.Give;
import com.shengxian.entity.Parameter;
import com.shengxian.entity.Settlement;
import com.shengxian.mapper.OrderMapper;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.InventoryMapper;
import com.shengxian.mapper.PurchaseMapper;
import com.shengxian.service.InventoryService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-10-14
 * @Version: 1.0
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private ShopMapper shopMapper;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private PurchaseMapper purchaseMapper;


    //添加仓库信息
    @Override
    @Transactional
    public Integer addInventory(String token ,Integer role, String name) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        return inventoryMapper.addInventory(bid,name);
    }

    //查询商家仓库信息
    @Override
    public List<HashMap> findInventoryList(String token ,Integer role)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        return inventoryMapper.findInventoryList(bid);
    }

    //根据仓库ID查询仓库信息
    @Override
    public HashMap findInventoryByWid(Integer warehouseId) {
        return inventoryMapper.findInventoryByWid(warehouseId);
    }

    //修改仓库信息
    @Override
    @Transactional
    public Integer updateInventory(Integer warehouseId, String name) {
        return inventoryMapper.updateInventory(warehouseId,name);
    }

    //删除仓库ID
    @Override
    @Transactional
    public Integer delectInventory(Integer warehouseId) throws NullPointerException ,Exception {

        //通过仓库ID查询库存表下是否存在产品
        List<HashMap> goodsId = inventoryMapper.findGoodsByWid(warehouseId);

        if (goodsId.size() > 0){
            throw new NullPointerException("该仓库中有产品存在，不能删除");
        }
        return  inventoryMapper.delectInventory(warehouseId);
    }

    //盘点
    @Override
    @Transactional
    public Integer check(String token ,Integer role , Integer status) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        return inventoryMapper.check(bid,status);
    }

    //查询商家是否盘点中
    @Override
    public Integer checkGoodsInventory(String token ,Integer role) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        return inventoryMapper.checkGoodsInventory(bid);
    }

    //查询盘点前的产品信息
    @Override
    public Page findBusinessGoodsInventory(String token ,Integer role , Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status ,Integer goods_id)throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo ;
        }
        //查询盘点前的产品信息总数
        Integer tatolCount =  inventoryMapper.findBusinessGoodsInventoryCount(new Parameter(bid ,category_id ,number ,name ,warehouse_id ,status ,goods_id));
        Page page = new Page(pageNum,tatolCount);

        //查询盘点前的产品信息
        List<HashMap> hashMaps =inventoryMapper.findBusinessGoodsInventory(new Parameter(bid ,page.getStartIndex() ,page.getPageSize() ,category_id ,number ,name ,warehouse_id ,status ,goods_id));
        for (HashMap hashMap:hashMaps){

            if (shield != null && Integer.valueOf(shield.get("inv").toString()) == 1){
                hashMap.put("inv","库存不可见");
            }else {
                hashMap.put("inv","库存可见");
            }

            if (shield != null && Integer.valueOf(shield.get("shield").toString()) == 1){
                hashMap.put("cost","进价不可见");
            }else {
                hashMap.put("cost","进价可见");
            }

            //判断当前时间大于结算时间，查询过期的未盘点的产品
            Integer goodsid = inventoryMapper.notOverdueInventory(Integer.valueOf(hashMap.get("id").toString()));
            if (goodsid != null){ //超出时间未结算了，修改状态
                inventoryMapper.updateGoodsState(goodsid,0,null);
            }
        }

        //仓库产品总金额
        double price = inventoryMapper.WGTatolMoney(new Parameter(bid ,category_id ,number ,name ,warehouse_id ,status,goods_id));

        page.setRecords(hashMaps);
        page.setPrice(new BigDecimal(price));
        return page;
    }

    //查询盘点过后的产品信息
    @Override
    public Page findSettlementInventory(String token ,Integer role , Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status ,Integer goods_id)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo ;
        }

        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        //查询盘点过后的产品信息总数
        Integer tatolCount =  inventoryMapper.findSettlementInventoryCount(new Parameter(bid ,category_id ,number ,name ,warehouse_id ,status ,goods_id));
        Page page = new Page(pageNum,tatolCount);

        //查询盘点过后的产品信息
        List<HashMap> hashMaps =inventoryMapper.findSettlementInventory(new Parameter(bid ,page.getStartIndex() ,page.getPageSize(),category_id ,number ,name ,warehouse_id ,status ,goods_id));

        for (HashMap hashMap: hashMaps ) {

            if (shield == null || Integer.valueOf(shield.get("shield").toString()) == 0){

                hashMap.put("cost","进价可见");
            }else {

                hashMap.put("cost","进价不可见");
            }

            if (shield == null || Integer.valueOf(shield.get("inv").toString()) == 0){

                hashMap.put("inv","库存可见");
            }else {

                hashMap.put("inv","库存不可见");
            }
        }

        //统计结算产品的总金额
        double totalSum = inventoryMapper.findSettlementInventoryTotalSum(new Parameter(bid ,category_id ,number ,name ,warehouse_id ,status ,goods_id));
        page.setRecords(hashMaps);
        page.setPrice(new BigDecimal(totalSum));
        return page;
    }

    //修改盘点库存
    @Override
    @Transactional
    public Integer updateCheckGoodsInventory(String token ,Integer inventory_id, Double actual, Double update_inventory)throws NullPointerException ,Exception {

        //通过库存id查询产品是否已经修改盘点数量了
        Double num = inventoryMapper.selectIsUpdateCheckNum(inventory_id);
        if (num != null){
            throw new NullPointerException("该产品已经有人在盘点中了");
        }

        //获取操作人id，商家账号默认0
        Integer opid = shopMapper.idByToken(token);

        if (actual.equals(update_inventory)){//正常库存

            return inventoryMapper.updateCheckGoodsInventory(inventory_id,update_inventory,2,opid);

        }else { //异常库存

            return inventoryMapper.updateCheckGoodsInventory(inventory_id,update_inventory,1,opid);
        }
    }

    //盘点回退
    @Override
    @Transactional
    public Integer retreatCheckGoodsInventory(Integer inventory_id)throws NullPointerException {

        //通过库存id查询产品是否已经修改盘点数量了
        Double num = inventoryMapper.selectIsUpdateCheckNum(inventory_id);
        if (num == null){
            throw new NullPointerException("该产品已经被回退了");
        }

        //修改盘点库存
        return inventoryMapper.updateCheckGoodsInventory(inventory_id,null,0,null);
    }

    //结算
    @Override
    @Transactional
    public Integer addSettlementInventory(String token ,Integer role ,String inventory_id)throws NullPointerException , Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        //获取操作人id，商家默认0
        Integer opid = shopMapper.idByToken(token);

        //添加盘点结算记录
        Settlement settlement = new Settlement(bid, new Date(),opid);

        //添加结算记录
        Integer count  = inventoryMapper.addSettlement(settlement);

        //查询存在结算的产品
        List<Integer> inventoryId = inventoryMapper.selectExistenceSettlementGoods(bid );//库存id

        for (Integer id : inventoryId ) {

            //通过库存id查询否已经有存在结算的产品了
            Double num = inventoryMapper.selectIsUpdateCheckNum(Integer.valueOf(id));
            if (num == null){
                throw new NullPointerException("产品已被结算过了"); //该产品已经有人在盘点中了
            }
            //通过库存id查询盘点的库存数量
            HashMap hashMap = inventoryMapper.selectInventory(id);
            if (hashMap == null){
                throw new Exception();
            }
            //添加盘点结算产品详情
            count= inventoryMapper.addSettlementGoodsDetail(settlement.getId(),Integer.valueOf(hashMap.get("goods_id").toString()),

            Double.valueOf(hashMap.get("actual").toString()),Double.valueOf(hashMap.get("num").toString()),new Date(),Integer.valueOf(hashMap.get("opid").toString()));

            //修改产品库存信息
            count = inventoryMapper.updateRetreatCheckGoodsInventory(Integer.valueOf(id),0,Double.valueOf(hashMap.get("num").toString()),null,null);

            //修改产品盘点状态
            inventoryMapper.updateGoodsState(Integer.valueOf(hashMap.get("goods_id").toString()),1,new Date());
        }
        return count;
    }

    //结算记录
    @Override
    public Page goodsSettlement(String token ,Integer role, Integer pageNo , String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        //结算记录总数
        Integer tatolCount = inventoryMapper.goodsSettlementCount(bid,startTime,endTime);
        Page page = new Page(pageNum,tatolCount);
        List<HashMap> hashMaps = inventoryMapper.goodsSettlement(bid,startTime,endTime,page.getStartIndex(),page.getPageSize());
        page.setRecords(hashMaps);
        return page;
    }

    //结算记录产品详情
    @Override
    public Page settlementGoodsRecord(String token , Integer pageNo, Integer settlement_id, String name, String number, Integer category_id, Integer warehouse_id, Integer  opid) {
        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
        HashMap shield = purchaseMapper.selectShield(token);

        //结算产品详情总数
        Integer totalCount =  inventoryMapper.settlementGoodsRecordCount(settlement_id,name,number,category_id,warehouse_id,opid);
        Page page = new Page(pageNum,totalCount,20);

        //结算产品详情
        List<HashMap> hashMaps =inventoryMapper.settlementGoodsRecord(settlement_id,name,number,category_id,warehouse_id ,opid,page.getStartIndex(),page.getPageSize());
        for (HashMap hashMap: hashMaps ) {

            if (shield == null || Integer.valueOf(shield.get("shield").toString()) == 0){
                hashMap.put("cost","进价可见");
            }else {
                hashMap.put("cost","进价不可见");
            }
            if (shield == null || Integer.valueOf(shield.get("inv").toString()) == 0){
                hashMap.put("inv","库存可见");
            }else {
                hashMap.put("inv","库存不可见");
            }
        }
        //结算后的产品记录总金额
        double totalSum = inventoryMapper.settlementGoodsRecordTotalSum(settlement_id,name,number,category_id,warehouse_id,opid);
        page.setRecords(hashMaps);
        page.setPrice(new BigDecimal(totalSum));
        return page;
    }


    //库存报表（每个仓库的详情信息）
    @Override
    public Page businessGoodsInventoryInfo(String token ,Integer role, Integer pageNo , Integer warehouse_id) throws NullPointerException, Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }

        //库存报表（每个仓库的详情信息)总数
        Integer tatolCount =  inventoryMapper.businessGoodsInventoryInfoCount(bid ,warehouse_id);
        Page page = new Page(pageNum,tatolCount);

        //库存报表（每个仓库的详情信息）
        List<HashMap> hashMaps =inventoryMapper.businessGoodsInventoryInfo(bid ,warehouse_id ,page.getStartIndex() ,page.getPageSize() );
        page.setRecords(hashMaps);
        double price = inventoryMapper.businessGoodsInventoryTotalMoney(bid ,warehouse_id );
        page.setPrice(new BigDecimal(price));
        return page;
    }

    //条件搜索店铺供应商
    @Override
    public List<HashMap> findBusinessSuppliers(String token ,Integer role , String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        //条件搜索商家的供应商
        return inventoryMapper.findBusinessSuppliers(bid,name);
    }

    //添加赠送产品
    @Override
    @Transactional
    public Integer addGiveGoods(String token ,Integer role , Give[] give) {

        Integer count = null;

        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //店铺登录默认0
        int operate_id = 0;

        if (role == 2 ){
            operate_id = shopMapper.findStaffIdByToken(token);
        }

        for (Give g:give ) {
            g.setOperate_id(operate_id);
            g.setGive_time(new Date());

            //添加赠送产品
            inventoryMapper.addGiveGoods(g);

            //赠送产品时，添加库存
            count = shopMapper.increaseGoodsInventory(g.getGoods_id(),g.getNum());

            //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
            Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate(g.getGoods_id());
            if (statisId == null ){
                //通过产品id查询每天产品的初始库存
                Double goodsInventory = shopMapper.selectGoodsInventory(g.getGoods_id() );

                //添加采购产品是否在每天产品库存统计记录(采购）
                shopMapper.addPurchaseGoodsInventoryStatis( bid , g.getGoods_id() ,new BigDecimal(goodsInventory) ,new BigDecimal( g.getNum() ) , new Date()  );
            }else {

                //增加采购产品是否在每天产品库存统计记录（采购）
                shopMapper.increasePurchaseGoodsInventoryStatis(statisId , new BigDecimal( g.getNum() ));
            }


        }
        return count;
    }

    //添加报损产品
    @Override
    @Transactional
    public Integer addLossGoods(String token  ,Integer role , Give[] give)throws NullPointerException ,Exception {

        Integer count = null;

        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //店铺登录默认0
        int operate_id = 0;

        if (role == 2 ){
            operate_id = shopMapper.findStaffIdByToken(token);
        }

        for (Give g:give ) {

            g.setOperate_id(operate_id);
            g.setLoss_time(new Date());

           //通过产品id查询产品实际库存，判断实际库存是否大于报损数量
            HashMap hashMap = shopMapper.findGoodsInventory(g.getGoods_id());
            if (Double.valueOf(hashMap.get("actual").toString()) < g.getNum() ){

                //通过产品id查询产品名称
                String goodsName = inventoryMapper.findGoodsName(g.getGoods_id());

                throw new NullPointerException("产品名称为："+goodsName+",实际库存低，不能报损");
            }

           //添加报损产品
            inventoryMapper.addLossGoods(g);

            //报损产品时，减少库存
            count = shopMapper.reduceGoodsInventory(g.getGoods_id(),g.getNum());

            //通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
            Integer statisId = shopMapper.selectGoodsInventoryStatisByBidAndDate(g.getGoods_id());
            if (statisId == null ){
                //通过产品id查询每天产品的初始库存
                Double goodsInventory = shopMapper.selectGoodsInventory(g.getGoods_id());

                //当前这件销售产品没有产品库存统计记录 则添加
                shopMapper.addSaleGoodsInventoryStatis( bid ,g.getGoods_id() , new BigDecimal(goodsInventory) ,new BigDecimal( g.getNum())  , new Date()  );
            }else {

                //增加销售产品是否在每天产品库存统计记录
                shopMapper.increaseSaleGoodsInventoryStatis(statisId , new BigDecimal( g.getNum()) );
            }
        }
        return count;
    }

    //分页查询赠送产品单
    @Override
    public Page findGiveGoods(String token ,Integer role , Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        double price = 0;

        //查询赠送产品单总数
        Integer tatolCount = inventoryMapper.findGiveGoodsCount(bid,warehouse_id,name,startTime,endTime);
        Page page = new Page(pageNum,tatolCount);

        //查询赠送产品单
        List<HashMap> hashMaps = inventoryMapper.findGiveGoods(bid,warehouse_id,name,startTime,endTime,page.getStartIndex(),page.getPageSize());

        for (HashMap hash : hashMaps) {

            price += Double.valueOf(hash.get("cost_price").toString());
        }
        page.setPrice(new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP));
        page.setRecords(hashMaps);
        return page;
    }

    //分页查询报损产品单
    @Override
    public Page findLossGoods(String token  ,Integer role, Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum = pageNo;
        }
        //查询报损产品单总数
        Integer tatolCount = inventoryMapper.findLossGoodsCount(bid,warehouse_id,name,startTime,endTime);
        Page page = new Page(pageNum,tatolCount);

        //查询报损产品单
        List<HashMap> hashMaps = inventoryMapper.findLossGoods(bid,warehouse_id,name,startTime,endTime,page.getStartIndex(),page.getPageSize());
        double price = 0;

        for (HashMap hashMap : hashMaps) {
            double cost_price = Double.valueOf(hashMap.get("cost_price").toString());
            price += cost_price;
        }
        page.setPrice(new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP));
        page.setRecords(hashMaps);
        return page;
    }


    //导出仓库产品详情
    @Override
    public HSSFWorkbook excelDownload(String token ,Integer role ,Integer id) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token ,role);

        String seetName = "仓库产品详情";//sheet名
        String[] title =new String[]{"类别", "编号", "品名","进货单价（元）","单位","仓库","实际库存","虚拟库存","金额"};//标题
        List<HashMap> hashMaps = inventoryMapper.excelDownload( bid ,id);
        Parameter parameter = new Parameter();
        parameter.setBusiness_id(bid);
        parameter.setWarehouse_id(id);
        double price = inventoryMapper.WGTatolMoney(parameter);
        String[][] values = new String[hashMaps.size()+1][];
        for (int i =0;i<= hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            if (hashMaps.size() == i){
                values[i][0] = "合计：";
                values[i][1] = String.valueOf(price);
            }else {
                HashMap hashMap = hashMaps.get(i);
                values[i][0] = hashMap.get("categoryName").toString();
                values[i][1] = hashMap.get("number").toString();
                values[i][2] = hashMap.get("name").toString();
                values[i][3] = hashMap.get("cost_price").toString();
                values[i][4] = hashMap.get("units").toString();
                values[i][5] = hashMap.get("wname").toString();
                values[i][6] = hashMap.get("actual").toString();
                values[i][7] = hashMap.get("fictitious").toString();
                values[i][8] = hashMap.get("price").toString();
            }
        }

        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //赠送产品导出
    @Override
    public HSSFWorkbook giveGoodslDownload(String token, Integer warehouse_id, String name, String startTime, String endTime) {
        Integer bid = null;
        bid = shopMapper.findIdByBusinessToken(token);
        if (bid == null ){
            bid = shopMapper.findIdByStaffToken(token);
        }
        String seetName = "赠送单详情";//sheet名
        String[] title =new String[]{"供应商", "品名", "进货单价（元）","单位","仓库","操作人","赠送数量","赠送时间"};//标题
        List<HashMap> hashMaps = inventoryMapper.giveGoodslDownload(bid,warehouse_id,name,startTime,endTime);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("suppliers_name").toString();
            values[i][1] = hashMap.get("name").toString();
            values[i][2] = hashMap.get("cost_price").toString();
            values[i][3] = hashMap.get("units").toString();
            values[i][4] = hashMap.get("wname").toString();
            values[i][5] = hashMap.get("operate_name").toString();
            values[i][6] = hashMap.get("num").toString();
            values[i][7] = hashMap.get("give_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //报损产品导出
    @Override
    public HSSFWorkbook lossGoodslDownload(String token, Integer warehouse_id, String name, String startTime, String endTime) {
        Integer bid = null;
        bid = shopMapper.findIdByBusinessToken(token);
        if (bid == null ){
            bid = shopMapper.findIdByStaffToken(token);
        }
        String seetName = "报损单详情";//sheet名
        String[] title =new String[]{"客户", "品名", "进货单价（元）","单位","仓库","操作人","报损数量","报损时间"};//标题
        List<HashMap> hashMaps = inventoryMapper.lossGoodslDownload(bid,warehouse_id,name,startTime,endTime);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("bind_name").toString();
            values[i][1] = hashMap.get("name").toString();
            values[i][2] = hashMap.get("cost_price").toString();
            values[i][3] = hashMap.get("units").toString();
            values[i][4] = hashMap.get("wname").toString();
            values[i][5] = hashMap.get("operate_name").toString();
            values[i][6] = hashMap.get("num").toString();
            values[i][7] = hashMap.get("loss_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //库存归零
    @Override
    @Transactional
    public Integer inventoryZeroing(String token ,Integer role , String password) throws NullPointerException, Exception {

        String pwd = shopMapper.businessPassword(token ,role);
        if (pwd == null){
            throw new NullPointerException("登录失效");
        }
        String pass = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
        if (!pwd.equals(pass)){
            throw  new NullPointerException("密码不正确");
        }

        //通过token和role查询店铺ID
        Integer business_id = shopMapper.shopipByTokenAndRole(token, role);

        //库存归零
        return inventoryMapper.inventoryZeroing(business_id);
    }
}
