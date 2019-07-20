package com.shengxian.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shengxian.common.MothPrinter;
import com.shengxian.common.util.DateUtil;
import com.shengxian.entity.*;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.MenuMapper;
import com.shengxian.service.MenuService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-10-09
 * @Version: 1.0
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private ShopMapper shopMapper;


    //菜单
    @Override
    public List<HashMap> selectOneMenu(Integer level, Integer sort) {
        return menuMapper.selectOneMenu(level,sort);
    }

    @Override
    public List<Menu> selectTwoMenuList(String token ,Integer role , Integer menuId) throws NullPointerException{

        //role == 1 是店铺 ，2是员工

        List<Menu> menus = null;

        if (role == 1){ //店铺直接查询菜单

            //通过一级菜单id查询第二级菜单列表(没权限，直接查询菜单表)
            menus = menuMapper.selectTwoMenuList(menuId);

        }else if (role == 2){ //员工得要权限才行

            //通过token查询员工id
            Integer sid = shopMapper.findStaffIdByToken(token);
            if (sid == null){
                throw new NullPointerException("您的账号登录失效或在另一台设备登录");
            }

            // 通过员工id和一级菜单id查询二级菜单权限
            menus = menuMapper.findTwoMenu(sid, menuId ,1);

        }
        return menus;
    }

    @Override
    public boolean isPower(String token,Integer menu_id)throws NullPointerException {
        Integer bid = null;
        bid = shopMapper.findIdByBusinessToken(token);
        if (bid == null){
            bid = shopMapper.findIdByStaffToken(token);
            String threeMenu = menuMapper.selectThreeMenuIdBySid(bid);
            if (threeMenu == null){
                throw new NullPointerException("您没有权限查看");
            }
            List<HashMap> hashMaps = menuMapper.selectThreeMenuList(menu_id);
            String[] split = threeMenu.split(",");
            for (String three:split ) {
                Iterator<HashMap> iterator = hashMaps.iterator();
                while(iterator.hasNext()){
                    HashMap next = iterator.next();
                    Integer id = (Integer) next.get("id");
                    if (Integer.parseInt(three) != id){
                        iterator.remove();
                    }
                }
            }
        }
       return true;
    }

    //获取员工角色
    @Override
    public List<Integer> staffRole(Integer staff_id, Integer type) {

        List<Integer> menu_id = null;
        if (type == 1){ //首页菜单
            menu_id = menuMapper.oneMenu(staff_id);
        }else if(type == 2){ //销售
            menu_id = menuMapper.twoMenu(staff_id);
        }else if(type == 3){//采购
            menu_id = menuMapper.threeMenu(staff_id);
        }else if(type == 4){//库存
            menu_id = menuMapper.fourMenu(staff_id);
        }else if(type == 5){//配送
            menu_id = menuMapper.fiveMenu(staff_id);
        }else if(type == 6){//财务
            menu_id = menuMapper.sixMenu(staff_id);
        }else if(type == 7){//资料
            menu_id = menuMapper.sevenMenu(staff_id);
        }else if(type == 9){//设置
            menu_id = menuMapper.nineMenu(staff_id);
        }else if(type == 8){//员工
            menu_id = menuMapper.eightMenu(staff_id);
        }
        return menu_id;
    }

    //修改员工角色
    @Override
    @Transactional
    public Integer udpateStaffRole(MenuRole menuRole){
        Integer count = null;
        //修改首页菜单权限1
        count = menuMapper.updateOneMenu(menuRole.getStaff_id(),menuRole.getOne());
        //修改销售菜单权限2
        count = menuMapper.updateTwoMenu(menuRole.getStaff_id(),menuRole.getTwo());
        //修改采购菜单权限3
        count = menuMapper.updateThreeMenu(menuRole.getStaff_id(),menuRole.getThree());
        //修改库存菜单权限4
        count = menuMapper.updateFourMenu(menuRole.getStaff_id(),menuRole.getFour());
        //修改配送菜单权限5
        count = menuMapper.updateFiveMenu(menuRole.getStaff_id(),menuRole.getFive());
        //修改财务菜单权限6
        count = menuMapper.updateSixMenu(menuRole.getStaff_id(),menuRole.getSix());
        //修改资料菜单权限7
        count = menuMapper.updateSevenMenu(menuRole.getStaff_id(),menuRole.getSeven());
        //修改员工菜单权限8
        count = menuMapper.updateEightMenu(menuRole.getStaff_id(),menuRole.getEight());
        //修改设置菜单权限9
        count = menuMapper.updateNineMenu(menuRole.getStaff_id(),menuRole.getNine());
        return count;
    }


    //添加模板
    @Override
    @Transactional
    public Integer addTemplate(String token  ,Integer role, String title, String name, String phone,Integer type) {

        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return menuMapper.addTemplate(new Template(bid,title,name,phone,type,""));
    }

    //修改模板
    @Override
    @Transactional
    public Integer updateTemplate(Template template)throws Exception {
        return menuMapper.updateTemplate(template);
    }

    //恢复模板
    @Override
    @Transactional
    public Integer updateRecoveryTemplate(Integer id, Integer type) throws Exception {
        if (type == 0){
            return menuMapper.updateRecoveryTemplate(new Template(id,"采购单","供应商名称","供应商电话","供应商"));
        }else if(type == 1){
            return menuMapper.updateRecoveryTemplate(new Template(id,"采购退货单","供应商名称","供应商电话","供应商"));
        }else if (type == 2){
            return menuMapper.updateRecoveryTemplate(new Template(id,"销售单","客户名称","客户电话","客户"));
        }else if (type == 3){
            return menuMapper.updateRecoveryTemplate(new Template(id,"销售退货单","客户名称","客户电话","客户"));
        }
        return null;
    }

    //修改模板二维码显示状态
    @Override
    @Transactional
    public Integer updateTemplateState(Integer id, Integer state) throws Exception {
        return menuMapper.updateTemplateState(id,state);
    }

    //删除log
    @Override
    @Transactional
    public Integer deleteTemplateLog(Integer id) {
        return menuMapper.deleteTemplateLog(id);
    }


    //通过员工id查询一级菜单权限
    @Override
    public List<Menu> findStaffOneMenu(Integer staff_id) {
        return menuMapper.findOneMenu(staff_id ,null);
    }

    //通过员工id和一级菜单id查询二级菜单
    @Override
    public List<Menu> findStaffTwoMenu(Integer staff_id, Integer sort) throws Exception {
        return menuMapper.findTwoMenu(staff_id ,sort ,null);
    }

    //通过员工id和二级菜单id查询三级菜单
    @Override
    public List<Menu> findStaffThreeMenu(Integer staff_id, Integer sort) throws Exception {
        return menuMapper.findThreeMenu(staff_id ,sort ,null);
    }

    //修改员工菜单权限
    @Override
    @Transactional
    public Integer updateStaffMenu(Menu[] oneMenus, Menu[] twoMenu, Menu[] threeMenu) throws Exception{

        Integer count = null;

        //修改一级菜单
        for (Menu one :oneMenus ) {
            count = menuMapper.updateStaffOneMenu(one.getM_id() ,one.getState());
        }

        //修改二级菜单
        for (Menu two :twoMenu ) {
            count = menuMapper.updateStaffTwoMenu(two.getM_id() ,two.getState());
        }

        //修改三级菜单
        for (Menu three :threeMenu ) {
            count = menuMapper.updateStaffThreeMenu(three.getM_id() ,three.getState());
        }

        return count;
    }

    //三级菜单
    @Override
    public List<Menu> selectThreeMenu(String token ,Integer role, Integer sort) {

        if (role == 1){
            return menuMapper.businessSelectThreeMenu(sort);
        }else {
            Integer id = shopMapper.registerIdByTokenAndRole(token, role);
            return menuMapper.staffSelectThreeMenu(id , sort);
        }
    }

    //员工APP菜单
    @Override
    public List<Menu> selecStaffAppMenu( Integer staff_id) {
        return menuMapper.selecStaffAppMenu(staff_id);
    }

    //修改员工APP菜单
    @Override
    @Transactional
    public Integer updateStaffAppMenu(Menu[] menu) {
        Integer count = null;
        //修改一级菜单
        for (Menu one :menu ) {
            count = menuMapper.updateStaffAppMenu(one.getId() ,one.getState());
        }
        return count;
    }


    //添加飞蛾打印机
    @Override
    @Transactional
    public Integer addPrinter(String token, Integer role, String sn, String key, String remark, String carnum,Integer num ,Integer ors) throws NullPointerException, Exception{

        Integer count = null;

        String msg = MothPrinter.addPrinter("'"+sn+"#"+key+"#"+remark+"#"+carnum+"'");
        JSONObject json = JSONObject.parseObject(msg);
        Integer ret = json.getInteger("ret");
        if (ret == 1002){
            throw new NullPointerException("验证失败 : 没有找到该打印机的信息");
        }
        JSONObject data = json.getJSONObject("data");
        JSONArray ok = data.getJSONArray("ok");
        if (ok.size() > 0){
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            Integer isExist = menuMapper.findBusinessPrinterIsExist(bid, sn);
            if (isExist != null ){
                menuMapper.deletePrinter(bid , sn );
            }
            count = menuMapper.addPrinter(new Printer(bid , sn ,key, remark ,carnum , msg ,num ,ors));
        }else {
            String no = data.getString("no");
            byte[] bytes = no.getBytes();
            String test = new String(bytes, "UTF-8");
            throw new NullPointerException(test);
        }

        return  count;

    }

    //查询店铺打印机
    @Override
    public List<Printer> queryPrinter(String token, Integer role) throws NullPointerException , UnsupportedEncodingException {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        List<Printer> printers = menuMapper.queryPrinter(bid ,null);
        for (Printer printer : printers  ) {
            //查询打印机的状态
            String msg = MothPrinter.queryPrinterStatus(printer.getSn1());
            JSONObject json = JSONObject.parseObject(msg);
            Integer r = json.getInteger("ret");
            if (r == 1002 ){
                throw new NullPointerException(json.getString("msg"));
            }
            String data = json.getString("data");
            byte[] bytes = data.getBytes();
            String s = new String(bytes, "UTF-8");
            printer.setStatus(s);

            //查询指定打印机当天的订单统计数
            String msg2 = MothPrinter.queryOrderInfoByDate(printer.getSn1(), DateUtil.getDay());
            JSONObject json2 = JSONObject.parseObject(msg2);
            Integer ret = json2.getInteger("ret");
            if (ret == 0 ){
                JSONObject data2 = json2.getJSONObject("data");
                printer.setPrint(data2.getInteger("print"));
                printer.setWaiting(data2.getInteger("waiting"));
            }
        }
        return printers;
    }

    //修改打印机
    @Override
    @Transactional
    public Integer printerEdit(String token, Integer role, String sn, String name, String phonenum , Integer num , Integer ORS) throws NullPointerException, Exception{


        String msg = MothPrinter.printerEdit(sn, name);
        JSONObject json = JSONObject.parseObject(msg);
        Integer ret = json.getInteger("ret");
        if (ret == -2){
            throw new NullPointerException("打印机编号为空");
        }
        if (ret == 1001){
            throw new NullPointerException("验证失败 : 打印机编号不合法");
        }
        if (ret == 1002){
            throw new NullPointerException("打印机编号和用户不匹配");
        }
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        return  menuMapper.printerEdit(bid , sn , name ,phonenum ,num ,ORS);
    }

    //删除打印机
    @Override
    public Integer printerDelList(String token, Integer role, String sn)throws NullPointerException , Exception {

        Integer count = null;

        String msg = MothPrinter.printerDelList(sn);
        JSONObject json = JSONObject.parseObject(msg);
        Integer ret = json.getInteger("ret");
        if (ret == -2){
            throw new NullPointerException("打印机编号为空");
        }
        JSONObject data = json.getJSONObject("data");
        JSONArray ok = data.getJSONArray("ok");
        if (ok.size() > 0){
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);
            count = menuMapper.deletePrinter(bid , sn );
        }else {
            String no = data.getString("no");
            byte[] bytes = no.getBytes();
            String test = new String(bytes, "UTF-8");
            throw new NullPointerException(test);
        }
        return count;
    }

    //启用打印机
    @Override
    public Integer enablePrinter(Integer id, Integer state) {
        return menuMapper.enablePrinter(id ,state);
    }

    //通过id查询打印机信息
    @Override
    public Printer selectPrinterById(Integer id) {
        return menuMapper.selectPrinterById(id);
    }



    //修改店铺使用哪个单据模块
    @Override
    @Transactional
    public Integer updateBusinessPrinterModular(String token, Integer role, Integer type) throws NullPointerException, Exception {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        if (type == 1){ //飞蛾打印机
            //查询店铺是否添加了飞蛾打印机
            List<Integer> printerList = menuMapper.queryPrinterId(bid ,null);
            if (printerList.size() <= 0 ){
                throw new NullPointerException("您未添加打印机，不能使用该模块");
            }
            //查询店铺是否启用了飞蛾打印机
            List<Integer> printerId = menuMapper.queryPrinterId(bid ,0);
            if (printerId.size() <= 0 ){
                throw new NullPointerException("打印机未启用，不能使用该模块");
            }
        }
        return menuMapper.updateBusinessPrinterModular(bid , type);
    }

    //查询店铺使用哪个单据模块
    @Override
    public Integer queryBusinessPrinterModular(String token, Integer role) {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        return menuMapper.queryBusinessPrinterModular(bid);
    }


    //调飞蛾打印机打印（销售订单）
    @Override
    public void mothPrintSale(String token ,Integer role ,Integer order_id, String titile) throws NullPointerException ,Exception{
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        List<Printer> printers = menuMapper.queryPrinter(bid , 0);
        if (printers.size() <=  0 ){
            throw new NullPointerException("飞蛾打印机未启用或未添加");
        }
        //通过订单id查询订单信息
        Order order = menuMapper.queryOrder(order_id);
        if (order == null ){
            throw new NullPointerException("订单不存在");
        }

        //通过订单id查询订单详情
        List<MothPrinterClass> mothPrinters = menuMapper.queryOrderDateil(order_id);

        for (int i = 0 ; i < printers.size() ; i ++){

            for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                MothPrinter.salePrint(printers.get(i).getSn1() ,titile,order.getBeizhu(), new BigDecimal(order.getPrice().toString()),order.getAddress(),
                        order.getPhone(),order.getName(),order.getOrder_number(),order.getNo() ,order.getFreight() ,order.getDifference_price(),order.getReduce() ,mothPrinters);

            }

        }
    }

    //调飞蛾打印机打印（采购订单）
    @Override
    public void mothPrintPurchase(String token ,Integer role ,Integer order_id, String titile)throws NullPointerException ,Exception {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        List<Printer> printers = menuMapper.queryPrinter(bid , 0);
        if (printers.size() <=  0 ){
            throw new NullPointerException("飞蛾打印机未启用或未添加");
        }
        //通过订单id查询订单信息
        PurchaseOrder purchaseOrder = menuMapper.queryPurchaseOrder(order_id);
        if (purchaseOrder == null ){
            throw new NullPointerException("订单不存在");
        }

        //通过订单id查询订单详情
        List<MothPrinterClass> mothPrinters = menuMapper.queryPurchaseOrderDateil(order_id);

        for (int i = 0 ; i < printers.size() ; i ++){

            for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                MothPrinter.puchasePrint(printers.get(i).getSn1() ,titile,purchaseOrder.getBeizhu(), new BigDecimal(purchaseOrder.getPrice().toString())
                        ,purchaseOrder.getAddress(),purchaseOrder.getPhone(),purchaseOrder.getName(),purchaseOrder.getOrder_number()
                        ,purchaseOrder.getNo(),purchaseOrder.getFreight() ,purchaseOrder.getDifference_price(),mothPrinters);
            }

        }
    }


    //调飞蛾打印机打印（临时订单）
    @Override
    public void mothPrintTemporarySale(String token, Integer role, Integer order_id, String titile) throws NullPointerException, Exception {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        List<Printer> printers = menuMapper.queryPrinter(bid , 0);
        if (printers.size() <=  0 ){
            throw new NullPointerException("飞蛾打印机未启用或未添加");
        }
        //通过订单id查询订单信息
        Order order = menuMapper.queryTemporaryOrder(order_id);
        if (order == null ){
            throw new NullPointerException("订单不存在");
        }

        //通过订单id查询订单详情
        List<MothPrinterClass> mothPrinters = menuMapper.queryTemporaryOrderDateil(order_id);

        for (int i = 0 ; i < printers.size() ; i ++){

            for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                MothPrinter.salePrint(printers.get(i).getSn1() ,titile,order.getBeizhu(), new BigDecimal(order.getPrice().toString()),order.getAddress(),order.getPhone(),order.getName(),order.getOrder_number(),order.getNo() ,order.getFreight() ,order.getDifference_price() ,order.getReduce() ,mothPrinters);

            }

        }
    }

    //积分兑换打印
    @Override
    public void mothPrintExchange(String token, Integer role, String goodsName, String price, String number, String num, String userName, String phone, String address, String time) {
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        List<Printer> printers = menuMapper.queryPrinter(bid , 0);
        if (printers.size() <=  0 ){
            throw new NullPointerException("飞蛾打印机未启用或未添加");
        }

        for (int i = 0 ; i < printers.size() ; i ++){

            for (int j = 1 ; j <= printers.get(i).getNum() ; j ++ ){

                MothPrinter.exchange(printers.get(i).getSn1(),goodsName,price,number ,num ,userName , phone ,address ,time);

            }

        }
    }
}
