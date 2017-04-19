package com.hjx.mgear.common.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hjx.mgear.config.Config;
import com.hjx.mgear.utils.common.ImageUtils;

/**
 * @author Jingxun Huang
 * @since 2017年4月18日
 */
@Service
public class CaptureImageService {

    private static Logger    LOGGER              = LoggerFactory.getLogger(CaptureImageService.class);

    private static final int CONNECTION_TIMEOUT  = 10 * 1000;

    private static final int SOCKET_READ_TIMEOUT = 30 * 1000;

    public CaptureImageService() {

    }

    // public boolean isImageExist(String imageUrl, String subDir) {
    //
    // if (StringUtils.isEmpty(imageUrl)) {
    // return false;
    // }
    //
    // File outputFile = ImageUtils.createOutputFile(imageUrl, Config.IMAGE_ROOT_DIR, subDir);
    // return outputFile.exists();
    // }

    public File saveImageFile(String imageUrl, String subDir) {

        if (StringUtils.isEmpty(imageUrl)) {
            throw new NullPointerException("imageUrl can not be null");
        }

        File outputFile = ImageUtils.createOutputFile(imageUrl, Config.IMAGE_CAPTURE_DIR, subDir);

        try {
            URL url = new URL(imageUrl);
            /* 文件不存在就下载 */
            if (!outputFile.exists()) {
                FileUtils.copyURLToFile(url, outputFile, CONNECTION_TIMEOUT, SOCKET_READ_TIMEOUT);
                LOGGER.info("Save Image: {}", outputFile);
            } else {
                LOGGER.info("Exist Image: {}", outputFile);
            }
        } catch (SocketTimeoutException e) {
            LOGGER.warn("抓取图片超时: {}", imageUrl);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOGGER.warn("抓取图片失败: {}", imageUrl, e);
            throw new RuntimeException(e);
        }

        return outputFile;
    }

}
