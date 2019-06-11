package com.shengxian.mapper;

import com.shengxian.entity.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 菜单管理
 *
 * @Author: yang
 * @Date: 2018/9/6
 * @Version: 1.0
 */
@Mapper
public interface MenuMapper {

    /**
     * 通过商家用户查询第一级菜单列表
     * @param
     * @return
     */
    List<Menu> selectOneMenuList();


    /**
     * 首页菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> oneMenu(@Param("staff_id") Integer staff_id);

    /**
     * 销售菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> twoMenu(@Param("staff_id") Integer staff_id);

    /**
     * 采购菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> threeMenu(@Param("staff_id") Integer staff_id);

    /**
     * 库存菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> fourMenu(@Param("staff_id") Integer staff_id);

    /**
     * 配送菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> fiveMenu(@Param("staff_id") Integer staff_id);

    /**
     * 财务菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> sixMenu(@Param("staff_id") Integer staff_id);

    /**
     * 资料菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> sevenMenu(@Param("staff_id") Integer staff_id);

    /**
     * 员工菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> eightMenu(@Param("staff_id") Integer staff_id);

    /**
     * 设置菜单权限
     * @param staff_id
     * @return
     */
    List<Integer> nineMenu(@Param("staff_id") Integer staff_id);



    /**
     * 通过员工ID查询三级菜单权限
     * @param staff_id
     * @return
     */
    String selectThreeMenuIdBySid(@Param("staff_id") Integer staff_id);

    /**
     * 通过id查询一级菜单列表
     * @param id
     * @return
     */
    HashMap selectOneMenuListById(@Param("id") Integer id);


    /**
     * 菜单
     * @param level 级别
     * @param sort 上级id
     * @return
     */
    List<HashMap> selectOneMenu(@Param("level") Integer level, @Param("sort") Integer sort);

    /**
     * 通过一级菜单id查询第二级菜单列表
     * @param id
     * @return
     */
    List<Menu> selectTwoMenuList(@Param("id") Integer id);

    /**
     * 通过一级菜单id查询第二级菜单
     * @param id
     * @return
     */
    HashMap menu(@Param("id") Integer id);

    /**
     * 通过二级菜单id查询三级菜单列表
     * @param id
     * @return
     */
    List<HashMap> selectThreeMenuList(@Param("id") Integer id);


    /**
     * 修改首页菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateOneMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改销售菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateTwoMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改采购菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateThreeMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改库存菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateFourMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改配送菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateFiveMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改财务菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateSixMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改资料菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateSevenMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改员工菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateEightMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 修改设置菜单权限
     * @param staff_id
     * @param menu
     * @return
     */
    Integer updateNineMenu(@Param("staff_id") Integer staff_id, @Param("menu") String menu);

    /**
     * 添加模板
     * @param template
     * @return
     */
    Integer addTemplate(Template template);

    /**
     * 修改模板
     * @param template
     * @return
     */
    Integer updateTemplate(Template template);

    /**
     * 恢复模板
     * @param template
     * @return
     */
    Integer updateRecoveryTemplate(Template template);

