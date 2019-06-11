package com.shengxian.entity;

/**
 * Description: 仓库提成类
 *
 * @Author: yang
 * @Date: 2018-10-21
 * @Version: 1.0
 */
public class WarehousePercent {

    private Integer id;
    private Integer warehouse_id;
    private String proportion;
    private Integer staff_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public String toSpring(){
        return "WarehousePercent{" +
                "warehouse_id"+warehouse_id+
                "proportion"+proportion+
                "staff_id"+staff_id+
                "}";
    }

}
