package com.shengxian.service.impl;

import com.shengxian.entity.WxloginInfo;
import com.shengxian.mapper.ShopAppMapper;
import com.shengxian.service.ShopAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-06-19
 * @Version: 1.1
 */
@Service
public class ShopAppServiceImpl implements ShopAppService {

    @Autowired
    private ShopAppMapper shopAppMapper;

    //查询员工APP版本号
    @Override
    public String version()  {
        return shopAppMapper.version();
    }

    //app中判断在是否有退出登录过
    @Override
    public boolean appIsLogin(String token ,String phone, String model, String system, String version, String platform, String SDKVersion) throws Exception{
        Integer bool = shopAppMapper.appIsLogin(token , phone , model, system, version, platform, SDKVersion);
        if (bool != null){
            return true;
        }
        return false;
    }

    //修改手机设备
    @Override
    @Transactional
    public Integer updateEquipment(String token, String model, String system, String version, String platform, String SDKVersion) throws Exception{
        return shopAppMapper.updateEquipment(token,model,system,version,platform,SDKVersion);
    }

    @Override
    public WxloginInfo getUserLoginInfo(WxloginInfo wxloginInfo) {
        return shopAppMapper.getUserLoginInfo(wxloginInfo);
    }

}
