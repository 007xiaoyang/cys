package com.shengxian.entity.clerkApp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-07-07
 * @Version: 1.0
 */
public class Calculator {

    private Integer id ;
    private Integer businessId;
    private String name ;
    private BigDecimal price;
    private Integer total ;
    private BigDecimal totalNum;
    private BigDecimal totalMoney;
    private Date createTime;

    private CalculatorDatell[] calculatorDatell;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CalculatorDatell[] getCalculatorDatell() {
        return calculatorDatell;
    }

    public void setCalculatorDatell(CalculatorDatell[] calculatorDatell) {
        this.calculatorDatell = calculatorDatell;
    }


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(BigDecimal totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "Calculator{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", total=" + total +
                ", totalNum=" + totalNum +
                ", createTime=" + createTime +
                ", calculatorDatell=" + Arrays.toString(calculatorDatell) +
                '}';
    }
}
