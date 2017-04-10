package com.hjx.mgear.source.fcmoto.service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjx.mgear.config.Config;
import com.hjx.mgear.config.Config.NamedUrl;
import com.hjx.mgear.source.fcmoto.entity.FCMProduct;
import com.hjx.mgear.source.fcmoto.entity.FCMSku;
import com.hjx.mgear.source.fcmoto.entity.FCMVariation;
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

    public FCMService() {
        super();
        addCookie("IC_TargetCurrency", "EUR", "www.fc-moto.de");
    }

    public void test() {

        for (NamedUrl namedUrl : Config.FCMOTO_URL_PRODUCTS) {

            for (int page = 1, maxPage = 1; page <= maxPage; page++) {

                String name = namedUrl.getName();
                String url = namedUrl.getUrl(1);

                /* 获取HTML */
                String htmlData = getHttpDataWithRetry(url, 3);
                System.out.println(MessageFormat.format("{0}:\n{1}", name, htmlData));

                /* 解析html */
                Document document = Jsoup.parse(htmlData);
                maxPage = fetchMaxPageSize(document);

                List<FCMProduct> productList = fetchProductBrief(document);
                productList.stream().forEach(p -> fetchProductSKU(p.getPpath(), p));

                // XXX XXXXXXXXXXX
                LOGGER.info("{}:", name);
                LOGGER.info("maxPage:{}", maxPage);
                productList.stream().map(FCMProduct::toString).forEach(s -> LOGGER.info(s));
                break;
            }
            break;
        }
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
                product.setUrl(MessageFormat.format(Config.FCMOTO_URL_PRODUCT_DETAIL, ppath));

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

        /* 获取colorMap */
        String xpath = MessageFormat.format(Config.FCMOTO_XPATH_ATTRIBUTE_ELEMENTS, Config.FCMOTO_ATTRIBUTE_ID_COLOR);
        Elements colorElementList = Xsoup.compile(xpath).evaluate(document).getElements();
        Map<String, String> colorMap = colorElementList.stream().collect(Collectors.toMap(e -> e.attr("predefvalueid"), e -> e.attr("title")));

        /* 获取sizeMap */
        xpath = MessageFormat.format(Config.FCMOTO_XPATH_ATTRIBUTE_ELEMENTS, Config.FCMOTO_ATTRIBUTE_ID_SIZE);
        Elements sizeElementList = Xsoup.compile(xpath).evaluate(document).getElements();
        Map<String, String> sizeMap = sizeElementList.stream().collect(Collectors.toMap(e -> e.attr("predefvalueid"), e -> e.text()));

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

        for (Object obj : subProducts.values()) {
            JSObject jsObj = (JSObject) obj;
            String skuId = (String) jsObj.getMember("ProductIF");
            String skuUrl = (String) jsObj.getMember("SubProductUrl");
            String available = (String) jsObj.getMember("IsAvailable");
            JSObject attributeValues = (JSObject) jsObj.getMember("AttributeValues");
            String colorValueId = (String) attributeValues.getMember(Config.FCMOTO_ATTRIBUTE_ID_COLOR);
            String sizeValueId = (String) attributeValues.getMember(Config.FCMOTO_ATTRIBUTE_ID_SIZE);

            FCMVariation variation = new FCMVariation();
            variation.setColor(colorMap.get(colorValueId));
            variation.setSize(sizeMap.get(sizeValueId));

            FCMSku sku = new FCMSku();
            sku.setSkuId(skuId);
            sku.setUrl(skuUrl);
            sku.setVariation(variation);
            sku.setAvailable(NumberUtils.toInt(available));
            sku.setAttributeValues(attributeValues.toString());
            product.getSkuList().add(sku);
        }

        product.getSkuList().stream().forEach(sku -> fillSkuImage(product, sku));
        product.getSkuList().stream().forEach(sku -> fillSkuQuantity(product, sku));
        return;
    }

    private Set<String> fillSkuImage(FCMProduct product, FCMSku sku) {

        Set<String> imageSet = new LinkedHashSet<>();

        /* 获取JSON */
        String url = MessageFormat.format(Config.FCMOTO_URL_IMAGE, sku.getSkuId());
        String jsonData = getHttpDataWithRetry(url, 3);
        JSONObject jsonObject = JSON.parseObject(jsonData);

        /* 获取webRoot */
        String webRoot = jsonObject.getString("ProductWebPath");
        product.setWebRoot(webRoot);

        /* 获取 mainImage */
        String mainImage = jsonObject.getJSONObject("ProductImages").getString("ImageLarge");
        imageSet.add(mainImage);

        /* 获取 otherImage */
        JSONArray slideShowArray = jsonObject.getJSONArray("ProductSlideShow");
        if (slideShowArray != null) {
            for (int i = 0; i < slideShowArray.size(); i++) {
                JSONObject obj = slideShowArray.getJSONObject(i);
                String image = obj.getJSONObject("Sizes").getString("Large");
                imageSet.add(image);
            }
        }
        sku.getImageList().addAll(imageSet);
        return imageSet;
    }

    private int fillSkuQuantity(FCMProduct product, FCMSku sku) {

        /* 获取JSON */
        String url = MessageFormat.format(Config.FCMOTO_URL_STOCK, product.getWebRoot(), sku.getSkuId(), System.currentTimeMillis());
        String jsonData = getHttpDataWithRetry(url, 3);
        JSONObject jsonObject = JSON.parseObject(jsonData);
        int availableStock = jsonObject.getIntValue("AvailableStock");
        sku.setQuantity(availableStock);
        
        return availableStock;
    }

    private int fetchMaxPageSize(Document document) {

        List<String> pageList = Xsoup.compile(Config.FCMOTO_XPATH_PAGE).evaluate(document).list();
        int pageSize = pageList.stream()
                               .filter(str -> NumberUtils.isDigits(str))
                               .mapToInt(NumberUtils::toInt)
                               .max()
                               .getAsInt();
        return pageSize;
    }
}
