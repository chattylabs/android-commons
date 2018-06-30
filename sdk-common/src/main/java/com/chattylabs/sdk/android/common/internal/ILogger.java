package com.chattylabs.sdk.android.common.internal;

public interface ILogger {

    void v(String tag, String msg, Object... args);

    void d(String tag, String msg, Object... args);

    void i(String tag, String msg, Object... args);

    void w(String tag, String msg, Object... args);

    void e(String tag, String msg, Object... args);

    void setup(String userId);

    void logException(Throwable ex);
}