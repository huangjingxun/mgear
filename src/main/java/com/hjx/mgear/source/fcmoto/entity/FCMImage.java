package com.hjx.mgear.source.fcmoto.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingxun Huang
 * @since 2017年4月17日
 */
public class FCMImage {

    private String       webRoot;
    private List<String> imageList = new ArrayList<>();

    public List<String> getImageList() {

        return imageList;
    }

    public String getWebRoot() {

        return webRoot;
    }

    public void setImageList(List<String> imageList) {

        this.imageList = imageList;
    }

    public void setWebRoot(String webRoot) {

        this.webRoot = webRoot;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("FCMImage [webRoot=");
        builder.append(webRoot);
        builder.append(", imageList=");
        builder.append(imageList);
        builder.append("]");
        return builder.toString();
    }
}
