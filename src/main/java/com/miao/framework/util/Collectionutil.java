package com.miao.framework.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * 集合操作工具类
 */
public class Collectionutil {

    /**
     * 集合是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }
}
