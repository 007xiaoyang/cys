package com.shengxian.sysLog;

import java.lang.annotation.*;

/**
 * Description: 系统日志注解
 *
 * @Target 所有标注了指定注解的类
 * @Retention 注解说明,这种类型的注解会被保留到那个阶段.
 * @Documented 注解表明这个注解应该被 javadoc工具记录.
 *
 * @Author: yang
 * @Date: 2019-03-27
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 模块
     * @return
     */
    String module()  default "";

    /**
     * 操作的方法
     * @return
     */
    String methods()  default "";
}
