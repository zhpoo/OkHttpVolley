package com.zozx.okvolley.demo;

import android.app.Application;

import com.zozx.okvolley.HttpRequest;
import com.zozx.okvolley.api.HttpHelper;
import com.zozx.okvolley.api.OkVolley;

/**
 * Created by zozx on 2017/2/23.
 * application for demo.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHttpClient();
    }

    private void initHttpClient() {
        OkVolley.init(this);
        HttpHelper.setHostUrl(getMyHost());
        HttpHelper.setCookie(getMyCookie());
        HttpHelper.setUserAgent(getMyUserAgent());
        HttpHelper.setDefaultRetryPolicy(2000, 2, 0.5f);
    }

    private String getMyUserAgent() {
        return "your user agent";
    }

    private String getMyHost() {
        return ApiConstant.HOST;
    }

    private String getMyCookie() {
        return "your cookie";
    }
}
