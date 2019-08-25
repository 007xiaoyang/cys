package com.shengxian.controller;

import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.Message;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.common.util.StringUtil;
import com.shengxian.entity.BindUser;
import com.shengxian.entity.Parameter;
import com.shengxian.service.ExcelService;
import com.shengxian.service.UserService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 用户
 *
 * @Author: yang
 * @Date: 2018/8/31
 * @Version: 1.0
 */
@Api(description = "用户资料")
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger log = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @Resource
    private ExcelService excelService;

    /**
     * 添加用户类别
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/addUserCategory")
    @SysLog(module = "用户管理",methods = "添加客户类别")
    @ApiOperation(value = "添加客户类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称" ,paramType = "query")
    })
    public Message addUserCategory(String token ,Integer role , String name){
        Message message = Message.non();
        if (StringUtils.isEmpty(name)){
            return message.code(Message.codeFailured).message("请输入类别名称");
        }
        try {
            Integer count = userService.addUserCategory(token, role, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (Exception e){
            log.error("员工控制层（/user/addUserCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询商家下的所有用户类别集合
     * @param token
     * @return
     */
    @RequestMapping("/findUserCategoryList")
    @ApiOperation(value = "添加客户类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message findUserCategoryList(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMap = userService.findUserCategoryList(token ,role);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/findUserCategoryList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 根据用户类别id查询类别信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findUserCategoryById")
    @ApiOperation(value = "根据用户类别id查询类别信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query"),
    })
    public Message findUserCategoryById(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        HashMap hashMap = userService.findUserCategoryById(id);
        if (hashMap == null ){
            return message.code(Message.codeFailured).message("客户类别id不存在");
        }
        return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
    }


    /**
     * 修改用户类别信息
     * @param token
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/updateUserCategory")
    @SysLog(module = "用户管理",methods = "修改用户类别")
    @ApiOperation(value = "修改用户类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "类别名称" ,paramType = "query")
    })
    public Message updateUserCategory(String token ,Integer role , Integer id, String name){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入客户类别id");
        }
        try {
            Integer count = userService.updateUserCategory(id, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            log.error("员工控制层（/user/updateUserCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除用户类别
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleUserCategory")
    @SysLog(module = "用户管理",methods = "删除用户类别")
    @ApiOperation(value = "删除用户类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message deleUserCategory(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入用户类别id");
        }
        try {
            Integer count = userService.deleUserCategory(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/deleUserCategory）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 挑选类别自动生成用户编号
     * @param token
     * @param id 用户类别ID
     * @return
     */
    @RequestMapping("/selectNumberByCategoryId")
    @ApiOperation(value = "挑选类别自动生成用户编号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "用户类别ID" ,paramType = "query")
    })
    public Message selectNumberByCategoryId(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入客户类别id");
        }
        String number = userService.selectNumberByCategoryId(id);
        if (StringUtils.isEmpty(number)){
            return message.code(Message.codeFailured).message("自动生成编号失败");
        }
        return message.code(Message.codeSuccessed).data(number).message("查询成功");
    }


    /**
     * 添加用户
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/addUser")
    @SysLog(module = "用户管理",methods = "添加用户")
    @ApiOperation(value = "添加用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message addCustomer(String token ,Integer role , String json){

        Message message = Message.non();
        try {
            BindUser bindUser = JSONObject.parseObject(json, BindUser.class);

            Integer count = userService.addUser(token , role , bindUser);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加失败");
            }
            return message.code(Message.codeSuccessed).message("添加成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/addCustomer）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 搜索产品
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/findGoods")
    @ApiOperation(value = "搜索产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "产品名称或编号" ,paramType = "query")
    })
    public Message findGoods(String token ,Integer role , String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMap = userService.findGoods(token ,role , names);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 搜索员工
     * @param token
     * @param name
     * @return
     */
    @RequestMapping("/findStaff")
    @ApiOperation(value = "搜索员工" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "员工名称或编号" ,paramType = "query")
    })
    public Message findStaff(String token ,Integer role , String name){
        Message message = Message.non();
        String names = StringUtil.StringFilter(name);
        try {
            List<HashMap> hashMap = userService.findStaff(token ,role , names);
            return message.code(Message.codeSuccessed).data(hashMap).message("操作成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/findStaff）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 条件查询类别下的用户信息集合
     * @param token
     * @return
     */
    @RequestMapping("/findCustomerInfoList")
    @ApiOperation(value = "条件查询类别下的用户信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "员工名称或编号" ,paramType = "query")
    })
    public Message findCustomerInfoList(String token ,Integer role , Parameter parameter){
        Message message = Message.non();
        parameter.setName(  StringUtil.StringFilter(parameter.getName()));
        try {
            Page page = userService.findCustomerInfoList(token ,role , parameter);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/findCustomerInfoList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param token
     * @param bandingId 绑定用户ID
     * @return
     */
    @RequestMapping("/findCostomerInfoById")
    @ApiOperation(value = "根据用户id查询用户信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "bandingId" ,value = "绑定用户ID" ,paramType = "query")
    })
    public Message findCostomerInfoById(String token ,Integer role , Integer bandingId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(bandingId)){
            return message.code(Message.codeFailured).message("请输入绑定用户id");
        }
        try {
            HashMap hashMap = userService.findCostomerInfoById(bandingId);
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/findCostomerInfoById）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }


    }

    /**
     * 修改用户信息
     * @param token
     * @param json
     * @return
     */
    @RequestMapping("/updateUser")
    @SysLog(module = "用户管理",methods = "修改用户")
    @ApiOperation(value = "修改用户信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "json" ,value = "json格式" ,paramType = "query")
    })
    public Message updateUser(String token ,Integer role , String json){
        Message message = Message.non();

        try {
            BindUser bindUser = JSONObject.parseObject(json, BindUser.class);
            if ( IntegerUtils.isEmpty(bindUser.getId() )){
                return message.code(Message.codeFailured).message("请输入绑定用户ID");
            }

            Integer count = userService.updateUser(token , role ,bindUser);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/updateUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除用户
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/deleteUser")
    @SysLog(module = "用户管理",methods = "删除用户")
    @ApiOperation(value = "删除用户" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "用户id" ,paramType = "query")
    })
    public Message deleteUser(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入用户id");
        }
        try {
            Integer count = userService.deleteUser(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除用户失败");
            }
            return message.code(Message.codeSuccessed).message("删除用户成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/deleteUser）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }
    /**
     *  删除用户产品积分
     * @param token
     * @param integraId 积分产品ID
     * @return
     */
    @RequestMapping("/deleteUserGoodsIntegra")
    @SysLog(module = "用户管理",methods = "删除用户产品积分")
    @ApiOperation(value = "删除用户产品积分" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "integraId" ,value = "积分产品ID" ,paramType = "query")
    })
    public Message deleteUserGoodsIntegra(String token ,Integer role , Integer integraId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(integraId)){
            return message.code(Message.codeFailured).message("请输入积分ID");
        }
        try {
            Integer count = userService.deleteUserGoodsIntegra(integraId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除用户产品积分失败");
            }
            return message.code(Message.codeSuccessed).message("删除用户产品积分成功");
        }catch (Exception e){
            log.error("员工控制层（/user/deleteUserGoodsIntegra）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *  删除用户收藏产品
     * @param token
     * @param collectionId 收藏ID
     * @return
     */
    @RequestMapping("/deleteUserGoodsCollection")
    @SysLog(module = "用户管理",methods = "删除用户收藏产品")
    @ApiOperation(value = "删除用户收藏产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "collectionId" ,value = "收藏ID" ,paramType = "query")
    })
    public Message deleteUserGoodsCollection(String token ,Integer role , Integer collectionId){
        Message message = Message.non();
        if ( IntegerUtils.isEmpty(collectionId)){
            return message.code(Message.codeFailured).message("请输入积分ID");
        }
        try {
            Integer count = userService.deleteUserGoodsCollection(collectionId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除用户收藏产品失败");
            }
            return message.code(Message.codeSuccessed).message("删除用户收藏产品成功");
        }catch (Exception e){
            log.error("员工控制层（/user/deleteUserGoodsCollection）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     *  删除用户屏蔽产品
     * @param token
     * @param shieldingId 屏蔽ID
     * @return
     */
    @RequestMapping("/deleteUserGoodsShielding")
    @SysLog(module = "用户管理",methods = "删除用户屏蔽产品")
    @ApiOperation(value = "删除用户屏蔽产品" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "shieldingId" ,value = "屏蔽ID" ,paramType = "query")
    })
    public Message deleteUserGoodsShielding(String token ,Integer role , Integer shieldingId){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(shieldingId)){
            return message.code(Message.codeFailured).message("请输入积分ID");
        }
        try {
            Integer count = userService.deleteUserGoodsShielding(shieldingId);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除用户屏蔽产品失败");
            }
            return message.code(Message.codeSuccessed).message("删除用户屏蔽产品成功");
        }catch (Exception e){
            log.error("员工控制层（/user/deleteUserGoodsShielding）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 导出用户资料
     * @param token
     * @param parameter
     */
    @RequestMapping("/excelDownload")
    @ApiOperation(value = "导出用户资料" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public void excelDownload(String token ,Integer role , HttpServletResponse response, Parameter parameter){
        HSSFWorkbook workbook = userService.excelDownload(token ,role , parameter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"客户资料"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }

    /**
     * 查询类别下的用户信息集合
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findUserListtByCategoryId")
    @ApiOperation(value = "查询类别下的用户信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message findUserListtByCategoryId(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入用户类别id");
        }
        try {
            List<HashMap> hashMaps = userService.findUserListtByCategoryId(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            log.error("员工控制层（/user/findUserListtByCategoryId）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 注册客户APP账号
     * @param token
     * @param role
     * @param phone
     * @return
     */
    @RequestMapping("/addUserPhone")
    @SysLog(module = "资料管理" ,methods = "注册客户APP账号")
    @ApiOperation(value = "注册客户APP账号" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号，密码默认123456" ,paramType = "query")
    })
    public Message addUserPhone(String token ,Integer role ,String phone){
        Message message = Message.non();
        try {
            if (!IntegerUtils.isEmpty(phone)){
                return message.code(Message.codeFailured).message("手机号码不能为空");
            }
            Integer count = userService.addUserPhone(phone);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("注册失败");
            }
            return message.code(Message.codeSuccessed).message("注册成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            log.error("员工控制层（/user/addUserPhone）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }

    }

}
