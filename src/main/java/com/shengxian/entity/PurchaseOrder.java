package com.shengxian.entity;

import java.util.Date;

/**
 * Description: 采购订单
 *
 * @Author: yang
 * @Date: 2018-11-10
 * @Version: 1.0
 */
public class PurchaseOrder {

    private Integer id;
    private String order_number; //订单号
    private Integer suppliers_id; //供应商
    private Integer business_id; //
    private int staff_id; // 业务员
    private String making; //制单人
    private Double price; //总金额
    private int status; //状态，0默认待审核，1采购到货，2未到货，3直接退货
    private int state; //0未付款，1欠款，2已完成
    private Integer type; //支付类型，1微信，2支付宝，3现金，4银行卡，5其它
    private Date create_time;
    private double freight; //运费
    private double difference_price;//差别价格
    private String beizhu;//备注
    private int print_frequ;//0默认未打印，1打印
    private int mold;//0采购单，1采购退货单
    private String number;

    private String originator;//制单人姓名
    private String name;//名称
    private String phone;//电话号码
    private String address;//地址
    private Integer tatolCount;//订单总数
    private String no;//二维码标识
    private String money;//中文字金额

    private String bAddress ;
    private String telephone ;

    private String createTime;

    private PurchaseOrderDetail[] purchaseOrderDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public Integer getSuppliers_id() {
        return suppliers_id;
    }

    public void setSuppliers_id(Integer suppliers_id) {
        this.suppliers_id = suppliers_id;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getMaking() {
        return making;
    }

    public void setMaking(String making) {
        this.making = making;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getDifference_price() {
        return difference_price;
    }

    public void setDifference_price(double difference_price) {
        this.difference_price = difference_price;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public int getPrint_frequ() {
        return print_frequ;
    }

    public void setPrint_frequ(int print_frequ) {
        this.print_frequ = print_frequ;
    }

    public int getMold() {
        return mold;
    }

    public void setMold(int mold) {
        this.mold = mold;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTatolCount() {
        return tatolCount;
    }

    public void setTatolCount(Integer tatolCount) {
        this.tatolCount = tatolCount;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public PurchaseOrderDetail[] getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail[] purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getbAddress() {
        return bAddress;
    }

    public void setbAddress(String bAddress) {
        this.bAddress = bAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
