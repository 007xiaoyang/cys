package com.shengxian.mapper;

import com.shengxian.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 店铺mapper层
 *
 * @Author: yang
 * @Date: 2018/8/21
 * @Version: 1.0
 */
@Mapper
public interface ShopMapper {


    /**
     * 拦截器查询店铺信息
     * @param token 登录token
     * @param role 1店铺，2员工
     * @return
     */
    HashMap interceptorSelectShopInfo(@Param("token") String token ,@Param("role") Integer role);

    /**
     * 手机号是否在店铺表里在注册过
     * @param phone
     * @return
     */
    Integer phoneWhetherShopRegistered(@Param("phone") String phone);

    /**
     * 手机号是否在店铺表里在注册过
     * @param phone
     * @return
     */
    Integer phoneWhetherStaffRegistered(@Param("phone") String phone);

    /**
     * 查询最后一条店铺编号
     * @return
     */
    String selectLastNumber();

    /**
     * 店铺注册
     * @param business
     * @param
     * @return
     */
    Integer addBusiness(Business business);

    /**
     * 添加商家盘点
     * @param business_id
     * @return
     */
    Integer addClerk(@Param("business_id") Integer business_id);

    /**
     * 登录
     * @param phone 手机号
     * @param role 1 店铺，2员工
     * @return
     */
    HashMap login(@Param("phone") String phone ,@Param("role") Integer role);

    /**
     * 修改登录token
     * @param id 注册ID
     * @param role 1店铺，2员工
     * @param token
     * @return
     */
    Integer updateLoginToken(@Param("id") Integer id ,@Param("role") Integer role ,@Param("token") String token);

    /**
     * 根据token和角色查询注册ID
     * @param token
     * @param role 1店铺，2员工
     * @return
     */
    Integer registerIdByTokenAndRole(@Param("token") String token ,@Param("role") Integer role );

    /**
     * 通过token和角色查询店铺信息
     * @param token
     * @param role 1店铺，2员工
     * @return
     */
    HashMap selectShopInfoByIdAndRole(@Param("token") String token  ,@Param("role") Integer role);


    /**
     * 找回密码
     * @param phone
     * @param password
     * @return
     */
    Integer updateRetrievePwd(@Param("phone") String phone, @Param("password") String password);

    /**
     * 通过token和role查询店铺ID
     * @param token
     * @param role 1店铺，2员工
     * @return
     */
    Integer shopipByTokenAndRole(@Param("token") String token ,@Param("role") Integer role);


    /**
     * 通过店铺ID和级别level查询添加所有类别的总数
     * @param bid
     * @return
     */
    Integer selectCategoryCount(@Param("bid") Integer bid , @Param("level") Integer level);


    /**
     * 根据一级类别id查询二级类别的最后一条序号
     * @param level
     * @return
     */
    String findGoodsCategoryCodeByLevel(@Param("level") Integer level);

    /**
     * 根据一级类别id查询一级序号
     * @param level
     * @return
     */
    String findOneCodeByLevel(@Param("id") Integer level);

    /**
     * 添加商品类别
     * @param goodsCategory 对象
     * @return
     */
    Integer addGoodsCategory(GoodsCategory goodsCategory);



    /**
     * 查询店铺一级商品类别
     * @param bid
     * @return
     */
    List<HashMap> findOneCategoryList(@Param("bid") Integer bid);

    /**
     * 根据一级类别id查询二级类别
     * @param categorId
     * @return
     */
    List<HashMap> findTwoCategoryList(@Param("level") Integer categorId);


    /**
     * 判断是大类别还是小类别
     * @param category_id 类别id
     * @return
     */
    Integer largeClassOrSmalClass(@Param("id") Integer category_id);


    /**
     *分页查询类别下的产品信息总数
     * @param
     * @return
     */
    Integer findGoodsListCount(Parameter parameter);

    /**
     * 分页查询类别下的产品信息集合
     * @return
     */
    List<HashMap> findGoodsList(Parameter parameter);

    /**
     * 修改产品类别
     * @param categoryId
     * @return
     */
    Integer updateGoodsCategory(@Param("categoryId") Integer categoryId, @Param("name") String name);

    /**
     * 查询当前的类别id是否是二级类别
     * @param categoryId
     * @return
     */
    Integer selectTwoCategoryByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 查询二级产品类别
     * @param level
     * @return
     */
    List<HashMap> selectCategoryListBylevel(@Param("level") Integer level);

