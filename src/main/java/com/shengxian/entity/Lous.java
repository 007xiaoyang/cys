package com.shengxian.entity;

/**
 * Description: 欠条类
 *
 * @Author: yang
 * @Date: 2018-12-13
 * @Version: 1.0
 */
public class Lous {

    private Integer id;
    private Integer business_id;
    private String obligor_name; //债务人姓名
    private String obligor_ID; //债务人身份证
    private String endTime ; //截止时间
    private String obligor_name2; //债务人姓名
    private String creditor_name; //债权人姓名
    private String creditor_ID; //债权人身份证
    private String money ; //现金
    private String  appoint_time; //约定时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public String getObligor_name() {
        return obligor_name;
    }

    public void setObligor_name(String obligor_name) {
        this.obligor_name = obligor_name;
    }

    public String getObligor_ID() {
        return obligor_ID;
    }

    public void setObligor_ID(String obligor_ID) {
        this.obligor_ID = obligor_ID;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getObligor_name2() {
        return obligor_name2;
    }

    public void setObligor_name2(String obligor_name2) {
        this.obligor_name2 = obligor_name2;
    }

    public String getCreditor_name() {
        return creditor_name;
    }

    public void setCreditor_name(String creditor_name) {
        this.creditor_name = creditor_name;
    }

    public String getCreditor_ID() {
        return creditor_ID;
    }

    public void setCreditor_ID(String creditor_ID) {
        this.creditor_ID = creditor_ID;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }
}
