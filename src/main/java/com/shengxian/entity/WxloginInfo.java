package com.shengxian.entity;

import java.util.Date;

/**
 * @Description : WXloginInfo
 * @Author: yang
 * @Date: 2019-11-16
 * @Version: 1.0
 */
public class WxloginInfo {
    private long id;
    private String loginPhone;
    private int loginType;
    private Date loginDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @Override
    public String toString() {
        return "WXloginInfo{" +
                "id=" + id +
                ", loginPhone='" + loginPhone + '\'' +
                ", loginType=" + loginType +
                ", loginDate=" + loginDate +
                '}';
    }
}