    /**
     * 根据产品类别id查询该类别下是否有产品
     * @param categoryId
     * @return
     */
    List<HashMap> findGoodsListByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 删除产品类别
     * @param categoryId
     * @return
     */
    Integer deleteGoodsCategory(@Param("categoryId") Integer categoryId);

    /**
     * 产品大类别置顶
     * @param category_id
     * @return
     */
    Integer upateCategoryRP(@Param("id") Integer category_id, @Param("status") Integer status);


    /**
     * 通过产品类别id查询最后一条产品编号
     * @param categoryId
     * @return
     */
    String findNumberByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 根据类别id查询产品类别序号
     * @param id
     * @return
     */
    String findCodeById(@Param("id") Integer id);

    /**
     * 方案菜单
     * @return
     */
    List<HashMap> scheme(@Param("bid") Integer bid);

    /**
     * 添加商家菜单方案
     * @param bid
     * @param name
     * @return
     */
    Integer addBusinessScheme(@Param("scheme_id") Integer scheme_id, @Param("bid") Integer bid, @Param("name") String name);

    /**
     * 修改商家菜单方案
     * @param bsid
     * @param name
     * @return
     */
    Integer updateBusinessScheme(@Param("scid") Integer bsid, @Param("name") String name);


    /**
     * 通过产品类别ID和产品编号查询是否存在
     * @param categoryId
     * @param number
     * @return
     */
    String findNumberByCidAndNum(@Param("categoryId") Integer categoryId, @Param("number") String number);

    /**
     * 添加产品
     * @param goods
     * @return
     */
    Integer addGoods(Goods goods);

    /**
     * 添加产品轮播图片
     * @param goodsId
     * @param img
     * @return
     */
    Integer addGoodsImgs(@Param("goodsId") Integer goodsId, @Param("img") String img);

    /**
     * 添加每件产品的十五种方案价格
     * @param goodsId 产品ID
     * @param schemeId 方案菜单ID
     * @param price 方案价格
     * @return
     */
    Integer addGoodsScheme(@Param("goodsId") Integer goodsId, @Param("schemeId") Integer schemeId, @Param("price") Double price);

    /**
     * 添加库存信息
     * @param goodsId 产品ID
     * @param warehouseId 仓库ID
     * @param minimum 最低库存
     * @param highest 最高库存
     * @return
     */
    Integer addInventory(@Param("goodsId") Integer goodsId, @Param("warehouseId") Integer warehouseId, @Param("minimum") Integer minimum, @Param("highest") Integer highest);

    /**
     * 通过产品ID查询产品信息（产品资料）
     * @param goods_id
     * @return
     */
    HashMap findGoodsByGid(@Param("goods_id") Integer goods_id);

    /**
     * 通过产品ID查询产品信息
     * @param goods_id
     * @return
     */
    HashMap goodsInfo(@Param("goods_id") Integer goods_id);


    /**
     *  通过产品ID查询产品轮播图片
     * @param goodsId
     * @return
     */
    List<HashMap>  findGoodsImgByGid(@Param("goodsId") Integer goodsId);

    /**
     * 通过产品ID查询产品价格方案
     * @param goodsId
     * @return
     */
    List<HashMap> findGoodsSchemeByGid(@Param("goodsId") Integer goodsId);

    /**
     * 通过产品ID查询产品原来编号
     * @param goodsId
     * @return
     */
    String findNumberByGid(@Param("goodsId") Integer goodsId);


    /**
     * 通过产品id查询产品主要的资料信息
     * @param goods_id 产品id
     * @return
     */
    HashMap selectGoodsInfoById(@Param("id") Integer goods_id);

    /**
     * 修改产品信息
     * @param goods
     * @return
     */
    Integer updateGoodsInfo(Goods goods);

    /**
     * 修改产品图片
     * @param id
     * @param img
     * @return
     */
    Integer updateGoodsImgs(@Param("id") Integer id, @Param("img") String img);

    /**
     * 修改产品价格方案表
     * @param id
     * @param price
     * @return
     */
    Integer updateGoodsScheme(@Param("id") Integer id, @Param("price") Double price);


    /**
     * 通过库存ID修改库存信息
     * @param goodsId
     * @param warehouseId
     * @param minimum
     * @param highest
     * @return
     */
    Integer updateInventory(@Param("goodsId") Integer goodsId, @Param("warehouseId") Integer warehouseId, @Param("minimum") Integer minimum, @Param("highest") Integer highest);

