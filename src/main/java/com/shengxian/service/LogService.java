package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.LogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

/**
 * Description: 操作类
 *
 * @Author: yang
 * @Date: 2018/7/27
 * @Version: 1.0
 */
public interface LogService {




    /**
     * 查询操作日志信息
     * @return
     */
    Page findLogList(String token  ,Integer role, Integer pageNo, String phone, String name, String startTime, String endTime)throws NullPointerException;
}
