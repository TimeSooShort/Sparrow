package com.miao.framework.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 */
public abstract class AspectProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    public Object dpProxy(ProxyChain proxyChain) throws Throwable {
        Object methodResult;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getMethod();
        Object[] params = proxyChain.getParameters();

        begin();
        try {
            if (intercept(cls, method, params)) {
                before(cls, method, params);
                methodResult = proxyChain.doProxyChain();
                after(cls, method, params);
            } else {
                methodResult = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            logger.error("AOP异常", e);
            error(cls, method, params, e);
            throw e;
        } finally {
            end();
        }
        return methodResult;
    }


    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void begin(){}

    public void end(){}

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {}

    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {}

    public void error(Class<?> cls, Method method, Object[] params, Throwable e){}
}