    /**
     *修改产品其它信息
     * @param goods 产品ID 保质期 退货有效期 包装体积 重量  产品详情 备注
     * @return
     */
    Integer updateGoods(Goods goods);

    /**
     * 通过产品id查询产品状态
     * @param goods_id
     * @return
     */
    Integer goodsStatus(@Param("id") Integer goods_id);

    /**
     * 通过产品id查询产品库存是否大于0以上的
     * @param goods_id 产品id
     * @return
     */
    Integer goodsInventory(@Param("id") Integer goods_id);


    /**
     * 删除产品信息
     * @param id
     * @return
     */
    Integer deleGoods(@Param("id") Integer id);

    /**
     * 通过产品ID修改产品上架状态
     * @param goodsId
     * @param status
     * @return
     */
    Integer updateStatusByGid(@Param("goodsId") Integer goodsId, @Param("status") Integer status);


    /**
     * 删除产品图片
     * @param id
     * @return
     */
    Integer deleteGoodsImg(@Param("id") Integer id);

    /**
     * 修改产品在APP上排序的优先级
     * @param goods_id
     * @return
     */
    Integer updateGoodsPriority(@Param("goods_id") Integer goods_id, @Param("priority") Integer priority);

    /**
     * 查询总平台推送给商家的信息总数
     * @param business_id 商家ID
     * @return
     */
    Integer findPushBusinessMessageCount(@Param("business_id") Integer business_id);

    /**
     * 查询总平台推送给商家的信息集合
     * @param business_id 商家ID
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> findPushBusinessMessage(@Param("business_id") Integer business_id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询商家添加积分产品的类别总数
     * @param business_id 商家ID
     * @return
     */
    Integer selectIntegrGoodsCategoryCount(@Param("business_id") Integer business_id);



    /**
     * 添加积分产品类别
     * @param goodsCategory
     * @return
     */
    Integer addIntegrGoodsCategory(GoodsCategory goodsCategory);


    /**
     * 查询店铺下的一级积分产品大类别名称
     * @param business_id
     * @return
     */
    List<HashMap> selectBusinessIntegrGoodsCategory(@Param("business_id") Integer business_id);

    /**
     * 查询店铺积分产品类别下的产品信息总数
     * @param parameter
     * @return
     */
    Integer selectIntegrGoodsListCount(Parameter parameter);

    /**
     * 查询店铺积分产品类别下的产品信息集合
     * @param parameter
     * @return
     */
    List<HashMap> selectIntegrGoodsList(Parameter parameter);

    /**
     * 通过积分产品类别id查询类别名称
     * @param id
     * @return
     */
    HashMap selectIntegrGoodsCategory(@Param("id") Integer id);


    /**
     * 修改积分产品类别名称
     * @param id
     * @return
     */
    Integer updateIntegrGoodsCategory(@Param("id") Integer id, @Param("name") String name);


    /**
     * 通过积分产品类别id查询该类别下是否有积分产品信息
     * @param category_id
     * @return
     */
    List<HashMap> selectIntegrGoodsInfo(@Param("category_id") Integer category_id);

    /**
     * 删除积分产品类别
     * @param id
     * @return
     */
    Integer deleteIntegrGoodsCategory(@Param("id") Integer id);


    /**
     * 查询积分产品下的最后一条产品编号
     * @param category_id 积分产品类别id
     * @return
     */
    String selectIntegrGoodsLastNumber(@Param("category_id") Integer category_id);

    /**
     * 通过积分产品类别id查询类别序号
     * @param id 积分产品类别id
     * @return
     */
    String selectIntegrGoodsCode(@Param("id") Integer id);

    /**
     * 通过类别ID和编号查询当前输入的积分产品编号是否存在
     * @param category_id
     * @param number
     * @return
     */
    String selelctIntegrGoodsNumberByCidAndNum(@Param("category_id") Integer category_id, @Param("number") String number);


    /**
     * 添加积分产品信息
     * @param integrGoods
     * @return
     */
    Integer addIntegrGoods(IntegrGoods integrGoods);

    /**
     * 添加积分产品库存信息
     * @param goodsInventory
     * @return
     */
    Integer addIntegrGoodsInventory(GoodsInventory goodsInventory);


