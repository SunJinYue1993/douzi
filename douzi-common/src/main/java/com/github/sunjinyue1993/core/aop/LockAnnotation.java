package com.github.sunjinyue1993.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加在竞争统一资源的方法上，分布式锁生效
 * @author: SunJY
 * @date: 2020/9/17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LockAnnotation {

}
