package com.shengxian.controller;

import com.shengxian.common.Message;
import com.shengxian.common.util.*;
import com.shengxian.service.DistributeService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 配送管理
 *
 * @Author: yang
 * @Date: 2018-12-07
 * @Version: 1.0
 */
@Api(description = "配送管理")
@RestController
@RequestMapping("/distribute")
public class DistributeController {

    @Resource
    private DistributeService distributeService;

    private static Logger log =Logger.getLogger(DistributeController.class);

    /**
     * 所有未到货的产品
     * @param token
     * @param pageNo
     * @param type 1汇总，2明细
     * @param wid 仓库id
     * @return
     */
    @RequestMapping("/allNotArrivalOrder")
    @ApiOperation(value = "所有未到货的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1汇总，2明细" ,paramType = "query"),
            @ApiImplicitParam(name = "wid" ,value = "仓库id" ,paramType = "query")
    })
    public Message allNotArrivalOrder(String token ,Integer role, Integer pageNo, Integer type, Integer wid){
        Message message = Message.non();
        Page page=null;
        try {
            if (type == 1){
                page =   distributeService.allNotArrivalGoodsSummary(token ,role ,pageNo,wid);
            }else {
                page =   distributeService.allNotArrivalGoodsDetail(token , role ,pageNo,wid);
            }
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/allNotArrivalOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 未分配给员工派送的订单
     * @param token
     * @return
     */
    @RequestMapping("/notDistributeOrder")
    @ApiOperation(value = "未分配给员工派送的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query")
    })
    public Message notDistributeOrder(String token ,Integer role , String name, String number){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMaps = distributeService.notDistributeOrder(token ,role ,names,number);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/notDistributeOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 分配订单给员工配送
     * @param token
     * @param order_id
     * @param staff_id
     * @return
     */
    @RequestMapping("/distributeOrderToStaff")
    @SysLog(module = "配送管理" ,methods = "分配订单给员工配送")
    @ApiOperation(value = "分配订单给员工配送" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message distributeOrderToStaff(String token ,Integer role , String order_id, Integer staff_id){
        Message message = Message.non();
        try {
            Integer count = distributeService.distributeOrderToStaff(token ,role , order_id, staff_id);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("指定员工派送失败");
            }
            return message.code(Message.codeSuccessed).message("指定员工派送成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/distributeOrderToStaff）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 已经分配给员工配送的订单
     * @param token
     * @param pageNo
     * @param status 3分配未到货的订单，4到货的订单
     * @param staff_id 员工id
     * @return
     */
    @RequestMapping("/alreadyDistributeOrder")
    @ApiOperation(value = "已经分配给员工配送的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "3分配未到货的订单，4到货的订单" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message alreadyDistributeOrder(String token ,Integer role, Integer pageNo, Integer status, Integer staff_id){
        Message message = Message.non();
        try {
            Page page = distributeService.alreadyDistributeOrder(token ,role ,pageNo,status,staff_id);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/alreadyDistributeOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过配送id查询配送订单详情
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findDistributeOrderDetail")
    @ApiOperation(value = "通过配送id查询配送订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "配送id" ,paramType = "query"),
            @ApiImplicitParam(name = "is_del" ,value = "配送id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "3分配未到货的订单，4到货的订单" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query")
    })
    public Message findDistributeOrderDetail(String token ,Integer role , Integer id, Integer is_del, Integer status , String number){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = distributeService.findDistributeOrderDetail(id, is_del, status, number);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/findDistributeOrderDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过配送id查询配送订单详情不同状态的总金额
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findDistributeOrderDetailTotalMoney")
    @ApiOperation(value = "通过配送id查询配送订单详情不同状态的总金额" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "配送id" ,paramType = "query"),
            @ApiImplicitParam(name = "is_del" ,value = "配送id" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "3分配未到货的订单，4到货的订单" ,paramType = "query"),
    })
    public Message findDistributeOrderDetailTotalMoney(String token ,Integer role , Integer id , Integer is_del , Integer status){

        Message message = Message.non();
        try {
            HashMap hashMap = distributeService.findDistributeOrderDetailTotalMoney(id, is_del, status);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/findDistributeOrderDetailTotalMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 员工配送完成的订单
     * @param token
     * @param pageNo
     * @param type 0 或不传
     * @param status 传1 或不传
     * @param staff_id 员工id
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/saleDistributeCompletOrder")
    @ApiOperation(value = "员工配送完成的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0 或不传" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "传1 或不传" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "传1 或不传" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "传1 或不传" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "传1 或不传" ,paramType = "query")
    })
    public Message completeDistributeOrder(String token ,Integer role , Integer pageNo, Integer type, Integer status, Integer staff_id, String startTime, String endTime){
        Message message = Message.non();
        String start = DateUtil.getDay() ,end = DateUtil.getDay();
        if ( startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("") ){
            start = startTime;end=endTime;
        }
        try {
            Page page = distributeService.saleDistributeCompletOrderSummary(token ,role ,pageNo,staff_id,type,status,start,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/completeDistributeOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }









    /**
     * 员工信息
     * @return
     */
    @RequestMapping("/staffTatolDetail")
    @ApiOperation(value = "员工信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message staffTatolDetail(String token ,Integer role , Integer pageNo, Integer staff_id){
        Message message = Message.non();
        try {
            Page page = distributeService.staffTatolDetail(token , role ,pageNo,staff_id);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/staffTatolDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 统计员工每月订单
     * @return
     */
    @RequestMapping("/staffMonthOrder")
    @ApiOperation(value = "统计员工每月订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message staffMonthOrder(String token ,Integer role , Integer pageNo, Integer staff_id){
        Message message = Message.non();
        try {
            Page page = distributeService.staffMonthOrder(token ,role ,pageNo ,staff_id);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/staffMonthOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工工资结算
     * @param token
     * @param staff_id
     * @param money
     * @param time
     * @return
     */
    @RequestMapping("/addStaffWageSettlement")
    @SysLog(module = "员工管理" ,methods = "员工工资结算")
    @ApiOperation(value = "员工工资结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "money" ,value = "结算金额" ,paramType = "query"),
            @ApiImplicitParam(name = "time" ,value = "结算时间" ,paramType = "query"),
    })
    public Message addStaffWageSettlement(String token ,Integer role , Integer staff_id, Double money, String time){
        Message message = Message.non();
        try {
            Integer count = distributeService.addStaffWageSettlement(staff_id,money,time);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("结算失败");
            }
            return message.code(Message.codeSuccessed).message("结算成功");
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/addStaffWageSettlement）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工提成订单明细
     * @param token
     * @param staff_id
     * @param time
     * @return
     */
    @RequestMapping("/staffDayDetail")
    @ApiOperation(value = "员工提成订单明细" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "time" ,value = "时间" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1产品，2客户,3重量,4车次,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "" ,paramType = "query")
    })
    public Message staffDayDetail(String token ,Integer role , Integer pageNo, Integer staff_id , String time,String name ,Integer type ,String startTime ,String endTime){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {

            Page page = distributeService.staffDayDetail(pageNo,staff_id,time ,names ,type ,startTime , endTime);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/staffDayDetail）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *  员工订单
     * @param token
     * @param pageNo
     * @param staff_id
     * @param time
     * @return
     */
    @RequestMapping("/salePurchaseOrder")
    @ApiOperation(value = "员工订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "time" ,value = "时间" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0销售 ，1采购" ,paramType = "query")
    })
    public Message salePurchaseOrder(String token ,Integer role , Integer pageNo, Integer staff_id , String time ,String name ,Integer type){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            Page page = distributeService.salePurchaseOrder(pageNo,staff_id,time ,names ,type );
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("配送管理控制层（/distribute/salePurchaseOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

}