    /**
     *通过积分产品id查询产品信息
     * @param id
     * @return
     */
    HashMap selectIntegrGoodsInfoById(@Param("id") Integer id);

    /**
     * 通积分产品id查询原来的编号
     * @param id 积分产品id
     * @return
     */
    String selectIntegrGoodsNumberById(@Param("id") Integer id);

    /**
     * 修改积分产品信息
     * @param integrGoods
     * @return
     */
    Integer updateIntegrGoodsInfo(IntegrGoods integrGoods);

    /**
     * 修改积分产品库存信息
     * @param goodsInventory
     * @return
     */
    Integer updateIntegrGoodsInventory(GoodsInventory goodsInventory);

    /**
     * 判断积分产品是否存在正在兑换的订单
     * @param goods_id
     * @return
     */
    List<Integer> selectIntegerOrder(@Param("goods_id") Integer goods_id);

    /**
     * 删除积分产品
     * @param id 产品id
     * @return
     */
    Integer deleteIntegrGoods(@Param("id") Integer id);

    /**
     * 删除积分产品库存
     * @param goods_id 产品id
     * @return
     */
    Integer deleteIntegrGoodsInventory(Integer goods_id);


    /**
     * 用户申请绑定商家
     * @param user_id
     * @param business_id
     * @param applyTime
     * @return
     */
    Integer addBindingApplication(@Param("user_id") Integer user_id, @Param("business_id") Integer business_id, @Param("applyTime") Date applyTime);


    /**
     * 分享有奖二维码
     * @param token
     * @param role
     * @return
     */
    HashMap sharePhone(String token ,Integer role);

    /**
     * 根据手机号查询邀请商家注册的信息
     * @param phone
     * @param open
     * @return
     */
    List<HashMap> findsharePhone(@Param("phone") String phone, @Param("open") Integer open);

    /**
     * 积分产品下架
     * @param goods_id
     * @param status
     * @return
     */
    Integer InventoryGoodsIsLower(@Param("goods_id") Integer goods_id,@Param("status") Integer status);


    /**
     * 获取pc端系统公告
     * @return
     */
    HashMap systemBulletin();

    /**
     * 获取电子协议
     * @return
     */
    HashMap agreement();

    /**
     * 通过商家id查询商家信息
     * @param bid
     * @return
     */
    HashMap findBusinessInfo(@Param("id") Integer bid);

    /**
     * 修改商家设置
     * @param business
     * @return
     */
    Integer updateBusinessSetting(Business business);

    /**
     * 查询用户的推荐产品
     * @param bid 商家id
     * @param binding_id 用户id 0等于所有
     * @return
     */
    List<HashMap> selectUserRecommendGoods(@Param("business_id") Integer bid, @Param("binding_id") Integer binding_id);

    /**
     * 查询是否已经推荐产品给用户了
     * @param goods_id
     * @param binding_id
     * @return
     */
    Integer isRecommendGoodsGaveUser(@Param("goods_id") Integer goods_id, @Param("binding_id") Integer binding_id);

    /**
     * 添加用户的推荐产品
     * @param recommend
     * @return
     */
    Integer addUserRecommendGoods(Recommend recommend);


    /**
     * 下架或上传推荐产品
     * @param id 推荐产品id
     * @param is_upload 0上传，1下架
     * @return
     */
    Integer updateUserRecommendGoods(@Param("id") Integer id, @Param("is_upload") Integer is_upload);

    /**
     * 删除推荐产品
     * @param id 推荐产品id
     * @return
     */
    Integer deteleUserRecommendGoods(@Param("id") Integer id);

    /**
     * 查询限购产品
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> selectRestrictionGoods(@Param("business_id") Integer bid, @Param("name") String name);

    /**
     * 搜索未限购的产品
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> searchNotRestrictionGoods(@Param("business_id") Integer bid, @Param("name") String name, @Param("goods_id") Integer goods_id, @Param("category_id") Integer category_id);


    /**
     * 通过产品id查询产品信息
     * @param goods_id
     * @return
     */
    HashMap selectGoodsInfoByGid(@Param("id") Integer goods_id);


    /**
     * 查询产品是否添加限购了
     * @param goods_id
     * @return
     */
    Integer isAddRestriction(@Param("goods_id") Integer goods_id);

