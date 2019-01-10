package com.miao.framework.core.classTemplate;

import java.lang.annotation.Annotation;

/**
 * 用于获取注解标识的类
 */
public abstract class AnnotationClassTemplate extends ClassTemplate {

    private final Class<? extends Annotation> annotation;

    public AnnotationClassTemplate(String packageName, Class<? extends Annotation> annotation) {
        super(packageName);
        this.annotation = annotation;
    }
}
