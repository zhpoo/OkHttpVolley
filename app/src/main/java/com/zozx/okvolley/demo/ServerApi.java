package com.zozx.okvolley.demo;

import com.zozx.okvolley.api.OkVolley;
import com.zozx.okvolley.json.JsonRequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zozx on 2017/2/23.
 * server http api.
 */
public class ServerApi {

    private ServerApi() {
    }

    private static Map<String, Object> createParams() {
        return new HashMap<>();
    }

    public static void login(String username, String password, JsonRequestListener listener) {
        Map<String, Object> params = createParams();
        params.put("username", username);
        params.put("password", password);
        OkVolley.post(ApiConstant.login, params, listener);
    }
}