    /**
     * 添加限购产品
     * @param goods_id 限购的产品
     * @param num 限购数量
     * @param create_time 创建时间
     * @return
     */
    Integer addRestrictionGoods(@Param("business_id") Integer bid, @Param("goods_id") Integer goods_id, @Param("num") Double num, @Param("create_time") Date create_time);

    /**
     * 删除限购产品
     * @param id
     * @return
     */
    Integer deleteRestrictionGoods(Integer id);

    /**
     * 添加优惠券
     * @param coupon
     * @return
     */
    Integer addCoupon(Coupon coupon);

    /**
     * 查询优惠券
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> selectAllCoupon(@Param("business_id") Integer bid, @Param("name") String name);

    /**
     * 通过id查询优惠券信息
     * @param id 优惠券id
     * @return
     */
    HashMap selectCouponById(@Param("id") Integer id);

    /**
     * 修改优惠券
     * @param coupon
     * @return
     */
    Integer updateCoupon(Coupon coupon);

    /**
     * 通过优惠券id查询优惠券是否派送给客户过(防止客户的优惠券未使用或未过期)
     * @param id
     * @return
     */
    List<Integer> bindingCouponList(@Param("id") Integer id );

    /**
     * 删除优惠券
     * @param id
     * @return
     */
    Integer deleteCoupon(@Param("id") Integer id);

    /**
     * 用户优惠券
     * @param bid
     * @return
     */
    List<HashMap> bindingCoupon(@Param("business_id") Integer bid, @Param("binding_id") Integer binding_id);

    /**
     * 派送优惠券给用户
     * @param coupon
     * @return
     */
    Integer addBindingCoupon(Coupon coupon);

    /**
     * 添加满赠产品
     * @param goods_id 产品id
     * @param full 满
     * @param bestow 赠
     * @param create_time 创建时间
     * @return
     */
    Integer addFullBestowGoods(@Param("bid") Integer bid, @Param("goods_id") Integer goods_id, @Param("full") Double full, @Param("bestow") Double bestow, @Param("create_time") Date create_time);

    /**
     * 搜索未添加满赠的产品
     * @param bid
     * @param name
     * @return
     */
    List<HashMap> searchNotFullBestowGoods(@Param("business_id") Integer bid, @Param("name") String name, @Param("category_id") Integer category_id);

    /**
     * 查询已添加满赠的产品
     * @param bid
     * @param name
     * @return
     * @throws NullPointerException
     */
    List<HashMap> selectAddFullBestowGoods(@Param("business_id") Integer bid, @Param("name") String name)throws NullPointerException;

    /**
     * 删除满赠产品
     * @param id
     * @return
     */
    Integer deleteFullBestowGoods(Integer id);

    /**
     * 通过用户id查询用户积分id
     * @param binding_id 用户id
     * @return
     */
    Integer selectBindingIntegraId(@Param("binding_id") Integer binding_id);

    /**
     * 修改用户总积分（赠）
     * @param id 用户积分id
     *@param integar_num 用户积分
     * @return
     */
    Integer updateBindingIntegra(@Param("id") Integer id, @Param("integar_num") Double integar_num);

    /**
     * 修改用户总积分（减）
     * @param id 用户积分id
     *@param integar_num 用户积分
     * @return
     */
    Integer reduceBindingIntegra(@Param("id") Integer id, @Param("integar_num") Double integar_num);

    /**
     * （减）修改用户积分
     * @param id 积分id
     * @param integra 积分
     * @return
     */
    Integer updateUserIntegra(@Param("id") Integer id, @Param("integra") Double integra);

    /**
     * 赠送用户积分明细
     * @return
     */
    Integer addGiveBindingIntegra(Integra integra);

    /**
     * 查询用户积分总数
     * @param bid 商家id
     * @param id 用户id
     * @return
     */
    Integer selectUserIntegraCount(@Param("business_id") Integer bid, @Param("id") Integer id);

    /**
     * 查询用户积分
     * @param bid
     * @param id
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<HashMap> selectUserIntegra(@Param("business_id") Integer bid, @Param("id") Integer id, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询赠送用户积分列表（只保存三天记录）
     * @param id 用户id
     * @return
     */
    List<HashMap> selectBindingIntegraGive(@Param("id") Integer id);


    /**
     * 添加限时秒杀
     * @param seckill
     * @return
     */
    Integer addLimitedSeckill(Seckill seckill);

    /**
     * 查询限时秒杀时间段
     * @param bid 商家id
     * @return
     */
    List<HashMap> selectLimitedSeckill(@Param("business_id") Integer bid);

