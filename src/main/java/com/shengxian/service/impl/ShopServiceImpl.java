package com.shengxian.service.impl;

import com.google.common.collect.Lists;
import com.shengxian.common.util.*;
import com.shengxian.entity.*;
import com.shengxian.mapper.MenuMapper;
import com.shengxian.mapper.PurchaseMapper;
import com.shengxian.mapper.ShopMapper;
import com.shengxian.mapper.UserMapper;
import com.shengxian.service.ShopService;
import com.shengxian.vo.GoodsCategoryVO;
import com.shengxian.vo.GoodsVO;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipException;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2019-03-29
 * @Version: 1.0
 */
@Service
public class ShopServiceImpl implements ShopService {

        @Resource
        private ShopMapper shopMapper;
        @Resource
        private MenuMapper menuMapper;
        @Resource
        private UserMapper userMapper;

        @Resource
        private PurchaseMapper purchaseMapper;



        //店铺注册
        @Override
        @Transactional
        public Integer addBusiness(String storeName, String phone, String password, String invitation) throws NullPointerException, Exception {

            //判断手机号是否在店铺表里注册
            Integer shop_id = shopMapper.phoneWhetherShopRegistered(phone);
            if (shop_id != null) {
                throw new NullPointerException("手机号已经申请过成为商家了");
            }
            Integer staff_id = shopMapper.phoneWhetherStaffRegistered(phone);
            if (staff_id != null) {
                throw new NullPointerException("手机号码已经注册过员工账号了");
            }
            //判断邀请码是否存在
            if (invitation != null && !invitation.equals("")) {
                Integer shop_phone = shopMapper.phoneWhetherShopRegistered(invitation);
                if (shop_phone == null) {
                    Integer staff_phone = shopMapper.phoneWhetherStaffRegistered(invitation);
                    if (staff_phone == null) {
                        throw new NullPointerException("邀请码不存在");
                    }
                }
            }
            //查询最后一条编号
            String number = shopMapper.selectLastNumber();
            if (number == null){
                number = "1001";
            }else {
                number = GroupNumber.getNumber(Integer.valueOf(number));
            }
            //密码加密
            String pwd = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
            //方法重写
            Business business = new Business(phone, pwd,number, storeName, UUID.randomUUID().toString(), invitation, new Date());
            //注册商家
            Integer count = shopMapper.addBusiness(business);

            //添加商家盘点状态
            shopMapper.addClerk(business.getId());

            //添加商家采购模板
            menuMapper.addTemplate(new Template(business.getId(),"采购单","供应商名称","供应商电话",0,"供应商"));
            //添加商家采购退货模板
            menuMapper.addTemplate(new Template(business.getId(),"采购退货单","供应商名称","供应商电话",1,"供应商"));
            //添加商家销售模板
            menuMapper.addTemplate(new Template(business.getId(),"销售单","客户名称","客户电话",2,"客户"));
            //添加商家销售退货模板
            menuMapper.addTemplate(new Template(business.getId(),"销售退货单","客户名称","客户电话",3 ,"客户"));

            //添加店铺模板2
            shopMapper.addTemplateTwo(business.getId() , "采购单" ,0 ,null  ,null);
            shopMapper.addTemplateTwo(business.getId() , "采购退货单" ,1 ,null  ,null);
            shopMapper.addTemplateTwo(business.getId() , "销售单" ,2 ,null  ,null);
            shopMapper.addTemplateTwo(business.getId() , "销售退货单" ,3 ,null  ,null);

            ////添加店铺模板3
            shopMapper.addTemplateThree(business.getId() , "采购单" ,0 ,null  ,null);
            shopMapper.addTemplateThree(business.getId() , "采购退货单" ,1 ,null  ,null);
            shopMapper.addTemplateThree(business.getId() , "销售单" ,2 ,null  ,null);
            shopMapper.addTemplateThree(business.getId() , "销售退货单" ,3 ,null  ,null);

            ////添加店铺模板4
            shopMapper.addTemplateFour(business.getId() , "采购单" ,0 ,null  ,null);
            shopMapper.addTemplateFour(business.getId() , "采购退货单" ,1 ,null  ,null);
            shopMapper.addTemplateFour(business.getId() , "销售单" ,2 ,null  ,null);
            shopMapper.addTemplateFour(business.getId() , "销售退货单" ,3 ,null  ,null);

            ////添加店铺模板5（小票）
            shopMapper.addTemplateFive(business.getId() , "采购单" ,0 ,null  ,null);
            shopMapper.addTemplateFive(business.getId() , "采购退货单" ,1 ,null  ,null);
            shopMapper.addTemplateFive(business.getId() , "销售单" ,2 ,null  ,null);
            shopMapper.addTemplateFive(business.getId() , "销售退货单" ,3 ,null  ,null);

            ////添加店铺模板6
            shopMapper.addTemplateSix(business.getId() , "采购单" ,0 ,null  ,null);
            shopMapper.addTemplateSix(business.getId() , "采购退货单" ,1 ,null  ,null);
            shopMapper.addTemplateSix(business.getId() , "销售单" ,2 ,null  ,null);
            shopMapper.addTemplateSix(business.getId() , "销售退货单" ,3 ,null  ,null);

            //注册商家默认添加15种方案名称
            for (int i = 1;i <16 ; i++){
                //添加商家菜单方案
                shopMapper.addBusinessScheme(i,business.getId(),String.valueOf(i));
            }
            return count;
        }

        //登录
        @Override
        @CacheEvict(value = "demoCache" , allEntries = true)
        public String login(Integer role, String phone, String password) throws  NullPointerException ,Exception{

            //密码加密
            String pwd = PasswordMD5.EncoderByMd5(password + Global.passwordKey);

            //通过手机号和角色查询店铺数据
            HashMap login = shopMapper.login(phone, role);

            if (login == null ){
                throw new NullPointerException("账号不存在");
            }
            //判断密码
            if (!login.get("password").toString() .equals(pwd)){
                throw new NullPointerException("密码不正确");
            }
            //判断员工账号是否审核
            if (Integer.parseInt(login.get("if_adopt").toString()) == 0) {
                throw new NullPointerException("账号未审核，请联系总平台审核通过");
            }
            //判断店铺账号是否禁用
            if (Integer.parseInt(login.get("b_ble").toString()) == 1) {
                throw new NullPointerException("账号已被禁用，如需解冻，请联系总平台管理员");
            }
            //判断员工账号是否禁用
            if (Integer.parseInt(login.get("s_ble").toString()) == 1) {
                throw new NullPointerException("账号已被禁用，如需解冻，请联系店铺管理员");
            }
            //判断店铺使用期限
            if (login.get("duration") == null || Integer.valueOf(login.get("duration").toString()) <= -1) {
                throw new NullPointerException("您的使用期限已到，请联系你的业务经理");
            }

            String token = login.get("token").toString();

            //判断token登录权限
            if (Integer.parseInt(login.get("power").toString()) == 0) { //判断权限0默认一人登录，1多人登录
                token = UUID.randomUUID().toString();
                //修改登录token
                shopMapper.updateLoginToken(Integer.parseInt(login.get("id").toString()),role, token);
            }
            return token;
        }

