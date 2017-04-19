package com.hjx.mgear.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Jingxun Huang
 * @since 2017年4月18日
 */
public class CaptureImageEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String            imageUrl;
    private String            subDir;

    public CaptureImageEvent(Object source) {
        super(source);
    }

    public CaptureImageEvent(Object source, String imageUrl, String subDir) {
        super(source);
        this.imageUrl = imageUrl;
        this.subDir = subDir;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public String getSubDir() {

        return subDir;
    }

    public void setSubDir(String subDir) {

        this.subDir = subDir;
    }

}
