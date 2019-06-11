package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 绑定用户类
 *
 * @Author: yang
 * @Date: 2018-10-24
 * @Version: 1.0
 */
public class BindUser {

    private Integer id;//绑定ID
    private Integer category_id;// 用户类别ID
    private String number;//用户编号
    private String user_name;//用户名称
    private String telephone;//电话
    private String illustrated1;//说明1
    private String illustrated2;//说明2
    private String remark1;//备注1
    private String remark2;//备注2
    private Integer scheme_id;//用户会员方案0-15种
    private String best_time;//配送时间
    private Double credit;//信用额度
    private Integer limited;//限制天数
    private String market_integral;//商场积分比例
    private String underline_integral;//线下积分
    private Integer business_id;//关联商家表ID
    private Integer user_id;//关联用户表ID
    private String notice;//公告
    private Date binding_time;//绑定时间
    private Date update_time;//更新时间
    private Double starting_price;//起送价
    private Integer source;//来源，0线上，1线下
    private String cycle;//进货周期
    private Integer is_del;
    private Integer staff_id;
    private String address;

    private String phone;//用户手机号，
    //员工提成
    private Integer spid;//员工的用户提成ID

    private String proportion;//员工用户提成


    /**
     * 绑定用户产品积分类
     */
    private BindUserGoodsIntegra[] bindUserGoodsIntegras;

    /**
     * 绑定用户收藏产品类
     */
    private BindUserGoodsCollection[] bindUserGoodsCollection;

    /**
     * 绑定用户屏蔽产品类
     */
    private BindUserGoodsShielding[] bindUserGoodsShielding;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIllustrated1() {
        return illustrated1;
    }

    public void setIllustrated1(String illustrated1) {
        this.illustrated1 = illustrated1;
    }

    public String getIllustrated2() {
        return illustrated2;
    }

    public void setIllustrated2(String illustrated2) {
        this.illustrated2 = illustrated2;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public Integer getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(Integer scheme_id) {
        this.scheme_id = scheme_id;
    }

    public String getBest_time() {
        return best_time;
    }

    public void setBest_time(String best_time) {
        this.best_time = best_time;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getLimited() {
        return limited;
    }

    public void setLimited(Integer limited) {
        this.limited = limited;
    }

    public String getMarket_integral() {
        return market_integral;
    }

    public void setMarket_integral(String market_integral) {
        this.market_integral = market_integral;
    }

    public String getUnderline_integral() {
        return underline_integral;
    }

    public void setUnderline_integral(String underline_integral) {
        this.underline_integral = underline_integral;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Date getBinding_time() {
        return binding_time;
    }

    public void setBinding_time(Date binding_time) {
        this.binding_time = binding_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Double getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(Double starting_price) {
        this.starting_price = starting_price;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public Integer getIs_del() {
        return is_del;
    }

    public void setIs_del(Integer is_del) {
        this.is_del = is_del;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public BindUserGoodsIntegra[] getBindUserGoodsIntegras() {
        return bindUserGoodsIntegras;
    }

    public void setBindUserGoodsIntegras(BindUserGoodsIntegra[] bindUserGoodsIntegras) {
        this.bindUserGoodsIntegras = bindUserGoodsIntegras;
    }

    public BindUserGoodsCollection[] getBindUserGoodsCollection() {
        return bindUserGoodsCollection;
    }

    public void setBindUserGoodsCollection(BindUserGoodsCollection[] bindUserGoodsCollection) {
        this.bindUserGoodsCollection = bindUserGoodsCollection;
    }

    public BindUserGoodsShielding[] getBindUserGoodsShielding() {
        return bindUserGoodsShielding;
    }

    public void setBindUserGoodsShielding(BindUserGoodsShielding[] bindUserGoodsShielding) {
        this.bindUserGoodsShielding = bindUserGoodsShielding;
    }
}
