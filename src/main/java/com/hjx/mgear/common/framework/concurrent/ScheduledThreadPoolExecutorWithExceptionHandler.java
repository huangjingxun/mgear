package com.hjx.mgear.common.framework.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 包装{@link ScheduledThreadPoolExecutor}，打印未捕获异常
 * 
 * @see Throwable#printStackTrace()
 * @author Jingxun Huang
 * @since 2016年8月2日
 */
public class ScheduledThreadPoolExecutorWithExceptionHandler extends ScheduledThreadPoolExecutor {
    private static Logger LOGGER = LoggerFactory.getLogger(ScheduledThreadPoolExecutorWithExceptionHandler.class);

    /**
     * @param corePoolSize
     * @see ScheduledThreadPoolExecutorWithExceptionHandler
     */
    public ScheduledThreadPoolExecutorWithExceptionHandler(int corePoolSize) {
        super(corePoolSize);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {

        return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {

        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }

    private Runnable wrapRunnable(Runnable command) {

        return new LogOnExceptionRunnable(command);
    }

    private class LogOnExceptionRunnable implements Runnable {
        private Runnable theRunnable;

        public LogOnExceptionRunnable(Runnable theRunnable) {
            super();
            this.theRunnable = theRunnable;
        }

        @Override
        public void run() {

            try {
                theRunnable.run();
            } catch (Throwable t) {
                // LOG IT HERE!!!
                LOGGER.error("线程池捕获到异常，不终止执行此线程", t);
                t.printStackTrace();

                /* 不抛出异常，避免线程池终止继续执行此线程 */
                // throw new RuntimeException(t);
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
