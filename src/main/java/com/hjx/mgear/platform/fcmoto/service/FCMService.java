package com.hjx.mgear.platform.fcmoto.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjx.mgear.common.event.CaptureImageEvent;
import com.hjx.mgear.common.service.CaptureImageService;
import com.hjx.mgear.common.service.ImageWaterMarkService;
import com.hjx.mgear.config.Config;
import com.hjx.mgear.config.Config.ProductUrl;
import com.hjx.mgear.platform.fcmoto.dto.FCMImage;
import com.hjx.mgear.platform.fcmoto.dto.FCMProduct;
import com.hjx.mgear.platform.fcmoto.dto.FCMSku;
import com.hjx.mgear.utils.common.ImageUtils;
import com.hjx.mgear.utils.http.AbstractHttpService;

import jdk.nashorn.api.scripting.JSObject;
import us.codecraft.xsoup.Xsoup;

/**
 * @author Jingxun Huang
 * @since 2017年4月10日
 */
@Service
public class FCMService extends AbstractHttpService {
    private final static Logger              LOGGER                = LoggerFactory.getLogger(FCMService.class);
    private final static ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private final static ScriptEngine        SCRIPT_ENGINE         = SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");

    @Autowired
    private CaptureImageService              captureImageService;
    @Autowired
    private ImageWaterMarkService            imageWaterMarkService;
    @Autowired
    private ApplicationContext               applicationContext;

    public FCMService() {
        super();
        // addCookie("IC_TargetCurrency", "EUR", "www.fc-moto.de");
    }

    public void execute() {

        /* 防止重复的product */
        LinkedHashMap<String, FCMProduct> productMap = new LinkedHashMap<>();

        for (ProductUrl productUrl : Config.FCMOTO_URL_PRODUCTS) {

            /* 分页读取productList */
            for (int page = 1, maxPage = 1; page <= maxPage; page++) {

                String name = productUrl.getCategory();
                String url = productUrl.getUrl(page);

                /* 获取HTML */
                String htmlData = getHttpDataWithRetry(url, 3);
                Document document = Jsoup.parse(htmlData);

                maxPage = fetchMaxPageSize(document);

                List<FCMProduct> productList = fetchProductBrief(document);
                for (FCMProduct product : productList) {
                    product.setBrand(productUrl.getBrand());
                    product.setCategory(productUrl.getCategory());
                    productMap.put(product.getPpath(), product);
                }
                LOGGER.info("Got page: [{}] {}/{}", productUrl.getCategory(), page, maxPage);
                break;
            }

            break;
        }

        /* 填充product */
        for (FCMProduct product : productMap.values()) {
            fetchProductDetail(product);
            fetchProductSKU(product.getPpath(), product);
            FCMImage image = fetchProductImage(product.getPid());
            product.setImage(image);

            /* 抓取图片文件 */
            buildImageUrl(image).stream().forEach(this::fetchImageFile);

            /* 输出数据 */
            output(product);
        }
    }

    private void fetchProductDetail(FCMProduct product) {

        /* 获取HTML */
        String url = MessageFormat.format(Config.FCMOTO_URL_PRODUCT_DETAIL_CN, product.getPpath());
        String htmlData = getHttpDataWithRetry(url, 3);
        Document document = Jsoup.parse(htmlData);
        String detailCN = Xsoup.compile(Config.FCMOTO_XPATH_PRODUCT_DETAIL).evaluate(document).get();
        product.setDetailCN(detailCN);

        url = MessageFormat.format(Config.FCMOTO_URL_PRODUCT_DETAIL_EN, product.getPpath());
        htmlData = getHttpDataWithRetry(url, 3);
        document = Jsoup.parse(htmlData);
        String detailEN = Xsoup.compile(Config.FCMOTO_XPATH_PRODUCT_DETAIL).evaluate(document).get();
        product.setDetailEN(detailEN);
    }

