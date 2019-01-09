package com.miao.framework.core.classScanner.impl;

import com.miao.framework.core.classScanner.ClassScanner;
import com.miao.framework.core.classTemplate.ClassTemplate;

import java.util.List;

/**
 * 默认类扫描器
 */
public class DefaultClassScanner implements ClassScanner {

    public List<Class<?>> getClassList(String packageName) {
        return new ClassTemplate(packageName) {
            public boolean checkAddClass(Class<?> cls) {
                return cls.getName().startsWith(packageName);
            }
        }.getClassList();
    }
}
