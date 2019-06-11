package com.shengxian.entity;

/**
 * Description: 产品图片类
 *
 * @Author: yang
 * @Date: 2018-10-23
 * @Version: 1.0
 */
public class GoodsImg {

    private Integer id;
    private Integer goods_id;
    private String img;

    private Integer suits_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSuits_id() {
        return suits_id;
    }

    public void setSuits_id(Integer suits_id) {
        this.suits_id = suits_id;
    }
}
