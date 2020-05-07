package com.shengxian.vo;

import java.util.Date;

/**
 * @Description : OrderVO
 * @Author: yang
 * @Date: 2020-03-22
 * @Version: 1.0
 */
public class OrderVO {
    private Long id;
    private Long binding_id;
    private Long business_id;
    private String user_name;
    private Double price;
    private int part;
    private String making;
    private String order_number;
    private Date create_time;
    private String phone;
    private int mold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Long binding_id) {
        this.binding_id = binding_id;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getMaking() {
        return making;
    }

    public void setMaking(String making) {
        this.making = making;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMold() {
        return mold;
    }

    public void setMold(int mold) {
        this.mold = mold;
    }
}