    /**
     * 更新秒杀时间段
     * @param seckill
     * @return
     */
    Integer updateLimitedSeckill(Seckill seckill);

    /**
     * 删除秒杀时间段
     * @param id 秒杀id
     * @return
     */
    Integer deleteLimitedSeckill(@Param("id") Integer id);

    /**
     * 查询秒杀时间段下是否有产品
     * @param id
     * @return
     */
    List<Integer> limitedSeckillGoods(@Param("id") Integer id);

    /**
     * 删除秒杀产品
     * @param id 秒杀时间段id
     * @return
     */
    Integer deleteLimitedSeckillGoods(@Param("id") Integer id);

    /**
     * 查询时间段下的秒杀产品
     * @param id
     * @return
     */
    List<HashMap> selectLimitedSeckillGoods(@Param("id") Integer id);


    /**
     * 搜索未在同一时间段秒杀的产品
     * @param bid 商家id
     * @param seckill_id 秒杀id
     * @param name 产品名称或编号或条形码
     * @return
     */
    List<HashMap> notLimitedSeckillGoods(@Param("business_id") Integer bid, @Param("seckill_id") Integer seckill_id, @Param("name") String name);

    /**
     * 添加限时秒杀详情
     * @param seckillDetail
     * @return
     */
    Integer addLimiteSeckillGoods(SeckillDetail seckillDetail);

    /**
     * 更换秒杀产品
     * @param seckillDetail
     * @return
     */
    Integer updateLimitedSeckillGoods(SeckillDetail seckillDetail);

    /**
     * 删除秒杀产品
     * @param id 秒杀产品id
     * @return
     */
    Integer deteleLimitedSeckillGoods(@Param("id") Integer id);

    /**
     * 添加满减活动
     * @param seckill
     * @return
     */
    Integer addFullReductionActivity(Seckill seckill);


    /**
     * 查询满减活动
     * @param bid
     * @return
     */
    List<HashMap> selectFullReductionActivity(@Param("business_id") Integer bid);

    /**
     * 修改满减活动
     * @param seckill
     * @return
     */
    Integer updateFullReductionActivity(Seckill seckill);

    /**
     * 删除满减活动
     * @param id 满减活动id
     * @return
     */
    Integer deleteFullReductionActivity(@Param("id") Integer id);


    /**
     * 通过token查询服务商id
     * @param token
     * @return
     */
    Integer findIdByBusinessToken(@Param("token") String token);


    /**
     * 修改商家密码
     * @param id
     * @param pwd
     * @return
     */
    Integer udpateBusinessPassword(@Param("id") Integer id, @Param("pwd") String pwd);


    /**
     * 查询第一产品类别
     * @param bid 商家id
     * @return
     */
    List<HashMap> oneCategory(@Param("business_id") Integer bid);

    /**
     * 查询第二产品类别
     * @param id 类别id
     * @return
     */
    List<HashMap> twoCategory(@Param("id") Integer id);

    /**
     * 导出产品资料
     * @param parameter
     * @return
     */
    List<HashMap> excelDownload(Parameter parameter);


    /**
     * 导出积分产品资料
     * @param
     * @return
     */
    List<HashMap> IntegrGoodsexcelDownload(@Param("business_id") Integer business_id, @Param("category_id") Integer category_id, @Param("number") String number, @Param("name") String name, @Param("barcode") Long barcode);


    /**
     * 匹配店铺密码
     * @param token
     * @param role
     * @return
     */
    String businessPassword(@Param("token") String token ,@Param("role") Integer role );

    /**
     * 轮播图
     * @return
     */
    List<HashMap> BMJ();

    /**
     * 查询店铺id和产品二维码识别码
     * @param business_id
     * @return
     */
    HashMap goodsOR(@Param("id") Integer business_id );


    /**
     * 刷新产品二维码识别码
     * @param business_id
     * @param icode
     * @return
     */
    Integer refreshGoodsORIdentificationCode(@Param("id") Integer business_id , @Param("icode") Integer icode);





    /**
     * 查询所有的商家
     * @return
     */
    List<Integer> businessId();

    /**
     * 查询商家id
     * @param token
     * @return
     */
    Integer businessIdByToken(@Param("token") String token);

    /**
     * 获取操作人id，商家账号默认0
     * @return
     */
    Integer idByToken(@Param("token") String token);

