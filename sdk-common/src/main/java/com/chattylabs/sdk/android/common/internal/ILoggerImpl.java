package com.chattylabs.sdk.android.common.internal;

import android.util.Log;

import com.chattylabs.sdk.android.common.BuildConfig;

public class ILoggerImpl implements ILogger {

    @Override
    public void v(String tag, String msg, Object... args) {
        sendByLog(Log.VERBOSE, tag, msg, args);
    }

    @Override
    public void d(String tag, String msg, Object... args) {
        sendByLog(Log.DEBUG, tag, msg, args);
    }

    @Override
    public void i(String tag, String msg, Object... args) {
        sendByLog(Log.INFO, tag, msg, args);
    }

    @Override
    public void w(String tag, String msg, Object... args) {
        sendByLog(Log.WARN, tag, msg, args);
    }

    @Override
    public void e(String tag, String msg, Object... args) {
        sendByLog(Log.ERROR, tag, msg, args);
    }

    @Override
    public void setup(String userId) {}

    @Override
    public void logException(Throwable ex) {
        Log.wtf("ILogger", ex);
    }

    private void sendByLog(int priority, String tag, String msg, Object... args) {
        if (BuildConfig.DEBUG) {
            Log.println(priority, tag, formatMessage(msg, args));
        }
    }

    private String formatMessage(String message, Object[] args) {
        return (args != null && args.length > 0) ? String.format(message, args) : message;
    }
}
