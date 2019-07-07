package com.shengxian.entity;

import java.util.Arrays;
import java.util.Date;

/**
 * Description: 员工类
 *
 * @Author: yang
 * @Date: 2018/8/25
 * @Version: 1.0
 */
public class Staff {


    private Integer id; //
    private String phone; //手机号码
    private String password; //密码
    private Integer category_id; //关联员工类别表ID
    private String number; //编号
    private String name; //员工名称
    private String token; //
    private Integer business_id; //关联商家表ID
    private int shield; //屏蔽进价
    private int shield_inventory ; //屏蔽库存
    private int shieldMin ; //屏蔽最低售价
    private Date create_time; //创建时间
    private Integer is_disable; //0默认，1禁用
    private Integer is_del; //0默认,1删除

    private GoodsPercent[] goodsPercent;//产品提成
    private UserPercent[] userPercents; //用户提成
    private WarehousePercent[] warehousePercents; //仓库提成
    private OtherPercentage[] otherPercentages;

    public Staff() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getShield_inventory() {
        return shield_inventory;
    }

    public void setShield_inventory(int shield_inventory) {
        this.shield_inventory = shield_inventory;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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


    public GoodsPercent[] getGoodsPercent() {
        return goodsPercent;
    }

    public void setGoodsPercent(GoodsPercent[] goodsPercent) {
        this.goodsPercent = goodsPercent;
    }

    public UserPercent[] getUserPercents() {
        return userPercents;
    }

    public void setUserPercents(UserPercent[] userPercents) {
        this.userPercents = userPercents;
    }

    public WarehousePercent[] getWarehousePercents() {
        return warehousePercents;
    }

    public void setWarehousePercents(WarehousePercent[] warehousePercents) {
        this.warehousePercents = warehousePercents;
    }

    public int getShieldMin() {
        return shieldMin;
    }

    public void setShieldMin(int shieldMin) {
        this.shieldMin = shieldMin;
    }

    public OtherPercentage[] getOtherPercentages() {
        return otherPercentages;
    }

    public void setOtherPercentages(OtherPercentage[] otherPercentages) {
        this.otherPercentages = otherPercentages;
    }


    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", category_id=" + category_id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", business_id=" + business_id +
                ", shield=" + shield +
                ", shield_inventory=" + shield_inventory +
                ", shieldMin=" + shieldMin +
                ", create_time=" + create_time +
                ", is_disable=" + is_disable +
                ", is_del=" + is_del +
                ", goodsPercent=" + Arrays.toString(goodsPercent) +
                ", userPercents=" + Arrays.toString(userPercents) +
                ", warehousePercents=" + Arrays.toString(warehousePercents) +
                ", otherPercentages=" + Arrays.toString(otherPercentages) +
                '}';
    }
}
