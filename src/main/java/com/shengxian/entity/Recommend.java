package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 推荐产品类
 *
 * @Author: yang
 * @Date: 2018-12-10
 * @Version: 1.0
 */
public class Recommend {

    private Integer id;
    private Integer goods_id;
    private Integer binding_id;
    private Date create_time;

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

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
