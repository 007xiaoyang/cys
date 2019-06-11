package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.Give;
import com.shengxian.entity.Parameter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-10-14
 * @Version: 1.0
 */
public interface InventoryService {

    /**
     * 添加仓库信息
     * @param token
     * @param name
     * @return
     */
    Integer addInventory(String token ,Integer role , String name);

    /**
     * 查询商家仓库信息
     * @param token
     * @return
     */
    List<HashMap> findInventoryList(String token ,Integer role )throws NullPointerException;

    /**
     * 根据仓库ID查询仓库信息
     * @param warehouseId
     * @return
     */
    HashMap findInventoryByWid(Integer warehouseId);

    /**
     * 修改仓库信息
     * @param warehouseId
     * @param name
     * @return
     */
    Integer updateInventory(Integer warehouseId, String name);

    /**
     * 删除仓库ID
     * @param warehouseId 仓库ID
     * @return
     */
    Integer delectInventory(Integer warehouseId)throws NullPointerException ,Exception;

    /**
     * 盘点
     * @param token
     * @param status
     * @return
     */
    Integer check(String token ,Integer role , Integer status);

    /**
     * 查询商家是否盘点中
     * @return
     */
    Integer checkGoodsInventory(String token ,Integer role);

    /**
     * 查询盘点前的产品信息
     * @param token
     * @return
     */
    Page findBusinessGoodsInventory(String token ,Integer role , Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status ,Integer goods_id)throws NullPointerException ,Exception;

    /**
     *查询盘点过后的产品信息
     * @param token
     * @return
     */
    Page findSettlementInventory(String token ,Integer role, Integer pageNo , String name , String number , Integer category_id , Integer warehouse_id ,Integer status ,Integer goods_id)throws NullPointerException;

    /**
     * 修改盘点库存
     * @param inventory_id
     * @param actual
     * @param update_inventory
     * @return
     */
    Integer updateCheckGoodsInventory(String token, Integer inventory_id, Double actual, Double update_inventory)throws NullPointerException ,Exception;

    /**
     * 盘点回退
     * @param inventory_id
     * @return
     */
    Integer retreatCheckGoodsInventory(Integer inventory_id)throws NullPointerException;

    /**
     * 结算
     * @param inventory_id
     * @return
     */
    Integer addSettlementInventory(String token ,Integer role, String inventory_id)throws NullPointerException, Exception;

    /**
     *结算记录
     * @param token
     * @return
     */
    Page goodsSettlement(String token ,Integer role, Integer pageNo, String startTime, String endTime)throws NullPointerException;

    /**
     * 结算产品详情
     * @param settlement_id 结算id
     * @param name
     * @param number
     * @param category_id 类别id
     * @param warehouse_id 仓库id
     * @return
     */
    Page settlementGoodsRecord(String token, Integer pageNo, Integer settlement_id, String name, String number, Integer category_id, Integer warehouse_id, Integer opid);


    /**
     * 库存报表（每个仓库的详情信息）
     * @param token
     * @return
     */
    Page businessGoodsInventoryInfo(String token ,Integer role, Integer pageNo, Integer warehouse_id)throws NullPointerException ,Exception;


    /**
     * 条件搜索商家供应商
     * @param token
     * @param name
     * @return
     */
    List<HashMap> findBusinessSuppliers(String token ,Integer role , String name)throws NullPointerException ;

    /**
     * 添加赠送产品
     * @param give
     * @return
     */
    Integer addGiveGoods(String token  ,Integer role , Give[] give)throws Exception;

    /**
     * 添加报损产品
     * @param give
     * @return
     */
    Integer addLossGoods(String token  ,Integer role , Give[] give)throws NullPointerException, Exception;

    /**
     * 分页查询赠送产品单
     * @param token
     * @param
     * @return
     */
    Page findGiveGoods(String token  ,Integer role, Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime)throws NullPointerException;

    /**
     * 分页查询报损产品单
     * @param token
     * @param
     * @return
     */
    Page findLossGoods(String token  ,Integer role, Integer pageNo, Integer warehouse_id, String name, String startTime, String endTime)throws NullPointerException;

    /**
     * 导出仓库产品详情
     * @param id
     * @return
     */
    HSSFWorkbook excelDownload(String token ,Integer role ,Integer id);

    /**
     * 赠送产品导出
     * @param token
     * @param warehouse_id
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    HSSFWorkbook giveGoodslDownload(String token, Integer warehouse_id, String name, String startTime, String endTime);

    /**
     * 报损产品导出
     * @param token
     * @param warehouse_id
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    HSSFWorkbook lossGoodslDownload(String token, Integer warehouse_id, String name, String startTime, String endTime);

    /**
     * 库存归零
     * @param token
     * @param password
     * @return
     */
    Integer inventoryZeroing(String token  ,Integer role, String password)throws NullPointerException, Exception;
}
