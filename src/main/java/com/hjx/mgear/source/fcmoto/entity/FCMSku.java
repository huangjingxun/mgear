package com.hjx.mgear.source.fcmoto.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingxun Huang
 * @since 2017年4月12日
 */
public class FCMSku {

    private String       skuId;
    private String       url;
    private String       attributeValues;
    private int          available;
    private int          quantity;
    private FCMVariation variation = new FCMVariation();
    private List<String> imageList = new ArrayList<>();

    public List<String> getImageList() {

        return imageList;
    }

    public void setImageList(List<String> imageList) {

        this.imageList = imageList;
    }

    public FCMSku() {
        super();
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("FCMSku [skuId=");
        builder.append(skuId);
        builder.append(", url=");
        builder.append(url);
        builder.append(", attributeValues=");
        builder.append(attributeValues);
        builder.append(", available=");
        builder.append(available);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", imageList=");
        builder.append(imageList);
        builder.append(", variation=");
        builder.append(variation);
        builder.append("]");
        return builder.toString();
    }

    public String getAttributeValues() {

        return attributeValues;
    }

    public int getAvailable() {

        return available;
    }

    public int getQuantity() {

        return quantity;
    }

    public String getSkuId() {

        return skuId;
    }

    public String getUrl() {

        return url;
    }

    public FCMVariation getVariation() {

        return variation;
    }

    public void setAttributeValues(String attributeValues) {

        this.attributeValues = attributeValues;
    }

    public void setAvailable(int available) {

        this.available = available;
    }

    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    public void setSkuId(String skuId) {

        this.skuId = skuId;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public void setVariation(FCMVariation variation) {

        this.variation = variation;
    }
}
