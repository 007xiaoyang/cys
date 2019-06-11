package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 赠送类
 *
 * @Author: yang
 * @Date: 2018-11-06
 * @Version: 1.0
 */
public class Give {

    private Integer id;
    private Integer business_id;
    private Integer goods_id;//产品id
    private Integer suppliers_id;//供应商id
    private Integer binding_id; //绑定用户id
    private Double num;//数量
    private Date give_time; //赠送时间
    private Date loss_time;//报损时间
    private int operate_id; //操作人
    private String beizhu; //备注
    private int status;
    private Integer order_id;



    public Give() {
    }

    public Give(Integer goods_id, Integer binding_id, Double num, Date loss_time, int operate_id, int status, Integer order_id) {
        this.goods_id = goods_id;
        this.binding_id = binding_id;
        this.num = num;
        this.loss_time = loss_time;
        this.operate_id = operate_id;
        this.status = status;
        this.order_id = order_id;
    }

    public Give(Integer goods_id, Integer suppliers_id, Double num, Date give_time, Integer operate_id, String beizhu) {
        this.goods_id = goods_id;
        this.suppliers_id = suppliers_id;
        this.num = num;
        this.give_time = give_time;
        this.operate_id = operate_id;
        this.beizhu = beizhu;

    }
    public Give(Integer goods_id, Integer binding_id, Double num, Date loss_time, int operate_id, int status) {
        this.goods_id = goods_id;
        this.binding_id = binding_id;
        this.num = num;
        this.loss_time = loss_time;
        this.operate_id = operate_id;
        this.status = status;
    }

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

    public Integer getSuppliers_id() {
        return suppliers_id;
    }

    public void setSuppliers_id(Integer suppliers_id) {
        this.suppliers_id = suppliers_id;
    }

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Date getGive_time() {
        return give_time;
    }

    public void setGive_time(Date give_time) {
        this.give_time = give_time;
    }

    public Date getLoss_time() {
        return loss_time;
    }

    public void setLoss_time(Date loss_time) {
        this.loss_time = loss_time;
    }

    public int getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }
}
