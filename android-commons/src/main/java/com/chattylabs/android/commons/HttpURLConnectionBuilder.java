package com.chattylabs.android.commons;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpURLConnectionBuilder {

    private static final int DEFAULT_TIMEOUT = 2 * 60 * 1000;
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final long FORM_FIELD_LIMIT = 4 * 1024 * 1024;
    public static final int FIELDS_LIMIT = 25;

    private final String mUrlString;

    private String mRequestMethod;
    private String mRequestBody;
    private int mTimeout = DEFAULT_TIMEOUT;

    private final Map<String, String> mHeaders;

    public HttpURLConnectionBuilder(String urlString) {
        mUrlString = urlString;
        mHeaders = new HashMap<>();
        mHeaders.put("User-Agent", "ChattyLabs/Android");
    }

    public HttpURLConnectionBuilder setRequestMethod(String requestMethod) {
        mRequestMethod = requestMethod;
        return this;
    }

    public HttpURLConnectionBuilder setRequestBody(String requestBody) {
        mRequestBody = requestBody;
        return this;
    }

    public HttpURLConnectionBuilder writeFormFields(Map<String, String> fields) {

        // We should add limit on fields because a large number of fields can throw the OOM exception
        if (fields.size() > FIELDS_LIMIT) {
            throw new IllegalArgumentException("Fields size too large: " + fields.size() +
                    " - max allowed: " + FIELDS_LIMIT);
        }

        for (String key: fields.keySet()) {
            String value = fields.get(key);
            if (value != null && value.length() > FORM_FIELD_LIMIT) {
                throw new IllegalArgumentException("Form field " + key + " size too large: " +
                        value.length() + " - max allowed: " + FORM_FIELD_LIMIT);
            }
        }

        try {
            String formString = getFormString(fields, DEFAULT_CHARSET);
            setHeader("Content-Type", "application/x-www-form-urlencoded");
            setRequestBody(formString);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    public HttpURLConnectionBuilder setTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout has to be positive.");
        }
        mTimeout = timeout;
        return this;
    }

    public HttpURLConnectionBuilder setHeader(String name, String value) {
        mHeaders.put(name, value);
        return this;
    }

    public HttpURLConnection build() throws IOException {
        HttpURLConnection connection;
        URL url = new URL(mUrlString);
        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(mTimeout);
        connection.setReadTimeout(mTimeout);

        if (!TextUtils.isEmpty(mRequestMethod)) {
            connection.setRequestMethod(mRequestMethod);
            if (!TextUtils.isEmpty(mRequestBody) || mRequestMethod.equalsIgnoreCase("POST") ||
                    mRequestMethod.equalsIgnoreCase("PUT")) {
                connection.setDoOutput(true);
            }
        }

        for (String name : mHeaders.keySet()) {
            connection.setRequestProperty(name, mHeaders.get(name));
        }

        if (!TextUtils.isEmpty(mRequestBody)) {
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, DEFAULT_CHARSET));
            writer.write(mRequestBody);
            writer.flush();
            writer.close();
        }

        return connection;
    }

    private static String getFormString(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        List<String> protoList = new ArrayList<>();
        for (String key : params.keySet()) {
            String value = params.get(key);
            key = URLEncoder.encode(key, charset);
            value = URLEncoder.encode(value, charset);
            protoList.add(key + "=" + value);
        }
        return TextUtils.join("&", protoList);
    }
}
