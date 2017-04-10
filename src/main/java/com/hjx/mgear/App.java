package com.hjx.mgear;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hjx.mgear.common.framework.spring.CustomizedPropertyConfigurer;
import com.hjx.mgear.config.Config;

/**
 * 主调度程序
 * 
 * @author Jingxun Huang
 * @since 2016年8月2日
 */
public class App {

    private static final Logger                   LOGGER     = LoggerFactory.getLogger(App.class);

    private static String                         LOCAL_HOST = Config.LOCAL_HOST;

    private static int                            PORT       = Config.PORT;

    private static ClassPathXmlApplicationContext APP_CONTEXT;

    private static ServerSocket                   SOCKET;

    public static void main(String[] args) {

        LOGGER.warn("App start !!!");

        avoidDuplicatedRun();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

                this.setName("Exit Handler");
                appExit();
            }
        });

        init();

        dealArgs(args);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                appExit();
            }
        }
    }

    private static void appExit() {

        LOGGER.warn("App is trying to exit");

        if (APP_CONTEXT != null) {
            LOGGER.warn("Shutting down sprint context...");
            APP_CONTEXT.close();
            LOGGER.warn("Shutting down sprint context succeed");
        }
        LOGGER.warn("Exit OK");
    }

    private static void avoidDuplicatedRun() {

        try {
            SOCKET = new ServerSocket(PORT, 10, InetAddress.getByName(null));
            LOGGER.info(SOCKET.toString());
        } catch (IOException e) {
            LOGGER.error("不能重复启动实例，程序退出!");
            System.exit(-1);
        }
    }

    private static void dealArgs(String[] args) {

        if (args.length > 0) {
            if ("test".equals(args[0])) {
                System.out.println(CustomizedPropertyConfigurer.getContextProperty("datasource.username"));
                System.out.println(LOCAL_HOST);
            } else {
                System.out.println("Usage: java -jar jarfile [args...]");
                System.out.println("Arguments: ");
                System.out.println("\ttest: test config file if override successfully");
            }
            System.exit(0);
        }
    }

    private static void init() {

        APP_CONTEXT = new ClassPathXmlApplicationContext("spring.xml");
    }
}
