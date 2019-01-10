package com.miao.framework.core.classScanner.impl;

import com.miao.framework.core.classScanner.ClassScanner;
import com.miao.framework.core.classTemplate.AnnotationClassTemplate;
import com.miao.framework.core.classTemplate.ClassTemplate;
import com.miao.framework.core.classTemplate.SuperClassTemplate;

import java.lang.annotation.Annotation;
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

    public List<Class<?>> getClassBySuperClass(String packageName, Class<?> superClass) {
        return new SuperClassTemplate(packageName, superClass) {
            public boolean checkAddClass(Class<?> cls) {
                return superClass.isAssignableFrom(cls) && !superClass.equals(cls);
            }
        }.getClassList();
    }

    public List<Class<?>> getClassByAnnotation(String packageName, final Class<? extends Annotation> annotation) {
        return new AnnotationClassTemplate(packageName, annotation) {
            public boolean checkAddClass(Class<?> cls) {
                return cls.isAnnotationPresent(annotation);
            }
        }.getClassList();
    }

}
