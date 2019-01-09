package com.miao.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象操作工具类
 */
public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * 通过反射创建实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        T instance;
        try {
            Class<?> cls = ClassUtil.loadClass(className);
            instance = (T) cls.newInstance();
        } catch (Exception e) {
            logger.error("获取"+className+"实例失败", e);
            throw new RuntimeException(e);
        }
        return instance;
    }
}