    /**
     * 修改模板二维码显示状态
     * @param id
     * @param state
     * @return
     */
    Integer updateTemplateState(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 删除log
     * @param id
     * @return
     */
    Integer deleteTemplateLog(@Param("id") Integer id);


    /**
     * 根据级别和上级id查询对应的菜单id
     * @param level
     * @param sort
     * @return
     */
    List<Integer> findMenuLevelAndSort(@Param("level") Integer level ,@Param("sort") Integer sort);

    /**
     * 添加一级菜单权限
     * @param staff_id
     * @param menu_id
     * @return
     */
    Integer addOneMenu(@Param("staff_id") Integer staff_id ,@Param("menu_id") Integer menu_id);

    /**
     * 添加二级菜单权限
     * @param staff_id
     * @param menu_id
     * @param sort 上级id
     * @return
     */
    Integer addTwoMenu(@Param("staff_id") Integer staff_id ,@Param("menu_id") Integer menu_id ,@Param("sort") Integer sort);

    /**
     * 添加三级菜单权限
     * @param staff_id
     * @param menu_id
     * @param sort 上级id
     * @return
     */
    Integer addThreeMenu(@Param("staff_id") Integer staff_id ,@Param("menu_id") Integer menu_id ,@Param("sort") Integer sort);


    /**
     * 通过员工id查询一级菜单权限
     * @param staff_id 员工id
     * @return
     */
    List<Menu> findOneMenu(@Param("staff_id") Integer staff_id  , @Param("state") Integer state);

    /**
     * 通过员工id和一级菜单id查询二级菜单权限
     * @param staff_id 员工id
     * @param sort 上级菜单id
     * @return
     */
    List<Menu> findTwoMenu(@Param("staff_id") Integer staff_id , @Param("sort") Integer sort  ,@Param("state") Integer state);

    /**
     * 通过员工id和二级菜单id查询三级菜单权限
     * @param staff_id 员工id
     * @param sort 上级菜单id
     * @return
     */
    List<Menu> findThreeMenu(@Param("staff_id") Integer staff_id , @Param("sort") Integer sort ,@Param("state") Integer state);


    /**
     * 修改员工一级菜单
     * @param id
     * @param state
     * @return
     */
    Integer updateStaffOneMenu(@Param("id") Integer id ,@Param("state") Integer state);

    /**
     * 修改员工二级菜单
     * @param id
     * @param state
     * @return
     */
    Integer updateStaffTwoMenu(@Param("id") Integer id ,@Param("state") Integer state);

    /**
     * 修改员工三级菜单
     * @param id
     * @param state
     * @return
     */
    Integer updateStaffThreeMenu(@Param("id") Integer id ,@Param("state") Integer state);


    /**
     * 查询app菜单表
     * @return
     */
    List<Menu> selectAppMenu();

    /**
     * 添加员工APP功能菜单列表
     * @param staff_id
     * @param menu
     * @return
     */
    Integer addStaffAppMenu(@Param("staff_id") Integer staff_id ,@Param("menu") String menu );

    /**
     * 店铺查询三级菜单
     * @param sort
     * @return
     */
    List<Menu> businessSelectThreeMenu( @Param("sort") Integer sort );

    /**
     * 员工查询三级菜单
     * @param sort
     * @return
     */
    List<Menu> staffSelectThreeMenu(@Param("token") String token , @Param("sort") Integer sort );

    /**
     * 员工APP菜单
     * @param staff_id
     * @return
     */
    List<Menu> selecStaffAppMenu(@Param("staff_id") Integer staff_id);

    /**
     * 修改员工APP菜单
     * @param id
     * @param state
     * @return
     */
    Integer updateStaffAppMenu(@Param("id") Integer id ,@Param("state") Integer state );

    /**
     * 添加飞蛾打印机
     * @param printer
     * @return
     */
    Integer addPrinter(Printer printer);

    /**
     * 查询店铺打印机
     * @param business_id
     * @return
     */
    List<Printer> queryPrinter(@Param("business_id") Integer business_id ,@Param("state") Integer state);

    /**
     * 修改打印机
     * @param business_id
     * @param sn
     * @param name
     * @param phonenum
     * @return
     */
    Integer printerEdit(@Param("business_id") Integer business_id , @Param("sn") String sn, @Param("remark1") String name, @Param("carnum1") String phonenum,@Param("num") Integer num ,@Param("ors") Integer ors);

    /**
     * 删除打印机
     * @param business_id
     * @param sn
     * @return
     */
    Integer deletePrinter(@Param("business_id") Integer business_id ,@Param("sn") String sn);


    /**
     * 启用打印机
     * @param id
     * @param state
     * @return
     */
    Integer enablePrinter(@Param("id") Integer id ,@Param("state") Integer state);

    /**
     * 通过id查询打印机信息
     * @param id
     * @return
     */
    Printer selectPrinterById(@Param("id") Integer id );


    /**
     * 修改店铺使用哪个单据模块
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    Integer updateBusinessPrinterModular(@Param("id") Integer business_id  ,@Param("type") Integer type)throws NullPointerException ,Exception;

    /**
     * 查询店铺使用哪个单据模块
     * @return
     */
    Integer queryBusinessPrinterModular(@Param("id") Integer business_id);

    /**
     * 查询店铺添加打印机id列表
     * @param business_id
     * @return
     */
    List<Integer> queryPrinterId(@Param("business_id") Integer business_id ,@Param("state") Integer state);

    /**
     * 通过销售订单id查询飞蛾打印机需要打印的信息
     * @param order_id
     * @return
     */
    Order queryOrder(@Param("id") Integer order_id);

    /**
     * 通过销售订单id查询订单详情
     * @param order_id
     * @return
     */
    List<MothPrinterClass> queryOrderDateil(@Param("order_id") Integer order_id);

    /**
     * 通过采购订单id查询飞蛾打印机需要打印的信息
     * @param order_id
     * @return
     */
    PurchaseOrder queryPurchaseOrder(@Param("id") Integer order_id);

    /**
     * 通过采购订单id查询采购订单详情
     * @param order_id
     * @return
     */
    List<MothPrinterClass> queryPurchaseOrderDateil(@Param("purchase_id") Integer order_id);


    /**
     * 通过临时订单id查询飞蛾打印机需要打印的信息
     * @param order_id
     * @return
     */
    Order queryTemporaryOrder(@Param("id") Integer order_id);

    /**
     * 通过临时订单id查询订单详情
     * @param order_id
     * @return
     */
    List<MothPrinterClass> queryTemporaryOrderDateil(@Param("order_id") Integer order_id);
}