        //pc端首页
        @Override
        public HashMap home(String token ,Integer role) throws NullPointerException, Exception{

            //通过token和角色查询店铺信息
            HashMap shop = shopMapper.selectShopInfoByIdAndRole(token , role);
            if (shop == null ){
                throw new NullPointerException("您的账号登录失效或在另一台设备登录");
            }
            List<Menu> oneMenu = null;

            if (role == 1 ){
                //商家没权限，获取一级所有的菜单，
                oneMenu = menuMapper.selectOneMenuList();

            }else if(role == 2) {
                //通过员工id查询一级菜单权限
                oneMenu = menuMapper.findOneMenu( Integer.valueOf(shop.get("id").toString()) , 1);

            }
            shop.put("menu", oneMenu);
            return shop;
        }
    @Override
    public HashMap getHome(String token ,Integer role) throws  Exception{

        //通过token和角色查询店铺信息
        HashMap shop = shopMapper.selectShopInfoByIdAndRole(token , role);
        if (shop == null ){
            throw new RuntimeException("您的账号登录失效或在另一台设备登录");
        }
        List<HashMap> imgs = shopMapper.BMJ();
        shop.put("imgs" , imgs);
        return shop;
    }

    @Override
    public Integer getUsefulLlife(String token, Integer role) {
        //通过token和role查询店铺ID
        Integer id = shopMapper.shopipByTokenAndRole(token ,role);
        return shopMapper.getUsefulLlife((long)id);
    }

    //找回密码
        @Override
        @Transactional
        public Integer updateRetrievePwd(String phone, String password) throws NullPointerException, Exception {
            //判断手机号是否存在
            Integer isPhone = shopMapper.phoneWhetherShopRegistered(phone);
            if (isPhone == null) {
                throw new NullPointerException("手机号码未注册");
            }
            String pwd = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
            return shopMapper.updateRetrievePwd(phone, pwd);
        }

        //添加产品类别
        @Override
        @Transactional
        public Integer addGoodsCategory(String token ,Integer role, String name, Integer level) throws NullPointerException{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            String code = null ;

            //通过店铺ID和级别level查询添加所有类别的总数
            Integer categoryCount = shopMapper.selectCategoryCount(id, 0);
            if (categoryCount > 19) {
                throw new NullPointerException("类别最多只能添加20组");
            }

            if (level != null && level == 0 ){

                if (categoryCount == 0 ){
                    code = "1";
                }else {
                    code =  GroupNumber.getNumber(categoryCount);
                }

            }else if (level != null && level != 0){

                //根据一级类别id查询二级类别的最后一条序号
                code = shopMapper.findGoodsCategoryCodeByLevel(level);

                if (code == null ){ // == null 说明还没添加二级类别

                    code = shopMapper.findOneCodeByLevel(level);

                }else {
                    code = GroupNumber.getNumber(Integer.parseInt(code));
                }
            }
            //添加商品类别
            return shopMapper.addGoodsCategory(new GoodsCategory(id, name, level, code));
        }

        //查询类别下的商品信息
        /*@Override
        public List<HashMap> findGoodsCategoryList(String token ,Integer role) throws NullPointerException {
            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //查询店铺一级商品类别
            List<HashMap> oneCagory = shopMapper.findOneCategoryList(id);
            for (HashMap one : oneCagory) {
                List<HashMap> twoCategory = shopMapper.findTwoCategoryList(Integer.parseInt(one.get("id").toString()));
                one.put("twoCategory", twoCategory);
            }
            return oneCagory;
        }*/

    //查询类别下的商品信息
    @Override
    public List<GoodsCategoryVO> findGoodsCategoryList(String token ,Integer role) throws NullPointerException {

        //通过token和role查询店铺ID
        Integer id = shopMapper.shopipByTokenAndRole(token ,role);
        List<GoodsCategoryVO> vos = new ArrayList<>();

        List<GoodsCategoryVO> categoryList = shopMapper.getGoodsCategoryList((long) id);
        //遍历所有的菜单分类
        categoryList.forEach((category) -> {
            if(category.getLevel().equals(0) ){
                vos.add(category);
            }
        });
        //删除根节点
        categoryList.removeAll(vos);
        List<GoodsCategoryVO> gc = new ArrayList<>();
        gc.addAll(categoryList);

        //为根菜单设置子菜单，getClild是递归调用的
        vos.forEach((root) -> {
            //子菜单
            List<GoodsCategoryVO> childList = Lists.newArrayList();
            categoryList.forEach((son) -> {
                if( root.getId().equals((long)son.getLevel())){
                    childList.add(son);
                }
            });
            categoryList.removeAll(childList);
            root.setTwoCategory(childList);//给根节点设置子节点
        });
        return vos;
    }

        //分页查询类别下的产品信息
        @Override
        public Page findGoodsList(String token,Integer role, Integer pageNo, Integer categoryId, String number, String name, Long barcode) throws NullPointerException {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            int paegNum = 1;
            if (IntegerUtils.isNotEmpty(pageNo)) {
                paegNum = pageNo;
            }

            Integer cid = null , level = null ;

            //判断是大类别id还是小类别id
            Integer le = shopMapper.largeClassOrSmalClass(categoryId);
            if (IntegerUtils.isEmpty(le)){ //判断level如果等于0，就代表是大类下的产品
                level = categoryId;
            }else {
                cid = categoryId;
            }

            //判断当前登录的账号是否是员工的
            HashMap shield = purchaseMapper.selectShield(token);

            //分页查询类别下的产品信息总数
            Integer totalCount = shopMapper.findGoodsListCount(new Parameter(id, cid, number, name, barcode,level));
            Page page = new Page(paegNum, totalCount);
            //分页查询类别下的产品信息集合
            List<HashMap> goodsList = shopMapper.findGoodsList(new Parameter(id, page.getStartIndex(), page.getPageSize(), cid, number, name, barcode ,level));
            for (HashMap goods: goodsList){
                if (shield != null && Integer.valueOf(shield.get("min").toString()) == 1){
                    goods.put("min","最低进价不可见");
                }else {
                    goods.put("min","最低进价可见");
                }
                if (shield != null && Integer.valueOf(shield.get("inv").toString()) == 1){
                    goods.put("inv","库存不可见");
                }else {
                    goods.put("inv","库存可见");
                }

            }
            page.setRecords(goodsList);
            return page;
        }

