package com.chattylabs.android.commons;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ThreadUtilsTest {

    public static final String RESULT = "1234";
    private ThreadUtils.SerialThread serialThread;
    private String test;

    @Before
    public void setUp() {
        serialThread = ThreadUtils.newSerialThread();
        test = "";
    }

    @After
    public void tearDown() {
        serialThread.awaitAfterTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void newSerialThread() throws InterruptedException {
        AsyncTester tester = new AsyncTester(() -> {
            System.out.println("--start");
            serialThread.addTask(() -> test += "1");
            serialThread.addTask(() -> test += "2");
            serialThread.addTask(() -> test += "3");
            serialThread.addTask(() -> {
                test += "4";
                serialThread.shutdown();
                Assert.assertEquals(RESULT, test);
            });
        });
        tester.start();
        tester.test();
    }

    @Test
    public void newSerialThreadBlocking_withTimer() throws InterruptedException {
        AsyncTester tester = new AsyncTester(() -> {
            System.out.println("--start");
            serialThread.addTaskBlocking(() -> {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        test += "1";
                        serialThread.release();
                    }
                }, 1000);
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                test += "2";
                serialThread.release();
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        test += "3";
                        serialThread.release();
                    }
                }, 1000);
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                test += "4";
                serialThread.release();
                serialThread.shutdown();
                Assert.assertEquals(RESULT, test);
            },10, TimeUnit.SECONDS);
        });
        tester.start();
        tester.test();
    }

    @Test
    public void newSerialThreadBlocking_withoutTimer() throws InterruptedException {
        AsyncTester tester = new AsyncTester(() -> {
            System.out.println("--start");
            serialThread.addTaskBlocking(() -> {
                test += "1";
                serialThread.release();
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                test += "2";
                serialThread.release();
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                test += "3";
                serialThread.release();
            },10, TimeUnit.SECONDS);
            serialThread.addTaskBlocking(() -> {
                test += "4";
                serialThread.release();
                serialThread.shutdown();
                Assert.assertEquals(RESULT, test);
            },10, TimeUnit.SECONDS);
        });
        tester.start();
        tester.test();
    }

}