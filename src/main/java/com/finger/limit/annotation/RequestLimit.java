package com.finger.limit.annotation;

import java.lang.annotation.*;
 
/**
 * create by: lixh
 * description: 自定义限流注解
 * create time: 2021/8/4 9:35
 * @return
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
 
    /**
     * 时间内  秒为单位
     */
    int second() default 10;
 
 
 
    /**
     *  允许访问次数
     */
    int maxCount() default 5;
 
 
    //默认效果 ： 10秒内 对于使用该注解的接口，只能总请求访问数 不能大于 5次
 
}