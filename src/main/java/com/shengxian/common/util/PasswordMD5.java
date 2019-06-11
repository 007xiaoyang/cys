package com.shengxian.common.util;


import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

//import sun.misc.BASE64Encoder;
//import java.security.MessageDigest;

/**
 * MD5密码加密工具类
 * @author 卢幸
 */
public class PasswordMD5 {
    /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        //MessageDigest md5=MessageDigest.getInstance("MD5");
        //BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        return DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
    }
}
