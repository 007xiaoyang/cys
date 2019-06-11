package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 绑定用户收藏产品类
 *
 * @Author: yang
 * @Date: 2018-10-24
 * @Version: 1.0
 */
public class BindUserGoodsCollection {

    private Integer id;
    private Integer binding_id;//联商家绑定用户ID、
    private Integer goods_id;//产品ID
    private Date collection_time;//收藏时间

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

    public Date getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(Date collection_time) {
        this.collection_time = collection_time;
    }
}
