package com.shengxian.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description : Goods
 * @Author: yang
 * @Date: 2019-12-01
 * @Version: 1.0
 */
public class GoodsVO {

    @ApiModelProperty(value = "产品id")
    private Long id ;

    @ApiModelProperty(value = "类别id")
    private Long categoryId;

    @ApiModelProperty(value = "编号")
    private String number;

    @ApiModelProperty(value = "产品id")
    private String name ;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "单位")
    private String units;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    private Double costPrice;

    private Double minimumPrice;

    private Double temporaryPrice;

    @ApiModelProperty(value = "商品发布状态  1上架审核中，2通过代表上架，3未通过  4下架")
    private Integer status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Double getTemporaryPrice() {
        return temporaryPrice;
    }

    public void setTemporaryPrice(Double temporaryPrice) {
        this.temporaryPrice = temporaryPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
