package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description : StaffVO
 * @Author: yang
 * @Date: 2019-11-27
 * @Version: 1.0
 */
public class StaffVO {

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private Long id ;

    /**
     * 员工名称
     */
    @ApiModelProperty(value = "员工名称")
    private String name;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private String number;

    /**
     * 关联类别表id
     */
    @ApiModelProperty(value = "关联类别表id")
    private Long categoryId;

    /**
     * 是否被禁用 0默认 ，1禁用
     */
    @ApiModelProperty(value = "是否被禁用 0默认 ，1禁用")
    private int isDisable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }
}