    private void output(FCMProduct product) {

        LOGGER.info("Output start: ");
        List<String> dataList = new ArrayList<>();
        dataList.add("productName: " + product.getProductName());
        dataList.add("ppath: " + product.getPpath());
        dataList.add("url: " + product.getUrl());
        dataList.add("pid: " + product.getPid());
        dataList.add("priceStr: " + product.getPriceStr());
        dataList.add("priceRMB: " + Config.EUR_TO_CNY.multiply(product.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        String productPath = Config.OUTPUT_DIR + "/" + Config.IMAGE_FCMOTO_DIR + "/" + product.getBrand() + "/" + product.getCategory() + "/" + product.getProductName();
        File outputFile = new File(productPath + "/" + "listing.txt");
        File productImageDir = new File(productPath + "/Image");
        copyImageToDir(product.getImage(), productImageDir, false);

        /* SKU */
        dataList.add("");
        dataList.add("SKU: ");
        dataList.add("pid\t\tstock\tskuName");

        List<FCMSku> skuList = product.getSkuList();
        Collections.sort(skuList);
        for (FCMSku sku : skuList) {
            String skuName = sku.getVariation().values().stream().map(s -> s.replaceAll("[\\\\\\/:*?\\\"<>|]+", "-")).sorted(Comparator.reverseOrder()).collect(Collectors.joining("_"));
            if (StringUtils.isEmpty(skuName)) {
                skuName = sku.getSkuId();
            }
            File skuImageDir = new File( productPath + "/" + skuName.split("_")[0]);
            copyImageToDir(sku.getImage(), skuImageDir, false);
            dataList.add(MessageFormat.format("{0}\t{1}\t{2}", sku.getSkuId(), sku.getStock(), skuName));
        }
        dataList.add("");
        dataList.add("DetailEN: ");
        dataList.add(product.getDetailEN());
        dataList.add("DetailCN: ");
        dataList.add(product.getDetailCN());

        try {
            FileUtils.touch(outputFile);
            FileUtils.writeLines(outputFile, dataList);
        } catch (IOException e) {
            LOGGER.warn("Output file failed: {}", outputFile, e);
        }
    }

    private void copyImageToDir(FCMImage image, File dir, boolean overwrite) {

        for (int i = 0; i < image.getImageList().size(); i++) {
            String imageName = image.getImageList().get(i);
            File srcFile = getImageFile(image.getWebRoot(), imageName);
            ImageUtils.copyFileToDirectory(srcFile, dir, String.valueOf(i), overwrite);
        }
    }

    private File getImageFile(String dir, String fileName) {

        if (null == dir) {
            throw new NullPointerException("dir can not be null");
        } else if (null == fileName) {
            throw new NullPointerException("fileName can not be null");
        }
        String filePath = Config.IMAGE_WATERMARK_DIR + "/" + Config.IMAGE_FCMOTO_DIR + "/" + dir + "/" + fileName;
        return new File(filePath);
    }

    private List<FCMProduct> fetchProductBrief(Document document) {

        List<FCMProduct> productBriefList = new ArrayList<>();
        Elements elements = Xsoup.compile(Config.FCMOTO_XPATH_PRODUCTS).evaluate(document).getElements();
        for (Element element : elements) {
            try {
                FCMProduct product = new FCMProduct();

                /* Title */
                Element titleElement = element.select("> div > div.ListItemProductInfoContainer > div.ListItemProductTopFloatArea > h3 > a").first();
                product.setProductName(titleElement.attr("title"));
                /* Price */
                Element priceElement = element.select("> div > div.ListItemProductInfoContainer > div:nth-child(4) > span").first();
                Element priceSpan = priceElement.select("> span").first();
                product.setPrice(new BigDecimal(priceSpan.attr("priceunformed")));
                product.setPriceStr(priceSpan.text());
                /* Parent product URL */
                String ppath = StringUtils.substringBefore(element.attr("ppath"), "/SubProducts/");
                product.setPpath(ppath);
                product.setUrl(MessageFormat.format(Config.FCMOTO_URL_PRODUCT_DETAIL_CN, ppath));

                /* Add */
                productBriefList.add(product);
            } catch (Exception e) {
                LOGGER.warn("", e);
            }
        }

        return productBriefList;
    }

    private void fetchProductSKU(String ppath, FCMProduct product) {

        String url = MessageFormat.format(Config.FCMOTO_URL_SUBPRODUCT, ppath);
        String htmlData = getHttpDataWithRetry(url, 3);

        Document document = Jsoup.parse(htmlData);
        /* 获取parent product PID */
        String pid = Xsoup.compile(Config.FCMOTO_XPATH_PID).evaluate(document).get();
        product.setPid(pid);

        /* 获取scrpit */
        String script = Xsoup.compile(Config.FCMOTO_XPATH_SUBPRODUCT).evaluate(document).getElements().first().html();
        script = StringUtils.substringBefore(script, "ICPAttrSuperArray");

        try {
            SCRIPT_ENGINE.eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        JSObject localProduct = (JSObject) SCRIPT_ENGINE.get("LocalProduct");
        JSObject subProducts = (JSObject) localProduct.getMember("SubProducts");
        JSObject attributeIDs = (JSObject) localProduct.getMember("AttributeIDs");

        Map<String, Map<String, String>> attrMap = new HashMap<>();

        for (Object obj : attributeIDs.values()) {
            String attributeId = (String) obj;
            String xpath = MessageFormat.format(Config.FCMOTO_XPATH_ATTRIBUTE_ELEMENTS, attributeId);
            Elements elementList = Xsoup.compile(xpath).evaluate(document).getElements();
            Map<String, String> attrValueNameMap = new HashMap<>();
            attrMap.put(attributeId, attrValueNameMap);
            for (Element element : elementList) {
                String predefvalueid = element.attr("predefvalueid");
                String value = element.text();
                /* 优先取text，取不到再取title */
                if (StringUtils.isEmpty(value)) {
                    value = element.attr("title");
                }
                if (StringUtils.isEmpty(value)) {
                    LOGGER.error("取不到variation value, element:\n [{}]", element);
                    throw new RuntimeException("取不到variation value");
                }
                attrValueNameMap.put(predefvalueid, value);
            }
        }

        if (CollectionUtils.isEmpty(subProducts.values())) {
            /* 没有subProduct，product作为sku */
            FCMSku sku = new FCMSku();
            sku.setSkuId(product.getPid());
            sku.setUrl(product.getUrl());
            sku.getVariation().clear();
            sku.setAvailable(1);
            sku.setAttributeValues(StringUtils.EMPTY);
            product.getSkuList().add(sku);
        } else {
            for (Object obj : subProducts.values()) {
                JSObject jsObj = (JSObject) obj;
                String skuId = (String) jsObj.getMember("ProductIF");
                String skuUrl = (String) jsObj.getMember("SubProductUrl");
                Integer available = (Integer) jsObj.getMember("IsAvailable");
                JSObject attributeValues = (JSObject) jsObj.getMember("AttributeValues");

                FCMSku sku = new FCMSku();
                sku.setSkuId(skuId);
                sku.setUrl(skuUrl);
                sku.setAvailable(available);
                sku.setAttributeValues(attributeValues.toString());
                for (String keyId : attributeValues.keySet()) {
                    String valId = (String) attributeValues.getMember(keyId);
                    sku.getVariation().put(valId, attrMap.get(keyId).get(valId));
                }
                product.getSkuList().add(sku);
            }
        }

        for (int i = 0; i < product.getSkuList().size(); i++) {
            FCMSku sku = product.getSkuList().get(i);
            LOGGER.info("Get SKU quantity&image,Product[{}],SKU[{}],progress[{}/{}]", product.getPid(), sku.getSkuId(), i + 1, product.getSkuList().size());

            FCMImage image = fetchProductImage(sku.getSkuId());
            int stock = fetchSkuStock(image.getWebRoot(), sku.getSkuId());

            sku.setImage(image);
            sku.setStock(stock);
        }
        return;
    }

    private FCMImage fetchProductImage(String pid) {

        FCMImage fcmImage = new FCMImage();

        /* 获取JSON */
        String url = MessageFormat.format(Config.FCMOTO_URL_IMAGE, pid);
        String jsonData = getHttpDataWithRetry(url, 3);
        JSONObject jsonObject = JSON.parseObject(jsonData);

        /* 获取webRoot */
        String webRoot = jsonObject.getString("ProductWebPath");
        fcmImage.setWebRoot(webRoot);

        /* 获取 mainImage */
        String mainImage = jsonObject.getJSONObject("ProductImages").getString("ImageLarge");
        fcmImage.getImageList().add(mainImage);

        /* 获取 otherImage */
        JSONArray slideShowArray = jsonObject.getJSONArray("ProductSlideShow");
        if (slideShowArray != null) {
            for (int i = 0; i < slideShowArray.size(); i++) {
                JSONObject obj = slideShowArray.getJSONObject(i);
                String image = obj.getJSONObject("Sizes").getString("Large");
                fcmImage.getImageList().add(image);
            }
        }

        return fcmImage;
    }

    private int fetchSkuStock(String webRoot, String skuId) {

        /* 获取JSON */
        String url = MessageFormat.format(Config.FCMOTO_URL_STOCK, webRoot, skuId, String.valueOf(System.currentTimeMillis()));
        String jsonData = getHttpDataWithRetry(url, 3);
        JSONObject jsonObject = JSON.parseObject(jsonData);
        int availableStock = jsonObject.getIntValue("AvailableStock");
        return availableStock;
    }

    private int fetchMaxPageSize(Document document) {

        List<String> pageList = Xsoup.compile(Config.FCMOTO_XPATH_PAGE).evaluate(document).list();
        int pageSize = pageList.stream()
                               .filter(str -> NumberUtils.isDigits(str))
                               .mapToInt(NumberUtils::toInt)
                               .max()
                               .orElse(1);
        return pageSize;
    }

    private List<String> buildImageUrl(FCMImage image) {

        return image.getImageList().stream().map(s -> Config.FCMOTO_URL + image.getWebRoot() + '/' + s).collect(Collectors.toList());
    }

    private void fetchImageFileAsync(String imageUrl) {

        applicationContext.publishEvent(new CaptureImageEvent(this, imageUrl, Config.IMAGE_FCMOTO_DIR));
    }

    private void fetchImageFile(String imageUrl) {

        try {
            File imageFile = captureImageService.saveImageFile(imageUrl, Config.IMAGE_FCMOTO_DIR);
            imageWaterMarkService.createWaterMark(imageFile, imageUrl, Config.IMAGE_FCMOTO_DIR, Config.IMAGE_WATER_MARK_TEXT);
        } catch (Exception e) {
            LOGGER.warn("fetchImage 失败", e);
        }
    }
}
