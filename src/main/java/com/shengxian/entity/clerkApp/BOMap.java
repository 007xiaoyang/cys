package com.shengxian.entity.clerkApp;

import io.swagger.models.auth.In;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-04-21
 * @Version: 1.0
 */
public class BOMap {

    private Integer id ;
    private Integer binding_id ;
    private String user_name;
    private String number;
    private Double price;
    private Double difference_price;
    private  Double freight;
    private String beizhu;
    private Double coupon;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDifference_price() {
        return difference_price;
    }

    public void setDifference_price(Double difference_price) {
        this.difference_price = difference_price;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public Double getCoupon() {
        return coupon;
    }

    public void setCoupon(Double coupon) {
        this.coupon = coupon;
    }
}
