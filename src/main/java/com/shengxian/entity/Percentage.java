package com.shengxian.entity;

/**
 * Description: 提成参数对象
 *
 * @Author: yang
 * @Date: 2019-04-17
 * @Version: 1.0
 */
public class Percentage {

    private Integer goods_id;
    private Integer staff_id ;
    private Integer binding_id;
    private Double a ;
    private Double b ;


    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
    }

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }
}
