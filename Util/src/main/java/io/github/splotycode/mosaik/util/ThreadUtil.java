package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;

/**
 * General Utils for threads
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadUtil {

    /**
     * Let the thread sleeps
     * @param delay time to sleep in milliseconds
     */
    public static void sleep(long delay) {
        sleep(delay, false);
    }

    /**
     * Let the thread sleeps
     * @param ignore should we ignore InterruptedException's that happen while sleeping
     * @param delay time to sleep in milliseconds
     */
    public static void sleep(long delay, boolean ignore) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            if (!ignore) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    /**
     * Runs a Task with a specific timeout
     * @param runnable the runnable to run
     * @param timeout the timeout in milliseconds
     * @throws Exception if the timeout is reached or a exception happens in the runnable
     */
    public static void runWithTimeout(final Runnable runnable, long timeout) throws Exception {
        runWithTimeout(() -> {
            runnable.run();
            return null;
        }, timeout);
    }

    /**
     * Runs a Task with a specific timeout
     * @param callable the runnable to run
     * @param timeout the timeout in milliseconds
     * @throws Exception if the timeout is reached or a exception happens in the runnable
     */
    public static <T> T runWithTimeout(Callable<T> callable, long timeout) throws Exception {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<T> future = executor.submit(callable);
        executor.shutdown();
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        } catch (ExecutionException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

}
