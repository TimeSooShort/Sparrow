package com.miao.framework.core.classScanner;

import java.util.List;

/**
 * 类扫描器
 */
public interface ClassScanner {

    /**
     * 获取指定包名里的所有类
     */
    List<Class<?>> getClassList(String packageName);
}
