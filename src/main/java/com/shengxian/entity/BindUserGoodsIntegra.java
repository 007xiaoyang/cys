package com.shengxian.entity;

/**
 * Description: 绑定用户产品积分类
 *
 * @Author: yang
 * @Date: 2018-10-24
 * @Version: 1.0
 */
public class BindUserGoodsIntegra {

    private Integer id;//
    private Integer binding_id;//绑定用户ID
    private Integer goods_id;//产品ID
    private String proportion;//比例

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

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }
}
