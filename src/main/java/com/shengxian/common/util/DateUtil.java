package com.shengxian.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description: 获取时间工具类的集和类
 *
 * @Author: yang
 * @Date: 2018/8/17
 * @Version: 1.0
 */
public class DateUtil {

    /**
     * 获取年yyyy时间格式
     */
    public final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    /**
     * 获取年月日yyyy-MM-dd时间格式
     */
    public final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取年月日yyyyMMdd时间格式
     */
    public final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
    /**
     *  获取年月日时分秒yyyy-MM-dd HH:mm:ss时间格式
     */
    public final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取年月日时分秒毫秒yyyy-MM-dd HH:mm:ss时间格式
     */
    public final static SimpleDateFormat sdfmsTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     * 获取年月日时分秒yyyyMMddHHmmss时间格式
     */
    public final static SimpleDateFormat allTime = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取YYYY格式 不传参
     *
     * @return
     */
    public static String getYear(){
        return sdfYear.format(new Date());
    }
    /**
     * 获取YYYY格式 传参
     * @param date
     * @return
     */
    public static String getYear(Date date){
        return sdfYear.format(date);
    }


    public static String getYM(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date endDate=new Date();
        Calendar date=Calendar.getInstance();
        date.setTime(endDate);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1);
        return sdf.format(date.getTime());
    }


    /**
     * 获取YYYY-MM-DD格式 不传参
     * @return
     */
    public static String getDay(){
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式 传参
     * @param date
     * @return
     */
    public static String getDay(Date date){
        return sdfDay.format(date);
    }

    /**
     * 获取YYYYMMDD格式 不传参
     * @return
     */
    public static String getDays(){
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式 传参
     * @param date
     * @return
     */
    public static String getDays(Date date){
        return sdfDays.format(date);
    }

    /**
     * 获取YYYY-MM-dd HH:mm:ss格式 不传参
     * @return
     */
    public static String getTime(){
        return sdfTime.format(new Date());
    }

    /**
     * 获取YYYY-MM-dd HH:mm:ss格式 传参
     * @param date
     * @return
     */
    public static String getTime(Date date){
        return sdfTime.format(date);
    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss.SSS格式 不传参
     * @return
     */
    public static String getMsTime(){
        return sdfmsTime.format(new Date());
    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss.SSS格式 传参
     * @param date
     * @return
     */
    public static String getMsTime(Date date){
        return sdfmsTime.format(date);
    }

    /**
     * 获取YYYYMMDDHHmmss格式 不传参
     * @return
     */
    public static String getAllTime(){
        return allTime.format(new Date());
    }

    /**
     * 获取YYYYMMDDHHmmss格式 传参
     * @param date
     * @return
     */
    public static String getAllTime(Date date){
        return allTime.format(date);
    }


    /**
     * 比较日期 （如果s >= e 返回ture 否则返回 false）
     * @param s
     * @param e
     * @return
     */
    public static boolean compareDate(Date s ,Date e){
        if (s == null || s.equals("") || e == null || e.equals(""))
            return false;

        return s.getTime() >= e.getTime();
    }
    /**
     * 比较日期 （如果s >= e 返回ture 否则返回 false）
     * @param s
     * @param e
     * @return
     */
    public static boolean compareDate(String s ,String e){

        if (s == null || s.equals("") || e == null || e.equals("")){
            return false;
        }

        if (parseDate(s) == null || parseDate(e) == null){
            return false;
        }

        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * 比较日期 （如果s > e 返回ture 否则返回 false）
     * @param s (yyyy-MM-dd HH:mm:ss）
     * @param e (yyyy-MM-dd HH:mm:ss）
     * @return
     */
    public static boolean compareDateTime(String s ,String e){

        if (s == null || s.equals("") || e == null || e.equals("")){
            return false;
        }
        return parseTime(s).getTime() > parseTime(e).getTime();
    }

    /**
     * 比较日期 （如果s <= e 返回ture 否则返回 false）
     * @param s
     * @param e
     * @return
     */
    public static boolean compareDate1(String s ,String e){

        if (s == null || s.equals("") || e == null || e.equals("")){
            return false;
        }


        if (parseDate(s) == null || parseDate(e) == null){
            return false;
        }
        return parseDate(s).getTime() <= parseDate(e).getTime();
    }

    /**
     * 格式化日期 (yyyy-MM-dd)
     * 字符串转换Date
     * @param date
     * @return
     */
    public static Date parseDate(String date){
        try {
            return sdfDay.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期 (yyyy-MM-dd HH:mm:ss）
     * 字符串转换Date
     * @param date
     * @return
     */
    public static Date parseTime(String date){
        try {
            return sdfTime.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期 （自定义）
     * 字符串转换Date
     * @param date
     * @param pattern
     * @return
     */
    public static Date parseDate(String date ,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
           return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期 （自定义）
     * Date转换字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 把日期转换为时间戳
     * @param date
     * @return
     */
    public static Timestamp fromat(Date date){
        return  new Timestamp(date.getTime());
    }

    /**
     * 效验日期是否合法
     * @param s
     * @return
     */
    public static boolean isValidDate(String s){
        try {
            sdfTime.parse(s);
            return true;
        } catch (ParseException e) {
          //如果throw java text.ParesException 或者NumllpointerException ,就说明格式不对
            return false;
        }
    }

    /**
     * 效验日期是否合法 (自定义)
     * @param s
     * @param pattern
     * @return
     */
    public static boolean isValiDate(String s ,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            format.parse(s);
            return true;
        } catch (ParseException e) {
            //如果throw java text.ParesException 或者NumllpointerException ,就说明格式不对
            return  false;
        }
    }

    /**
     * 功能描述：计算一年的天数
     * @param starTime
     * @param endTime
     * @return
     */
    public static int getDiffYear(String starTime ,String endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int)((format.parse(endTime).getTime() - format.parse(starTime).getTime())/(1000 * 60* 60 *24)/365);
            return years;
        } catch (ParseException e) {
            //如果throw java text.ParesException 或者NumllpointerException ,就说明格式不对
            return 0;
        }
    }

    /**
     *功能描述：计算一年的天数 （ 自定义）
     * @param starTime
     * @param endTime
     * @param pattern
     * @return
     */
    public static int getDiffYear(String starTime ,String endTime ,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            int years = (int)((format.parse(endTime).getTime() - format.parse(starTime).getTime())/(1000 * 60* 60 *24)/365);
            return years;
        } catch (ParseException e) {
            //如果throw java text.ParesException 或者NumllpointerException ,就说明格式不对
            return 0;
        }
    }

    /**
     * 功能描述：时间想减得到天数
     * 字符串时间格式
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getDaySub(String beginDateStr ,String endDateStr){
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        Date beginDate = null ;
        Date endDate = null;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = (endDate.getTime() - beginDate.getTime())*1000; //得到两个时间相差的毫秒数
        day = l /(24 * 3600 );//得到相隔天数 （小时）
        long hours = l%(24*3600)/3600; //得到分钟
        long min = l % (3600 )/60;
        System.out.println(day);
        System.out.println(hours);
        System.out.println(min);
        return day;
    }

    /**
     * 功能描述：时间想减得到天数
     * 字符串时间格式
     * @param endDate
     * @param
     * @return
     */
    public static long getDay(String endDate ){
        String beginDate = getMsTime();
        return getDaySub(beginDate, endDate);
    }

    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days){
        int daysInt = Integer.parseInt(days);
        Calendar calendar = Calendar.getInstance();//java.utli包
        calendar.add(Calendar.DATE,daysInt); //日期减，如果不够减会将月变动
        Date date = calendar.getTime();//获取时间戳
        String dateStr = getTime(date);
        return dateStr;
    }

    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days){
        int daysInt = Integer.parseInt(days);
        Calendar calendar = Calendar.getInstance();//java.utli包
        calendar.add(Calendar.DATE,daysInt); //日期减，如果不够减会将月变动
        Date date = calendar.getTime();
        String dateStr = format(date, "E");
        return dateStr;
    }

    /**
     * 把long类型时间戳转换成字符串时间 （自定义）
     * @param millSec
     * @param pattern
     * @return
     */
    public static String transferLongToDate(Long millSec ,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(millSec);
        return format.format(date);
    }

    /**
     * 把String 类型时间转换成long类型时间戳 （自定义）
     * @param time
     * @param pattern
     * @return
     */
    public static long transferStringToLong(String time ,String pattern) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = format.parse(time);
          return date.getTime();
    }



    /**
     * 得到几天后的时间
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+day);
        return calendar.getTime();
    }

    /**
     * 获得当天零时零分零秒
     * @return
     */
    public static String initDateByDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return getTime(calendar.getTime());
    }





    /**
     * 获取YYYY-MM-DD格式 传参
     * @param date
     * @return
     */
    public static String getDaye(Date date ,String hm){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(date)+" "+hm;
    }


    public static void main(String[] args) {
/*
        //数据库拼接起来的时间
        String startTime = "2019-04-12 12:30";

        //课程开始多少分钟后答题
        String start = "5";
        //转换成int类型
        Integer i = Integer.valueOf(start);

        Date time = parseDate(startTime ,"yyyy-MM-dd HH:mm");

        long l = time.getTime() + i * 60 * 1000;
        String s = transferLongToDate(l, "yyyy-MM-dd HH:mm");

        //比较两个时间大小
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");*/

        Date date = new Date();
        System.out.println(date);
        String a = "2019-05-06 05:00:00";
        String b = "2019-05-06 09:31:00";



        String c = "2019-04";
        String d = "2019-04";


       /* if (!(a.compareTo(b) <= 0 && c.compareTo(d) >= 0)){
            System.out.println("不成立");
        }*/

        boolean b1 = compareDateTime(a, b);
        System.out.println(b1);


    }
}
