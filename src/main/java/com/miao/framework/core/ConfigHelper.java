package com.miao.framework.core;

import com.miao.framework.constant.FrameworkConstant;
import com.miao.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 获取sparrow.properties属性文件的属性值
 */
public class ConfigHelper {

    /**
     * sparrow.properties
     */
    private static final Properties props = PropsUtil.loadProps(FrameworkConstant.CONFIG_PROPS);

    /**
     * 获取属性值
     */
    public static String getSrting(String key) {
        return PropsUtil.getString(props, key);
    }
}
