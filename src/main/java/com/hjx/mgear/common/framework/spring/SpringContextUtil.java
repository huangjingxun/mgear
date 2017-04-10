package com.hjx.mgear.common.framework.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
 * 
 * @author Jingxun Huang
 * @since 2016年8月2日
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringContextUtil() {

    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {

        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... args) {

        return applicationContext.getBean(clazz, args);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {

        return applicationContext.getBean(beanName, clazz);
    }

    public static <T> Object getBean(String beanName, Object... args) {

        return applicationContext.getBean(beanName, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        SpringContextUtil.applicationContext = applicationContext;
    }
}
