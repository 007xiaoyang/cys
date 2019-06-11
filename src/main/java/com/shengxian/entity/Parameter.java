package com.shengxian.entity;

import io.swagger.models.auth.In;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description: 条件查询参数类
 *
 * @Author: yang
 * @Date: 2018-10-27
 * @Version: 1.0
 */
public class Parameter {

    private String phone;
    private Integer business_id; //商家id
    private Integer startIndex; //起始索引
    private Integer pageSize; //每页的页数
    private Integer category_id; //类别id
    private String number; // 编号
    private String name; // 名称
    private Long barcode; //条码
    private Integer pageNo;
    private int pageNum;
    private Integer warehouse_id;//仓库id
    private Integer status;// 默认空，0查询和修改，1异常，2正常
    private Integer staff_id; //业务员id
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime; //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime; //结束时间

    private Integer goods_id;
    private Integer level;

    private Integer mold ;

    public Parameter() {
    }



    public Parameter(Integer business_id, Integer category_id, String number, String name, Integer warehouse_id, Integer status , Integer goods_id) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.warehouse_id = warehouse_id;
        this.status = status;
        this.goods_id = goods_id;

    }

    public Parameter(Integer business_id, Integer startIndex, Integer pageSize, Integer category_id, String number, String name, Integer warehouse_id, Integer status , Integer goods_id) {
        this.business_id = business_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.warehouse_id = warehouse_id;
        this.status = status;
        this.goods_id = goods_id;
    }

    //积分
    public Parameter(Integer business_id,Integer category_id, String number, String name, Long barcode) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.barcode = barcode;
    }

    //积分
    public Parameter(Integer business_id,Integer category_id, String number, String name, Long barcode ,Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.barcode = barcode;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }


    public Parameter(Integer category_id, String number, String name, Long barcode, Integer pageNo) {
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.barcode = barcode;
        this.pageNo = pageNo;
    }

    //供应商参数
    public Parameter(Integer business_id, Integer category_id, String number, String name) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
    }
    public Parameter(Integer business_id, Integer startIndex, Integer pageSize, Integer category_id, String number, String name) {
        this.business_id = business_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
    }


    //产品操作
    public Parameter(Integer business_id, Integer category_id, String number, String name, Long barcode,Integer level) {
        this.business_id = business_id;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.barcode = barcode;
        this.level = level;
    }

    public  Parameter(Integer business_id, Integer startIndex, Integer pageSize, Integer category_id, String number, String name, Long barcode ,Integer level) {
        this.business_id = business_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.category_id = category_id;
        this.number = number;
        this.name = name;
        this.barcode = barcode;
        this.level = level;
    }

    public Parameter(Integer startIndex, Integer pageSize) {
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }
}
