package com.shengxian.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Description: 继承WebMvcConfigurerAdapter类并重写addInterceptors方法；
 *
 * @Author: yang
 * @Date: 2019-03-27
 * @param
 * @Version: 1.0
 */
@SpringBootConfiguration
public class MyStringMVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private InterceptorConfig loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                .excludePathPatterns("/websocket"  ,"/websocket/**")
                .excludePathPatterns("/business/ERROR_URL1" ,"/business/ERROR_URL2" ,"/business/ERROR_URL3" ,"/business/ERROR_URL4" ,"/business/ERROR_URL5") //异常跳转
                //获取验证码,注册,登录,找回密码 ,根据手机号查询邀请商家注册的信息
                .excludePathPatterns("/business/sendSms" ,"/business/register" ,"/business/login" ,"/business/retrievePwd" ,"/business/findsharePhone")
                //获取pc端系统公告,协议,轮播图,修改产品在APP上排序的优先级
                .excludePathPatterns("/business/system_bulletin" ,"/business/agreement" ,"/business/BMJ" ,"/business/updateGoodsPriority","/business/twoCategory")
                //挑选类别自动生成用户编号
                .excludePathPatterns("/user/selectNumberByCategoryId" )
                //挑选类别自动生成员工编号 ,匹配员工密码
                .excludePathPatterns("/staff/automaticSelectStaffnumberByCategoryId" ,"/staff/findStaffPWD")
                //单张图片上传 ,多张图片上传
                .excludePathPatterns("/fileupload/upload", "/fileupload/uploadList")
                // 获取员工角色 ,查询商家是否盘点中
                .excludePathPatterns("/menu/staffRole" ,"/inventory/checkGoodsInventory")
                //查询赠送单 ,查询报损单,导出仓库产品详情 ,赠送产品导出,报损产品导出
                .excludePathPatterns("/inventory/findGiveGoods" ,"/inventory/findLossGoods" ,"/inventory/excelDownload" ,"/inventory/giveGoodslDownload" ,"/inventory/lossGoodslDownload")
                // 根据仓库ID查询仓库信息 ,结算记录,结算的产品详情,库存报表（每个仓库的详情信息）
                .excludePathPatterns("/inventory/findInventoryByWid" ,"/inventory/goodsSettlement","/inventory/settlementGoodsRecord","/inventory/businessGoodsInventoryInfo" )
                //挑选类别自动生成供应商编号 ,导出供应商资料
                .excludePathPatterns("/suppliers/selectNumberByCategoryId" ,"/suppliers/excelDownload")
                //商家财务结算记录 ,通过财务结算id查询结算记录,欠条id查询欠条内容
                .excludePathPatterns("/finance/settlementInfo" ,"/finance/settlementByid" ,"/finance/findLous")
                //销售风险订单记录 ,采购风险订单记录 ,收款分类,最后结算时间
                .excludePathPatterns("/finance/riskOrder" ,"/finance/purchaseRiskOrder" ,"/finance/receivablesType" ,"/finance/finalSettlementTime")
                //生产采购订单编号 ,通过名称或编号或条码搜索商家产品（采购和销售）,查询商家供应商产品收藏 ,通过产品id搜索产品（采购）
                .excludePathPatterns("/purchase/createPurchaseOrder" ,"/purchase/selectBusinessGoods","/purchase/findBusinessGoodsCollection" ,"/purchase/selectBusinessGoodsById")
                //通过采购订单id查询订单详情 ,打印模板 ,打印采购订单详情 ,回退取消订单 ,导出待采购订单信息
                .excludePathPatterns("/purchase/selectPurchaseOrderDetailById" ,"/purchase/findPurchaseOrderDetail","/purchase/printTemplate" ,"/purchase/purchasePrintDetail" ,"/purchase/updatePurchaseStatus" ,"/purchase/excelPurchasereport" )
                // 计算这一年总采购金额,计算这季度总采购金额 ,计算这月总采购金额 ,计算这周总采购金额 ,计算当天总采购金额 ,自定义时间段总采购金额
                .excludePathPatterns("/purchase/yearPurchassPirce","/purchase/quarterPurchassPrice","/purchase/monthPurchassPrice","/purchase/weekPurchassPrice","/purchase/daysPurchassPrice","/purchase/definitionPurchassPrice")
                //创建订单编号 ,搜索商家用户 ,通过用户id查询用户信息 ,通过用户方案id和产品id查询查询产品信息 ,通过订单id查询订单详情
                .excludePathPatterns("/order/createOrder" ,"/order/selectBindingUser" ,"/order/selectUserInfoById" ,"/order/findGoodsInfoBySidAndGid" ,"/order/findOrderInfoById")
                //打印销售订单 ,打印销售订单详情
                .excludePathPatterns("/order/orderPrint" ,"/order/orderPrintDetail" )
                //待接单总数,未打印的订单总数 ,待送货订单总数 , 查询未付款,欠款订单总数 ,用户取消订单总数 ,申请审核用户信息总数 ,申请欠款审核总数
                .excludePathPatterns("/order/waitingOrderCount" ,"/order/notPrintedOrderCount" ,"/order/stayDeliveredCount","/order/arrivalOrderCount" ,"/order/userCancelOrderCount" ,"/order/auditUserCount","/order/arrearsAuditCount")
                //积分兑换订单总数 ,售货服务总数
                .excludePathPatterns("/order/selectIntegraOrderCount" ,"/order/salesServiceCount")
                //没有销售用户导出 ,超期用户导出 ,打印临时订单详情 ,打印临时订单
                .excludePathPatterns("/order/noSalesUserDownload" ,"/order/overduePurchaseUserDownload" ,"/order/printTemporaryOrderDetail" ,"/order/printTemporaryOrder")
                //通过员工id查询一级菜单 ,通过员工id和一级菜单id查询二级菜单 ,通过员工id和二级菜单id查询三级菜单 ,三级菜单
                .excludePathPatterns("/menu/findStaffOneMenu" ,"/menu/findStaffTwoMenu" ,"/menu/findStaffThreeMenu" ,"/menu/selectThreeMenu" )
                //查询员工APP版本号 ，员工app中判断在是否有退出登录过 ，修改员工APP手机设备 ,APP中通过订单id查询订单详情
                .excludePathPatterns("/clerk/version" ,"/clerk/appIsLogin" ,"/clerk/updateEquipment" ,"/clerk/orderDateil")
                //每天产品库存情况总数（默认当天的） ,产品风控总数(默认当天的) ,采购风险订单记录总数（默认当天的） ,"销售风险订单记录总数（默认当天的）
                .excludePathPatterns("/finance/dayGoodsInventorySituationCount" ,"/finance/goodsWindControlCount" ,"/finance/purchaseRiskOrderCount","/finance/riskOrderCount")
                //模板2
                .excludePathPatterns("/template/addTemplateTwo" , "/template/selectTemplateTwoList" , "/template/selectTemplateTwo" ,"/template/updateTemplateTwo")
                //模板3
                .excludePathPatterns("/template/addTemplateThree" , "/template/selectTemplateThreeList" , "/template/selectTemplateThree" ,"/template/updateTemplateThree")
                //模板5
                .excludePathPatterns( "/template/selectTemplateFiveList" , "/template/selectTemplateFive" ,"/template/updateTemplateFive")
                //产品二维码,刷新产品二维码识别码
                .excludePathPatterns("/business/goodsOR" , "/business/refreshGoodsORIdentificationCode" )
               //收款记录
                .excludePathPatterns("/order/selectOrderMoneyRecords" ,"/purchase/selectOrderMoneyRecords" ,"/clerk/IP")
                //客户账单 ,查看分享到微信的信息
                .excludePathPatterns("/clerk/shareWXRecord" ,"/clerk/selectWXShareRecord" ,"/clerk/orderWXShareRecord")
                //临时用户
                //查询店铺类别 ,店铺类别下的产品 ,加入购物车 ,减掉购物车 ,当前临时客户的购物车 ,结算 ,下订单
                .excludePathPatterns("/temporary/businessCategory" , "/temporary/businessGoods","/temporary/addShoppingCart" ,"/temporary/reduceShoppingCart" ,"/temporary/temporaryShoppingcart","/temporary/settlement","/temporary/addOrdre"  );
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
