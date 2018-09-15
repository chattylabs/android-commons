package com.chattylabs.android.commons.internal;

import android.util.Log;

import javax.inject.Inject;

@dagger.Reusable
public class ILoggerImpl implements ILogger {
    private boolean isBuildDebug;

    @Inject
    public ILoggerImpl() {
    }

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
    public void setBuildDebug(boolean debug) {
        this.isBuildDebug = debug;
    }

    @Override
    public void logException(Throwable ex) {
        Log.wtf("ILogger", ex);
    }

    private void sendByLog(int priority, String tag, String msg, Object... args) {
        if (isBuildDebug) {
            Log.println(priority, tag, formatMessage(msg, args));
        }
    }

    private String formatMessage(String message, Object[] args) {
        return (args != null && args.length > 0) ? String.format(message, args) : message;
    }
}
