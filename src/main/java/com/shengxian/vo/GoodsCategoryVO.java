package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Description : GoodsCategory
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class GoodsCategoryVO {

    @ApiModelProperty(value = "类别id")
    private Long id ;

    @ApiModelProperty(value = "店铺id")
    private Long businessId;

    @ApiModelProperty(value = "类别名称")
    private String  name;

    @ApiModelProperty(value = "级别 0一级 二级是一级的id")
    private Integer level;

    @ApiModelProperty(value = "产品序号")
    private String code;

    @ApiModelProperty(value = "1用户APP上置顶")
    private Integer status;

    @ApiModelProperty(value = "子菜单")
    private List<GoodsCategoryVO> children ;

    @ApiModelProperty(value = "子菜单下的产品集合")
    private List<GoodsVO> gChildren;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GoodsCategoryVO> getChildren() {
        return children;
    }

    public void setChildren(List<GoodsCategoryVO> children) {
        this.children = children;
    }

    public List<GoodsVO> getgChildren() {
        return gChildren;
    }

    public void setgChildren(List<GoodsVO> gChildren) {
        this.gChildren = gChildren;
    }
}
