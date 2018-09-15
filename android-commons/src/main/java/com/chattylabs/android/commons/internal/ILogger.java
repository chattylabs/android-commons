package com.chattylabs.sdk.android.common.internal;

@dagger.Reusable
public interface ILogger {

    void v(String tag, String msg, Object... args);

    void d(String tag, String msg, Object... args);

    void i(String tag, String msg, Object... args);

    void w(String tag, String msg, Object... args);

    void e(String tag, String msg, Object... args);

    void setup(String userId);

    void setBuildDebug(boolean debug);

    void logException(Throwable ex);
}
