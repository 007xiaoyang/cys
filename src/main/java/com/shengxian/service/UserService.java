package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.BindUser;
import com.shengxian.entity.Parameter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 客户服务层
 *
 * @Author: yang
 * @Date: 2018/8/31
 * @Version: 1.0
 */
public interface UserService {

    /**
     * 添加客户类别
     * @param token
     * @param name
     * @return
     */
    Integer addUserCategory(String token ,Integer role, String name)throws Exception;

    /**
     * 查询商家下的所有用户类别集合
     * @param token
     * @return
     */
    List<HashMap> findUserCategoryList(String token ,Integer role )throws NullPointerException;

    /**
     *根据客户类别id查询类别信息
     * @param id
     * @return
     */
    HashMap findUserCategoryById(Integer id);

    /**
     * 修改用户类别信息
     * @param id
     * @param name
     * @return
     */
    Integer updateUserCategory(Integer id, String name);

    /**
     * 删除用户类别
     * @param id 类别id
     * @return
     */
    Integer deleUserCategory(Integer id)throws NullPointerException;

    /**
     * 挑选类别自动生成客户编号
     * @param id 类别id
     * @return
     */
    String selectNumberByCategoryId(Integer id);

    /**
     * 添加客户信息
     * @param token
     * @param bindUser
     * @return
     */
    Integer addUser(String token ,Integer role , BindUser bindUser)throws NullPointerException,Exception;


    /**
     * 搜索产品
     * @param token
     * @param name
     * @return
     */
    List<HashMap> findGoods(String token ,Integer role, String name)throws NullPointerException;

    /**
     * 搜索员工
     * @param token
     * @param name
     * @return
     */
    List<HashMap> findStaff(String token ,Integer role , String name)throws NullPointerException;

    /**
     * 条件查询类别下的客户信息集合
     * @return
     */
    Page findCustomerInfoList(String token  ,Integer role , Parameter parameter)throws NullPointerException;

    /**
     * 根据用户id查询客户信息
     * @param bandingId 绑定用户id
     * @return
     */
    HashMap findCostomerInfoById(Integer bandingId)throws NullPointerException;

    /**
     * 修改用户信息
     * @param bindUser
     * @return
     */
    Integer updateUser(String token ,Integer role ,BindUser bindUser)throws NullPointerException ,Exception;

    /**
     * 删除用户
     * @param id
     * @return
     */
    Integer deleteUser(Integer id)throws NullPointerException;

    /**
     * 删除用户产品积分
     * @param integraId 积分ID
     * @return
     */
    Integer deleteUserGoodsIntegra(Integer integraId);

    /**
     * 删除用户收藏产品
     * @param collectionId 收藏分ID
     * @return
     */
    Integer deleteUserGoodsCollection(Integer collectionId);

    /**
     * 删除用户屏蔽产品
     * @param shieldingId 屏蔽ID
     * @return
     */
    Integer deleteUserGoodsShielding(Integer shieldingId);

    /**
     * 导出用户数据
     * @param token
     * @param parameter
     * @return
     */
    HSSFWorkbook excelDownload(String token ,Integer role, Parameter parameter);

    /**
     * 查询类别下的用户信息集合
     * @param id
     * @return
     */
    List<HashMap> findUserListtByCategoryId(Integer id);

    /**
     * 注册客户APP账号
     * @param phone
     * @return
     */
    Integer addUserPhone(String phone)throws NullPointerException ,Exception;
}
