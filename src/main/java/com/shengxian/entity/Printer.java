package com.shengxian.entity;

/**
 * Description: 飞蛾打印机类
 *
 * @Author: yang
 * @Date: 2019-04-30
 * @Version: 1.0
 */
public class Printer {

    private Integer id ;
    private Integer business_id;
    private String sn1;
    private String key1;
    private String remark1;
    private String carnum1;
    private String msg1 ;
    private Integer state;
    private int num;
    private Integer ors;

    private String status;
    private int print ; // 已打印数量
    private int waiting ; //未打印数量


    public Printer() {
    }

    public Printer(Integer business_id, String sn1, String key1, String remark1, String carnum1 ,String msg1 ,Integer num ,Integer ors) {
        this.business_id = business_id;
        this.sn1 = sn1;
        this.key1 = key1;
        this.remark1 = remark1;
        this.carnum1 = carnum1;
        this.msg1 = msg1;
        this.num = num;
        this.ors = ors;
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

    public String getSn1() {
        return sn1;
    }

    public void setSn1(String sn1) {
        this.sn1 = sn1;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getCarnum1() {
        return carnum1;
    }

    public void setCarnum1(String carnum1) {
        this.carnum1 = carnum1;
    }

    public String getMsg1() {
        return msg1;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrint() {
        return print;
    }

    public void setPrint(int print) {
        this.print = print;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public Integer getOrs() {
        return ors;
    }

    public void setOrs(Integer ors) {
        this.ors = ors;
    }
}
