package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */
@Target(ElementType.METHOD) //表示这个注解只能用于方法上
@Retention(RetentionPolicy.RUNTIME)
//注解会保留到运行时，并且可以通过反射获取。
// 常用于框架（如 Spring）中需要在运行时对注解进行处理的场景。
public @interface AutoFill
{
    //数据库操作类型：UPDATE INSERT
    OperationType value();
}
