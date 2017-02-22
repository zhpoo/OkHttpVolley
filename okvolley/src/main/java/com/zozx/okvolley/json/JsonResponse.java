package com.zozx.okvolley.json;

import java.util.Map;

/**
 * Created by zozx on 16/6/28.
 * the response of {@link JsonRequest}
 */
public class JsonResponse<MODEL> {

    /**
     * the response headers of the json request
     */
    public final Map<String, String> headers;
    /**
     * the model object of the json request
     */
    public final MODEL model;

    /**
     * the status code of the json request
     */
    public final int statusCode;

    public JsonResponse(MODEL model, Map<String, String> headers, int statusCode) {
        this.headers = headers;
        this.model = model;
        this.statusCode = statusCode;
    }
}
