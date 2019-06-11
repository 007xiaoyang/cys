package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 优惠券类
 *
 * @Author: yang
 * @Date: 2018-12-19
 * @Version: 1.0
 */
public class Coupon {

    private Integer id;
    private Integer business_id; //商家id
    private Integer binding_id; //用户id
    private Integer coupon_id; //优惠券id
    private String name; //优惠券名称
    private double full; //满
    private double reduce; //减
    private Date create_time; //创建时间
    private String startTime; //开始时间
    private String endTime; //到期时间
    private Integer type;
    private Integer state;


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

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
    }

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFull() {
        return full;
    }

    public void setFull(double full) {
        this.full = full;
    }

    public double getReduce() {
        return reduce;
    }

    public void setReduce(double reduce) {
        this.reduce = reduce;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
