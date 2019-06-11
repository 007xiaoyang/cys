package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.WebSocketUtil;
import com.shengxian.common.util.*;
import com.shengxian.entity.PurchaseOrder;
import com.shengxian.entity.PurchaseOrderDetail;
import com.shengxian.entity.Template;
import com.shengxian.service.ExcelService;
import com.shengxian.service.MenuService;
import com.shengxian.service.PurchaseService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 采购controller层
 *
 * @Author: yang
 * @Date: 2018/9/18
 * @Version: 1.0
 */
@Api(description = "采购管理")
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private static Logger log = Logger.getLogger(PurchaseController.class);
    @Resource
    private PurchaseService purchaseService;
    @Resource
    private MenuService menuService;

    @Resource
    private ExcelService excelService;

    /**
     * 生产采购订单编号
     * @param token
     * @return
     */
    @RequestMapping("/createPurchaseOrder")
    @ApiOperation(value = "生产采购订单编号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message createPurchaseOrder(String token ,Integer role ){
        Message message = Message.non();
       try {
           String order = purchaseService.createPurchaseOrder(token ,role );
           return message.code(Message.codeSuccessed).data(order).message("获取成功");
       }catch (NullPointerException e){
           return message.code(Message.codeFailured).message(e.getMessage());
       }
    }

    /**
     * 通过名称或编号或条码搜索商家产品（采购和销售）
     * @param token
     * @param name 名称或编号
     * @return
     */
    @RequestMapping("/selectBusinessGoods")
    @ApiOperation(value = "通过名称或编号或条码搜索商家产品（采购和销售）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称或编号" ,paramType = "query")
    })
    public Message selectBusinessGoods(String token ,Integer role, String name){
        Message message = Message.non();
       try {
           List<HashMap> hashMaps = purchaseService.selectBusinessGoods(token ,role ,name);
           return message.code(Message.codeSuccessed).data(hashMaps).message("搜索成功");
       }catch (NullPointerException e){
           return message.code(Message.codeFailured).message(e.getMessage());
       }
    }


    /**
     * 查询商家供应商产品收藏
     * @param token
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/findBusinessGoodsCollection")
    @ApiOperation(value = "查询商家供应商产品收藏" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliers_id" ,value = "供应商id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message findBusinessGoodsCollection(String token ,Integer role, Integer suppliers_id, String name , String startTime, String endTime){
        Message message = Message.non();
       try {
           List<HashMap> hashMap= purchaseService.findBusinessGoodsCollection(token ,role ,suppliers_id,name,startTime,endTime);
           return message.code(Message.codeSuccessed).data(hashMap).message("搜索成功");
       }catch (NullPointerException e){
           return message.code(Message.codeFailured).message(e.getMessage());
       }catch (Exception e){
           log.error("采购控制层（/purchase/findBusinessGoodsCollection）接口报错---------"+e.getMessage());
           return message.code(Message.codeFailured).message(Global.ERROR);
       }
    }

    /**
     * 添加商家收藏的产品
     * @param token
     * @param goods_id
     * @return
     */
    @RequestMapping("/addBusinessGoodsCollection")
    @SysLog(module = "采购管理",methods = "添加收藏产品")
    @ApiOperation(value = "添加商家收藏的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliers_id" ,value = "供应商id" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query")
    })
    public Message addBusinessGoodsCollection(String token ,Integer role, Integer goods_id, Integer suppliers_id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("请输入要收藏的产品id");
        }
        try {
            Integer count = purchaseService.addBusinessGoodsCollection(token ,role , goods_id,suppliers_id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("收藏失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("收藏成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/addBusinessGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 取消商家收藏的产品
     * @param token
     * @param id 收藏id
     * @return
     */
    @RequestMapping("/deleteBusinessGoodsCollection")
    @SysLog(module = "采购管理",methods = "取消产品收藏")
    @ApiOperation(value = "取消商家收藏的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "收藏id" ,paramType = "query")
    })
    public Message deleteBusinessGoodsCollection(String token ,Integer role, String id){
        Message message = Message.non();
        if (StringUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入收藏id");
        }
        try {
            Integer count = purchaseService.deleteBusinessGoodsCollection(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("取消失败");
            }
            return message.code(Message.codeSuccessed).message("取消成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/deleteBusinessGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过产品id搜索产品（采购）
     * @param goods_id 产品id
     * @param suppliers_id 供应商id
     * @return
     */
    @RequestMapping("/selectBusinessGoodsById")
    @ApiOperation(value = "通过产品id搜索产品（采购）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliers_id" ,value = "供应商id" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query")
    })
    public Message selectBusinessGoodsById(String token ,Integer role, String goods_id , Integer suppliers_id){
        Message message = Message.non();
        if (StringUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("请输入产品id");
        }
        try {
            List<HashMap> hashMaps = purchaseService.selectBusinessGoodsById(token, goods_id, suppliers_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/selectBusinessGoodsById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询商家采购的订单
     * @param token
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/selectPurchaseOrder")
    @ApiOperation(value = "查询商家采购的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliers_id" ,value = "供应商id" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message selectPurchaseOrder(String token ,Integer role, Integer suppliers_id, String startTime, String endTime){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = purchaseService.selectPurchaseOrder(token ,role , suppliers_id, startTime, endTime);
            return message.code(Message.codeSuccessed).data(hashMaps).message("搜索成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/selectPurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过采购订单id查询订单详情(返回集合)
     * @param
     * @return
     */
    @RequestMapping("/selectPurchaseOrderDetailById")
    @ApiOperation(value = "通过采购订单id查询订单详情（返回集合)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id" ,value = "订单id（可以多个订单id，）" ,paramType = "query")
    })
    public Message selectPurchaseOrderDetailById(String order_id){
        Message message = Message.non();
        if (StringUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入采购订单id");
        }
        try {
            List<HashMap> hashMaps = purchaseService.selectPurchaseOrderDetailById(order_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/selectPurchaseOrderDetailById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加采购产品单
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addPurchaseOrder")
    @SysLog(module = "采购管理",methods = "添加采购产品")
    @ApiOperation(value = "添加采购产品单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addPurchaseOrder(String token ,Integer role, String json){
        Message message = Message.non();
        PurchaseOrder purchaseOrder = JSONObject.parseObject(json, PurchaseOrder.class);
        try {
            Integer count = purchaseService.addPurchaseOrder(token ,role ,purchaseOrder);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("采购失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("采购成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/addPurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购退货产品单
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addPurchaseReturnOrder")
    @SysLog(module = "采购管理",methods = "采购退货单")
    @ApiOperation(value = "添加采购退货产品单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addPurchaseReturnOrder(String token ,Integer role, String json){
        Message message = Message.non();
        try {
            PurchaseOrder purchaseOrder = JSONObject.parseObject(json, PurchaseOrder.class);

            Integer count = purchaseService.addPurchaseReturnOrder(token ,role ,purchaseOrder);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("采购失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("采购成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/addPurchaseReturnOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 代采购报表
     * @param token
     * @return
     */
    @RequestMapping("/Purchasereport")
    @ApiOperation(value = "代采购报表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "仓库id" ,paramType = "query")
    })
    public Message Purchasereport(String token ,Integer role, Integer pageNo, Integer id){
        Message message = Message.non();
        try {
            Page page = purchaseService.Purchasereport(token ,role , pageNo, id);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/Purchasereport）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 待审核总数
     * @param token
     * @return
     */
    @RequestMapping("/stayAuditedCount")
    @ApiOperation(value = "待审核总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "" ,paramType = "query")
    })
    public Message stayAuditedCount(String token ,Integer role, Integer  status, Integer state){
        Message message = Message.non();
        try {
            Integer count = purchaseService.stayAuditedCount(token ,role ,status,state);
            return message.code(Message.codeSuccessed).data(count).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/stayAuditedCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 待审核(采购未到货的订单)
     * @param token
     * @return
     */
    @RequestMapping("/stayAudited")
    @ApiOperation(value = "待审核(采购未到货的订单)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称,订单号，标识码" ,paramType = "query")
    })
    public Message stayAudited(String token ,Integer role, Integer  pageNo, String name){
        Message message = Message.non();
        try {
            Page page = purchaseService.stayAudited(token ,role ,pageNo,name);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/stayAudited）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购确认
     * @param token
     * @param purchase_id 订单id
     * @param mold 0采购单，1采购退货单
     * @return
     */
    @RequestMapping("/updatePurchaseConfirmArrival")
    @SysLog(module = "采购管理",methods = "审核采购订单")
    @ApiOperation(value = "审核采购订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "purchase_id" ,value = "采购订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0采购单，1采购退货单" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "0默认待审核，1采购到货,2取消" ,paramType = "query")
    })
    public Message updatePurchaseConfirmArrival(String token ,Integer role, Integer purchase_id, Integer mold, Integer status){
        Message message = Message.non();
        try {
            Integer count = purchaseService.updatePurchaseConfirmArrival(token ,role ,purchase_id,mold,status);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("采购确认失败");
            }
            return message.code(Message.codeSuccessed).message("采购确认成功");
        }catch ( IndexOutOfBoundsException e){
            return message.code(Message.codeFailured).message("员工采购提成金额未填写，请去员工资料里去完善！");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/updatePurchaseConfirmArrival）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 未到货的订单直接收款
     * @param token
     * @param id 订单id
     * @param state 0未付款，1申请欠款审核，2欠款，3已完成
     * @return
     */
    @RequestMapping("/updateOrderReceivables")
    @SysLog(module = "采购管理" ,methods = "未审核的采购订单直接收款")
    @ApiOperation(value = "未审核的采购订单直接收款（暂时无用）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款审核，2欠款，3已完成" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1微信，2支付宝，3现金，4银行卡，5其它（state传0，type则不传）" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "收款金额（state传0，money则不传）" ,paramType = "query")
    })
    public   Message updateOrderReceivables(String token ,Integer role, Integer id , Integer state, Integer mold , Integer type, Double money){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = purchaseService.updateOrderReceivables(token ,role , id, state,  mold, type , money );
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderReceivables）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 未审核的采购订单取消
     * @param token
     * @param id 订单id
     * @return
     */
    @RequestMapping("/updateOrderCancel")
    @SysLog(module = "采购管理" ,methods = "未审核的采购订单取消")
    @ApiOperation(value = "未审核的采购订单取消" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query")
    })
    public  Message updateOrderCancel(String token ,Integer role, Integer id ,  Integer mold ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = purchaseService.updateOrderCancel(token ,role , id ,  mold );
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderCancel）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 采购未付款订单
     * @param token
     * @return
     */
    @RequestMapping("/purchaseUnpaidOrder")
    @ApiOperation(value = "采购未付款订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称标识码" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message purchaseUnpaidOrder(String token ,Integer role, Integer pageNo, String name ,String number ,String staffName){
        Message message = Message.non();

        try {
            Page page = purchaseService.purchaseUnpaidOrder(token ,role ,pageNo ,name,number , staffName);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/purchaseUnpaidOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购欠款订单
     * @param token
     * @return
     */
    @RequestMapping("/purchaseArrearsOrder")
    @ApiOperation(value = "采购欠款订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称标识码" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message purchaseArrearsOrder(String token ,Integer role, Integer pageNo, String name ,String number ,String staffName){
        Message message = Message.non();

        try {
            Page page = purchaseService.purchaseArrearsOrder(token ,role ,pageNo ,name ,number , staffName);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/purchaseArrearsOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询采购订已完成单
     * @param token
     * @return
     */
    @RequestMapping("/findPurchaseOrder")
    @ApiOperation(value = "查询采购订已完成单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称标识码" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message findPurchaseOrder(String token ,Integer role, Integer pageNo,String name ,String number , String startTime, String endTime ,String staffName){
        Message message = Message.non();

        String start = DateUtil.getDay(),   end = DateUtil.getDay();
        if ( IntegerUtils.isEmpty(startTime ,endTime )){
            //当没有传开始时间和结束时间，默认查询当天的订单
            start = startTime; end = endTime;
        }
        try {
            Page page = purchaseService.findPurchaseOrder(token ,role ,pageNo ,name ,number ,start,end ,staffName);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/findPurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 确认收款和确认欠款
     * @param token
     * @param id 采购订单id
     * @param state 0未付款，1欠款，2已完成
     * @param type 支付类型，1微信，2支付宝，3现金，4银行卡，5其它
     * @param money 实收款
     * @return
     */
    @RequestMapping("/updatePurchaseOrderState")
    @SysLog(module = "采购管理",methods = "确认采购收款或欠款")
    @ApiOperation(value = "确认采购收款或欠款" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "采购订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1欠款，2已完成的" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "支付类型，1微信，2支付宝，3现金，4银行卡，5其它" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "实收款" ,paramType = "query")
    })
    public Message updatePurchaseOrderState(String token ,Integer role, Integer id, Integer state, Integer type, double money ){
        Message message = Message.non();
        if (state == null){
           return message.code(Message.codeFailured).message("请输入状态");
        }
        try {
            Integer count = purchaseService.updatePurchaseOrderState(token ,role,id,state,type,money);
            if (count == null || count == 0){
                return message.code(Message.codeFailured).message("操作失败");
            }else if (count == -1){
                return message.code(Message.codeFailured).message("您的账号登录失效或在另一台设备登录");
            }
            return message.code(Message.codeSuccessed).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/updatePurchaseOrderState）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 订单详情
     * @param id 订单id
     * @return
     */
    @RequestMapping("/findPurchaseOrderDetail")
    @ApiOperation(value = "订单详情(返回差价和运费)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "采购订单id" ,paramType = "query")
    })
    public Message findPurchaseOrderDetail(Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入采购订单id");
        }
        try {
            HashMap hashMap =  purchaseService.findPurchaseOrderDetail(id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/findPurchaseOrderDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }


    /**
     * 删除订单详情产品
     * @param id 详情id
     * @return
     */
    @RequestMapping("/deletePurchaseDetail")
    @SysLog(module = "采购管理",methods = "删除订单详情产品")
    @ApiOperation(value = "删除订单详情产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "采购订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0采购，1退货" ,paramType = "query")
    })
    public Message deletePurchaseDetail(String token ,Integer role, Integer id ,Integer mold){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入采购订单id");
        }
        try {
            Integer count =  purchaseService.deletePurchaseDetail(id ,mold);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("删除失败");
            }else if (count == -1){
                return message.code(Message.codeFailured).message("订单详情id不存在");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/deletePurchaseDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 修改采购订单金额
     * @param
     * @return
     */
    @RequestMapping("/updatePurchaseOrderPrice")
    @SysLog(module = "采购管理",methods = "修改采购订单产品")
    @ApiOperation(value = "修改采购订单金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public synchronized Message updatePurchaseOrderPrice(String token ,Integer role, String json){
        Message message = Message.non();
        PurchaseOrder purchaseOrder = JSONObject.parseObject(json, PurchaseOrder.class);
        try {
            Integer count = purchaseService.updatePurchaseOrderPrice(token ,role , purchaseOrder);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改订单总金额失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/updatePurchaseOrderPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购修改产品进价
     * @param token
     * @param goods_id 产品id
     * @param menu_id 菜单id
     * @return
     */
    @RequestMapping("/updateGoodsCostPrice")
    @SysLog(module = "采购管理",methods = "修改产品进价")
    @ApiOperation(value = "采购修改产品进价" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "menu_id" ,value = "菜单id" ,paramType = "query")
    })
    public Message updateGoodsCostPrice(String token ,Integer role, Integer goods_id, Integer menu_id){
        Message message = Message.non();
        try {
            //修改进价之前，查询当前登录账号是否有权限
            boolean power = menuService.isPower(token,menu_id);
            if ( power == false){
                return message.code(Message.codeFailured).message("您没有权限修改产品进价");
            }
            if (goods_id == null || goods_id == 0){
                return message.code(Message.codeFailured).message("请输入产品id");
            }
      /*  Integer count =    purchaseService.updateGoodsCostPrice(goods_id);
        if (count == null || count == 0){
            return message.code(Message.codeFailured).message("修改进价失败");
        }*/
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/updateGoodsCostPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 搜索商家供应商
     * @param token
     * @return
     */
    @RequestMapping("/findBusinessSupplies")
    @ApiOperation(value = "搜索商家供应商" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称" ,paramType = "query")
    })
    public Message findBusinessSupplies(String token ,Integer role, String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = purchaseService.findBusinessSupplies(token ,role , name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("搜索成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 打印模板
     * @param token
     * @param type
     * @return
     */
    @RequestMapping("/printTemplate")
    @ApiOperation(value = "打印模板" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3临时销售订单，4退货销售订单" ,paramType = "query")
    })
    public Message printTemplate(String token ,Integer role, Integer type){
        Message message = Message.non();
        try {
            Template template = purchaseService.printTemplate(token ,role ,type);
            return message.code(Message.codeSuccessed).data(template).message("获取成功");
        }catch (NullPointerException e){
            log.error("采购控制层（/purchase/printTemplate）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 打印采购订单
     * @param token
     * @param order_id
     * @return
     */
    @RequestMapping("/purchasePrint")
    @ApiOperation(value = "打印采购订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "采购订单id" ,paramType = "query")
    })
    public Message purchasePrint(String token ,Integer role, Integer order_id){
        Message message = Message.non();
        try {
            PurchaseOrder purchaseOrder = purchaseService.purchasePrint(token ,role , order_id);
            return message.code(Message.codeSuccessed).data(purchaseOrder).message("获取成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/purchasePrint）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 打印采购订单详情
     * @param
     * @param order_id
     * @return
     */
    @RequestMapping("/purchasePrintDetail")
    @ApiOperation(value = "打印采购订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "采购订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "pageSize" ,value = "每页总数" ,paramType = "query")
    })
    public Message purchasePrintDetail(Integer pageNo , Integer order_id ,Integer pageSize){
        Message message = Message.non();
        if ( order_id == null || order_id == 0){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            List<PurchaseOrderDetail> purchaseOrderDetail = purchaseService.purchasePrintDetail(pageNo, order_id ,pageSize);
            return message.code(Message.codeSuccessed).data(purchaseOrderDetail).message("获取成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/purchasePrintDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改订单打印的次数
     * @param token
     * @param order_id
     * @return
     */
    @RequestMapping("/updatePrintFrequ")
    @SysLog(module = "采购管理",methods = "打印订单")
    @ApiOperation(value = "修改订单打印的次数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "采购订单id" ,paramType = "query")
    })
    public Message updatePrintFrequ(String token ,Integer role, Integer order_id){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入采购订单id");
        }
        try {
            Integer count = purchaseService.updatePrintFrequ(order_id);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("打印失败");
            }
            return message.code(Message.codeSuccessed).message("打印成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/updatePrintFrequ）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 所有采购单据
     * @param token
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/allPurchaseOrder")
    @ApiOperation(value = "修改订单打印的次数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "" ,paramType = "query")
    })
    public Message allPurchaseOrder(String token ,Integer role, Integer pageNo, String name, String number, String startTime, String endTime, Integer mold){
        Message message = Message.non();
        try {
            String start = startTime , end = endTime;

            start = DateUtil.getDay();  end = DateUtil.getDay();
            if (startTime != null && !"".equals(startTime) && endTime != null && !"".equals(endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }

            Page page = purchaseService.allPurchaseOrder(token ,role ,pageNo,name,number,start ,end,mold);
            return message.code(Message.codeSuccessed).data(page).message("搜索成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/allPurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这一年总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/yearPurchassPirce")
    @ApiOperation(value = "计算这一年总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap yearPurchassPirce(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.yearPurchassPirce(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/yearPurchassPirce）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这季度总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/quarterPurchassPrice")
    @ApiOperation(value = "计算这季度总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap quarterPurchassPrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.quarterPurchassPrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/quarterPurchassPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这月总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/monthPurchassPrice")
    @ApiOperation(value = "计算这月总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap monthPurchassPrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.monthPurchassPrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/monthPurchassPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这周总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/weekPurchassPrice")
    @ApiOperation(value = "计算这周总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap weekPurchassPrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.weekPurchassPrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/weekPurchassPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算当天总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/daysPurchassPrice")
    @ApiOperation(value = "计算当天总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap daysPurchassPrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.daysPurchassPrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/daysPurchassPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 自定义时间段总采购金额
     * @param token
     * @return
     */
    @RequestMapping("/definitionPurchassPrice")
    @ApiOperation(value = "自定义时间段总采购金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "token" ,paramType = "query")
    })
    public HashMap definitionPurchassPrice(String token ,Integer role,String startTime,String endTime){
        Message message = Message.non();
        try {
            HashMap hashMap = purchaseService.definitionPurchassPrice(token , role ,startTime,endTime);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/definitionPurchassPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 导出待采购订单信息
     * @param token
     * @param id 仓库id
     * @return
     */
    @RequestMapping("/excelPurchasereport")
    @ApiOperation(value = "导出待采购订单信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "仓库id" ,paramType = "query")
    })
    public void excelPurchasereport(String token ,Integer role, Integer id , HttpServletResponse response){
        Message message = Message.non();
        HSSFWorkbook workbook =purchaseService.excelPurchasereport(token ,role ,id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"待采购订单"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }


    /**
     * 修改产品进价
     * @param token
     * @return
     */
    @RequestMapping("/updateGoodsPrice")
    @SysLog(module = "采购管理",methods = "修改产品进价")
    @ApiOperation(value = "导出待采购订单信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "cost_price" ,value = "产品进价" ,paramType = "query")
    })
    public Message updateGoodsPrice(String token ,Integer role, Integer goods_id, Double cost_price){
        Message message = Message.non();
        try {
            Integer count = purchaseService.updateGoodsPrice(goods_id,cost_price);
            if (count == null || count == 0){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("采购控制层（/purchase/updateGoodsPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }
    /**
     * 采购取消订单总数
     * @param token
     * @return
     */
    @RequestMapping("/cancelOrderCount")
    @ApiOperation(value = "采购取消订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message cancelOrderCount(String token ,Integer role){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            Integer count = purchaseService.cancelOrderCount(token ,role ,start,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/cancelOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购取消订单列表
     * @param token
     * @return
     */
    @RequestMapping("/cancelOrder")
    @ApiOperation(value = "采购取消订单列表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message cancelOrder(String token ,Integer role, Integer pageNo, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay();
            String end = DateUtil.getDay();
            if (startTime != null && !"".equals(startTime) && endTime != null && !"".equals(endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = purchaseService.cancelOrder(token ,role ,pageNo,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("采购控制层（/purchase/cancelOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 回退取消订单
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/updatePurchaseStatus")
    @SysLog(module = "采购管理" ,methods = "回退取消订单")
    @ApiOperation(value = "回退取消订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "页数" ,paramType = "订单id")
    })
    public Message updatePurchaseStatus(String token ,Integer role, Integer id){
        Message message = Message.non();
        try {
            Integer count = purchaseService.updatePurchaseStatus(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("回退失败");
            }
            return message.code(Message.codeSuccessed).message("回退成功，订单在待审核了");
        }catch (Exception e){
            log.error("采购控制层（/purchase/updatePurchaseStatus）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 采购收款记录
     * @param id
     * @return
     */
    @RequestMapping("/selectOrderMoneyRecords")
    @ApiOperation(value = "采购收款记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query")
    })
    public Message selectOrderMoneyRecords(String token , Integer role , Integer id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = purchaseService.selectOrderMoneyRecords(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("查询成功");
        }catch (Exception e){
            log.error("订单控制层（/purchase/selectOrderMoneyRecords）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }






    @RequestMapping("/find")
    public Message find(String token, String news){
        Message message = Message.non();

            WebSocketUtil ws = new WebSocketUtil();
            JSONObject jo = new JSONObject();
            jo.put("message", news);
            jo.put("token",token);
        System.out.println("开始测试");
        try {
            ws.onMessage(jo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.code(Message.codeSuccessed).message("dfs");
    }


































}
