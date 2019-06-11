package com.shengxian.entity;

/**
 * Description: 积分产品类
 *
 * @Author: yang
 * @Date: 2018-10-28
 * @Version: 1.0
 */
public class IntegrGoods {

    private Integer id;
    private String img; //产品图片
    private Integer category_id; //积分产品类别id
    private String number; // 产品编号
    private String name; // 产品名称
    private String brand; // 产品品牌
    private String spec; // 产品规格
    private String units; // 单位
    private Long barcode;//条码
    private Integer integr_price; //产品兑换积分
    private Integer status ; //状态0保存,1上架审核中，2通过代表上架，3未通过  4下架
    private String illustrate; //说明
    private String goods_detail; //商品详情
    private Integer is_del;
    private Integer business_id; //商家id
    private Integer actual;//实际库存
    private Double temporary_price ;

    private GoodsInventory goodsInventory; //产品库存对象

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public Integer getIntegr_price() {
        return integr_price;
    }

    public void setIntegr_price(Integer integr_price) {
        this.integr_price = integr_price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIllustrate() {
        return illustrate;
    }

    public void setIllustrate(String illustrate) {
        this.illustrate = illustrate;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public Integer getIs_del() {
        return is_del;
    }

    public void setIs_del(Integer is_del) {
        this.is_del = is_del;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public GoodsInventory getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(GoodsInventory goodsInventory) {
        this.goodsInventory = goodsInventory;
    }

    public Double getTemporary_price() {
        return temporary_price;
    }

    public void setTemporary_price(Double temporary_price) {
        this.temporary_price = temporary_price;
    }
}
