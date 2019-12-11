package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.Staff;
import com.shengxian.vo.StaffCategoryVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/24
 * @Version: 1.0
 */
public interface StaffService {



    /**
     * 添加员工类别
     * @param token
     * @param name
     * @return
     */
    Integer addStaffCategory(String token ,Integer role , String name);

    /**
     * 查询服务商下的所有员工类别集合
     * @param token
     * @return
     */
    List<HashMap> findStaffCategoryInfoListBySid(String token  ,Integer role)throws NullPointerException;

    /**
     * 通过员工类别id查询员工类别信息
     * @param categoryId 员工类别id
     * @return
     */
    HashMap findStaffCategoryInfoById(Integer categoryId);

    /**
     *  修改员工类别信息
     * @param id 员工类别id
     * @param name 类别名称
     * @return
     */
    Integer updateStaffCategoryByid(Integer id, String name);

    /**
     * 删除员工类别
     * @param id 类别id
     * @return
     */
    Integer deleStaffCategory(Integer id)throws NullPointerException ,Exception;

    /**
     * 挑选类别自动生成员工编号
     * @param id
     * @return
     */
    String automaticSelectStaffnumberByCategoryId(Integer id);

    /**
     * 通过用户类别ID和用户编号或名称查询商家绑定用户的信息
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    List<HashMap> findBindingInfoByUser(Integer categoryId, String number, String name);

    /**
     * 通过产品类别ID和编号或产品名称查询产品信息
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    List<HashMap> findGoodsInfoByCid(Integer categoryId, String number, String name);

    /**
     * 搜索店铺绑定用户
     * @param token
     * @return
     */
    List<HashMap> findBusinessUser(String token ,Integer role , String name);

    /**
     * 添加员工信息
     * @param token
     * @param staff
     * @return
     */
    Integer addSstaff(String token ,Integer role , Staff staff) throws NullPointerException, UnsupportedEncodingException, NoSuchAlgorithmException;



    /**
     * 查询类别下的员工信息集合
     * @param id
     * @return
     */
    Page findStaffInfoList(String token ,Integer role, Integer pageNo, Integer id, String name)throws NullPointerException ;

    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    HashMap findStaffInfoById(Integer id)throws NullPointerException;

    /**
     * 修改员工信息
     * @param staff
     * @return
     */
    Integer updateStaff(Staff staff)throws NullPointerException ,Exception;

    /**
     * 删除员工信息
     * @param id
     * @return
     */
    Integer deleSatff(Integer id);

    /**
     * 匹配员工密码
     * @param staff_id
     * @param password
     * @return
     */
    boolean findStaffPWD(Integer staff_id, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 修改员工密码
     * @param staff_id
     * @param password
     * @return
     */
    Integer updateStaffPwd(Integer staff_id, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 删除员工的产品提成信息
     * @param id 员工的产品提成ID
     * @return
     */
    Integer deleteStaffGoodsPercent(Integer id);

    /**
     * 删除员工的用户提成信息
     * @param id 员工的用户提成ID
     * @return
     */
    Integer deleteStaffUserPercent(Integer id);

    /**
     * 删除员工的用仓库提成信息
     * @param id 员工的用仓库提成id
     * @return
     */
    Integer deleteStaffWarehousePercent(Integer id);

    /**
     * 删除员工的其它销售提成信息
     * @param id
     * @return
     */
    Integer deleteStaffOtherPercent(Integer id);

    /**
     * 查询类别下所有员工信息集合
     * @param id
     * @return
     */
    List<HashMap> findStaffInfoListByCategoryId(Integer id);

    /**
     * 导出用户数据
     * @param token
     * @param
     * @return
     */
    HSSFWorkbook excelDownload(String token ,Integer role, Integer id, String phone, String number, String name);


    List<StaffCategoryVO> getStaffList(String token , Integer role);

}

