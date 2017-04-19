package com.hjx.mgear.common.listener;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.hjx.mgear.common.event.CaptureImageEvent;
import com.hjx.mgear.common.service.CaptureImageService;
import com.hjx.mgear.common.service.ImageWaterMarkService;
import com.hjx.mgear.config.Config;

/**
 * @author Jingxun Huang
 * @since 2017年4月18日
 */
@Component
public class CaptureImageListener implements ApplicationListener<CaptureImageEvent> {
    private final static Logger   LOGGER = LoggerFactory.getLogger(CaptureImageListener.class);

    @Autowired
    private CaptureImageService   captureImageService;

    @Autowired
    private ImageWaterMarkService imageWaterMarkService;

    @Async("captureImageScheduler")
    @Override
    public void onApplicationEvent(CaptureImageEvent event) {

        try {
            File imageFile = captureImageService.saveImageFile(event.getImageUrl(), event.getSubDir());

            imageWaterMarkService.createWaterMark(imageFile, event.getImageUrl(), event.getSubDir(), Config.IMAGE_WATER_MARK_TEXT);
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
    }

}
