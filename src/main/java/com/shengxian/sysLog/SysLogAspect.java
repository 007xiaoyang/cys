package com.shengxian.sysLog;

import com.alibaba.fastjson.JSON;
import com.shengxian.entity.SysLogEntity;
import com.shengxian.interceptor.InterceptorConfig;
import com.shengxian.mapper.LogMapper;
import com.shengxian.service.LogService;
import com.shengxian.service.ShopService;
import com.shengxian.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-27
 * @Version: 1.0
 */
@Aspect
@Component
public class SysLogAspect {

    private static final  Logger log = LoggerFactory.getLogger(SysLogAspect.class);

    //注入service,用来将日志信息保存在数据库
    @Resource
    private LogMapper logMapper;


    // 定义Pointcut，Pointcut的名称 就是simplePointcut，此方法不能有返回值，该方法只是一个标示
    // @annotation 指定自定义注解
    @Pointcut("@annotation(com.shengxian.sysLog.SysLog)")
    public void logPointCut(){}

    @Before("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        log.info("----------------------------开始进入自定义日志记录注解-------------------------------------");

        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();

        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            //注解上的描述
            sysLogEntity.setModule(syslog.module());
            sysLogEntity.setMethod(syslog.methods());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        //请求的参数
        Object[] args = joinPoint.getArgs();

        String token = args[0].toString();
       // String token = JSON.toJSONString(args[0]);

        //通过role角色来判断是店铺还是员工账号登录的
        //1是店铺登录

        HashMap hashMap = logMapper.selectBusinessInfoByToken(token);

        if (hashMap == null){
            hashMap = logMapper.selectStaffInfoByToken(token);
        }

        //获取系统时间
        String time = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        //方法通知前获取时间,为什么要记录这个时间呢？当然是用来计算模块执行时间的
        long start = System.currentTimeMillis();
        sysLogEntity.setPhone(hashMap.get("phone").toString());//登录的账号
        sysLogEntity.setName(hashMap.get("name").toString());//操作人名称
        sysLogEntity.setExecution_time(time);//执行时间
        sysLogEntity.setBusiness_id(Integer.valueOf(hashMap.get("id").toString())); //商家ID
        long end = System.currentTimeMillis();
        //将计算好的时间保存在实体中
        sysLogEntity.setRsponse_data(""+(end-start)); //响应时间
        sysLogEntity.setCommite("执行成功！");
        //保存进数据库
        logMapper.saveLog(sysLogEntity);
        log.info("--------------------保存操作方法记录到数据库-----------------------------");
    }

   /*@AfterReturning(returning="rvt", pointcut="@annotation(com.cf.utils.annotation.SysLog)")
   public Object AfterExec(JoinPoint joinPoint,Object rvt){

   }*/


}
