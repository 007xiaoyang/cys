package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.common.util.StringUtil;
import com.shengxian.entity.Staff;
import com.shengxian.service.ExcelService;
import com.shengxian.service.StaffService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 员工
 *
 * @Author: yang
 * @Date: 2018/8/24
 * @Version: 1.0
 */
@Api(description = "员工")
@RestController
@RequestMapping("/staff")
public class StaffController {

    private static Logger log = Logger.getLogger(StaffController.class);

    @Resource
    private StaffService staffService;

    @Resource
    private ExcelService excelService;


    /**
     * 添加员工类别
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/addStaffCategory")
    @SysLog(module = "员工管理",methods = "添加员工类别")
    @ApiOperation(value = "添加员工类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称" ,paramType = "query")
    })
    public Message addStaffCategory(String token ,Integer role , String name){
        Message message = Message.non();
        if (StringUtils.isEmpty(name)){
            return message.code(Message.codeFailured).message("请输入员工类别名称");
        }
        try {
            Integer count = staffService.addStaffCategory(token ,role , name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (Exception e) {
            log.error("员工控制层（/staff/addStaffCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询服务商下的所有员工类别集合
     * @param token
     * @return
     */
    @RequestMapping("/findStaffCategoryInfoList")
    @ApiOperation(value = "查询服务商下的所有员工类别集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message findStaffCategoryInfoList(String token  ,Integer role){
        Message message = Message.non();
        try {
            List<HashMap> hashMap = staffService.findStaffCategoryInfoListBySid(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("员工控制层（/staff/findStaffCategoryInfoList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过员工类别id查询员工类别信息
     * @param token
     * @param categoryId
     * @return
     */
    @RequestMapping("/findStaffCategoryInfo")
    @ApiOperation(value = "通过员工类别id查询员工类别信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "类别id" ,paramType = "query")
    })
    public Message findStaffCategoryInfo(String token ,Integer role , Integer categoryId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(categoryId)){
            return message.code(Message.codeFailured).message("请输入员工类别id");
        }
        HashMap hashMap = staffService.findStaffCategoryInfoById(categoryId);
        if (hashMap == null){
            return message.code(Message.codeFailured).message("员工类别id不存在");
        }
        return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
    }

    /**
     * 修改员工类别信息
     * @param token
     * @param id 员工类别id
     * @return
     */
    @RequestMapping("/updateStaffCategory")
    @ApiOperation(value = "修改员工类别信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称" ,paramType = "query")
    })
    @SysLog(module = "员工管理",methods = "修改员工类别")
    public Message updateStaffCategory(String token , Integer role , Integer id, String name){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工类别id");
        }
        try {
            Integer count = staffService.updateStaffCategoryByid(id, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("员工控制层（/staff/updateStaffCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除员工类别
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleStaffCategory")
    @SysLog(module = "员工管理",methods = "删除员工类别")
    @ApiOperation(value = "删除员工类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工类别id" ,paramType = "query")
    })
    public Message deleStaffCategory(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工类别id");
        }
        try {
            Integer count = staffService.deleStaffCategory(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e) {
            log.error("员工控制层（/staff/deleStaffCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 挑选类别自动生成员工编号
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/automaticSelectStaffnumberByCategoryId")
    @ApiOperation(value = "挑选类别自动生成员工编号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工类别id" ,paramType = "query")
    })
    public Message automaticSelectStaffnumberByCategoryId(String token ,Integer role  , Integer id ){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工类别id");
        }
        try {
            String number = staffService.automaticSelectStaffnumberByCategoryId(id);
            if (StringUtils.isEmpty(number)) {
                return message.code(Message.codeFailured).message("自动生成编号失败");
            }
            return message.code(Message.codeSuccessed).data(number).message("查询成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过用户类别ID和用户编号或名称查询商家绑定用户的信息
     * @param token
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    @RequestMapping("/findBindingInfoByUser")
    @ApiOperation(value = "通过用户类别ID和用户编号或名称查询商家绑定用户的信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "用户类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "用户编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query")
    })
    public Message findBindingInfoByUser(String token, Integer categoryId, String number, String name){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(categoryId)) {
                return message.code(Message.codeFailured).message("请输入用户类别ID");
            }
            List<HashMap> hashMap = staffService.findBindingInfoByUser(categoryId, number, name);
            return message.code(Message.codeSuccessed).data(hashMap).message("获取成功");
        }catch (Exception e) {
            log.error("员工控制层（/staff/findBindingInfoByUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 通过产品类别ID和编号或产品名称查询产品信息
     * @param token
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    @RequestMapping("/findGoodsInfoByCid")
    @ApiOperation(value = "通过产品类别ID和编号或产品名称查询产品信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "用户类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "用户编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query")
    })
    public Message findGoodsInfoByCid(String token ,Integer role , Integer categoryId, String number, String name){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(categoryId)){
            return message.code(Message.codeFailured).message("请输入产品类别ID");
        }
        try {
            List<HashMap> hashMap = staffService.findGoodsInfoByCid(categoryId, number, name);
            return message.code(Message.codeFailured).data(hashMap).message("获取成功");
        }catch (Exception e) {
            log.error("员工控制层（/staff/findGoodsInfoByCid）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 搜索绑定用户
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/findBusinessUser")
    @ApiOperation(value = "搜索店铺绑定用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "用户类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "用户编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "用户名称" ,paramType = "query")
    })
    public Message findBusinessUser(String token ,Integer role  , String name){
        Message message = Message.non();
        try {
            List<HashMap> hashMaps = staffService.findBusinessUser(token ,role , name);
            return message.code(Message.codeSuccessed).data(hashMaps).message("操作成功");
        }catch (Exception e) {
            log.error("员工控制层（/staff/findBusinessUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加员工信息
     * @param token
     * @param staffJson
     * @return
     */
    @RequestMapping(value = "/addSstaff",method = RequestMethod.POST)
    @SysLog(module = "员工管理",methods = "添加员工")
    @ApiOperation(value = "添加员工信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staffJson" ,value = "json格式" ,paramType = "query")
    })
    public Message addSstaff(String token ,Integer role , String staffJson){
        Message message = Message.non();
        try {
            Staff staff = JSONObject.parseObject(staffJson, Staff.class);

            Integer count =  staffService.addSstaff(token ,role ,staff);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        } catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/staff/addSstaff）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 条件查询类别下的员工信息集合
     * @param token
     * @param pageNo
     * @param id 类别id
     * @param name 姓名
     * @return
     */
    @RequestMapping("/findStaffInfoList")
    @ApiOperation(value = "条件查询类别下的员工信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "姓名" ,paramType = "query")
    })
    public Message findStaffInfoList(String token ,Integer role , Integer pageNo, Integer id, String name){
        Message message = Message.non();
        try {
            Page page = staffService.findStaffInfoList(token , role ,pageNo,id,name);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/staff/findStaffInfoList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 根据员工id查询员工信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findStaffInfo")
    @ApiOperation(value = "根据员工id查询员工信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "页数" ,paramType = "query")
    })
    public Message findStaffInfo(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工id");
        }
        try {
            HashMap hashMap = staffService.findStaffInfoById(id);
            if (hashMap == null) {
                return message.code(Message.codeFailured).message("员工id存在");
            }
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/findStaffInfo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改员工信息
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/updateStaff")
    @SysLog(module = "员工管理",methods = "修改员工")
    @ApiOperation(value = "修改员工信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staffJson" ,value = "json格式" ,paramType = "query")
    })
    public Message updateStaff(String token ,Integer role , String json){
        Message message = Message.non();
        try {
            Staff staff = JSONObject.parseObject(json, Staff.class);
            if ( IntegerUtils.isEmpty(staff.getId())) {
                return message.code(Message.codeFailured).message("请输入员工ID");
            }
            Integer count = staffService.updateStaff(staff);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/staff/updateStaff）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除员工
     * @param token
     * @param id 员工id
     * @return
     */
    @RequestMapping("/deleSatff")
    @SysLog(module = "员工管理",methods = "删除员工")
    @ApiOperation(value = "删除员工" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工id" ,paramType = "query")
    })
    public Message deleSatff(String token ,Integer role , Integer id){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(id)) {
                return message.code(Message.codeFailured).message("请输入员工id");
            }
            Integer count = staffService.deleSatff(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/deleSatff）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 匹配员工密码
     * @param
     * @param staff_id
     * @param password
     * @return
     */
    @RequestMapping("/findStaffPWD")
    @ApiOperation(value = "匹配员工密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工id" ,paramType = "query")
    })
    public Message findStaffPWD(Integer staff_id, String password){
        Message message = Message.non();
        try {
            boolean is = staffService.findStaffPWD(staff_id,password);
            if (is == false){
                return message.code(Message.codeFailured).message("密码错误");
            }
            return message.code(Message.codeSuccessed).message("密码正确");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message.code(Message.codeFailured).message(Global.ERROR);
    }

    /**
     * 修改员工密码
     * @param token
     * @param staff_id 员工ID
     * @param password 密码
     * @return
     */
    @RequestMapping("/updateStaffPwd")
    @SysLog(module = "员工管理",methods = "修改员工密码")
    @ApiOperation(value = "修改员工密码" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "staff_id" ,value = "员工id" ,paramType = "query"),
            @ApiImplicitParam(name = "password" ,value = "密码" ,paramType = "query")
    })
    public Message updateStaffPwd(String token ,Integer role , Integer staff_id, String password){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(staff_id)){
            return message.code(Message.codeFailured).message("请输入员工ID");
        }
        try {
            Integer count = staffService.updateStaffPwd(staff_id,password);
            if ( IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message.code(Message.codeFailured).message(Global.ERROR);
    }

    /**
     * 删除员工的产品提成信息
     * @param token
     * @param id 员工的产品提成ID
     * @return
     */
    @RequestMapping("/deleteStaffGoodsPercent")
    @SysLog(module = "员工管理",methods = "删除员工的产品提成信息")
    @ApiOperation(value = "删除员工的产品提成信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工的产品提成ID" ,paramType = "query")
    })
    public Message deleteStaffGoodsPercent(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工的产品提成ID");
        }
        try {
            Integer count = staffService.deleteStaffGoodsPercent(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除员工的产品提成失败");
            }
            return message.code(Message.codeSuccessed).message("删除员工的产品提成成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/deleteStaffGoodsPercent）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除员工的用户提成信息
     * @param token
     * @param id 员工的用户提成ID
     * @return
     */
    @RequestMapping("/deleteStaffUserPercent")
    @SysLog(module = "员工管理",methods = "删除员工的用户提成信息")
    @ApiOperation(value = "删除员工的用户提成信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工的用户提成ID" ,paramType = "query")
    })
    public Message deleteStaffUserPercent(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工的用户提成ID");
        }

        try {
            Integer count = staffService.deleteStaffUserPercent(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除员工的用户提成失败");
            }
            return message.code(Message.codeSuccessed).message("删除员工的用户提成成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/deleteStaffUserPercent）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

    /**
     * 删除员工的仓库提成信息
     * @param token
     * @param id 员工的仓库提成ID
     * @return
     */
    @RequestMapping("/deleteStaffWarehousePercent")
    @SysLog(module = "员工管理",methods = "删除员工的仓库提成信息")
    @ApiOperation(value = "删除员工的仓库提成信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工的仓库提成ID" ,paramType = "query")
    })
    public Message deleteStaffWarehousePercent(String token, Integer id){
        Message message = Message.non();
        try {
            if (id == null || id == 0) {
                return message.code(Message.codeFailured).message("请输入员工的仓库提成ID");
            }
            Integer count = staffService.deleteStaffWarehousePercent(id);
            if (count == null || count == 0) {
                return message.code(Message.codeFailured).message("删除员工的仓库提成失败");
            }
            return message.code(Message.codeSuccessed).message("删除员工的仓库提成成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/deleteStaffWarehousePercent）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除员工的其它销售提成信息
     * @param token
     * @param id 员工的其它销售提成ID
     * @return
     */
    @RequestMapping("/deleteStaffOtherPercent")
    @SysLog(module = "员工管理",methods = "删除员工的其它销售提成信息")
    @ApiOperation(value = "删除员工的其它销售提成信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "员工的其它销售提成ID" ,paramType = "query")
    })
    public Message deleteStaffOtherPercent(String token ,Integer role , Integer id){
        Message message = Message.non();
        try {
            if (IntegerUtils.isEmpty(id)) {
                return message.code(Message.codeFailured).message("请输入员工的其它销售提成ID");
            }
            Integer count = staffService.deleteStaffOtherPercent(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除员工的其它销售提成失败");
            }
            return message.code(Message.codeSuccessed).message("删除员工的其它销售提成成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/deleteStaffOtherPercent）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询类别下所有员工信息集合
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findStaffInfoListByCategoryId")
    @ApiOperation(value = "查询类别下所有员工信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message findStaffInfoListByCategoryId(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入员工类别id");
        }
        try {
            List<HashMap> hashMaps = staffService.findStaffInfoListByCategoryId(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工控制层（/staff/findStaffInfoListByCategoryId）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 导出员工资料
     * @param token
     * @param
     */
    @RequestMapping("/excelDownload")
    @ApiOperation(value = "导出员工资料" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "员工手机" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "员工编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "员工名称" ,paramType = "query")
    })
    public void excelDownload(String token ,Integer role , HttpServletResponse response, Integer id, String phone, String number, String name){
        HSSFWorkbook workbook = staffService.excelDownload(token ,role , id,phone,number,name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"客户资料"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

}

