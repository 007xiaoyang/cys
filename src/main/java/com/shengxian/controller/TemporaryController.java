package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.WebSocketUtil;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.entity.GoodsCategory;
import com.shengxian.entity.Order;
import com.shengxian.entity.ShoppingHashMap;
import com.shengxian.service.TemporaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 临时用户控制层
 *
 * @Author: yang
 * @Date: 2019-05-11
 * @Version: 1.0
 */
@Api(description = "临时用户控制层")
@RestController
@RequestMapping("/temporary")
public class TemporaryController {



    private Logger log = Logger.getLogger(TemporaryController.class);

    @Resource
    private TemporaryService temporaryService;


    /**
     * 查询店铺类别
     * @param business_id 店铺id
     * @param level 二级类别时传一级类别id
     * @return
     */
    @RequestMapping("/businessCategory")
    @ApiOperation(value = "查询店铺类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
            @ApiImplicitParam(name = "level" ,value = "二级类别时传一级类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "icode" ,value = "产品二维码验证码" ,paramType = "query")
    })
    public Message businessCategory( Integer business_id, Integer level ,Integer icode){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(business_id)){
            return message.code(Message.codeFailured).message("店铺id不能为空");
        }
        if (IntegerUtils.isEmpty(icode)){
            return message.code(Message.codeFailured).message("产品二维码验证码不能为空");
        }

        try {
            List<GoodsCategory> hashMaps = temporaryService.businessCategory(business_id, level , icode);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }


    /**
     * 店铺类别下的产品
     * @param business_id 店铺id
     * @param category_id 类别id
     * @param name 产品名称
     * @return
     */
    @RequestMapping("/businessGoods")
    @ApiOperation(value = "店铺类别下的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query")
    })
    public Message businessGoods(Integer pageNo,Integer business_id ,Integer category_id ,String name){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(business_id)){
            return message.code(Message.codeFailured).message("店铺id不能为空");
        }
        try {
            Page page = temporaryService.businessGoods(pageNo, business_id, category_id, name);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e){
            log.error("temporary/businessGoods"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 加入购物车
     * @param goods_id
     * @param num
     * @return
     */
    @RequestMapping("/addShoppingCart")
    @ApiOperation(value = "加入购物车" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tic" ,value = "临时客户识别码(生成唯一的传到后台)" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "num" ,value = "数量" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1挑数量，2加数量" ,paramType = "query")
    })
    public Message addShoppingCart(String tic , Integer goods_id,double num ,Integer type){
        Message message = Message.non();
        if (!IntegerUtils.isEmpty(tic)){
            return message.code(Message.codeFailured).message("临时客户识别码不能为空");
        }
        if (IntegerUtils.isEmpty(goods_id)){
            return message.code(Message.codeFailured).message("要加入购物车的产品id不能为空");
        }
        if (num == 0.0){
            return message.code(Message.codeFailured).message("数量不能为空");
        }
        try {
            Integer count = temporaryService.addShoppingCart(tic , goods_id, num ,type);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("加入购物车失败");
            }
            return message.code(Message.codeSuccessed).message("加入购物车成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("order/addShoppingCart"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }
    /**
     * 减掉购物车
     * @param tscdId
     * @return
     */
    @RequestMapping("/reduceShoppingCart")
    @ApiOperation(value = "减掉购物车" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tscdId" ,value = "购物车详情id" ,paramType = "query")
    })
    public Message reduceShoppingCart(Integer tscdId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(tscdId)){
            return message.code(Message.codeFailured).message("详情id不能为空");
        }
        try {
            Integer count = temporaryService.reduceShoppingCart( tscdId);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("加入购物车失败");
            }
            return message.code(Message.codeSuccessed).message("加入购物车成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("order/reduceShoppingCart"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 当前临时客户的购物车
     * @param tic
     * @return
     */
  @RequestMapping("/temporaryShoppingcart")
  @ApiOperation(value = "当前临时客户的购物车" ,httpMethod = "POST")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
          @ApiImplicitParam(name = "tic" ,value = "临时客户识别码" ,paramType = "query")
  })
    public synchronized Message temporaryShoppingcart(Integer business_id ,String tic ){
        Message message = Message.non();
        try {
            ShoppingHashMap hashMap = temporaryService.temporaryShoppingcart( business_id , tic);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("order/bindingShoppingcart"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 结算
     * @param tic
     * @param tscdId 购物车详情id
     * @param business_id 店铺id
     * @return
     */
    @RequestMapping("/settlement")
    @ApiOperation(value = "结算" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tic" ,value = "临时客户识别码" ,paramType = "query"),
            @ApiImplicitParam(name = "tscdId" ,value = "购物车详情id" ,paramType = "query"),
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),

    })
    public Message settlement(String tic , String tscdId , Integer business_id){
        Message message = Message.non();
        try{
            HashMap hashMap = temporaryService.settlement(tic ,tscdId ,business_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("结算成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("order/settlement"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 下订单
     * @param json
     * @return
     */
    @RequestMapping("/addOrdre")
    @ApiOperation(value = "下订单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tic" ,value = "临时客户识别码" ,paramType = "query"),
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "{'price':100 ,'beizhu':'测试','orderDetails':[{'goods_id':'756','order_number':'1','order_price':'18'},{'goods_id':'757','order_number':'1','order_price':'19'}]}" ,paramType = "query")
    })
    public synchronized Message addOrder(String tic , Integer business_id , String json){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(business_id)){
            return message.code(Message.codeFailured).message("店铺id不能为空");
        }
        try {
            Order order = JSONObject.parseObject(json, Order.class);
            Integer count = temporaryService.addOrder(tic , business_id ,order);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("下单失败");
            }
            return message.code(Message.codeSuccessed).data(count).message("下单成功");

        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("order/addOrdre"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


}
