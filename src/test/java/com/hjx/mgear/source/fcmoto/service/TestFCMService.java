package com.hjx.mgear.source.fcmoto.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jingxun Huang
 * @since 2017年4月10日
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations={"file:src/main/resources/spring.xml"})
@ContextConfiguration("classpath:spring.xml")
public class TestFCMService {
    @SuppressWarnings("unused")
    private static Logger LOGGER = LoggerFactory.getLogger(TestFCMService.class);
    @Autowired
    private FCMService    fcmService;

    @Test
    public void test() {

        fcmService.test();
    }
}
