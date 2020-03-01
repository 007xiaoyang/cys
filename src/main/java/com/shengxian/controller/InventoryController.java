package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.*;
import com.shengxian.entity.Exp;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.service.ExcelService;
import com.shengxian.service.InventoryService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 库存
 *
 * @Author: yang
 * @Date: 2018-10-14
 * @Version: 1.0
 */
@Api(description = "库存管理")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private static Logger log = Logger.getLogger(InventoryController.class);
    @Resource
    private InventoryService inventoryService;

    @Resource
    private ExcelService excelService;

    @Autowired
    private ShopMapper shopMapper;

    /**
         * 添加仓库信息
         * @param token
         * @param name
         * @return
         */
        @RequestMapping("/addInventory")
        @SysLog(module = "库存管理",methods = "添加仓库")
        @ApiOperation(value = "添加仓库信息" ,httpMethod = "POST")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
                @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
                @ApiImplicitParam(name = "name" ,value = "仓库名称" ,paramType = "query")
        })
        public Message addInventory(String token ,Integer role , String name){
            Message message = Message.non();
            if (StringUtils.isEmpty(name)){
                return message.code(Message.codeFailured).message("请输入仓库名称");
            }
        try {
            Integer count = inventoryService.addInventory(token ,role, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加仓库失败");
            }
            return message.code(Message.codeSuccessed).message("添加仓库成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/addInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询商家仓库信息
     * @param token
     * @return
     */
    @RequestMapping("/findInventoryList")
    @ApiOperation(value = "查询商家仓库信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
    })
    public Message findInventoryList(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = inventoryService.findInventoryList(token ,role );
            return message.code(Message.codeSuccessed).data(hashMaps).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }


    /**
     * 根据仓库ID查询仓库信息
     * @param token
     * @param warehouseId 仓库ID
     * @return
     */
    @RequestMapping("/findInventoryByWid")
    @ApiOperation(value = "根据仓库ID查询仓库信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouseId" ,value = "仓库id" ,paramType = "query")
    })
    public Message findInventoryByWid(String token ,Integer role  , Integer warehouseId){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(warehouseId)) {
                return message.code(Message.codeFailured).message("请输入仓库ID");
            }
            HashMap hashMap = inventoryService.findInventoryByWid(warehouseId);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/findInventoryByWid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改仓库信息
     * @param token
     * @param warehouseId
     * @param name
     * @return
     */
    @RequestMapping("/updateInventory")
    @SysLog(module = "库存管理",methods = "修改仓库信息")
    @ApiOperation(value = "修改仓库信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouseId" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "仓库名称" ,paramType = "query")
    })
    public Message updateInventory(String token ,Integer role , Integer warehouseId, String name){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(warehouseId)){
            return message.code(Message.codeFailured).message("请输入仓库ID");
        }
        if (StringUtils.isEmpty(name) ){
            return message.code(Message.codeFailured).message("请输入仓库名称");
        }
        try {
            Integer count = inventoryService.updateInventory(warehouseId, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/updateInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 删除仓库
     * @param token
     * @param warehouseId
     * @return
     */
    @RequestMapping("/delectInventory")
    @SysLog(module = "库存管理",methods = "删除仓库")
    @ApiOperation(value = "删除仓库" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouseId" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "仓库名称" ,paramType = "query")
    })
    public Message delectInventory(String token ,Integer role , Integer warehouseId){
        Message message = Message.non();

        if ( IntegerUtils.isEmpty(warehouseId)){
            return message.code(Message.codeFailured).message("请输入仓库ID");
        }
        try {
            Integer count = inventoryService.delectInventory(warehouseId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/delectInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 盘点
     * @param token
     * @param status 状态，0默认不盘点，1正在盘点中
     * @return
     */
    @RequestMapping("/check")
    @SysLog(module = "库存管理",methods = "盘点")
    @ApiOperation(value = "盘点" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "状态，0默认不盘点，1正在盘点中" ,paramType = "query")
    })
    public Message check(String token ,Integer role , Integer status){
        Message message = Message.non();
        if (status == null){
            return message.code(Message.codeFailured).message("请输入盘点状态");
        }
        try {
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);
            Integer count = inventoryService.check(bid ,status);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("盘点失败");
            }
            return message.code(Message.codeSuccessed).message("盘点进行中");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/check）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询商家是否盘点中
     * @param token
     * @return
     */
    @RequestMapping("/checkGoodsInventory")
    @ApiOperation(value = "查询商家是否盘点中" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message checkGoodsInventory(String token ,Integer role ){
        Message message = Message.non();
        try {
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);
            Integer count = inventoryService.checkGoodsInventory(bid);
            return message.code(Message.codeSuccessed).data(count).message("操作成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/checkGoodsInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询盘点前的产品信息
     * @param token
     * @param
     * @return
     */
    @RequestMapping("/findBusinessGoodsInventory")
    @ApiOperation(value = "查询盘点前的产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "（前库存列表时此字段不传）0查询和修改，1异常，2正常" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query")
    })
    public Message findBusinessGoodsInventory(String token , Integer role , Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status, Integer goods_id ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = inventoryService.findBusinessGoodsInventory(token ,role ,pageNo,names ,number ,category_id ,warehouse_id ,status,goods_id  );
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/findBusinessGoodsInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }
    /**
     *查询盘点过后的产品信息
     * @param token
     * @param
     * @return
     */
    @RequestMapping("/findSettlementInventory")
    @ApiOperation(value = "查询盘点过后的产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "（前库存列表时此字段不传）0查询和修改，1异常，2正常" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query")
    })
    public Message findSettlementInventory(String token ,Integer role , Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status, Integer goods_id ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = inventoryService.findSettlementInventory(token ,role , pageNo ,names ,number ,category_id ,warehouse_id ,status,goods_id);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/findSettlementInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 修改盘点库存
     * @param token
     * @param inventory_id
     * @return
     */
    @RequestMapping("/updateCheckGoodsInventory")
    @SysLog(module = "库存管理",methods = "修改盘点库存")
    @ApiOperation(value = "修改盘点库存" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "inventory_id" ,value = "库存id" ,paramType = "query"),
            @ApiImplicitParam(name = "actual" ,value = "原来实际库存" ,paramType = "query"),
            @ApiImplicitParam(name = "update_inventory" ,value = "修改的库存" ,paramType = "query")
    })
    public Message updateCheckGoodsInventory(String token ,Integer role , Integer inventory_id, Double actual, Double update_inventory){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(inventory_id)){
            return message.code(Message.codeFailured).message("请输入库存id");
        }
        try {
            Integer count = inventoryService.updateCheckGoodsInventory(token, inventory_id, actual, update_inventory);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改盘点库存失败");
            }
            return message.code(Message.codeSuccessed).message("修改盘点库存成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/updateCheckGoodsInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 盘点回退
     * @param token
     * @param inventory_id
     * @return
     */
    @RequestMapping("/retreatCheckGoodsInventory")
    @SysLog(module = "库存管理",methods = "盘点回退")
    @ApiOperation(value = "盘点回退" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "inventory_id" ,value = "库存id" ,paramType = "query")
    })
    public Message retreatCheckGoodsInventory(String token ,Integer role , Integer inventory_id){
        Message message = Message.non();
        try {
            if ( IntegerUtils.isEmpty(inventory_id)){
                return message.code(Message.codeFailured).message("请输入库存id");
            }
            Integer count = inventoryService.retreatCheckGoodsInventory(inventory_id);

            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("回退失败");
            }
            return message.code(Message.codeSuccessed).message("回退成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/retreatCheckGoodsInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 结算
     * @param token
     * @param inventory_id
     * @return
     */
    @RequestMapping("/settlementInventory")
    @SysLog(module = "库存管理",methods = "库存结算")
    @ApiOperation(value = "结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "inventory_id" ,value = "库存id" ,paramType = "query")
    })
    public Message settlementInventory(String token ,Integer role , String inventory_id){
        Message message = Message.non();
        try {
            Integer count = inventoryService.addSettlementInventory(token ,role , inventory_id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("结算失败");
            }
            return message.code(Message.codeSuccessed).message("结算成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/settlementInventory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 结算记录
     * @param token
     * @return
     */
    @RequestMapping("/goodsSettlement")
    @ApiOperation(value = "结算记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message goodsSettlement(String token ,Integer role , Integer pageNo, String startTime, String endTime){
        Message message = Message.non();
        try {
            Page page = inventoryService.goodsSettlement(token ,role ,pageNo,startTime,endTime);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/goodsSettlement）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 结算的产品详情
     * @param token
     * @return
     */
    @RequestMapping("/settlementGoodsRecord")
    @ApiOperation(value = "结算的产品详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "settlement_id" ,value = "结算id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "产品编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "opid" ,value = "操作人" ,paramType = "query")
    })
    public Message settlementGoodsRecord(String token, Integer pageNo, Integer settlement_id, String name, String number, Integer category_id, Integer warehouse_id, Integer opid ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = inventoryService.settlementGoodsRecord(token, pageNo, settlement_id, names, number, category_id, warehouse_id, opid);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/settlementGoodsRecord）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 仓库设置（每个仓库所有产品的详情信息）
     * @param token
     * @param warehouse_id
     * @return
     */
    @RequestMapping("/businessGoodsInventoryInfo")
    @ApiOperation(value = "仓库设置（每个仓库的详情信息）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query")
    })
    public Message businessGoodsInventoryInfo(String token ,Integer role , Integer pageNo, Integer warehouse_id){
        Message message = Message.non();
        try {
            Page page = inventoryService.businessGoodsInventoryInfo(token , role ,pageNo , warehouse_id);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/businessGoodsInventoryInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);

        }
    }


    /**
     * 条件查询商家的供应商
     * @param token
     * @param name 条件搜索，可以名称或编号
     * @return
     */
    @RequestMapping("/findBusinessSuppliers")
    @ApiOperation(value = "条件查询商家的供应商" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称或编号" ,paramType = "query")
    })
    public Message findBusinessSuppliers(String token ,Integer role , String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMaps = inventoryService.findBusinessSuppliers(token ,role ,names);
            return message.code(Message.codeSuccessed).data(hashMaps).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }


    /**
     * 添加赠送产品
     * @param token
     * @return
     */
    @RequestMapping("/addGiveGoods")
    @SysLog(module = "库存管理" , methods = "赠送产品")
    @ApiOperation(value = "添加赠送产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addGiveGoods(String token ,Integer role , String json){
        Message message = Message.non();
        try {
            Exp exp = JSONObject.parseObject(json, Exp.class);

            Integer count = inventoryService.addGiveGoods(token, role, exp.getGives());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("赠送产品失败");
            }
            return message.code(Message.codeSuccessed).message("赠送产品成功");
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/addGiveGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加报损产品
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addLossGoods")
    @SysLog(module = "库存管理" , methods = "添加报损产品")
    @ApiOperation(value = "添加报损产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addLossGoods(String token ,Integer role , String json){
        Message message = Message.non();
        Exp exp = JSONObject.parseObject(json, Exp.class);
        try {
            Integer count = inventoryService.addLossGoods(token, role, exp.getGives());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("报损失败");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("报损的数量大于产品库存");
            }
            return message.code(Message.codeSuccessed).message("报损成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/addLossGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询赠送单
     * @param token
     * @return
     */
    @RequestMapping("/findGiveGoods")
    @ApiOperation(value = "查询赠送单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message findGiveGoods(String token ,Integer role , Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
         try {
             String start = DateUtil.getDay(),  end = DateUtil.getDay();
             if ( IntegerUtils.isEmpty(startTime ,endTime)){
                 //当没有传开始时间和结束时间，默认查询当天的订单
                 start = startTime; end = endTime;
             }

             Page page = inventoryService.findGiveGoods(token ,role , pageNo, warehouse_id, names, start, end);
             return message.code(Message.codeSuccessed).data(page).message("查询成功");
         }catch (NullPointerException e){
             return message.code(Message.codeFailured).message(e.getMessage());
         }catch (Exception e){
             log.error("库存管理控制层（/inventory/findGiveGoods）接口报错---------"+e.getMessage());
             return message.code(Message.codeFailured).message(Global.ERROR);
         }
    }

    /**
     *查询报损单
     * @param token
     * @return
     */
    @RequestMapping("/findLossGoods")
    @ApiOperation(value = "查询赠送单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message findLossGoods(String token ,Integer role , Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            String start = DateUtil.getDay(),  end = DateUtil.getDay();
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = inventoryService.findLossGoods(token ,role ,pageNo ,warehouse_id ,names ,start ,end);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/findLossGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 导出仓库产品详情
     * @param token
     * @param
     */
    @RequestMapping("/excelDownload")
    @ApiOperation(value = "导出仓库产品详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "仓库id" ,paramType = "query")
    })
    public void InventoryDownload(String token ,Integer role , HttpServletResponse response, Integer id){
        Message message = Message.non();
        HSSFWorkbook workbook = inventoryService.excelDownload(token ,role , id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"仓库产品详情"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 赠送产品导出
     * @param token
     * @param
     */
    @RequestMapping("/giveGoodslDownload")
    @ApiOperation(value = "赠送产品导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public void giveGoodslDownload(String token, Integer warehouse_id,String name,String startTime,String endTime , HttpServletResponse response){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        HSSFWorkbook workbook = inventoryService.giveGoodslDownload(token,warehouse_id,names,startTime,endTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"赠送产品详情"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 报损产品导出
     * @param token
     * @param
     */
    @RequestMapping("/lossGoodslDownload")
    @ApiOperation(value = "报损产品导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "warehouse_id" ,value = "仓库id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public void lossGoodslDownload(String token, Integer warehouse_id,String name,String startTime,String endTime , HttpServletResponse response){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        HSSFWorkbook workbook = inventoryService.lossGoodslDownload(token,warehouse_id,names,startTime,endTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"报损产品详情"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 库存归零
     * @param token
     * @param password
     * @return
     */
    @RequestMapping("/inventoryZeroing")
    @SysLog(module = "库存管理" , methods = "库存归零")
    @ApiOperation(value = "库存归零" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "店铺密码" ,paramType = "query")
    })
    public Message inventoryZeroing(String token ,Integer role , String password){
        Message message = Message.non();
        if (StringUtils.isEmpty(password)){
            return message.code(Message.codeFailured).message("密码不能为空");
        }
        try {
            Integer count = inventoryService.inventoryZeroing(token ,role ,password);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("库存归零失败");
            }
            return message.code(Message.codeSuccessed).message("归零成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("库存管理控制层（/inventory/inventoryZeroing）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }




}
