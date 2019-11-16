package com.shengxian.entity;

/**
 * Description: 模板类
 *
 * @Author: yang
 * @Date: 2018-11-17
 * @Version: 1.0
 */
public class Template {

    private Integer id;
    private Integer business_id;
    private String title;//标题
    private String no;
    private String originator;//制单人
    private String name;//
    private String phone;
    private String address;
    private String beizhu;
    private String serial;//序号
    private String goods_name;//货物名称
    private String number;//数量
    private String unit_price;//单价
    private String money;//金额
    private String img;
    private String one;
    private String two;
    private String three;
    private String four;
    private Integer type;
    private Integer state;
    private int barcode;

    public Template() {
    }

    public Template(Integer business_id, String title, String name, String phone, Integer type ,String userName) {
        this.business_id = business_id;
        this.title = title;
        this.no = "单据编号";
        this.originator = "制单人";
        this.name = name;
        this.phone = phone;
        this.address = "地址";
        this.beizhu = "备注";
        this.serial = "序号";
        this.goods_name = "货物名称";
        this.number = "数量";
        this.unit_price = "单价";
        this.money = "金额";
        this.one = "致尊敬的"+userName+"，此单经收货人签收或盖章，将作为收欠款凭证！";
        this.two = "为了更好的服务到位，尽量在晚上微信订货，方便早上安排送货，当天上午6点-12点订货，";
        this.three = "下午送到，下午3点钟订货，晚上之前送到，当天下午3点后订货，明天中午之前送到。";
        this.four = "希望客户合理备货，尽量不要催货，好让本店安排，有所不便之处，敬请谅解。";
        this.type = type;
        this.barcode = barcode;
    }

    /**
     * 恢复模板
     * @param id
     * @param title
     * @param name
     * @param phone
     */
    public Template(Integer id, String title, String name, String phone ,String userName ) {
        this.id = id;
        this.img = null;
        this.title = title;
        this.no = "单据编号";
        this.originator = "制单人";
        this.name = name;
        this.phone = phone;
        this.address = "地址";
        this.beizhu = "备注";
        this.serial = "序号";
        this.goods_name = "货物名称";
        this.number = "数量";
        this.unit_price = "单价";
        this.money = "金额";
        this.one = "致尊敬的"+userName+"，此单经收货人签收或盖章，将作为收欠款凭证！";
        this.two = "为了更好的服务到位，尽量在晚上微信订货，方便早上安排送货，当天上午6点-12点订货，";
        this.three = "下午送到，下午3点钟订货，晚上之前送到，当天下午3点后订货，明天中午之前送到。";
        this.four = "希望客户合理备货，尽量不要催货，好让本店安排，有所不便之处，敬请谅解。";
        this.barcode = 0;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getBarcode(){return  barcode;}
    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }
}
