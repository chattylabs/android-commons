package com.chattylabs.sdk.android.common;

import android.os.Build;
import android.os.Debug;

public class DeviceUtils {

    /**
     * Determines whether the app is running on aan emulator or on a real device.
     *
     * @return YES if the app is running on an emulator, NO if it is running on a real device
     */
    public static boolean isEmulator() {
        return Build.BRAND.equalsIgnoreCase("generic");
    }

    /**
     * Determines if a debugger is currently attached.
     *
     * @return YES if debugger is attached, otherwise NO.
     */
    public static boolean isDebuggerConnected(){
        return Debug.isDebuggerConnected();
    }
}
