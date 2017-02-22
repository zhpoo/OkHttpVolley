package com.zozx.okvolley.demo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.$Gson$Types;
import com.zozx.okvolley.HttpError;
import com.zozx.okvolley.json.JsonRequestListener;
import com.zozx.okvolley.json.JsonResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zozx on 2017/2/23.
 * base http request callback.
 */
public abstract class BaseHttpListener<MODEL> implements JsonRequestListener<MODEL> {

    protected final static Gson GSON = new Gson();

    @Override
    public MODEL parseResponse(String responseJson) throws Throwable {
        try {
            return GSON.fromJson(responseJson, getResponseType());
        } catch (JsonSyntaxException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestStart() {
    }

    @Override
    public final void onSuccess(JsonResponse<MODEL> response) {
        if (response == null) {
            onResponseDataError();
            return;
        }
        onHandleHeaders(response.headers);
        onHandleModel(response.model);
    }

    protected void onHandleHeaders(Map<String, String> headers) {
    }

    protected void onResponseDataError() {
    }

    protected abstract void onHandleModel(MODEL model);

    @Override
    public void onError(HttpError error) {
    }

    protected Type getResponseType() {
        Type argument = getParameterArgument();
        return $Gson$Types.canonicalize(argument);
    }

    private Type getParameterArgument() {
        Type superclass = getClass().getGenericSuperclass();
        Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
        return arguments[0];
    }
}
