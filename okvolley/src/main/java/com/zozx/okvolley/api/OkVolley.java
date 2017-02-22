package com.zozx.okvolley.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zozx.okvolley.HttpMethod;
import com.zozx.okvolley.HttpRequest;
import com.zozx.okvolley.OkHttp3Stack;
import com.zozx.okvolley.json.JsonRequest;
import com.zozx.okvolley.json.JsonRequestListener;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by zozx on 16/6/27.
 * volley + okHttp for lazy people.
 */
@SuppressWarnings("unused")
public class OkVolley {

    private static OkVolley instance;
    private final RequestQueue mRequestQueue;

    private OkVolley(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext(),
                new OkHttp3Stack(new OkHttpClient()));
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (OkVolley.class) {
                if (instance == null) {
                    instance = new OkVolley(context.getApplicationContext());
                }
            }
        }
    }

    public static OkVolley getInstance() {
        if (instance == null) {
            throw new IllegalStateException("should call OkVolley.init(Context) first. " +
                    "may be in your application's onCreate()");
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * add a request,tag is null
     */
    public void addRequest(Request request) {
        addRequest(request, null);
    }

    /**
     * add a request
     */
    public void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    /**
     * cancel request with tag which was set before.
     */
    public void cancelRequest(Object tag) {
        if (tag != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * send a jsonRequest with {@link HttpRequest} and {@link JsonRequestListener},
     * a {@code null} tag will be set.
     *
     * @param httpRequest the request want to send.
     * @param listener    callback.
     * @param <T>         response json model type.
     * @return the real volley request.
     */
    public <T> Request jsonRequest(HttpRequest httpRequest, JsonRequestListener<T> listener) {
        return jsonRequest(httpRequest, listener, null);
    }

    /**
     * send a jsonRequest with {@link HttpRequest} and {@link JsonRequestListener}
     *
     * @param httpRequest the request want to send.
     * @param listener    callback.
     * @param <T>         response json model type.
     * @param requestTag  the request tag.
     * @return the real volley request.
     */
    public <T> Request jsonRequest(HttpRequest httpRequest, JsonRequestListener<T> listener, Object requestTag) {
        JsonRequest<T> request = new JsonRequest<>(httpRequest, listener);
        listener.onRequestStart();
        addRequest(request, requestTag);
        return request;
    }

    // ——————————————————————————————————————— request api —————————————————————————————————————————

    /**
     * send a post request.
     *
     * @param partUrl  the part url of you server api.
     * @param params   request params.
     * @param listener callback.
     */
    public static void post(String partUrl, Map<String, Object> params, JsonRequestListener listener) {
        OkVolley.getInstance().jsonRequest(HttpHelper.builderPOST(partUrl).put(params).build(), listener);
    }

    /**
     * send a post request.
     *
     * @param partUrl  the part url of you server api.
     * @param params   request params.
     * @param listener callback.
     * @param tag      the request tag.
     */
    public static void post(String partUrl, Map<String, Object> params, JsonRequestListener listener, Object tag) {
        OkVolley.getInstance().jsonRequest(HttpHelper.builderPOST(partUrl).put(params).build(), listener, tag);
    }

    /**
     * send a post request with full server api url.
     *
     * @param fullUrl  full server api url.
     * @param params   request params.
     * @param listener callback.
     */
    public static void postDirect(String fullUrl, Map<String, Object> params, JsonRequestListener listener) {
        OkVolley.getInstance().jsonRequest(HttpHelper.builderFullUrlMethod(fullUrl, HttpMethod.POST).put(params).build(), listener);
    }

    /**
     * send a get request with full server api url.
     *
     * @param partUrl  the part url of you server api.
     * @param params   request params.
     * @param listener callback.
     */
    public static void get(String partUrl, Map<String, Object> params, JsonRequestListener listener) {
        OkVolley.getInstance().jsonRequest(HttpHelper.builderGET(partUrl).put(params).build(), listener);
    }

    /**
     * send a get request with full server api url.
     *
     * @param fullUrl  full server api url.
     * @param listener callback.
     */
    public static void getDirect(String fullUrl, JsonRequestListener listener) {
        OkVolley.getInstance().jsonRequest(HttpHelper.builderFullGET(fullUrl).build(), listener);
    }
}
