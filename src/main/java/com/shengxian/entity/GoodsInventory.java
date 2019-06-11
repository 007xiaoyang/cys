package com.shengxian.entity;

/**
 * Description: 产品库存类
 *
 * @Author: yang
 * @Date: 2018-10-23
 * @Version: 1.0
 */
public class GoodsInventory {

    private Integer id;
    private Integer goods_id;
    private Integer warehouse_id;
    private Integer actual;//实际库存
    private Integer fictitious;//虚拟库存
    private Integer minimum;//最低库存
    private Integer highest;//最高库存


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

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public Integer getFictitious() {
        return fictitious;
    }

    public void setFictitious(Integer fictitious) {
        this.fictitious = fictitious;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getHighest() {
        return highest;
    }

    public void setHighest(Integer highest) {
        this.highest = highest;
    }
}
