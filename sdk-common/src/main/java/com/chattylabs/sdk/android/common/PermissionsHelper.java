package com.chattylabs.sdk.android.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public abstract class PermissionsHelper {

    public static final int REQUEST_CODE = 100010;

    public static void check(Activity activity, String[] permissions, Runnable runnable) {
        if (needsPermissions(activity.getApplicationContext(), permissions)) {

            // Should we show an explanation?
            if (needsExplanation(activity, permissions)) {

                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);

                // TODO
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity, permissions, 10);
            }
        } else {
            runnable.run();
        }
    }

    public static boolean needsPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }

    public  static boolean isPermissionGranted(@NonNull int[] grantResults) {
        return grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionRequest(int requestCode) {
        return requestCode == REQUEST_CODE;
    }

    private static boolean needsExplanation(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }
}
