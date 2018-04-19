package com.chattylabs.sdk.android.common.internal.android;

import android.os.Handler;
import android.os.Looper;

public class AndroidHandlerImpl implements AndroidHandler {
    private Handler handler;

    public AndroidHandlerImpl() {
        handler = new Handler();
    }

    public AndroidHandlerImpl(Looper looper) {
        handler = new Handler(looper);
    }

    @Override
    public boolean postDelayed(Runnable r, long delayMillis) {
        EspressoIdlingResource.BLOCK();
        return handler.postDelayed(() -> {
            r.run(); EspressoIdlingResource.CONTINUE();
        }, delayMillis);
    }

    @Override
    public boolean post(Runnable r) {
        EspressoIdlingResource.BLOCK();
        return handler.post(() -> {
            r.run(); EspressoIdlingResource.CONTINUE();
        });
    }

    @Override
    public void removeCallbacksAndMessages(Object token) {
        handler.removeCallbacksAndMessages(token);
    }
}
