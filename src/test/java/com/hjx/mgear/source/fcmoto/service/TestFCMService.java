package com.hjx.mgear.source.fcmoto.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hjx.mgear.platform.fcmoto.service.FCMService;

/**
 * @author Jingxun Huang
 * @since 2017年4月10日
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations={"file:src/main/resources/spring.xml"})
@ContextConfiguration("classpath:spring.xml")
public class TestFCMService {
    private static Logger           LOGGER = LoggerFactory.getLogger(TestFCMService.class);
    @Autowired
    private FCMService              fcmService;

    @Autowired
    @Qualifier("captureImageScheduler")
    private ThreadPoolTaskScheduler scheduler;

    @Test
    public void test() {

        fcmService.execute();

        ScheduledThreadPoolExecutor executor = scheduler.getScheduledThreadPoolExecutor();

        try {

            while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                long completedTaskCount = executor.getCompletedTaskCount();
                long totalTaskCount = executor.getTaskCount();
                LOGGER.info("CaptureImage progress: {}/{}.", completedTaskCount, totalTaskCount);
                if (completedTaskCount == totalTaskCount) {
                    LOGGER.info("All task completed, shutdown executor.");
                    executor.shutdown();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("executor interruppted");
            Thread.currentThread().interrupt();
        }
    }
}
