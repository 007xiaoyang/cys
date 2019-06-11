package com.shengxian.controller;

import com.shengxian.common.Message;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.entity.Template;
import com.shengxian.service.TemplateService;
import com.shengxian.sysLog.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Generated;
import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 模板控制层
 *
 * @Author: yang
 * @Date: 2019-05-09
 * @Version: 1.0
 */
@Api(description = "模板控制层")
@RestController
@RequestMapping("/template")
public class TemplateController {

    private static Logger log = Logger.getLogger(TemplateController.class);

    @Resource
    private TemplateService templateService;

    /**
     * 添加模板2
     * @return
     */
    @RequestMapping("/addTemplateTwo")
    @ApiOperation(value = "添加模板2" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3退货销售订单" ,paramType = "query"),
            @ApiImplicitParam(name = "one" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "two" ,value = "" ,paramType = "query")
    })
    public Message addTemplateTwo(Integer business_id ,String title ,Integer type ,String one , String two){
        Message message = Message.non();
        Integer count = templateService.addTemplateTwo(business_id, title ,type , one ,two);
        if (IntegerUtils.isEmpty(count)){
            return message.code(Message.codeFailured).message("添加失败");
        }
        return message.code(Message.codeSuccessed).message("添加成功");
    }

    /**
     * 查询店铺模板2集合
     * @return
     */
    @RequestMapping("/selectTemplateTwoList")
    @ApiOperation(value = "查询店铺模板2集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
    })
    public Message selectTemplateTwoList(String token ,Integer role){
        Message message = Message.non();
        try {
            List<Template> templates = templateService.selectTemplateTwoList(token, role);
            return message.code(Message.codeSuccessed).data(templates).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateTwoList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 根据类型查询店铺模板2
     * @return
     */
    @RequestMapping("/selectTemplateTwo")
    @ApiOperation(value = "根据类型查询店铺模板2" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3退货销售订单" ,paramType = "query")
    })
    public Message selectTemplateTwo(String token ,Integer role ,Integer type ){
        Message message = Message.non();
        try {
            Template template = templateService.selectTemplateTwo(token, role, type);
            return message.code(Message.codeSuccessed).data(template).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateTwo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改模板2
     * @return
     */
    @RequestMapping("/updateTemplateTwo")
    @SysLog(module = "模板管理" ,methods = "修改模板2")
    @ApiOperation(value = "修改模板2" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "标题名称" ,paramType = "query"),
            @ApiImplicitParam(name = "one" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "two" ,value = "" ,paramType = "query")
    })
    public Message updateTemplateTwo(String token ,Integer role ,Integer id ,String title ,String one , String two){
        Message message = Message.non();
        try {
            Integer count = templateService.updateTemplateTwo(id, title, one, two);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/updateTemplateTwo）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 添加模板3
     * @return
     */
    @RequestMapping("/addTemplateThree")
    @ApiOperation(value = "添加模板3" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business_id" ,value = "店铺id" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3退货销售订单" ,paramType = "query"),
            @ApiImplicitParam(name = "one" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "two" ,value = "" ,paramType = "query")
    })
    public Message addTemplateThree(Integer business_id ,String title ,Integer type ,String one , String two){
        Message message = Message.non();
        Integer count = templateService.addTemplateThree(business_id, title ,type , one ,two);
        if (IntegerUtils.isEmpty(count)){
            return message.code(Message.codeFailured).message("添加失败");
        }
        return message.code(Message.codeSuccessed).message("添加成功");
    }

    /**
     * 查询店铺模板3集合
     * @return
     */
    @RequestMapping("/selectTemplateThreeList")
    @ApiOperation(value = "查询店铺模板3集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
    })
    public Message selectTemplateThreeList(String token ,Integer role){
        Message message = Message.non();
        try {
            List<Template> templates = templateService.selectTemplateThreeList(token, role);
            return message.code(Message.codeSuccessed).data(templates).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateThreeList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 根据类型查询店铺模板3
     * @return
     */
    @RequestMapping("/selectTemplateThree")
    @ApiOperation(value = "根据类型查询店铺模板3" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3退货销售订单" ,paramType = "query")
    })
    public Message selectTemplateThree(String token ,Integer role ,Integer type ){
        Message message = Message.non();
        try {
            Template template = templateService.selectTemplateThree(token, role, type);
            return message.code(Message.codeSuccessed).data(template).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateThree）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改模板3
     * @return
     */
    @RequestMapping("/updateTemplateThree")
    @SysLog(module = "模板管理" ,methods = "修改模板3")
    @ApiOperation(value = "修改模板3" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "标题名称" ,paramType = "query"),
            @ApiImplicitParam(name = "one" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "two" ,value = "" ,paramType = "query")
    })
    public Message updateTemplateThree(String token ,Integer role ,Integer id ,String title ,String one , String two){
        Message message = Message.non();
        try {
            Integer count = templateService.updateTemplateThree(id, title, one, two);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/updateTemplateThree）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 查询店铺模板5集合
     * @return
     */
    @RequestMapping("/selectTemplateFiveList")
    @ApiOperation(value = "查询店铺模板5集合" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
    })
    public Message selectTemplateFiveList(String token ,Integer role){
        Message message = Message.non();
        try {
            List<Template> templates = templateService.selectTemplateFiveList(token, role);
            return message.code(Message.codeSuccessed).data(templates).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateFiveList）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 根据类型查询店铺模板5
     * @return
     */
    @RequestMapping("/selectTemplateFive")
    @ApiOperation(value = "根据类型查询店铺模板5" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "type" ,value = "0采购订单,1采购退货订单，2销售订单，3退货销售订单" ,paramType = "query")
    })
    public Message selectTemplateFive(String token ,Integer role ,Integer type ){
        Message message = Message.non();
        try {
            Template template = templateService.selectTemplateFive(token, role, type);
            return message.code(Message.codeSuccessed).data(template).message("添加成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/selectTemplateFive）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }

    /**
     * 修改模板5
     * @return
     */
    @RequestMapping("/updateTemplateFive")
    @SysLog(module = "模板管理" ,methods = "修改模板5")
    @ApiOperation(value = "修改模板5" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "id" ,value = "模板id" ,paramType = "query"),
            @ApiImplicitParam(name = "title" ,value = "标题名称" ,paramType = "query"),
            @ApiImplicitParam(name = "one" ,value = "" ,paramType = "query"),
            @ApiImplicitParam(name = "state" ,value = "1启动二维码" ,paramType = "query")
    })
    public Message updateTemplateFive(String token ,Integer role ,Integer id ,String title ,String one , Integer state){
        Message message = Message.non();
        try {
            Integer count = templateService.updateTemplateFive(id, title, one, state);
            if (IntegerUtils.isEmpty(count)) {
                return message.code(Message.codeFailured).message("修改失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e) {
            log.error("模板控制层（/template/updateTemplateFive）接口报错---------"+e.getMessage());
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


}
