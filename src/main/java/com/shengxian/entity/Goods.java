package com.shengxian.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description: 产品类
 *
 * @Author: yang
 * @Date: 2018-10-12
 * @Version: 1.0
 */
public class Goods {

    private Integer id;
    private Integer category_id; //关联商品类别表id
    private String number; //商品编号
    private String name; //商品名称
    private String brand; //品牌
    private String spec; //规格
    private String units;//单位
    private String tonnage; //吨位
    private long barcode; //条码
    private String colour; //颜色
    private String origin ; //产地
    private double cost_price; //进价
    private double minimum_price; //最低售价
    private double temporary_price; //临时售价
    private String volume; //包装体积
    private Double weight; //重量
    private String period; //保质期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date return_time; //退货有效期
    private Integer status; //商品发布状态  1上架审核中，2通过代表上架，3未通过  4下架
    private Integer business_id; //关联商家表id
    private String goods_detail; //产品详情
    private String beizhu; //备注
    private Integer is_del;

    private GoodsScheme[] goodsSchemes;
    private GoodsInventory goodsInventories;
    private GoodsImg[] goodsImgs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public double getMinimum_price() {
        return minimum_price;
    }

    public void setMinimum_price(double minimum_price) {
        this.minimum_price = minimum_price;
    }

    public double getTemporary_price() {
        return temporary_price;
    }

    public void setTemporary_price(double temporary_price) {
        this.temporary_price = temporary_price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getReturn_time() {
        return return_time;
    }

    public void setReturn_time(Date return_time) {
        this.return_time = return_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public Integer getIs_del() {
        return is_del;
    }

    public void setIs_del(Integer is_del) {
        this.is_del = is_del;
    }

    public GoodsScheme[] getGoodsSchemes() {
        return goodsSchemes;
    }

    public void setGoodsSchemes(GoodsScheme[] goodsSchemes) {
        this.goodsSchemes = goodsSchemes;
    }

    public GoodsInventory getGoodsInventories() {
        return goodsInventories;
    }

    public void setGoodsInventories(GoodsInventory goodsInventories) {
        this.goodsInventories = goodsInventories;
    }

    public GoodsImg[] getGoodsImgs() {
        return goodsImgs;
    }

    public void setGoodsImgs(GoodsImg[] goodsImgs) {
        this.goodsImgs = goodsImgs;
    }
}
