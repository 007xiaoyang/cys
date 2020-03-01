package com.shengxian.mapper;

import com.shengxian.entity.Parameter;
import com.shengxian.entity.Suppliers;
import com.shengxian.vo.SuppliersCategoryVO;
import com.shengxian.vo.SuppliersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 供应商
 *
 * @Author: yang
 * @Date: 2018/8/22
 * @Version: 1.0
 */
@Mapper
public interface SuppliersMapper {

    /**
     * 根据服务商id查询添加供应商类别的最后一条序号
     * @param bid
     * @return
     */
    String findCodeBySid(@Param("bid") Integer bid);

    /**
     * 根据类别id查询序号
     * @param id
     * @return
     */
    String findCodeByid(@Param("id") Integer id);

    /**
     * 添加供应商类别信息
     * @param name
     * @return
     */
    Integer addSupplierCategory(@Param("bid") Integer bid, @Param("name") String name, @Param("code") String code);

    /**
     * 通过商家ID查询供应商类别总数
     * @param bid 商家ID
     * @return
     */
    Integer findSupplierCategoryCount(@Param("bid") Integer bid);

    /**
     * 查询服务商下的供应商类别信息集合
     * @param bid
     * @return
     */
    List<HashMap> findSuppliersCategoryInfoListBySid(@Param("bid") Integer bid);

    /**
     * 修改供应商类别信息
     * @param id 供应商类别ID
     * @param name
     * @return
     */
    Integer updateSuppliersCategory(@Param("id") Integer id, @Param("name") String name);

    /**
     * 条件查询类别下的供应商总数
     * @return
     */
    Integer findSuppliersInfoListCount(Parameter parameter);

    /**
     * 条件查询类别下的供应商集合
     * @return
     */
    List<HashMap> findSuppliersInfoList(Parameter parameter);

    /**
     * 查询类别下的供应商集合
     * @param categoryId
     * @return
     */
    List<HashMap> findSuppliersList(@Param("categoryId") Integer categoryId);
    /**
     * 删除供应商类别
     * @param id
     * @return
     */
    Integer deleSuppliersCategory(@Param("id") Integer id);

    /**
     * 添加供应商信息
     * @param suppliers
     * @return
     */
    Integer addSuppliersInfo(Suppliers suppliers);

    /**
     * 通过类别id查询最后一条供应商编号
     * @param categoryId
     * @return
     */
    String findNumberByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 通过id查询供应商信息
     * @param id 供应商id
     * @return
     */
    HashMap findSuppliersInfoById(@Param("id") Integer id);

    /**
     * 根据类别id和编号查询是否存在
     * @param categoryId
     * @param number
     * @return
     */
    Integer findIdByCidAndNumber(@Param("categoryId") Integer categoryId, @Param("number") String number);

    /**
     * 修改供应商信息
     * @param suppliers
     * @return
     */
    Integer updateSuppliersInfo(Suppliers suppliers);


    /**
     * 删除供应商前判断是否有未付款或欠款的订单
     * @param id
     * @return
     */
    List<Integer> dayPurchaseOrder(@Param("id") Integer id);

    /**
     * 删除供应商前判断是否有未付款或欠款的订单
     * @param id
     * @return
     */
    List<Integer> selectPurchaseOrder(@Param("id") Integer id);

    /**
     * 删除供应商
     * @param id
     * @return
     */
    Integer deleSuppliers(@Param("id") Integer id);

    /**
     * 供应商导出
     * @return
     */
    List<HashMap> excelDownload(Parameter parameter);


    List<SuppliersCategoryVO> getSuppliersCategoryList(@Param("businessId") Long businessId);

    List<SuppliersVO> getSuppliersList(@Param("businessId") Long businessId);
}
