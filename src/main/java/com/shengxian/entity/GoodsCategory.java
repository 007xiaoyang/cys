package com.shengxian.entity;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/9/25
 * @Version: 1.0
 */
public class GoodsCategory {

    private Integer id;
    private Integer business_id;
    private String name;
    private Integer level;
    private String code;

    public GoodsCategory() {
    }

    public GoodsCategory( Integer business_id, String name, Integer level, String code) {
        this.business_id = business_id;
        this.name = name;
        this.level = level;
        this.code = code;
    }
    public GoodsCategory( Integer business_id, String name,String code) {
        this.business_id = business_id;
        this.name = name;
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
