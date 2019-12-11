package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

/**
 * @Description : SuppliersVO
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class SuppliersVO {

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long id ;

    /**
     * 类别id
     */
    @ApiModelProperty(value = "类别id")
    private Long categoryId;

    /**
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private String number;

    /**
     * 供应商手机号
     */
    @ApiModelProperty(value = "供应商手机号")
    private String phone;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String  name;

    /**
     * 供应商地址
     */
    @ApiModelProperty(value = "供应商地址")
    private String address;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String illustrated;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long businessId;

    private Integer isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIllustrated() {
        return illustrated;
    }

    public void setIllustrated(String illustrated) {
        this.illustrated = illustrated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
