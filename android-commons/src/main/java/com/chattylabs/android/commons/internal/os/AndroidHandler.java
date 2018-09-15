package com.chattylabs.android.commons.internal.os;

public interface AndroidHandler {

    boolean postDelayed(Runnable r, long delayMillis);

    boolean post(Runnable r);

    void removeCallbacksAndMessages(Object token);

}
