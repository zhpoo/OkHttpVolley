package com.zozx.okvolley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zozx on 16/6/28.
 * request for wrapping http request.
 */
public class HttpRequest {

    private Builder mBuilder;

    private HttpRequest(Builder builder) {
        this.mBuilder = builder;
    }

    public boolean isNeedMultipart() {
        return mBuilder.needMultipart;
    }

    public Map<String, String> getHeaders() {
        return mBuilder.headers;
    }

    public HttpMethod getMethod() {
        return mBuilder.method;
    }

    public Map<String, String> getStringParams() {
        return mBuilder.stringParams;
    }

    public Map<String, Object> getObjectParams() {
        return mBuilder.objectParams;
    }

    public String getContentType() {
        return mBuilder.contentType;
    }

    public String getParamsEncoding() {
        return mBuilder.paramsEncoding;
    }

    public HttpRetryPolicy getRetryPolicy() {
        return mBuilder.retryPolicy;
    }

    public String getUrl() {
        return mBuilder.url;
    }

    @SuppressWarnings("unused")
    public static final class Builder {
        private String url;
        private String paramsEncoding = "UTF-8";
        private HttpMethod method = HttpMethod.GET;
        private String contentType = "application/x-www-form-urlencoded; charset=utf-8";
        private HttpRetryPolicy retryPolicy = new HttpRetryPolicy();
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> stringParams = new HashMap<>();
        private Map<String, Object> objectParams = new HashMap<>();
        private boolean needMultipart = false;

        public Builder(String url) {
            this.url = url;
        }

        /**
         * set request's encoding, default is UTF-8
         *
         * @param encoding encoding
         * @return this builder
         */
        public Builder setEncoding(String encoding) {
            this.paramsEncoding = encoding;
            return this;
        }

        /**
         * add a request's header.
         *
         * @param key   header key
         * @param value header value
         * @return this builder
         */
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        /**
         * add request's headers
         *
         * @param headers headers map.
         * @return this builder
         */
        public Builder addHeader(Map<String, String> headers) {
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    this.headers.put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        /**
         * set http method, default {@link HttpMethod#GET}
         *
         * @param method {@link HttpMethod}
         * @return this builder
         */
        public Builder setMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * add a request param.
         *
         * @param key   key
         * @param value value
         */
        public Builder put(String key, Object value) {
            if (value != null) {
                this.objectParams.put(key, value);
                this.stringParams.put(key, String.valueOf(value));
                if (value instanceof File) {
                    needMultipart = true;
                }
            }
            return this;
        }

        /**
         * * add request params.
         *
         * @param params params map.
         * @return this builder
         */
        public Builder put(Map<String, Object> params) {
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        /**
         * set request's Content-Type.
         *
         * @param contentType content type.
         * @return this builder
         */
        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * set request's retry policy
         *
         * @param initialTimeoutMs  initial timeout milliseconds.
         * @param maxNumRetries     count of retry times.
         * @param backoffMultiplier backoff for each retry.
         * @return this builder
         */
        public Builder setRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
            this.retryPolicy = new HttpRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier);
            return this;
        }

        /**
         * create a {@link HttpRequest} instance by this builder.
         *
         * @return a http request created by this builder.
         */
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
