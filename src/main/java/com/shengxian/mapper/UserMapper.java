package com.shengxian.mapper;

import com.shengxian.entity.*;
import com.shengxian.vo.UserCategoryVO;
import com.shengxian.vo.UserVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/31
 * @Version: 1.0
 */
@Mapper
public interface UserMapper {


    /**
     * 根据商家id查询用户类别最后一条序号
     * @param bid
     * @return
     */
    String findUserCategroyCodeByBid(@Param("bid") Integer bid);

    /**
     * 添加用户类别
     * * @param bid 商家id
     * @param name 类别名称
     * @param code 序号
     * @return
     */
    Integer addUserCategory(@Param("bid") Integer bid, @Param("name") String name, @Param("code") String code);

    /**
     *查询商家下的所有用户类别集合
     * @param bid 商家id
     * @return
     */
    List<HashMap> findUserCategoryList(@Param("bid") Integer bid);

    /**
     * 根据用户类别id查询类别信息
     * @param id 类别id
     * @return
     */
    HashMap findUserCategoryById(@Param("id") Integer id);

    /**
     * 修改用户类别信息
     * @param id
     * @param name
     * @return
     */
    Integer updateUserCategory(@Param("id") Integer id, @Param("name") String name);

    /**
     * 通过类别id查询类别下的客户信息集合
     * @param categoryId
     * @return
     */
    List<HashMap> findUserListtByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 删除用户类别
     * @param id
     * @return
     */
    Integer deleUserCategory(@Param("id") Integer id);

    /**
     * 通过店铺id查询店铺第一个类别id
     * @param business_id
     * @return
     */
    Integer findOneCategoryId(Integer business_id );

    /**
     * 根据用户类别id查询最后一条用户编号
     * @param categoryId
     * @return
     */
    String findNumberByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 根据用户类别id查询对应的类别序号
     * @param id 类别id
     * @return
     */
    String findCodeById(@Param("id") Integer id);

    /**
     * 添加用户信息
     * @param bindUser
     * @return
     */
    Integer addBinding(BindUser bindUser);

    /**
     * 添加用户积分
     * @param binding_id
     * @return
     */
    Integer addBindingIntegra(@Param("binding_id") Integer binding_id, @Param("create_time") Date create_time);

