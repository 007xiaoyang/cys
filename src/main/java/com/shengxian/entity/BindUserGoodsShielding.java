package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 绑定用户屏蔽产品类
 *
 * @Author: yang
 * @Date: 2018-10-24
 * @Version: 1.0
 */
public class BindUserGoodsShielding {

    private Integer id;
    private Integer binding_id;
    private Integer goods_id;
    private Date shielding_time;


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

    public Date getShielding_time() {
        return shielding_time;
    }

    public void setShielding_time(Date shielding_time) {
        this.shielding_time = shielding_time;
    }
}
