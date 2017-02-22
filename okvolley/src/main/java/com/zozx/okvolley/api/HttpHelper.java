package com.zozx.okvolley.api;

import android.text.TextUtils;

import com.zozx.okvolley.HttpMethod;
import com.zozx.okvolley.HttpRequest.Builder;

/**
 * Created by zozx on 2017/2/22.
 * http helper.
 */
@SuppressWarnings("unused")
public class HttpHelper {

    public static final String HEADER_KEY_COOKIE = "Cookie";
    public static final String HEADER_KEY_USER_AGENT = "User-Agent";

    private static String userAgent;
    private static String cookie;
    private static String hostUrl;

    private static int defaultInitialTimeoutMs;
    private static int defaultMaxNumRetries;
    private static float defaultBackoffMultiplier;

    private HttpHelper() {
    }

    public static void setUserAgent(String userAgent) {
        HttpHelper.userAgent = userAgent;
    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static void setCookie(String cookie) {
        HttpHelper.cookie = cookie;
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setDefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        HttpHelper.defaultInitialTimeoutMs = initialTimeoutMs;
        HttpHelper.defaultMaxNumRetries = maxNumRetries;
        HttpHelper.defaultBackoffMultiplier = backoffMultiplier;
    }

    public static int getDefaultInitialTimeoutMs() {
        return defaultInitialTimeoutMs;
    }

    public static int getDefaultMaxNumRetries() {
        return defaultMaxNumRetries;
    }

    public static float getDefaultBackoffMultiplier() {
        return defaultBackoffMultiplier;
    }

    public static void setHostUrl(String hostUrl) {
        HttpHelper.hostUrl = hostUrl;
    }

    public static String getHostUrl() {
        return hostUrl;
    }

    public static String getFullUrl(String partUrl) {
        String hostUrl = getHostUrl();
        if (TextUtils.isEmpty(hostUrl)) {
            throw new IllegalStateException("should call HttpHelper.setHostUrl(String) to set your host url first ");
        }
        return hostUrl + partUrl;
    }

    // —————————————————————————————————————— builders —————————————————————————————————————————————

    public static Builder builderPOST(String partUrl) {
        return builderMethod(partUrl, HttpMethod.POST);
    }

    public static Builder builderGET(String partUrl) {
        return builderMethod(partUrl, HttpMethod.GET);
    }

    public static Builder builderMethod(String partUrl, HttpMethod method) {
        return builderPartUrl(partUrl).setMethod(method);
    }

    public static Builder builderPartUrl(String partUrl) {
        return builder(getFullUrl(partUrl));
    }

    public static Builder builderFullGET(String fullUrl) {
        return builderFullUrlMethod(fullUrl, HttpMethod.GET);
    }

    public static Builder builderFullUrlMethod(String fullUrl, HttpMethod method) {
        return builder(fullUrl).setMethod(method);
    }

    public static Builder builder(String fullUrl) {
        Builder builder = new Builder(fullUrl);
        String cookie = getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            builder.addHeader(HEADER_KEY_COOKIE, cookie);
        }
        String userAgent = getUserAgent();
        if (!TextUtils.isEmpty(userAgent)) {
            builder.addHeader(HEADER_KEY_USER_AGENT, userAgent);
        }
        int timeoutMs = getDefaultInitialTimeoutMs();
        if (timeoutMs > 0) {
            builder.setRetryPolicy(timeoutMs, getDefaultMaxNumRetries(), getDefaultBackoffMultiplier());
        }
        return builder;
    }
}