    /**
     * 查询商家库存设置
     * @param bid
     * @return
     */
    Integer amountSet(@Param("id") Integer bid);

    /**
     * 获取登录人的名称
     * @param token
     * @return
     */
    String findName(@Param("token") String token);


    /**
     * 通过token查询服务商id
     * @param token
     * @return
     */
    Integer findIdByStaffToken(@Param("token") String token);

    /**
     * 通过token查询员工id
     * @param token
     * @return
     */
    Integer findStaffIdByToken(@Param("token") String token);

    /**
     * 通过token查询服务商信息
     * @param token
     * @return
     */
    HashMap findSellerBySellerToken(@Param("token") String token);

    /**
     * 通过token查询员工信息
     * @param token
     * @return
     */
    HashMap findClerkByClerkToken(@Param("token") String token);




    /**
     * 通过产品id查询产品实际库存
     * @param goods_id
     * @return
     */
    Double selectGoodsInventory(@Param("goods_id") Integer goods_id);


    /**
     * 通过产品id查询产品实际库存和虚拟库存
     * @param goods_id
     * @return
     */
    HashMap findGoodsInventory(@Param("goods_id") Integer goods_id);

    /**
     * 增加产品的实际库存和虚拟库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer increaseGoodsInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     * 增加产品虚拟库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer increaseGoodsFictitiousInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     *增加产品实际库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer increaseGoodsActualInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);


    /**
     * 减少产品虚拟库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer reduceGoodsFictitiousInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     * 减少产品实际库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer reduceGoodsActualInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);

    /**
     * 减少产品实际库存和虚拟库存
     * @param goods_id 产品id
     * @param num 数量
     * @return
     */
    Integer reduceGoodsInventory(@Param("goods_id") Integer goods_id, @Param("num") Double num);


    /**
     * 查询商家的配送价
     * @param bid
     * @return
     */
    Integer findFreight(@Param("id") Integer bid);

    /**
     * 根据产品id查询产品进价金额
     * @param goods_id
     * @return
     */
    double findGoodsCostPrice(@Param("id") Integer goods_id);


    /**
     * 通过用户id获取对应的积分比例
     * @param id
     * @return
     */
    HashMap selectBindingIntegra(@Param("id") Integer id);


    /**
     * 通过店铺产品id和当前时间判断该产品是否在每天产品库存统计记录里
     * @param goods_id 产品id
     * @return
     */
    Integer selectGoodsInventoryStatisByBidAndDate(@Param("goods_id") Integer goods_id);

    /**
     * 添加销售产品是否在每天产品库存统计记录(销售）
     * @param business_id 店铺id
     * @param goods_id 产品id
     * @param sale 销售产品
     * @param time 创建时间
     * @return
     */
    Integer addSaleGoodsInventoryStatis(@Param("business_id") Integer business_id  , @Param("goods_id") Integer goods_id,@Param("total") BigDecimal total,@Param("sale") BigDecimal sale,@Param("time") Date time);

    /**
     * 添加数据 减销售产品是否在每天产品库存统计记录(销售）
     * @param business_id 店铺id
     * @param goods_id 产品id
     * @param sale 销售产品
     * @param time 创建时间
     * @return
     */
    Integer addReduceSaleGoodsInventoryStatis(@Param("business_id") Integer business_id  , @Param("goods_id") Integer goods_id,@Param("total") BigDecimal total,@Param("sale") BigDecimal sale,@Param("time") Date time);


    /**
     *增加产品是否在每天产品库存统计记录（销售）
     * @param id 产品统计id
     * @param sale 销售产品
     * @return
     */
    Integer increaseSaleGoodsInventoryStatis(@Param("id") Integer id ,@Param("sale") BigDecimal sale);


    /**
     *减少产品是否在每天产品库存统计记录（销售）
     * @param id 产品统计id
     * @param sale 销售产品
     * @return
     */
    Integer reduceSaleGoodsInventoryStatis(@Param("id") Integer id ,@Param("sale") BigDecimal sale);


    /**
     * 添加采购产品是否在每天产品库存统计记录(采购）
     * @param business_id 店铺id
     * @param goods_id 产品id
     * @param purchase 采购产品
     * @param time 创建时间
     * @return
     */
    Integer addPurchaseGoodsInventoryStatis(@Param("business_id") Integer business_id  , @Param("goods_id") Integer goods_id,@Param("total") BigDecimal total ,@Param("purchase") BigDecimal purchase,@Param("time") Date time);


