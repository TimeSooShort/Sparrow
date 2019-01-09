package com.miao.framework.util;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串操作工具类
 */
public class StringUtil {

    /**
     * 是否为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
}
