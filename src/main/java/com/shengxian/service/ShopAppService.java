package com.shengxian.service;

import com.shengxian.entity.WxloginInfo;
import io.swagger.models.auth.In;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-06-19
 * @Version: 1.1
 */
public interface ShopAppService  {

    /**
     * 查询店铺PP版本号
     * @return
     * @throws Exception
     */
    String version()throws Exception;

    /**
     * 店铺app中判断在是否有退出登录过
     * @param phone 手机号
     * @param model 手机型号
     * @param system  操作系统版本
     * @param version F: 引擎版本号
     * @param platform 客户端平台
     * @param SDKVersion  客户端基础库版本
     * @return
     */
    boolean appIsLogin(String token ,String phone ,String model,String system,String version ,String platform ,String SDKVersion)throws Exception;

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
    Integer updateEquipment(String token,String model,String system,String version,String platform ,String SDKVersion)throws Exception;

    /**
     * 获取用户登录信息
     * @param wxloginInfo
     * @return
     */
    WxloginInfo getUserLoginInfo(WxloginInfo wxloginInfo);

    /**
     * 修改用户登录信息
     * @param wxloginInfo
     * @return
     * @throws Exception
     */
    Integer updataUserLoginInfo(WxloginInfo wxloginInfo)throws Exception;

}
