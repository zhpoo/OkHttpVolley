package com.zozx.okvolley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zozx on 16/6/27.
 * An {@link HttpStack} based on {@link OkHttpClient}.
 */
@SuppressWarnings("deprecation")
public class OkHttp3Stack implements HttpStack {

    private final OkHttpClient okHttpClient;

    public OkHttp3Stack(OkHttpClient client) {
        this.okHttpClient = client;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        int timeoutMs = request.getTimeoutMs();
        OkHttpClient client = okHttpClient.newBuilder()
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build();

        okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();
        Map<String, String> requestHeaders = request.getHeaders();
        for (final String name : requestHeaders.keySet()) {
            okHttpRequestBuilder.addHeader(name, requestHeaders.get(name));
        }

        for (final String name : additionalHeaders.keySet()) {
            okHttpRequestBuilder.addHeader(name, additionalHeaders.get(name));
        }

        setConnectionParametersForRequest(okHttpRequestBuilder, request);

        okhttp3.Request okHttp3Request = okHttpRequestBuilder.url(request.getUrl()).build();

        Response okHttpResponse = client.newCall(okHttp3Request).execute();
        StatusLine responseStatus = new BasicStatusLine(
                parseProtocol(okHttpResponse.protocol()),
                okHttpResponse.code(),
                okHttpResponse.message());

        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromOkHttpResponse(okHttpResponse));

        Headers responseHeaders = okHttpResponse.headers();
        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            final String name = responseHeaders.name(i), value = responseHeaders.value(i);
            if (name != null) {
                response.addHeader(new BasicHeader(name, value));
            }
        }

        return response;
    }

    private static HttpEntity entityFromOkHttpResponse(Response response) throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody body = response.body();

        entity.setContent(body.byteStream());
        entity.setContentLength(body.contentLength());
        entity.setContentEncoding(response.header("Content-Encoding"));

        if (body.contentType() != null) {
            entity.setContentType(body.contentType().type());
        }
        return entity;
    }

    private static void setConnectionParametersForRequest(okhttp3.Request.Builder builder, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    builder.post(RequestBody.create(MediaType.parse(request.getPostBodyContentType()), postBody));
                }
                break;

            case Method.GET:
                builder.get();
                break;

            case Method.DELETE:
                builder.delete();
                break;

            case Method.POST:
                builder.post(createRequestBody(request));
                break;

            case Method.PUT:
                builder.put(createRequestBody(request));
                break;

            case Method.HEAD:
                builder.head();
                break;

            case Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;

            case Method.TRACE:
                builder.method("TRACE", null);
                break;

            case Method.PATCH:
                builder.patch(createRequestBody(request));
                break;

            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static RequestBody createRequestBody(Request request) throws AuthFailureError {
        final byte[] body = request.getBody();
        if (body == null) return null;

        return RequestBody.create(MediaType.parse(request.getBodyContentType()), body);
    }

    private static ProtocolVersion parseProtocol(final Protocol protocol) {
        switch (protocol) {
            case HTTP_1_0:
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1:
                return new ProtocolVersion("HTTP", 1, 1);
            case SPDY_3:
                return new ProtocolVersion("SPDY", 3, 1);
            case HTTP_2:
                return new ProtocolVersion("HTTP", 2, 0);
        }

        throw new IllegalAccessError("Unknown protocol");
    }
}
