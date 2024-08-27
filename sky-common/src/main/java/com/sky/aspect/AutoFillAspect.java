package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

//AOP知识复习:
// 1.连接点：JoinPoint**，可以被AOP控制的方法（暗含方法执行时的相关信息）
// 2. 通知：Advice**，指哪些重复的逻辑，也就是共性功能（最终体现为一个方法）
// 3.切入点：PointCut**，匹配连接点的条件，通知仅会在切入点方法执行时被应用
// 4.切面：Aspect**，描述通知与切入点的对应关系（通知+切入点）
///前置通知 @Before 环绕通知@Around 后置通知@After 返回后通知@AfterReturning 异常通知@AfterThrowing
//返回后通知（程序在正常执行的情况下，会执行的后置通知） 异常通知（程序在出现异常的情况下，执行的后置通知）
//- 目标方法前的通知方法：字母排名靠前的先执行
//- 目标方法后的通知方法：字母排名靠前的后执行
@Aspect
@Component
@Slf4j
public class AutoFillAspect
{
    /**
     * 切入点： com.sky.mapper 包下的类中，所有带有 @AutoFill 注解的方法。
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint)
    {
        log.info("开始进行公共字段自动填充...");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0)
        {
            return;
        }

        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT)
        {
            //为4个公共字段赋值
            //这里使用反射而不是直接调用entity的set方法原因在于：使用中可能需要对多个类进行这样的AOP操作，如果调用set方法，则对每个类都要具体写对应代码
            //getArgs得到的entity是Object类型，如果要使用具体的set方法，则需要进行对应的强制类型转换，这样子就失去了AOP的通用性
            try
            {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(operationType == OperationType.UPDATE)
        {
            //为2个公共字段赋值
            try
            {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
