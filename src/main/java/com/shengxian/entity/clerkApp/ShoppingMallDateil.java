package com.shengxian.entity.clerkApp;

import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-14
 * @Version: 1.0
 */
public class ShoppingMallDateil {

    private Integer id ;
    private Integer sm_id ; //购物车ID
    private Integer goods_id ; //产品ID
    private Double num ; //产品销售数量
    private Double price ; //产品销售单价
    private Integer type; //0默认，1赠送
    private Integer mold ; //0录入订单，1采购入库
    private Date create_time;


    private String name ; //产品名称
    private String units ;//单位

    public ShoppingMallDateil() {
    }

    /**
     *
     * @param sm_id 购物车ID
     * @param goods_id 产品ID
     * @param price 产品销售单价
     * @param num 产品销售数量
     * @param type 0默认，1赠送
     * @param mold  //0录入订单，1采购入库
     * @param create_time
     */
    public ShoppingMallDateil(Integer sm_id, Integer goods_id, Double price,Double num , Integer type, Integer mold, Date create_time) {
        this.sm_id = sm_id;
        this.goods_id = goods_id;
        this.price = price;
        this.num = num;
        this.type = type;
        this.mold = mold;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSm_id() {
        return sm_id;
    }

    public void setSm_id(Integer sm_id) {
        this.sm_id = sm_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMold() {
        return mold;
    }

    public void setMold(Integer mold) {
        this.mold = mold;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
