package com.zozx.okvolley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zozx on 16/6/28.
 * a subclass abstraction {@link Request} which wrapped {@link HttpRequest}
 */
public abstract class RequestWrapper<T> extends Request<T> {

    protected final HttpRequest mHttpRequest;
    protected final HttpRequestListener<T> mHttpListener;

    public RequestWrapper(HttpRequest request, HttpRequestListener<T> listener) {
        super(transMethod(request.getMethod()), request.getUrl(), null);
        this.mHttpRequest = request;
        this.mHttpListener = listener;
        setHttpRetryPolicy(request.getRetryPolicy());
    }

    public void setHttpRetryPolicy(HttpRetryPolicy retry) {
        if (retry != null) {
            setRetryPolicy(new DefaultRetryPolicy(
                    retry.getInitialTimeoutMs(),
                    retry.getMaxNumRetries(),
                    retry.getBackoffMultiplier()));
        }
    }

    /**
     * return the url for this request, joint params if method is GET.
     */
    @Override
    public String getUrl() {
        try {
            if (getMethod() == Method.GET && (getParams() != null && getParams().size() > 0)) {
                String params = getEncodedUrlParams();
                String extra = "";
                if (!TextUtils.isEmpty(params)) {
                    if (!mHttpRequest.getUrl().contains("?")) {
                        extra += "?";
                    } else {
                        extra += "&";
                    }
                    extra += params;
                }
                return mHttpRequest.getUrl() + extra;
            }
        } catch (AuthFailureError ignored) {
        }
        return mHttpRequest.getUrl();
    }

    /**
     * joint GET params.
     */
    public String getEncodedUrlParams() throws AuthFailureError {
        StringBuilder result = new StringBuilder();
        String encoding = getParamsEncoding();
        Map<String, String> params = getParams();
        try {
            for (Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (result.length() != 0) {
                    result.append('&');
                }
                result.append(URLEncoder.encode(entry.getKey(), encoding));
                result.append('=');
                result.append(URLEncoder.encode(entry.getValue(), encoding));
            }
            return result.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * return http request headers.
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHttpRequest.getHeaders();
    }

    /**
     * return http request params.
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHttpRequest.getStringParams();
    }

    protected Map<String, Object> getObjectParams() {
        return mHttpRequest.getObjectParams();
    }

    /**
     * return the http request Content-Type.
     */
    @Override
    public String getBodyContentType() {
        return mHttpRequest.getContentType();
    }

    /**
     * delivery the successful response if not canceled.
     *
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(T response) {
        if (mHttpListener != null) {
            mHttpListener.onSuccess(response);
        }
    }

    /**
     * delivery the failed response if not canceled.
     *
     * @param error Error details
     */
    @Override
    public void deliverError(VolleyError error) {
        if (mHttpListener != null) {
            int statusCode = error == null || error.networkResponse == null ? 0 : error.networkResponse.statusCode;
            mHttpListener.onError(new HttpError(statusCode, error));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isCanceled()) {
            sb.append("[CANCELED]");
        }
        sb.append(getMethodName()).append(":");
        sb.append(getUrl());

        if (getMethod() != Method.GET) {
            Map<String, Object> params = getObjectParams();
            if (params != null && params.size() > 0) {
                sb.append("?");
                for (Entry<String, Object> entry : params.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue() instanceof File ? "[FILE:" + entry.getValue() + "]" : entry.getValue());
                    sb.append("&");
                }
                sb.deleteCharAt(sb.length() - 1);       // delete last "&"
            }
        }
        return sb.toString();
    }

    public String getMethodName() {
        switch (getMethod()) {
            case Method.POST:
                return "POST";
            case Method.GET:
                return "GET";
            case Method.DEPRECATED_GET_OR_POST:
                return "DEPRECATED_GET_OR_POST";
            case Method.PUT:
                return "PUT";
            case Method.HEAD:
                return "HEAD";
            case Method.OPTIONS:
                return "OPTIONS";
            case Method.DELETE:
                return "DELETE";
            case Method.PATCH:
                return "PATCH";
            case Method.TRACE:
                return "TRACE";
            default:
                return "UNKNOWN METHOD";
        }
    }

    /**
     * transform {@link HttpMethod} to {@link Method}
     */
    private static int transMethod(HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                return Method.GET;
            case POST:
                return Method.POST;
            case DELETE:
                return Method.DELETE;
            case HEAD:
                return Method.HEAD;
            case OPTIONS:
                return Method.OPTIONS;
            case PATCH:
                return Method.PATCH;
            case PUT:
                return Method.PUT;
            case TRACE:
                return Method.TRACE;
            case DEPRECATED_GET_OR_POST:
                return Method.DEPRECATED_GET_OR_POST;
            default:
                return Method.DEPRECATED_GET_OR_POST;
        }
    }
}
