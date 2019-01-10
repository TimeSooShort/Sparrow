package com.miao.framework.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理生成
 */
public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyBeanList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(o, method, objects, methodProxy, targetClass, proxyBeanList).doProxyChain();
            }
        });
    }
}
