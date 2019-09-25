package com.shengxian.entity;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-10
 * @Version: 1.0
 */
public class WageSettlement {


    private String time;
    private  Double money;
    private Double statis;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getStatis() {
        return statis;
    }

    public void setStatis(Double statis) {
        this.statis = statis;
    }

    @Override
    public String toString() {
        return "WageSettlement{" +
                "time='" + time + '\'' +
                ", money=" + money +
                ", statis=" + statis +
                '}';
    }
}
