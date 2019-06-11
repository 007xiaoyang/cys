package com.shengxian.quartz;

import com.shengxian.service.TeamUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-28
 * @Version: 1.0
 */
@Component
public class ScheduledTask {

   private static Logger log = LoggerFactory.getLogger(ScheduledTask.class);

   @Resource
   private TeamUpdateService teamUpdateService;


    @Scheduled(cron = "0 0 0 * * ?")
    private void text2(){
        log.warn("---------------------------开始进入（每晚清除订单结算状态）-------------------------------------------");
        teamUpdateService.settlement();
    }

    @Scheduled(cron = "0 0 2 1 * ? ")
    private void text(){
        log.warn("---------------------------开始进入（每月定时员工提成统计）-------------------------------------------");
        teamUpdateService.teamUpdate();
    }


}
