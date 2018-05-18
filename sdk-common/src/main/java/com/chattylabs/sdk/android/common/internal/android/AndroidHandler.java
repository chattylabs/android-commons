package com.chattylabs.sdk.android.common.internal.android;

public interface AndroidHandler {

    boolean postDelayed(Runnable r, long delayMillis);

    boolean post(Runnable r);

    void removeCallbacksAndMessages(Object token);

}
