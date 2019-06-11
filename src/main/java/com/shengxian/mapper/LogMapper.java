package com.shengxian.mapper;

import com.shengxian.entity.LogEntity;
import com.shengxian.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 操作类
 *
 * @Author: yang
 * @Date: 2018/7/27
 * @Version: 1.0
 */
@Mapper
public interface LogMapper {



    /**
     * 添加商家的操作日志信息
     * @param sysLogEntity
     * @return
     */
    Integer saveLog(SysLogEntity sysLogEntity);

    /**
     * 查询商家操作日志信息
     * @return
     */
    List<LogEntity> findLogList(@Param("business_id") Integer business_id, @Param("phone") String phone, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询商家操作日志的总数
     * @param phone
     * @param name
     * @return
     */
    Integer findLogListCount(@Param("business_id") Integer business_id, @Param("phone") String phone, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 操作记录方法
     * 通过商家token查询商家信息
     * @param token
     * @return
     */
    HashMap selectBusinessInfoByToken(@Param("token") String token);

    /**
     * 操作记录方法
     * 通过员工token查询员工姓名和该员工所属的商家
     * @param token
     * @return
     */
    HashMap selectStaffInfoByToken(@Param("token")String token);

}
