package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description : User
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class UserVO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long id ;


    @ApiModelProperty(value = "类别id")
    private  Long categoryId;

    @ApiModelProperty(value = "店铺id")
    private Long businessId;

    @ApiModelProperty(value = "会员方案0-15种")
    private Long userId;


    @ApiModelProperty(value = "专员id")
    private Long staffId;

    @ApiModelProperty(value = "专员id")
    private String number;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "会员方案0-15种")
    private Integer schemeId;

    @ApiModelProperty(value =  "来源，0线上，1线下")
    private Integer source;

    @ApiModelProperty(value =  "1是扫码客户，名称不能修改")
    private Integer type;


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

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
