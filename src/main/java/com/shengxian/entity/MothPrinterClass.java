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
    private BigDecimal price ; //产品单价
    private BigDecimal money ; //每个产品合计
    private Integer type;

    public MothPrinterClass() {
    }

    public MothPrinterClass(String goodsName, Double num, BigDecimal price, BigDecimal money) {
        this.goodsName = goodsName;
        this.num = num;
        this.price = price;
        this.money = money;
    }

    public static String  divisionString(String goods , String num ,BigDecimal price ,BigDecimal money ){

        String value = "";
        int goodsLength = goods.length();
        int numberLength = num.length();


        if (goodsLength == 1){
            value += goods +"           ";
        }else if (goodsLength == 2 ){
            value += goods +"         ";
        }else if (goodsLength == 3 ){
            value += goods +"       ";
        }else if (goodsLength == 4 ){
            value += goods +"     ";
        }else if (goodsLength == 5 ){
            value += goods +"   ";
        }else if (goodsLength == 6 ){
            value += goods +" ";
        }else if (goodsLength > 6 ){
            value += goods.substring(0 , 6) +" ";
        }

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
        return value+price+"元<BR>"+money+"元";
    }

    public static void main(String[] args) {
        String a = "地方的噶撒的发";
        String substring = a.substring(0, 6);
        System.out.println(substring);
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
}
