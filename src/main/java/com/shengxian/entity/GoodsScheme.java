package com.shengxian.entity;

/**
 * Description: 产品方案类
 *
 * @Author: yang
 * @Date: 2018-10-23
 * @Version: 1.0
 */
public class GoodsScheme {

    private Integer id;
    private Integer goods_id;
    private Integer scheme_id;
    private Double price;


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

    public Integer getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(Integer scheme_id) {
        this.scheme_id = scheme_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
