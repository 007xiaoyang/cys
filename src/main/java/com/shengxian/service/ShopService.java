package com.shengxian.service;

import com.shengxian.common.util.Page;
import com.shengxian.entity.*;
import com.shengxian.vo.GoodsCategoryVO;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018/8/21
 * @Version: 1.0
 */
public interface ShopService {



    /**
     * 注册店铺
     * @return
     */
    Integer addBusiness(String storeName, String phone, String password, String invitation) throws NullPointerException ,Exception;

    /**
     * 登录
     * @param role 1店铺，2员工
     * @param phone 手机号
     * @param password
     * @return
     */
    String login(Integer role, String phone, String password) throws NullPointerException ,Exception;

    /**
     * 首页
     * @param token
     * @return
     */
    HashMap home(String token ,Integer role)throws NullPointerException , Exception;

    /**
     * 微信小程序首页轮播图
     * @param token
     * @return
     */
    HashMap getHome(String token ,Integer role)throws  Exception;

    Integer getUsefulLlife(String token ,Integer role);


    /**
     * 找回密码
     * @param phone
     * @return
     */
    Integer updateRetrievePwd(String phone, String password) throws NullPointerException, Exception;


    /**
     * 添加商品类别
     * @param token
     * @param name
     * @param level
     * @return
     */
    Integer addGoodsCategory(String token,Integer role, String name, Integer level) throws NullPointerException;

    /**
     *查询类别下的商品信息
     * @param token
     * @return
     */
    //List<HashMap> findGoodsCategoryList(String token ,Integer role)throws NullPointerException;
    List<GoodsCategoryVO> findGoodsCategoryList(String token ,Integer role)throws NullPointerException;

    /**
     * 分页查询类别下的产品信息
     * @param token
     * @param pageNo
     * @param categoryId 产品类别ID
     * @param number 产品编号
     * @param name 产品名称
     * @param barcode 条码
     * @return
     */
    Page findGoodsList(String token, Integer role, Integer pageNo, Integer categoryId, String number, String name, Long barcode)throws NullPointerException ;

    /**
     * 修改产品类别
     * @param categoryId
     * @return
     */
    Integer updateGoodsCategory(Integer categoryId, String name);

    /**
     * 删除产品类别
     * @param categoryId
     * @return
     */
    Integer deleteGoodsCategory(Integer categoryId)throws NullPointerException ,Exception;

    /**
     * 产品大类别置顶
     * @param category_id
     * @return
     */
    Integer upateCategoryRP(Integer category_id, Integer status)throws Exception;

    /**
     * 挑选产品类别id自动生成产品编号
     * @param categoryId
     * @return
     */
    String automaticGoodsNumberByCategoryId(Integer categoryId);

    /**
     * 查询店铺方案菜单
     * @return
     */
    List<HashMap> scheme(String token ,Integer role)throws Exception;

    /**
     * 添加商家菜单方案
     * @param token
     * @param name
     * @return
     */
    Integer addBusinessScheme(String token ,Integer role, String name);

    /**
     * 修改商家菜单方案
     * @param bsid
     * @param name
     * @return
     */
    Integer updateBusinessScheme(Integer bsid, String name)throws Exception;

    /**
     * 添加产品
     * @param token
     * @param goods
     * @return
     */
    Integer addGoodsInfo(String token ,Integer role, Goods goods)throws NullPointerException ,Exception;

    /**
     *通过产品ID查询产品信息（产品资料）
     * @param goodsId
     * @return
     */
    HashMap findGoodsByGid(String token  ,Integer role , Integer goodsId)throws NullPointerException;

    /**
     *通过产品ID查询产品信息
     * @param goodsId
     * @return
     */
    HashMap goodsInfo(String token  ,Integer role , Integer goodsId)throws NullPointerException;

    /**
     *修改产品信息
     * @param goods
     * @return
     */
    Integer updateGoods(Goods goods)throws NullPointerException;

    /**
     * 删除商品信息
     * @param id
     * @return
     */
    Integer deleGoods(Integer id)throws NullPointerException;


    /**
     * 产品上架
     * @param goodsId
     * @return
     */
    Integer isUpper(Integer goodsId);

    /**
     * 产品下架
     * @param goodsId
     * @return
     */
    Integer isLower(Integer goodsId);

