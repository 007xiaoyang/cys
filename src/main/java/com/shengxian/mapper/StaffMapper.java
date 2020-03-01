package com.shengxian.mapper;

import com.shengxian.entity.*;
import com.shengxian.vo.StaffCategoryVO;
import com.shengxian.vo.StaffVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/24
 * @Version: 1.0
 */
@Mapper
public interface StaffMapper {



    /**
     * 通过商家id获取商家所有的员工
     * @param bid
     * @return
     */
    List<Integer> staffList(@Param("buseinss_id") Integer bid);

    /**
     * 根据type统计对应的员工提成总数
     * @param staff_id
     * @param type （3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成）
     * @return
     */
    Double staffFrequency(@Param("staff_id") Integer staff_id, @Param("type") Integer type);


    /**
     *统计员工一个月之内是否有开单次数提成记录总数
     * @param staff_id
     * @return
     */
    Integer staffOpenFrequency(@Param("staff_id") Integer staff_id);

    /**
     * 查询员工一个月之内是否有车次提成记录总数
     * @param staff_id
     * @return
     */
    Integer staffOnceFrequency(@Param("staff_id") Integer staff_id);

    /**
     * 查询员工一个月之内是否有到货提成记录总数
     * @param staff_id
     * @return
     */
    Integer staffArriveFrequency(@Param("staff_id") Integer staff_id);


    /**
     * 添加员工定时每月月底统计（提成
     * @param staff_id
     * @param   //3重量,5送达，6店总销售，7开单,8采购数量提成，9采购重量提成，10月总采购提成
     * @return
     */
    Integer addStaffTimingStatis(@Param("staff_id") Integer staff_id, @Param("statis") Double statis, @Param("type") Integer type);


    /**
     * 根据服务商id查询员工类别的总数
     * @param business_id 服务商id
     * @return
     */
    Integer findStaffCategoryCountByBid(@Param("business_id") Integer business_id);

    /**
     *根据服务商id查询员工类别最后一条序号
     * @param business_id
     * @return
     */
    String findStaffCategroyCodeByBid(@Param("business_id") Integer business_id);

    /**
     * 添加员工类别
     * @param business_id 商家id
     * @param name 类别名称
     * @param code 类别序号
     * @return
     */
    Integer addStaffCategory(@Param("business_id") Integer business_id, @Param("name") String name, @Param("code") String code);

    /**
     *  查询服务商下的所有员工类别集合
     * @param business_id 商家id
     * @return
     */
    List<HashMap> findStaffCategoryInfoListByBid(@Param("business_id") Integer business_id);

    /**
     * 通过员工类别id查询员工类别信息
     * @param id 员工类别id
     * @return
     */
    HashMap findStaffCategoryInfoById(@Param("id") Integer id);
    /**
     * 修改员工类别信息
     * @param id
     * @return
     */
    Integer updateStaffCategoryByid(@Param("id") Integer id, @Param("name") String name);

    /**
     * 查询类别下所有员工信息总数
     * @param categoryId
     * @param name
     * @return
     */
    Integer  findStaffInfoListCount(@Param("business_id") Integer business_id, @Param("categoryId") Integer categoryId, @Param("name") String name);

    /**
     * 条件查询类别下所有员工信息集合
     * @param startIndex
     * @param pageSize
     * @param categoryId
     * @param name
     * @return
     */
    List<HashMap> findStaffInfoListById(@Param("business_id") Integer business_id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("categoryId") Integer categoryId, @Param("name") String name);

    /**
     * 查询类别下所有员工信息集合
     * @param categoryId
     * @return
     */
    List<HashMap> findStaffInfoListByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 删除员工类别
     * @param id 员工类别id
     * @return
     */
    Integer deleStaffCategory(@Param("id") Integer id);

    /**
     * 通过员工类别id查询最后一条员工编号
     * @param categoryId 员工类别id
     * @return
     */
    String findNumberByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 根据类别id查询员工类别序号
     * @param id
     * @return
     */
    String findCodeById(@Param("id") Integer id);

    /**
     * 判断手机号是否注册过员工
     * @param phone
     * @return
     */
    Integer findIdByPhone(@Param("phone") String phone);

    /**
     * 判断手机号码已经注册过店铺了
     * @param phone
     * @return
     */
    Integer findShopByPhone(@Param("phone") String phone );

    /**
     * 通过用户类别ID和用户编号或名称查询商家绑定用户的信息
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    List<HashMap> findBindingInfoByUser(@Param("categoryId") Integer categoryId, @Param("number") String number, @Param("name") String name);

    /**
     * 通过产品类别ID和产品编号或产品名称
     * @param categoryId
     * @param number
     * @param name
     * @return
     */
    List<HashMap> findGoodsInfoByCid(@Param("categoryId") Integer categoryId, @Param("number") String number, @Param("name") String name);

