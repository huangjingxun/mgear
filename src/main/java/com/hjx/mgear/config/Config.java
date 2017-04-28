package com.hjx.mgear.config;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Jingxun Huang
 * @since 2017年4月10日
 */
@Repository
public class Config extends AbstractConfig {

    public static class ProductUrl {
        private String brand;
        private String category;
        private String url;

        public ProductUrl(String brand, String category, String url) {
            super();
            this.brand = brand;
            this.category = category;
            this.url = url;
        }

        public String getBrand() {

            return brand;
        }

        public String getCategory() {

            return category;
        }

        public String getUrl() {

            return url;
        }

        public String getUrl(int page) {

            return MessageFormat.format(url, page);
        }

        public void setBrand(String brand) {

            this.brand = brand;
        }

        public void setCategory(String category) {

            this.category = category;
        }

        public void setUrl(String url) {

            this.url = url;
        }

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();
            builder.append("ProductUrl [brand=");
            builder.append(brand);
            builder.append(", category=");
            builder.append(category);
            builder.append(", url=");
            builder.append(url);
            builder.append("]");
            return builder.toString();
        }
    }

    @SuppressWarnings("unused")
    private final static Logger                  LOGGER                          = LoggerFactory.getLogger(Config.class);

    private static final String                  CONFIG_FILE_NAME                = "config.properties";
    private static final PropertiesConfiguration CONF                            = loadProperties(CONFIG_FILE_NAME);

    public static final Integer                  CONNECT_TIMEOUT_SECONDS         = CONF.getInteger("CONNECT_TIMEOUT_SECONDS", 30);
    public static final Integer                  SOCKET_TIMEOUT_SECONDS          = CONF.getInteger("SOCKET_TIMEOUT_SECONDS", 180);
    public static final String                   LOCAL_HOST                      = CONF.getString("LOCAL_HOST", "localhost");
    public static final int                      PORT                            = CONF.getInt("PORT");

    public static final BigDecimal               EUR_TO_CNY                      = CONF.getBigDecimal("EUR_TO_CNY");

    public static final String                   OUTPUT_DIR                      = CONF.getString("OUTPUT_DIR");

    public static final String                   IMAGE_CAPTURE_DIR               = CONF.getString("IMAGE_CAPTURE_DIR");
    public static final String                   IMAGE_WATERMARK_DIR             = CONF.getString("IMAGE_WATERMARK_DIR");
    public static final String                   IMAGE_WATER_MARK_TEXT           = CONF.getString("IMAGE_WATER_MARK_TEXT");
    public static final String                   IMAGE_FCMOTO_DIR                = CONF.getString("IMAGE_FCMOTO_DIR");

    public static final String                   FCMOTO_URL                      = CONF.getString("FCMOTO_URL");
    public static final List<ProductUrl>         FCMOTO_URL_PRODUCTS             = Arrays.stream(CONF.getStringArray("FCMOTO_URL_PRODUCTS"))
                                                                                         .map(str -> StringUtils.split(str, '|'))
                                                                                         .map(strArray -> new ProductUrl(strArray[0], strArray[1], strArray[2]))
                                                                                         .collect(Collectors.toList());
    public static final String                   FCMOTO_XPATH_PAGE               = CONF.getString("FCMOTO_XPATH_PAGE");
    public static final String                   FCMOTO_XPATH_PRODUCTS           = CONF.getString("FCMOTO_XPATH_PRODUCTS");

    public static final String                   FCMOTO_URL_PRODUCT_DETAIL_EN    = CONF.getString("FCMOTO_URL_PRODUCT_DETAIL_EN");
    public static final String                   FCMOTO_URL_PRODUCT_DETAIL_CN    = CONF.getString("FCMOTO_URL_PRODUCT_DETAIL_CN");
    public static final String                   FCMOTO_XPATH_PRODUCT_DETAIL     = CONF.getString("FCMOTO_XPATH_PRODUCT_DETAIL");

    public static final String                   FCMOTO_URL_IMAGE                = CONF.getString("FCMOTO_URL_IMAGE");
    public static final String                   FCMOTO_URL_STOCK                = CONF.getString("FCMOTO_URL_STOCK");
    public static final String                   FCMOTO_URL_PRODUCT_PRICE        = CONF.getString("FCMOTO_URL_PRODUCT_PRICE");

    public static final String                   FCMOTO_URL_SUBPRODUCT           = CONF.getString("FCMOTO_URL_SUBPRODUCT");
    public static final String                   FCMOTO_XPATH_PID                = CONF.getString("FCMOTO_XPATH_PID");
    public static final String                   FCMOTO_XPATH_SUBPRODUCT         = CONF.getString("FCMOTO_XPATH_SUBPRODUCT");
    public static final String                   FCMOTO_XPATH_ATTRIBUTE_ELEMENTS = CONF.getString("FCMOTO_XPATH_ATTRIBUTE_ELEMENTS");

    public Config() {
        super();
        setFileChangedReloadingStrategy(CONF);
    }

}
