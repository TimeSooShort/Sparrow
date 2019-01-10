package com.miao.framework.aop.proxy;

/**
 * 代理接口
 */
public interface Proxy {

    Object dpProxy(ProxyChain proxyChain) throws Throwable;
}
