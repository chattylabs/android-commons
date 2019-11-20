package chattylabs.android.commons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadUtils {

    public interface SerialThread {
        void addTask(Runnable task);

        void addTaskBlocking(Runnable task, long time, TimeUnit unit);

        boolean acceptTask();

        void shutdown();

        void shutdownNow();

        void release();

        void awaitAfterTermination(long time, TimeUnit unit);
    }

    public static SerialThread newSerialThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return new SerialThread() {
            private int hold = 0;
            // Lock
            private final Lock lock = new ReentrantLock();
            private final Condition condition = lock.newCondition();

            @Override
            public void addTask(Runnable task) {
                if (acceptTask()) {
                    Future<?> future = executorService.submit(task);
                    try {
                        future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void addTaskBlocking(Runnable task, long time, TimeUnit unit) {
                if (acceptTask()) {
                    Future<?> future = executorService.submit(() -> {
                        //System.out.println("thread: " + Thread.currentThread().getName());
                        hold++;
                        //System.out.println("hold: " + hold);
                        if (hold > 1) {
                            System.out.println("wait...");
                            lock.lock();
                            try {
                                condition.await(time, unit);
                            } catch (InterruptedException ignore) {
                            } finally {
                                lock.unlock();
                            }
                        }
                        //System.out.println("consume");
                        task.run();
                    });
                    try {
                        future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public boolean acceptTask() {
                return !executorService.isTerminated() && !executorService.isShutdown();
            }

            @Override
            public void awaitAfterTermination(long time, TimeUnit unit) {
                try {
                    executorService.awaitTermination(time, unit);
                } catch (InterruptedException ignore) {}
            }

            @Override
            public void shutdown() {
                System.out.println("shutdown");
                if (hold > 0) hold = 0;
                lock.lock();
                try {
                    condition.signalAll();
                } catch (Exception ignore) {
                } finally {
                    lock.unlock();
                }
                executorService.shutdown();
            }

            @Override
            public void shutdownNow() {
                System.out.println("shutdownNow");
                if (hold > 0) hold = 0;
                lock.lock();
                try {
                    condition.signalAll();
                } catch (Exception ignore) {
                } finally {
                    lock.unlock();
                }
                executorService.shutdownNow();
            }

            @Override
            public void release() {
                if (hold > 0) hold--;
                System.out.println("release hold: " + hold);
                lock.lock();
                try {
                    condition.signalAll();
                } catch (Exception ignore) {
                } finally {
                    lock.unlock();
                }
            }
        };
    }
}
