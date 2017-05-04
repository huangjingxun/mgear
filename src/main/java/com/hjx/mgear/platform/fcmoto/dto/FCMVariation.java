package com.hjx.mgear.platform.fcmoto.dto;

/**
 * @author Jingxun Huang
 * @since 2017年4月14日
 */
public class FCMVariation {
    private String color;
    private String size;

    public String getColor() {

        return color;
    }

    public String getSize() {

        return size;
    }

    public void setColor(String color) {

        this.color = color;
    }

    public void setSize(String size) {

        this.size = size;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("FCMVariation [color=");
        builder.append(color);
        builder.append(", size=");
        builder.append(size);
        builder.append("]");
        return builder.toString();
    }
}
