package com.chattylabs.sdk.android.common;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class AppValidator extends AsyncTask<Void, Void, Boolean> {
    private static final String SECRET_KEY_ENDPOINT =
            "https://us-central1-chattylabs-98c57.cloudfunctions.net/secretKey";

    private File currentFile;
    private OnKeySuccess onSuccess;
    private OnKeyError onError;
    private String secretKey;
    private String packageName;
    private long exceedTime;

    public AppValidator(String secretKey,
                        String packageName,
                        File secretKeyFile,
                        long exceedTime,
                        OnKeySuccess onSuccess,
                        OnKeyError onError) {
        this.secretKey = secretKey;
        this.packageName = packageName;
        this.exceedTime = exceedTime;
        this.currentFile = secretKeyFile;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return validate();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (!success) {
            onError.execute();
            throw new IllegalAccessError("The secret key does not match");
        } else {
            try {
                boolean isExceeding = currentFile.exists() &&
                        (System.currentTimeMillis() - currentFile.lastModified()) > exceedTime;
                if (!currentFile.exists() || isExceeding) {
                    currentFile.createNewFile();
                }
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
            onSuccess.execute();
        }
    }

    private boolean validate() {
        boolean isExceeding = (System.currentTimeMillis() - currentFile.lastModified()) > exceedTime;
        if (currentFile.exists() && !isExceeding) {
                return true;
        }
        boolean successful = false;
        HttpURLConnection urlConnection = null;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", secretKey);
            parameters.put("package", packageName);
            urlConnection = new HttpURLConnectionBuilder(SECRET_KEY_ENDPOINT)
                    .setRequestMethod("POST")
                    .writeFormFields(parameters)
                    .build();
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            successful = (responseCode == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return successful;
    }
}
