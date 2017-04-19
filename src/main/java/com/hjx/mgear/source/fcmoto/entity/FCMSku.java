package com.hjx.mgear.source.fcmoto.entity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jingxun Huang
 * @since 2017年4月12日
 */
public class FCMSku implements Comparable<FCMSku> {

    private String              skuId;
    private String              url;
    private String              attributeValues;
    private int                 available;
    private int                 stock;
    private Map<String, String> variation = new HashMap<String, String>();
    private FCMImage            image;

    public FCMSku() {
        super();
    }

    public String getAttributeValues() {

        return attributeValues;
    }

    public int getAvailable() {

        return available;
    }

    public FCMImage getImage() {

        return image;
    }

    public String getSkuId() {

        return skuId;
    }

    public int getStock() {

        return stock;
    }

    public String getUrl() {

        return url;
    }

    public Map<String, String> getVariation() {

        return variation;
    }

    public void setAttributeValues(String attributeValues) {

        this.attributeValues = attributeValues;
    }

    public void setAvailable(int available) {

        this.available = available;
    }

    public void setImage(FCMImage image) {

        this.image = image;
    }

    public void setSkuId(String skuId) {

        this.skuId = skuId;
    }

    public void setStock(int stock) {

        this.stock = stock;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public void setVariation(Map<String, String> variation) {

        this.variation = variation;
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
        builder.append(", stock=");
        builder.append(stock);
        builder.append(", variation=");
        builder.append(variation);
        builder.append(", image=");
        builder.append(image);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int compareTo(FCMSku that) {

        if (null == that || null == that.getVariation() || that.getVariation().size() <= 0) {
            return 1;
        }

        if (null == this.getVariation() || this.getVariation().size() <= 0) {
            return -1;
        }

        int result = 0;
        result = this.getVariation().size() - that.getVariation().size();

        if (0 == result) {
            List<String> thisList = new ArrayList<String>(this.getVariation().values());
            List<String> thatList = new ArrayList<String>(that.getVariation().values());
            Collections.sort(thisList, Collections.reverseOrder());
            Collections.sort(thatList, Collections.reverseOrder());

            for (int i = 0; i < thisList.size(); i++) {
                result = Collator.getInstance(Locale.CHINESE).compare(thisList.get(i), thatList.get(i));
                if (result != 0) {
                    break;
                }
            }
        }

        return result;
    }
}