        //修改产品类别
        @Override
        @Transactional
        public Integer updateGoodsCategory(Integer categoryId, String name) {
            return shopMapper.updateGoodsCategory(categoryId, name);
        }

        //删除产品类别
        @Override
        @Transactional
        public Integer deleteGoodsCategory(Integer categoryId) throws NullPointerException ,Exception{
            //根据产品类别id，查询是一级类别还是二级类别
            Integer level = shopMapper.selectTwoCategoryByCategoryId(categoryId);
            if (level == null) {
               throw new NullPointerException("产品类别id不存在");
            }

            //判断级别level字段为 == null或 == 0 时，则要查询是否有二级类别，
            if (level == null || level == 0) {

                //查询二级产品类别
                List<HashMap> categorys = shopMapper.selectCategoryListBylevel(categoryId);

                for (HashMap category : categorys) {

                    //根据产品类别id查询该类别下是否有产品
                    List<HashMap> goods = shopMapper.findGoodsListByCategoryId(Integer.valueOf(category.get("id").toString()));
                    if (goods.size() > 0) {
                        throw new NullPointerException("必须删除类别下面的产品信息，才能删除该类别");
                    }
                }
                //删除二级类别
                shopMapper.deleteTwoGoodsCategory(categoryId);

            } else {

                //根据产品类别id查询该类别下是否有产品
                List<HashMap> goods2 = shopMapper.findGoodsListByCategoryId(categoryId);

                if (goods2.size() > 0) {
                    throw new NullPointerException("必须删除类别下面的产品信息，才能删除该类别");
                }
            }
            //删除产品类别
            return shopMapper.deleteGoodsCategory(categoryId);
        }

        //产品大类别置顶
        @Override
        @Transactional
        public Integer upateCategoryRP(Integer category_id ,Integer status) throws Exception {
            return shopMapper.upateCategoryRP(category_id ,status);
        }

        //挑选产品类别id自动生成产品编号
        @Override
        public String automaticGoodsNumberByCategoryId(Integer categoryId) {
            //查询最后一条产品编号
            String num = shopMapper.findNumberByCategoryId(categoryId);

            if (num == null) {
                //根据类别id查询产品类别序号
                num = (shopMapper.findCodeById(categoryId) + "001" );

            }else{
                num = GroupNumber.getNumber(Integer.parseInt(num));
            }
            return num;
        }