    /**
     * 添加采购产品是否在每天产品库存统计记录(采购）
     * @param business_id 店铺id
     * @param goods_id 产品id
     * @param purchase 采购产品
     * @param time 创建时间
     * @return
     */
    Integer addReducePurchaseGoodsInventoryStatis(@Param("business_id") Integer business_id  , @Param("goods_id") Integer goods_id,@Param("total") BigDecimal total ,@Param("purchase") BigDecimal purchase,@Param("time") Date time);



    /**
     *增加采购产品是否在每天产品库存统计记录（采购）
     * @param id 产品统计id
     * @param purchase 采购产品
     * @return
     */
    Integer increasePurchaseGoodsInventoryStatis(@Param("id") Integer id ,@Param("purchase") BigDecimal purchase);


    /**
     *减少采购产品是否在每天产品库存统计记录（采购）
     * @param id 产品统计id
     * @param purchase 采购产品
     * @return
     */
    Integer reducePurchaseGoodsInventoryStatis(@Param("id") Integer id ,@Param("purchase") BigDecimal purchase);


    /**
     * 查询店铺启用的打印机
     * @param business_id
     * @return
     */
    List<String> businessPrinter(@Param("business_id") Integer business_id);


    /**
     * 添加模板2
     * @param business_id
     * @param title
     * @param type
     * @param one
     * @param two
     * @return
     */
    Integer addTemplateTwo( @Param("business_id") Integer business_id,@Param("title") String title ,@Param("type") Integer type ,@Param("one") String one , @Param("two") String two);

    /**
     * 查询店铺模板2集合
     * @param business_id
     * @return
     */
    List<Template> selectTemplateTwoList(@Param("business_id") Integer business_id);

    /**
     * 根据店铺和类型查询店铺模板2
     * @param business_id
     * @param type
     * @return
     */
    Template selectTemplateTwo(@Param("business_id") Integer business_id ,@Param("type") Integer type);

    /**
     * 修改模板2
     * @param id
     * @param title
     * @param one
     * @param two
     * @return
     */
    Integer updateTemplateTwo(@Param("id") Integer id,@Param("title") String title  ,@Param("one") String one ,@Param("two") String two );

    /**
     * 添加模板3
     * @param business_id
     * @param title
     * @param type
     * @param one
     * @param two
     * @return
     */
    Integer addTemplateThree( @Param("business_id") Integer business_id,@Param("title") String title ,@Param("type") Integer type ,@Param("one") String one , @Param("two") String two);

    /**
     * 查询店铺模板3集合
     * @param business_id
     * @return
     */
    List<Template> selectTemplateThreeList(@Param("business_id") Integer business_id);

    /**
     * 根据店铺和类型查询店铺模板3
     * @param business_id
     * @param type
     * @return
     */
    Template selectTemplateThree(@Param("business_id") Integer business_id ,@Param("type") Integer type);

    /**
     * 修改模板3
     * @param id
     * @param title
     * @param one
     * @param two
     * @return
     */
    Integer updateTemplateThree(@Param("id") Integer id,@Param("title") String title  ,@Param("one") String one ,@Param("two") String two );


    /**
     * 查询店铺模板5集合
     * @param business_id
     * @return
     */
    List<Template> selectTemplateFiveList(@Param("business_id") Integer business_id);

    /**
     * 根据店铺和类型查询店铺模板5
     * @param business_id
     * @param type
     * @return
     */
    Template selectTemplateFive(@Param("business_id") Integer business_id ,@Param("type") Integer type);

    /**
     * 修改模板5
     * @param id
     * @param title
     * @param one
     * @param state
     * @return
     */
    Integer updateTemplateFive(@Param("id") Integer id,@Param("title") String title  ,@Param("one") String one ,@Param("state") Integer state );

    /**
     * 通过绑定用户id查询方案id
     * @param bindingId
     * @return
     */
    Integer selectBindingSchemeId(@Param("id") Integer bindingId);


    /**
     * 销售收款记录分组拼接
     * @param id
     * @return
     */
    String salesMoneyRecordsGroupConcat(@Param("id") Integer id);

    /**
     * 采购收款记录分组拼接
     * @param id
     * @return
     */
    String purchaseMoneyRecordsGroupConcat(@Param("id") Integer id);



}
