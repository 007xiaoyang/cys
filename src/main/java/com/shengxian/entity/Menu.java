package com.shengxian.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 菜单类
 *
 * @Author: yang
 * @Date: 2019-04-10
 * @Version: 1.0
 */

public class Menu {

    private Integer id ; //菜单id
    private String menu ; //菜单名称
    private Integer state ; //0没权限，1有权限
    private Integer m_id ; //权限id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getM_id() {
        return m_id;
    }

    public void setM_id(Integer m_id) {
        this.m_id = m_id;
    }
}
