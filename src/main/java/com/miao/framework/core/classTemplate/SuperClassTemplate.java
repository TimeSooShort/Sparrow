package com.miao.framework.core.classTemplate;

/**
 * 用于获取子类
 */
public abstract class SuperClassTemplate extends ClassTemplate {

    protected final Class<?> superClass;

    public SuperClassTemplate(String packageName, Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }
}
