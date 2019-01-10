package com.miao.framework.core.fault;

/**
 * 初始化错误
 */
public class InitializationError extends Error {
    public InitializationError() {
    }

    public InitializationError(String message) {
        super(message);
    }

    public InitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationError(Throwable cause) {
        super(cause);
    }

    public InitializationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
