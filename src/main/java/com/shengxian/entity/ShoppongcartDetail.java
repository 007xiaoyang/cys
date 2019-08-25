package com.shengxian.entity;

import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-01-20
 * @Version: 1.0
 */
public class ShoppongcartDetail {

   private Integer id;
   private Integer tsc_id;
   private Integer goods_id;
   private Double num;
   private Double price ;
   private Date create_time;

    public ShoppongcartDetail() {
    }

    public ShoppongcartDetail(Integer tsc_id, Integer goods_id, Double num ,Double price, Date create_time) {
        this.tsc_id = tsc_id;
        this.goods_id = goods_id;
        this.num = num;
        this.price = price;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTsc_id() {
        return tsc_id;
    }

    public void setTsc_id(Integer tsc_id) {
        this.tsc_id = tsc_id;
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "ShoppongcartDetail{" +
                "id=" + id +
                ", tsc_id=" + tsc_id +
                ", goods_id=" + goods_id +
                ", num=" + num +
                ", price=" + price +
                ", create_time=" + create_time +
                '}';
    }
}
