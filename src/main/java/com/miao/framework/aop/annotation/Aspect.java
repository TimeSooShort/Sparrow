package com.miao.framework.aop.annotation;

import java.lang.annotation.*;

/**
 * 切面，标明该代理类代理的类对象
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 通过包路径标识代理的类
     */
    String pkg() default "";

    /**
     * 通过Class类型标识代理的类
     */
    String cls() default "";

    /**
     * 通过接口标识代理的类
     */
    Class<? extends Annotation> annotation() default Aspect.class;
}
