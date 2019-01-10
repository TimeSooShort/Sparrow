package com.miao.framework.ioc;

import com.miao.framework.core.ClassHelper;
import com.miao.framework.core.fault.InitializationError;
import com.miao.framework.ioc.annotation.Impl;
import com.miao.framework.ioc.annotation.Inject;
import com.miao.framework.util.ArrayUtil;
import com.miao.framework.util.Collectionutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * IOC功能的实现类
 */
public class IocHelper {

    static {
        try {
            // 获取bean map
            Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
            for (Map.Entry<Class<?>, Object> entryMap : beanMap.entrySet()) {
                Class<?> beanClass = entryMap.getKey();
                Object beanInstance = entryMap.getValue();
                // 该类的所有字段
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        // 查找Inject注解的字段
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 获取该字段的类名
                            Class<?> className = beanField.getType();
                            // 查找其实现类
                            Class<?> implClass = findImplClass(className);
                            if (implClass != null) {
                                // 获取该实现类的bean
                                Object implInstance = beanMap.get(implClass);
                                if (implInstance != null) {
                                    // 注入实现类
                                    beanField.setAccessible(true);
                                    beanField.set(beanInstance, implInstance);
                                } else {
                                    throw new InitializationError("依赖注入失败！类名："+
                                            beanClass.getSimpleName()+"，字段名："+className.getSimpleName());
                                }
                            }
                        }
                    }
                }

            }
        } catch (IllegalAccessException e) {
            throw new InitializationError("初始化 IocHelper 错误", e);
        }
    }

    /**
     * 三种情况：superClass是具体实现类，直接返回；
     * superClass有Impl注解，指定了实现类
     * superClass是接口或者有子类的类则返回其第一个实现类
     */
    private static Class<?> findImplClass(Class<?> superClass) {
        Class<?> implClass = superClass;
        // 如果该类有Impl注解，说明有指定实现类
        if (superClass.isAnnotationPresent(Impl.class)){
            implClass = superClass.getAnnotation(Impl.class).value();
        } else {
            // 通过父类查找子类，返回第一个实现类
            List<Class<?>> classList = ClassHelper.getClassListBySuper(superClass);
            if (Collectionutil.isNotEmpty(classList)) {
                implClass = classList.get(0);
            }
        }
        return implClass;
    }
}
