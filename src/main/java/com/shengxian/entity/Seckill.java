package com.shengxian.entity;

/**
 * Description: 限时秒杀
 *
 * @Author: yang
 * @Date: 2018-12-22
 * @Version: 1.0
 */
public class Seckill {

    private Integer id;
    private Integer business_id; //商家id
    private String startTime ; //开始时间
    private String endTime; //结算时间

    private Double full; //满减活动 满金额
    private Double reduce ; // 减金额

    private SeckillDetail[] seckillDetails;

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

    public SeckillDetail[] getSeckillDetails() {
        return seckillDetails;
    }

    public void setSeckillDetails(SeckillDetail[] seckillDetails) {
        this.seckillDetails = seckillDetails;
    }

    public Double getFull() {
        return full;
    }

    public void setFull(Double full) {
        this.full = full;
    }

    public Double getReduce() {
        return reduce;
    }

    public void setReduce(Double reduce) {
        this.reduce = reduce;
    }
}