    /**
     *删除产品图片
     * @param id
     * @return
     */
    Integer deleteGoodsImg(Integer id);

    /**
     * 修改产品在APP上排序的优先级
     * @param goods_id
     * @return
     */
    Integer updateGoodsPriority(Integer goods_id, Integer priority);

    /**
     * 分页查询总平台推送给商家的信息
     * @param token
     * @param pageNo
     * @return
     */
    Page findPushBusinessMessage(String token ,Integer role, Integer pageNo);



    /**
     *添加积分产品类别
     * @param token
     * @param name
     * @return
     */
    Integer addIntegrGoodsCategory(String token ,Integer role, String name)throws NullPointerException ,Exception;

    /**
     * 查询店铺下的积分产品类别名称
      * @param token
     * @return
     */
    List<HashMap> selectBusinessIntegrGoodsCategory(String token ,Integer role)throws NullPointerException;

    /**
     * 分页查询店铺积分产品信息
     * @param token
     * @param pageNo
     * @param categoryId
     * @param number
     * @param name
     * @param barcode
     * @return
     */
    Page selectIntegrGoodsList(String token ,Integer role, Integer pageNo, Integer categoryId, String number, String name, Long barcode)throws NullPointerException;


    /**
     * 通过积分产品类别id查询类别名称
     * @param id
     * @return
     */
    HashMap selectIntegrGoodsCategory(Integer id);

    /**
     * 修改积分产品类别名称
     * @param category_id
     * @return
     */
    Integer updateIntegrGoodsCategory(Integer category_id, String name);

    /**
     * 删除积分产品类别
     * @param id
     * @return
     */
    Integer deleteIntegrGoodsCategory(Integer id);

    /**
     * 自动生成积分产品编号
     * @param category_id
     * @return
     */
    String autoMakeIntegrGoodsNumber(Integer category_id);

    /**
     * 添加积分产品
     * @param token
     * @param integrGoods
     * @return
     */
    Integer addIntegrGoods(String token  ,Integer role, IntegrGoods integrGoods)throws NullPointerException ,Exception;

    /**
     * 通过积分产品id查询产品信息
     * @param id 产品id
     * @return
     */
    HashMap selectIntegrGoodsInfoById(Integer id);

    /**
     * 修改积分产品信息
     * @param integrGoods
     * @return
     */
    Integer updateIntegrGoodsInfo(IntegrGoods integrGoods)throws NullPointerException ,Exception ;

    /**
     * 删除积分产品
     * @param id
     * @return
     */
    Integer deleteIntegrGoods(Integer id)throws NullPointerException;

    /**
     * 获取商家二维码
     * @param token
     * @return
     */
    Integer findBusinessTDCode(String token ,Integer role);

    /**
     * 用户申请绑定商家
     * @param phone
     * @param business_id
     * @return
     */
    Integer addBindingApplication(String phone, Integer business_id);

    /**
     * 分享有奖二维码
     * @param token
     * @return
     */
    HashMap sharePhone(String token ,Integer role );


    /**
     * 根据手机号查询邀请商家注册的信息
     * @param phone
     * @param open
     * @return
     */
    List<HashMap> findsharePhone(String phone, Integer open);

    /**
     * 积分产品下架
     * @param gooods_id
     * @return
     */
    Integer InventoryGoodsIsLower(Integer gooods_id);

    /**
     * 获取pc端系统公告
     * @return
     */
    HashMap systemBulletin();

    /**
     * 获取电子协议
     * @return
     */
    HashMap agreement()throws Exception;

    /**
     * 查询类别下的产品信息集合
     * @param id
     * @return
     */
    List<HashMap> findGoodsListByCategoryId(Integer id);





    /**
     * 商家设置
     * @param token
     * @return
     */
    HashMap businessSet(String token ,Integer role);

    /**
     * 修改商家设置
     * @param business
     * @return
     */
    Integer updateBusinessSetting(Business business)throws Exception;

    /**
     * 查询用户的推荐产品
     * @param token
     * @param binding_id
     * @return
     */
    List<HashMap> selectUserRecommendGoods(String token,  Integer role ,Integer binding_id)throws NullPointerException,Exception;

    /**\
     * 添加用户的推荐产品
     * @param recommend
     * @return
     */
    Integer addUserRecommendGoods(Integer binding_id, Recommend[] recommend)throws Exception;

