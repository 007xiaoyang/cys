package com.shengxian.service.impl;

import com.shengxian.common.util.IntegerUtils;
import com.shengxian.common.util.Page;
import com.shengxian.entity.LogEntity;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.LogMapper;
import com.shengxian.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 操作类
 *
 * @Author: yang
 * @Date: 2018/7/27
 * @Version: 1.0
 */
@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;
    @Resource
    private ShopMapper shopMapper;

    //查询操作日志信息
    @Override
    public Page findLogList(String token  ,Integer role, Integer pageNo , String phone, String name , String startTime, String endTime)throws NullPointerException {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        int pageNum=1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        Integer totalCount = logMapper.findLogListCount(bid,phone,name,startTime,endTime);
        Page page = new Page(pageNum,totalCount);
        List<LogEntity> logList = logMapper.findLogList(bid,phone,name,startTime,endTime ,page.getStartIndex(), page.getPageSize());
        page.setRecords(logList);
        return page;
    }
}
