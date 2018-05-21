package com.chattylabs.sdk.android.common;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecretKeyValidator extends AsyncTask<Void, Void, Boolean> {
    private static final long CACHED_TIME = TimeUnit.DAYS.toMillis(5);
    private static final String SECRET_KEY_ENDPOINT =
            "https://us-central1-chattylabs-98c57.cloudfunctions.net/secretKey";

    private File directory;
    private OnKeySuccess onSuccess;
    private OnKeyError onError;
    private String secretKey;

    public SecretKeyValidator(String secretKey, File directory, OnKeySuccess onSuccess, OnKeyError onError) {
        this.secretKey = secretKey;
        this.directory = directory;
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
                new File(directory.getAbsolutePath(), secretKey).createNewFile();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
            onSuccess.execute();
        }
    }

    private boolean validate() {
        File currentFileInstance = new File(directory.getAbsolutePath() +
                File.separator + secretKey);
        if (currentFileInstance.exists() &&
            (System.currentTimeMillis() - currentFileInstance.lastModified()) <= CACHED_TIME) {
                return true;
        }
        boolean successful = false;
        HttpURLConnection urlConnection = null;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", secretKey);
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
