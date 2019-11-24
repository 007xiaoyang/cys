package com.shengxian.controller;

import com.shengxian.common.Message;
import com.shengxian.common.util.Global;
import com.shengxian.common.util.IntegerUtils;
import com.shengxian.entity.WxloginInfo;
import com.shengxian.service.ShopAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 店铺APP
 *
 * @Author: yang
 * @Date: 2019-06-18
 * @Version: 1.1
 */
@Api(description = "店铺APP接口")
@RestController
@RequestMapping("/shopApp")
public class ShopAppController {

    private Logger log = Logger.getLogger(ShopController.class);

    @Autowired
    private ShopAppService shopAppService;

    /**
     * 查询店铺APP版本号
     * @return
     */
    @RequestMapping("/version")
    @ApiOperation(value = "查询店铺APP版本号" ,httpMethod = "POST")
    public Message version(){
        Message message = Message.non();
        try {
            String version = shopAppService.version();
            return message.code(Message.codeSuccessed).data(version).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 店铺APP中判断在是否有退出登录过
     * @param phone 手机号
     * @param model 手机型号
     * @param system  操作系统版本
     * @param version F: 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion  客户端基础库版本
     * @return
     */
    @RequestMapping("/appIsLogin")
    @ApiOperation(value = "店铺APP中判断在是否有退出登录过" ,httpMethod = "POST")
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
            boolean is_boolean = shopAppService.appIsLogin(token ,phone , model , system , version , platform , SDKVersion);
            return message.code(Message.codeSuccessed).data(is_boolean).message("获取成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 修改店铺APP手机设备
     * @param token
     * @param model 手机型号
     * @param system 操作系统版本
     * @param version 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion 客户端基础库版本
     * @return
     */
    @RequestMapping("/updateEquipment")
    @ApiOperation(value = "修改店铺APP手机设备" ,httpMethod = "POST")
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
            Integer count = shopAppService.updateEquipment(token,model,system,version,platform,SDKVersion);
            if (IntegerUtils.isEmpty(count)){
                return message.code(Message.codeFailured).message("修改手机设备失败");
            }
            return message.code(Message.codeSuccessed).message("修改成功");
        }catch (Exception e){
            return message.code(Message.codeFailured).message(Global.ERROR);
        }
    }


    /**
     * 获取用户登录信息
     * @param wxloginInfo
     * @return
     */
    @RequestMapping("/getUserLoginInfo")
    @ApiOperation(value = "获取用户登录信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "role" ,value = "1店铺，2员工" ,paramType = "query"),
            @ApiImplicitParam(name = "phone" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "loginType" ,value = "0用户 ，1店铺，2员工" ,paramType = "query")
    })
    public Message getUserLoginInfo(WxloginInfo wxloginInfo){
        Message message = Message.non();
        WxloginInfo userLoginInfo = shopAppService.getUserLoginInfo(wxloginInfo);
        return message.code(Message.codeSuccessed).data(userLoginInfo).message("获取成功");
    }
    /**
     * 修改用户登录信息
     * @param wxloginInfo
     * @return
     */
    @RequestMapping("/updataUserLoginInfo")
    @ApiOperation(value = "修改用户登录信息" ,httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone" ,value = "token" ,paramType = "query"),
            @ApiImplicitParam(name = "loginType" ,value = "0用户 ，1店铺，2员工" ,paramType = "query")
    })
    public Message updataUserLoginInfo(String token ,Integer role , WxloginInfo wxloginInfo){
        Message message = Message.non();
        try {
            shopAppService.updataUserLoginInfo(wxloginInfo);
        }catch (Exception e){
            log.error(e);
            return message.code(Message.codeFailured).message(e.getMessage());
        }
        return message.code(Message.codeSuccessed).message("获取成功");
    }



}
