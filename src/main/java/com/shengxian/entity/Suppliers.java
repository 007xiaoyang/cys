package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 供应商类
 *
 * @Author: yang
 * @Date: 2018/8/22
 * @Version: 1.0
 */
public class Suppliers {

    private Integer id;
    private Integer category_id;
    private String number;
    private String phone;
    private String name;
    private String address;
    private String illustrated;
    private String remarks;
    private Date create_time;
    private Integer business_id;
    private Integer is_del;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIllustrated() {
        return illustrated;
    }

    public void setIllustrated(String illustrated) {
        this.illustrated = illustrated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getIs_del() {
        return is_del;
    }

    public void setIs_del(Integer is_del) {
        this.is_del = is_del;
    }
}
