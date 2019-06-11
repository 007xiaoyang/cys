package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.MothPrinter;
import com.shengxian.common.util.DateUtil;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.entity.*;
import com.shengxian.service.MenuService;
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
 * Description: 菜单
 *
 * @Author: yang
 * @Date: 2018-10-09
 * @Version: 1.0
 */

@Api(description = "菜单")
@RestController
@RequestMapping("/menu")
public class MenuController {

    private static Logger log = Logger.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;


    /**
     * 菜单
     * @param token
     * @param level 级别
     * @param sort 上级id
     * @return
     */
    @RequestMapping("/selectOneMenu")
    @ApiOperation(value = "菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "level" ,value = "级别" ,paramType = "query"),
            @ApiImplicitParam(name = "sort" ,value = "上级id" ,paramType = "query")
    })
    public Message selectOneMenu(String token ,Integer role , Integer level, Integer sort){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = menuService.selectOneMenu(level, sort);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/selectOneMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }



    /**
     * 通过一级菜单查询二级菜单
     * @param token
     * @param menuId
     * @return
     */
    @RequestMapping("selectTwoMenuList")
    @ApiOperation(value = "通过一级菜单查询二级菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "menuId" ,value = "一级菜单id" ,paramType = "query")
    })
    public Message findMenuList(String token ,Integer role , Integer menuId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(menuId)){
            return message.code(Message.codeFailured).message("请输入菜单ID");
        }
        try {
            List<Menu> hashMap = menuService.selectTwoMenuList(token ,role ,menuId);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/selectTwoMenuList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 获取员工角色
     * @param token
     * @param staff_id
     * @return
     */
    @RequestMapping("/staffRole")
    @ApiOperation(value = "获取员工角色" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "1首页，2销售，3采购，4库存,5配送,6财务,7资料,8员工,9设置" ,paramType = "query")
    })
    public Message staffRole(String token  ,Integer role , Integer staff_id, Integer type){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(staff_id)){
            return message.code(Message.codeFailured).message("请选择员工");
        }
        if (IntegerUtils.isEmpty(type)){
            return message.code(Message.codeFailured).message("类型不能为空");
        }
        try {
            List<Integer> mene = menuService.staffRole(staff_id, type);
            return message.code(Message.codeSuccessed).data(mene).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/staffRole）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改员工角色
     * @param token
     * @param menuRole
     * @return
     */
    @RequestMapping("/udpateStaffRole")
    @SysLog(module = "PC权限管理" ,methods = "修改员工角色")
    @ApiOperation(value = "修改员工角色" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message udpateStaffRole(String token ,Integer role , MenuRole menuRole){
        Message message = Message.non();
        try {
            Integer count = menuService.udpateStaffRole(menuRole);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/udpateStaffRole）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加模板
     * @param token
     * @param
     * @return
     */
    @RequestMapping("/addTemplate")
    @SysLog(module = "模板管理" ,methods = "添加模板")
    @ApiOperation(value = "添加模板" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "模板标题" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "内容" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "类型" ,paramType = "query"),
    })
    public Message addTemplate(String token ,Integer role , String title, String name, String phone, Integer type){
        Message message = Message.non();
        try {
            Integer count = menuService.addTemplate(token ,role, title, name, phone, type);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/addTemplate）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改模板
     * @param token
     * @param template
     * @return
     */
    @RequestMapping("/updateTemplate")
    @SysLog(module = "模板管理" ,methods = "修改模板")
    @ApiOperation(value = "修改模板" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "模板标题" ,paramType = "query")
    })
    public Message updateTemplate(String token ,Integer role , Template template){
        Message message = Message.non();
        try {
            Integer count = menuService.updateTemplate(template);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateTemplate）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 恢复模板
     * @param token
     * @param id 模板id
     * @param type
     * @return
     */
    @RequestMapping("/updateRecoveryTemplate")
    @SysLog(module = "模板管理" ,methods = "恢复模板")
    @ApiOperation(value = "恢复模板" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "模板类型:0采购单 ,1采购退货单,2销售单 ,3销售退货单" ,paramType = "query")
    })
    public Message updateRecoveryTemplate(String token ,Integer role , Integer id, Integer type){
        Message message = Message.non();
        try {
            Integer count = menuService.updateRecoveryTemplate(id,type);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateRecoveryTemplate）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改模板二维码显示状态
     * @param id
     * @param state
     * @return
     */
    @RequestMapping("/updateTemplateState")
    @SysLog(module = "模板管理" ,methods = "修改模板二维码显示状态")
    @ApiOperation(value = "修改模板二维码显示状态" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "二维码显示状态" ,paramType = "query")
    })
    public Message updateTemplateState(String token ,Integer role , Integer id , Integer state){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("模板id不能为空");
        }
        if (state == null ){
            return message.code(Message.codeFailured).message("状态不能为空");
        }
        try {
            Integer count = menuService.updateTemplateState(id,state);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateTemplateState）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除log
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteLog")
    @SysLog(module = "模板管理" ,methods = "删除log")
    @ApiOperation(value = "删除log" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query")
    })
    public Message deleteTemplateLog(String token ,Integer role , Integer id){
        Message message = Message.non();
        try {
            Integer count = menuService.deleteTemplateLog(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除log失败");
            }
            return message.code(Message.codeSuccessed).message("删除log成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/deleteLog）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过员工id查询一级菜单
     * @param token
     * @param staff_id
     * @return
     */
    @RequestMapping("/findStaffOneMenu")
    @ApiOperation(value = "通过员工id查询一级菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message findStaffOneMenu(String token ,Integer role ,Integer staff_id ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(staff_id)){
            return message.code(Message.codeFailured).message("员工id不能为空");
        }
        try {
            List<Menu> oneMenu = menuService.findStaffOneMenu(staff_id);
            return message.code(Message.codeSuccessed).data(oneMenu).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/findStaffOneMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过员工id和一级菜单id查询二级菜单
     * @param token
     * @param staff_id
     * @return
     */
    @RequestMapping("/findStaffTwoMenu")
    @ApiOperation(value = "通过员工id和一级菜单id查询二级菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "sort" ,value = "一级菜单id" ,paramType = "query")
    })
    public Message findStaffTwoMenu(String token ,Integer role ,Integer staff_id ,Integer sort ){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(staff_id)){
                return message.code(Message.codeFailured).message("员工id不能为空");
            }
            if (IntegerUtils.isEmpty(sort)){
                return message.code(Message.codeFailured).message("一级菜单id不能为空");
            }

            List<Menu> oneMenu = menuService.findStaffTwoMenu(staff_id ,sort);
            return message.code(Message.codeSuccessed).data(oneMenu).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/findStaffTwoMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 通过员工id和二级菜单id查询三级菜单
     * @param token
     * @param staff_id
     * @return
     */
    @RequestMapping("/findStaffThreeMenu")
    @ApiOperation(value = "通过员工id和二级菜单id查询三级菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "sort" ,value = "二级菜单id" ,paramType = "query")
    })
    public Message findStaffThreeMenu(String token ,Integer role ,Integer staff_id ,Integer sort ){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(staff_id)){
                return message.code(Message.codeFailured).message("员工id不能为空");
            }
            if (IntegerUtils.isEmpty(sort)){
                return message.code(Message.codeFailured).message("二级菜单id不能为空");
            }

            List<Menu> threeMenu = menuService.findStaffThreeMenu(staff_id ,sort);
            return message.code(Message.codeSuccessed).data(threeMenu).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/findStaffThreeMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 修改员工菜单权限
     * @param token
     * @return
     */
    @RequestMapping("/updateStaffMenu")
    @SysLog(module = "商家设置" ,methods = "修改员工菜单权限")
    @ApiOperation(value = "修改员工菜单权限" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "{'ontMenus':'[{'m_id':1 ,'state':1}]','twoMenus':'[{'m_id':1 ,'state':1}]','threeMenus':'[{'m_id':1 ,'state':1}]',}" ,paramType = "query"),
            @ApiImplicitParam(name = "" ,value = "0没权限，1有权限" ,paramType = "query")
    })
    public Message updateStaffMenu(String token ,Integer role ,String json){
        Message message = Message.non();
        try {
            Exp exp = JSONObject.parseObject(json, Exp.class);
            Integer count = menuService.updateStaffMenu(exp.getOntMenus() ,exp.getTwoMenus() ,exp.getThreeMenus());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateStaffMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 三级菜单
     * @param token
     * @param role
     * @param sort
     * @return
     */
    @RequestMapping("/selectThreeMenu")
    @ApiOperation(value = "三级菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sort" ,value = "二级菜单id" ,paramType = "query")
    })
    public Message selectThreeMenu(String token ,Integer role ,Integer sort){
        Message message = Message.non();
        try {
            if (role != 1 && role != 2) {
                return message.code(Message.codeFailured).message("角色不能为空");
            }
            List<Menu> menus = menuService.selectThreeMenu(token, role, sort);
            return message.code(Message.codeSuccessed).data(menus).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/selectThreeMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /** 员工APP菜单
     * @param token
     * @param role
     * @return
             */
    @RequestMapping("/selecStaffAppMenu")
    @ApiOperation(value = "员工APP菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query")
    })
    public Message selecStaffAppMenu(String token ,Integer role ,Integer staff_id){
        Message message = Message.non();
        try {

            List<Menu> threeMenu = menuService.selecStaffAppMenu(staff_id);
            return message.code(Message.codeSuccessed).data(threeMenu).message("获取成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/selecStaffAppMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /** 修改员工APP菜单
     * @param token
     * @param role
     * @return
     */
    @RequestMapping("/updateStaffAppMenu")
    @ApiOperation(value = "修改员工APP菜单" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "{'ontMenus':[{'id':1 ,'state':1}]" ,paramType = "query")
    })
    public Message updateStaffAppMenu(String token ,Integer role ,String json){
        Message message = Message.non();
        try {
            Exp exp = JSONObject.parseObject(json, Exp.class);
            Integer count = menuService.updateStaffAppMenu(exp.getOntMenus());
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateStaffAppMenu）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 添加飞蛾打印机
     * @param
     * @return
     */
    @RequestMapping("/addPrinter")
    @SysLog(module = "飞蛾打印机设置" ,methods = "添加飞蛾打印机")
    @ApiOperation(value = "添加飞蛾打印机" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号(必填)" ,paramType = "query"),
            @ApiImplicitParam(name = "key" ,value = "打印机识别码(必填)" ,paramType = "query"),
            @ApiImplicitParam(name = "remark" ,value = "备注" ,paramType = "query"),
            @ApiImplicitParam(name = "carnum" ,value = "流量卡" ,paramType = "query"),
            @ApiImplicitParam(name = "num" ,value = "打印次数" ,paramType = "query")
    })
    public Message addPrinter(String token , Integer role , String sn, String key, String remark, String carnum ,Integer num  ,Integer ors){
        Message message = Message.non();
        try {

            Integer s = menuService.addPrinter(token ,role ,sn ,key ,remark ,carnum ,num ,ors);
            return message.code(Message.codeSuccessed).data(s).message("添加成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/addPrinter）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询店铺打印机
     * @return
     */
    @RequestMapping("/queryPrinter")
    @ApiOperation(value = "查询店铺打印机" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message queryPrinter(String token,Integer role ){
        Message message = Message.non();
        try {

            List<Printer> printers = menuService.queryPrinter(token ,role);
            return message.code(Message.codeSuccessed).data(printers).message("查询成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/queryPrinter）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 查询打印机状态接口
     * @param sn
     * @return
     */
    @RequestMapping("/queryPrinterStatus")
    @ApiOperation(value = "查询打印机状态接口" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号" ,paramType = "query")
    })
    public Message queryPrinterStatus(String token,Integer role ,String sn){
        Message message = Message.non();
        try {

            String msg = MothPrinter.queryPrinterStatus(sn);
            JSONObject json = JSONObject.parseObject(msg);
            String data = json.getString("data");
            byte[] bytes = data.getBytes();
            return message.code(Message.codeSuccessed).data(new String(bytes ,"UTF-8")).message("查询成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/queryPrinterStatus）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询指定打印机某天的订单统计数
     * @param sn
     * @return
     */
    @RequestMapping("/queryOrderInfoByDate")
    @ApiOperation(value = "查询指定打印机某天的订单统计数" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号" ,paramType = "query"),
            @ApiImplicitParam(name = "date" ,value = "默认当天时间" ,paramType = "query")
    })
    public Message queryOrderInfoByDate(String token,Integer role ,String sn ,String date){
        Message message = Message.non();
        try {
            String day = DateUtil.getDay();

            if (date != null && date != ""){
                day = date ;
            }
            String s = MothPrinter.queryOrderInfoByDate(sn ,day);
            return message.code(Message.codeSuccessed).data(s).message("查询成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/queryOrderInfoByDate）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改打印机信息
     * @param sn
     * @return
     */
    @RequestMapping("/printerEdit")
    @ApiOperation(value = "修改打印机信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "打印机备注名称(必填)" ,paramType = "query"),
            @ApiImplicitParam(name = "phonenum" ,value = "打印机流量卡号码" ,paramType = "query"),
            @ApiImplicitParam(name = "num" ,value = "打印次数" ,paramType = "query")
    })
    public Message printerEdit(String token,Integer role ,String sn,String name ,String phonenum ,Integer num ,Integer ors){
        Message message = Message.non();
        try {
            Integer count = menuService.printerEdit( token ,role ,sn , name ,phonenum ,num ,  ors);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/printerEdit）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除打印机
     * @param sn
     * @return
     */
    @RequestMapping("/printerDelList")
    @ApiOperation(value = "删除打印机" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号" ,paramType = "query")
    })
    public Message printerDelList(String token,Integer role ,String sn){
        Message message = Message.non();
        try {
            Integer count = menuService.printerDelList(token ,role ,sn );
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/printerDelList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 清空待打印队列
     * @param sn
     * @return
     */
    @RequestMapping("/delPrinterSqs")
    @ApiOperation(value = "清空待打印队列" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "sn" ,value = "打印机编号" ,paramType = "query")
    })
    public Message delPrinterSqs(String token,Integer role ,String sn){
        Message message = Message.non();
        try {
            String msg = MothPrinter.delPrinterSqs(sn);
            JSONObject json = JSONObject.parseObject(msg);
            Integer ret = json.getInteger("ret");
            if (ret == -2) {
                String msg1 = json.getString("msg");
                byte[] bytes = msg1.getBytes();
                String test = new String(bytes, "UTF-8");
                return message.code(Message.codeFailured).message(test);
            }else if (ret == 1002){
                return message.code(Message.codeFailured).message("验证失败 : 没有找到该打印机的信息");
            }
            return message.code(Message.codeSuccessed).message("成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/delPrinterSqs）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 启用打印机
     * @return
     */
    @RequestMapping("/enablePrinter")
    @ApiOperation(value = "启用打印机" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "打印机id" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "0启用，1不启用" ,paramType = "query")
    })
    public Message  enablePrinter(String token ,Integer role ,Integer id ,Integer state){
        Message message = Message.non();
        try {
            Integer count = menuService.enablePrinter(id ,state );
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("操作失败");
            }
            return message.code(Message.codeSuccessed).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/enablePrinter）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过id查询打印机信息
     * @return
     */
    @RequestMapping("/selectPrinterById")
    @ApiOperation(value = "通过id查询打印机信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "打印机id" ,paramType = "query")
    })
    public Message  selectPrinterById(String token ,Integer role ,Integer id){
        Message message = Message.non();
        try {
            Printer printer = menuService.selectPrinterById(id);
            return message.code(Message.codeSuccessed).data(printer).message("操作成功");
        }catch (Exception e){
            log.error("菜单控制层（/menu/selectPrinterById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 修改店铺使用哪个单据模块
     * @return
     */
    @RequestMapping("/updateBusinessPrinterModular")
    @ApiOperation(value = "修改店铺使用哪个单据模块" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0（默认） ，1飞蛾打印机（有加单据模块的以此推类）" ,paramType = "query")
    })
    public Message updateBusinessPrinterModular(String token ,Integer role ,Integer type){
        Message message = Message.non();
        try {
            Integer count = menuService.updateBusinessPrinterModular(token ,role  ,type);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("操作失败");
            }
            return message.code(Message.codeSuccessed).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/updateBusinessPrinterModular）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询店铺使用哪个单据模块
     * @return
     */
    @RequestMapping("/queryBusinessPrinterModular")
    @ApiOperation(value = "查询店铺使用哪个单据模块" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message queryBusinessPrinterModular(String token ,Integer role ){
        Message message = Message.non();
        try {
            Integer count = menuService.queryBusinessPrinterModular(token ,role );
            return message.code(Message.codeSuccessed).data(count).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/queryBusinessPrinterModular）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 调飞蛾打印机打印（销售订单）
     * @return
     */
    @RequestMapping("/mothPrintSale")
    @ApiOperation(value = "调飞蛾打印机打印（销售订单）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "titile" ,value = "打印模块名称" ,paramType = "query")
    })
    public Message mothPrintSale(String token ,Integer role ,Integer order_id ,String titile){
        Message message = Message.non();
        try {
            menuService.mothPrintSale(token , role , order_id  , titile );
            return message.code(Message.codeSuccessed).message("打印成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/mothPrint）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 调飞蛾打印机打印（采购订单）
     * @return
     */
    @RequestMapping("/mothPrintPurchase")
    @ApiOperation(value = "调飞蛾打印机打印（采购订单）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "titile" ,value = "打印模块名称" ,paramType = "query")
    })
    public Message mothPrintPurchase(String token ,Integer role ,Integer order_id  ,String titile){
        Message message = Message.non();
        try {
            menuService.mothPrintPurchase(token , role , order_id  , titile );
            return message.code(Message.codeSuccessed).message("");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/mothPrintPurchase）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 调飞蛾打印机打印（临时订单）
     * @return
     */
    @RequestMapping("/mothPrintTemporarySale")
    @ApiOperation(value = "调飞蛾打印机打印（临时订单）" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "order_id" ,value = "订单id" ,paramType = "query"),
            @ApiImplicitParam(name = "titile" ,value = "打印模块名称" ,paramType = "query")
    })
    public Message mothPrintTemporarySale(String token ,Integer role ,Integer order_id ,String titile){
        Message message = Message.non();
        try {
            menuService.mothPrintTemporarySale(token , role , order_id  , titile );
            return message.code(Message.codeSuccessed).message("打印成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/mothPrintTemporarySale）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 飞蛾积分兑换打印
     * @return
     */
    @RequestMapping("/mothPrintExchange")
    @ApiOperation(value = "飞蛾积分兑换打印" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "goodsName" ,value = "产品名称" ,paramType = "query"),
            @ApiImplicitParam(name = "price" ,value = "兑换积分" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "订单编号" ,paramType = "query"),
            @ApiImplicitParam(name = "num" ,value = "数量" ,paramType = "query"),
            @ApiImplicitParam(name = "userName" ,value = "用户名称" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "address" ,value = "地址" ,paramType = "query"),
            @ApiImplicitParam(name = "time" ,value = "兑换时间" ,paramType = "query")
    })
    public Message mothPrintExchange(String token ,Integer role ,String goodsName  ,String price ,String number ,String num ,String userName ,String phone ,String address ,String time){
        Message message = Message.non();
        try {
            menuService.mothPrintExchange(token , role , goodsName  , price ,number ,num ,userName ,phone , address ,time );
            return message.code(Message.codeSuccessed).message("打印成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("菜单控制层（/menu/mothPrintExchange）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


}
