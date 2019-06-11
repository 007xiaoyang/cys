import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-06-11
 * @Version: 1.0
 */
public class Test {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        StringBuffer sb = new StringBuffer();

        int size = list.size();
        System.out.println(size);
        for (Integer id :list){
            sb.append(id +   ",");
        }
        System.out.println(sb.toString());
    }

}
