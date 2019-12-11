package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @Description : SuppliersCategoryVO
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class SuppliersCategoryVO {

    /**
     * 类别id
     */
    @ApiModelProperty(value = "类别id")
    private Long id ;

    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String name;

    /**
     * 类别编号
     */
    @ApiModelProperty(value = "类别编号")
    private String code;

    /**
     * 供应商对象
     */
    @ApiModelProperty(value = "供应商信息对象")
    private List<SuppliersVO> children;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SuppliersVO> getChildren() {
        return children;
    }

    public void setChildren(List<SuppliersVO> children) {
        this.children = children;
    }
}
