package com.hjx.mgear.common.framework.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jingxun Huang
 * @since 2017年1月19日
 */
public class FixedThreadPoolExecutor extends ThreadPoolExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(FixedThreadPoolExecutor.class);

    public FixedThreadPoolExecutor(int corePoolSize, String threadName) {
        super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryWithName(threadName));
    }

    public void shutdownAndAwaitTermination() {

        this.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait for existing tasks to terminate
            while (!this.awaitTermination(5, TimeUnit.SECONDS))
                ;
        } catch (InterruptedException ie) {
            try {
                if (!this.awaitTermination(60, TimeUnit.SECONDS)) {
                    this.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!this.awaitTermination(60, TimeUnit.SECONDS)) {
                        LOGGER.warn("Pool did not terminate");
                    }
                }
            } catch (InterruptedException e) {
            } finally {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {

        super.afterExecute(r, t);
        if (t == null && r instanceof Future) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }
        if (t != null) {
            /* 线程池抛出异常的时候 记录一下 */
            LOGGER.error("线程池捕获到异常", t);
        }
    }
}
