package com.chattylabs.android.commons;

import android.os.Build;
import android.os.Debug;

public class DeviceUtils {

    /**
     * Determines whether the app is running on an emulator or on a real device.
     *
     * @return TRUE if the app is running on an emulator, FALSE if it is running on a real device
     */
    public static boolean isEmulator() {
        return Build.BRAND.equalsIgnoreCase("generic");
    }

    /**
     * Determines if a debugger is currently attached.
     *
     * @return TRUE if debugger is attached, FALSE otherwise.
     */
    public static boolean isDebuggerConnected(){
        return Debug.isDebuggerConnected();
    }
}
