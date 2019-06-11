package com.shengxian.entity;

/**
 * Description: 限时活动类
 *
 * @Author: yang
 * @Date: 2018-12-20
 * @Version: 1.0
 */
public class Limited {

    private Integer id;
    private Integer business_id;
    private Integer goods_id; //产品id
    private Double activity_price; //活动价
    private String startTime; //开始时间
    private String endTime; //结束时间

    private double num; //数量

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

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Double getActivity_price() {
        return activity_price;
    }

    public void setActivity_price(Double activity_price) {
        this.activity_price = activity_price;
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

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
