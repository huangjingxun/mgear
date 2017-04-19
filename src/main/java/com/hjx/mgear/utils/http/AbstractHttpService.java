package com.hjx.mgear.utils.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjx.mgear.config.Config;

/**
 * @author Jingxun Huang
 * @since 2016年9月5日
 */
public abstract class AbstractHttpService {

    private static Logger         LOGGER                       = LoggerFactory.getLogger(AbstractHttpService.class);

    protected final static int    CONNECT_TIMEOUT_MICROSECONDS = Config.CONNECT_TIMEOUT_SECONDS * 1000;
    protected final static int    SOCKET_TIMEOUT_MICROSECONDS  = Config.SOCKET_TIMEOUT_SECONDS * 1000;

    protected BasicCookieStore    COOKIE_STORE                 = new BasicCookieStore();

    protected CloseableHttpClient HTTP_CLIENT                  = null;

    public AbstractHttpService() {
        super();
        initHttpClient();
    }

    protected static boolean isSuccessWithJsonResponse(String jsonResponse) {

        if (StringUtils.isEmpty(jsonResponse)) {
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        String result = jsonObject.getString("result");
        if ("success".equals(result)) {
            return true;
        }
        return false;
    }

    private void initHttpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                                                   .setConnectionRequestTimeout(CONNECT_TIMEOUT_MICROSECONDS)
                                                   .setConnectTimeout(CONNECT_TIMEOUT_MICROSECONDS)
                                                   .setSocketTimeout(SOCKET_TIMEOUT_MICROSECONDS)
                                                   .build();
        HTTP_CLIENT = HttpClients.custom()
                                 .setDefaultCookieStore(COOKIE_STORE)
                                 .setDefaultRequestConfig(requestConfig)
                                 .build();

    }

    /**
     * @See {@link org.apache.http.impl.client.BasicCookieStore#addCookie(Cookie)}
     * @param name
     * @param value
     */
    protected void addCookie(String name, String value, String domain) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        COOKIE_STORE.addCookie(cookie);
    }

    protected String executePost(String url, String entityContent) {

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(entityContent));
            httpPost.setHeader("Content-Type", "application/json");

            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            if (null == response || HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.warn("POST请求失败,URL:{},response:{}", httpPost.getRequestLine().toString(), null != response ? response.getStatusLine().toString() : null);
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            LOGGER.warn("POST请求异常,URL:{},ex:{}", url, ex.getMessage());
            return null;
        }
    }

    protected String executeDelete(String url, List<NameValuePair> paramList) {

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(paramList)) {
                uriBuilder.addParameters(paramList);
            }
            HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
            httpDelete.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpDelete);
            if (null == response || HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LOGGER.warn("DELETE请求失败,URL:{},response:{}", httpDelete.getRequestLine().toString(), null != response ? response.getStatusLine().toString() : null);
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            LOGGER.warn("DELETE请求异常,URL:{},ex:{}", url, ex.getMessage());
            return null;
        }
    }

    protected String getHttpDataWithRetry(String url, int retry) {

        return getHttpDataWithRetry(url, null, retry);
    }

    protected String getHttpDataWithRetry(String url, List<NameValuePair> paramList, int retry) {

        /* 重试3次 */
        int retryCount = 0;
        String httpBodyString = null;
        do {
            if (retryCount > 0) {
                LOGGER.info("重试: {}", retryCount);
            }
            try {
                httpBodyString = getHttpData(url, paramList);
            } catch (URISyntaxException e) {
                throw new RuntimeException();
            }
            retryCount++;
        } while (null == httpBodyString && retryCount < retry);

        if (retryCount > 1 && httpBodyString != null) {
            LOGGER.info("重试成功.");
        }
        return httpBodyString;
    }

    private String getHttpData(String url, List<NameValuePair> paramList) throws URISyntaxException {

        if (StringUtils.isEmpty(url)) {
            throw new NullPointerException();
        }

        URIBuilder uriBuilder = new URIBuilder(url);

        if (!CollectionUtils.isEmpty(paramList)) {
            uriBuilder.addParameters(paramList);
        }

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = null;
        // LOGGER.info("获取HTTP数据, URL[{}]", url);

        try {
            response = HTTP_CLIENT.execute(httpGet);

            if (null == response || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                if (response != null)
                    LOGGER.warn("网站无法访问, " + response.getStatusLine().toString() + ", " + httpGet.getRequestLine().toString());
                else
                    LOGGER.warn("网站无法访问, " + "response: null, " + httpGet.getRequestLine().toString());
                return null;
            }
            HttpEntity entity = response.getEntity();
            String httpBodyString = EntityUtils.toString(entity);

            return httpBodyString;
        } catch (Exception e) {
            LOGGER.warn("获取Http数据失败：", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }
}
