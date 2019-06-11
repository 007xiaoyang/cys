package com.shengxian.entity;

/**
 * Description: 产品提成类
 *
 * @Author: yang
 * @Date: 2018-10-21
 * @Version: 1.0
 */
public class GoodsPercent {

    private Integer id;
    private Integer goods_id;
    private String proportion;
    private Integer staff_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }
}
