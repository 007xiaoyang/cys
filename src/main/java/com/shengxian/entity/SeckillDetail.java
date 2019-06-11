package com.shengxian.entity;

/**
 * Description： 秒杀明细
 *
 * @Author: yang
 * @Date: 2018-12-22
 * @Version: 1.0
 */
public class SeckillDetail {

    private Integer id;
    private Integer seckill_id;//秒杀id
    private Integer goods_id;
    private Double activity_price;
    private Integer count; //参与人数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeckill_id() {
        return seckill_id;
    }

    public void setSeckill_id(Integer seckill_id) {
        this.seckill_id = seckill_id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Double getActivity_price() {
        return activity_price;
    }

    public void setActivity_price(Double activity_price) {
        this.activity_price = activity_price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