    /**
     * 下架或上传推荐产品
     * @param id 推荐产品id
     * @param is_upload 0上传，1下架
     * @return
     */
    Integer updateUserRecommendGoods(Integer id, Integer is_upload) throws Exception;

    /**
     * 删除推荐产品
     * @param id 推荐产品id
     * @return
     */
    Integer deteleUserRecommendGoods(Integer id)throws Exception;

    /**
     * 查询限购产品
     * @param token
     * @param name 产品名称或编号
     * @return
     * @throws NullPointerException
     */
    List<HashMap> selectRestrictionGoods(String token  ,Integer role , String name)throws NullPointerException ,Exception;

    /**
     * 搜索未限购的产品
     * @param token
     * @param name
     * @return
     * @throws NullPointerException
     */
    List<HashMap> searchNotRestrictionGoods(String token ,Integer role, String name, Integer goods_id, Integer category_id)throws NullPointerException,Exception;

    /**
     * 通过产品id查询产品信息
     * @param goods_id 产品id
     * @return
     */
    List<HashMap> selectGoodsInfoByGid(String goods_id)throws Exception;

    /**
     * 添加限购产品
     * @param token
     * @param goods_id
     * @param num
     * @return
     */
    Integer addRestrictionGoods(String token ,Integer role, Integer goods_id, Double num)throws NullPointerException,Exception;

    /**
     * 删除限购产品
     * @param id
     * @return
     */
    Integer deleteRestrictionGoods(Integer id)throws Exception;

    /**
     * 添加优惠券
     * @param token
     * @param coupon
     * @return
     */
    Integer addCoupon(String token ,Integer role, Coupon coupon)throws Exception;

    /**
     * 查询优惠券
     * @param token
     * @param name
     * @return
     */
    List<HashMap> selectAllCoupon(String token ,Integer role, String name)throws NullPointerException ,Exception;

    /**
     * 通过id查询优惠券信息
     * @param id 优惠券id
     * @return
     */
    HashMap selectCouponById(Integer id)throws Exception;

    /**
     * 修改优惠券
     * @param coupon
     * @return
     */
    Integer updateCoupon(Coupon coupon)throws Exception;

    /**
     * 删除优惠券
     * @param id
     * @return
     */
    Integer deleteCoupon(Integer id)throws NullPointerException ,Exception;

    /**
     * 用户优惠券
     * @param token
     * @return
     */
    List<HashMap> bindingCoupon(String token ,Integer role , Integer binding_id)throws NullPointerException,Exception;

    /**
     * 派送优惠券给用户
     * @param coupon
     * @return
     */
    Integer addBindingCoupon(Coupon coupon)throws Exception;

    /**
     * 添加满赠产品
     * @param goods_id 产品id
     * @param full 满
     * @param bestow 赠
     * @return
     */
    Integer addFullBestowGoods(String token ,Integer role , Integer goods_id, Double full, Double bestow)throws NullPointerException,Exception;

    /**
     * 搜索未添加满赠的产品
     * @param token
     * @param name
     * @return
     */
    List<HashMap> searchNotFullBestowGoods(String token ,Integer role , String name, Integer category_id)throws NullPointerException,Exception;

    /**
     * 查询已添加满赠的产品
     * @param token
     * @param name
     * @return
     * @throws NullPointerException
     */
    List<HashMap> selectAddFullBestowGoods(String token ,Integer role , String name)throws NullPointerException ,Exception;

    /**
     * 删除满赠产品
     * @param id
     * @return
     */
    Integer deleteFullBestowGoods(Integer id)throws Exception;

    /**
     * 用户积分赠送
     * @param integra
     * @return
     */
    Integer addGiveBindingIntegra(Integra integra)throws NullPointerException, Exception;

    /**
     * 查询用户积分
     * @param token
     * @param pageNo
     * @param id
     * @return
     */
    Page selectUserIntegra(String token ,Integer role , Integer pageNo, Integer id)throws NullPointerException, Exception;

    /**
     * 查询赠送用户积分列表（只保存三天记录）
     * @param id
     * @return
     */
    List<HashMap> selectBindingIntegraGive(Integer id)throws Exception;


