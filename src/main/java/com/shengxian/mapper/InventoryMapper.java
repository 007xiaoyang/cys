package com.shengxian.mapper;

import com.shengxian.entity.Give;
import com.shengxian.entity.Parameter;
import com.shengxian.entity.Settlement;
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
 * @Date: 2018-10-14
 * @Version: 1.0
 */
@Mapper
public interface InventoryMapper {

    /**
     * 添加仓库信息
     * @param business_id
     * @param name
     * @return
     */
    Integer addInventory(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 查询商家仓库信息
     * @param busiessId
     * @return
     */
    List<HashMap> findInventoryList(@Param("busiessId") Integer busiessId);

    /**
     * 根据仓库ID查询仓库信息
     * @param warehouseId 仓库ID
     * @return
     */
    HashMap findInventoryByWid(@Param("warehouseId") Integer warehouseId);

    /**
     * 修改仓库信息
     * @param warehouseId
     * @param name
     * @return
     */
    Integer updateInventory(@Param("warehouseId") Integer warehouseId, @Param("name") String name);

    /**
     * 删除仓库
     * @param id
     * @return
     */
    Integer delectInventory(@Param("id") Integer id);

    /**
     *通过仓库ID查询库存表下是否存在产品
     * @param warehouse_id
     * @return
     */
    List<HashMap> findGoodsByWid(@Param("warehouse_id") Integer warehouse_id);

    /**
     * 盘点
     * @param business_id
     * @param status
     * @return
     */
    Integer check(@Param("business_id") Integer business_id, @Param("status") Integer status);

    /**
     * 查询商家是否盘点中
     * @param business_id
     * @return
     */
    Integer checkGoodsInventory(@Param("business_id") Integer business_id);

    /**
     * 查询盘点前的产品信息总数
     * @return
     */
    Integer findBusinessGoodsInventoryCount(Parameter parameter);

    /**
     * 查询盘点前的产品信息
     * @param parameter
     * @return
     */
    List<HashMap> findBusinessGoodsInventory(Parameter parameter);

    /**
     *查询过期的未盘点的产品
     * @param goods_id
     * @return
     */
    Integer notOverdueInventory(@Param("id") Integer goods_id);

    /**
     * 修改产品盘点状态
     * @param goods_id
     * @return
     */
    Integer updateGoodsState(@Param("id") Integer goods_id, @Param("state") Integer state, @Param("time") Date time);

    /**
     * 查询盘点过后的产品信息总数
     * @param parameter
     * @return
     */
    Integer findSettlementInventoryCount(Parameter parameter);
    /**
     * 查询盘点过后的产品信息
     * @param parameter
     * @return
     */
    List<HashMap> findSettlementInventory(Parameter parameter);

    /**
     * 统计结算产品的总金额
     * @param parameter
     * @return
     */
    double findSettlementInventoryTotalSum(Parameter parameter);

    /**
     * 查询存在结算的产品
     * @param bid
     * @return
     */
    List<Integer> selectExistenceSettlementGoods(@Param("business_id") Integer bid);


    /**
     * 通过库存id查询产品是否已经修改盘点数量了
     * @param id
     * @return
     */
    Double selectIsUpdateCheckNum(@Param("id") Integer id);

    /**
     * 修改盘点库存
     * @param id
     * @param update_inventory 盘点的库存
     * @param status 状态
     * @return
     */
    Integer updateCheckGoodsInventory(@Param("id") Integer id, @Param("update_inventory") Double update_inventory, @Param("status") Integer status, @Param("opid") Integer opid);

    /**
     * 添加结算记录
     * @param settlement
     * @return
     */
    Integer addSettlement(Settlement settlement);

    /**
     * 查询盘点的库存
     * @param id
     * @return
     */
    HashMap selectInventory(@Param("id") Integer id);

    /**
     * 添加结算产品详情
     * @param bid 商家id
     * @param goods_id 产品id
     * @param actual 盘点前的实际库存数量
     * @param num 盘点的库存数量
     * @param create_time 创建时间
     * @return
     */
    Integer addSettlementGoodsDetail(@Param("settlement_id") Integer bid, @Param("goods_id") Integer goods_id, @Param("actual") Double actual, @Param("num") Double num, @Param("create_time") Date create_time, @Param("opid") Integer opid);

    /**
     * 结算
     * @param id 库存id
     * @param status 状态
     * @param actual 实际库存数量
     * @param update_inventory 盘点的库存数量
     * @param opid 结算人清空
     * @return
     */
    Integer updateRetreatCheckGoodsInventory(@Param("id") Integer id, @Param("status") Integer status, @Param("actual") Double actual, @Param("update_inventory") Double update_inventory, @Param("opid") Integer opid);

    /**
     * 结算记录总数
     * @param bid
     * @param startTime
     * @param endTime
     * @return
     */
    Integer goodsSettlementCount(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 结算记录信息
     * @param bid
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> goodsSettlement(@Param("business_id") Integer bid, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 结算产品详情总数
     * @param settlement_id
     * @param name
     * @param number
     * @param cid
     * @param wid
     * @return
     */
    Integer settlementGoodsRecordCount(@Param("settlement_id") Integer settlement_id, @Param("name") String name, @Param("number") String number, @Param("category_id") Integer cid, @Param("warehouse_id") Integer wid, @Param("opid") Integer opid);

    /**
     * 结算产品详情
     * @param
     * @return
     */
    List<HashMap> settlementGoodsRecord(@Param("settlement_id") Integer settlement_id, @Param("name") String name, @Param("number") String number, @Param("category_id") Integer cid, @Param("warehouse_id") Integer wid, @Param("opid") Integer opid, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 结算后的产品记录总金额
     * @param settlement_id
     * @param name
     * @param number
     * @param cid
     * @param wid
     * @return
     */
    double settlementGoodsRecordTotalSum(@Param("settlement_id") Integer settlement_id, @Param("name") String name, @Param("number") String number, @Param("category_id") Integer cid, @Param("warehouse_id") Integer wid, @Param("opid") Integer opid);


    /**
     * 仓库设置（每个仓库所有产品的详情信息）总数
     * @param business_id 店铺id
     * @param wid 仓库id
     * @return
     */
    Integer businessGoodsInventoryInfoCount(@Param("business_id") Integer business_id ,@Param("wid") Integer wid);

    /**
     * 仓库设置（每个仓库的所有产品详情信息）
     * @param business_id 店铺id
     * @param wid 仓库id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> businessGoodsInventoryInfo(@Param("business_id") Integer business_id , @Param("wid") Integer wid , @Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize );

    /**
     * 每个仓库所有产品的总金额
     * @param business_id
     * @param wid
     * @return
     */
    Double businessGoodsInventoryTotalMoney(@Param("business_id") Integer business_id ,@Param("wid") Integer wid);

    /**
     * 条件搜索商家的供应商
     * @param business_id
     * @param name
     * @return
     */
    List<HashMap> findBusinessSuppliers(@Param("business_id") Integer business_id, @Param("name") String name);

    /**
     * 添加赠送产品
     * @param give
     * @return
     */
    Integer addGiveGoods(Give give);

    /**
     * 通过产品id查询产品名称
     * @param goods_id 产品id
     * @return
     */
    String findGoodsName(@Param("id") Integer goods_id);

    /**
     * 添加报损产品
     * @param give
     * @return
     */
    Integer addLossGoods(Give give);

    /**
     * 查询赠送产品单总数
     * @param bid
     * @param
     * @param wid
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    Integer findGiveGoodsCount(@Param("business_id") Integer bid, @Param("warehouse_id") Integer wid, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     * 查询赠送产品单
     * @param
     * @return
     */
    List<HashMap> findGiveGoods(@Param("business_id") Integer bid, @Param("warehouse_id") Integer wid, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询报损产品单总数
     * @param
     * @return
     */
    Integer findLossGoodsCount(@Param("business_id") Integer bid, @Param("warehouse_id") Integer wid, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询报损产品单
     * @param
     * @return
     */
    List<HashMap> findLossGoods(@Param("business_id") Integer bid, @Param("warehouse_id") Integer wid, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 导出仓库产品详情
     * @param business_id 店铺id
     * @param id 仓库id
     * @return
     */
    List<HashMap> excelDownload(@Param("business_id")Integer business_id ,@Param("id") Integer id);

    /**
     * 仓库产品总金额
     * @param parameterd  仓库id
     * @return
     */
    double WGTatolMoney(Parameter parameterd);

    /**
     * 赠送产品导出
     * @param bid
     * @param warehouse_id
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    List<HashMap> giveGoodslDownload(@Param("business_id") Integer bid, @Param("warehouse_id") Integer warehouse_id, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 报损产品导出
     * @param bid
     * @param warehouse_id
     * @param name
     * @param startTime
     * @param endTime
     * @return
     */
    List<HashMap> lossGoodslDownload(@Param("business_id") Integer bid, @Param("warehouse_id") Integer warehouse_id, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 库存归零
     * @param business_id
     * @return
     */
    Integer inventoryZeroing(@Param("business_id") Integer business_id);

}
