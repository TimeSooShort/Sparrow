package com.miao.framework.aop;

import com.miao.framework.InstanceFactory;
import com.miao.framework.aop.annotation.Aspect;
import com.miao.framework.aop.annotation.AspectOrder;
import com.miao.framework.aop.proxy.AspectProxy;
import com.miao.framework.aop.proxy.Proxy;
import com.miao.framework.aop.proxy.ProxyManager;
import com.miao.framework.core.ClassHelper;
import com.miao.framework.core.classScanner.ClassScanner;
import com.miao.framework.core.fault.InitializationError;
import com.miao.framework.ioc.BeanHelper;
import com.miao.framework.util.ClassUtil;
import com.miao.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AOP入口
 */
public class AopHelper {

    private static final ClassScanner scanner = InstanceFactory.getClassScanner();

    static {
        try {
            // 代理类AspectProxy class -> 被代理类的集合target class list
            Map<Class<?>, List<Class<?>>> proxyWithTargetsMap = createProxyWithTargetsMap();
            // 被代理类targetClass -> 其代理类实例AspectProxy instance
            Map<Class<?>, List<Proxy>> targetWithProxyInstanceMap = createTargetWithProxyInstanceMap(proxyWithTargetsMap);
            // 遍历，生成被代理类的代理对象实例， 加入到BeanHelper里的beanMap，即放入到Bean容器中
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetWithProxyInstanceMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxyInstanceList = entry.getValue();
                // CGlib生成目标代理类
                Object targetProxyInstance = ProxyManager.createProxy(targetClass, proxyInstanceList);
                BeanHelper.setBeanMap(targetClass, targetProxyInstance);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InitializationError("初始化AOPHelper出错", e);
        }
    }

    private static Map<Class<?>, List<Class<?>>> createProxyWithTargetsMap() {
        Map<Class<?>, List<Class<?>>> proxyWithTargetsMap = new HashMap<>();
        addAspectProxy(proxyWithTargetsMap);
        return proxyWithTargetsMap;
    }

    /**
     * 构造切面代理map： 代理类AspectProxy class -> 被代理类的集合target class list
     */
    private static void addAspectProxy(Map<Class<?>, List<Class<?>>> proxyWithTargetMap) {
        // 通过父类AspectProxy查找所有代理类
        List<Class<?>> aspectProxyClassList = ClassHelper.getClassListBySuper(AspectProxy.class);
        // 排序，有AspectOrder注解的类排在前
        sortAspectProxyClassList(aspectProxyClassList);
        for (Class<?> aspectProxyClass : aspectProxyClassList) {
            if (aspectProxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = aspectProxyClass.getAnnotation(Aspect.class);
                // 通过Aspect里的信息查找被该代理类代理的所有被代理类
                List<Class<?>> targetClassList = createTargetClassListByAspect(aspect);
                proxyWithTargetMap.put(aspectProxyClass, targetClassList);
            }
        }
    }

    /**
     * 通过代理类的Aspect注解得到被其代理的所有类，AspectProxy class -> target class list
     */
    private static List<Class<?>> createTargetClassListByAspect(Aspect aspect) {
        List<Class<?>> targetClassList = new ArrayList<>();
        String targetPackage = aspect.pkg(); // 被代理类的包地址
        String targetClass = aspect.cls(); // 被代理类的class类型
        Class<? extends Annotation> targetAnnotation = aspect.annotation(); //被代理类的注解类型
        if (StringUtil.isNotEmpty(targetPackage)) {
            // 包+class 确定一个target class
            if (StringUtil.isNotEmpty(targetClass)) {
                targetClassList.add(ClassUtil.loadClass(targetPackage+"."+targetClass));
            } else {
                // 包+注解（非Aspect）
                if (!targetAnnotation.equals(Aspect.class)) {
                    targetClassList.addAll(scanner.getClassByAnnotation(targetPackage, targetAnnotation));
                } else {
                    // 包
                    targetClassList.addAll(scanner.getClassList(targetPackage));
                }
            }
        }else if (!targetAnnotation.equals(Aspect.class)) {
            // 只有注解（非Aspect），默认根目录
            targetClassList.addAll(ClassHelper.getClassListByAnnotation(targetAnnotation));
        }
        return targetClassList;
    }

    /**
     * 创建 被代理类targetClass -> 其代理类实例AspectProxy instance 的Map
     */
    private static Map<Class<?>, List<Proxy>> createTargetWithProxyInstanceMap(
            Map<Class<?>, List<Class<?>>> proxyWithTargetsMap) throws IllegalAccessException, InstantiationException {
        Map<Class<?>, List<Proxy>> targetWithProxyInstanceMap = new HashMap<>();
        for (Map.Entry<Class<?>, List<Class<?>>> entry : proxyWithTargetsMap.entrySet()) {
            Class<?> proxyClass = entry.getKey(); // 代理类
            List<Class<?>> targetClassList = entry.getValue(); // 被代理类集合
            for (Class<?> targetClass : targetClassList) {
                // 每次都创建一个代理类实例
                Proxy proxyInstance = (Proxy) proxyClass.newInstance();
                if (targetWithProxyInstanceMap.containsKey(targetClass)) {
                    targetWithProxyInstanceMap.get(targetClass).add(proxyInstance);
                }else {
                    ArrayList<Proxy> proxyInstanceList = new ArrayList<>();
                    proxyInstanceList.add(proxyInstance);
                    targetWithProxyInstanceMap.put(targetClass, proxyInstanceList);
                }
            }
        }
        return targetWithProxyInstanceMap;
    }

    /**
     * 排序，规则是AspectOrder注解的类在前
     */
    private static void sortAspectProxyClassList(List<Class<?>> aspectProxyClassList) {
        aspectProxyClassList.sort(new Comparator<Class<?>>() {
            public int compare(Class<?> o1, Class<?> o2) {
                // 规则是AspectOrder注解的类在前
                if (o1.isAnnotationPresent(AspectOrder.class) && o2.isAnnotationPresent(AspectOrder.class)) {
                    return getAspectOrderValue(o1) - getAspectOrderValue(o2);
                } else if (o1.isAnnotationPresent(AspectOrder.class) || o2.isAnnotationPresent(AspectOrder.class)) {
                    return getAspectOrderValue(o2);
                } else {
                    return o1.hashCode() - o2.hashCode();
                }
            }

            private int getAspectOrderValue(Class<?> o) {
                return o.isAnnotationPresent(AspectOrder.class) ?
                        o.getAnnotation(AspectOrder.class).value() : -1;
            }
        });
    }
}
