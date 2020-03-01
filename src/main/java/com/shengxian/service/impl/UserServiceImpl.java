package com.shengxian.service.impl;

import com.google.common.collect.Lists;
import com.shengxian.common.util.*;
import com.shengxian.entity.*;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.StaffMapper;
import com.shengxian.mapper.UserMapper;
import com.shengxian.service.UserService;
import com.shengxian.vo.UserCategoryVO;
import com.shengxian.vo.UserVO;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/31
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private StaffMapper staffMapper;

    //添加客户类别
    @Override
    @Transactional
    public Integer addUserCategory(String token ,Integer role , String name) throws Exception{

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        //通过店铺id查询用户类别最后一条序号
        String code = userMapper.findUserCategroyCodeByBid(bid);
        if (code == null){
            code = "1";
        }else {
           code = GroupNumber.getNumber(Integer.parseInt(code));
        }
        return  userMapper.addUserCategory(bid,name,code);
    }


    //查询商家下的所有用户类别集合
    @Override
    public List<HashMap> findUserCategoryList(String token ,Integer role )throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return userMapper.findUserCategoryList(bid);
    }


    //根据客户类别id查询类别信息
    @Override
    public HashMap findUserCategoryById(Integer id) {
        return userMapper.findUserCategoryById(id);
    }

    //修改用户类别信息
    @Override
    @Transactional
    public Integer updateUserCategory(Integer id, String name) {
        return userMapper.updateUserCategory(id,name);
    }

    //删除用户类别
    @Override
    @Transactional
    public Integer deleUserCategory(Integer id)throws NullPointerException {

        //通过类别id查询类别下的客户信息集合
        List<HashMap> user = userMapper.findUserListtByCategoryId(id);
        if (user.size()>0 ){
            throw new NullPointerException("该类别下还有用户存在，不能删除");
        }
        return userMapper.deleUserCategory(id);
    }

    //挑选类别自动生成客户编号
    @Override
    public String selectNumberByCategoryId(Integer id) {

        //根据用户类别id查询最后一条用户编号
        String num = userMapper.findNumberByCategoryId(id);

        if (num == null){
            num = userMapper.findCodeById(id) + "001";

        }else {
            num = GroupNumber.getNumber(Integer.parseInt(num));
        }
        return num;
    }

    //添加客户信息
    @Override
    @Transactional
    public Integer addUser(String token ,Integer role , BindUser bindUser)throws NullPointerException,Exception {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        Integer uid = null;

        //判断商家添加用户时是否绑定用户的手机号码
        if (bindUser.getPhone() != null && !"".equals(bindUser.getPhone())){
            //根据用户手机号判断是否存在
            uid = userMapper.findUidByPhone(bindUser.getPhone());
            if (uid == null){
                throw new NullPointerException("手机号码还未注册");
            }
            //通过手机号和店铺查询当前号码有被当前店铺绑定了别的用户了吗
            List<Integer> bindId = userMapper.findIdByPhoneAndBid((long) bid, bindUser.getPhone());
            if(bindId.size() >= 1){
                throw new NullPointerException("手机号码已经绑定过别的客户了");
            }

        }

        if (bindUser.getTelephone() != null && !bindUser.getTelephone().equals("")){
            //查询备留电话是否有重复的
            List<Integer> teleSize = userMapper.selectTelephone(bid , bindUser.getTelephone());
            if (teleSize.size() >= 1 ){
                throw new NullPointerException("备留联系电话有重复的了");
            }
        }

        //根据类别id和编号查询编号是否存在
        String num = userMapper.findUserByCategoryIdAndNumber(bindUser.getCategory_id(), bindUser.getNumber());

        if (num != null){
            throw new NullPointerException("用户编号已存在");
        }
        bindUser.setBusiness_id(bid);//设值商家ID
        bindUser.setBinding_time(new Date());//绑定时间
        bindUser.setUser_id(uid);//设值绑定用户的注册ID

        //添加用户信息
        Integer count = userMapper.addBinding(bindUser);

        //添加用户积分
        userMapper.addBindingIntegra(bindUser.getId(),new Date());

        //用户的产品提成
        BindUserGoodsIntegra[] goodsintegra = bindUser.getBindUserGoodsIntegras();
        for (BindUserGoodsIntegra integra: goodsintegra  ) {
            if (integra.getGoods_id() != null && integra.getProportion() != null && integra.getGoods_id() != 0 && !"".equals(integra.getProportion())){
                //添加用户的产品积分
                userMapper.addUserGoodsIntegras(bindUser.getId(),integra.getGoods_id(),integra.getProportion());
            }
        }

        //用户的收藏产品
        BindUserGoodsCollection[] goodsCollection = bindUser.getBindUserGoodsCollection();
        for (BindUserGoodsCollection collection: goodsCollection  ) {
            if (collection.getGoods_id() != null && collection.getGoods_id() != 0){
                userMapper.addGoodsCollection(bindUser.getId(),collection.getGoods_id(),new Date());
            }

        }

        //用户的屏蔽产品
        BindUserGoodsShielding[] goodsShielding = bindUser.getBindUserGoodsShielding();
        for (BindUserGoodsShielding shielding: goodsShielding ) {
            if (shielding.getGoods_id() != null && shielding.getGoods_id() != 0){
                //添加用户的屏蔽产品
                userMapper.addGoodsShielding(bindUser.getId(),shielding.getGoods_id(),new Date());
            }

        }
      return count;
    }

    //搜索产品
    @Override
    public List<HashMap> findGoods(String token ,Integer role, String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return userMapper.findGoods(bid,name);
    }

    //搜索员工
    @Override
    public List<HashMap> findStaff(String token ,Integer role , String name)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        return userMapper.findStaff(bid,name);
    }

    //条件查询类别下的客户信息集合
    @Override
    public Page findCustomerInfoList(String token ,Integer role , Parameter parameter)throws NullPointerException {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        int pageNum = 1;
        if (parameter.getPageNo() != null && parameter.getPageNo() != 0){
            pageNum = parameter.getPageNo();
        }
        parameter.setBusiness_id(bid);

        //条件查询类别下的客户信息总数
        Integer totalCount = userMapper.findCustomerInfoListCount(parameter);
        Page page = new Page(pageNum,totalCount);
        parameter.setStartIndex(page.getStartIndex());
        parameter.setPageSize(page.getPageSize());

        //条件查询类别下的客户信息
        List<HashMap> customer = userMapper.findCustomerInfoList(parameter);
        page.setRecords(customer);
        return page;
    }

    //根据用户id查询客户信息
    @Override
    public HashMap findCostomerInfoById(Integer bandingId)throws NullPointerException {

        //通过绑定ID查询绑定的商家信息
        HashMap user = userMapper.findUserInfoById(bandingId);
        if (user == null){
            throw new NullPointerException("用户信息不存在");
        }
        //通过绑定ID查询绑定用户的产品积分
        List<HashMap> integra = userMapper.selectUserIntegraGoods(bandingId);

        //通过绑定ID查询绑定用户的收藏产品
        List<HashMap> collection = userMapper.selectUserCollectionGoods(bandingId);

        //通过绑定ID查询绑定用户的屏蔽产品
        List<HashMap> shielding = userMapper.selectUserShieldingGoods(bandingId);

        user.put("integra",integra);
        user.put("collection",collection);
        user.put("shielding",shielding);
        return user;
    }

    //修改用户信息
    @Override
    @Transactional
    public Integer updateUser(String token ,Integer role ,BindUser bindUser)throws NullPointerException ,Exception {
        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);
        Integer uid = null;
        if (!StringUtils.isEmpty(bindUser.getPhone())){

            //根据用户手机号判断是否存在
            uid = userMapper.findUidByPhone(bindUser.getPhone());
            if (IntegerUtils.isEmpty(uid)){
               throw new NullPointerException("手机号码还未注册");
            }
            //通过用户绑定id查询原来绑定的号码
            String oldPhone = userMapper.getOldPhone(bindUser.getId());
            if(oldPhone != null && oldPhone.equals(bindUser.getPhone())){

                //通过手机号和店铺查询当前号码有被当前店铺绑定了别的用户了吗
                List<Integer> bindId = userMapper.findIdByPhoneAndBid((long) bid, bindUser.getPhone());
                if(bindId.size() > 1){
                    throw new NullPointerException("手机号码已经绑定过别的客户了");
                }
            }else {
                //通过手机号和店铺查询当前号码有被当前店铺绑定了别的用户了吗
                List<Integer> bindId = userMapper.findIdByPhoneAndBid((long) bid, bindUser.getPhone());
                if(bindId.size() >= 1){
                    throw new NullPointerException("手机号码已经绑定过别的客户了");
                }
            }

        }





        if (bindUser.getTelephone() != null && !bindUser.getTelephone().equals("")){


            //查询备留电话是否有重复的
            List<Integer> teleSize = userMapper.selectTelephone(bid  , bindUser.getTelephone());
            if (teleSize.size() > 1 ){
                throw new NullPointerException("备留联系电话有重复的了");
            }
        }

        //通过绑定ID查询用户原来编号
        String number1 = userMapper.findUserNumberByBindingId(bindUser.getId());

        //判断查出来的编号和输入的是否一致
        if (! number1.equals(bindUser.getNumber())){

            //根据类别id和编号查询编号是否存在
            String num = userMapper.findUserByCategoryIdAndNumber(bindUser.getCategory_id(), bindUser.getNumber());
            if (num != null){
                throw new NullPointerException("用户编号已存在");
            }
        }
        bindUser.setUser_id(uid);//设值绑定用户的注册ID

        //通过绑定用户ID修改绑定用户信息
        Integer count = userMapper.updateUser(bindUser);

        //用户的产品积分
        BindUserGoodsIntegra[] goodsIntegra = bindUser.getBindUserGoodsIntegras();
        for (BindUserGoodsIntegra integra: goodsIntegra) {
            if (integra.getGoods_id() != null && integra.getGoods_id() != 0){
                //修改用户的产品积分
                integra.setBinding_id(bindUser.getId());
                userMapper.updateUserGoodsIntegras(integra);
            }
        }

        //用户的收藏产品
        BindUserGoodsCollection[] goodsCollectin = bindUser.getBindUserGoodsCollection();
        for(BindUserGoodsCollection collection :goodsCollectin){
            if (collection.getGoods_id() != null && collection.getGoods_id() != 0){
                //修改用户的产品收藏
                collection.setBinding_id(bindUser.getId());
                collection.setCollection_time(new Date());
                userMapper.updateUserGoodsCollection(collection);
            }

        }
        //用户的屏蔽产品
        BindUserGoodsShielding[] goodsShielding = bindUser.getBindUserGoodsShielding();
        for(BindUserGoodsShielding shielding:goodsShielding){
            if (shielding.getGoods_id() != null && shielding.getGoods_id() != 0){
                //添加用户的产品收藏
                shielding.setBinding_id(bindUser.getId());
                shielding.setShielding_time(new Date());
                userMapper.updateUserGoodsShielding(shielding);
            }
        }
        return count;
    }

    //删除用户
    @Override
    @Transactional
    public Integer deleteUser(Integer id) throws NullPointerException{

        //删除用户之前判断该用户是否有欠款和未付款的订单
        List<Long> status = userMapper.selectBindingOrderStatus((long)id);
        if (status.size() > 0){
            throw new NullPointerException("该用户还有未到货之前的订单");
        }

        //删除用户之前判断该用户是否有欠款和未付款的订单
        List<Integer> oid = userMapper.selectBindingOrder(id);
        if (oid.size() > 0){
            throw new NullPointerException("该用户还有未付款或欠款的订单未结清");
        }


        return userMapper.deleteUser(id);
    }

    //删除用户产品积分
    @Override
    @Transactional
    public Integer deleteUserGoodsIntegra(Integer integraId) {
        return userMapper.deleteUserGoodsIntegra(integraId);
    }

    //删除用户收藏产品
    @Override
    @Transactional
    public Integer deleteUserGoodsCollection(Integer collectionId) {
        return userMapper.deleteUserGoodsCollection(collectionId);
    }

    //删除用户屏蔽产品
    @Override
    @Transactional
    public Integer deleteUserGoodsShielding(Integer shieldingId) {
        return userMapper.deleteUserGoodsShielding(shieldingId);
    }


    //导出用户数据
    @Override
    public HSSFWorkbook excelDownload(String token ,Integer role , Parameter parameter) {

        //通过token和role查询店铺ID
        Integer bid = shopMapper.shopipByTokenAndRole(token, role);

        parameter.setBusiness_id(bid);
        String seetName = "客户资料";//sheet名
        String[] title =new String[]{ "编号", "客户名称","注册账号","权限方案","会员方案","积分","上次销售"};//标题
        List<HashMap> hashMaps = userMapper.excelDownloadCustomerInfoList(parameter);
        String[][] values = new String[hashMaps.size()][];
        for (int i =0;i<hashMaps.size();i++){
            //标签长度和数据长度要一致
            values[i] = new String[title.length];
            HashMap hashMap = hashMaps.get(i);
            values[i][0] = hashMap.get("number").toString();
            values[i][1] = hashMap.get("user_name").toString();
            if (hashMap.get("phone") == null){
                values[i][2] = "暂无";
            }else {
                values[i][2] = hashMap.get("phone").toString();
            }

            if (hashMap.get("source").equals("0")){
                values[i][3] = "线上线下可购买" ;
            }else {
                values[i][3] = "只能线下可购买";
            }

            if (hashMap.get("scheme_name")== null){
                values[i][4] ="默认价格";
            }else {
                values[i][4] =hashMap.get("scheme_name").toString();
            }
            values[i][5] = hashMap.get("integar_num").toString();

            if (hashMap.get("create_time")== null){
                values[i][6] ="无销售记录";
            }else {
                values[i][6] = hashMap.get("create_time").toString();
            }
        }
        HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(seetName, title, values, null);
        return workbook;
    }

    //查询类别下的用户信息集合
    @Override
    public List<HashMap> findUserListtByCategoryId(Integer id) {
        return userMapper.findUserListtByCategoryId(id);
    }

    //注册客户APP账号
    @Override
    @Transactional
    public Integer addUserPhone(String phone) throws NullPointerException ,Exception{

        Integer userPhone = userMapper.findUidByPhone(phone);
        if (userPhone != null ){
            throw new NullPointerException("手机号码已经注册过了");
        }
        String pwd = PasswordMD5.EncoderByMd5("123456" + Global.passwordKey);
        return userMapper.addUserPhone(phone ,pwd , UUID.randomUUID().toString() , new Date());
    }

    @Override
    public List<UserCategoryVO> getUserList(String token, Integer role) {

        //获取店铺id
        Integer businessId = shopMapper.shopipByTokenAndRole(token, role);

        List<UserCategoryVO> userCategoryList = userMapper.getUserCategoryList((long) businessId);

        List<UserVO> userList = userMapper.getUserList((long) businessId);

        userCategoryList.forEach(category -> {

            List<UserVO> userVOS = Lists.newArrayList();

            userList.forEach(user -> {
                if(user.getCategoryId().equals(category.getId())){
                    userVOS.add(user);
                }
            });
            userList.removeAll(userVOS);
            category.setChildren(userVOS);
        });
        return userCategoryList;
    }
}
