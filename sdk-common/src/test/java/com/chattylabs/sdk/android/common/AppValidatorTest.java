package com.chattylabs.sdk.android.common;

import android.app.Instrumentation;
import android.test.InstrumentationTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppValidatorTest {

    @Test
    public void testReturnsOk() throws Exception {
        try (MockWebServer server = new MockWebServer()) {

            server.enqueue(new MockResponse().setResponseCode(200));
            // Start the server.
            server.start();
            HttpUrl baseUrl = server.url("/test/");

            File file = mock(File.class);
            when(file.lastModified()).thenReturn(System.currentTimeMillis() - 10);
            when(file.exists()).thenReturn(false);
            OnKeySuccess success = mock(OnKeySuccess.class);
            OnKeyError error = mock(OnKeyError.class);

            AppValidator validator = new AppValidator(
                    "#key",
                    "#package",
                    file,
                    TimeUnit.MINUTES.toMillis(1),
                    success, error);

            validator.setEndpoint(baseUrl.toString());

            validator.execute();

            verify(success).execute();

            // Shut down the server. Instances cannot be reused.
            server.shutdown();
        }
    }
}