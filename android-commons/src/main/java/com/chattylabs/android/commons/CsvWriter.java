package com.chattylabs.android.commons;


import android.Manifest;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class CsvWriter implements RequiredPermissions {
    private static CsvWriter instance;

    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String DATETIME = "DATETIME";

    private String filename;
    private String delimiter;
    private File directory;
    private BufferedWriter file;
    private String empty;
    private String[] headers;
    private ThreadUtils.SerialThread serialThread;

    public static CsvWriter getInstance() {
        return instance == null ? (instance = new CsvWriter()) : instance;
    }

    private CsvWriter(){}

    @Override
    public String[] requiredPermissions() {
        return new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    private File getLogFile() {
        directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String name = filename + ".csv";
        return new File(directory, name);
    }

    public CsvWriter init(String filename, String delimiter, String empty) {
        this.filename = filename;
        this.delimiter = delimiter;
        this.empty = empty;
        this.serialThread = ThreadUtils.newSerialThread();
        return this;
    }

    public void addHeaders(Context context, String... headers) {
        this.headers = headers;
        serialThread.addTask(() -> init(context, headers));
    }

    private void init(Context context, String... headers) {
        File theFile = getLogFile();
        if (!theFile.exists()) {
            StringBuilder line = new StringBuilder();
            for (String value : headers) {
                line.append(delimiter).append(value);
            }
            line = new StringBuilder(TIMESTAMP + delimiter + DATETIME + line);

            try {
                // Make sure the Pictures directory exists.
                //noinspection ResultOfMethodCallIgnored
                directory.mkdirs();
                file = new BufferedWriter(new FileWriter(theFile));
                file.write(line.toString());
                file.newLine();
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (file == null) file = new BufferedWriter(new FileWriter(theFile, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (context != null) refreshMediaScanner(context);
    }

    @SafeVarargs
    public final void write(Context context, Pair<String, String>... keyValues) {
        serialThread.addTask(() -> writeLocal(context, keyValues));
    }

    @SafeVarargs
    private final void writeLocal(Context context, Pair<String, String>... keyValues) {
        if (file == null) {
            return;
        }

        init(filename, delimiter, empty).addHeaders(context, headers);

        StringBuilder line = new StringBuilder();

        head:
        for (String header : headers) {
            for (Pair<String, String> aKeyValue : keyValues) {
                if (header.equals(aKeyValue.first)) {
                    line.append(delimiter).append(aKeyValue.second);
                    continue head;
                }
            }
            line.append(delimiter).append(empty);
        }

        String timestamp = Long.toString(System.currentTimeMillis());
        String datetime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        line = new StringBuilder(timestamp + delimiter + datetime + line);

        try {
            file.write(line.toString());
            file.newLine();
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (context != null) refreshMediaScanner(context);
    }

    public void close(Context context) {
        if (file == null) {
            return;
        }

        try {
            file.close();
            serialThread.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (context != null) refreshMediaScanner(context);
    }

    private void refreshMediaScanner(Context context) {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context, new String[] { file.toString() }, null, null);
    }
}
