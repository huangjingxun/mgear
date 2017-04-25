package com.hjx.mgear.common.service;

import java.awt.Color;
import java.io.File;

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
public class ImageWaterMarkService {

    private static Logger LOGGER = LoggerFactory.getLogger(ImageWaterMarkService.class);

    public ImageWaterMarkService() {

    }

    public File createWaterMark(File srcImgFile, String imageUrl, String subDir, String logoText) {

        if (StringUtils.isEmpty(logoText)) {
            return null;
        }

        File outputImageFile = ImageUtils.createOutputFile(imageUrl, Config.IMAGE_WATERMARK_DIR, subDir);
        if (!outputImageFile.exists()) {
            createWaterMarkByText(srcImgFile, outputImageFile, logoText);
            LOGGER.info("Create Image Water Mark: {}", outputImageFile);
        }
        return outputImageFile;
    }

    private void createWaterMarkByText(File srcImgFile,
                                       File outputImageFile,
                                       String text) {

        ImageUtils.createWaterMarkByText(srcImgFile, outputImageFile, text, "宋体", Color.WHITE, Color.YELLOW, 40, "JPG", 0, 0.3f);
    }
}
