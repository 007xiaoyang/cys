package com.shengxian.entity;

/**
 * Description: 采购订单详情
 *
 * @Author: yang
 * @Date: 2018-11-10
 * @Version: 1.0
 */
public class PurchaseOrderDetail {

    private Integer id;
    private Integer goods_id; //
    private Integer purchase_id; //采购id
    private Double purchase_number; //采购数量
    private Double purchase_price; //每件产品的采购金额
    private String goods_name;
    private String units;
    private int type; //默认0，1赠送的
    private Double price;
    private int mold;//0采购订单详情，1采购退货订单详情


    private String name ;

    public PurchaseOrderDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(Integer purchase_id) {
        this.purchase_id = purchase_id;
    }

    public Double getPurchase_number() {
        return purchase_number;
    }

    public void setPurchase_number(Double purchase_number) {
        this.purchase_number = purchase_number;
    }

    public Double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(Double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getMold() {
        return mold;
    }

    public void setMold(int mold) {
        this.mold = mold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
