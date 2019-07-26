package com.shengxian.entity;

/**
 * Description: 财务条件搜索参数类
 *
 * @Author: yang
 * @Date: 2018-12-03
 * @Version: 1.0
 */
public class Paramt {

    private Integer business_id;
    private Integer staff_id;
    private String name;
    private String number;
    private String startTime;
    private String endTime;
    private Integer startIndex;
    private Integer pageSize;

    private Integer status;
    private String state;
    private  String staffName ;
    private Integer bindindId ;
    private String goodsName;

    private Integer is;
    private Integer part;
    private Integer mold;

    private Integer type;

    private String type2;


    public Paramt() {
    }

    /**
     *分享微信上需要的参数
     */
    public Paramt (  String goodsName ,Integer business_id ,Integer type ,Integer bindindId ,String name, String startTime, String endTime){
        this.goodsName = goodsName;
        this.business_id = business_id;
        this.type = type;
        this.bindindId = bindindId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;

    }
    /**
     *分享微信上需要的参数
     */
    public Paramt (String goodsName ,Integer business_id ,Integer type ,Integer bindindId ,String name, String startTime, String endTime ,Integer startIndex ,Integer pageSize){
        this.goodsName = goodsName;
        this.business_id = business_id;
        this.type = type;
        this.bindindId = bindindId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex ;
        this.pageSize = pageSize;

    }

    public Paramt(Integer business_id, String name) {
        this.business_id = business_id;
        this.name = name;
    }

    public Paramt(Integer business_id, String name, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, String name, Integer mold) {
        this.business_id = business_id;
        this.name = name;
        this.mold = mold;
    }

    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, String state) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize, String state) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.state = state;
    }

    public Paramt(Integer business_id, Integer staff_id, String name, String startTime, String endTime) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Paramt(Integer business_id, Integer staff_id, String name, String startTime, String endTime,Integer status) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Paramt(Integer business_id, Integer staff_id, String name, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, Integer staff_id) {
        this.business_id = business_id;
        this.staff_id = staff_id;
    }

    public Paramt(Integer business_id, Integer staff_id, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, Integer staff_id, Integer status) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.status = status;
    }

    public Paramt(Integer business_id, Integer staff_id,  Integer status,Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.status = status;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, Integer staff_id, String name, String number) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.number = number;
    }

    public Paramt(Integer business_id, Integer staff_id, String name, String number, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.name = name;
        this.number = number;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, String name, String startTime, String endTime) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Paramt(Integer business_id, String name, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    /**
     * 财务总销售订单
     * @param business_id
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     */
    public Paramt(Integer business_id, String name, String number ,Integer type,  String startTime, String endTime) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    /**
     * 财务总销售订单
     * @param business_id
     * @param name
     * @param number
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     */
    public Paramt(Integer business_id,  String name, String number ,Integer type, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, String name, String number ,String startTime, String endTime) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Paramt(Integer business_id,  String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, String state, Integer is) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.is = is;
    }

    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, String state, Integer part, Integer mold) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.part = part;
        this.mold = mold;
    }

    //总销售搜索条件
    public Paramt(Integer business_id ,Integer type, String name, String number, String startTime, String endTime, Integer mold ) {
        this.business_id = business_id;
        this.type = type;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mold = mold;
    }
    //总销售搜索条件
    public Paramt(Integer business_id ,Integer type, String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize, Integer mold ) {
        this.business_id = business_id;
        this.type = type;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.mold = mold;
        this.type2 = type2;
    }

    //
    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, Integer mold) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mold = mold;
    }
    //
    public Paramt(Integer business_id, String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize, Integer mold) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.mold = mold;
    }

    /**
     * 用户销售毛利
     * @param business_id
     * @param name
     * @param startTime
     * @param endTime
     * @param is
     */
    public Paramt(Integer business_id, String name, String startTime, String endTime, Integer is ,String goodsName) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.is = is;
        this.goodsName = goodsName;
    }

    /**
     * 用户销售毛利页数
     * @param business_id
     * @param name
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @param is
     * @param goodsName
     */
    public Paramt(Integer business_id, String name, String startTime, String endTime, Integer startIndex, Integer pageSize, Integer is ,String goodsName) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.is = is;
        this.goodsName = goodsName;
    }

    public Paramt(Integer business_id, String name, String startTime, String endTime, Integer is) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.is = is;
    }

    public Paramt(Integer business_id, String name, String startTime, String endTime, Integer startIndex, Integer pageSize, Integer is) {
        this.business_id = business_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.is = is;
    }

    public Paramt(Integer business_id, Integer staff_id,Integer status, String startTime, String endTime, Integer is) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.is = is;
    }

    public Paramt(Integer business_id, Integer staff_id,Integer status, String startTime, String endTime, Integer startIndex, Integer pageSize, Integer is) {
        this.business_id = business_id;
        this.staff_id = staff_id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.is = is;
    }

    //待送货订单汇总参数
    public Paramt(Integer business_id, String name, String number ,Integer mold ) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.mold = mold;
    }

    //待送货订单汇总参数
    public Paramt(Integer business_id, String name, String number  ,Integer mold , Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.mold = mold;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(Integer business_id, String name, String number ) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
    }

    public Paramt(Integer business_id, String name, String number, Integer startIndex, Integer pageSize) {
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Paramt(String staffName ,Integer business_id, String name, String number, Integer mold) {
        this.staffName = staffName;
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.mold = mold;
    }

    //销售欠款收的钱
    public Paramt(Integer business_id,Integer mold,Integer part,String name,String number,String startTime,String endTime){
        this.business_id=business_id;
        this.mold = mold;
        this.part = part;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //采购欠款收的钱
    public Paramt(Integer business_id,Integer mold,String name,String number,String startTime,String endTime){
        this.business_id=business_id;
        this.mold = mold;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Paramt(String staffName ,Integer business_id, String name, String number ) {
        this.staffName = staffName;
        this.business_id = business_id;
        this.name = name;
        this.number = number;
    }

    public Paramt(String staffName ,Integer business_id, String name, String number, Integer startIndex, Integer pageSize) {
        this.staffName = staffName;
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }


    public Paramt(String staffName , Integer business_id, String name, String number, String startTime, String endTime ) {
        this.staffName = staffName;
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Paramt(String staffName , Integer business_id,  String name, String number, String startTime, String endTime, Integer startIndex, Integer pageSize) {
        this.staffName = staffName;
        this.business_id = business_id;
        this.name = name;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(Integer staff_id) {
        this.staff_id = staff_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getIs() {
        return is;
    }

    public void setIs(Integer is) {
        this.is = is;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "Paramt{" +
                "business_id=" + business_id +
                ", staff_id=" + staff_id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startIndex=" + startIndex +
                ", pageSize=" + pageSize +
                ", status=" + status +
                ", state='" + state + '\'' +
                ", staffName='" + staffName + '\'' +
                ", bindindId=" + bindindId +
                ", goodsName='" + goodsName + '\'' +
                ", is=" + is +
                ", part=" + part +
                ", mold=" + mold +
                ", type=" + type +
                '}';
    }
}
