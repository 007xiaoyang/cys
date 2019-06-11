package com.shengxian.controller;

import com.shengxian.common.Message;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.common.util.ToolUTtil;
import com.shengxian.entity.Parameter;
import com.shengxian.entity.Suppliers;
import com.shengxian.service.ExcelService;
import com.shengxian.service.SuppliersService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * Description: 供应商
 *
 * @Author: yang
 * @Date: 2018/8/22
 * @Version: 1.0
 */
@Api(description = "供应商")
@RestController
@RequestMapping("/suppliers")
public class SuppliersController {

    @Resource
    private SuppliersService suppliersService;
    @Resource
    private ExcelService excelService;

    /**
     * 添加供应商类别信息
     * @return
     */
    @RequestMapping("/addSupplierCategory")
    @SysLog(module = "供应商管理" ,methods = "添加供应商类别")
    @ApiOperation(value = "添加供应商类别信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商类别名称" ,paramType = "query")
    })
    public Message addSupplierCategory(String token ,Integer role , String name){
        Message message = Message.non();
        if (StringUtils.isEmpty(name)){
            return message.code(Message.codeFailured).message("请输入类别名称");
        }
        Integer count = suppliersService.addSupplierCategory(token ,role , name);
        if (IntegerUtils.isEmpty(count)){
            return message.code(Message.codeFailured).message("添加失败");
        }
        return message.code(Message.codeSuccessed).message("添加成功");
    }

    /**
     * 查询服务商下的供应商类别信息集合
     * @param token
     * @return
     */
    @RequestMapping("/findSuppliersCategoryInfoList")
    @ApiOperation(value = "查询服务商下的供应商类别信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query")
    })
    public Message findSuppliersCategoryInfoList(String token ,Integer role ){
        Message message = Message.non();
        try {
            List<HashMap> hashMap = suppliersService.findSuppliersCategoryInfoList(token ,role );
            return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }
    }

    /**
     * 修改供应商类别信息
     * @param id 类别id
     * @param name
     * @return
     */
    @RequestMapping("/updateSuppliersCategory")
    @SysLog(module = "供应商管理" ,methods = "修改供应商类别")
    @ApiOperation(value = "修改供应商类别信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商类别名称" ,paramType = "query")
    })
    public Message updateSuppliersCategory(String token ,Integer role , Integer id , String name){
        Message message = Message.non();
        if (StringUtils.isEmpty(name) ){
            return message.code(Message.codeFailured).message("请输入类别名称");
        }
        try {
            Integer count = suppliersService.updateSuppliersCategory(id, name);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除供应商类别
     * @param id
     * @return
     */
    @RequestMapping("/deleSuppliersCategory")
    @SysLog(module = "供应商管理" ,methods = "删除供应商类别")
    @ApiOperation(value = "删除供应商类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message deleSuppliersCategory(String token, Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        try {
            Integer count = suppliersService.deleSuppliersCategory(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
     }


    /**
     * 挑选类别自动生成供应商编号
     * @param id
     * @return
     */
    @RequestMapping("/selectNumberByCategoryId")
    @ApiOperation(value = "删除供应商类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message selectNumberByCategoryId(String token ,Integer role ,Integer id){
        Message message = Message.non();

        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        try {
            String number = suppliersService.selectNumberByCategoryId(id);
            if (StringUtils.isEmpty(number)){
                return message.code(Message.codeFailured).message("获取失败");
            }
            return message.code(Message.codeSuccessed).data(number).message("查询成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加供应商信息
     * @return
     */
    @RequestMapping("/addSuppliersInfo")
    @SysLog(module = "供应商管理" ,methods = "添加供应商")
    @ApiOperation(value = "删除供应商类别" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "编号" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "address" ,value = "地址" ,paramType = "query"),
            @ApiImplicitParam(name = "illustrated" ,value = "说明" ,paramType = "query"),
            @ApiImplicitParam(name = "remarks" ,value = "备注" ,paramType = "query")
    })
    public Message addSuppliersInfo(String token ,Integer role , Suppliers suppliers){
        Message message = Message.non();

        if (ToolUTtil.isEmpty(suppliers)){
            return message.code(Message.codeFailured).message("操作异常");
        }
        if (IntegerUtils.isEmpty(suppliers.getCategory_id() )){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        if ( StringUtils.isEmpty(suppliers.getNumber())){
            return message.code(Message.codeFailured).message("请输入供应商编号");
        }
        try {
            Integer count = suppliersService.addSuppliersInfo(token ,role , suppliers);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("添加供应商失败");
            }
            return message.code(Message.codeSuccessed).message("添加供应商成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 分页条件查询类别下的供应商信息集合
     * @param token
     * @param pageNo
     * @param categoryId 供应商类别ID
     * @param number 供应商编号
     * @param name 供应商名称
     * @return
     */
    @RequestMapping("/findSuppliersInfoList")
    @ApiOperation(value = "分页条件查询类别下的供应商信息集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "供应商编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称" ,paramType = "query")
    })
    public Message findSuppliersInfoList(String token ,Integer role , Integer pageNo , Integer categoryId, String number, String name){
        Message message = Message.non();
        try {
            Page page = suppliersService.findSuppliersInfoList(token, role, pageNo, categoryId, number, name);
            return message.code(Message.codeSuccessed).data(page).message("查询成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 根据供应商id查询供应商信息
     * @param token
     * @param id 供应商id
     * @return
     */
    @RequestMapping("/findSuppliersInfoById")
    @ApiOperation(value = "根据供应商id查询供应商信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNo" ,value = "页数" ,paramType = "query"),
            @ApiImplicitParam(name = "categoryId" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "供应商编号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "供应商名称" ,paramType = "query")
    })
    public Message findSuppliersInfoById(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入供应商id");
        }
        HashMap hashMap = suppliersService.findSuppliersInfoById(id);
        if (hashMap == null){
            return message.code(Message.codeFailured).message("供应商id不存在");
        }
        return message.code(Message.codeSuccessed).data(hashMap).message("查询成功");
    }

    /**
     *修改供应商信息
     * @param suppliers
     * @return
     */
    @RequestMapping("/updateSuppliersInfoById")
    @SysLog(module = "供应商管理" ,methods = "修改供应商")
    @ApiOperation(value = "修改供应商信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "供应商id" ,paramType = "query"),
            @ApiImplicitParam(name = "category_id" ,value = "类别id" ,paramType = "query"),
            @ApiImplicitParam(name = "number" ,value = "编号" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "手机号" ,paramType = "query"),
            @ApiImplicitParam(name = "name" ,value = "名称" ,paramType = "query"),
            @ApiImplicitParam(name = "address" ,value = "地址" ,paramType = "query"),
            @ApiImplicitParam(name = "illustrated" ,value = "说明" ,paramType = "query"),
            @ApiImplicitParam(name = "remarks" ,value = "备注" ,paramType = "query")
    })
    public Message updateSuppliersInfoById(String token, Suppliers suppliers){
        Message message = Message.non();
        if (ToolUTtil.isEmpty(suppliers)){
            return message.code(Message.codeFailured).message("操作异常");
        }
        if (suppliers.getCategory_id() == null || suppliers.getCategory_id() == 0){
            return message.code(Message.codeFailured).message("请输入类别id");
        }
        if (suppliers.getNumber() == null || suppliers.getNumber().equals("")){
            return message.code(Message.codeFailured).message("请输入供应商编号");
        }
        try {
            Integer count = suppliersService.updateSuppliersInfoById(suppliers);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            } else if (count == -1) {
                return message.code(Message.codeFailured).message("当前类别下的编号已存在");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 删除供应商
     * @param id
     * @return
     */
    @RequestMapping("/deleSuppliers")
    @SysLog(module = "供应商管理" ,methods = "删除供应商")
    @ApiOperation(value = "删除供应商" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "供应商id" ,paramType = "query")
    })
    public Message deleSuppliers(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id)){
            return message.code(Message.codeFailured).message("请输入供应商id");
        }
        try {
            Integer count = suppliersService.deleSuppliers(id);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("删除失败");
            }
            return message.code(Message.codeSuccessed).message("删除成功");
        }catch (NullPointerException e){
            return message.code(Message.codeFailured).message(e.getMessage());
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 导出供应商资料
     * @param response
     * @param
     * @return
     */
    @RequestMapping("/excelDownload")
    @ApiOperation(value = "删除供应商" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
    })
    public void excelDownload(String token ,Integer role , HttpServletResponse response, Parameter parameter){
        Message message = Message.non();
        HSSFWorkbook workbook = suppliersService.excelDownload(token ,role ,parameter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName =dateFormat.format(new Date())+"供应商资料"; //文件名
        excelService.excelDownload(response,fileName,workbook);
    }


    /**
     * 通过类别id查询类别下的供应商集合
     * @param token
     * @param id
     * @return
     */
    @RequestMapping("/findSuppliersList")
    @ApiOperation(value = "通过类别id查询类别下的供应商集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "类别id" ,paramType = "query")
    })
    public Message findSuppliersList(String token ,Integer role , Integer id){
        Message message = Message.non();
        if (IntegerUtils.isEmpty(id) ){
            return message.code(Message.codeFailured).message("请输入供应商类别id");
        }
        try {
            List<HashMap> hashMaps = suppliersService.findSuppliersList(id);
            return message.code(Message.codeSuccessed).data(hashMaps).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


}
