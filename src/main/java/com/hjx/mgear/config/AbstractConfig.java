package com.hjx.mgear.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjx.mgear.utils.common.Utils;

/**
 * @author Jingxun Huang
 * @since 2017年4月10日
 */
public abstract class AbstractConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractConfig.class);

    protected static PropertiesConfiguration loadProperties(String configFileName) {

        LOGGER.info("loading properties , fileName = " + configFileName);
        try {
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.setEncoding("UTF-8");

            /* 优先读取父目录的配置文件 */
            String confFilePath = Utils.getAppParentPath() + File.separator + configFileName;
            File confFile = new File(confFilePath);
            if (confFile.exists()) {
                config.load(confFilePath);
            } else {
                config.load(configFileName);
            }
            return config;
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected static void setFileChangedReloadingStrategy(PropertiesConfiguration config) {

        FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
        config.setReloadingStrategy(reloadingStrategy);
    }
}
