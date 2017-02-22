package com.zozx.okvolley.json;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.zozx.okvolley.HttpRequest;
import com.zozx.okvolley.RequestWrapper;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by zozx on 16/6/28.
 * mask a request with json result, support file upload.
 */
public class JsonRequest<MODEL> extends RequestWrapper<JsonResponse<MODEL>> {

    private final boolean isNeedMultipart;
    private MultipartEntity multipartEntity;

    public JsonRequest(HttpRequest httpRequest, JsonRequestListener<MODEL> listener) {
        super(httpRequest, listener);
        isNeedMultipart = httpRequest.isNeedMultipart();
        if (isNeedMultipart) {
            buildMultipartEntity();
        }
    }

    private void buildMultipartEntity() {
        multipartEntity = new MultipartEntity();
        Map<String, Object> params = mHttpRequest.getObjectParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                multipartEntity.addPart(key, new FileBody((File) value));
            } else {
                try {
                    multipartEntity.addPart(key, new StringBody(String.valueOf(value), Charset.forName(mHttpRequest.getParamsEncoding())));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBodyContentType() {
        if (isNeedMultipart && multipartEntity != null) {
            return multipartEntity.getContentType().getValue();
        }
        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (isNeedMultipart && multipartEntity != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                multipartEntity.writeTo(bos);
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.getBody();
    }

    @Override
    protected Response<JsonResponse<MODEL>> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseJson = new String(response.data, HttpHeaderParser.parseCharset(response.headers, getParamsEncoding()));
            MODEL model = ((JsonRequestListener<MODEL>) mHttpListener).parseResponse(responseJson);
            JsonResponse<MODEL> result = new JsonResponse<>(model, response.headers, response.statusCode);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(response));
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.error(new ParseError(response));
        }
    }
}
