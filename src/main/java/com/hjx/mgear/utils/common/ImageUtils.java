package com.hjx.mgear.utils.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author Jingxun Huang
 * @since 2017年4月19日
 */
public class ImageUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    public static void createWaterMarkByText(File srcImgFile,
                                             File outputImageFile,
                                             String text,
                                             String fontName,
                                             Color colorFrom,
                                             Color colorTo,
                                             int fontSize,
                                             String outputFormatName,
                                             double degree,
                                             float alpha) {

        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(srcImgFile);

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                                                      srcImg.getHeight(null),
                                                      BufferedImage.TYPE_INT_RGB);

            Graphics2D g = buffImg.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null),
                                                 srcImg.getHeight(null),
                                                 Image.SCALE_SMOOTH),
                        0,
                        0,
                        null);

            if (degree > 0) {
                g.rotate(Math.toRadians(degree),
                         (double) buffImg.getWidth() / 2,
                         (double) buffImg.getHeight() / 2);
            }

            // g.setColor(color);
            g.setFont(new Font(fontName, Font.BOLD, fontSize));
            Rectangle2D stringBounds = g.getFont().getStringBounds(text, g.getFontRenderContext());
            int x = (int) (buffImg.getWidth() - stringBounds.getWidth()) / 2;
            int y = (int) (buffImg.getHeight() - stringBounds.getHeight()) / 2;
            g.setPaint(new GradientPaint(x,
                                         y,
                                         colorFrom,
                                         x + (int) stringBounds.getWidth(),
                                         y + (int) stringBounds.getHeight(),
                                         colorTo));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(text, x, y);
            g.dispose();
            if (!outputImageFile.exists()) {
                FileUtils.touch(outputImageFile);
            }
            os = new FileOutputStream(outputImageFile);

            // 生成图片
            ImageIO.write(buffImg, outputFormatName, os);

        } catch (Exception e) {
            LOGGER.warn("添加水印失败:{}", outputImageFile);
            throw new RuntimeException("添加水印失败", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
            }
        }
    }

    public static File createOutputFile(String imageUrl, String rootDir, String subDir) {

        String urlTail = getUrlTail(imageUrl);
        if (StringUtils.isEmpty(urlTail)) {
            return null;
        }
        return new File(StringUtils.join(new String[] { rootDir, subDir }), urlTail);
    }

    public static String getUrlTail(String imageUrl) {

        /* 去除https://xxxxx/ */
        Pattern pattern = Pattern.compile("^(https?://[^/]*)(.*)$");
        Matcher matcher = pattern.matcher(imageUrl);
        try {
            if (matcher.find()) {
                String urlTail = matcher.group(2);
                return urlTail;
            }
        } catch (Exception e) {
        }
        String errorMsg = MessageFormatter.format("获取图片Tail失败:[{}]", imageUrl).getMessage();
        LOGGER.warn(errorMsg);
        throw new RuntimeException(errorMsg);
    }

    public static boolean copyFileToDirectory(File srcFile, File dir, String newName, boolean overwrite) {

        try {
            String ext = FilenameUtils.getExtension(srcFile.getName());
            File newFile = FileUtils.getFile(dir, newName + "." + ext);
            if (!newFile.exists() || overwrite) {
                FileUtils.touch(newFile);
                FileUtils.copyFile(srcFile, newFile);
            }
            return true;
        } catch (IOException e) {
            LOGGER.warn("Copy image failed: {}", srcFile, e);
            return false;
        }
    }
}
