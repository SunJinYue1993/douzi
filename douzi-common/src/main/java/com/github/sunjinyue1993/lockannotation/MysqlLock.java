package com.github.sunjinyue1993.lockannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 同步注解, 标注在需要同步的方法
 * @author SunJY
 * @date 2020/11/2 下午 9:15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MysqlLock {
}
