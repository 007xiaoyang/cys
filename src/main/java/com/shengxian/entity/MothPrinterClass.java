package com.shengxian.entity;

import net.sf.jsqlparser.expression.operators.arithmetic.Division;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 飞蛾打印机类
 *
 * @Author: yang
 * @Date: 2019-04-28
 * @Version: 1.0
 */
public class MothPrinterClass {
    private Integer id ;
    private String goodsName ; //产品名称
    private Double num; //产品数量
    private String  units;
    private BigDecimal price ; //产品单价
    private BigDecimal money ; //每个产品合计
    private Integer type;

    public MothPrinterClass() {
    }

    public MothPrinterClass(String  units, Double num, BigDecimal price, BigDecimal money) {
        this. units =  units;
        this.num = num;
        this.price = price;
        this.money = money;
    }

    public static String  divisionString( String num ,BigDecimal price ,BigDecimal money ){

        String value = "";
        int numberLength = num.length();
        String pl = price.toString() ;
        int priceLength = pl.length();

        if (numberLength == 1 ){
            value += num + "        ";
        }else if (numberLength == 2 ){
            value += num + "       ";
        }else if (numberLength == 3 ){
            value += num + "      ";
        }else if (numberLength == 4){
            value += num + "     ";
        }else if (numberLength == 5){
            value += num + "    ";
        }else if (numberLength == 6){
            value += num + "   ";
        }else if (numberLength == 7){
            value += num + "  ";
        }else if (numberLength == 8){
            value += num + " ";
        }else if (numberLength == 9){
            value += num ;
        }else if (numberLength > 9){
            value += num.substring( 0  , 9);
        }

        if (priceLength == 1 ){
            value += price + "元        ";
        }else if (priceLength == 2 ){
            value += price + "元       ";
        }else if (priceLength == 3 ){
            value += price + "元      ";
        }else if (priceLength == 4){
            value += price + "元     ";
        }else if (priceLength == 5){
            value += price + "元    ";
        }else if (priceLength == 6){
            value += price + "元   ";
        }else if (priceLength == 7){
            value += price + "元  ";
        }else if (priceLength == 8){
            value += price + "元 ";
        }else if (priceLength == 9){
            value += price ;
        }else if (priceLength > 9){
            value += price.toString().substring( 0  , 9)+"元";
        }
        return value+money+"元<BR>";
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


    @Override
    public String toString() {
        return "MothPrinterClass{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", num=" + num +
                ", units='" + units + '\'' +
                ", price=" + price +
                ", money=" + money +
                ", type=" + type +
                '}';
    }
}
