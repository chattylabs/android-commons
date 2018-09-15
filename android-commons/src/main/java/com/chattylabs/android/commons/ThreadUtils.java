package com.chattylabs.sdk.android.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    public interface SerialThread {
        void addTask(Runnable task);

        boolean acceptTask();

        void shutdown();

        void shutdownNow();
    }

    public static SerialThread newSerialThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return new SerialThread() {

            @Override
            public void addTask(Runnable task) {
                if (acceptTask()) executorService.submit(task);
            }

            @Override
            public boolean acceptTask() {
                return !executorService.isTerminated() && !executorService.isShutdown();
            }

            @Override
            public void shutdown() {
                executorService.shutdown();
                try {
                    executorService.awaitTermination(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void shutdownNow() {
                executorService.shutdownNow();
            }
        };
    }
}
