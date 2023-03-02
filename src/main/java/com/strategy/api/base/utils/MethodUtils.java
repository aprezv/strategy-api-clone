package com.strategy.api.base.utils;

/**
 * Created on 2020-11-13.
 */
public class MethodUtils {
    public static String getMethodName() {
        StackTraceElement callingMethod = Thread.currentThread().getStackTrace()[2];
        return String.format("%s.%s",callingMethod.getClassName(), callingMethod.getMethodName());
    }

    public static String getMessageCode(String suffix) {
        StackTraceElement callingMethod = Thread.currentThread().getStackTrace()[2];
        return String.format("%s.%s.%s",callingMethod.getClassName(), callingMethod.getMethodName(), suffix);
    }
}
