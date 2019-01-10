package com.miao.framework.core;

import com.miao.framework.InstanceFactory;
import com.miao.framework.constant.FrameworkConstant;
import com.miao.framework.core.classScanner.ClassScanner;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Class的帮助类
 */
public class ClassHelper {

    /**
     * 获取用户项目的根目录
     */
    private static final String BASE_PACKAGE = ConfigHelper.getSrting(FrameworkConstant.BASE_PACKAGE);

    /**
     * 启动时实例化类扫描器
     */
    private static final ClassScanner scanner = InstanceFactory.getClassScanner();

    /**
     * 加载项目跟目录下的所有类，并不初始化
     */
    public static List<Class<?>> getClassList() {
        return scanner.getClassList(BASE_PACKAGE);
    }

    /**
     * 获取指定包下的指定接口或类的子类
     */
    public static List<Class<?>> getClassListBySuper(Class<?> superClass) {
        return scanner.getClassBySuperClass(BASE_PACKAGE, superClass);
    }

    /**
     * 获取被指定注解标识的类
     */
    public static List<Class<?>> getClassListByAnnotation(Class<? extends Annotation> annotation) {
        return scanner.getClassByAnnotation(BASE_PACKAGE, annotation);
    }
}
