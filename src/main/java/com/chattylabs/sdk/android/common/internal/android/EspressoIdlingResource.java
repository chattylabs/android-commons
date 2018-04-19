package com.chattylabs.sdk.android.common.internal.android;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource mCountingIdlingResource = new CountingIdlingResource(RESOURCE);

    public static void BLOCK() {
        mCountingIdlingResource.increment();
    }

    public static void CONTINUE() {
        mCountingIdlingResource.decrement();
    }

    public static boolean isIdleNow() {
        return mCountingIdlingResource.isIdleNow();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