    /**
     * 搜索产品
     * @param business_id
     * @param name
     * @return
     */
    List<HashMap> findGoods(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 搜索员工
     * @param business_id
     * @param name
     * @return
     */
    List<HashMap> findStaff(@Param("business_id") Integer business_id, @Param("name") String name);


    /**
     * 通过手机号查询用户是否存在
     * @param phone
     * @return
     */
    Integer findUidByPhone(@Param("phone") String phone);

    String getOldPhone(@Param("id") Integer id);

    /**
     * 通过手机号和店铺查询当前号码有被当前店铺绑定了别的用户了吗
     * @param phone
     * @return
     */
    List<Integer> findIdByPhoneAndBid(@Param("id")  Long businessId , @Param("phone") String phone);

    /**
     * 根据类别id和编号查询是否存在
     * @param categoryId
     * @param number
     * @return
     */
    String findUserByCategoryIdAndNumber(@Param("categoryId") Integer categoryId, @Param("number") String number);

    /**
     * 添加用户的产品积分
     * @param binding_id
     * @param goods_id
     * @param proportion
     * @return
     */
    Integer addUserGoodsIntegras(@Param("binding_id") Integer binding_id, @Param("goods_id") Integer goods_id, @Param("proportion") String proportion);

    /**
     * 添加用户的收藏产品
     * @param binding_id
     * @param goods_id
     * @param collectionTime
     * @return
     */
    Integer addGoodsCollection(@Param("binding_id") Integer binding_id, @Param("goods_id") Integer goods_id, @Param("collectionTime") Date collectionTime);

    /**
     *添加用户的屏蔽产品
     * @param binding_id
     * @param goods_id
     * @param shieldingTime
     * @return
     */
    Integer addGoodsShielding(@Param("binding_id") Integer binding_id, @Param("goods_id") Integer goods_id, @Param("shieldingTime") Date shieldingTime);

    /**
     *条件查询类别下的客户信息总数
     * @return
     */
    Integer findCustomerInfoListCount(Parameter parameter);

    /**
     * 条件查询类别下的客户信息集合1
     * @return
     */
    List<HashMap> findCustomerInfoList(Parameter parameter);

    /**
     * 根据客户id查询客户信息
     * @param id 客户id
     * @return
     */
    HashMap findUserInfoById(@Param("id") Integer id);

    /**
     * 通过绑定ID查询绑定用户的积分产品
     * @param bindingId
     * @return
     */
    List<HashMap> selectUserIntegraGoods(@Param("bindingId") Integer bindingId);

    /**
     * 通过绑定ID查询绑定用户的收藏产品
     * @param bindingId
     * @return
     */
    List<HashMap> selectUserCollectionGoods(@Param("bindingId") Integer bindingId);

    /**
     * 通过绑定ID查询绑定用户的屏蔽产品
     * @param bindingId
     * @return
     */
    List<HashMap> selectUserShieldingGoods(@Param("bindingId") Integer bindingId);

    /**
     * 修改绑定用户信息
     * @param bindUser
     * @return
     */
    Integer updateUser(BindUser bindUser);

    /**
     * 查询备留电话是否有重复的
     * @param telephone
     * @return
     */
    List<Integer> selectTelephone(@Param("business_id") Integer business_id  ,@Param("telephone") String telephone);

    /**
     * 通过用户类别ID查询用户编号
     * @param binding
     * @return
     */
    String findUserNumberByBindingId(@Param("binding") Integer binding);

    /**
     * 修改用户产品积分信息
     * @return
     */
    Integer updateUserGoodsIntegras(BindUserGoodsIntegra bindUserGoodsIntegra);

    /**
     * 修改用户收藏产品信息
     * @param bindUserGoodsCollection
     * @return
     */
    Integer updateUserGoodsCollection(BindUserGoodsCollection bindUserGoodsCollection);

    /**
     * 修改用户屏蔽产品信息
     * @param bindUserGoodsShielding
     * @return
     */
    Integer updateUserGoodsShielding(BindUserGoodsShielding bindUserGoodsShielding);

    /**
     * 根据员工的用户提成ID查询绑定用户ID
     * @param spid 员工的用户提成
     * @return
     */
    Integer findBindidBySPId(@Param("spid") Integer spid);

    /**
     *删除用户之前判断该用户是否有欠款和未付款的订单
     * @param id
     * @return
     */
    List<Long> selectBindingOrderStatus(@Param("id") Long id);

    /**
     *删除用户之前判断该用户是否有欠款和未付款的订单
     * @param id
     * @return
     */
    List<Integer> selectBindingOrder(@Param("id") Integer id);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Integer deleteUser(@Param("id") Integer id);

    /**
     * 删除用户产品积分
     * @param integraId
     * @return
     */
    Integer deleteUserGoodsIntegra(@Param("integraId") Integer integraId);

    /**
     * 删除用户收藏产品
     * @param collectionId 收藏ID
     * @return
     */
    Integer deleteUserGoodsCollection(@Param("collectionId") Integer collectionId);

    /**
     * 删除用户屏蔽产品
     * @param shieldingId 屏蔽ID
     * @return
     */
    Integer deleteUserGoodsShielding(@Param("shieldingId") Integer shieldingId);

    /**
     * 导出类别下的客户信息集合
     * @param parameter
     * @return
     */
    List<HashMap> excelDownloadCustomerInfoList(Parameter parameter);

    /**
     * 注册客户APP账号
     * @param phone
     * @param password
     * @return
     */
    Integer addUserPhone(@Param("phone") String phone ,@Param("password") String password , @Param("token") String token ,@Param("time") Date time);



    List<UserCategoryVO> getUserCategoryList(@Param("businessId") Long businessId);

    List<UserVO> getUserList(@Param("businessId") Long businessId);

    /**
     *条件查询类别下专员的客户信息总数
     * @return
     */
    Integer getCommissionerCustomerInfoListCount(Parameter parameter);

    /**
     * 条件查询类别下专员的客户信息
     * @return
     */
    List<HashMap> getCommissionerCustomerInfoList(Parameter parameter);

    String getStaffNameById(@Param("id") Integer id);

}

