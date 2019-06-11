package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 员工配送类
 *
 * @Author: yang
 * @Date: 2018-12-14
 * @Version: 1.0
 */
public class Dispatc {

    private Integer id;
    private Integer business_id;
    private Integer staff_id;
    private Date create_time;

    public Dispatc() {
    }

    public Dispatc(Integer business_id, Integer staff_id, Date create_time) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
