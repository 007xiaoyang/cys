package com.shengxian.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-10-10
 * @Version: 1.0
 */
@ApiModel(value = "店铺对象模型")
public class Business {

    private Integer id;

    @ApiModelProperty(value = "店铺图片" ,required = true)
    private String img;

    @ApiModelProperty(value = "店铺类别id" ,required = true)
    private Integer category_id; //关联商家类别表ID

    @ApiModelProperty(value = "编号" ,required = true)
    private String number; //编号

    @ApiModelProperty(value = "手机号" ,required = true)
    private String phone; //手机号

    @ApiModelProperty(value = "密码" ,required = true)
    private String password;

    @ApiModelProperty(value = "名称" ,required = true)
    private String name; //名称

    @ApiModelProperty(value = "店铺名称" ,required = true)
    private String store_name; //商店名称

    @ApiModelProperty(value = "使用权限" ,required = true)
    private Integer duration; //使用期限

    private String TDCode; //店铺二维码路径
    private String token;

    @ApiModelProperty(value = "库存设置，0低于库存也能购买，1不能购买" ,required = true)
    private int amount_set;

    @ApiModelProperty(value = "起送价" ,required = true)
    private double starting_price;// 起送价

    @ApiModelProperty(value = "配送范围" ,required = true)
    private String confine;// 配送范围

    @ApiModelProperty(value = "营业时间" ,required = true)
    private String business_hours; //营业时间

    @ApiModelProperty(value = "公告" ,required = true)
    private String notice; //公告

    @ApiModelProperty(value = "商家电话" ,required = true)
    private String telephone;//商家电话

    @ApiModelProperty(value = "登录权限，0默认一人登录，1多人登录" ,required = true)
    private Integer power; //登录权限，0默认一人登录，1多人登录

    @ApiModelProperty(value = "邀请人号码" ,required = true)
    private String invitation; //邀请人号码

    @ApiModelProperty(value = "说明" ,required = true)
    private String illustrated; //说明

    @ApiModelProperty(value = "备注" ,required = true)
    private String remarks; //备注

    private String device_id; //设备id

    @ApiModelProperty(value = "创建时间" ,required = true)
    private Date create_time; //创建时间

    private Date update_time; //

    private Integer is_disable; //0默认，1禁用

    private Integer is_del; //0默认,1删除

    @ApiModelProperty(value = "地址" ,required = true)
    private String address;//地址

    private String LAL;//经纬度

    public Business() {
    }

    public Business(String phone, String password,String number, String store_name, String token,String invitation, Date create_time) {
        this.phone = phone;
        this.password = password;
        this.number = number;
        this.store_name = store_name;
        this.token = token;
        this.invitation=invitation;
        this.create_time = create_time;
    }

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

    public int getAmount_set() {
        return amount_set;
    }

    public void setAmount_set(int amount_set) {
        this.amount_set = amount_set;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTDCode() {
        return TDCode;
    }

    public void setTDCode(String TDCode) {
        this.TDCode = TDCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(double starting_price) {
        this.starting_price = starting_price;
    }

    public String getConfine() {
        return confine;
    }

    public void setConfine(String confine) {
        this.confine = confine;
    }

    public String getBusiness_hours() {
        return business_hours;
    }

    public void setBusiness_hours(String business_hours) {
        this.business_hours = business_hours;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getIllustrated() {
        return illustrated;
    }

    public void setIllustrated(String illustrated) {
        this.illustrated = illustrated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Integer getIs_disable() {
        return is_disable;
    }

    public void setIs_disable(Integer is_disable) {
        this.is_disable = is_disable;
    }

    public Integer getIs_del() {
        return is_del;
    }

    public void setIs_del(Integer is_del) {
        this.is_del = is_del;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLAL() {
        return LAL;
    }

    public void setLAL(String LAL) {
        this.LAL = LAL;
    }
}
