package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.Sendsms;
import com.shengxian.common.util.DateUtil;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.entity.*;
import com.shengxian.service.ShopService;
import com.shengxian.service.ExcelService;
import com.shengxian.service.LogService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 店铺控制层
 *
 * @Author: yang
 * @Date: 2019-03-27
 * @Version: 1.0
 */
@Api(description = "登录")
@RestController
@RequestMapping("/business")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private LogService logService;

    @Autowired
    private ExcelService excelService;

    private Logger log = Logger.getLogger(ShopController.class);
    /**
     * 获取验证码
     * @param phone
     * @return
     */
    @RequestMapping("/sendSms")
    @ApiOperation(value = "获取验证码" ,httpMethod = "POST")
    @ApiImplicitParam(name = "phone" ,value = "手机号码" ,paramType = "query")
    public Message sendSms(String phone){
        Message message = Message.non();
        if (phone == null || "".equals(phone)){
            return message.code(Message.codeFailured).message("请输入手机号");
        }
        try {
            Integer code = Sendsms.sendSMS(phone);
            if (code == null){
                return message.code(Message.codeFailured).message("获取验证码失败");
            }
            return message.code(Message.codeSuccessed).data(code).message("获取验证码成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return message.code(Message.codeFailured).message(Global.ERROR);
    }

    /**
     * 注册
     * @param storeName 店名
     * @param phone 手机号
     * @param password 密码
     * @param invitation 邀请人号码
     * @return
     */
    @RequestMapping("/register")
    @ApiOperation(value = "注册" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeName" ,value = "店铺名称" ,paramType = "query"),
            @ApiImplicitParam(name = "phoen" ,value = "密码" ,paramType = "query"),
            @ApiImplicitParam(name = "invitation" ,value = "邀请人号码" ,paramType = "query")
    })
    public Message register(String storeName, String phone,String password,String invitation){
        Message message = Message.non();
        if (StringUtils.isEmpty(storeName)){
            return message.code(Message.codeFailured).message("请输入店名");
        }
        if (StringUtils.isEmpty(phone)){
            return message.code(Message.codeFailured).message("请输入手机号");
        }
        if ( StringUtils.isEmpty(password)) {
            return message.code(Message.codeFailured).message("请输入密码");
        }
        try {
            //注册店铺
            Integer count = shopService.addBusiness(storeName,phone,password,invitation);

            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("注册失败");
            }

            return message.code(Message.codeSuccessed).message("注册成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return message.code(Message.codeFailured).message(Global.ERROR);
    }


    /**
     * 用户登录
     * @param role 1服务商，2用户
     * @param phone 手机号
     * @param password 密码
     * @return
     */
    @RequestMapping("/login")
    @ApiOperation(value = "用户登录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role" ,value = "角色" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号码" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "登录密码" ,paramType = "query")
    })
    public Message login(Integer role , String phone, String password, HttpServletRequest request, HttpServletResponse response){
        Message message = Message.non();
        if (StringUtils.isEmpty(phone)){
            return message.code(Message.codeFailured).message("请输入账号");
        }
        if (StringUtils.isEmpty(password)){
            return message.code(Message.codeFailured).message("请输入账号密码");
        }
        if (!(role == 1 || role ==2)){
            return message.code(Message.codeFailured).message("角色只要服务商或者员工");
        }
        try {
            String token = shopService.login(role,phone, password);
            return message.code(Message.codeSuccessed).data(token).message("登录成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("店铺控制层（/business/login）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * pc端首页
     * @param token
     * @return
     */
    @RequestMapping("/home")
    @ApiOperation(value = "pc端首页" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message home( String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = shopService.home(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("店铺控制层（/business/home）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 找回密码
     * @param phone
     * @return
     */
    @RequestMapping("retrievePwd")
    @ApiOperation(value = "找回密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone" ,value = "手机号码" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "新密码" ,paramType = "query")
    })
    public Message RetrievePwd(String phone,String password){
        Message message = Message.non();

        if ( StringUtils.isEmpty(phone)){
            return message.code(Message.codeFailured).message("请输入手机号");
        }

        try {
            Integer count = shopService.updateRetrievePwd(phone,password);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("找回密码失败");
            }
            return message.code(Message.codeSuccessed).message("找回密码成功");
        } catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        } catch (Exception e) {
            log.error("店铺控制层（/business/RetrievePwd）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加产品类别
     * @param token
     * @param name
     * @param level 1级类别ID
     * @return
     */
    @RequestMapping("addGoodsCategory")
    @SysLog(module = "资料管理", methods = "添加产品类别")
    @ApiOperation(value = "添加产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称" ,paramType = "query"),
            @ApiImplicitParam(name = "level" ,value = "级别，一级传0，二级传一级ID" ,paramType = "query")
    })
    public Message addGoodsCategory(String token ,Integer role ,String name,Integer level){
        Message message = Message.non();
        try {
            if (name == null || "".equals(name)) {
                return message.code(Message.codeFailured).message("请输入商品类别名称");
            }
            Integer count = shopService.addGoodsCategory(token, role, name, level);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询店铺下的产品类别集合
     * @param token
     * @return
     */
    @RequestMapping("/findGoodsCategoryList")
    @ApiOperation(value = "查询店铺下的产品类别集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message findGoodsCategoryList(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> goodsCategory = shopService.findGoodsCategoryList(token ,role);
            return message.code(Message.codeSuccessed).data(goodsCategory).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/findGoodsCategoryList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 分页查询类别下的产品信息
     * @param token
     * @param pageNo
     * @param categoryId 产品类别ID
     * @param number 产品编号
     * @param name 产品名称
     * @param barcode 条码
     * @return
     */
    @RequestMapping("/findGoodsList")
    @ApiOperation(value = "分页查询类别下的产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "barcode" ,value = "条码" ,paramType = "query")
    })
    public Message findGoodsList(String token,Integer role ,Integer pageNo ,Integer categoryId,String number,String name,Long barcode ){
        Message message = Message.non();
        try {
            Page page = shopService.findGoodsList(token , role ,pageNo,categoryId ,number ,name,barcode);
            return message.code(Message.codeSuccessed).data(page).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/findGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改产品类别
     * @param token
     * @param categoryId
     * @return
     */
    @RequestMapping("/updateGoodsCategory")
    @SysLog(module = "资料管理", methods = "修改产品类别")
    @ApiOperation(value = "修改产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品类别名称" ,paramType = "query")
    })
    public Message updateGoodsCategory(String token,Integer role,Integer categoryId,String name){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(categoryId)) {
                return message.code(Message.codeFailured).message("请输入产品类别id");
            }
            if (StringUtils.isEmpty(name)) {
                return message.code(Message.codeFailured).message("请输入类别名称");
            }
            Integer count = shopService.updateGoodsCategory(categoryId, name);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("修改产品类别失败");
            }
            return message.code(Message.codeSuccessed).message("修改产品类别成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除产品类别
     * @param token
     * @param categoryId
     * @return
     */
    @RequestMapping("/deleteGoodsCategory")
    @ResponseBody
    @SysLog(module = "资料管理", methods = "删除产品类别")
    @ApiOperation(value = "删除产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query")
    })
    public Message deleteGoodsCategory(String token ,Integer role,Integer categoryId){
        Message message = Message.non();
        try {
            if (categoryId == null || categoryId.equals(0)) {
                return message.code(Message.codeFailured).message("请输入产品类别id");
            }
            Integer count = shopService.deleteGoodsCategory(categoryId);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("删除产品类别失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 产品大类别置顶
     * @param token
     * @param role
     * @param category_id 产品类别ID
     * @param status  0默认，1置顶
     * @return
     */
    @RequestMapping("/upateCategoryRP")
    @SysLog(module = "资料管理", methods = "产品大类别置顶")
    @ApiOperation(value = "删除产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query"),
            @ApiImplicitParam(name = "status" ,value = "0默认，1置顶" ,paramType = "query")
    })
    public Message upateCategoryRP(String token, Integer role ,Integer category_id ,Integer status){
        Message message = Message.non();
        if (category_id == null || category_id.equals(0)){
            return message.code(Message.codeFailured).message("请输入产品类别id");
        }
        try {
            Integer count = shopService.upateCategoryRP(category_id ,status);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("置顶失败");
            }
            return message.code(Message.codeSuccessed).message("置顶成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/upateCategoryRP）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 挑选产品类别id自动生成产品编号
     * @param token
     * @param categoryId
     * @return
     */
    @RequestMapping("/automaticGoodsNumberByCategoryId")
    @ApiOperation(value = "挑选产品类别id自动生成产品编号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "产品类别ID" ,paramType = "query")
    })
    public Message findGoodsNumberByCategoryId(String token , Integer role,Integer categoryId){
        Message message = Message.non();
        if (categoryId == null || categoryId == 0){
            return  message.code(Message.codeFailured).message("请输入产品类别id");
        }
        String number = shopService.automaticGoodsNumberByCategoryId(categoryId);
        if (number == null || "".equals(number)){
            return message.code(Message.codeFailured).message("获取产品编号失败");
        }
        return message.code(Message.codeSuccessed).data(number).message("获取产品编号成功");
    }

    /**
     * 查询店铺方案菜单
     * @return
     */
    @RequestMapping("/scheme")
    @ApiOperation(value = "查询店铺方案菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message scheme(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMap = shopService.scheme(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/scheme）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加商家菜单方案
     * @param token
     * @return
     */
    @RequestMapping("/addBusinessScheme")
    @SysLog(module = "资料管理", methods = "添加商家价格方案")
    @ApiOperation(value = "添加商家菜单方案" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "方案名称" ,paramType = "query")
    })
    public Message addBusinessScheme(String token ,Integer role ,String name){
        Message message  = Message.non();
        try {
            if (name == null || "".equals(name)) {
                return message.code(Message.codeFailured).message("请输入商家菜单方案名称");
            }
            Integer count = shopService.addBusinessScheme(token, role, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addBusinessScheme）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改商家菜单方案
     * @param token
     * @param bsid
     * @return
     */
    @RequestMapping("/updateBusinessScheme")
    @SysLog(module = "资料管理", methods = "修改商家价格方案")
    @ApiOperation(value = "修改商家菜单方案" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "bsid" ,value = "方案菜单ID",paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "方案名称" ,paramType = "query")
    })
    public Message updateBusinessScheme(String token ,Integer role ,Integer bsid,String name){
        Message message  = Message.non();
        if (IntegerUtils.isEmpty(bsid)){
            return message.code(Message.codeFailured).message("请输入商家菜单方案ID");
        }
        if (StringUtils.isEmpty(name) ){
            return message.code(Message.codeFailured).message("请输入商家菜单方案名称");
        }
        try {
            Integer count = shopService.updateBusinessScheme(bsid, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateBusinessScheme）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 添加产品信息
     * @return
     */
    @RequestMapping("/addGoods")
    @SysLog(module = "资料管理", methods = "添加产品")
    @ApiOperation(value = "添加产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式",paramType = "query")
    })
    public synchronized Message addGoodsInfo(String token ,Integer role ,String json){
        Message message =Message.non();
        try {
            Goods goods = JSONObject.parseObject(json, Goods.class);
            Integer count = shopService.addGoodsInfo(token, role, goods);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加产品信息失败");
            }
            return message.code(Message.codeSuccessed).message("添加产品信息成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addGoodsInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 通过产品ID查询产品信息（产品资料）
     * @param token
     * @param goodsId
     * @return
     */
    @RequestMapping("/findGoodsByGid")
    @ApiOperation(value = "通过产品ID查询产品信息（产品资料）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品ID",paramType = "query")
    })
    public Message findGoodsByGid(String token ,Integer role ,Integer goodsId){
        Message message = Message.non();
        if (goodsId == null || goodsId ==0){
            return message.code(Message.codeFailured).message("请输入产品ID");
        }
        try {
            HashMap hashMap = shopService.findGoodsByGid(token ,role ,goodsId);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/findGoodsByGid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过产品ID查询产品信息
     * @param token
     * @param goodsId
     * @return
     */
    @RequestMapping("/goodsInfo")
    @ApiOperation(value = "通过产品ID查询产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品ID",paramType = "query")
    })
    public Message goodsInfo(String token ,Integer role ,Integer goodsId){
        Message message = Message.non();
        if (goodsId == null || goodsId ==0){
            return message.code(Message.codeFailured).message("请输入产品ID");
        }
        try {
            HashMap hashMap = shopService.goodsInfo(token ,role ,goodsId);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/goodsInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }




    /**
     * 修改产品信息
     * @return
     */
    @RequestMapping("/updateGoods")
    @SysLog(module = "资料管理",methods = "修改产品信息")
    @ApiOperation(value = "修改产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式",paramType = "query")
    })
    public Message updateGoods(String token ,Integer role,String json){
        Message message = Message.non();
        try {
            Goods goods = JSONObject.parseObject(json, Goods.class);

            Integer count = shopService.updateGoods(goods);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除产品
     * @return
     */
    @RequestMapping("/deleGoods")
    @SysLog(module = "资料管理", methods = "删除产品")
    @ApiOperation(value = "删除产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品ID",paramType = "query")
    })
    public Message deleGoods(String token,Integer role , Integer goodsId){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(goodsId)){
            return message.code(Message.codeFailured).message("请输入产品id");
        }
        try {
            Integer count = shopService.deleGoods(goodsId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 产品上架
     * @param token
     * @param goodsId
     * @return
     */
    @RequestMapping("/isUpper")
    @SysLog(module = "资料管理", methods = "产品上架")
    @ApiOperation(value = "产品上架" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品ID",paramType = "query")
    })
    public Message isUpper(String token ,Integer role,Integer goodsId){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(goodsId)){
            return message.code(Message.codeFailured).message("请输入产品ID");
        }
        try {
            Integer count = shopService.isUpper(goodsId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("上架失败");
            }
            return message.code(Message.codeSuccessed).message("上架成功，等待审核");
        }catch (Exception e) {
            log.error("店铺控制层（/business/isUpper）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 产品下架
     * @param token
     * @param goodsId
     * @return
     */
    @RequestMapping("/isLower")
    @SysLog(module = "资料管理", methods = "产品下架")
    @ApiOperation(value = "产品下架" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品ID",paramType = "query")
    })
    public Message isLower(String token ,Integer role,Integer goodsId){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(goodsId)){
            return message.code(Message.codeFailured).message("请输入产品ID");
        }
        try {
            Integer count = shopService.isLower(goodsId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("下架失败");
            }
            return message.code(Message.codeSuccessed).message("下架成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/isLower）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除产品图片
     * @param id
     * @return
     */
    @RequestMapping("/deleteGoodsImg")
    @SysLog(module = "资料管理", methods = "删除产品图片")
    @ApiOperation(value = "删除产品图片" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "产品图片ID",paramType = "query")
    })
    public Message deleteGoodsImg(String token ,Integer role,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入产品图片ID");
        }
        try {
            Integer count = shopService.deleteGoodsImg(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("取消图片失败");
            }
            return message.code(Message.codeSuccessed).message("取消图片成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteGoodsImg）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改产品在APP上排序的优先级
     * @param goods_id
     * @return
     */
    @RequestMapping("/updateGoodsPriority")
    @ApiOperation(value = "修改产品在APP上排序的优先级" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "priority" ,value = "0默认 ，1优先级" ,paramType = "query")
    })
    public Message updateGoodsPriority(Integer goods_id,Integer priority ){
        Message message = Message.non();
        if (goods_id == null || goods_id == 0){
            return message.code(Message.codeFailured).message("请输入产品id");
        }
        try {
            Integer count = shopService.updateGoodsPriority(goods_id, priority);

            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateGoodsPriority）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 分页查询总平台推送给商家的信息
     * @param token
     * @return
     */
    @RequestMapping("/findPushBusinessMessage")
    @ApiOperation(value = "分页查询总平台推送给商家的信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数",paramType = "query")
    })
    public Message findPushBusinessMessage(String token ,Integer role,Integer pageNo){
        Message message = Message.non();
        Page page = shopService.findPushBusinessMessage(token , role, pageNo);
        return message.code(Message.codeSuccessed).data(page).message("查询成功");
    }


    /**
     * 分页查询商家的操作记录
     * @param token
     * @param phone
     * @param name
     * @return
     */
    @RequestMapping("/findBusinessOperationRecord")
    @ApiOperation(value = "分页查询商家的操作记录" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数",paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "登录的账号",paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "操作人名称或执行的方法",paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "页数",paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "页数",paramType = "query")
    })
    public Message findBusinessOperationRecord(String token ,Integer role,Integer pageNo,String phone,String name,String startTime,String endTime){
        Message message = Message.non();
        String start= DateUtil.getDay();
        String end=DateUtil.getDay();
        try {
            if (startTime != null && !startTime.equals("") && endTime != null && !endTime.equals("")){
                start = startTime; end = endTime;
            }
            Page logList = logService.findLogList(token ,role,pageNo, phone, name,start,end);
            return message.code(Message.codeSuccessed).data(logList).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeSuccessed).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/findBusinessOperationRecord）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 添加积分产品类别
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/addIntegrGoodsCategory")
    @SysLog(module = "资料管理",methods = "添加积分产品类别")
    @ApiOperation(value = "添加积分产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称",paramType = "query")
    })
    public Message addIntegrGoodsCategory(String token ,Integer role ,String name){
        Message message = Message.non();
        if (StringUtils.isEmpty(name)){
            return message.code(Message.codeFailured).message("请输入积分产品类别名称");
        }
        try {
            Integer count = shopService.addIntegrGoodsCategory(token, role, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加积分产品类别失败");
            }
            return message.code(Message.codeSuccessed).message("添加积分产品类别成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addIntegrGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询店铺的积分产品类别
     * @param token
     * @return
     */
    @RequestMapping("/selectBusinessIntegrGoodsCategory")
    @ApiOperation(value = "查询店铺的积分产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message selectBusinessIntegrGoodsCategory(String token ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMap =shopService.selectBusinessIntegrGoodsCategory(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectBusinessIntegrGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 分页查询店铺积分产品信息
     * @param token
     * @param pageNo
     * @param categoryId 产品类别ID
     * @param number 产品编号
     * @param name 产品名称
     * @param barcode 条码
     * @return
     */
    @RequestMapping("/selectIntegrGoodsList")
    @ApiOperation(value = "分页查询店铺积分产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "产品编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "barcode" ,value = "条码" ,paramType = "query")
    })
    public Message selectIntegrGoodsList(String token ,Integer role,Integer pageNo ,Integer categoryId,String number,String name,Long barcode ){
        Message message = Message.non();
        try {
            Page page = shopService.selectIntegrGoodsList(token ,role,pageNo,categoryId,number,name,barcode);
            return message.code(Message.codeSuccessed).data(page).data(page).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectIntegrGoodsList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过积分产品类别id查询类别名称
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectIntegrGoodsCategory")
    @ApiOperation(value = "通过积分产品类别id查询类别名称" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分类别id" ,paramType = "query")
    })
    public Message selectIntegrGoodsCategory(String token ,Integer role ,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入积分产品类别id");
        }
        HashMap hashMap = shopService.selectIntegrGoodsCategory(id);
        return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
    }


    /**
     * 修改积分产品类别名称
     * @param token
     * @param id 类别id
     * @return
     */
    @RequestMapping("/updateIntegrGoodsCategory")
    @SysLog(module = "资料管理" ,methods = "修改积分类别名称")
    @ApiOperation(value = "修改积分产品类别名称" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分类别名称" ,paramType = "query")
    })
    public Message updateIntegrGoodsCategory(String token ,Integer role ,Integer id,String name){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(id)) {
                return message.code(Message.codeFailured).message("请输入积分产品类别id");
            }
            if (StringUtils.isEmpty(name)) {
                return message.code(Message.codeFailured).message("请输入积分产品类别名称");
            }
            Integer count = shopService.updateIntegrGoodsCategory(id, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改积分产品类别失败");
            }
            return message.code(Message.codeSuccessed).message("修改积分产品类别成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateIntegrGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除积分产品类别
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteIntegrGoodsCategory")
    @SysLog(module = "资料管理" ,methods = "删除积分产品类别")
    @ApiOperation(value = "删除积分产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分类别id" ,paramType = "query")
    })
    public Message deleteIntegrGoodsCategory(String token , Integer role , Integer id){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(id)) {
                return message.code(Message.codeFailured).message("请输入积分产品类别id");
            }
            Integer count = shopService.deleteIntegrGoodsCategory(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除积分产品类别失败");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("当前类别下还有产品存在，不能删除");
            }
            return message.code(Message.codeSuccessed).message("删除积分产品类别成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteIntegrGoodsCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 自动生成积分产品编号
     * @param token
     * @param category_id
     * @return
     */
    @RequestMapping("/autoMakeIntegrGoodsNumber")
    @ApiOperation(value = "删除积分产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "积分类别id" ,paramType = "query")
    })
    public Message autoMakeIntegrGoodsNumber(String token ,Integer role,Integer category_id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(category_id)){
            return message.code(Message.codeFailured).message("请输入积分产品类别id");
        }
        String number = shopService.autoMakeIntegrGoodsNumber(category_id);
        if (StringUtils.isEmpty(number)){
            return message.code(Message.codeFailured).message("自动生成编号失败");
        }
        return message.code(Message.codeSuccessed).data(number).message("生成编号成功");
    }

    /**
     * 添加积分产品信息
     * @param token
     * @param
     * @return
     */
    @RequestMapping("/addIntegrGoods")
    @SysLog(module = "资料管理" ,methods = "添加积分产品信息")
    @ApiOperation(value = "添加积分产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addIntegrGoods(String token  ,Integer role, String json){
        Message message = Message.non();
        try {
            IntegrGoods integrGoods = JSONObject.parseObject(json, IntegrGoods.class);

            Integer count = shopService.addIntegrGoods(token, role, integrGoods);

            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加积分产品失败");
            }
            return message.code(Message.codeSuccessed).message("添加积分产品成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addIntegrGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过积分产品id查询产品信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectIntegrGoodsInfo")
    @ApiOperation(value = "添加积分产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分产品id" ,paramType = "query")
    })
    public Message selectIntegrGoodsInfo(String token ,Integer role ,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id) ){
            return message.code(Message.codeFailured).message("请输入积分产品id");
        }
        HashMap hashMap = shopService.selectIntegrGoodsInfoById(id);
        return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
    }

    /**
     * 修改积分产品信息
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/updateIntegrGoodsInfo")
    @SysLog(module = "资料管理" ,methods = "修改积分产品信息")
    @ApiOperation(value = "修改积分产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message updateIntegrGoodsInfo(String token ,Integer role  ,String json){
        Message message = Message.non();
        try {
            IntegrGoods integrGoods = JSONObject.parseObject(json, IntegrGoods.class);
            if (IntegerUtils.isEmpty(integrGoods.getId())) {
                return message.code(Message.codeFailured).message("请输入积分产品id");
            }

            Integer count = shopService.updateIntegrGoodsInfo(integrGoods);

            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改积分产品信息失败");
            }
            return message.code(Message.codeSuccessed).message("修改积分产品信息成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateIntegrGoodsInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除积分产品
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteIntegrGoods")
    @SysLog(module = "资料管理" ,methods = "删除积分产品")
    @ApiOperation(value = "删除积分产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "积分产品id" ,paramType = "query")
    })
    public Message deleteIntegrGoods(String token ,Integer role,Integer id){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(id)) {
                return message.code(Message.codeFailured).message("请输入积分产品id");
            }
            Integer count = shopService.deleteIntegrGoods(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除积分产品失败");
            }
            return message.code(Message.codeSuccessed).message("删除积分产品成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteIntegrGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 商家二维码id,前端拿id生产二维码
     * @param token
     * @return
     */
    @RequestMapping("/findBusinessTDCode")
    @ApiOperation(value = "商家二维码id,前端拿id生产二维码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message findBusinessTDCode(String token ,Integer role){
        Message message = Message.non();
        try {
            Integer id = shopService.findBusinessTDCode(token ,role);
            return message.code(Message.codeSuccessed).data(id).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 用户申请绑定商家
     * @param token
     * @param phone
     * @param business_id
     * @return
     */
    @RequestMapping("/addBindingApplication")
    @ApiOperation(value = "用户申请绑定商家" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query")
    })
    public Message addBindingApplication(String token,Integer role ,String phone,Integer business_id){
        Message message = Message.non();
        try {
            Integer count = shopService.addBindingApplication(phone, business_id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("申请失败");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("手机号未注册");
            }
            return message.code(Message.codeSuccessed).message("申请成功，等待商家审核");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addBindingApplication）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 分享有奖二维码 获取当前登录的手机号码
     * @param token
     * @return
     */
    @RequestMapping("/sharePhone")
    @ApiOperation(value = "分享有奖二维码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message sharePhone(String token ,Integer role ){
        Message message = Message.non();
        try {
            HashMap hashMap = shopService.sharePhone(token, role);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 根据手机号查询邀请商家注册的信息
     * @param phone 手机号
     * @param open null查询所有邀请商家，1已购买使用权
     * @return
     */
    @RequestMapping("/findsharePhone")
    @ApiOperation(value = "根据手机号查询邀请商家注册的信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "open" ,value = "null查询所有邀请商家，1已购买使用权" ,paramType = "query")
    })
    public Message findsharePhone(String phone,Integer open){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.findsharePhone(phone, open);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 积分产品下架
     * @param token
     * @param goodsId
     * @return
     */
    @RequestMapping("/InventoryGoodsIsLower")
    @SysLog(module = "资料管理" ,methods = "积分产品下架")
    @ApiOperation(value = "积分产品下架" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsId" ,value = "产品id" ,paramType = "query")
    })
    public Message InventoryGoodsIsLower(String token ,Integer role ,Integer goodsId){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(goodsId)) {
                return message.code(Message.codeFailured).message("请输入产品ID");
            }
            Integer count = shopService.InventoryGoodsIsLower(goodsId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("下架失败");
            }
            return message.code(Message.codeSuccessed).message("下架成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/InventoryGoodsIsLower）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 获取pc端系统公告
     * @return
     */
    @RequestMapping("/system_bulletin")
    @ApiOperation(value = "获取pc端系统公告" ,httpMethod = "POST")
    public Message systemBulletin(){
        Message message = Message.non();
        try {
            HashMap hashMap = shopService.systemBulletin();
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 获取电子协议
     * @return
     */
    @RequestMapping("/agreement")
    @ApiOperation(value = "获取pc端系统公告" ,httpMethod = "POST")
    public Message agreement(){
        Message message = Message.non();
        try {
            HashMap hashMap = shopService.agreement();
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询类别下的产品信息集合
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findGoodsListByCategoryId")
    @ApiOperation(value = "查询类别下的产品信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message findGoodsListByCategoryId(String token,Integer role ,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        try {
            List<HashMap> hashMaps = shopService.findGoodsListByCategoryId(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/findGoodsListByCategoryId）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }




    /**
     * 商家设置
     * @param token
     * @return
     */
    @RequestMapping("/businessSetting")
    @ApiOperation(value = "商家设置" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message businessSetting(String token ,Integer role){
        Message message = Message.non();
        try {
            HashMap hashMap = shopService.businessSet(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 修改商家设置
     * @param token
     * @param business
     * @return
     */
    @RequestMapping("/updateBusinessSetting")
    @ApiOperation(value = "修改商家设置" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "img" ,value = "店铺图片" ,paramType = "query"),
            @ApiImplicitParam(name = "amount_set" ,value = "库存设置，0低于库存能购买，1不能购买" ,paramType = "query"),
            @ApiImplicitParam(name = "confine" ,value = "配送范围" ,paramType = "query"),
            @ApiImplicitParam(name = "business_hours" ,value = "营业时间" ,paramType = "query"),
            @ApiImplicitParam(name = "notice" ,value = "店铺公告" ,paramType = "query"),
            @ApiImplicitParam(name = "telephone" ,value = "商城电话" ,paramType = "query"),
            @ApiImplicitParam(name = "address" ,value = "店铺地址" ,paramType = "query")
    })
    public Message updateBusinessSetting(String token ,Integer role , Business business){
        Message message = Message.non();
        if (business.getId() == null || business.getId() == 0){
            return message.code(Message.codeFailured).message("请输入商家id");
        }
        try {
            Integer count = shopService.updateBusinessSetting(business);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateBusinessSetting）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询用户的推荐产品
     * @param token
     * @param binding_id 用户id
     * @return
     */
    @RequestMapping("/selectUserRecommendGoods")
    @ApiOperation(value = "查询用户的推荐产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户id" ,paramType = "query")
    })
    public Message selectRecommendGoods(String token ,Integer role ,Integer binding_id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectUserRecommendGoods(token ,role, binding_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectUserRecommendGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加用户的推荐产品
     * @param token
     * @param binding_id 用户id
     * @param json
     * @return
     */
    @RequestMapping("/addUserRecommendGoods")
    @SysLog( module = "设置管理" ,methods = "添加用户的推荐产品")
    @ApiOperation(value = "添加用户的推荐产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户id" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addUserRecommendGoods(String token ,Integer role ,Integer binding_id,String json){
        Message message = Message.non();
        try {
            Exp exp = JSONObject.parseObject(json, Exp.class);

            Integer count = shopService.addUserRecommendGoods(binding_id, exp.getRecommends());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("推荐失败");
            }
            return message.code(Message.codeSuccessed).message("推荐成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addUserRecommendGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 下架或上传推荐产品
     * @param token
     * @param id 推荐产品id
     * @param is_upload 0上传，1下架
     * @return
     */
    @RequestMapping("/updateUserRecommendGoods")
    @SysLog( module = "客户管理" ,methods = "下架或上传推荐产品")
    @ApiOperation(value = "下架或上传推荐产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "推荐产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "is_upload" ,value = "0上传，1下架" ,paramType = "query")
    })
    public Message updateUserRecommendGoods(String token ,Integer role ,Integer id,Integer is_upload){
        Message message = Message.non();
        try {
            Integer count = shopService.updateUserRecommendGoods(id, is_upload);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("操作失败");
            }
            return message.code(Message.codeSuccessed).message("操作成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateUserRecommendGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除推荐产品
     * @param token
     * @param id 推荐产品id
     * @return
     */
    @RequestMapping("/deteleUserRecommendGoods")
    @SysLog( module = "设置管理" ,methods = "删除推荐产品")
    @ApiOperation(value = "删除推荐产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "推荐产品id" ,paramType = "query")
    })
    public Message deteleUserRecommendGoods(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = shopService.deteleUserRecommendGoods(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deteleUserRecommendGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询限购产品
     * @param token
     * @param name 产品名称或编号
     * @return
     */
    @RequestMapping("/selectRestrictionGoods")
    @ApiOperation(value = "查询限购产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query")
    })
    public Message selectRestrictionGoods(String token ,Integer role ,String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectRestrictionGoods(token ,role,name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectRestrictionGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 搜索未限购的产品
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/searchNotRestrictionGoods")
    @ApiOperation(value = "查询限购产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query")
    })
    public Message searchNotRestrictionGoods(String token ,Integer role ,String name,Integer goods_id,Integer category_id){
        Message message =Message.non();
        try {
            List<HashMap> hashMaps = shopService.searchNotRestrictionGoods(token ,role,name,goods_id,category_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/searchNotRestrictionGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 通过产品id查询产品信息
     * @param token
     * @param goods_id
     * @return
     */
    @RequestMapping("/selectGoodsInfoByGid")
    @ApiOperation(value = "通过产品id查询产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
    })
    public Message selectGoodsInfoByGid(String token ,Integer role ,String goods_id){
        Message message =Message.non();
        try {
            List<HashMap> hashMap = shopService.selectGoodsInfoByGid(goods_id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectGoodsInfoByGid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加限购产品
     * @param token
     * @param goods_id 限购产品id
     * @param num 限购数量
     * @return
     */
    @RequestMapping("/addRestrictionGoods")
    @SysLog(module = "设置管理"  ,methods = "添加限购产品")
    @ApiOperation(value = "添加限购产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
    })
    public Message addRestrictionGoods(String token ,Integer role ,Integer goods_id,Double num){
        Message message = Message.non();
        try {
            Integer count = shopService.addRestrictionGoods(token ,role, goods_id, num);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加限购产品失败");
            }
            return message.code(Message.codeSuccessed).message("添加限购产品成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addRestrictionGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除限购产品
     * @param token
     * @param id 限购id
     * @return
     */
    @RequestMapping("/deleteRestrictionGoods")
    @SysLog(module = "设置管理"  ,methods = "删除限购产品")
    @ApiOperation(value = "删除限购产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "限购id" ,paramType = "query"),
    })
    public Message deleteRestrictionGoods(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = shopService.deleteRestrictionGoods(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除限购产品失败");
            }
            return message.code(Message.codeSuccessed).message("删除限购产品成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteRestrictionGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加优惠券
     * @param token
     * @param coupon
     * @return
     */
    @RequestMapping("/addCoupon")
    @SysLog(module = "设置管理"  ,methods = "添加优惠券模板")
    @ApiOperation(value = "添加优惠券" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "优惠券名称" ,paramType = "query"),
            @ApiImplicitParam(name = "full" ,value = "满" ,paramType = "query"),
            @ApiImplicitParam(name = "reduce" ,value = "减" ,paramType = "query")
    })
    public Message addCoupon(String token , Integer role,Coupon coupon){
        Message message = Message.non();
        try {
            Integer count = shopService.addCoupon(token ,role, coupon);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加优惠券失败");
            }
            return message.code(Message.codeSuccessed).message("添加优惠券成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询优惠券
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/selectAllCoupon")
    @ApiOperation(value = "查询优惠券" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "优惠券名称" ,paramType = "query")
    })
    public Message selectAllCoupon(String token ,Integer role ,String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectAllCoupon(token ,role ,name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectAllCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过id查询优惠券信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectCouponById")
    @ApiOperation(value = "通过id查询优惠券信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "优惠券名称" ,paramType = "query")
    })
    public Message selectCouponById(String token ,Integer role,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入优惠券id");
        }
        try {
            HashMap hashMap = shopService.selectCouponById(id);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectCouponById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 修改优惠券
     * @param token
     * @param coupon
     * @return
     */
    @RequestMapping("/updateCoupon")
    @SysLog(module = "设置管理"  ,methods = "修改优惠券")
    @ApiOperation(value = "修改优惠券" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "优惠券id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "优惠券名称" ,paramType = "query"),
            @ApiImplicitParam(name = "full" ,value = "满" ,paramType = "query"),
            @ApiImplicitParam(name = "reduce" ,value = "减" ,paramType = "query")
    })
    public Message updateCoupon(String token ,Integer role ,Coupon coupon){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(coupon.getId())){
            return message.code(Message.codeFailured).message("请输入优惠券id");
        }
        try {
            Integer count = shopService.updateCoupon(coupon);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 删除优惠券
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteCoupon")
    @SysLog(module = "设置管理"  ,methods = "删除优惠券")
    @ApiOperation(value = "删除优惠券" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "优惠券id" ,paramType = "query")
    })
    public Message deleteCoupon(String token ,Integer role ,Integer id){
        Message message = Message.non();
        if (id == null || id ==  0){
            return message.code(Message.codeFailured).message("请输入优惠券id");
        }
        try {
            Integer count = shopService.deleteCoupon(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 用户优惠券
     * @param token
     * @param binding_id 用户id
     * @return
     */
    @RequestMapping("/bindingCoupon")
    @ApiOperation(value = "用户优惠券" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "用户id" ,paramType = "query")
    })
    public Message bindingCoupon(String token ,Integer role ,Integer binding_id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.bindingCoupon(token ,role ,binding_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/bindingCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 派送优惠券给用户
     * @param token
     * @param coupon
     * @return
     */
    @RequestMapping("/addBindingCoupon")
    @SysLog(module = "设置管理"  ,methods = "派送优惠券给用户")
    @ApiOperation(value = "派送优惠券给用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "coupon_id" ,value = "优惠券id" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "客户id" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message addBindingCoupon(String token ,Integer role ,Coupon coupon){
        Message message = Message.non();
        try {
            Integer count = shopService.addBindingCoupon(coupon);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("派送失败");
            }
            return message.code(Message.codeSuccessed).message("派送成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addBindingCoupon）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加满赠产品
     * @param token
     * @param goods_id
     * @param full
     * @param bestow
     * @return
     */
    @RequestMapping("/addFullBestowGoods")
    @SysLog(module = "设置管理"  ,methods = "添加满赠产品")
    @ApiOperation(value = "添加满赠产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "full" ,value = "满" ,paramType = "query"),
            @ApiImplicitParam(name = "bestow" ,value = "赠" ,paramType = "query")
    })
    public Message addFullBestowGoods(String token ,Integer role ,Integer goods_id,Double full,Double bestow){
        Message message = Message.non();
        try {
            Integer count = shopService.addFullBestowGoods(token ,role ,goods_id, full, bestow);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/addFullBestowGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 搜索未添加满赠的产品
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/searchNotFullBestow")
    @ApiOperation(value = "搜索未添加满赠的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query")
    })
    public Message searchNotAddFullBestow(String token ,Integer role ,String name,Integer category_id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.searchNotFullBestowGoods(token ,role,name,category_id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/searchNotAddFullBestow）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询已添加满赠的产品
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/selectAddFullBestowGoods")
    @ApiOperation(value = "查询已添加满赠的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query")
    })
    public Message selectAddFullBestow(String token ,Integer role ,String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectAddFullBestowGoods(token ,role,name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectAddFullBestow）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除满赠产品
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteFullBestowGoods")
    @ApiOperation(value = "删除满赠产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "满赠id" ,paramType = "query")
    })
    public Message deleteFullBestowGoods(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = shopService.deleteFullBestowGoods(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteFullBestowGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 赠送用户积分
     * @param token
     * @param integra
     * @return
     */
    @RequestMapping("/addGiveBindingIntegra")
    @SysLog(module = "设置管理"  ,methods = "赠送用户积分")
    @ApiOperation(value = "赠送用户积分" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "binding_id" ,value = "用户id" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1商城，2线下，3积分兑换，4人工赠送，5人工减" ,paramType = "query"),
            @ApiImplicitParam(name = "integra" ,value = "积分数" ,paramType = "query"),
            @ApiImplicitParam(name = "illustrate" ,value = "说明" ,paramType = "query")
    })
    public Message addGiveBindingIntegra(String token ,Integer role ,Integra integra){
        Message message = Message.non();
        try {
            Integer count = shopService.addGiveBindingIntegra(integra);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("赠送失败");
            }
            return message.code(Message.codeSuccessed).message("赠送成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addGiveBindingIntegra）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *
     * 查询用户积分
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectUserIntegra")
    @ApiOperation(value = "查询用户积分" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "用户id" ,paramType = "query")
    })
    public Message selectUserIntegra(String token ,Integer role ,Integer pageNo,Integer id){
        Message message = Message.non();
        try {
            Page page = shopService.selectUserIntegra(token ,role ,pageNo,id);
            return message.code(Message.codeSuccessed).data(page).message("获取成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectUserIntegra）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询赠送用户积分列表（只保存三天记录）
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/selectBindingIntegraGive")
    @ApiOperation(value = "查询赠送用户积分列表（只保存三天记录）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "用户积分id" ,paramType = "query")
    })
    public Message selectBindingIntegraGive(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectBindingIntegraGive(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e) {
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectBindingIntegraGive）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加限时秒杀时间段
     * @param token
     * @return
     */
    @RequestMapping("/addLimitedSeckill")
    @SysLog(module = "设置管理" ,methods = "添加限时秒杀时间段")
    @ApiOperation(value = "添加限时秒杀时间段" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query")
    })
    public Message addLimitedSeckill(String token ,Integer role ,Seckill seckill){
        Message message = Message.non();
        try {
            Integer count = shopService.addLimitedSeckill(token ,role, seckill);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addLimitedSeckill）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询限时秒杀时间段
     * @param token
     * @return
     */
    @RequestMapping("/selectLimitedSeckill")
    @ApiOperation(value = "查询限时秒杀时间段" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message selectLimitedSeckill(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.selectLimitedSeckill(token ,role );
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectLimitedSeckill）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 更新秒杀时间段
     * @param token
     * @param seckill
     * @return
     */
    @RequestMapping("/updateLimitedSeckill")
    @SysLog(module = "设置管理" ,methods = "更新秒杀时间段")
    @ApiOperation(value = "更新秒杀时间段" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query")
    })
    public Message updateLimitedSeckill(String token ,Integer role ,Seckill seckill){
        Message message = Message.non();
        try {
            Integer count = shopService.updateLimitedSeckill(seckill);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("更换失败");
            }
            return message.code(Message.codeSuccessed).message("更换成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateLimitedSeckill）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除秒杀时间段
     * @param token
     * @param id 秒杀id
     * @return
     */
    @RequestMapping("/deleteLimitedSeckill")
    @SysLog(module = "设置管理" ,methods = "删除秒杀时间段")
    @ApiOperation(value = "删除秒杀时间段" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "秒杀id" ,paramType = "query")
    })
    public Message deleteLimitedSeckill(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = shopService.deleteLimitedSeckill(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("更换失败");
            }
            return message.code(Message.codeSuccessed).message("更换成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteLimitedSeckill）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询时间段下的秒杀产品
     * @param token
     * @param id 秒杀id
     * @return
     */
    @RequestMapping("/selectLimitedSeckillGoods")
    @ApiOperation(value = "查询时间段下的秒杀产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "秒杀id" ,paramType = "query")
    })
    public Message selectLimitedSeckillGoods(String token ,Integer role ,Integer id ){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入秒杀id");
        }
        try {
            List<HashMap> hashMaps = shopService.selectLimitedSeckillGoods(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectLimitedSeckillGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 搜索未在同一时间段秒杀的产品
     * @param token 商家id
     * @param seckill_id 秒杀id
     * @param name 产品名称或编号或条形码
     * @return
     */
    @RequestMapping("/notLimitedSeckillGoods")
    @ApiOperation(value = "搜索未在同一时间段秒杀的产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "seckill_id" ,value = "秒杀id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号或条形码" ,paramType = "query")
    })
    public Message notLimitedSeckillGoods(String token , Integer role ,Integer seckill_id ,String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.notLimitedSeckillGoods(token ,role, seckill_id,name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/notLimitedSeckillGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }



    /**
     * 添加限时秒杀产品
     * @param token
     * @return
     */
    @RequestMapping("/addLimitedSeckillGoods")
    @SysLog(module = "设置管理" ,methods = "添加限时秒杀产品")
    @ApiOperation(value = "添加限时秒杀产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "seckill_id" ,value = "秒杀id" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "activity_price" ,value = "秒杀单价" ,paramType = "query")
    })
    public Message addLimitedSeckillGoods(String token ,Integer role ,SeckillDetail seckillDetail){
        Message message = Message.non();
        try {
            Integer count = shopService.addLimitedSeckillGoods(seckillDetail);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addLimitedSeckillGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 更换秒杀产品
     * @param token
     * @param seckillDetail
     * @return
     */
    @RequestMapping("/updateLimitedSeckillGoods")
    @SysLog(module = "设置管理" ,methods = "更换秒杀产品")
    @ApiOperation(value = "更换秒杀产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "seckill_id" ,value = "秒杀id" ,paramType = "query"),
            @ApiImplicitParam(name = "goods_id" ,value = "产品id" ,paramType = "query"),
            @ApiImplicitParam(name = "activity_price" ,value = "秒杀单价" ,paramType = "query")
    })
    public Message updateLimitedSeckillGoods(String token ,Integer role ,SeckillDetail seckillDetail){
        Message message = Message.non();
        try {
            Integer count = shopService.updateLimitedSeckillGoods(seckillDetail);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("更换失败");
            }
            return message.code(Message.codeSuccessed).message("更换成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateLimitedSeckillGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除秒杀产品
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deteleLimitedSeckillGoods")
    @SysLog(module = "设置管理" ,methods = "删除秒杀产品")
    @ApiOperation(value = "删除秒杀产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "秒杀明细id" ,paramType = "query")
    })
    public Message deteleLimitedSeckillGoods(String token ,Integer role ,Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入秒杀产品id");
        }
        try {
            Integer count = shopService.deteleLimitedSeckillGoods(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deteleLimitedSeckillGoods）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加店铺满减活动
     * @param token
     * @return
     */
    @RequestMapping("/addFullReductionActivity")
    @SysLog(module = "设置管理" ,methods = "添加店铺满减活动")
    @ApiOperation(value = "添加店铺满减活动" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "full" ,value = "满" ,paramType = "query"),
            @ApiImplicitParam(name = "reduce" ,value = "减" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message addFullReductionActivity(String token ,Integer role ,Seckill seckill){
        Message message = Message.non();
        try {
            Integer count = shopService.addFullReductionActivity(token , role , seckill);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加满减活动失败");
            }
            return message.code(Message.codeSuccessed).message("添加满减活动成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/addFullReductionActivity）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询满减活动
     * @param token
     * @return
     */
    @RequestMapping("/selectFullReductionActivity")
    @ApiOperation(value = "查询满减活动" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message selectFullReductionActivity(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMap = shopService.selectFullReductionActivity(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/selectFullReductionActivity）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改满减活动
     * @param token
     * @param seckill
     * @return
     */
    @RequestMapping("/updateFullReductionActivity")
    @SysLog(module = "设置管理" ,methods = "修改满减活动")
    @ApiOperation(value = "修改满减活动" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "满减id" ,paramType = "query"),
            @ApiImplicitParam(name = "full" ,value = "满" ,paramType = "query"),
            @ApiImplicitParam(name = "reduce" ,value = "减" ,paramType = "query"),
            @ApiImplicitParam(name = "startTime" ,value = "开始时间" ,paramType = "query"),
            @ApiImplicitParam(name = "endTime" ,value = "结束时间" ,paramType = "query")
    })
    public Message updateFullReductionActivity(String token ,Integer role ,Seckill seckill){
        Message message = Message.non();
        try {
            Integer count = shopService.updateFullReductionActivity(seckill);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改满减活动失败");
            }
            return message.code(Message.codeSuccessed).message("修改满减活动成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/updateFullReductionActivity）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除满减活动
     * @param token
     * @param id 满减id
     * @return
     */
    @RequestMapping("/deleteFullReductionActivity")
    @SysLog(module = "设置管理" ,methods = "删除满减活动")
    @ApiOperation(value = "删除满减活动" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "满减id" ,paramType = "query")
    })
    public Message deleteFullReductionActivity(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Integer count = shopService.deleteFullReductionActivity(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/deleteFullReductionActivity）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 判断是否是商家登录的
     * @param token
     * @return
     */
    @RequestMapping("/isBuseinssToken")
    @ApiOperation(value = "判断是否是商家登录的" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message isBuseinssToken(String token,Integer role ){
        Message message = Message.non();
        try {
            boolean count = shopService.isBuseinssToken(token  );
            return message.code(Message.codeSuccessed).data(count).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/isBuseinssToken）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改商家密码
     * @param token
     * @param password
     * @return
     */
    @RequestMapping("/udpateBusinessPassword")
    @SysLog(module = "商家设置",methods = "修改商家密码")
    @ApiOperation(value = "修改商家密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "新密码" ,paramType = "query")
    })
    public Message udpateBusinessPassword(String token ,Integer role ,String password){
        Message message = Message.non();
        try {
            Integer count = shopService.udpateBusinessPassword(token,password);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/udpateBusinessPassword）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询第一产品类别
     * @param token
     * @return
     */
    @RequestMapping("/oneCategory")
    @ApiOperation(value = "查询第一产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public Message oneCategory(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.oneCategory(token , role);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/oneCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询第二产品类别
     * @param id
     * @return
     */
    @RequestMapping("/twoCategory")
    @ApiOperation(value = "查询第二产品类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "第一类别id" ,paramType = "query")
    })
    public Message twoCategory(Integer id){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.twoCategory(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e) {
            log.error("店铺控制层（/business/twoCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 导出产品信息
     * @param response
     * @param
     * @return
     */
    @RequestMapping("/excelDownload")
    @ApiOperation(value = "导出产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public void excelDownload(String token ,Integer role ,HttpServletResponse response, Integer category_id, String number,String name,Long barcode,Integer pageNo){
        Message message = Message.non();
        HSSFWorkbook workbook = shopService.excelDownload(token ,role ,new Parameter(category_id,number,name,barcode,pageNo));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"产品资料"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 导出积分产品信息
     * @param response
     * @param
     * @return
     */
    @RequestMapping("/IntegrGoodsexcelDownload")
    @ApiOperation(value = "导出积分产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query")
    })
    public void IntegrGoodsexcelDownload(String token ,Integer role ,HttpServletResponse response, Integer categoryId,String number,String name,Long barcode){
        Message message = Message.non();
        HSSFWorkbook workbook = shopService.IntegrGoodsexcelDownload(token ,role , categoryId, number, name, barcode);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"积分产品资料"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }


    /**
     * 匹配店铺密码
     * @param token
     * @param password
     * @return
     */
    @RequestMapping("/businessPassword")
    @ApiOperation(value = "匹配店铺密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "密码" ,paramType = "query")
    })
    public Message businessPassword(String token ,Integer role ,String password){
        Message message = Message.non();
        if (StringUtils.isEmpty(password)){
            return message.code(Message.codeFailured).message("密码不能为空");
        }
        try {
            boolean bolan = shopService.businessPassword(token ,role, password);
            return message.code(Message.codeSuccessed).data(bolan).message("获取成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("店铺控制层（/business/businessPassword）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 轮播图
     * @return
     */
    @RequestMapping("/BMJ")
    @ApiOperation(value = "轮播图" ,httpMethod = "POST")
    public Message BMJ(){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = shopService.BMJ();
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 产品二维码
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/goodsOR")
    @ApiOperation(value = "产品二维码" ,httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
    })
    public Message goodsOR(String token ,Integer role ){
        Message message = Message.non();
        HashMap hashMap = shopService.goodsOR(token , role);
        if (hashMap == null ){
            message.code(Message.codeFailured).message("获取失败");
        }
        return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
    }

    /**
     * 刷新产品二维码识别码
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/refreshGoodsORIdentificationCode")
    @SysLog(module = "资料管理" ,methods = "刷新产品二维码识别码")
    @ApiOperation(value = "刷新产品二维码识别码" ,httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "角色(1店铺，2员工)" ,paramType = "query"),
    })
    public Message refreshGoodsORIdentificationCode(String token ,Integer role ){
        Message message = Message.non();
        Integer hashMap = shopService.refreshGoodsORIdentificationCode(token , role);
        if (hashMap == null ){
            message.code(Message.codeFailured).message("获取失败");
        }
        return message.code(Message.codeSuccessed).message("获取成功");
    }




















    @ApiOperation(value = "测试",notes = "测试接口" ,httpMethod = "POST")
    @RequestMapping(value = "/test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token",value = "token" ,paramType = "query"),
            @ApiImplicitParam(name ="role" ,value = "角色，1店铺，2员工"  ,paramType = "query"),
            @ApiImplicitParam(name = "name" , value = "店铺名称"  ,paramType = "query")
    })
    public Message test(String token ,Integer role ,String name){
        Message message = Message.non();
        return message.code(Message.codeSuccessed).data(name).message("获取成功");
    }




























    @RequestMapping("/ERROR_URL1")
    public Message ERROR_URL1(){
        Message message = Message.non();
        return message.code(Message.codeFailured).message("请先登陆");
    }

    @RequestMapping("/ERROR_URL2")
    public Message ERROR_URL2(){
        Message message = Message.non();
        return message.code(Message.codeFailured).message("角色不匹配");
    }
    @RequestMapping("/ERROR_URL3")
    public Message ERROR_URL3(){
        Message message = Message.non();
        return message.code(Message.codeFailured).message("您的账号在另一台设备登录");
    }

    @RequestMapping("/ERROR_URL4")
    public Message ERROR_URL4(){
        Message message = Message.non();
        return message.code(Message.codeFailured).message("您的账号是否出现违反行为导致禁用了？如需解冻，则联系您的上级");
    }

    /**
     * 店铺使用权限
     * @return
     */
    @RequestMapping("/ERROR_URL5")
    @ResponseBody
    public Message ERROR_URL5(){
        Message message = Message.non();
        return message.code(Message.codeFailured).message("店铺使用权限已到期");
    }



}
