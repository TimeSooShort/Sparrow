package com.miao.framework.aop.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理链
 */
public class ProxyChain {

    private final Object target;
    private final Method method;
    private final Object[] parameters;
    private final MethodProxy methodProxy;
    private final Class<?> targetClass;
    private final List<Proxy> proxyBeanList;

    private int index = 0;

    public ProxyChain(Object target, Method method, Object[] parameters,
                      MethodProxy methodProxy, Class<?> targetClass, List<Proxy> proxyBeanList) {
        this.target = target;
        this.method = method;
        this.parameters = parameters;
        this.methodProxy = methodProxy;
        this.targetClass = targetClass;
        this.proxyBeanList = proxyBeanList;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult = null;
        if (index < proxyBeanList.size()) {
            methodResult = proxyBeanList.get(index++).dpProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(target, parameters);
        }
        return methodResult;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
