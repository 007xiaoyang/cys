package com.shengxian.common.util;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-01-09
 * @Version: 1.0
 */
public class IntegerUtils {

    public static boolean isEmpty(Integer s){
        return s == null || s == 0;
    }

    /**
     * 字符串判断
     * @param a
     * @param b
     * @return
     */
    public static boolean isEmpty(String a ,String b ){
        return  a != null && !"".equals(a) && b != null && !"".equals(b);
    }

    /**
     * 字符串判断
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        return s != null && !"".equals(s);
    }



    public static boolean isNotEmpty(Integer s){
        return s != null && s != 0;
    }


    public static boolean endsWith(String str ,String end ){

        return str.endsWith(str);
    }



    public static void main(String[] args) {

        String str = "123_hello" , endLLO = "llo", endELL = "ell";

        boolean a = endsWith(str, endLLO);

        boolean b = endsWith(str, endELL);

        System.out.println(a);
        System.out.println(b);

    }

}
