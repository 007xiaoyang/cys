package com.shengxian.common;

import com.shengxian.common.util.DateUtil;
import com.shengxian.entity.MothPrinterClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 飞蛾打印机接口
 *
 * @Author: yang
 * @Date: 2019-04-28
 * @Version: 1.0
 */
public class MothPrinter {

    private static Logger log = Logger.getLogger(MothPrinter.class);

    public static final String URL = "http://api.feieyun.cn/Api/Open/";//不需要修改

    public static final String USER = "2954407205@qq.com";//*必填*：账号名
    public static final String UKEY = "Acdsyb8v4zwKWS2U";//*必填*: 注册账号后生成的UKEY
    public static final String SN = "817006805";//*必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API

    private List<MothPrinterClass> mothPrinterClasses;

    public List<MothPrinterClass> getMothPrinterClasses() {
        return mothPrinterClasses;
    }

    public void setMothPrinterClasses(List<MothPrinterClass> mothPrinterClasses) {
        this.mothPrinterClasses = mothPrinterClasses;
    }

    //添加打印机接口
    public static String addPrinter(String snlist){

        log.warn(snlist);

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent",snlist));

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 打印订单
     * @param sns
     * @param beizhu
     * @param tatol
     * @param address
     * @param phone
     * @param name
     * @param orderNumber
     * @param no
     * @param mothPrinterClasses
     */
    public static void salePrint(String sns ,String title,String beizhu , BigDecimal tatol ,String address,String phone ,String name ,String orderNumber ,String no,Double f ,Double dp,Double reduce ,List<MothPrinterClass> mothPrinterClasses){
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式


            String typename = "";

            String content;
            content = "<CB>"+title+"</CB><BR>";
            content += "名称："+name+"<BR>";
            content += "联系电话："+phone+"<BR>";
            content += "送货地点："+address+"<BR>";
            content += "名称<BR>";
            content += "数量　    单价         金额<BR>";
            content += "--------------------------------<BR>";
            for (MothPrinterClass moth: mothPrinterClasses  ) {
                if (moth != null){
                    if (moth.getType() == 1){
                        typename = "（赠送）";
                    }
                    content += moth.getGoodsName()+typename+"<BR>";
                    String values = MothPrinterClass.divisionString( String.valueOf(moth.getNum())+moth.getUnits() ,moth.getPrice() ,moth.getMoney());
                    content += values;
                }
            }
            content += "--------------------------------<BR>";
            content += "备注："+beizhu+"<BR>";
            content += "运费："+f+"元<BR>";
            content += "差价："+dp+"元<BR>";
            content += "优惠："+reduce+"元<BR>";
            content += "--------------------------------<BR>";
            content += "<B>总计："+tatol+"元</B><BR>";
            content += "打印时间："+ DateUtil.getTime() +"<BR>";
            content += "订单号："+ orderNumber+"<BR>";
            content += "标识码："+ no+"<BR>";

            //通过POST请求，发送打印信息到服务器
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)//读取超时
                    .setConnectTimeout(30000)//连接超时
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost post = new HttpPost(URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("user",USER));
            String STIME = String.valueOf(System.currentTimeMillis()/1000);
            nvps.add(new BasicNameValuePair("stime",STIME));
            nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
            nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn",sns));
            nvps.add(new BasicNameValuePair("content",content));
            nvps.add(new BasicNameValuePair("times","1"));//打印联数

            CloseableHttpResponse response = null;
            String result = null;
            try
            {
                post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
                response = httpClient.execute(post);
                int statecode = response.getStatusLine().getStatusCode();
                if(statecode == 200){
                    HttpEntity httpentity = response.getEntity();
                    if (httpentity != null){
                        //服务器返回的JSON字符串，建议要当做日志记录起来
                        result = EntityUtils.toString(httpentity);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally{
                try {
                    if(response!=null){
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(result);


    }

    /**
     * 打印订单
     * @param sns
     * @param beizhu
     * @param tatol
     * @param address
     * @param phone
     * @param name
     * @param orderNumber
     * @param no
     * @param mothPrinterClasses
     */
    public static void puchasePrint(String sns ,String title,String beizhu , BigDecimal tatol ,String address,String phone ,String name ,String orderNumber ,String no,Double f ,Double dp ,List<MothPrinterClass> mothPrinterClasses){
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式

        String typename = "";

        String content;
        content = "<CB>"+title+"</CB><BR>";
        content += "名称："+name+"<BR>";
        content += "联系电话："+phone+"<BR>";
        content += "送货地点："+address+"<BR>";
        content += "名称<BR>";
        content += "数量　    单价         金额<BR>";
        content += "--------------------------------<BR>";
        for (MothPrinterClass moth: mothPrinterClasses  ) {
            if (moth != null){
                if (moth.getType() == 1){
                    typename = "（赠送）";
                }
                content += moth.getGoodsName()+typename+"<BR>";
                String values = MothPrinterClass.divisionString( String.valueOf(moth.getNum())+moth.getUnits() ,moth.getPrice() ,moth.getMoney());
                content += values;
            }

        }
        content += "--------------------------------<BR>";
        content += "备注："+beizhu+"<BR>";
        content += "运费："+f+"<BR>";
        content += "差价："+dp+"<BR>";
        content += "--------------------------------<BR>";
        content += "<B>总计："+tatol+"元</B><BR>";
        content += "打印时间："+ DateUtil.getTime() +"<BR>";
        content += "订单号："+ orderNumber+"<BR>";
        content += "标识码："+ no+"<BR>";

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sns));
        nvps.add(new BasicNameValuePair("content",content));
        nvps.add(new BasicNameValuePair("times","1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);


    }


    /**
     *  查询打印机的状态
     * @param sn
     * @return
     */
    public static String queryPrinterStatus(String sn){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryPrinterStatus"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 查询指定打印机某天的订单统计数
     * @param sn
     * @param date
     * @return
     */
    public static String queryOrderInfoByDate(String sn,String date){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_queryOrderInfoByDate"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("date",date));//yyyy-MM-dd格式

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    /**
     * 修改打印机信息
     * @param sn 打印机编号
     * @param name 打印机备注名称
     * @return
     */
    public static String printerEdit(String sn,String name ){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerEdit"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));
        nvps.add(new BasicNameValuePair("name",name));//打印机备注名称

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 删除打印机
     * @param snlist 打印机编号
     * @return
     */
    public static String printerDelList(String snlist){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printerDelList"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("snlist",snlist));

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 清空待打印队列
     * @param sn 打印机编号
     * @return
     */
    public static String delPrinterSqs(String sn){

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_delPrinterSqs"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sn));

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 兑换打印
     * @param sns
     * @param goodsName
     * @param price
     * @param number
     * @param num
     * @param userName
     * @param phone
     * @param address
     * @param time
     */
    public static void exchange(String sns ,String goodsName  ,String price ,String number ,String num ,String userName ,String phone ,String address ,String time){
        //标签说明：
        //单标签:
        //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
        //成对标签：
        //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
        //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
        //拼凑订单内容时可参考如下格式
        //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式

        String typename = "";

        String content;
        content = "<CB>"+"积分产品兑换单"+"</CB><BR>";
        content += "兑换用户："+userName+"<BR>";
        content += "联系电话："+phone+"<BR>";
        content += "送货地点："+address+"<BR><BR>";
        content += "兑换编号："+number+"<BR>";
        content += "积分商品："+goodsName+"<BR>";
        content += "商品价值："+price+"积分<BR>";
        content += "兑换数量："+num+"<BR>";
        content += "兑换时间："+time;

        content += "送单人：<BR>";
        content += "签收人：<BR>";

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user",USER));
        String STIME = String.valueOf(System.currentTimeMillis()/1000);
        nvps.add(new BasicNameValuePair("stime",STIME));
        nvps.add(new BasicNameValuePair("sig",signature(USER,UKEY,STIME)));
        nvps.add(new BasicNameValuePair("apiname","Open_printMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn",sns));
        nvps.add(new BasicNameValuePair("content",content));
        nvps.add(new BasicNameValuePair("times","1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try
        {
            post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if(statecode == 200){
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null){
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);


    }




    //生成签名字符串
    private static String signature(String USER,String UKEY,String STIME){
        String s = DigestUtils.sha1Hex(USER+UKEY+STIME);
        return s;
    }

    @Override
    public String toString() {
        return "MothPrinter{" +
                "mothPrinterClasses=" + mothPrinterClasses +
                '}';
    }
}