        //查询店铺方案菜单
        @Override
        public List<HashMap> scheme(String token ,Integer role) throws Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //查询店铺方案菜单
            return shopMapper.scheme(id);
        }

        //添加商家菜单方案
        @Override
        @Transactional
        public Integer addBusinessScheme(String token,Integer role , String name) {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //添加商家菜单方案
            return shopMapper.addBusinessScheme(null,id, name);
        }

        //修改商家菜单方案
        @Override
        @Transactional
        public Integer updateBusinessScheme(Integer bsid, String name) throws Exception{
            return shopMapper.updateBusinessScheme(bsid, name);
        }

        //添加产品
        @Override
        @Transactional
        public Integer addGoodsInfo(String token ,Integer role , Goods goods)throws NullPointerException ,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //通过产品类别ID和产品编号查询是否存在
            String num = shopMapper.findNumberByCidAndNum(goods.getCategory_id(), goods.getNumber());

            if (num != null) {
                throw new NullPointerException("产品编号已存在");
            }
            goods.setBusiness_id(id);
            //添加产品
            shopMapper.addGoods(goods);

            //添加图片
            GoodsImg[] goodsImgs = goods.getGoodsImgs();

            if (goodsImgs != null){
                for (GoodsImg img : goodsImgs) {
                    //添加产品轮播图片
                    shopMapper.addGoodsImgs(goods.getId(), img.getImg());
                }
            }
            //产品价格方案
            GoodsScheme[] goodsSchemes = goods.getGoodsSchemes();
            for (GoodsScheme scheme : goodsSchemes) {
                //添加每件产品的十五种方案价格
                shopMapper.addGoodsScheme(goods.getId(), scheme.getScheme_id(), scheme.getPrice());
            }

            //产品库存, 通过库存ID修改库存信息
            GoodsInventory inventories = goods.getGoodsInventories();

            //添加库存信息
            return shopMapper.addInventory(goods.getId(), inventories.getWarehouse_id(), inventories.getMinimum(), inventories.getHighest());

        }

        //通过产品ID查询产品信息（产品资料）
        @Override
        public HashMap findGoodsByGid(String token  ,Integer role  ,Integer goodsId) throws NullPointerException {


            //通过ID查询产品信息
            HashMap goods = shopMapper.findGoodsByGid(goodsId);

            if (goods == null) {
                throw new NullPointerException(" 产品ID不存在");
            }

            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap shield = purchaseMapper.selectShield(token);
            if (shield != null && Integer.valueOf(shield.get("shield").toString()) == 1){
                goods.put("cost","进价不可见");
            }else {
                goods.put("cost","进价可见");
            }
            if (shield != null && Integer.valueOf(shield.get("min").toString()) == 1){
                goods.put("min","最低进价不可见");
            }else {
                goods.put("min","最低进价可见");
            }
            //通过产品ID查询产品轮播图片
            List<HashMap> goodsImg = shopMapper.findGoodsImgByGid(goodsId);

            //通过产品ID查询产品价格方案
            List<HashMap> goodsScheme = shopMapper.findGoodsSchemeByGid(goodsId);

            goods.put("goodsImg", goodsImg);
            goods.put("goodsScheme", goodsScheme);
            return goods;
        }
        //通过产品ID查询产品信息
        @Override
        public HashMap goodsInfo(String token, Integer role, Integer goodsId) throws NullPointerException {
            //通过ID查询产品信息
            HashMap goods = shopMapper.goodsInfo(goodsId);

            if (goods == null) {
                throw new NullPointerException("产品ID不存在");
            }

            //查询当前登录的是否是员工账号,是则判断进价是否屏蔽
            HashMap shield = purchaseMapper.selectShield(token);
            if (shield != null && Integer.valueOf(shield.get("min").toString()) == 1){
                goods.put("min","最低进价不可见");
            }else {
                goods.put("min","最低进价可见");
            }
            if (shield != null && Integer.valueOf(shield.get("shield").toString()) == 1){
                goods.put("cost","进价不可见");
            }else {
                goods.put("cost","进价可见");
            }

            if (shield != null && Integer.valueOf(shield.get("inv").toString()) == 1){
                goods.put("inv","库存不可见");
            }else {
                goods.put("inv","库存可见");
            }


            return goods;
        }

        //修改产品信息
        @Override
        @Transactional
        public Integer updateGoods(Goods goods) throws NullPointerException{

            //通过产品ID查询产品原来编号
            String num = shopMapper.findNumberByGid(goods.getId());

            //判断原来的和输入的编号是否一致,不一致则要查询编号是否存在
            if (!num.equals(goods.getNumber())) {

                //通过产品类别ID和输入的产品编号查询是否存在
                String number = shopMapper.findNumberByCidAndNum( goods.getCategory_id() , goods.getNumber());

                if (number != null) {
                   throw new NullPointerException("修改的产品编号已存在");
                }
            }

            //判断上架的产品是否需要审核
            if (goods.getStatus() == 1){

                //通过产品id查询产品主要的资料信息
                HashMap hashMap = shopMapper.selectGoodsInfoById(goods.getId());

                //获取第一张图片
                GoodsImg[] imgs = goods.getGoodsImgs();
                for (int i = 0;i <1;i++){

                    //如果原来的产品名称和图片没有变动过的话，则直接通过
                    if (hashMap.get("name").toString().equals(goods.getName())  && hashMap.get("img").toString().equals(imgs[i].getImg())){
                        goods.setStatus(2);
                    }
                }
            }

            //修改产品信息
            shopMapper.updateGoodsInfo(goods);

            //修改图片
            GoodsImg[] goodsImgs = goods.getGoodsImgs();
            if (goodsImgs != null){
                for (GoodsImg img : goodsImgs) {

                    //修改产品轮播图片
                    if (img.getId() != null) {
                        shopMapper.updateGoodsImgs(img.getId(), img.getImg());
                    } else {
                        shopMapper.addGoodsImgs(goods.getId(), img.getImg());
                    }
                }
            }
            //产品价格方案
            GoodsScheme[] goodsSchemes = goods.getGoodsSchemes();
            for (GoodsScheme scheme : goodsSchemes) {
                //通过产品方案ID修改每件产品的方案价格
                shopMapper.updateGoodsScheme(scheme.getId(), scheme.getPrice());
            }

            //通过库存ID修改库存信息
            GoodsInventory inventories = goods.getGoodsInventories();

            //通过库存ID修改库存信息
            shopMapper.updateInventory(inventories.getId(), inventories.getWarehouse_id(), inventories.getMinimum(), inventories.getHighest());

            //修改产品其它信息
            return  shopMapper.updateGoods(goods);
        }

        //删除产品信息
        @Override
        @Transactional
        public Integer deleGoods(Integer id) throws NullPointerException{

            //通过产品id查询产品是否处于上架状态
            Integer status = shopMapper.goodsStatus(id);
            if (status == 2){
                throw new NullPointerException("要先下架产品才能删除哟～");
            }
            //查询产品是否还有未到货的订单
            List<Long> isExistStatus = shopMapper.noReceivablesOrderStatus((long)id);
            if(isExistStatus.size() > 0){
                throw new NullPointerException("产品还有存在未到货之前的订单，不能删除");
            }

            //查询产品是否还有未收款的订单
            List<Long> isExistState = shopMapper.noReceivablesOrderState((long)id);
            if(isExistState.size() > 0){
                throw new NullPointerException("产品还有存在未收款的订单，不能删除");
            }

            //产品还有存在采购未审核的订单
            List<Long> iStatue = shopMapper.noReceivablesPuchaseStatus((long)id);
            if(iStatue.size() > 0){
                throw new NullPointerException("产品还有存在采购未审核的订单，不能删除");
            }

            //产品还有存在采购未审核的订单
            List<Long> iState = shopMapper.noReceivablesPuchaseState((long)id);
            if(iState.size() > 0){
                throw new NullPointerException("产品还有存在采购未付款的订单，不能删除");
            }
            //通过产品id查询产品库存是否大于0以上的
            Integer vid = shopMapper.goodsInventory(id);
            if (vid != null ){
                throw new NullPointerException("产品还有库存，不能删除");
            }



            return shopMapper.deleGoods(id);
        }

        //产品上架
        @Override
        @Transactional
        public Integer isUpper(Integer goodsId) {
            return shopMapper.updateStatusByGid(goodsId, 2);
        }

        //产品下架
        @Override
        @Transactional
        public Integer isLower(Integer goodsId) {
            return shopMapper.updateStatusByGid(goodsId, 4);
        }


        //删除产品图片
        @Override
        @Transactional
        public Integer deleteGoodsImg(Integer id) {
            return shopMapper.deleteGoodsImg(id);
        }

        //修改产品在APP上排序的优先级
        @Override
        @Transactional
        public Integer updateGoodsPriority(Integer goods_id, Integer priority) {
            return shopMapper.updateGoodsPriority(goods_id, priority);
        }

        //分页查询总平台推送给商家的信息
        @Override
        public Page findPushBusinessMessage(String token , Integer role, Integer pageNo) {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            int pageNum = 1;
            if (IntegerUtils.isNotEmpty(pageNo)) {
                pageNum = pageNo;
            }

            //查询总平台推送给商家的信息总数
            Integer tatolCount = shopMapper.findPushBusinessMessageCount(id);
            Page page = new Page(pageNum, tatolCount);
            //查询总平台推送给商家的信息
            List<HashMap> message = shopMapper.findPushBusinessMessage(id, page.getStartIndex(), page.getPageSize());
            page.setRecords(message);
            return page;
        }

        //添加积分产品类别
        @Override
        @Transactional
        public Integer addIntegrGoodsCategory(String token ,Integer role, String name )throws NullPointerException ,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            String code = null;

            //查询商家添加积分产品的类别总数
            Integer count = shopMapper.selectIntegrGoodsCategoryCount(id);
            if (count > 19) {
                throw new NullPointerException("积分产品类别不能超过19组");
            }
            if (count == 0) {
                code = "1";
            } else {
                code = GroupNumber.getNumber(count);
            }
            return shopMapper.addIntegrGoodsCategory(new GoodsCategory(id, name,  code));
        }

        //查询店铺下的积分产品类别名称
        @Override
        public List<HashMap> selectBusinessIntegrGoodsCategory(String token ,Integer role) throws NullPointerException {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //查询积分产品一级大类别
            return shopMapper.selectBusinessIntegrGoodsCategory(id);
        }

        //分页查询店铺积分产品信息
        @Override
        public Page selectIntegrGoodsList(String token ,Integer role, Integer pageNo, Integer categoryId, String number, String name, Long barcode) throws NullPointerException {
            int pageNum = 1;
            if (IntegerUtils.isNotEmpty(pageNo)) {
                pageNum = pageNo;
            }

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            Integer tatolCount = shopMapper.selectIntegrGoodsListCount(new Parameter(id, categoryId, number, name, barcode));
            Page page = new Page(pageNum, tatolCount);
            List<HashMap> hashMaps = shopMapper.selectIntegrGoodsList(new Parameter(id, categoryId, number, name, barcode ,page.getStartIndex() ,page.getPageSize()));
            page.setRecords(hashMaps);
            return page;
        }

        //通过积分产品类别id查询类别名称
        @Override
        public HashMap selectIntegrGoodsCategory(Integer id) {
            return shopMapper.selectIntegrGoodsCategory(id);
        }

        //修改积分产品类别名称
        @Override
        @Transactional
        public Integer updateIntegrGoodsCategory(Integer id, String name) {
            return shopMapper.updateIntegrGoodsCategory(id, name);
        }

        //删除积分产品类别
        @Override
        @Transactional
        public Integer deleteIntegrGoodsCategory(Integer id) {

            //通过类别id查询该类别下是否有积分产品信息
            List<HashMap> hashMap = shopMapper.selectIntegrGoodsInfo(id);
            if (hashMap.size() > 0) {
                return -1;
            }
            return shopMapper.deleteIntegrGoodsCategory(id);
        }

        //自动生成积分产品编号
        @Override
        public String autoMakeIntegrGoodsNumber(Integer category_id) {

            //查询类别下积分产品最后一条产品编号
            String num = shopMapper.selectIntegrGoodsLastNumber(category_id);
            if (num == null) {
                //通过积分产品类别id查询类别序号
                num  = ( shopMapper.selectIntegrGoodsCode(category_id) + "0001");
            } else {
                num = GroupNumber.getNumber(Integer.parseInt(num));
            }
            return num;
        }

        //添加积分产品
        @Override
        @Transactional
        public Integer addIntegrGoods(String token ,Integer role, IntegrGoods integrGoods)throws NullPointerException ,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token ,role);

            //通过积分产品类别ID和积分产品编号查询编号是否存在
            String num = shopMapper.selelctIntegrGoodsNumberByCidAndNum(integrGoods.getCategory_id(), integrGoods.getNumber());
            if (num != null) {
               throw new NullPointerException("您输入的积分产品编号已存在");
            }

            integrGoods.setBusiness_id(id);
            //添加积分产品信息
            return shopMapper.addIntegrGoods(integrGoods);

        }

        //通过积分产品id查询产品信息
        @Override
        public HashMap selectIntegrGoodsInfoById(Integer id) {
            return shopMapper.selectIntegrGoodsInfoById(id);
        }

        //修改积分产品信息
        @Override
        @Transactional
        public Integer updateIntegrGoodsInfo(IntegrGoods integrGoods)throws NullPointerException ,Exception {

            //通积分产品id查询原来的编号
            String number = shopMapper.selectIntegrGoodsNumberById(integrGoods.getId());

            //判断输入的是否一致
            if (!number.equals(integrGoods.getNumber())) {

                //通过积分产品类别ID和积分产品编号查询编号是否存在
                String num = shopMapper.selelctIntegrGoodsNumberByCidAndNum(integrGoods.getCategory_id(), integrGoods.getNumber());
                if (num != null) {
                   throw new NullPointerException("您输入的产品编号已存在");
                }
            }
            //修改积分产品信息
            return shopMapper.updateIntegrGoodsInfo(integrGoods);

        }

        //删除积分产品
        @Override
        @Transactional
        public Integer deleteIntegrGoods(Integer id)throws NullPointerException {

            //判断积分产品是否存在正在兑换的订单
            List<Integer> order_id = shopMapper.selectIntegerOrder(id);
            if (order_id.size() >0 ){
                throw new NullPointerException("此产品有正在兑换订单，不能删除");
            }
            //删除积分产品
            return shopMapper.deleteIntegrGoods(id);

        }

        //获取商家二维码
        @Override
        public Integer findBusinessTDCode(String token  ,Integer role) {

            //通过token和role查询店铺ID
           return shopMapper.shopipByTokenAndRole(token ,role);
        }

        //用户申请绑定商家
        @Override
        @Transactional
        public Integer addBindingApplication(String phone, Integer business_id) {
            //查询用户手机号是否注册过
            Integer uid = userMapper.findUidByPhone(phone);
            if (uid == null){
                return -1;
            }
            return shopMapper.addBindingApplication(uid,business_id,new Date());
        }

        //分享有奖二维码
        @Override
        public HashMap sharePhone(String token ,Integer role) {

            //分享有奖二维码
            return  shopMapper.sharePhone(token,role);
        }

        //根据手机号查询邀请商家注册的信息
        @Override
        public List<HashMap> findsharePhone(String phone, Integer open) {
            return shopMapper.findsharePhone(phone,open);
        }

        //积分产品下架
        @Override
        @Transactional
        public Integer InventoryGoodsIsLower(Integer gooods_id) {
            return shopMapper.InventoryGoodsIsLower(gooods_id,4);
        }

        //获取pc端系统公告
        @Override
        public HashMap systemBulletin() {
            return shopMapper.systemBulletin();
        }

        //获取电子协议
        @Override
        public HashMap agreement() throws Exception{
            return shopMapper.agreement();
        }

        //查询类别下的产品信息集合
        @Override
        public List<HashMap> findGoodsListByCategoryId(Integer id) {
            return shopMapper.findGoodsListByCategoryId(id);
        }


        //商家设置
        @Override
        public HashMap businessSet(String token  ,Integer role) {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            //通过商家id查询商家信息
            return shopMapper.findBusinessInfo(id);
        }

        //修改商家设置
        @Override
        @Transactional
        public Integer updateBusinessSetting(Business business)throws Exception {
            return shopMapper.updateBusinessSetting(business);
        }


        //查询用户的推荐产品
        @Override
        public List<HashMap> selectUserRecommendGoods(String token, Integer role ,Integer binding_id) throws NullPointerException ,Exception{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            //查询用户的推荐产品
            return shopMapper.selectUserRecommendGoods(id,binding_id);
        }


        //添加用户的推荐产品
        @Override
        @Transactional
        public Integer addUserRecommendGoods(Integer binding_id, Recommend[] recommend)throws Exception {

            Integer count = null;
            for (Recommend rec: recommend  ) {

                //查询是否已经推荐产品给用户了
                Integer uid = shopMapper.isRecommendGoodsGaveUser( rec.getGoods_id(),binding_id);
                if (uid != null){
                    throw new NullPointerException("产品已经推荐给用户了");
                }

                //添加推荐产品
                rec.setBinding_id(binding_id);
                rec.setCreate_time(new Date());
                count = shopMapper.addUserRecommendGoods(rec);
            }
            return count;
        }

        //下架或上传推荐产品
        @Override
        @Transactional
        public Integer updateUserRecommendGoods(Integer id, Integer is_upload)throws Exception {
            return shopMapper.updateUserRecommendGoods(id,is_upload);
        }

        //删除推荐产品
        @Override
        @Transactional
        public Integer deteleUserRecommendGoods(Integer id) throws Exception{
            return shopMapper.deteleUserRecommendGoods(id);
        }

        //查询限购产品
        @Override
        public List<HashMap> selectRestrictionGoods(String token  ,Integer role , String name) throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            //查询限购产品
            return shopMapper.selectRestrictionGoods(id,name);
        }

        //搜索未限购的产品
        @Override
        public List<HashMap> searchNotRestrictionGoods(String token ,Integer role, String name ,Integer goods_id,Integer category_id) throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.searchNotRestrictionGoods(id,name,goods_id,category_id);
        }

        //通过产品id查询产品信息
        @Override
        public List<HashMap> selectGoodsInfoByGid(String goods_id) throws Exception{

            List<HashMap> list= new ArrayList<HashMap>();
            String[] gid = goods_id.split(",");
            for (String id:gid  ) {

                HashMap hashMap = shopMapper.selectGoodsInfoByGid(Integer.valueOf(id));
                list.add(hashMap);
            }
            return list;
        }

        //添加限购产品
        @Override
        @Transactional
        public Integer addRestrictionGoods(String token ,Integer role , Integer goods_id, Double num) throws NullPointerException, Exception{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            //通过产品id查询是否添加限购了
            Integer count = shopMapper.isAddRestriction(goods_id);
            if (count != null){
                throw new NullPointerException("该产品已经添加限购了");
            }
            return shopMapper.addRestrictionGoods(id,goods_id,num,new Date());
        }

        //删除限购产品
        @Override
        @Transactional
        public Integer deleteRestrictionGoods(Integer id)throws Exception {
            return shopMapper.deleteRestrictionGoods(id);
        }

        //添加优惠券
        @Override
        @Transactional
        public Integer addCoupon(String token ,Integer role, Coupon coupon) throws Exception{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            coupon.setBusiness_id(id);
            coupon.setCreate_time(new Date());
            return shopMapper.addCoupon(coupon);
        }

        //查询优惠券
        @Override
        public List<HashMap> selectAllCoupon(String token ,Integer role, String name)throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.selectAllCoupon(id,name);
        }

        //通过id查询优惠券信息
        @Override
        public HashMap selectCouponById(Integer id) throws Exception{
            return shopMapper.selectCouponById(id);
        }

        //修改优惠券
        @Override
        @Transactional
        public Integer updateCoupon(Coupon coupon)throws Exception {
            coupon.setCreate_time(new Date());
            return shopMapper.updateCoupon(coupon);
        }

        //删除优惠券
        @Override
        @Transactional
        public Integer deleteCoupon(Integer id) throws NullPointerException  ,Exception{

            //通过优惠券id查询优惠券是否派送给客户过(防止客户的优惠券未使用或未过期)
            List<Integer> coupon_id = shopMapper.bindingCouponList(id);
            if (coupon_id.size() > 0 ){
                throw new NullPointerException("客户有此优惠券，等使用或过期了才能删除");
            }
            return shopMapper.deleteCoupon(id);
        }

        //用户优惠券
        @Override
        public List<HashMap> bindingCoupon(String token ,Integer role ,Integer binding_id) throws NullPointerException ,Exception{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.bindingCoupon(id ,binding_id);
        }


        //派送优惠券给用户
        @Override
        @Transactional
        public Integer addBindingCoupon(Coupon coupon)throws Exception {

            //通过优惠券id查询优惠券信息
            HashMap hashMap = shopMapper.selectCouponById(coupon.getCoupon_id());

            coupon.setFull(Double.valueOf(hashMap.get("full").toString()));//满
            coupon.setReduce(Double.valueOf(hashMap.get("reduce").toString()));//减

            return shopMapper.addBindingCoupon(coupon);
        }

        //添加满赠产品
        @Override
        @Transactional
        public Integer addFullBestowGoods(String token ,Integer role ,Integer goods_id, Double full, Double bestow) throws NullPointerException, Exception{

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.addFullBestowGoods(id,goods_id,full,bestow,new Date());
        }


        //搜索未添加满赠的产品
        @Override
        public List<HashMap> searchNotFullBestowGoods(String token ,Integer role, String name,Integer category_id) throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.searchNotFullBestowGoods(id,name ,category_id);
        }

        //查询已添加满赠的产品
        @Override
        public List<HashMap> selectAddFullBestowGoods(String token ,Integer role , String name) throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer id = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.selectAddFullBestowGoods(id,name);
        }

        //删除满赠产品
        @Override
        @Transactional
        public Integer deleteFullBestowGoods(Integer id) throws Exception{
            return shopMapper.deleteFullBestowGoods(id);
        }

        //用户积分赠送
        @Override
        @Transactional
        public Integer addGiveBindingIntegra(Integra integra)throws NullPointerException , Exception {

            //通过用户id查询用户积分id
            Integer integra_id = shopMapper.selectBindingIntegraId(integra.getBinding_id());

            if (integra_id == null){
                throw new NullPointerException("没有用户积分id");
            }

            if (integra.getType() == 4){//赠积分

                shopMapper.updateBindingIntegra(integra_id,integra.getIntegra());

            }else {//减积分
                shopMapper.updateUserIntegra(integra_id,integra.getIntegra());
            }

            //添加赠送用户积分明细
            integra.setIntegra_id(integra_id);
            integra.setCreate_time(new Date());
            return  shopMapper.addGiveBindingIntegra(integra);
        }

        //查询用户积分
        @Override
        public Page selectUserIntegra(String token ,Integer role , Integer pageNo, Integer id) throws NullPointerException, Exception {

            int pageNum =1;
            if (IntegerUtils.isNotEmpty(pageNo)){
                pageNum =pageNo;
            }

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            //查询用户积分总数
            Integer totalCount = shopMapper.selectUserIntegraCount(bid,id);
            Page page = new Page(pageNum,totalCount);
            //查询用户积分
            List<HashMap> hashMaps = shopMapper.selectUserIntegra(bid,id,page.getStartIndex(),page.getPageSize());
            page.setRecords(hashMaps);
            return page;
        }

        //查询赠送用户积分列表（只保存三天记录）
        @Override
        public List<HashMap> selectBindingIntegraGive(Integer id) throws Exception{
            return shopMapper.selectBindingIntegraGive(id);
        }

        //添加限时秒杀
        @Override
        @Transactional
        public Integer addLimitedSeckill(String token  ,Integer role, Seckill seckill)throws Exception {

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            seckill.setBusiness_id(bid);
            //添加限时秒杀
            return  shopMapper.addLimitedSeckill(seckill);
        }

        //查询限时秒杀时间段
        @Override
        public List<HashMap> selectLimitedSeckill(String token  ,Integer role )throws NullPointerException,Exception{

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.selectLimitedSeckill(bid);
        }

        //更新秒杀时间段
        @Override
        @Transactional
        public Integer updateLimitedSeckill(Seckill seckill)throws Exception {
            return shopMapper.updateLimitedSeckill(seckill);
        }

        //删除秒杀时间段
        @Override
        @Transactional
        public Integer deleteLimitedSeckill(Integer id)throws Exception {

            Integer count =  shopMapper.deleteLimitedSeckill(id);//删除秒杀时间段

            //查询秒杀时间段下是否有产品
            List<Integer> seckillId = shopMapper.limitedSeckillGoods(id);
            if (seckillId.size() > 0){

                shopMapper.deleteLimitedSeckillGoods(id);//删除秒杀产品
            }
            return count;
        }

        //查询时间段下的秒杀产品
        @Override
        public List<HashMap> selectLimitedSeckillGoods(Integer id)throws Exception {
            return shopMapper.selectLimitedSeckillGoods(id);
        }

        //搜索未在同一时间段秒杀的产品
        @Override
        public List<HashMap> notLimitedSeckillGoods(String token , Integer role, Integer seckill_id, String name)throws NullPointerException,Exception {

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.notLimitedSeckillGoods(bid,seckill_id,name);
        }

        //添加限时秒杀产品
        @Override
        @Transactional
        public Integer addLimitedSeckillGoods( SeckillDetail seckillDetail)throws Exception {
            //添加限时秒杀产品
            return shopMapper.addLimiteSeckillGoods(seckillDetail);
        }

        //更换秒杀产品
        @Override
        @Transactional
        public Integer updateLimitedSeckillGoods(SeckillDetail seckillDetail)throws Exception {
            return shopMapper.updateLimitedSeckillGoods(seckillDetail);
        }

        //删除秒杀产品
        @Override
        @Transactional
        public Integer deteleLimitedSeckillGoods(Integer id)throws Exception {
            return shopMapper.deteleLimitedSeckillGoods(id);
        }


        //添加店铺满减活动
        @Override
        @Transactional
        public Integer addFullReductionActivity(String token  ,Integer role, Seckill seckill) {

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            //添加店铺ID
            seckill.setBusiness_id(bid);
            return shopMapper.addFullReductionActivity(seckill);
        }

        //查询满减活动
        @Override
        public List<HashMap> selectFullReductionActivity(String token ,Integer role ) throws NullPointerException ,Exception{

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.selectFullReductionActivity(bid);
        }

        //修改满减活动
        @Override
        public Integer updateFullReductionActivity(Seckill seckill)throws Exception {
            return shopMapper.updateFullReductionActivity(seckill);
        }

        //删除满减活动
        @Override
        @Transactional
        public Integer deleteFullReductionActivity(Integer id)throws Exception {
            return shopMapper.deleteFullReductionActivity(id);
        }


        //判断是否是商家登录的
        @Override
        public boolean isBuseinssToken(String token ) throws Exception {

            Integer bid = shopMapper.findIdByBusinessToken(token);
            if (bid != null && !bid.equals(0)){
                return true;
            }
            return false;
        }

        //修改商家密码
        @Override
        @Transactional
        public Integer udpateBusinessPassword(String token, String password) throws Exception {
            Integer id = shopMapper.findIdByBusinessToken(token);
            String pwd = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
            return shopMapper.udpateBusinessPassword(id,pwd);
        }

        //查询第一产品类别
        @Override
        public List<HashMap> oneCategory(String token ,Integer role) throws NullPointerException, Exception {

            //通过token和role查询店铺ID
            Integer bid = shopMapper.shopipByTokenAndRole(token, role);

            return shopMapper.oneCategory(bid);
        }

        //查询第二产品类别
        @Override
        public List<HashMap> twoCategory(Integer id) throws Exception {
            return shopMapper.twoCategory(id);
        }

        //导出产品资料
        @Override
        public HSSFWorkbook excelDownload(String token ,Integer role , Parameter parameter) {
            Integer bid = shopMapper.businessIdByToken(token);
            if (bid == null) {
                throw new NullPointerException("您的账号登录失效或在另一台设备登录");
            }
            String sheetName = "产品资料";//sheet名
            String[] title = new String[]{"产品编号", "条形码", "产品名称", "最低售价", "实际库存", "虚拟出库", "状态"};//标题

            parameter.setBusiness_id(bid);
            List<HashMap> goods = shopMapper.excelDownload(parameter);
            String[][] values = new String[goods.size()][];
            for (int i = 0; i < goods.size(); i++) {
                values[i] = new String[title.length];
                HashMap hashMap = goods.get(i);
                values[i][0] = hashMap.get("number").toString();
                if ("0".equals( hashMap.get("barcode").toString())){
                    values[i][1] = "未录入条形码";
                }else {
                    values[i][1] =hashMap.get("barcode").toString();
                }
                values[i][2] = hashMap.get("name").toString();
                values[i][3] = hashMap.get("minimum_price").toString();
                values[i][4] = hashMap.get("actual").toString();
                values[i][5] = hashMap.get("fictitious").toString();
                if ("0".equals(hashMap.get("status").toString())){
                    values[i][6] = "保存";
                }else if("1".equals(hashMap.get("status").toString())){
                    values[i][6] = "审核中";
                }else if("2".equals(hashMap.get("status").toString())){
                    values[i][6] = "通过";
                }else if("3".equals(hashMap.get("status").toString())){
                    values[i][6] = "未通过";
                }else if("4".equals(hashMap.get("status").toString())){
                    values[i][6] = "下架";
                }
            }
            HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(sheetName, title, values, null);
            return workbook;

        }


        //导出积分产品资料
        @Override
        public HSSFWorkbook IntegrGoodsexcelDownload(String token ,Integer role, Integer categoryId, String number, String name, Long barcode) {
            Integer bid = shopMapper.businessIdByToken(token);
            if (bid == null) {
                throw new NullPointerException("您的账号登录失效或在另一台设备登录");
            }
            String sheetName = "积分产品资料";//sheet名
            String[] title = new String[]{"类别", "编号", "名称", "规格", "积分兑换值" ,"库存"};//标题

            List<HashMap> goods = shopMapper.IntegrGoodsexcelDownload(bid,categoryId,number,name,barcode);
            String[][] values = new String[goods.size()][];
            for (int i = 0; i < goods.size(); i++) {
                values[i] = new String[title.length];
                HashMap hashMap = goods.get(i);
                values[i][0] = hashMap.get("categoryName").toString();
                if (hashMap.get("barcode") == null){
                    values[i][1] ="";
                }else {
                    values[i][1] =hashMap.get("barcode").toString();
                }

                values[i][2] = hashMap.get("name").toString();
                values[i][3] = hashMap.get("spec").toString();
                values[i][4] = hashMap.get("integr_price").toString();
                values[i][5] = hashMap.get("actual").toString();
            }
            HSSFWorkbook workbook = ExcelUtil.getHSSWorkbook(sheetName, title, values, null);
            return workbook;

        }

        //匹配店铺密码
        @Override
        public boolean businessPassword(String token ,Integer role , String password) throws NullPointerException, Exception {

            String pwd = shopMapper.businessPassword(token ,role);

            String p = PasswordMD5.EncoderByMd5(password + Global.passwordKey);
            if (pwd.equals(p)){
                return  true;
            }
            return false;
        }

        //轮播图
        @Override
        public List<HashMap> BMJ() {
            return shopMapper.BMJ();
        }

        //产品二维码
        @Override
        public HashMap goodsOR(String token, Integer role) {
            //通过token和角色查询店铺id和产品二维码识别码
            Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
            HashMap hashMap = shopMapper.goodsOR(business_id);
            if (hashMap != null && hashMap.get("icode") == null){
                int icode = (int) ((Math.random() * 4 + 1) * 1000);
                shopMapper.refreshGoodsORIdentificationCode(business_id , icode);
                hashMap.put("icode" , icode );
            }
            return hashMap;
        }

        //刷新产品二维码识别码
        @Override
        @Transactional
        public Integer refreshGoodsORIdentificationCode(String token, Integer role) {
            Integer business_id = shopMapper.shopipByTokenAndRole(token, role);
            int icode = (int) ((Math.random() * 4 + 1) * 1000);
            return shopMapper.refreshGoodsORIdentificationCode(business_id , icode);
        }

    @Override
    public List<GoodsCategoryVO> getGoodsList(String token, Integer role) {


        Integer businessId = shopMapper.shopipByTokenAndRole(token, role);

        List<GoodsCategoryVO> vos = new ArrayList<>();

        List<GoodsCategoryVO> goodsCategoryList = shopMapper.getGoodsCategoryList((long) businessId);

        List<GoodsVO> goodsList = shopMapper.getGoodsList((long) businessId);

        //遍历所有的菜单分类
        goodsCategoryList.forEach((category) -> {
            if(category.getLevel().equals(0) ){
                vos.add(category);
            }
        });
        //删除根节点
        goodsCategoryList.removeAll(vos);
        //为根菜单设置子菜单，getClild是递归调用的
        vos.forEach((root) -> {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<GoodsCategoryVO> childList = getChild(root.getId(), goodsCategoryList , goodsList);
            root.setChildren(childList);//给根节点设置子节点
        });
        return vos;
    }


    /**
     * 获取子节点
     * @param id 父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<GoodsCategoryVO> getChild(Long id,List<GoodsCategoryVO> allMenu , List<GoodsVO> goodsList){
        //子菜单
        List<GoodsCategoryVO> childList = Lists.newArrayList();


        allMenu.forEach((son) -> {
            List<GoodsVO> gvos = Lists.newArrayList();
            if( id.equals((long)son.getLevel())){
                childList.add(son);

                goodsList.forEach((goods) -> {
                    if(son.getId().equals(goods.getCategoryId())){
                       gvos.add(goods);
                    }
                });
                goodsList.removeAll(gvos);
                son.setgChildren(gvos);

            }
        });


        if(childList.size() == 0){
            return new ArrayList<GoodsCategoryVO>();
        }
        allMenu.removeAll(childList);
        return childList;
    }


    @Override
    public List<GoodsCategoryVO> getCategroyList(String token, Integer role) {

        List<GoodsCategoryVO> vos = new ArrayList<>();
        Integer businessId = shopMapper.shopipByTokenAndRole(token, role);
        GoodsCategoryVO cate = new GoodsCategoryVO();
        cate.setId(null);
        cate.setName("全部");
        vos.add(cate);

        List<GoodsCategoryVO> goodsCategoryList = shopMapper.getGoodsCategoryList((long) businessId);



        //遍历所有的菜单分类
        goodsCategoryList.forEach((category) -> {
            if(category.getLevel().equals(0) ){
                vos.add(category);
            }
        });
        //删除根节点
        goodsCategoryList.removeAll(vos);
        List<GoodsCategoryVO> gc = new ArrayList<>();
        gc.addAll(goodsCategoryList);

        //为根菜单设置子菜单，getClild是递归调用的
        vos.forEach((root) -> {
            if(root.getId()== null && root.getName().equals("全部")){
                root.setChildren(gc);
            }else {
                /* 获取根节点下的所有子节点 使用getChild方法*/
                List<GoodsCategoryVO> childList = getCategoryChild(root.getId(), goodsCategoryList );
                root.setChildren(childList);//给根节点设置子节点
            }

        });
        return vos;
    }

    /**
     * 获取子节点
     * @param id 父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<GoodsCategoryVO> getCategoryChild(Long id,List<GoodsCategoryVO> allMenu ){
        //子菜单
        List<GoodsCategoryVO> childList = Lists.newArrayList();


        allMenu.forEach((son) -> {
            List<GoodsVO> gvos = Lists.newArrayList();
            if( id.equals((long)son.getLevel())){
                childList.add(son);
                son.setgChildren(gvos);
            }
        });


        if(childList.size() == 0){
            return new ArrayList<GoodsCategoryVO>();
        }
        allMenu.removeAll(childList);
        return childList;
    }

}
