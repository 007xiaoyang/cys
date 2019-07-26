package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.DateUtil;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.entity.Lous;
import com.shengxian.entity.Settlement;
import com.shengxian.service.ExcelService;
import com.shengxian.service.FinanceService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Description: 财务
 *
 * @Author: yang
 * @Date: 2018-12-02
 * @Version: 1.0
 */
@Api(description = "财务")
@RestController
@RequestMapping("/finance")
public class FinanceController {

    private static Logger log = Logger.getLogger(FinanceController.class);

    @Autowired
    private FinanceService financeService;

    @Autowired
    private ExcelService excelService;

    /**
     * 商城产品金额
     * @param token
     * @param pageNo
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/mallMoney")
    @ApiOperation(value = "商城产品金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message mallMoney(String token ,Integer role , Integer pageNo, String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.mallMoney(token ,role ,pageNo,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/mallMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *线下产品金额
     * @param token
     * @param pageNo
     * @param staff_id 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/underLineMoney")
    @ApiOperation(value = "线下产品金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message underLineMoney(String token  ,Integer role , Integer pageNo, Integer staff_id , String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.underLineMoney(token ,role ,pageNo,staff_id,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/underLineMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *退货销售订单
     * @param token
     * @param pageNo
     * @param staff_if 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/returnMoney")
    @ApiOperation(value = "退货销售订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message returnMoney(String token  ,Integer role , Integer pageNo, Integer staff_if , String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.returnMoney(token ,role ,pageNo,staff_if,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/returnMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 总销售订单
     * @param token
     * @param pageNo
     * @param staff_id
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/tatolSaleMoney")
    @ApiOperation(value = "总销售订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "" ,paramType = "query")
    })
    public Message tatolSaleMoney(String token  ,Integer role , Integer pageNo, Integer staff_id , String name, String number, String startTime, String endTime ,Integer type){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.tatolSaleMoney(token ,role ,pageNo,staff_id,name,number,start,end ,type);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/tatolSaleMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 销售财务结算
     * @param token
     * @return
     */
    @RequestMapping("/saleFinanceSettlement")
    @SysLog(module = "财务管理" ,methods = "销售财务结算")
    @ApiOperation(value = "销售财务结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public synchronized Message financeSettlement(String token  ,Integer role , String json){
        Message message = Message.non();
        try {
            Settlement settlement = JSONObject.parseObject(json, Settlement.class);

            Integer count = financeService.addFinanceSettlement(token ,role , settlement);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("您已经没有要结算的金额了，请明天在结算");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("您的账号登录失效或在另一台设备登录");
            } else if (count == -2) {
                return message.code(Message.codeFailured).message("您已经没有未结算的金额了");
            }
            return message.code(Message.codeSuccessed).message("结算成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/financeSettlement）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *采购订单
     * @param token
     * @param pageNo
     * @param staff_if 业务员
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/purchaseMoney")
    @ApiOperation(value = "采购订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message purchaseMoney(String token  ,Integer role , Integer pageNo, Integer staff_if , String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.purchaseMoney(token ,role ,pageNo,staff_if,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/purchaseMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *费用记录
     * @param token
     * @param pageNo
     * @param staff_id
     * @param name 费用名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/expenseMoney")
    @ApiOperation(value = "费用记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id,店铺默认0" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "费用名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message expenseMoney(String token  ,Integer role , Integer pageNo , Integer staff_id, String name, String startTime, String endTime){
        Message message = Message.non();
        try {
            Page page = financeService.expenseMoney(token ,role ,pageNo,staff_id ,name,startTime,endTime);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/expenseMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *采购退货订单
     * @param token
     * @param pageNo
     * @param name 用户名称或编号或标识号
     * @param number 订单编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/purchaseReturnMoney")
    @ApiOperation(value = "采购退货订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message purchaseReturnMoney(String token  ,Integer role , Integer pageNo , String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.purchaseReturnMoney(token ,role ,pageNo,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/purchaseReturnMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 总采购订单
     * @param token
     * @param pageNo
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/tatolPurchaseOrder")
    @ApiOperation(value = "总采购订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称或编号或标识号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message tatolPurchaseOrder(String token  ,Integer role , Integer pageNo , String name, String number, String startTime, String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.tatolPurchaseOrder(token ,role ,pageNo,name,number,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/tatolPurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购财务结算
     * @param token
     * @return
     */
    @RequestMapping("/purchaseFinanceSettlement")
    @SysLog(module = "财务管理" ,methods = "采购财务结算")
    @ApiOperation(value = "采购财务结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public synchronized Message purchaseFinanceSettlement(String token  ,Integer role , String json){
        Message message = Message.non();
        try {
            Settlement settlement = JSONObject.parseObject(json, Settlement.class);
            Integer count = financeService.aadPurchaseFinanceSettlement(token ,role , settlement);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("结算失败");
            }
            return message.code(Message.codeSuccessed).message("结算成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/purchaseFinanceSettlement）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 商家财务结算记录
     * @param token
     * @return
     */
    @RequestMapping("/settlementInfo")
    @ApiOperation(value = "总采购订单" ,httpMethod = "POST")
    @ApiImplicitParams({
                @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
                @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
                @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
                @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
                @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
                @ApiImplicitParam(name = "type" ,value = "1销售，2采购" ,paramType = "query")
            })
    public Message settlementInfo(String token ,Integer role , Integer pageNo, String startTime, String endTime, Integer type){
        Message message = Message.non();
        try {
            Page page = financeService.settlementInfo(token ,role ,pageNo,startTime,endTime,type);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/settlementInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过财务结算id查询结算记录
     * @param token
     * @param id 结算id
     * @return
     */
    @RequestMapping("/settlementByid")
    @ApiOperation(value = "通过财务结算id查询结算记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0销售，1采购" ,paramType = "query")
    })
    public Message settlementByid(String token ,Integer role  , Integer id){
        Message message = Message.non();
        try {
            HashMap hashMap = financeService.settlementByid(id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/settlementByid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }





    /**
     * 用户销售毛利
     * @param token
     * @param type 1 销售汇总，2销售明细
     * @return
     */
    @RequestMapping("/userSaleProfit")
    @ApiOperation(value = "用户销售毛利" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1用户销售汇总，2用户销售明细" ,paramType = "query"),
            @ApiImplicitParam(name = "bindindId" ,value = "用户id" ,paramType = "query")
    })
    public Message userSaleProfit(String token ,Integer role , Integer type, Integer pageNo, String name ,String goodsName, String startTime, String endTime ,Integer bindindId ){
        Message message = Message.non();
        Page page = null;
        try {
            String start = DateUtil.getDay()  ,end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime )){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            if (type == 1){
                //用户销售汇总
                page = financeService.userSaleProfitSummary(token ,role ,pageNo,name ,goodsName ,start,end ,bindindId);
            }else if (type == 2){
                //用户销售明细
                page = financeService.userSaleProfitDetails(token ,role ,pageNo,name ,goodsName ,start,end ,bindindId);
            }
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/userSaleProfit）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 产品销售毛利
     * @param token
     * @param type 1 销售汇总，2销售明细
     * @return
     */
    @RequestMapping("/goodsSaleProfit")
    @ApiOperation(value = "产品销售毛利" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1用户销售汇总，2用户销售明细" ,paramType = "query")
    })
    public Message goodsSaleProfit(String token ,Integer role , Integer type, Integer pageNo, String name , String startTime, String endTime, Integer is ){
        Message message = Message.non();
        Page page = null;
        System.out.println(type);
        try {
            String start = DateUtil.getDay() ,  end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            if (type == 1){
                page = financeService.goodsSaleProfitSummary(token ,role ,pageNo,name,start,end,is);
            }else if (type == 2){
                page = financeService.goodsSaleProfitDetails(token ,role ,pageNo,name,start,end,is);
            }
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/goodsSaleProfit）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加欠条
     * @param token
     * @return
     */
    @RequestMapping("/addIous")
    @SysLog(module = "财务管理" ,methods = "添加欠条")
    @ApiOperation(value = "添加欠条" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message ious(String token ,Integer role , Lous lous){
        Message message = Message.non();
        try {
            Integer count = financeService.addIous(token, role, lous);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/addIous）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 欠条id查询欠条内容
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findLous")
    @ApiOperation(value = "添加欠条" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "欠条id" ,paramType = "query")
    })
    public Message findLous(String token ,Integer role , Integer id){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入欠条id");
        }
        try {
            HashMap hashMap = financeService.findLous(id);
            return message.code(Message.codeSuccessed).data(hashMap).message("成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     *销售风险订单记录总数（默认当天的）
     * @param token
     * @return
     */
    @RequestMapping("/riskOrderCount")
    @ApiOperation(value = "销售风险订单记录总数（默认当天的）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message riskOrderCount(String token ,Integer role ){
        Message message = Message.non();

        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            Integer count = financeService.riskOrderCount(token ,role ,start,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/riskOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }


    /**
     * 销售风险订单记录（默认当天的）
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/riskOrder")
    @ApiOperation(value = "销售风险订单记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message riskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime){
        Message message = Message.non();

        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime) ){
                start = startTime ; end = endTime;
            }

            Page page = financeService.riskOrder(token ,role ,pageNo,name,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/riskOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 采购风险订单记录总数（默认当天的）
     * @param token
     * @return
     */
    @RequestMapping("/purchaseRiskOrderCount")
    @ApiOperation(value = "采购风险订单记录总数（默认当天的）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message purchaseRiskOrderCount(String token ,Integer role ){
        Message message = Message.non();

        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            Integer count = financeService.purchaseRiskOrderCount(token ,role ,start,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/purchaseRiskOrderCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 采购风险订单记录
     * @param token
     * @param pageNo
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/purchaseRiskOrder")
    @ApiOperation(value = "采购风险订单记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message purchaseRiskOrder(String token ,Integer role , Integer pageNo, String name, String startTime, String endTime){
        Message message = Message.non();

        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime) ){
                start = startTime ; end = endTime;
            }

            Page page = financeService.purchaseRiskOrder(token ,role ,pageNo,name,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/purchaseRiskOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改销售风险订单的实收款金额
     * @param token
     * @param id 订单id
     * @param money 实收款金额
     * @return
     */
    @RequestMapping("/updateRiskOrderMoney")
    @SysLog(module = "财务管理 " , methods = "修改销售风险订单的实收款金额")
    @ApiOperation(value = "修改销售风险订单的实收款金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "实收款金额" ,paramType = "query")
    })
    public Message updateRiskOrderMoney(String token ,Integer role , Integer id, Double money ){
        Message message = Message.non();
        try {
            Integer count = financeService.updateRiskOrderMoney(id, money );
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改实收款失败");
            }
            return message.code(Message.codeSuccessed).message("修改实收款成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/updateRiskOrderMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改采购风险订单的实收款金额
     * @param token
     * @param id 订单id
     * @param money 实收款金额
     * @return
     */
    @RequestMapping("/updatePurchaseRiskOrderMoney")
    @SysLog(module = "财务管理 " , methods = "修改采购风险订单的实收款金额")
    @ApiOperation(value = "修改销售风险订单的实收款金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "实收款金额" ,paramType = "query")
    })
    public Message updatePurchaseRiskOrderMoney(String token ,Integer role, Integer id, Double money){
        Message message = Message.non();
        try {
            Integer count = financeService.updatePurchaseRiskOrderMoney(id, money );
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改实收款失败");
            }
            return message.code(Message.codeSuccessed).message("修改实收款成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/updatePurchaseRiskOrderMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 收款分类
     * @param token
     * @param type 1销售，2采购
     * @param mold 0（销售或采购），1（销售退货或采购退货）
     * @param startTime 开始时间
     * @param endTime 结算时间
     * @return
     */
    @RequestMapping("/receivablesType")
    @ApiOperation(value = "收款分类" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1销售，2采购" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0（销售或采购），1（销售退货或采购退货）" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "1销售，2采购" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "1销售，2采购" ,paramType = "query")
    })
    public Message receivablesType(String token ,Integer role , Integer type, Integer mold, String startTime, String endTime){
        Message message = Message.non();

        try {

            String start = DateUtil.getDay() ,end = DateUtil.getDay();

            if ( IntegerUtils.isEmpty(startTime ,endTime) ){
                start = startTime; end=endTime;
            }
            if (type ==1 ){
                //销售收款分类
                HashMap hashMap = financeService.saleReceivablesType(token ,role ,mold ,start,end);
                return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
            }else{
                //采购收款分类
                HashMap hashMap = financeService.purchaseReceivablesType(token ,role ,mold,start,end);
                return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
            }
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("财务控制层（/finance/receivablesType）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 最后结算时间
     * @param token
     * @return
     */
    @RequestMapping("/finalSettlementTime")
    @ApiOperation(value = "最后结算时间" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1销售，2采购" ,paramType = "query")
    })
    public Message finalSettlementTime(String token , Integer role , Integer type){
        Message message= Message.non();
        try {
            HashMap hashMap = financeService.finalSettlementTime(token ,role , type);
            if (hashMap == null){
                return message.code(Message.codeSuccessed).data("无结算记录").message("获取成功");
            }
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/finalSettlementTime）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 每天产品库存情况总数（默认当天的）
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/dayGoodsInventorySituationCount")
    @ApiOperation(value = "每天产品库存情况总数（默认当天的）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message dayGoodsInventorySituationCount(String token ,Integer role ){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,  end = DateUtil.getDay();
            Integer count = financeService.dayGoodsInventorySituationCount(token ,role  ,start ,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/dayGoodsInventorySituationCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 每天产品库存情况
     * @param token
     * @param role
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/dayGoodsInventorySituation")
    @ApiOperation(value = "每天产品库存情况" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message dayGoodsInventorySituation(String token ,Integer role ,Integer pageNo ,String name ,String startTime ,String endTime ){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,  end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.dayGoodsInventorySituation(token ,role ,pageNo, name ,start ,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/dayGoodsInventorySituation）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 产品风控总数(默认当天的)
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/goodsWindControlCount")
    @ApiOperation(value = "产品风控总数(默认当天的)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message goodsWindControlCount(String token ,Integer role ){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,  end = DateUtil.getDay();
            Integer count = financeService.goodsWindControlCount(token ,role  ,start ,end);
            return message.code(Message.codeSuccessed).data(count).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/goodsWindControlCount）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 产品风控
     * @param token
     * @param role
     * @param pageNo
     * @param name
     * @return
     */
    @RequestMapping("/goodsWindControl")
    @ApiOperation(value = "产品风控" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message goodsWindControl(String token ,Integer role ,Integer pageNo ,String name ,String startTime ,String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() ,  end = DateUtil.getDay();

            if (IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = financeService.goodsWindControl(token ,role ,pageNo, name  ,start ,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("财务控制层（/finance/goodsWindControl）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 用户销售明细导出
     * @param token
     * @return
     */
    @RequestMapping("/userSaleDetailDownload")
    @ApiOperation(value = "用户销售明细导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "bindindId" ,value = "用户id" ,paramType = "query")
    })
    public void userSaleDetailDownload(String token ,Integer role ,   String name ,String goodsName, String startTime, String endTime ,Integer bindindId , HttpServletResponse response){
        Message message = Message.non();
        String start = DateUtil.getDay()  ,end = DateUtil.getDay();

        if ( IntegerUtils.isEmpty(startTime ,endTime )){
            //当没有传开始时间和结束时间，默认查询当天的订单
            start = startTime; end = endTime;
        }
        HSSFWorkbook workbook = financeService.userSaleDetailDownload(token , role ,name ,goodsName , start ,end ,bindindId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"用户销售明细导出"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 用户销售汇总导出
     * @param token
     * @return
     */
    @RequestMapping("/userSaleSummaryDownload")
    @ApiOperation(value = "用户销售汇总导出" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
            @ApiImplicitParam(name = "bindindId" ,value = "用户id" ,paramType = "query")
    })
    public void userSaleSummaryDownload(String token ,Integer role ,   String name ,String goodsName , String startTime, String endTime ,Integer bindindId , HttpServletResponse response){
        Message message = Message.non();
        String start = DateUtil.getDay()  ,end = DateUtil.getDay();

        if ( IntegerUtils.isEmpty(startTime ,endTime )){
            //当没有传开始时间和结束时间，默认查询当天的订单
            start = startTime; end = endTime;
        }
        HSSFWorkbook workbook = financeService.userSaleSummaryDownload(token , role ,name ,goodsName , start ,end ,bindindId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"用户销售明细导出"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }



}
