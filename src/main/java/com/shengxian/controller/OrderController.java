package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.*;
import com.shengxian.entity.Exp;
import com.shengxian.entity.Order;
import com.shengxian.service.ExcelService;
import com.shengxian.service.OrderService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 销售订单
 *
 * @Author: yang
 * @Date: 2018-11-20
 * @Version: 1.0
 */

@Api(description = "销售管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private ExcelService excelService;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    /**
     * 创建订单编号
     * @param token
     * @return
     */
    @RequestMapping("/createOrder")
    @ApiOperation(value = "创建订单编号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message createPurchaseOrder(String token ,Integer role ){
        Message message = Message.non();
        try {
            HashMap order = orderService.createOrder(token ,role );
            return message.code(Message.codeSuccessed).data(order).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 搜索商家用户
     * @param name
     * @return
     */
    @RequestMapping("/selectBindingUser")
    @ApiOperation(value = "搜索商家用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号" ,paramType = "query")
    })
    public Message selectBindingUser(String token ,Integer role , String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMaps = orderService.selectBindingUser(token ,role ,names);
            return message.code(Message.codeSuccessed).data(hashMaps).message("搜索成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     *通过用户id查询用户信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectUserInfoById")
    @ApiOperation(value = "通过用户id查询用户信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "用户id" ,paramType = "query")
    })
    public Message selectUserInfoById(String token ,Integer role , Integer id){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入用户id");
        }
        try {
            HashMap hashMap = orderService.selectUserInfoById(id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/selectUserInfoById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询绑定用户的收藏产品
     * @param token
     * @param binding_id 店铺的用户id
     * @return
     */
    @RequestMapping("/selectBandingUserGoodsCollection")
    @ApiOperation(value = "查询绑定用户的收藏产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "绑定用户id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号" ,paramType = "query")
    })
    public Message selectBandingUserGoodsCollection(String token ,Integer role , Integer binding_id, String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMaps = orderService.selectBandingUserGoodsCollection(token ,role , binding_id,names);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/selectBandingUserGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(e.getMessage());
        }

    }

    /**
     * 添加用户的产品收藏
     * @param token
     * @param binding_id 商家用户id
     * @param goods_id 产品id
     * @return
     */
    @RequestMapping("/addBandingUserGoodsCollection")
    @SysLog(module = "销售管理",methods = "添加用户产品收藏")
    @ApiOperation(value = "添加用户的产品收藏" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "绑定用户id" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query")
    })
    public Message addBandingUserGoodsCollection(String token ,Integer role, Integer binding_id , Integer goods_id){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("请输入要收藏的产品id");
        }
        try {
            Integer count = orderService.addBandingUserGoodsCollection(binding_id, goods_id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("收藏失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("收藏成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/addBandingUserGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除用户收藏产品
     * @param token
     * @param collection_id
     * @return
     */
    @RequestMapping("/deleteBandingUserGoodsCollection")
    @SysLog(module = "销售管理",methods = "取消用户产品收藏")
    @ApiOperation(value = "删除用户收藏产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "collection_id" ,value = "收藏id" ,paramType = "query")
    })
    public Message deleteBandingUserGoodsCollection(String token ,Integer role , String collection_id){
        Message message = Message.non();
        try {
            Integer count = orderService.deleteBandingUserGoodsCollection(collection_id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("取消收藏产品失败");
            }
            return message.code(Message.codeSuccessed).message("取消收藏产品成功");
        }catch (Exception e){
            log.error("订单控制层（/order/deleteBandingUserGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过用户方案id和产品id查询查询产品信息
     * @param token
     * @param goods_id 产品id
     * @param scheme_id 用户的方案id
     * @return
     */
    @RequestMapping("/findGoodsInfoBySidAndGid")
    @ApiOperation(value = "通过用户方案id和产品id查询查询产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "scheme_id" ,value = "客户方案id" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户绑定id" ,paramType = "query")
    })
    public Message findGoodsInfoByScheme_idAndGoods_id(String token ,Integer role, String goods_id, Integer scheme_id, Integer binding_id ){
        Message message  = Message.non();
        if (!IntegerUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("请输入产品id");
        }
        if (scheme_id == null){
            return message.code(Message.codeFailured).message("请输入用户方案id");
        }
        try {
            List<HashMap> hashMaps = orderService.findGoodsInfoBySidAndGid(token, role, goods_id, scheme_id, binding_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/findGoodsInfoBySidAndGid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 销售订单
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addOrder")
    @SysLog(module = "销售管理" ,methods = "下订单")
    @ApiOperation(value = "销售订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public  Message addOrder(String token ,Integer role, String json){
        Message message = Message.non();

        try {
            Order order = JSONObject.parseObject(json, Order.class);

            Integer count = orderService.addOrder(token ,role , order);

            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("提交订单失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("提交订单成功");
        }catch (IndexOutOfBoundsException e){
            return message.code(Message.codeFailured).message("员工开单提成金额没输入，请去员工资料里去完善！");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/addOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 销售退货订单
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addReturnOrder")
    @SysLog(module = "销售管理" ,methods = "下退货订单")
    @ApiOperation(value = "销售退货订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addReturnOrder(String token ,Integer role ,  String json){
        Message message = Message.non();

        try {
            Order order = JSONObject.parseObject(json, Order.class);

            Integer count = orderService.addReturnOrder(token ,role , order);

            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("提交订单失败");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("您的账号登录失效或在另一台设备登录");
            }
            return message.code(Message.codeSuccessed).data(count).message("提交订单成功");
        }catch (Exception e){
            log.error("订单控制层（/order/addReturnOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * （临时订单）通过订单编号查询历史订单
     * @param token
     * @param number
     * @return
     */
    @RequestMapping("/historyOrder")
    @ApiOperation(value = "临时订单）通过订单编号查询历史订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query")
    })
    public Message historyOrder(String token ,Integer role, String number){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.historyOrder(number);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }


    /**
     * 查询历史订单
     * @param token
     * @param binding_id
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/findHistoryOrder")
    @ApiOperation(value = "查询历史订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户绑定id" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message findHistoryOrder(String token ,Integer role, Integer binding_id, String startTime, String endTime){
        Message message = Message.non();
        String start = DateUtil.getDay();
        String end = DateUtil.getDay();
        try {
            if (IntegerUtils.isEmpty(binding_id)) {
                return message.code(Message.codeFailured).message("请输入用户id");
            }
            if ( IntegerUtils.isEmpty(startTime ,endTime )) {
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime;
                end = endTime;
            }
            List<HashMap> hashMaps = orderService.findHistoryOrder(binding_id, start, end);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/findHistoryOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 通过订单id查询订单详情
     * @param order_id
     * @return
     */
    @RequestMapping("/findOrderInfoById")
    @ApiOperation(value = "通过订单id查询订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query")
    })
    public Message findOrderInfoById(String order_id){
        Message message = Message.non();
        if ( !IntegerUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            List<HashMap> hashMaps = orderService.findOrderInfoById(order_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/findOrderInfoById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 临时订单
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addFalseOrder")
    @SysLog(module = "销售管理" ,methods = "添加临时订单")
    @ApiOperation(value = "添加临时订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addFalseOrder(String token ,Integer role, String json){
        Message message = Message.non();

        try {
            Order order = JSONObject.parseObject(json, Order.class);

            Integer count = orderService.addFalseOrder(token ,role ,order);
            if ( IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("提交订单失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("提交订单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/addFalseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加费用支出
     * @param
     * @return
     */
    @RequestMapping("/addExpense")
    @SysLog(module = "销售管理",methods = "添加费用支出或收入")
    @ApiOperation(value = "添加费用支出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addExpense(String token ,Integer role, String json){
        Message message = Message.non();
        try {
            Exp exp = JSONObject.parseObject(json, Exp.class);
            Integer count = orderService.addExpense(token ,role , exp.getExpenses());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加费用支出失败");
            }
            return message.code(Message.codeSuccessed).message("添加费用支出成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/addExpense）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 费用列表
     * @param token
     * @return
     */
    @RequestMapping("/expenseList")
    @ApiOperation(value = "费用列表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0费用支出，1费用收入" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "json格式" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "json格式" ,paramType = "query")
    })
    public Message expenseList(String token ,Integer role, Integer pageNo, Integer type, String startTime, String endTime){
        Message message = Message.non();
        String start = DateUtil.getDay();
        String end = DateUtil.getDay();
        try {
            if (IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = orderService.expenseList( token ,role , pageNo, type, start, end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/expenseList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除费用信息
     * @param token
     * @param role
     * @param id
     * @return
     */
    @RequestMapping("/deleteExpense")
    @SysLog(module = "销售管理",methods = "删除费用信息")
    @ApiOperation(value = "删除费用信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "费用id" ,paramType = "query")
    })
    public Message deleteExpense(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = orderService.deleteExpense(token , role ,id);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/deleteExpense）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 超期进货的用户总数
     * @param token
     * @return
     */
    @RequestMapping("/overduePurchaseUserCount")
    @ApiOperation(value = "超期进货的用户总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message overduePurchaseUserCount(String token ,Integer role){
        Message message = Message.non();
        Integer count = orderService.overduePurchaseUserCount(token ,role );
        return message.code(Message.codeSuccessed).data(count).message("获取成功");
    }


    /**
     * 超期进货的用户
     * @param token
     * @param name 搜索条件，名称或编号
     * @param cycle 进货周期天数
     * @return
     */
    @RequestMapping("/overduePurchaseUser")
    @ApiOperation(value = "超期进货的用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "cycle" ,value = "进货周期天数" ,paramType = "query")
    })
    public Message overduePurchaseUser(String token ,Integer role, Integer pageNo, String name, Integer cycle){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.overduePurchaseUser(token ,role ,pageNo,names ,cycle);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/overduePurchaseUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 标记超期进货为已读
     * @param
     * @return
     */
    @RequestMapping("/markReaded")
    @ApiOperation(value = "标记超期进货为已读" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId" ,value = "token" ,paramType = "query")
    })
    public Message markReaded(Integer orderId){
        Message message = Message.non();
        Integer count = orderService.markReaded(orderId );
        return message.code(Message.codeSuccessed).data(count).message("操作成功");
    }


    /**
     * 没有销售的用户总数
     * @param token
     * @return
     */
    @RequestMapping("/noSalesUserCount")
    @ApiOperation(value = "没有销售的用户总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message noSalesUserCount(String token ,Integer role){
        Message message = Message.non();
        Integer count = orderService.noSalesUserCount(token ,role );
        return message.code(Message.codeSuccessed).data(count).message("获取成功");

    }

    /**
     * 没有销售的用户
     * @param token
     * @param name 编号或名称
     * @return
     */
    @RequestMapping("/noSalesUser")
    @ApiOperation(value = "没有销售的用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "编号或名称" ,paramType = "query")
    })
    public Message noSalesUser(String token ,Integer role, Integer pageNo, String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.noSalesUser(token ,role ,pageNo,names);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/noSalesUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 待接单总数
     * @param token
     * @return
     */
    @RequestMapping("/waitingOrderCount")
    @ApiOperation(value = "待接单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message waitingOrderCount(String token ,Integer role){
        Message message = Message.non();
        try {
            Integer count = orderService.waitingOrderCount(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/waitingOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 待接单
     * @param token
     * @return
     */
    @RequestMapping("/waitingOrder")
    @ApiOperation(value = "待接单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message waitingOrder(String token ,Integer role ,Integer pageNo){
        Message message = Message.non();
        try {
            Page page = orderService.waitingOrder(token ,role  , pageNo);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/waitingOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 订单详情
     * @param token
     * @param id 订单id
     * @return
     */
    @RequestMapping("/orderDetail")
    @ApiOperation(value = "订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query")
    })
    public Message orderDetail(String token ,Integer role, Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            HashMap hashMaps = orderService.orderDetail(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/orderDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除订单详情产品
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteOrderDetail")
    @SysLog(module = "销售管理" ,methods = "删除订单详情产品")
    @ApiOperation(value = "删除订单详情产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query")
    })
    public Message deleteOrderDetail(String token ,Integer role, Integer id ,Integer mold){
        Message message = Message.non();
        try {
            Integer count = orderService.deleteOrderDetail(id ,mold);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/deleteOrderDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改订单详情
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/updateOrderPrice")
    @SysLog(module = "销售管理" ,methods = "修改订单详情")
    @ApiOperation(value = "修改订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public  Message updateOrderPrice(String token ,Integer role, String json){
        Message message = Message.non();
        try {
            Order order = JSONObject.parseObject(json, Order.class);
            Integer count = orderService.updateOrderPrice(token ,role , order);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderPrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 修改订单打印状态
     * @param token
     * @param id 订单id
     * @param
     * @return
     */
    @RequestMapping("/updatePrintFrequ")
    @ApiOperation(value = "修改订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query")
    })
    public Message updatePrintFrequ(String token ,Integer role , Integer id ){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = orderService.updatePrintFrequ(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("订单控制层（/order/updatePrintFrequ）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 接受或拒绝（员工取消接单）
     * @param token
     * @param id 订单id
     * @param status 2待送货（接受），3派送中，5拒绝接单
     * @return
     */
    @RequestMapping("/updateOrderAcceptOrRejectionStatus")
    @SysLog(module = "销售管理" ,methods = "接受或拒绝（员工取消接单）")
    @ApiOperation(value = "接受或拒绝（员工取消接单）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "2接受，3员工接单，5拒绝接单" ,paramType = "query"),
            @ApiImplicitParam(name = "reason" ,value = "拒绝原因" ,paramType = "query"),
    })
    public Message updateOrderAcceptOrRejectionStatus(String token ,Integer role, Integer id , Integer status, String reason  ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        if (status == null ){
            return message.code(Message.codeFailured).message("请输入订单状态");
        }
        try {
            Integer count = orderService.updateOrderAcceptOrRejectionStatus(token , role , id , status, reason);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderAcceptOrRejectionStatus）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 一键接单
     * @param token
     * @return
     */
    @RequestMapping("/onekeyAcceptOrder")
    @SysLog(module = "销售管理" ,methods = "一键接单")
    @ApiOperation(value = "一键接单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "1商城待接单，2待送货（接受），3派送中，4确认到货，5拒绝接单，6取消订单" ,paramType = "query")
    })
    public Message onekeyAcceptOrder(String token ,Integer role ){
        Message message = Message.non();
        try {
            Integer count = orderService.onekeyAcceptOrder(token ,role);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("一键接单失败");
            }
            return message.code(Message.codeSuccessed).message("一键接单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/onekeyAcceptOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 未打印的订单总数
     * @param token
     * @return
     */
    @RequestMapping("/notPrintedOrderCount")
    @ApiOperation(value = "未打印的订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query")
    })
    public Message notPrintedOrderCount(String token ,Integer role){
        Message message = Message.non();
        try {
            Integer count = orderService.notPrintedOrderCount(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/notPrintedOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 未打印的订单
     * @param token
     * @return
     */
    @RequestMapping("/notPrintedOrder")
    @ApiOperation(value = "未打印的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query")
    })
    public Message notPrintedOrder(String token ,Integer role, Integer pageNo, String name, String number ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.notPrintedOrder(token ,role ,pageNo,names ,number);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/notPrintedOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 未打印的订单汇总
     * @param token
     * @return
     */
    @RequestMapping("/notPrintedOrderSummary")
    @ApiOperation(value = "未打印的订单汇总" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query")
    })
    public Message notPrintedOrderSummary(String token ,Integer role, Integer pageNo, String name, String number ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.notPrintedOrderSummary(token ,role ,pageNo,names ,number);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/notPrintedOrderSummary）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 待送货订单总数
     * @param token
     * @return
     */
    @RequestMapping("/stayDeliveredCount")
    @ApiOperation(value = "待送货订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message stayDeliveredCount(String token ,Integer role){
        Message message = Message.non();
        try {
            Integer count = orderService.stayDeliveredCount(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/stayDeliveredCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 待送货订单
     * @param token
     * @param pageNo
     * @param name 用户名称或用户编号或二维码标识号
     * @param number 订单编号
     * @return
     */
    @RequestMapping("/stayDelivered")
    @ApiOperation(value = "待送货订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message stayDelivered(String token ,Integer role, Integer pageNo, String name, String number ,String staffName ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.stayDelivered(token ,role ,pageNo,names ,number ,staffName);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/stayDelivered）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 待送货订单汇总
     * @param token
     * @param pageNo
     * @param name 用户名称或用户编号或二维码标识号
     * @param number 订单编号
     * @return
     */
    @RequestMapping("/stayDeliveredSummary")
    @ApiOperation(value = "待送货订单汇总" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query")
    })
    public Message stayDeliveredSummary(String token ,Integer role, Integer pageNo, String name, String number ,Integer mold  ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.stayDeliveredSummary(token ,role ,pageNo,names ,number ,mold);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/stayDeliveredSummary）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 确认到货或取消状态（4 确认已送达）（6 取消订单）
     * @param token
     * @param id 订单id
     * @param status 1商城待接单，2待送货（接受），3派送中，4确认到货，5拒绝接单，6取消订单
     * @return
     */
    @RequestMapping("/updateOrderConfirmOrCancelStatus")
    @SysLog(module = "销售管理" ,methods = "确认到货或取消订单")
    @ApiOperation(value = "确认到货或取消状态" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = " 1商城待接单，2待送货（接受），3派送中，4确认到货，5拒绝接单，6取消订单" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query")
    })
    public  Message updateOrderConfirmOrCancelStatus(String token ,Integer role, Integer id , Integer status, Integer mold){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        if (IntegerUtils.isEmpty(status) ){
            return message.code(Message.codeFailured).message("请输入订单状态");
        }
        try {
            Integer count = orderService.updateOrderConfirmOrCancelStatus(token ,role , id, status, mold);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderConfirmOrCancelStatus）接口报错---------"+e.getMessage());
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
    @SysLog(module = "销售管理" ,methods = "未到货的订单直接收款")
    @ApiOperation(value = "未到货的订单直接收款(暂时无用)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款审核，2欠款，3已完成" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售，1退货" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1微信，2支付宝，3现金，4银行卡，5其它（state传0，type则不传）" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "收款金额（state传0，money则不传）" ,paramType = "query")
    })
    public synchronized  Message updateOrderReceivables(String token ,Integer role, Integer id , Integer state, Integer mold , Integer type, Double money){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = orderService.updateOrderReceivables(token ,role , id, state,  mold, type , money );
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
     * 未到货的订单取消
     * @param token
     * @param id 订单id
     * @return
     */
    @RequestMapping("/updateOrderCancel")
    @SysLog(module = "销售管理" ,methods = "未到货的订单取消")
    @ApiOperation(value = "未到货的订单取消" ,httpMethod = "POST")
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
            Integer count = orderService.updateOrderCancel(token ,role , id ,  mold );
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
     * 查询未付款,欠款订单总数
     * @param token
     * @param state
     * @return
     */
    @RequestMapping("/arrivalOrderCount")
    @ApiOperation(value = "查询未付款,欠款订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款，2欠款，3完成" ,paramType = "query")
    })
    public Message arrivalOrderCount(String token ,Integer role, Integer state){
        Message message = Message.non();
        try {
            Integer count = orderService.arrivalOrderCount(token ,role ,state);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/arrivalOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询未付款的订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/unpaidOrder")
    @ApiOperation(value = "查询未付款的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message unpaidOrder(String token ,Integer role, Integer pageNo, String name, String number , String startTime, String endTime ,String staffName ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
           Page page = orderService.unpaidOrder(token ,role ,pageNo,names ,number,startTime,endTime ,staffName);
           return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/unpaidOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询欠款的订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/arrearsOrder")
    @ApiOperation(value = "查询欠款的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message arrearsOrder(String token ,Integer role, Integer pageNo, String name, String number , String startTime, String endTime,String staffName ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = orderService.arrearsOrder(token ,role ,pageNo,names ,number,startTime,endTime ,staffName);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/arrearsOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }




    /**
     * 查询已完成订单
     * @param token
     * @param pageNo
     * @param name 用户名称或编号
     *@param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/arrivalOrder")
    @ApiOperation(value = "查询已完成订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "staffName" ,value = "操作人搜索" ,paramType = "query")
    })
    public Message arrivalOrder(String token ,Integer role, Integer pageNo, String name, String number , String startTime, String endTime,String staffName ){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            String start =startTime ,end=endTime;
            start = DateUtil.getDay();  end = DateUtil.getDay();
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = orderService.arrivalOrder(token ,role ,pageNo ,names ,number,start,end ,staffName);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/arrivalOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 收款或欠款
     * @param token
     * @param id 订单id
     * @param state 0未付款，1申请欠款审核，2欠款，3已完成，4拒绝欠款
     * @return
     */
    @RequestMapping("/updateArrivalOrder")
    @SysLog(module = "销售管理" ,methods = "收款或欠款")
    @ApiOperation(value = "收款或欠款" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款审核，2欠款，3已完成，4拒绝欠款" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "支付类型" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "收款金额" ,paramType = "query")
    })
    public synchronized Message updateArrivalOrder(String token ,Integer role, Integer id, Integer state, Integer type, Double money){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        if (state !=0 ){
            if (money == null){
                return message.code(Message.codeFailured).message("请输入实收金额");
            }
        }
        try {
            Integer count = orderService.updateArrivalOrder(token ,role , id, state, type, money);
            if ( IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("操作失败");
            }
            return message.code(Message.codeSuccessed).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateArrivalOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询
     * @param token
     * @return
     */
    @RequestMapping("/refuseOrder")
    @ApiOperation(value = "查询拒绝订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query")
    })
    public Message refuseOrder(String token ,Integer role, Integer pageNo){
        Message message = Message.non();
       try {
           Page page = orderService.refuseOrder(token ,role ,pageNo);
           return message.code(Message.codeSuccessed).data(page).message("获取成功");
       }catch (NullPointerException e){
           return message.code(Message.codeFailured).message(e.getMessage());
       }catch (Exception e){
           log.error("订单控制层（/order/refuseOrder）接口报错---------"+e.getMessage());
           return message.code(Message.codeFailured).message(Global.ERROR);
       }
    }

    /**
     * 所有销售订单
     * @param token
     * @param pageNo
     * @param name 标识码，用户名称/用户编号/审核人/送货人/收款人
     * @param number 订单编号
     * @return
     */
    @RequestMapping("/allSaleOrder")
    @ApiOperation(value = "所有销售订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "标识码，用户名称/用户编号/审核人/送货人/收款人" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "销售单，退货单" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "" ,paramType = "query")
    })
    public Message allSaleOrder(String token ,Integer role,  Integer pageNo, String name, String number, String startTime, String endTime, Integer mold ,Integer type){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        String start =startTime ,end=endTime;
        try {
            end = DateUtil.getDay();
            start = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime)){

                //当没有传开始时间和结束时间，默认查询当天的订单
                end = endTime; start = startTime;
            }
            Page page = orderService.allSaleOrder(token ,role ,pageNo ,names ,number,start,end,mold ,type);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/allSaleOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这一年总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/yearSalePirce")
    @ApiOperation(value = "计算这一年总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap yearSalePirce(String token ,Integer role) {
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.yearSalePirce(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/yearSalePirce）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这季度总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/quarterSalePrice")
    @ApiOperation(value = "计算这季度总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap quarterSalePrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.quarterSalePrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/quarterSalePrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这月总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/monthSalePrice")
    @ApiOperation(value = "计算这月总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap monthSalePrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.monthSalePrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/monthSalePrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算这周总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/weekSalePrice")
    @ApiOperation(value = "计算这周总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap weekSalePrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.weekSalePrice(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/weekSalePrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 计算当天总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/daysSalePrice")
    @ApiOperation(value = "计算当天总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public HashMap daysPurchassPrice(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.daysSalePrice(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/daysSalePrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 自定义时间段总销售金额
     * @param token
     * @return
     */
    @RequestMapping("/definitionSalePrice")
    @ApiOperation(value = "自定义时间段总销售金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public HashMap definitionSalePrice(String token ,Integer role,String startTime,String endTime){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.definitionSalePrice(token ,role ,startTime,endTime);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/definitionSalePrice）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 用户取消订单总数
     * @param token
     * @return
     */
    @RequestMapping("/userCancelOrderCount")
    @ApiOperation(value = "用户取消订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message userCancelOrderCount(String token ,Integer role){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay();
            String end = DateUtil.getDay();
            Integer count = orderService.userCancelOrderCount(token ,role,start,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/userCancelOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 用户取消订单
     * @param token
     * @return
     */
    @RequestMapping("/userCancelOrder")
    @ApiOperation(value = "用户取消订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message userCancelOrder(String token ,Integer role, Integer pageNo, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay();
            String end = DateUtil.getDay();
            if (IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = orderService.userCancelOrder(token ,role ,pageNo,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/userCancelOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 申请审核用户信息总数
     * @param token
     * @return
     */
    @RequestMapping("/auditUserCount")
    @ApiOperation(value = "申请审核用户信息总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message auditUserCount(String token ,Integer role){
        Message message = Message.non();
        try {
           Integer count = orderService.auditUserCount(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/auditUserCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 申请审核用户信息
     * @param token
     * @return
     */
    @RequestMapping("/auditUser")
    @ApiOperation(value = "申请审核用户信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message auditUser(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = orderService.auditUser(token ,role );
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/auditUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 审核用户
     * @param token
     * @param id 审核id
     * @param state 1不通过，2通过
     * @param reason 不通过原因
     * @return
     */
    @RequestMapping("/updateAuditUser")
    @SysLog(module = "销售管理" ,methods = "审核用户")
    @ApiOperation(value = "审核用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "审核id" ,value = "审核id" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "1不通过，2通过" ,paramType = "query"),
            @ApiImplicitParam(name = "reason" ,value = "不通过原因" ,paramType = "query"),
    })
    public Message updateAuditUser(String token ,Integer role, Integer id, String phone, Integer state, String reason){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入审核id");
        }
        try {
            HashMap count = orderService.updateAuditUser(token ,role ,id, phone, state, reason);
            return message.code(Message.codeSuccessed).data(count).message("审核成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateAuditUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 申请欠款审核总数
     * @param token
     * @return
     */
    @RequestMapping("/arrearsAuditCount")
    @ApiOperation(value = "申请欠款审核总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message arrearsAuditCount(String token ,Integer role){
        Message message = Message.non();
        try {
            Integer count = orderService.arrearsAuditCount(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 申请欠款审核列表
     * @param token
     * @return
     */
    @RequestMapping("/arrearsAudit")
    @ApiOperation(value = "申请欠款审核列表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message arrearsAudit(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = orderService.arrearsAudit(token ,role );
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 审核欠款
     * @param token
     * @param id
     * @param state 0未付款，1申请欠款审核，2欠款，3已完成，4拒绝欠款
     * @return
     */
    @RequestMapping("/updateArrearsAudit")
    @SysLog(module = "销售管理" ,methods = "审核欠款")
    @ApiOperation(value = "审核欠款" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款审核，2欠款，3已完成，4拒绝欠款" ,paramType = "query")
    })
    public Message updateArrearsAudit(String token ,Integer role, Integer id, Integer state){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = orderService.updateArrearsAudit(id, state);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("审核失败");
            }
            return message.code(Message.codeSuccessed).message("审核成功");
        }catch (Exception e){
            log.info(e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 打印销售订单
     * @param token
     * @param order_id
     * @return
     */
    @RequestMapping("/orderPrint")
    @ApiOperation(value = "打印销售订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query")
    })
    public Message orderPrint(String token ,Integer role, Integer order_id){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.orderPrint( order_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/orderPrint）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 打印销售订单详情
     * @param
     * @param order_id
     * @return
     */
    @RequestMapping("/orderPrintDetail")
    @ApiOperation(value = "打印销售订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "pageSize" ,value = "每页总数" ,paramType = "query")
    })
    public Message orderPrintDetail(String token ,Integer role, Integer pageNo , Integer order_id ,Integer pageSize){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            List<HashMap> hashMaps = orderService.orderPrintDetail(pageNo, order_id ,pageSize);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/orderPrintDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 打印临时订单
     * @param token
     * @param order_id
     * @return
     */
    @RequestMapping("/printTemporaryOrder")
    @ApiOperation(value = "打印临时订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query")
    })
    public Message printTemporaryOrder(String token ,Integer role, Integer order_id){
        Message message = Message.non();
        try {
            HashMap hashMap = orderService.printTemporaryOrder( order_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/printTemporaryOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 打印临时订单详情
     * @param
     * @param order_id
     * @return
     */
    @RequestMapping("/printTemporaryOrderDetail")
    @ApiOperation(value = "打印临时订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "pageSize" ,value = "每页总数" ,paramType = "query")
    })
    public Message printTemporaryOrderDetail(String token ,Integer role, Integer pageNo , Integer order_id ,Integer pageSize){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入订单id");
        }
        List<HashMap> hashMaps = orderService.printTemporaryOrderDetail(pageNo, order_id ,pageSize);
        return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
    }


    /**
     * 超期用户导出
     * @param token
     * @param
     */
    @RequestMapping("/overduePurchaseUserDownload")
    @ApiOperation(value = "超期用户导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public void overduePurchaseUserDownload(String token ,Integer role, HttpServletResponse response){
        Message message = Message.non();
        HSSFWorkbook workbook = orderService.overduePurchaseUserDownload(token ,role );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"超期用户"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 没有销售用户导出
     * @param token
     * @param
     */
    @RequestMapping("/noSalesUserDownload")
    @ApiOperation(value = "没有销售用户导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public void noSalesUserDownload(String token ,Integer role, HttpServletResponse response){
        Message message = Message.non();
        HSSFWorkbook workbook = orderService.noSalesUserDownload(token ,role );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"没有销售用户"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 回退取消订单
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/updateOrderStatus")
    @SysLog(module = "销售管理" ,methods = "回退取消订单")
    @ApiOperation(value = "回退取消订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售单，1销售退货单" ,paramType = "query")
    })
    public Message updateOrderStatus(String token ,Integer role, Integer id , Integer mold){
        Message message = Message.non();
        try {
            Integer count = orderService.updateOrderStatus(id ,mold);
            if (count == null || count == 0){
                return message.code(Message.codeFailured).message("回退失败");
            }
            return message.code(Message.codeSuccessed).message("回退成功,订单在待送货了");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateOrderStatus）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 验单
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/updateCheck")
    @ApiOperation(value = "验单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query")
    })
    public Message updateCheck(String token ,Integer role , Integer id){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(id)){
            message.code(Message.codeFailured).message("请输入订单id");
        }
        try {
            Integer count = orderService.updateCheck(id);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("验单失败");
            }
            return message.code(Message.codeSuccessed).message("验单成功");
        }catch (Exception e){
            log.error("订单控制层（/order/updateCheck）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 一键取消验单
     * @param token
     * @param state
     * @return
     */
    @RequestMapping("/updateCancelCheck")
    @ApiOperation(value = "一键取消验单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0未付款，1申请欠款审核，2欠款，3已完成，4拒绝欠款" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message updateCancelCheck(String token ,Integer role , Integer state, String startTime , String endTime){
        Message message = Message.non();
        if (state == null){
            message.code(Message.codeFailured).message("请输入订单id");
        }
        String start = null ,end = null;

        if (state.equals(3)){
            start = DateUtil.getDay();  end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime) ){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
        }

        try {
            Integer count = orderService.updateCancelCheck(token ,role ,state,start,end);

            if ( IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("一键取消验单失败");
            }
            return message.code(Message.codeSuccessed).message("一键取消验单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateCancelCheck）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 一键取消待送货验单
     * @param token
     * @return
     */
    @RequestMapping("/updateStayDeliveredCancelCheck")
    @ApiOperation(value = "一键取消待送货验单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message updateStayDeliveredCancelCheck(String token ,Integer role ){
        Message message = Message.non();
        try {
            Integer count = orderService.updateStayDeliveredCancelCheck(token ,role );

            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("一键取消验单失败");
            }
            return message.code(Message.codeSuccessed).message("一键取消验单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateStayDeliveredCancelCheck）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     *
     * 积分兑换订单总数
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/selectIntegraOrderCount")
    @ApiOperation(value = "积分兑换订单总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "1待送货（确认兑换），2取消兑换，3已到货" ,paramType = "query")
    })
    public Message selectIntegraOrderCount(String token , Integer role ,Integer state ){
        Message message = Message.non();
        try {
            Integer count = orderService.selectIntegraOrderCount(token ,role  ,state );

            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/selectIntegraOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 积分兑换订单
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/selectIntegraOrder")
    @ApiOperation(value = "积分兑换订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "1待送货（确认兑换），2取消兑换，3已到货" ,paramType = "query")
    })
    public Message selectIntegraOrder(String token , Integer role ,Integer pageNo ,Integer state ){
        Message message = Message.non();
        try {
            Page page = orderService.selectIntegraOrder(token ,role ,pageNo ,state);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/selectIntegraOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *
     * 积分兑换订单确认
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/updateIntegraOrder")
    @SysLog(module = "销售管理" ,methods = "积分兑换订单确认")
    @ApiOperation(value = "积分兑换订单确认" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单i" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "1待送货（确认兑换），2取消兑换，3已到货" ,paramType = "query")
    })
    public Message updateIntegraOrder(String token , Integer role ,Integer id ,Integer state ){
        Message message = Message.non();
        try {
            Integer count = orderService.updateIntegraOrder(token ,role  ,id ,state );
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("确认失败");
            }
            return message.code(Message.codeSuccessed).message("确认成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("订单控制层（/order/updateIntegraOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 售货服务总数
     * @return
     */
    @RequestMapping("/salesServiceTCount")
    @ApiOperation(value = "售货服务总数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message salesServiceTCount( String token , Integer role ,Integer state ){
        Message message = Message.non();
        try {
            Integer count = orderService.salesServiceTCount( token , role ,state);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/salesServiceTCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 售货服务
     * @param pageNo
     * @return
     */
    @RequestMapping("/salesService")
    @ApiOperation(value = "售货服务" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query")
    })
    public Message salesService( String token , Integer role , Integer pageNo ,Integer state){
        Message message = Message.non();
        try {
            Page page = orderService.salesService( token , role , pageNo ,state);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("订单控制层（/order/salesService）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 处理售货服务
     * @param id
     * @param state
     * @return
     */
    @RequestMapping("/updateSalesService")
    @SysLog(module = "销售管理" ,methods = "处理售货服务")
    @ApiOperation(value = "处理售货服务" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "售后id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "投诉处理状态:0未处理，1处理中，2处理完成" ,paramType = "query")
    })
    public Message updateSalesService( String token , Integer role ,Integer id,Integer state){
        Message message = Message.non();
        try {
            Integer count = orderService.updateSalesService(id,state);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("处理失败");
            }
            return message.code(Message.codeSuccessed).message("处理成功");
        }catch (Exception e){
            log.error("订单控制层（/order/updateSalesService）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 订单收款记录
     * @param id
     * @return
     */
    @RequestMapping("/selectOrderMoneyRecords")
    @ApiOperation(value = "订单收款记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "id" ,paramType = "query")
    })
    public Message selectOrderMoneyRecords(String token , Integer role , Integer id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = orderService.selectOrderMoneyRecords(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("查询成功");
        }catch (Exception e){
            log.error("订单控制层（/order/selectOrderMoneyRecords）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



}