    /**
     * 搜索店铺绑定用户
     * @param business_id
     * @param name
     * @return
     */
    List<HashMap> findBusinessUser(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 添加员工信息
     * @param staff
     * @return
     */
    Integer addStaff(Staff staff);

    /**
     * 添加员工产品提成
     * @param gid 产品id
     * @param proportion 比例
     * @param sid 员工id
     * @return
     */
    Integer addStaffGoodsPercentage(@Param("gid") Integer gid, @Param("proportion") String proportion, @Param("sid") Integer sid);

    /**
     * 添加员工用户提成
     * @param bid 绑定id
     * @param proportion 提成比例
     * @param sid 员工id
     * @return
     */
    Integer addStaffUserPercentage(@Param("bid") Integer bid, @Param("proportion") String proportion, @Param("sid") Integer sid);

    /**
     * 添加员工仓库提成
     * @param wid 仓库id
     * @param proportion 提成比例
     * @param sid 员工id
     * @return
     */
    Integer addStaffWarehousePercentage(@Param("wid") Integer wid, @Param("proportion") String proportion, @Param("sid") Integer sid);

    /**
     * 员工提成
     * @param proportion 比例
     * @param sid 员工id
     * @param type 4销售吨位，5销售车次，6销售到货，7销售，8销售开单，9采购产品数量，10采购吨位，11采购车次，12采购金额
     * @return
     */
    Integer addStaffPercentage(@Param("proportion") String proportion, @Param("sid") Integer sid, @Param("type") Integer type);


    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    HashMap findStaffInfoById(@Param("id") Integer id);

    /**
     * 根据员工id查询产品提成（销售）
     * @param sid
     * @return
     */
    List<HashMap> findStaffGoodsPercentage(@Param("sid") Integer sid);

    /**
     * 根据员工id查询用户提成（销售）
     * @param sid
     * @return
     */
    List<HashMap> findStaffUserPercentage(@Param("sid") Integer sid);

    /**
     * 根据员工id查询仓库提成（销售）
     * @param sid
     * @return
     */
    List<HashMap> findStaffWarehousePercentage(@Param("sid") Integer sid);

    /**
     * 根据员工id查询员工提成
     * @param sid 员工id
     * @return
     */
    List<HashMap> findStaffSalePercentage(@Param("sid") Integer sid);

    /**
     * 修改员工信息
     * @param staff
     * @return
     */
    Integer updateStaff(Staff staff);

    /**
     *根据员工类别id和员工编号查询编号是否存在
     * @param categoryId
     * @param number
     * @return
     */
    String findNumberByCategoryIdAndNumber(@Param("categoryId") Integer categoryId, @Param("number") String number);

    /**
     * 修改员工的产品提成
     * @param goodsPercent
     * @return
     */
    Integer updateGoodsPercent(GoodsPercent goodsPercent);
    Integer updateStaffGoodsPercent(GoodsPercent goodsPercent);

    /**
     * 修改员工的用户提成
     * @param userPercent
     * @return
     */
    Integer updateUserPercent(UserPercent userPercent);
    Integer updateStaffUserPercent(UserPercent userPercent);

    /**
     * 修改员工的仓库提成
     * @param warehousePercent
     * @return
     */
    Integer updateStaffWarehousePercent(WarehousePercent warehousePercent);

    /**
     *修改员工的其它销售提成
     * @param otherPercentage
     * @return
     */
    Integer updateStaffOtherPercent(OtherPercentage otherPercentage);

    /**
     * 删除员工信息
     * @param id 员工id
     * @return
     */
    Integer deleSatff(@Param("id") Integer id);

    /**
     * 查询员工密码
     * @param id
     * @return
     */
    String findStaffPassword(@Param("id") Integer id);
    /**
     * 修改员工密码
     * @param id 员工ID
     * @param password 员工密码
     * @return
     */
    Integer updateStaffPwd(@Param("id") Integer id, @Param("password") String password);

    /**
     * 删除员工的产品提成信息
     * @param id
     * @return
     */
    Integer deleteStaffGoodsPercent(@Param("id") Integer id);

    /**
     * 删除员工的用户提成信息
     * @param id 员工的用户提成ID
     * @return
     */
    Integer deleteStaffUserPercent(@Param("id") Integer id);

    /**
     * 删除员工的仓库提成信息
     * @param id 员工的用户提成ID
     * @return
     */
    Integer deleteStaffWarehousePercent(@Param("id") Integer id);

    /**
     * 删除员工的其它销售提成信息
     * @param id 员工的用户提成ID
     * @return
     */
    Integer deleteStaffOtherPercent(@Param("id") Integer id);


    /**
     * 导出类别下所有员工信息总数
     * @param categoryId
     * @param phone
     * @param number
     * @param name
     * @return
     */
    List<HashMap>  excelDownloadStaffInfoList(@Param("business_id") Integer business_id, @Param("categoryId") Integer categoryId, @Param("phone") String phone, @Param("number") String number, @Param("name") String name);


    /**
     * 通过店铺id查询店铺类别集合
     * @param businessId
     * @return
     */
    List<StaffCategoryVO> getStaffCategoryInfo(@Param("businessId") Long businessId);

    /**
     * 获取店铺下的员工
     * @param businessId
     * @return
     */
    List<StaffVO> getStaffInfo(@Param("businessId") Long businessId );

}
