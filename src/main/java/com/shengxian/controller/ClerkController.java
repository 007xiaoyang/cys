package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.*;
import com.shengxian.entity.WageSettlement;
import com.shengxian.entity.clerkApp.Calculator;
import com.shengxian.entity.clerkApp.ShoppingMall;
import com.shengxian.entity.clerkApp.ShoppingMallDateil;
import com.shengxian.service.ClerkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 员工APP接口
 *
 * @Author: yang
 * @Date: 2019-04-12
 * @Version: 1.0
 */
@Api(description = "员工APP接口")
@RestController
@RequestMapping("/clerk")
public class ClerkController {

    private static Logger log = Logger.getLogger(ClerkController.class);

    @Autowired
    private ClerkService clerkService;

    /**
     * 查询员工APP版本号
     * @return
     */
    @RequestMapping("/version")
    @ApiOperation(value = "查询员工APP版本号" ,httpMethod = "POST")
    public Message version(){
        Message message = Message.non();
        try {
            String version = clerkService.version();
            return message.code(Message.codeSuccessed).data(version).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/version）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 员工app中判断在是否有退出登录过
     * @param phone 手机号
     * @param model 手机型号
     * @param system  操作系统版本
     * @param version F: 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion  客户端基础库版本
     * @return
     */
    @RequestMapping("/appIsLogin")
    @ApiOperation(value = "员工app中判断在是否有退出登录过" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "存本地token" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "model" ,value = "手机型号" ,paramType = "query"),
            @ApiImplicitParam(name = "system" ,value = "操作系统版本" ,paramType = "query"),
            @ApiImplicitParam(name = "version" ,value = "引擎版本号" ,paramType = "query"),
            @ApiImplicitParam(name = "platform" ,value = "客户端平台" ,paramType = "query"),
            @ApiImplicitParam(name = "SDKVersion" ,value = "客户端基础库版本" ,paramType = "query")
    })
    public Message appIsLogin(String token ,String phone ,String model,String system,String version ,String platform ,String SDKVersion){
        Message message = Message.non();
        try {
            boolean is_boolean = clerkService.appIsLogin(token ,phone , model , system , version , platform , SDKVersion);
            return message.code(Message.codeSuccessed).data(is_boolean).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/appIsLogin）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 修改员工APP手机设备
     * @param token
     * @param model 手机型号
     * @param system 操作系统版本
     * @param version 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion 客户端基础库版本
     * @return
     */
    @RequestMapping("/updateEquipment")
    @ApiOperation(value = "修改员工APP手机设备" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "model" ,value = "手机型号" ,paramType = "query"),
            @ApiImplicitParam(name = "system" ,value = "操作系统版本" ,paramType = "query"),
            @ApiImplicitParam(name = "version" ,value = "引擎版本号" ,paramType = "query"),
            @ApiImplicitParam(name = "platform" ,value = "客户端平台" ,paramType = "query"),
            @ApiImplicitParam(name = "SDKVersion" ,value = "客户端基础库版本" ,paramType = "query")
    })
    public Message updateEquipment(String token ,Integer role ,String model,String system,String version,String platform ,String SDKVersion){
        Message message = Message.non();
        try {
            Integer count = clerkService.updateEquipment(token,model,system,version,platform,SDKVersion);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改手机设备失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/updateEquipment）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 共享订单
     * @param token
     * @return
     */
    @RequestMapping("/sharingOrder")
    @ApiOperation(value = "共享订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或标识码" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单号" ,paramType = "query")
    })
    public Message sharingOrder(String token ,Integer role ,Integer pageNo ,String name , String number){
        Message message = Message.non();
        try {
            Page page  = clerkService.sharingOrder(token ,role ,pageNo , name , number);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/sharingOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * APP中通过订单id查询销售订单详情
     * @param token
     * @param order_id
     * @return
     */
    @RequestMapping("/orderDateil")
    @ApiOperation(value = "APP中通过订单id查询销售订单详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单号" ,paramType = "query")
    })
    public Message orderDateil(String token ,Integer role ,Integer order_id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(order_id)){
            return message.code(Message.codeFailured).message("请输入订单ID");
        }
        try {
            HashMap hashMap = clerkService.orderDetail( order_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/orderDateil）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工APP未到货的订单
     * @param token
     * @param pageNo
     * @param name 订单编号或客户编号或客户名称
     * @return
     */
    @RequestMapping("/noArrivedOrder")
    @ApiOperation(value = "未到货的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "订单编号或客户编号或客户名称" ,paramType = "query")
    })
    public Message noArrivedOrder(String token  ,Integer role ,Integer pageNo,String name){
        Message message = Message.non();
        try {
            Page page = clerkService.noArrivedOrder(token ,role ,pageNo ,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/noArrivedOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 员工APP未收款的订单
     * @param token
     * @param pageNo
     * @param name 搜索条件，订单编号 ，客户名称，客户编号
     * @return
     */
    @RequestMapping("/uncollectedOrderList")
    @ApiOperation(value = "员工APP未收款的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "订单编号或客户编号或客户名称" ,paramType = "query")
    })
    public Message uncollectedOrderList(String token , Integer role ,Integer pageNo,String name ){
        Message message = Message.non();
        try {
            Page page = clerkService.uncollectedOrderList(token ,role , pageNo,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/uncollectedOrderList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工APP欠款的订单
     * @param token
     * @param pageNo
     * @param name 搜索条件，订单编号 ，客户名称，客户编号
     * @return
     */
    @RequestMapping("/arrearsOrderList")
    @ApiOperation(value = "员工APP欠款的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "订单编号或客户编号或客户名称" ,paramType = "query")
    })
    public Message arrearsOrderList(String token , Integer role ,Integer pageNo,String name ){
        Message message = Message.non();
        try {
            Page page = clerkService.arrearsOrderList(token ,role , pageNo,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/arrearsOrderList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工APP完成的订单
     * @param token
     * @param pageNo
     * @param name 搜索条件，订单编号 ，客户名称，客户编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/completeOrderList")
    @ApiOperation(value = "员工APP完成的订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "订单编号或客户编号或客户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query"),
    })
    public Message completeOrderList(String token ,Integer role ,Integer pageNo,String name,String startTime ,String endTime ){
        Message message = Message.non();
        try {
            String start = DateUtil.getDay() , end = DateUtil.getDay();
            if (startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("")){
                start = startTime ; end = endTime;
            }
            Page page = clerkService.completeOrderList(token ,role , pageNo,name ,start , end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/completeOrderList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工app功能列表
     * @param token
     * @return
     */
    @RequestMapping("/staffFunctionList")
    @ApiOperation(value = "员工app功能列表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message staffFunctionList(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = clerkService.staffFunctionList(token);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/staffFunctionList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 客户列表(分页)
     * @param token
     * @param pageNo
     * @param category_id 类别ID
     * @param name 客户名称或编号
     * @return
     */
    @RequestMapping("/bindingUserList")
    @ApiOperation(value = "客户列表(分页)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别ID" ,paramType = "query")
    })
    public Message bindingUserList(String token ,Integer role ,Integer pageNo,Integer category_id ,String name){
        Message message = Message.non();
        try {
            Page page = clerkService.bindingUserList(token ,role , pageNo ,category_id,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/bindingUserList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 供应商列表(分页)
     * @param token
     * @param pageNo
     * @param category_id 类别ID
     * @param name 客户名称或编号
     * @return
     */
    @RequestMapping("/suppliersList")
    @ApiOperation(value = "供应商列表(分页)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别ID" ,paramType = "query")
    })
    public Message suppliersList(String token ,Integer role ,Integer pageNo,Integer category_id ,String name){
        Message message = Message.non();
        try {
            Page page = clerkService.suppliersList(token ,role , pageNo ,category_id,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/suppliersList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 已录入的订单
     * @param token
     * @param role
     * @param mold
     * @param type
     * @return
     */
    @RequestMapping("/haveJoinedShoppingcart")
    @ApiOperation(value = "已录入的订单(销售或采购共用)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0录入订单，1采购入库" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0店铺APP购物车，1员工APP购物车" ,paramType = "query")
    })
    public Message haveJoinedShoppingcart(String token , Integer role,Integer pageNo ,Integer mold ,Integer type){
        Message message = Message.non();
        try {
            Page page = clerkService.haveJoinedShoppingcart(token ,role ,pageNo, mold ,type );
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/haveJoinedShoppingcart）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 通过客户ID查询客户信息
     * @param token
     * @param binding_id 客户id
     * @return
     */
    @RequestMapping("/bindingUserById")
    @ApiOperation(value = "通过客户ID查询客户信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户绑定id" ,paramType = "query")
    })
    public Message bindingUserById(String token  ,Integer role,Integer binding_id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(binding_id)){
            return message.code(Message.codeFailured).message("请输入客户ID");
        }
        try {
            HashMap hashMap = clerkService.bindingUserById(binding_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/bindingUserById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 店铺产品类别
     * @param token
     * @param level 级别 ，0大类
     * @return
     */
    @RequestMapping("/busienssGoodsCategory")
    @ApiOperation(value = "店铺产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "level" ,value = "级别 ，0大类" ,paramType = "query")
    })
    public Message busienssGoodsCategory(String token ,Integer role ,Integer level){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = clerkService.busienssGoodsCategory(token ,role , level);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/busienssGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * app店铺类别下的产品（客户）
     * @param token
     * @param pageNo
     * @param binding_id 客户绑定ID
     * @param category_id 产品类别ID
     * @param name 产品名称或编号
     * @return
     */
    @RequestMapping("/businessGoodsList")
    @ApiOperation(value = "app店铺类别下的产品（客户）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户绑定ID" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "产品类别ID" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
    })
    public Message businessGoodsList(String token ,Integer role ,Integer pageNo ,Integer binding_id,Integer category_id,String name){
        Message message = Message.non();
        try {
            Page page = clerkService.businessGoodsList(token ,role ,pageNo, binding_id, category_id, name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/businessGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * app店铺类别下的产品（供应商）
     * @param token
     * @param pageNo
     * @param suppliersId 供应商ID
     * @param categoryId 产品类别ID
     * @param name 产品名称或编号
     * @return
     */
    @RequestMapping("/businessGoodsListSuppliers")
    @ApiOperation(value = "app店铺类别下的产品（供应商）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliersId" ,value = "供应商ID" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
    })
    public Message businessGoodsListSuppliers(String token ,Integer role ,Integer pageNo ,Integer suppliersId,Integer categoryId,String name){
        Message message = Message.non();
        try {
            Page page = clerkService.businessGoodsListSuppliers(token ,role ,pageNo, suppliersId , categoryId, name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/businessGoodsListSuppliers）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 加入购物车(店铺APP和员工APP共用)
     * @param token
     * @param binding_id 客户绑定ID
     * @param goods_id 产品ID
     * @param type 0销售产品，1赠送产品
     * @param mold 0销售 ，1采购
     * @param price 产品单价
     * @return
     */
    @RequestMapping("/addShoppingMall")
    @ApiOperation(value = "加入购物车(店铺APP和员工APP共用)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户绑定ID" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品ID" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0销售产品，1赠送产品" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售 ，1采购" ,paramType = "query"),
            @ApiImplicitParam(name = "price" ,value = "产品单价" ,paramType = "query"),
            @ApiImplicitParam(name = "num" ,value = "数量" ,paramType = "query")
    })
    public Message addShoppingMall(String token , Integer role ,Integer binding_id,Integer goods_id ,Integer type,Integer mold ,Double price ,Double num){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("请输入产品ID");
        }
        try {
            Integer count = clerkService.addShoppingMall(token ,role ,binding_id,goods_id ,type ,mold ,price ,num);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("加入失败");
            }
            return message.code(Message.codeSuccessed).message("成功加入");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/addShoppingMall）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 减掉购物车
     * @param token
     * @param sm_id 购物车ID
     * @param smd_id 购物车详情ID
     * @return
     */
    @RequestMapping("/reduceShoppingMall")
    @ApiOperation(value = "减掉购物车" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query"),
            @ApiImplicitParam(name = "smd_id" ,value = "购物车详情ID" ,paramType = "query")
    })
    public Message reduceShoppingMall(String token , Integer role,Integer sm_id ,Integer smd_id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(sm_id) || IntegerUtils.isEmpty(smd_id)){
            return message.code(Message.codeFailured).message("购物车ID不能为空");
        }
        try {
            Integer count = clerkService.reduceShoppingMall(sm_id,smd_id);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("减掉购物车失败");
            }
            return message.code(Message.codeSuccessed).message("减成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/reduceShoppingMall）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除购物车产品
     * @param token
     * @param sm_id 购物车ID
     * @param smd_id 购物车详情ID
     * @return
     */
    @RequestMapping("/deleteShoppingMallDateil")
    @ApiOperation(value = "删除购物车产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query"),
            @ApiImplicitParam(name = "smd_id" ,value = "购物车详情ID" ,paramType = "query")
    })
    public Message deleteShoppingMallDateil(String token , Integer role,Integer sm_id ,Integer smd_id ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(sm_id) || IntegerUtils.isEmpty(smd_id)){
            return message.code(Message.codeFailured).message("购物车ID不能为空");
        }
        try {
            Integer count = clerkService.deleteShoppingMallDateil(sm_id,smd_id );
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("删除购物车产品失败");
            }
            return message.code(Message.codeSuccessed).message("删除购物车产品成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/deleteShoppingMallDateil）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 购物车总数和总金额(销售和采购共用,根据mold来区分是销售还是采购)
     * @param token
     * @param binding_id
     * @param mold 0销售 ，1采购
     * @return
     */
    @RequestMapping("/currentBindingShoppingMallCountAndMoney")
    @ApiOperation(value = "购物车总数和总金额(销售和采购共用,根据mold来区分是销售还是采购)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "绑定客户id" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售 ，1采购" ,paramType = "query")
    })
    public Message currentBindingShoppingMallCount(String token ,Integer role ,Integer binding_id ,Integer mold){
        Message message = Message.non();
        try {
            HashMap hashMap = clerkService.currentBindingShoppingMallCountAndMoney(token , role ,binding_id ,mold);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/currentBindingShoppingMallCountAndMoney）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 客户的购物车产品列表
     * @param token
     * @param sm_id 购物车ID
     * @return
     */
    @RequestMapping("/currentBindingShoppingMallGoodsList")
    @ApiOperation(value = "客户的购物车产品列表" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query")
    })
    public Message currentBindingShoppingMallGoodsList(String token ,Integer role  ,Integer sm_id){
        Message message = Message.non();
        try {
            List<ShoppingMallDateil> hashMaps = clerkService.currentBindingShoppingMallGoodsList(sm_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/currentBindingShoppingMallGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 结算
     * @param token
     * @param sm_id 购物车ID
     * @return
     */
    @RequestMapping("/settlement")
    @ApiOperation(value = "结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query")
    })
    public Message settlement(String token ,Integer role , Integer sm_id){
        Message message = Message.non();
        try {
            ShoppingMall mall = clerkService.settlement(sm_id);
            if (mall == null){
                return message.code(Message.codeFailured).message("结算失败");
            }
            return message.code(Message.codeSuccessed).data(mall).message("结算成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/settlement）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 销售下订单
     * @param token
     * @param sm_id 购物车ID
     * @param freight 运费
     * @param difference_price 差价
     * @param beizhu 备注
     * @param print 0未打印，1打印
     * @return
     */
    @RequestMapping("/addOrder")
    @ApiOperation(value = "销售下订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query"),
            @ApiImplicitParam(name = "freight" ,value = "运费" ,paramType = "query"),
            @ApiImplicitParam(name = "difference_price" ,value = "差价" ,paramType = "query"),
            @ApiImplicitParam(name = "beizhu" ,value = "备注" ,paramType = "query"),
            @ApiImplicitParam(name = "print" ,value = "0未打印，1打印" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售单，1退货单" ,paramType = "query")
    })
    public Message addOrder(String token ,Integer role ,Integer sm_id,double freight ,double difference_price,String beizhu ,Integer print , Integer mold){
        Message message = Message.non();
        try {
            Integer count = clerkService.addOrder(token ,role ,sm_id ,freight ,difference_price ,beizhu ,print ,mold);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("下单失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("下单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/addOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 店铺收藏客户的产品(分页)
     * @param token
     * @param pageNo
     * @param bindingId 供应商ID
     * @param name 产品名称
     * @return
     */
    @RequestMapping("/bindingCollectionBindingGoodsList")
    @ApiOperation(value = "店铺收藏客户的产品(分页)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "bindingId" ,value = "供应商ID" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称" ,paramType = "query")
    })
    public Message bindingCollectionBindingGoodsList(String token ,Integer role  ,Integer pageNo,Integer bindingId,String name ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(bindingId)){
            return message.code(Message.codeFailured).message("请输入客户ID");
        }
        try {
            Page page = clerkService.bindingCollectionBindingGoodsList(token ,role ,pageNo,bindingId ,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/bindingCollectionSuppliersGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 店铺收藏供应商的产品(分页)
     * @param token
     * @param pageNo
     * @param suppliersId 供应商ID
     * @param name 产品名称
     * @return
     */
    @RequestMapping("/bindingCollectionSuppliersGoodsList")
    @ApiOperation(value = "店铺收藏供应商的产品(分页)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliers_id" ,value = "供应商ID" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称" ,paramType = "query")
    })
    public Message bindingCollectionSuppliersGoodsList(String token ,Integer role  ,Integer pageNo,Integer suppliersId,String name ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(suppliersId)){
            return message.code(Message.codeFailured).message("请输入供应商ID");
        }
        try {
            Page page = clerkService.bindingCollectionSuppliersGoodsList(token ,role ,pageNo,suppliersId ,name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/bindingCollectionSuppliersGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 采购下订单
     * @param token
     * @param sm_id 购物车ID
     * @param freight 运费
     * @param difference_price 差价
     * @param beizhu 备注
     * @param print 0未打印，1打印
     * @return
     */
    @RequestMapping("/addPurchase")
    @ApiOperation(value = "采购下订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sm_id" ,value = "购物车ID" ,paramType = "query"),
            @ApiImplicitParam(name = "freight" ,value = "运费" ,paramType = "query"),
            @ApiImplicitParam(name = "difference_price" ,value = "差价" ,paramType = "query"),
            @ApiImplicitParam(name = "beizhu" ,value = "备注" ,paramType = "query"),
            @ApiImplicitParam(name = "print" ,value = "0未打印，1打印" ,paramType = "query"),
            @ApiImplicitParam(name = "mold" ,value = "0销售单，1退货单" ,paramType = "query")
    })
    public Message addPurchase(String token ,Integer role  ,Integer sm_id,double freight ,double difference_price,String beizhu ,Integer print ,Integer mold){
        Message message = Message.non();
        try {
            Integer count = clerkService.addPurchase(token ,role ,sm_id ,freight ,difference_price ,beizhu ,print,mold);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("下单失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("下单成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/addPurchase）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 销售收款(默认进来数据不加载，通过扫一扫或搜索)
     * @param token
     * @param pageNo
     * @param name 客户名称或客户编号或标识码
     * @param number 订单编号
     * @return
     */
    @RequestMapping("/saleReceivables")
    @ApiOperation(value = "销售收款(默认进来数据不加载，通过扫一扫或搜索)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "客户名称或客户编号或标识码" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query")
    })
    public Message saleReceivables(String token ,Integer role  ,Integer pageNo ,String name ,String number){
        Message message= Message.non();
        try {
            Page page = clerkService.saleReceivables(token ,role ,pageNo ,name ,number);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/saleReceivables）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 我的信息
     * @param token
     * @return
     */
    @RequestMapping("/myInfo")
    @ApiOperation(value = "我的信息)" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message myInfo(String token ,Integer role ){
        Message message = Message.non();
        try {
            HashMap hashMap = clerkService.myInfo(token ,role);
            if (hashMap == null ){
                return message.code(Message.codeFailured).message("登录失效");
            }
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/myInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 我的工资今日结算
     * @param token
     * @return
     */
    @RequestMapping("/mySalary")
    @ApiOperation(value = "我的工资今日结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message mySalary(String token ,Integer role  ){
        Message message = Message.non();
        try {
            HashMap hashMap = clerkService.mySalary(token ,role );
            if (hashMap == null ){
                return message.code(Message.codeFailured).message("登录失效");
            }
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/mySalary）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 我的工资本月结算
     * @param token
     * @return
     */
    @RequestMapping("/wageSettlement")
    @ApiOperation(value = "我的工资本月结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message wageSettlement(String token ,Integer role  ){
        Message message = Message.non();
        try {
            List<WageSettlement> hashMaps = clerkService.wageSettlement(token ,role  );
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/mySalary）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 提成分类
     * @param token
     * @return
     */
    @RequestMapping("/percentageClassification")
    @ApiOperation(value = "提成分类" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1产品 ，2客户，3重量，4车次，5送达，6金额，7开单，8采购数量，9采购重量 ，10采购金额" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束" ,paramType = "query")
    })
    public Message percentageClassification(String token ,Integer role ,Integer pageNo  ,Integer type ,String startTime ,String endTime){
        Message message = Message.non();
        try {
            String start = DateUtil.thisMonthOneNum();
            String end = DateUtil.getDay();
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = clerkService.percentageClassification(token ,role ,pageNo ,type ,start ,end );
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/percentageClassification）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 工作记录
     * @param token
     * @return
     */
    @RequestMapping("/payrollRecords")
    @ApiOperation(value = "工作记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束" ,paramType = "query")
    })
    public Message payrollRecords(String token ,Integer role ,Integer pageNo  ,String startTime ,String endTime){
        Message message = Message.non();
        try {
            Page page = clerkService.payrollRecords(token ,role ,pageNo ,startTime ,endTime );
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/payrollRecords）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 店铺或者员工APP修改密码
     * @param token
     * @param role
     * @param password
     * @param password2
     * @return
     */
    @RequestMapping("/updatePassword")
    @ApiOperation(value = "店铺或者员工APP修改密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "旧密码" ,paramType = "query"),
            @ApiImplicitParam(name = "password2" ,value = "新密码" ,paramType = "query")
    })
    public Message updatePassword(String token ,Integer role ,String password ,String password2){
        Message message = Message.non();
        try {
            Integer count = clerkService.updatePassword(token ,role ,password ,password2  );
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/updatePassword）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 客户账单
     * @param role
     * @return
     */
    @RequestMapping("/shareUserOrder")
    @ApiOperation(value = "客户账单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "查全部不传，查未完成的传1" ,paramType = "query"),
            @ApiImplicitParam(name = "bindingID" ,value = "客户id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message shareUserOrder(String token , Integer role ,Integer pageNo,Integer type ,Integer bindingID ,String name ,String goodsName ,String startTime , String endTime ){
        Message message= Message.non();
        String start = DateUtil.getDay();  String end = DateUtil.getDay();
        try {
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = clerkService.shareUserOrder(token ,role ,pageNo ,type ,bindingID ,name ,goodsName ,start  ,end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/shareUserOrder）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 客户账单分享到微信上
     * @return
     */
    @RequestMapping("/shareWXRecord")
    @ApiOperation(value = "客户账单分享到微信上" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "查全部不传，查未完成的传1" ,paramType = "query"),
            @ApiImplicitParam(name = "bindingID" ,value = "客户id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message shareWXRecord(String token, Integer role ,Integer type ,Integer bindingID ,String name ,String goodsName,String startTime , String endTime ){
        Message message= Message.non();
        if (IntegerUtils.isEmpty(bindingID)){
            return message.code(Message.codeFailured).message("要指定客户才能分享～");
        }
        try {
            String count = clerkService.shareWXRecord(token ,role ,type ,bindingID ,name ,goodsName,startTime  ,endTime);
            return message.code(Message.codeSuccessed).data(count).message("分享成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/shareWXRecord）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 查看分享到微信的客户账单信息
     * @return
     */
    @RequestMapping("/selectWXShareRecord")
    @ApiOperation(value = "查看分享到微信的客户账单信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shareID" ,value = "分享id" ,paramType = "query")
    })
    public Message selectUserWXShareRecord(String shareID){
        Message message= Message.non();

        try {
            //客户账单的分享
            HashMap hashMap = clerkService.userWXShareRecord(shareID);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/selectWXShareRecord）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查看分享到微信的订单信息
     * @return
     */
    @RequestMapping("/orderWXShareRecord")
    @ApiOperation(value = "查看分享到微信的订单信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shareID" ,value = "分享id" ,paramType = "query")
    })
    public Message orderWXShareRecord(Integer  orderID){
        Message message= Message.non();

        try {
            HashMap hashMap = clerkService.orderWXShareRecord(orderID);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (Exception e){
            log.error("员工APP控制层（/clerk/orderWXShareRecord）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 员工APP提成标题提醒
     * @return
     */
    @RequestMapping("/ritleReminder")
    @ApiOperation(value = "员工APP提成标题提醒" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),

    })
    public synchronized Message ritleReminder(String token ,Integer role){
        Message message= Message.non();
        List list = clerkService.ritleReminder(token , role );
        return message.code(Message.codeSuccessed).data(list).message("查询成功");
    }

    /**
     * 采购产品汇总
     * @param token
     * @param role
     * @param suppliersName
     * @param goodsName
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/purchaseGoodsSummary")
    @ApiOperation(value = "采购产品汇总" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliersName" ,value = "供应商名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message purchaseGoodsSummary(String token ,Integer role ,Integer pageNo,String suppliersName,String goodsName ,String startTime ,String endTime){
        Message message =Message.non();
        String start = DateUtil.getDay();  String end = DateUtil.getDay();
        try {
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }

            Page page = clerkService.purchaseGoodsSummary(token, role, pageNo, suppliersName, goodsName, start, end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeSuccessed).message(Global.ERROR);
        }
    }

    /**
     * 采购产品明细
     * @param token
     * @param role
     * @param suppliersName
     * @param goodsName
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/purchaseGoodsDetails")
    @ApiOperation(value = "采购产品明细" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "suppliersName" ,value = "供应商名称" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message purchaseGoodsDetails(String token ,Integer role ,Integer pageNo ,String suppliersName,String goodsName ,String startTime ,String endTime){
        Message message =Message.non();
        String start = DateUtil.getDay();  String end = DateUtil.getDay();
        try {
            if ( IntegerUtils.isEmpty(startTime ,endTime)){
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = clerkService.purchaseGoodsDetails(token, role, pageNo, suppliersName, goodsName, start, end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeSuccessed).message(Global.ERROR);
        }
    }

    /**
     * 计算器
     * @param token
     * @param role
     * @param json
     * @return
     */
    @RequestMapping("/addCalculator")
    @ApiOperation(value = "计算器" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "{'name':'张三' , 'price': 30 , 'tatol': 3 ,'calculatorDatell':[{'num':2},{'num':23} ,{'num':5}]}" ,paramType = "query")
    })
    public Message addCalculator(String token , Integer role , String json){
        Message message = Message.non();
        try {
            Calculator calculator = JSONObject.parseObject(json, Calculator.class);
            Integer count = clerkService.addCalculator(token , role , calculator);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询计算器
     * @param token
     * @param role
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/selectCalculator")
    @ApiOperation(value = "查询计算器" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message selectCalculator(String token , Integer role ,Integer pageNo ,String name , String startTime ,String endTime){
        Message message = Message.non();
        String start = DateUtil.getDay();  String end = DateUtil.getDay();
        try {
            if (IntegerUtils.isEmpty(startTime, endTime)) {
                //当没有传开始时间和结束时间，默认查询当天的订单
                start = startTime; end = endTime;
            }
            Page page = clerkService.selectCalculator(token , role, pageNo, name, start, end);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeSuccessed).message(Global.ERROR);
        }
    }

    /**
     * 查询计算器详情
     * @param calculatorId
     * @return
     */
    @RequestMapping("/selectCalculatorDateilById")
    @ApiOperation(value = "查询计算器详情" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "calculatorId" ,value = "计算器id" ,paramType = "query")
    })
    public Message selectCalculatorDateilById(Integer calculatorId){
        Message message = Message.non();
        List<HashMap> hashMaps = clerkService. selectCalculatorDateilById( calculatorId);
        return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
    }


    /**
     * IP
     * @return
     */
    @RequestMapping("/IP")
    @ApiOperation(value = "IP" ,httpMethod = "POST")
    public Message IP(HttpServletRequest request , HttpServletResponse response){
        Message message= Message.non();
        String uri = request.getRequestURI();//返回请求行中的资源名称
        String url = request.getRequestURL().toString();//获得客户端发送请求的完整url
        String ip = request.getRemoteAddr();//返回发出请求的IP地址
        String cid = CusAccessObjectUtil.getClientIpAddress(request);
        String host=request.getRemoteHost();//返回发出请求的客户机的主机名
        int port =request.getRemotePort();//返回发出请求的客户机的端口号。
        HashMap hashMap = new HashMap();
        hashMap.put("返回请求行中的资源名称 uri:" ,uri);
        hashMap.put("获得客户端发送请求的完整url url:" ,url);
        hashMap.put("返回发出请求的IP地址 ip:" ,ip);
        hashMap.put("返回发出请求的IP地址 cidip:" ,cid);
        hashMap.put("返回发出请求的客户机的主机名 host:" ,host);
        hashMap.put("返回发出请求的客户机的端口号 port:" ,port);
      return message.code(Message.codeSuccessed).data(hashMap);
    }

}
