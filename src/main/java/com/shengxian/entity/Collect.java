package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 收藏类
 *
 * @Author: yang
 * @Date: 2019-05-26
 * @Version: 1.0
 */
public class Collect {

    private Integer id ;
    private Integer binding_id ;
    private Integer suppliers_id ; //供应商id
    private Integer business_id ;
    private Integer goods_id;
    private Date time;

    public Collect(){}

    public Collect(Integer binding_id, Integer goods_id, Date time) {
        this.binding_id = binding_id;
        this.goods_id = goods_id;
        this.time = time;
    }

    public Collect(Integer goods_id , Date time , Integer suppliers_id ,Integer business_id){
        this.business_id = business_id;
        this.goods_id = goods_id;
        this.suppliers_id = suppliers_id;
        this.time = time;
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

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
