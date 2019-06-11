package com.shengxian.entity;

/**
 * Description: 日志实体类对象
 *
 * @Author: yang
 * @Date: 2018/7/27
 * @Version: 1.0
 */
public class LogEntity {

    private Integer id;
    private String phone;//登录的账号
    private String name; //操作人名称
    private String module;//执行的模块
    private String method;//执行的方法
    private String rsponse_data;//响应时间
    //private String IP;//IP地址
    private String execution_time;//执行时间
    private String commite;//执行描述
    private Integer business_id; //商家ID


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRsponse_data() {
        return rsponse_data;
    }

    public void setRsponse_data(String rsponse_data) {
        this.rsponse_data = rsponse_data;
    }

    public String getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(String execution_time) {
        this.execution_time = execution_time;
    }

    public String getCommite() {
        return commite;
    }

    public void setCommite(String commite) {
        this.commite = commite;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }
}
