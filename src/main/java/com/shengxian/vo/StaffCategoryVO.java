package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Description : StaffCategoryVO
 * @Author: yang
 * @Date: 2019-11-27
 * @Version: 1.0
 */
public class StaffCategoryVO {
    /**
     * 类别id
     */
    @ApiModelProperty(value = "类别id")
    private Long id ;

    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String name ;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long businessId;

    /**
     * 类别下对应的员工信息
     */
    @ApiModelProperty(value = "员工信息对象")
    private List<StaffVO> children;


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

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public List<StaffVO> getChildren() {
        return children;
    }

    public void setChildren(List<StaffVO> children) {
        this.children = children;
    }
}
