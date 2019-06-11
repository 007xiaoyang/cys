package com.shengxian.entity;

/**
 * Description: 员工其它的提成类
 *
 * @Author: yang
 * @Date: 2018-10-21
 * @Version: 1.0
 */
public class OtherPercentage {

    private Integer id;
    private Integer type;
    private String proportion;
    private Integer staff_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return "OtherPercentage{" +
                "type"+type+
                "proportion"+proportion+
                "staff_id"+staff_id+
                "}";
    }
}
