package com.shengxian.entity.clerkApp;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-14
 * @Version: 1.0
 */
public class ShoppingMall {

    private Integer id ;
    private Integer op_id; //根据type来判断是员工ID还是店铺ID
    private Integer consume_id ; //根据mold来判断是客户ID还是供应商ID
    private Integer type ; //0店铺APP购物车，1员工APP购物车
    private Double money; //购物车金额
    private Integer mold; //0录入订单，1采购入库
    private Date create_time; //创建时间
    private String time;

    private List mallDateil;

    public ShoppingMall() {
    }

    public ShoppingMall(Integer op_id, Integer consume_id, Integer type, Integer mold, Date create_time) {
        this.op_id = op_id;
        this.consume_id = consume_id;
        this.type = type;
        this.mold = mold;
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOp_id() {
        return op_id;
    }

    public void setOp_id(Integer op_id) {
        this.op_id = op_id;
    }

    public Integer getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(Integer consume_id) {
        this.consume_id = consume_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getMold() {
        return mold;
    }

    public void setMold(Integer mold) {
        this.mold = mold;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public List getMallDateil() {
        return mallDateil;
    }

    public void setMallDateil(List mallDateil) {
        this.mallDateil = mallDateil;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
