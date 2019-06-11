package com.shengxian.interceptor;

import com.shengxian.mapper.ShopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Description: controller方法前拦截器
 *
 * @Author: yang
 * @Date: 2019-03-27
 * @Version: 1.0
 */
@Component
public class InterceptorConfig implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);

    @Resource
    private ShopMapper shopMapper;

    //前置
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.info("---------------------开始进入请求地址拦截----------------------------");

        String token = request.getParameter("token");
        String role = request.getParameter("role");
        HashMap hashMap = null;
        if(token == null || token.trim().equals("")){
            request.getRequestDispatcher(ERROR_URL1).forward(request, response);
            return false;
        }
        if(role == null || role.trim().equals("") || (!role.equals("1") && !role.equals("2")) ){
            request.getRequestDispatcher(ERROR_URL2).forward(request, response);
            return false;
        }
        //通过员工token获取店铺信息
        hashMap = shopMapper.interceptorSelectShopInfo(token, Integer.valueOf(role));

        if (hashMap == null ){
            request.getRequestDispatcher(ERROR_URL3).forward(request, response);
            return false;
        }
        //判断店铺是否被禁用了
        if (hashMap.get("b_ble").toString().equals("1")){
            request.getRequestDispatcher(ERROR_URL4).forward(request, response);
            return false;
        }
        //判断员工账号是否被禁用了
        if (hashMap.get("s_ble").toString().equals("1")){
            request.getRequestDispatcher(ERROR_URL4).forward(request, response);
            return false;
        }
        //判断使用期限
        if (Integer.valueOf(hashMap.get("duration").toString()) <= -1 ){
            request.getRequestDispatcher(ERROR_URL5).forward(request, response);
            return false;
        }
        return true;
    }

    //后置
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //环绕
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


    /**
     * token为空
     */

    private static final String  ERROR_URL1 = "/business/ERROR_URL1";

    /**
     * 角色不能为空
     */

    private static final String ERROR_URL2 = "/business/ERROR_URL2";

    /**
     * token失效url
     */

    private static final String ERROR_URL3 = "/business/ERROR_URL3";

    /**
     * 冻结url
     */

    private static final String ERROR_URL4 = "/business/ERROR_URL4";

    /**
     * 店铺使用权限
     */

    private static final String ERROR_URL5 = "/business/ERROR_URL5";
}
