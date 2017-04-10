package com.hjx.mgear.common.framework.concurrent;

import java.util.concurrent.ThreadFactory;

import org.springframework.util.StringUtils;

/**
 * @author Jingxun Huang
 * @since 2017年1月19日
 */
public class ThreadFactoryWithName implements ThreadFactory {

    private int    counter = 0;
    private String threadName;

    public ThreadFactoryWithName(String threadName) {

        if (StringUtils.isEmpty(threadName)) {
            this.threadName = this.getClass().getSimpleName();
        } else {
            this.threadName = threadName;
        }
    }

    @Override
    public Thread newThread(Runnable r) {

        return new Thread(r, threadName + "-Thread-" + counter++);
    }
}
