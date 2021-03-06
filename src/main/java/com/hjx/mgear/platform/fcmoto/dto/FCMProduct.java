package com.hjx.mgear.platform.fcmoto.dto;

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
    private String       detailCN;
    private String       detailEN;
    private String       brand;
    private String       category;
    private FCMImage     image;
    private List<FCMSku> skuList = new ArrayList<>();

    public FCMProduct() {
        super();
    }

    public String getBrand() {

        return brand;
    }

    public String getCategory() {

        return category;
    }

    public String getDetailCN() {

        return detailCN;
    }

    public String getDetailEN() {

        return detailEN;
    }

    public FCMImage getImage() {

        return image;
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

    public void setBrand(String brand) {

        this.brand = brand;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    public void setDetailCN(String detailCN) {

        this.detailCN = detailCN;
    }

    public void setDetailEN(String detailEN) {

        this.detailEN = detailEN;
    }

    public void setImage(FCMImage image) {

        this.image = image;
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
        builder.append(", detailCN=");
        builder.append(detailCN);
        builder.append(", detailEN=");
        builder.append(detailEN);
        builder.append(", brand=");
        builder.append(brand);
        builder.append(", category=");
        builder.append(category);
        builder.append(", image=");
        builder.append(image);
        builder.append(", skuList=");
        builder.append(skuList);
        builder.append("]");
        return builder.toString();
    }
}