    /**
     * 添加限时秒杀
     * @param token
     * @param seckill
     * @return
     */
    Integer addLimitedSeckill(String token  ,Integer role, Seckill seckill)throws Exception;

    /**
     * 查询限时秒杀时间段
     * @param token
     * @return
     */
    List<HashMap> selectLimitedSeckill(String token  ,Integer role )throws NullPointerException,Exception;

    /**
     * 更新秒杀时间段
     * @param seckill
     * @return
     */
    Integer updateLimitedSeckill(Seckill seckill)throws Exception;

    /**
     * 删除秒杀时间段
     * @param id
     * @return
     */
    Integer deleteLimitedSeckill(Integer id)throws Exception;

    /**
     * 查询时间段下的秒杀产品
     * @param id 秒杀id
     * @return
     */
    List<HashMap> selectLimitedSeckillGoods(Integer id)throws Exception;

    /**
     * 搜索未在同一时间段秒杀的产品
     * @param token
     * @param name
     * @return
     */
    List<HashMap> notLimitedSeckillGoods(String token , Integer role, Integer seckill_id, String name)throws NullPointerException,Exception;

    /**
     * 添加限时秒杀产品
     * @param seckillDetail
     * @return
     */
    Integer addLimitedSeckillGoods(SeckillDetail seckillDetail)throws Exception;

    /**
     * 更换秒杀产品
     * @param seckillDetail
     * @return
     */
    Integer updateLimitedSeckillGoods(SeckillDetail seckillDetail)throws Exception;

    /**
     * 删除秒杀产品
     * @param id 秒杀产品id
     * @return
     */
    Integer deteleLimitedSeckillGoods(Integer id)throws Exception;

    /**
     * 添加店铺满减活动
     * @return
     */
    Integer addFullReductionActivity(String token ,Integer role , Seckill seckill)throws Exception;

    /**
     * 查询满减活动
     * @param token
     * @return
     * @throws NullPointerException
     */
    List<HashMap> selectFullReductionActivity(String token ,Integer role)throws NullPointerException,Exception;

    /**
     * 修改满减活动
     * @param seckill
     * @return
     */
    Integer updateFullReductionActivity(Seckill seckill)throws Exception;

    /**
     * 删除满减活动
     * @param id 满减活动id
     * @return
     */
    Integer deleteFullReductionActivity(Integer id)throws Exception;



    /**
     *判断是否是商家登录的
     * @param token
     * @return
     */
    boolean isBuseinssToken(String token)throws Exception;

    /**
     * 修改商家密码
     * @param password
     * @return
     * @throws Exception
     */
    Integer udpateBusinessPassword(String token, String password)throws Exception;

    /**
     * 查询第一产品类别
     * @param token
     * @return
     */
    List<HashMap> oneCategory(String token ,Integer role )throws NullPointerException, Exception;

    /**
     * 查询第二产品类别
     * @param id 类别id
     * @return
     * @throws Exception
     */
    List<HashMap> twoCategory(Integer id)throws Exception;

    /**
     * 导出产品资料
     * @param parameter
     * @return
     */
    HSSFWorkbook excelDownload(String token ,Integer role, Parameter parameter);


    /**
     * 导出积分产品资料
     * @param
     * @return
     */
    HSSFWorkbook IntegrGoodsexcelDownload(String token ,Integer role, Integer categoryId, String number, String name, Long barcode);


    /**
     * 匹配店铺密码
     * @param token
     * @param password
     * @return
     * @throws NullPointerException
     * @throws Exception
     */
    boolean businessPassword(String token  ,Integer role, String password)throws NullPointerException,Exception;

    /**
     * 轮播图
      * @return
     */
    List<HashMap> BMJ();

    /**
     * 产品二维码
     * @param token
     * @param role
     * @return
     */
    HashMap goodsOR(String token , Integer role );

    /**
     * 刷新产品二维码识别码
     * @param token
     * @param role
     * @return
     */
    Integer refreshGoodsORIdentificationCode(String token , Integer role);

    /**
     * 获取每个类别下对应的产品数据集合
     * @param token
     * @param role
     * @return
     */
    List<GoodsCategoryVO> getGoodsList(String token , Integer role);

    /**
     * 获取每个类别下对应的产品数据集合
     * @param token
     * @param role
     * @return
     */
    List<GoodsCategoryVO> getCategroyList(String token , Integer role);

}