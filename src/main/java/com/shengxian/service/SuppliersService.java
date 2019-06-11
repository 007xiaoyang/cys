package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.Parameter;
import com.shengxian.entity.Suppliers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/22
 * @Version: 1.0
 */
public interface SuppliersService {

    /**
     * 添加供应商类别信息
     * @param name
     * @return
     */
    Integer addSupplierCategory(String token ,Integer role , String name);

    /**
     * 查询供应商类别信息集合
     * @param token
     * @return
     */
    List<HashMap> findSuppliersCategoryInfoList(String token ,Integer role )throws NullPointerException ;

    /**
     * 修改供应商类别信息
     * @param id 类别id
     * @param name
     * @return
     */
    Integer updateSuppliersCategory(Integer id, String name);

    /**
     * 删除供应商类别
     * @param id
     * @return
     */
    Integer deleSuppliersCategory(Integer id)throws NullPointerException;

    /**
     *根据挑选类别自动生成编号
     * @param id
     * @return
     */
    String selectNumberByCategoryId(Integer id)throws Exception;

    /**
     * 添加供应商信息
     * @param suppliers
     * @return
     */
    Integer addSuppliersInfo(String token ,Integer role, Suppliers suppliers)throws NullPointerException;

    /**
     * 查询类别下的供应商信息集合
     * @param id 类别id
     * @return
     */
    Page findSuppliersInfoList(String token  ,Integer role, Integer pageNo, Integer id, String number, String name)throws NullPointerException;

    /**
     * 查询供应商信息
     * @param id 供应商id
     * @return
     */
    HashMap findSuppliersInfoById(Integer id);
    /**
     * 修改供应商信息
     * @param suppliers
     * @return
     */
    Integer updateSuppliersInfoById(Suppliers suppliers)throws NullPointerException ,Exception;

    /**
     * 删除供应商
     * @param id
     * @return
     */
    Integer deleSuppliers(Integer id)throws NullPointerException ,Exception;

    /**
     * 供应商导出
     * @param token
     * @param parameter
     * @return
     */
    HSSFWorkbook excelDownload(String token ,Integer role , Parameter parameter)throws NullPointerException;

    /**
     * 通过类别id查询类别下的供应商集合
     * @param id
     * @return
     */
    List<HashMap> findSuppliersList(Integer id);

}
