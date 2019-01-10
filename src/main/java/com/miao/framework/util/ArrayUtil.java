package com.miao.framework.util;

import org.apache.commons.lang.ArrayUtils;

/**
 * 数组操作工具类
 */
public class ArrayUtil {

    /**
     * 判断数组是否为空
     */
    public static boolean isNotEmpty(Object[] arr) {
        return !ArrayUtils.isEmpty(arr);
    }

    public static boolean isEmpty(Object[] arr) {
        return ArrayUtils.isEmpty(arr);
    }
}
