package com.miao.framework.core.classScanner;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 类扫描器
 */
public interface ClassScanner {

    /**
     * 获取指定包名里的所有类
     */
    List<Class<?>> getClassList(String packageName);

    /**
     * 获取指定包下的指定接口或类的子类
     */
    List<Class<?>> getClassBySuperClass(String packageName, Class<?> superClass);

    /**
     * 获取指定注解标识的类
     */
    List<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation);
}
