package com.shengxian.entity;

import java.util.Date;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-11-22
 * @Version: 1.0
 */
public class Order {

    private Integer id;
    private String order_number; //订单号
    private String no;//二维码标识
    private Integer binding_id; //用户id
    private Integer business_id; //
    private int staff_id; // 业务员
    private Integer coupon_id ;
    private Double price; //总金额
    private int status; //状态，0开单，1待接单，2已接单，3待收货，4已完成，5拒绝接单
    private int state; //0未付款，1欠款，2已完成
    private Integer type; //支付类型，1微信，2支付宝，3现金，4银行卡，5其它
    private Date create_time;
    private Date confirm_time;//到达时间
    private double statis;//统计提成金额
    private String beizhu;//备注
    private double freight; //运费
    private double difference_price; //差价
    private int print_frequ;//0默认未打印，1打印
    private int mold;//0销售单，1销售退货单
    private String making; //制单人
    private String deliver; //送货人
    private String payee; //收款人

    private int part ;




    private String originator;//制单人姓名
    private String name;//名称
    private String phone;//电话号码
    private String address;//地址
    private Integer tatolCount;//订单总数
    private String money;//中文字金额
    private Double reduce; //优惠

    private OrderDetail[] orderDetails;


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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(Integer binding_id) {
        this.binding_id = binding_id;
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

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
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

    public Date getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(Date confirm_time) {
        this.confirm_time = confirm_time;
    }

    public double getStatis() {
        return statis;
    }

    public void setStatis(double statis) {
        this.statis = statis;
    }


    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
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

    public String getMaking() {
        return making;
    }

    public void setMaking(String making) {
        this.making = making;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Double getReduce() {
        return reduce;
    }

    public void setReduce(Double reduce) {
        this.reduce = reduce;
    }

    public OrderDetail[] getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetail[] orderDetails) {
        this.orderDetails = orderDetails;
    }
}
