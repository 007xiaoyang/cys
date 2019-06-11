package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 结算类
 *
 * @Author: yang
 * @Date: 2018-12-04
 * @Version: 1.0
 */
public class Settlement {

    private Integer id;
    private Integer business_id;
    private Date create_time;

    private Integer staff_id;
    private double tatol; //总金额
    private double money;//结算金额
    private double arrears;//欠款
    private double unpaid;//未付款
    private double incomes;//费用收入
    private double expenditure;//费用支出
    private double return_tatol;
    private int type;//type
    private double risk;
    private int opid;

    private double wx;
    private double alipay;
    private double cash;
    private double bankcard;
    private double other;
    private Date oldTime;
    private String time ;


    public Settlement() {
    }

    public Settlement(Integer business_id, Date create_time,int opid) {
        this.business_id = business_id;
        this.create_time = create_time;
        this.opid = opid;
    }

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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public double getTatol() {
        return tatol;
    }

    public void setTatol(double tatol) {
        this.tatol = tatol;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getArrears() {
        return arrears;
    }

    public void setArrears(double arrears) {
        this.arrears = arrears;
    }

    public double getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(double unpaid) {
        this.unpaid = unpaid;
    }

    public double getIncomes() {
        return incomes;
    }

    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public double getReturn_tatol() {
        return return_tatol;
    }

    public void setReturn_tatol(double return_tatol) {
        this.return_tatol = return_tatol;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getWx() {
        return wx;
    }

    public void setWx(double wx) {
        this.wx = wx;
    }

    public double getAlipay() {
        return alipay;
    }

    public void setAlipay(double alipay) {
        this.alipay = alipay;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBankcard() {
        return bankcard;
    }

    public void setBankcard(double bankcard) {
        this.bankcard = bankcard;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public Date getOldTime() {
        return oldTime;
    }

    public void setOldTime(Date oldTime) {
        this.oldTime = oldTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
