package com.shengxian.entity;

/**
 * Description: 财务管理搜索条件参数类
 *
 * @Author: yang
 * @Date: 2018-12-02
 * @Version: 1.0
 */
public class Condition {

    private Integer staff_id;
    private Integer pageNo;
    private Integer startIndex; //起始索引
    private Integer pageSize; //每页的页数
    private String name;
    private String number;
    private String startTime;
    private String endTime;

    private Integer business_id;

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }
}
