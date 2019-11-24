package com.shengxian.mapper;

import com.shengxian.entity.WxloginInfo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-06-19
 * @Version: 1.1
 */
@Mapper
public interface ShopAppMapper {

    /**
     * 查询店铺APP版本号
     * @return
     */
    String version();

    /**
     * 店铺app中判断在是否有退出登录过
     * @param phone
     * @param model
     * @param system
     * @param version
     * @param platform
     * @param SDKVersion
     * @return
     */
    Integer appIsLogin(@Param("token") String token , @Param("phone") String phone , @Param("model") String model, @Param("system") String system, @Param("version") String version , @Param("platform") String platform , @Param("SDKVersion") String SDKVersion);


    /**
     * 修改店铺APP手机设备
     * @param token
     * @param model
     * @param system
     * @param version
     * @param platform
     * @param SDKVersion
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer  updateEquipment(@Param("token") String token,@Param("model") String model,@Param("system") String system,@Param("version") String version,@Param("platform") String platform ,@Param("SDKVersion") String SDKVersion);

    /**
     * 获取用户登录信息
     * @param wxloginInfo
     * @return
     */
    WxloginInfo getUserLoginInfo(WxloginInfo wxloginInfo);


    Integer addUserLoginInfo(WxloginInfo wxloginInfo);
    /**
     * 修改用户登录信息
     * @param wxloginInfo
     * @return
     */
    Integer updataUserLoginInfo(WxloginInfo wxloginInfo);

}
