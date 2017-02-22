package com.zozx.okvolley.json;

import com.zozx.okvolley.HttpRequestListener;

/**
 * Created by zozx on 16/8/31.
 * json http request listener
 */
public interface JsonRequestListener<T> extends HttpRequestListener<JsonResponse<T>> {

    /**
     * parse the response json
     *
     * @param responseJson same as name.
     * @return result
     * @throws Throwable
     */
    T parseResponse(String responseJson) throws Throwable;

    /**
     * this method will be invoked before request send.
     */
    void onRequestStart();
}
