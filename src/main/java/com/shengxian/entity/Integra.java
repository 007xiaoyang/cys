package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 积分类
 *
 * @Author: yang
 * @Date: 2018-12-19
 * @Version: 1.0
 */
public class Integra {

    private Integer id;
    private Integer binding_id;//用户id
    private Integer integra_id ;//用户积分id
    private Double integra; //积分
    private String illustrate;//说明
    private Integer type; //1商城，2线下，3积分兑换，4人工赠送，5人工减
    private Date create_time;
    private String order_number;
    public Integra() {
    }

    public Integra(Integer integra_id, Double integra, Integer type, Date create_time,String order_number) {
        this.integra_id = integra_id;
        this.integra = integra;
        this.type = type;
        this.create_time = create_time;
        this.order_number = order_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
    }

    public Integer getIntegra_id() {
        return integra_id;
    }

    public void setIntegra_id(Integer integra_id) {
        this.integra_id = integra_id;
    }

    public Double getIntegra() {
        return integra;
    }

    public void setIntegra(Double integra) {
        this.integra = integra;
    }

    public String getIllustrate() {
        return illustrate;
    }

    public void setIllustrate(String illustrate) {
        this.illustrate = illustrate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
