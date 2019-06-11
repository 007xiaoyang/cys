package com.shengxian.entity;

/**
 * Description: 员工的用户提成类
 *
 * @Author: yang
 * @Date: 2018-10-21
 * @Version: 1.0
 */
public class UserPercent {

    private Integer id;
    private Integer binding_id;
    private String proportion;
    private Integer staff_id;

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



}
