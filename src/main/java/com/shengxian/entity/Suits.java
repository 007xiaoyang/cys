package com.shengxian.entity;

/**
 * Description: 套装产品
 *
 * @Author: yang
 * @Date: 2018-12-23
 * @Version: 1.0
 */
public class Suits {

    private Integer id;
    private Integer business_id;
    private String img; //套装产品图片
    private String goods_id; //产品id ，多件产品
    private String startTime; //套装活动开始时间
    private String endTime; //套装活动时间结束时间

    private GoodsImg[] goodsImgs;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public GoodsImg[] getGoodsImgs() {
        return goodsImgs;
    }

    public void setGoodsImgs(GoodsImg[] goodsImgs) {
        this.goodsImgs = goodsImgs;
    }
}
