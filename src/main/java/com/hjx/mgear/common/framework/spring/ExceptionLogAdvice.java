package com.hjx.mgear.common.framework.spring;

import org.aspectj.lang.JoinPoint;
import org.slf4j.LoggerFactory;


public class ExceptionLogAdvice {


    public void afterThrowing(JoinPoint jp, Throwable ex) {

        LoggerFactory.getLogger(jp.getTarget().getClass()).error("spring 异常：", ex);
    }
}
