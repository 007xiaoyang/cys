package com.shengxian.service;

import com.shengxian.entity.Menu;
import com.shengxian.entity.MenuRole;
import com.shengxian.entity.Printer;
import com.shengxian.entity.Template;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public interface MenuService {


    /**
     * 菜单
     * @param level 级别
     * @param sort 上级id
     * @return
     */
    List<HashMap> selectOneMenu(Integer level, Integer sort);

    /**
     * 通过一级菜单查询二级菜单
     * @param token
     * @param menuId 一级菜单id
     * @return
     */
    List<Menu> selectTwoMenuList(String token ,Integer role , Integer menuId) throws NullPointerException;

    /**
     * 查询当前用户是否有第三级菜单
     * 的权限
     * @param token
     * @return
     */
    boolean isPower(String token, Integer menu_id)throws NullPointerException ;


    /**
     * 获取员工角色
     * @param staff_id
     * @param type
     * @return
     */
    List<Integer> staffRole(Integer staff_id, Integer type);


    /**
     * 修改员工角色
     * @param menuRole
     * @return
     */
    Integer udpateStaffRole(MenuRole menuRole);


    /**
     * 添加模板
     * @param token
     * @param title
     * @param name
     * @param phone
     * @param type
     * @return
     */
    Integer addTemplate(String token ,Integer role , String title, String name, String phone, Integer type);

    /**
     * 修改模板
     * @param template
     * @return
     */
    Integer updateTemplate(Template template)throws Exception;

    /**
     * 恢复模板
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    Integer updateRecoveryTemplate(Integer id, Integer type)throws Exception;

    /**
     * 修改模板二维码显示状态
     * @param id
     * @param state
     * @return
     * @throws Exception
     */
    Integer updateTemplateState(Integer id, Integer state)throws Exception;

    /**
     * 删除log
     * @param id
     * @return
     */
    Integer deleteTemplateLog(Integer id);

    /**
     * 通过员工id查询一级菜单权限
     * @param staff_id
     * @return
     */
    List<Menu> findStaffOneMenu(Integer staff_id)throws Exception;

    /**
     * 通过员工id和一级菜单id查询二级菜单
     * @param staff_id
     * @return
     */
    List<Menu> findStaffTwoMenu(Integer staff_id ,Integer sort)throws Exception;

    /**
     * 通过员工id和二级菜单id查询三级菜单
     * @param staff_id
     * @return
     */
    List<Menu> findStaffThreeMenu(Integer staff_id ,Integer sort)throws Exception;

    /**
     * 修改员工菜单权限
     * @param oneMenus
     * @return
     */
    Integer updateStaffMenu(Menu[] oneMenus ,Menu[] twoMenu ,Menu[] threeMenu)throws Exception;

    /**
     * 三级菜单
     * @param token
     * @param role
     * @param sort
     * @return
     */
    List<Menu> selectThreeMenu(String token ,Integer role , Integer sort);

    /**
     * 员工APP菜单
     * @param staff_id
     * @return
     */
    List<Menu> selecStaffAppMenu(Integer staff_id);

    /**
     * 修改员工APP菜单
     * @param menu
     * @return
     */
    Integer updateStaffAppMenu(Menu[] menu);

    /**
     * 添加飞蛾打印机
     * @param token
     * @param role
     * @return
     */
    Integer addPrinter(String token ,Integer role ,String sn, String key, String remark, String carnum,Integer num ,Integer ors)throws NullPointerException, Exception;


    /**
     * 查询店铺打印机
     * @param token
     * @param role
     * @return
     */
    List<Printer> queryPrinter(String token ,Integer role) throws UnsupportedEncodingException;

    /**
     * 修改打印机
     * @param token
     * @param role
     * @param sn
     * @param name
     * @param phonenum
     * @return
     */
    Integer printerEdit(String token,Integer role ,String sn,String name ,String phonenum ,Integer num  ,Integer ORS)throws NullPointerException, Exception;

    /**
     * 删除打印机
     * @param token
     * @param role
     * @param sn
     * @return
     */
    Integer printerDelList(String token,Integer role ,String sn)throws NullPointerException, Exception ;

    /**
     * 启用打印机
     * @param id
     * @param state
     * @return
     */
    Integer enablePrinter(Integer id ,Integer state);

    /**
     * 通过id查询打印机信息
     * @param id
     * @return
     */
    Printer selectPrinterById(@Param("id") Integer id );

    /**
     * 修改店铺使用哪个单据模块
     * @param token
     * @param role
     * @param type
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer updateBusinessPrinterModular(String token ,Integer role ,Integer type)throws NullPointerException ,Exception;

    /**
     * 查询店铺使用哪个单据模块
     * @param token
     * @param role
     * @return
     */
    Integer queryBusinessPrinterModular(String token ,Integer role);

    /**
     * 调飞蛾打印机打印（销售订单）
     * @param order_id
     * @param titile
     * @return
     */
    void mothPrintSale(String token ,Integer role ,Integer order_id ,String titile)throws NullPointerException ,Exception;

    /**
     * 调飞蛾打印机打印（采购订单）
     * @param order_id
     * @param titile
     */
    void mothPrintPurchase(String token ,Integer role ,Integer order_id  ,String titile)throws NullPointerException ,Exception;

    /**
     * 调飞蛾打印机打印（临时订单）
     * @param order_id
     * @param titile
     * @return
     */
    void mothPrintTemporarySale(String token ,Integer role ,Integer order_id ,String titile)throws NullPointerException ,Exception;

    /**
     * 积分兑换打印
     * @param token
     * @param role
     * @param goodsName
     * @param price
     * @param number
     * @param num
     * @param userName
     * @param phone
     * @param address
     * @param time
     */
    void  mothPrintExchange(String token ,Integer role ,String goodsName  ,String price ,String number ,String num ,String userName ,String phone ,String address ,String time);

}
