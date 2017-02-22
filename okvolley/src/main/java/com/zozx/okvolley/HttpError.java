package com.zozx.okvolley;

/**
 * Created by zozx on 16/9/3.
 * the http error wrapper.
 */
public class HttpError extends Exception {

    public final int statusCode;

    public HttpError(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }
}
