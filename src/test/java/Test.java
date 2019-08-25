import com.shengxian.common.MothPrinter;
import com.shengxian.common.util.DateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-06-11
 * @Version: 1.0
 */
public class Test {

    /**
          * 获取当前月、前一月、后一月的第一天和最后一天
          * @param sdf
          */
        public static void getMonth(SimpleDateFormat sdf){
        //定义当前月的总天数，比如30,31,28,29
        int maxCurrentMonthDay=0;
            Calendar calendar=Calendar.getInstance();
            System.out.println("当前时间: "+sdf.format(calendar.getTime()));

//当月一号
            Calendar calendar1=Calendar.getInstance();
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            System.out.println("本月第一天: "+sdf.format(calendar1.getTime()));
//当月最后一天
            Calendar calendar2=Calendar.getInstance();
            maxCurrentMonthDay=calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar2.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
            System.out.println("本月最后一天: "+sdf.format(calendar2.getTime()));

        /*//下个月一号
        Calendar calendar3=Calendar.getInstance();
        maxCurrentMonthDay=calendar3.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar3.add(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
             calendar3.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("下月第一天: "+sdf.format(calendar3.getTime()));
        //下个月最后一天
        Calendar calendar4=Calendar.getInstance();
        maxCurrentMonthDay=calendar4.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar4.add(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        //第一个maxCurrentMonthDay获取的是当月的天数，第二个maxCurrentMonthDay获取的是下个月的天数
        maxCurrentMonthDay=calendar4.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar4.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        System.out.println("下月第一天: "+sdf.format(calendar4.getTime()));
        
        //上个月一号
        Calendar calendar5=Calendar.getInstance();
        maxCurrentMonthDay=calendar5.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar5.add(Calendar.DAY_OF_MONTH, -maxCurrentMonthDay);
        calendar5.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("上月第一天: "+sdf.format(calendar5.getTime()));
        //上个月最后一天
        Calendar calendar6=Calendar.getInstance();
        maxCurrentMonthDay=calendar6.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar6.add(Calendar.DAY_OF_MONTH, -maxCurrentMonthDay);
        //第一个maxCurrentMonthDay获取的是当月的天数，第二个maxCurrentMonthDay获取的是上个月的天数
        maxCurrentMonthDay=calendar6.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar6.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        System.out.println("上月第一天: "+sdf.format(calendar6.getTime()));*/

    }
  public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字、中文
        // String regEx="[^a-zA-Z0-9]";
      // 清除掉[]中所有特殊字符
      String regEx = "[`~☆★!@#$%^&*()+=|{}':;,\\[\\]》·.<>/?~！@#￥%……（）——+|{}【】‘；：”“’。，、？]";
      Pattern p = Pattern.compile(regEx);
      Matcher m = p.matcher(str);
      return m.replaceAll("").trim().replace(" ", "").replace("\\", "");
    }


    public static long test(long a){
            return a;
    }

    public static void main(String[] args) {

        long a = test(1);

        System.out.println(a);

        String str = "";
        System.out.println(str);
        System.out.println(StringFilter(str));
     /*       String[] a = {"10.00" ,"12.00" ,"35.00" ,"20.00","10.00" ,"2.00" ,"35.00" ,"2.00" };
        StringBuffer size = new StringBuffer();
        String s = "";
        for (String c :a){
             s += MothPrinter.calculatorPrintUtils(c ,size);
        }
        System.out.print(s);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //addDay(sdf,10);
        //getWeekDay(sdf);
        getMonth(sdf);*/
       /* String s = DateUtil.thisMonthOneNum();
        System.out.println(s);*/
    }

}
