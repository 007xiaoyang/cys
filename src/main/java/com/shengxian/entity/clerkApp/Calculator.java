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
    private Integer tatol ;
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

    public Integer getTatol() {
        return tatol;
    }

    public void setTatol(Integer tatol) {
        this.tatol = tatol;
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

    @Override
    public String toString() {
        return "Calculator{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", name=" + name +
                ", price=" + price +
                ", tatol=" + tatol +
                ", createTime=" + createTime +
                ", calculatorDatell=" + Arrays.toString(calculatorDatell) +
                '}';
    }
}
