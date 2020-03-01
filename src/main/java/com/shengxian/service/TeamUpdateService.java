package com.shengxian.service;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/6/2
 * @Version: 1.0
 */
public interface TeamUpdateService {

    //每月定时员工提成统计
    void teamUpdate();

    //清除结算状态
    void settlement();

    void  cacheEvict();
}
