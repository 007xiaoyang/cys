package com.shengxian.common.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Description: 高频方法集和类
 *
 * @Author: yang
 * @Date: 2018/8/17
 * @Version: 1.0
 */
public class ToolUTtil {

    /**
     * 判断一个对象是否是时间类型
     * @param o
     * @return
     */
    public static String dateType(Object o){
        if (o instanceof Date){
            return DateUtil.getDay((Date)o);
        }else {
            return o.toString();
        }
    }

    /**
     * 生成随机数
     * @return
     */
    public static String getRandomNum(){
        return System.currentTimeMillis() + generateCellPhoneValNum();

    }

    /**
     * 获取0-9随机数字
     * @return
     */
    public static String generateCellPhoneValNum(){
        String [] beforeShuffle = new String[]{"1","2","3","4","5","5","7","8","9","0"};
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0;i < list.size();i++){
            buffer.append(list.get(i));
        }
        String afterShuffle = buffer.toString();
        return afterShuffle.substring(3,9);
    }

    /**
     * 比较两个对象是否相等
     * 相同的条件有两个，满足其一即可
     * 1 obj1 == null && obj2 == null ; 2 obj1.equals(obj2)
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1,Object obj2){
        return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
    }

    /**
     * 对象中是否包含元素
     * @param obj
     * @param element
     * @return
     */
    public static boolean contains(Object obj,Object element){
        if (obj == null){
            return false;
        }
        if (obj instanceof String ){
            if (element == null){
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if(obj instanceof Collections){
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map){
            return ((Map<?,?>) obj).values().contains(element);
        }
        if (obj instanceof Integer){
            Iterator<?> iter =(Iterator<?>) obj;
            while(iter.hasNext()){
                Object o = iter.next();
                if (equals(o,element)){
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration){
            Enumeration<?> enumeration =(Enumeration<?>) obj;
            while (enumeration.hasMoreElements()){
                Object o = enumeration.nextElement();
                if (equals(o,element)){
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equals(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对象是否不为空
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o){
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     * @param o String ,List,Map ,Object[],int[],long[]
     * @return
     */
    public static boolean isEmpty(Object o){
        if (o == null){
            return true;
        }
        if ( o instanceof String ){
            if (o.toString().trim().equals("")){
                return true;
            }
        }else if (o instanceof List){
            if (((List) o).size() == 0){
                return true;
            }
        }else if ( o instanceof  Map){
            if (((Map) o).size() == 0){
                return true;
            }
        }else if (o instanceof Set){
            if (((Set) o).size() == 0){
                return true;
            }
        }else if (o instanceof Object[]){
            if (((Object[]) o).length == 0){
                return true;
            }
        }else if (o instanceof  int[]){
            if(((long[]) o ).length == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否存在 Empty Object
     * @param os 对象组
     * @return
     */
    public static boolean isOneEmpty(Object... os){
        for (Object o :os){
            if (isEmpty(o)){
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组是否全是 Empty Object
     * @param os
     * @return
     */
    public static boolean isAllEmpry(Object... os){
      for (Object o:os){
          if (!isEmpty(o)){
              return false;
          }
        }
        return true;
    }

    /**
     * 是否为数字
     * @param obj
     * @return
     */
    public static boolean isNum(Object obj){
        try {
            Integer.parseInt(obj.toString());
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 如果为空，则调用默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static Object getValue(Object str ,Object defaultValue){
        if (isEmpty(str)){
            return defaultValue;
        }
        return str;
    }



   /* public static String format(String template ,Object... values){
        return
    }*/




    public static void main(String[] args) {
        boolean num = isNum("124533434");
        System.out.println(num);

    }
}
