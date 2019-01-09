package com.miao.framework;

import com.miao.framework.core.ConfigHelper;
import com.miao.framework.util.ObjectUtil;
import com.miao.framework.util.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例工厂类
 */
public class InstanceFactory {

    /**
     * 存储实现类的实例
     */
    private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

    @SuppressWarnings("unchecked")
    private static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
        // 先查缓存cache
        if (cache.containsKey(cacheKey)) {
            return (T) cache.get(cacheKey);
        }
        // 获取配置文件中配置的实现类， 没有则采用默认实现类
        String implClassName = ConfigHelper.getSrting(cacheKey);
        if (StringUtil.isEmpty(implClassName)) {
            implClassName = defaultImplClass.getName();
        }
        // 反射获取实例
        T instance = ObjectUtil.newInstance(implClassName);
        if (instance != null) {
            cache.put(cacheKey, instance);
        }
        return instance;
    }
}
