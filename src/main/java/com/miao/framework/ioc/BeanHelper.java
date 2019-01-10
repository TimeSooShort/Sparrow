package com.miao.framework.ioc;

import com.miao.framework.core.ClassHelper;
import com.miao.framework.core.fault.InitializationError;
import com.miao.framework.ioc.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化被注解的类
 */
public class BeanHelper {

    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    /**
     *  类的Class对象与其Bean实例
     */
    private static final Map<Class<?>, Object> beanMap = new HashMap<>();

    static {
        try {
            // 获取所有class列表
            List<Class<?>> classList = ClassHelper.getClassList();
            // 遍历找寻被注解的类，初始化
            for (Class<?> cls : classList) {
                if (cls.isAnnotationPresent(Bean.class)) {
                    Object instance = cls.newInstance();
                    beanMap.put(cls, instance);
                }
            }
        } catch (Exception e) {
            logger.error("初始化BeanHelper出错", e);
            throw new InitializationError("初始化BeanHelper出错", e);
        }
    }

    /**
     * 获取beanMap
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }

    /**
     * 设置Bean实例
     */
    public static void setBeanMap(Class<?> cls, Object object) {
        beanMap.put(cls, object);
    }
}
