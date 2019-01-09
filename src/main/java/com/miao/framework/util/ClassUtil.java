package com.miao.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 */
public class ClassUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className, boolean isInitialize) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("加载类出错。", e);
            throw new RuntimeException(e);
        }
        return cls;
    }
}
