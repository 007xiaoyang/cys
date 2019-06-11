package com.shengxian.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-05-09
 * @Version: 1.0
 */
public class ShoppingHashMap {

    private String tic;
    private String storeName;

    private Integer TScId; // 购物车id
    private Integer count ; //总数
    private List TScDetail; //临时客户购物车详情
    private BigDecimal tatolMoney;

    public String getTic() {
        return tic;
    }

    public void setTic(String tic) {
        this.tic = tic;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getTScId() {
        return TScId;
    }

    public void setTScId(Integer TScId) {
        this.TScId = TScId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List getTScDetail() {
        return TScDetail;
    }

    public void setTScDetail(List TScDetail) {
        this.TScDetail = TScDetail;
    }

    public BigDecimal getTatolMoney() {
        return tatolMoney;
    }

    public void setTatolMoney(BigDecimal tatolMoney) {
        this.tatolMoney = tatolMoney;
    }
}
