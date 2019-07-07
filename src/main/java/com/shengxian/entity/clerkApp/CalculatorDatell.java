package com.shengxian.entity.clerkApp;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-07-07
 * @Version: 1.0
 */
public class CalculatorDatell {

    private Integer id ;
    private Integer calculatorId;
    private BigDecimal num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCalculatorId() {
        return calculatorId;
    }

    public void setCalculatorId(Integer calculatorId) {
        this.calculatorId = calculatorId;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "CalculatorDatell{" +
                "id=" + id +
                ", calculatorId=" + calculatorId +
                ", num=" + num +
                '}';
    }
}
