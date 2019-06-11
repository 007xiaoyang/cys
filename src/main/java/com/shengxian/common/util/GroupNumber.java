package com.shengxian.common.util;

import java.util.HashMap;
import java.util.UUID;

/**
 * Description: 生成账号与编号工具类工具类
 *
 * @Author: yang
 * @Date: 2018/5/18
 * @Version: 1.0
 */
public class GroupNumber {

    public GroupNumber(){

    }

    /**
     * 自动生成账号
     * @return
     */
    public static Integer getAccount(){
        int result = (int) ((Math.random() * 9 + 1) * 100000);

        return result;
    }

    /**
     * 获取自动生成编号
     * @param number 从数据库里查出最后那条编号
     * @return
     */
    public static String getNumber( Integer number){
        return  generatedNumber(Integer.valueOf(number));
    }


    /**
     * 自动生成32位的token。
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }





    /**
     * 生成有规律的编号
     * @param num
     * @return
     */
    public static String generatedNumber(int num){
        num++;
        String result ;
        switch ((num+"").length()){
            case 1:
                result= ""+num;
                break;
            default:
                result = ""+num;
                break;
        }
        return result;
    }

    /**
     * 计算总数量提成
     * @param a 统计
     * @param b 满足
     * @param c 提成金额
     * @return
     */
    public static double value(Integer a,Integer b,Double c){
        double i = 0;
        if (a >= b){
            i = a/b*c;
        }
        return i;
    }

    /**
     * 计算吨位提成
     * @param a 多少斤
     * @param b 斤
     * @param c 提成金额
     * @return
     */
    public static double ton(Double a,Double b,Double c){
        double d = 0;
        if (a >= b){
            d = a / b * c;
        }
        return d;

        /*int i = 0;
        for (int j = 1;b>= j;j++){
            i +=2000;
        }
        double d=0;
        if (a>=i){
            d = a / i * c;
        }*/

    }
    /**
     * 计算总数量提成
     * @param a 统计
     * @param b 满足
     * @param c 提成金额
     * @return
     */
    public static double getValue(Double a,Double b,Double c){
        double i = 0;
        if (a >= b){
            i = a/b*c;
        }
        return i;
    }

    /**
     * 计算总数量提成
     * @param a 统计
     * @param b 满足
     * @param c 提成金额
     * @return
     */
    public static double getValue(Integer a,Integer b,Double c){
        double i = 0;
        if (a >= b){
            i = a/b*c;
        }
        return i;
    }

}
