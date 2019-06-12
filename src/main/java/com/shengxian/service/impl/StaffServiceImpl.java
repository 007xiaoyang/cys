package com.shengxian.service.impl;

import com.shengxian.common.util.*;
import com.shengxian.entity.*;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.MenuMapper;
import com.shengxian.mapper.StaffMapper;
import com.shengxian.service.StaffService;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Description: 员工
 *
 * @Author: yang
 * @Date: 2018/8/24
 * @Version: 1.0
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Resource
    private ShopMapper shopMapper;
    @Resource
    private StaffMapper staffMapper;

    @Resource
    private MenuMapper menuMapper;


    //添加员工类别
    @Override
    @Transactional
    public Integer addStaffCategory(String token ,Integer role, String name) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //根据服务商id查询员工类别最后一条序号
        String code = staffMapper.findStaffCategroyCodeByBid(bid);
        if (code == null ){
            code="1";
        }else {
            code = GroupNumber.getNumber(Integer.parseInt(code));
        }
        return staffMapper.addStaffCategory(bid,name,code);
    }

    //查询服务商下的所有员工类别集合
    @Override
    public List<HashMap> findStaffCategoryInfoListBySid(String token ,Integer role )throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //查询服务商下的所有员工类别集合
        return staffMapper.findStaffCategoryInfoListByBid(bid);
    }

    //通过员工类别id查询员工类别信息
    @Override
    public HashMap findStaffCategoryInfoById(Integer categoryId) {
        return staffMapper.findStaffCategoryInfoById(categoryId);
    }

    //修改员工类别信息
    @Override
    @Transactional
    public Integer updateStaffCategoryByid(Integer id,String name) {
        return staffMapper.updateStaffCategoryByid(id,name);
    }

    //删除员工类别
    @Override
    @Transactional
    public Integer deleStaffCategory(Integer id) throws NullPointerException ,Exception{

        //首先判断该类别下是否有员工信息，存在则不能删除
        List<HashMap> staff = staffMapper.findStaffInfoListByCategoryId(id);

        if (staff.size() > 0){
            throw new NullPointerException("该类别中还有员工存在，不能删除该类别");
        }
        return staffMapper.deleStaffCategory(id);
    }


    //挑选类别自动生成员工编号
    @Override
    public String automaticSelectStaffnumberByCategoryId(Integer id) {
        //通过员工类别id查询最后一条员工编号
        String num = staffMapper.findNumberByCategoryId(id);

        if (num != null){

            num = GroupNumber.getNumber(Integer.parseInt(num));

        }else {
            //根据类别id查询员工类别序号
            num = (staffMapper.findCodeById(id)  + "001" );
        }
        return num;
    }

    //根据用户类别ID条件用户编号或名称查询商家绑定用户的信息
    @Override
    public List<HashMap> findBindingInfoByUser(Integer categoryId, String number, String name) {
        return staffMapper.findBindingInfoByUser(categoryId,number,name);
    }

    //通过产品类别ID和编号或产品名称查询产品信息
    @Override
    public List<HashMap> findGoodsInfoByCid(Integer categoryId, String number, String name) {
        return staffMapper.findGoodsInfoByCid(categoryId,number,name);
    }

    //搜索店铺绑定用户
    @Override
    public List<HashMap> findBusinessUser(String token ,Integer role ,String name) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return staffMapper.findBusinessUser(bid,name);
    }

    //添加员工信息
    @Override
    @Transactional
    public Integer addSstaff(String token ,Integer role , Staff staff) throws NullPointerException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //判断手机号是否注册过员工
        Integer isPhone = staffMapper.findIdByPhone(staff.getPhone());
        if (isPhone != null ){
            throw new NullPointerException("手机号码已经注册过员工了");
        }
        //判断手机号码已经注册过店铺了
        Integer shopPhone = staffMapper.findShopByPhone(staff.getPhone());
        if (shopPhone != null ){
            throw new NullPointerException("手机号码已经注册过店铺了");
        }

        //根据类别id和编号查询是否存在
        String num = staffMapper.findNumberByCategoryIdAndNumber(staff.getCategory_id(), staff.getNumber());
        if (num != null){
            throw new NullPointerException("编号已存在");
        }
        staff.setBusiness_id(bid);
        staff.setToken(UUID.randomUUID().toString());
        staff.setCreate_time(new Date());
        staff.setPassword(PasswordMD5.EncoderByMd5(staff.getPassword()+ Global.passwordKey ));

        Integer count = staffMapper.addStaff(staff);


        //PC菜单权限
        //查询一级菜单
        List<Integer> one_menu_id = menuMapper.findMenuLevelAndSort(1, null);
        for (Integer om_id: one_menu_id  ) {

            //添加一级菜单权限
            menuMapper.addOneMenu(staff.getId() ,om_id);

            //查询二级菜单
            List<Integer> two_menu_id = menuMapper.findMenuLevelAndSort(2, om_id);
            for (Integer two_id : two_menu_id ) {
                //添加二级菜单权限
                menuMapper.addTwoMenu(staff.getId() , two_id ,om_id);


                //查询三级菜单
                List<Integer> three_menu_id = menuMapper.findMenuLevelAndSort(3, two_id);
                for (Integer three_id : three_menu_id){

                    //添加三级菜单权限
                    menuMapper.addThreeMenu(staff.getId() , three_id , two_id);
                }
            }
        }

        //查询app菜单表
        List<Menu> menus = menuMapper.selectAppMenu();
        for (Menu menu : menus  ) {
            //添加员工APP功能菜单列表
            menuMapper.addStaffAppMenu(staff.getId() , menu.getMenu());
        }

        //产品提成
        GoodsPercent[] goodsPercent = staff.getGoodsPercent();
        for (GoodsPercent goods: goodsPercent) {
            //添加员工产品提成
            staffMapper.addStaffGoodsPercentage(goods.getGoods_id(),goods.getProportion(),staff.getId());
        }

        //用户提成
        UserPercent[] userPercents = staff.getUserPercents();
        for (UserPercent user: userPercents) {
            //添加员工用户提成
            staffMapper.addStaffUserPercentage(user.getBinding_id(),user.getProportion(),staff.getId());
        }

        //其它提成
        OtherPercentage[] otherPercentages = staff.getOtherPercentages();
        for (OtherPercentage other: otherPercentages ) {
            if (other.getType() == 11){
                continue;
            }
            staffMapper.addStaffPercentage(other.getProportion(),staff.getId(),other.getType());
        }
        return count;

    }

    //查询类别下的员工信息集合
    @Override
    public Page findStaffInfoList(String token ,Integer role , Integer pageNo, Integer id, String name)throws NullPointerException{


        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (IntegerUtils.isNotEmpty(pageNo)){
            pageNum=pageNo;
        }
        //查询类别下所有员工信息总数
        Integer totlaCount = staffMapper.findStaffInfoListCount(bid,id,name);
        Page page = new Page(pageNum,totlaCount);

        //条件查询类别下所有员工信息集合
        List<HashMap> staff = staffMapper.findStaffInfoListById(bid,page.getStartIndex(), page.getPageSize(), id, name);
        page.setRecords(staff);
        return page;
    }

    //根据员工id查询员工信息
    @Override
    public HashMap findStaffInfoById(Integer id)throws NullPointerException {
        HashMap staff = staffMapper.findStaffInfoById(id);//根据员工id查询员工信息
        if (staff == null){
            throw new NullPointerException("员工id不存在");
        }
        //（销售）根据员工id查询产品提成
        List<HashMap> goodsPercent = staffMapper.findStaffGoodsPercentage(id);

        //（销售）根据员工id查询用户提成
        List<HashMap> userPercent = staffMapper.findStaffUserPercentage(id);

        //（销售）根据员工id查询仓库提成
        List<HashMap> warehousePercent = staffMapper.findStaffWarehousePercentage(id);

        //根据员工id查询员工提成
        List<HashMap> percent = staffMapper.findStaffSalePercentage(id);
        staff.put("goodsPercent",goodsPercent);
        staff.put("userPercent",userPercent);
        staff.put("warehousePercent",warehousePercent);
        staff.put("percent",percent);
        return staff;
    }


    @Override
    @Transactional
    public Integer updateStaff(Staff staff) throws NullPointerException ,Exception{

        //根据员工id查询员工信息
        HashMap staff2 = staffMapper.findStaffInfoById(staff.getId());

        //判断输入的员工编号和原来的编号是否不一致
        if (!staff.getNumber().equals(staff2.get("number").toString())){

            //根据员工类别id和员工编号查询当前编号是否存在
            String number = staffMapper.findNumberByCategoryIdAndNumber(staff.getCategory_id(), staff.getNumber());
            if (number != null){
               throw new NullPointerException("员工编号已存在，请重新输入");
            }
        }
        //修改员工信息
        Integer count = staffMapper.updateStaff(staff);

        //修改员工产品提成
        GoodsPercent[] goodsPercent = staff.getGoodsPercent();
        for (GoodsPercent goods: goodsPercent ) {
            goods.setStaff_id(staff.getId());
            staffMapper.updateStaffGoodsPercent(goods);
        }

        //修改员工的客户提成
        UserPercent[] userPercents = staff.getUserPercents();
        for (UserPercent user: userPercents  ) {
            user.setStaff_id(staff.getId());
            staffMapper.updateStaffUserPercent(user);
        }

      /*  //修改员工的仓库提成
        WarehousePercent[] warehousePercents = staff.getWarehousePercents();
        for (WarehousePercent warehouse: warehousePercents  ) {
            warehouse.setStaff_id(staff.getId());
            staffMapper.updateStaffWarehousePercent(warehouse);
        }*/
        //修改员工的其它销售提成
        OtherPercentage[] otherPercentages = staff.getOtherPercentages();
        for (OtherPercentage other:otherPercentages){
            staffMapper.updateStaffOtherPercent(other);
        }
        return count;

    }

    //删除员工信息
    @Override
    @Transactional
    public Integer deleSatff(Integer id) {
        return staffMapper.deleSatff(id);
    }

    //匹配员工密码
    @Override
    public boolean findStaffPWD(Integer staff_id, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //通过员工id查询原来密码
        String pwd = staffMapper.findStaffPassword(staff_id);
        String pwd1 = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
        if (pwd.equals(pwd1)){
            return true;
        }
        return false;
    }

    //修改员工密码
    @Override
    @Transactional
    public Integer updateStaffPwd(Integer staff_id, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String pwd = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
        return staffMapper.updateStaffPwd(staff_id,pwd);
    }


    //删除员工的产品提成信息
    @Override
    @Transactional
    public Integer deleteStaffGoodsPercent(Integer id) {
        return staffMapper.deleteStaffGoodsPercent(id);
    }

    //删除员工的用户提成信息
    @Override
    @Transactional
    public Integer deleteStaffUserPercent(Integer id) {
        return staffMapper.deleteStaffUserPercent(id);
    }

    //删除员工的用仓库提成信息
    @Override
    @Transactional
    public Integer deleteStaffWarehousePercent(Integer id) {
        return staffMapper.deleteStaffWarehousePercent(id);
    }

    //删除员工的其它销售提成信息
    @Override
    @Transactional
    public Integer deleteStaffOtherPercent(Integer id) {
        return staffMapper.deleteStaffOtherPercent(id);
    }


    //查询类别下所有员工信息集合
    @Override
    public List<HashMap> findStaffInfoListByCategoryId(Integer id) {
        return staffMapper.findStaffInfoListByCategoryId(id);
    }

    //导出用户数据
    @Override
    public HSSFWorkbook excelDownload(String token ,Integer role, Integer id, String phone, String number, String name) {
        Integer bid = shopMapper.businessIdByToken(token);
        if (bid == null) {
            throw new NullPointerException("您的账号登录失效或在另一台设备登录");
        }
        String seetName = "员工资料";//sheet名
        String[] title =new String[]{ "员工类别", "员工编号","员工名称","员工账号","创建时间"};//标题
        List<HashMap> hashMaps = staffMapper.excelDownloadStaffInfoList(bid,id,phone,number,name);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("categoryName").toString();
            values[i][1] = hashMap.get("number").toString();
            values[i][2] = hashMap.get("name").toString();
            values[i][3] = hashMap.get("phone").toString();
            values[i][4] = hashMap.get("create_time").toString();
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }
}
