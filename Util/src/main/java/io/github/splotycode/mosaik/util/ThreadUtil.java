package io.github.splotycode.mosaik.util;

import java.util.concurrent.*;

public final class ThreadUtil {

    public static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runWithTimeout(final Runnable runnable, long timeout) throws Exception {
        runWithTimeout(() -> {
            runnable.run();
            return null;
        }, timeout);
    }

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
            Throwable t = e.getCause();
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                throw new IllegalStateException(t);
            }
        }
    }

}
