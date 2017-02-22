package com.zozx.okvolley;

/**
 * Created by zozx on 16/6/28.
 * the listener for http request.
 */
public interface HttpRequestListener<T> {

    /**
     * invoked when request delivery success.
     *
     * @param response response data.
     */
    void onSuccess(T response);

    /**
     * invoked when request delivery error.
     *
     * @param error {@link HttpError}
     */
    void onError(HttpError error);
}
