package com.hjx.mgear.source.fcmoto.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jingxun Huang
 * @since 2017年4月11日
 */
public class FCMProduct {
    private String       pid;
    private String       productName;
    private BigDecimal   price;
    private String       priceStr;
    private String       url;
    private String       ppath;
    private String       webRoot;
    private List<String> imageList = new ArrayList<>();

    private List<FCMSku> skuList   = new ArrayList<>();

    public FCMProduct() {
        super();
    }

    public FCMProduct(String pid, String productName, BigDecimal price, String priceStr, String url) {
        super();
        this.pid = pid;
        this.productName = productName;
        this.price = price;
        this.priceStr = priceStr;
        this.url = url;
    }

    public FCMProduct(String pid, String productName, BigDecimal price, String priceStr, String url, String ppath) {
        super();
        this.pid = pid;
        this.productName = productName;
        this.price = price;
        this.priceStr = priceStr;
        this.url = url;
        this.ppath = ppath;
    }

    public List<String> getImageList() {

        return imageList;
    }

    public String getPid() {

        return pid;
    }

    public String getPpath() {

        return ppath;
    }

    public BigDecimal getPrice() {

        return price;
    }

    public String getPriceStr() {

        return priceStr;
    }

    public String getProductName() {

        return productName;
    }

    public List<FCMSku> getSkuList() {

        return skuList;
    }

    public String getUrl() {

        return url;
    }

    public String getWebRoot() {

        return webRoot;
    }

    public void setImageList(List<String> imageList) {

        this.imageList = imageList;
    }

    public void setPid(String pid) {

        this.pid = pid;
    }

    public void setPpath(String ppath) {

        this.ppath = ppath;
    }

    public void setPrice(BigDecimal price) {

        this.price = price;
    }

    public void setPriceStr(String priceStr) {

        this.priceStr = priceStr;
    }

    public void setProductName(String productName) {

        this.productName = productName;
    }

    public void setSkuList(List<FCMSku> skuList) {

        this.skuList = skuList;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public void setWebRoot(String webRoot) {

        this.webRoot = webRoot;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("FCMProduct [pid=");
        builder.append(pid);
        builder.append(", productName=");
        builder.append(productName);
        builder.append(", price=");
        builder.append(price);
        builder.append(", priceStr=");
        builder.append(priceStr);
        builder.append(", url=");
        builder.append(url);
        builder.append(", ppath=");
        builder.append(ppath);
        builder.append(", webRoot=");
        builder.append(webRoot);
        builder.append(", imageList=");
        builder.append(imageList);
        builder.append(", skuList=");
        builder.append(skuList);
        builder.append("]");
        return builder.toString();
    }
}
