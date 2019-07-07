import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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


    public static void main(String[] args) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //addDay(sdf,10);
        //getWeekDay(sdf);
        getMonth(sdf);
    }

}
