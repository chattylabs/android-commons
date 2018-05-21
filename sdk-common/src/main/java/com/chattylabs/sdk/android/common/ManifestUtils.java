package com.chattylabs.sdk.android.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ManifestUtils {
    private static final String SECRET_KEY = "com.chattylabs.sdk.android.secretKey";

    public static String getSecretKey(Context context) {
        return getManifestString(context, SECRET_KEY);
    }

    public static String getManifestString(Context context, String key) {
        return getBundle(context).getString(key);
    }

    private static Bundle getBundle(Context context) {
        Bundle bundle;
        try {
            bundle = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bundle;
    }
}
