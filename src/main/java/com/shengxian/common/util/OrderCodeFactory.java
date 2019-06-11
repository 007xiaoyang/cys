package com.shengxian.common.util;

import java.util.Date;
import java.util.Random;

/**
 * Description: 订单编码码生成器，生成32位数字编码，
 * 1位单号类型+17位时间戳+14位(用户id加密&随机数)
 * @Author: yang
 * @Date: 2018-11-06
 * @Version: 1.0
 */
public class OrderCodeFactory {

    /**
     * 线上订单类别头
     */
    private static final String ONLINE_ORDER_CODE = "5";
    /**
     * 线下订单类别头
     */
    private static final String UNDERLINE_ORDER_CODE = "2";
    /**
     * 退货类别头
     */
    private static final String RETURN_ORDER = "3";
    /**
     * 退款类别头
     */
    private static final String REFUND_ORDER = "4";
    /**
     * 未付款重新支付别头
     */
    private static final String AGAIN_ORDER = "5";

    /**
     * 采购订单别头
     */
    private static final String PURCHASE_ORDER = "9";
    /**
     * 随即编码
     */
    private static final int[] r = new int[]{7, 9, 6, 2, 8 , 1, 3, 0, 5, 4};
    /**
     * 用户id和随机数总长度
     */
    //private static final int maxLength = 14;


    /**
     * 根据id进行加密+加随机数组成固定长度编码
     * @param id 用户id
     * @param maxLength 用户id和随机数的总长度
     * @return
     */
    private static String toCode(Long id,Integer maxLength ) {
        String idStr = id.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1 ; i >= 0; i--) {
            idsbs.append(r[idStr.charAt(i)-'0']);
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }



    /**
     * 生成固定长度随机码
     * @param n 长度
     * @return
     */
    private static long getRandom(long n) {
        long min = 1,max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
        return rangeLong;
    }

    /**
     * 生成不带类别标头的编码
     * @param userId
     * @return
     */
    private static synchronized String getCode(Long userId,Integer maxLength ){
        userId = userId == null ? 10000 : userId;
        return DateUtil.format(new Date(),"YYMMddHHmmssSSS")+ toCode(userId,maxLength);
    }

    /**
     *  生成线上订单单号编码
     * @param userId
     * @return
     */
    public static String getOnlineOrderCode(String code , Long userId,Integer maxLength){
        return code + getCode(userId,maxLength);
    }

    /**
     * 生成线下订单单号编码
     * @param userId
     * @return
     */
    public static String getUnderLineOrderCode(Long userId,Integer maxLength){
        return UNDERLINE_ORDER_CODE+getCode(userId,maxLength);
    }

    /**
     * 生成退货单号编码
     * @param userId
     * @return
     */
    public static String getReturnCode(Long userId,Integer maxLength){
        return RETURN_ORDER+getCode(userId,maxLength);
    }

    /**
     * 生成退款单号编码
     * @param userId
     * @return
     */
    public static String getRefundCode(Long userId,Integer maxLength){
        return REFUND_ORDER + getCode(userId,maxLength);
    }

    /**
     * 生成未付款重新支付
     * @param userId
     * @return
     */
    public static String getAgainCode(Long userId,Integer maxLength){
        return AGAIN_ORDER + getCode(userId,maxLength);
    }

    /**
     * 生产采购订单编码
     * @param userId
     * @return
     */
    public static String getPurchaseOrder(Long userId,Integer maxLength){
        return PURCHASE_ORDER + getCode(userId,maxLength);
    }



    public static String getStringRandom(int one ,int code){

        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i = 0; i < one; i++) {
            buffer.append((char)(random.nextInt(26) + 65));
        }
        return buffer.toString()+getRandom(code);
    }

    public static void main(String[] args) {
        String stringRandom = getStringRandom(4,4 );
        System.out.println(stringRandom);
        String stringRandom2 = getStringRandom(3,4 );
        System.out.println(stringRandom2);
        String stringRandom1= getStringRandom(5,4 );
        System.out.println(stringRandom1);
    }
}
