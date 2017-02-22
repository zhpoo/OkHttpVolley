package com.zozx.okvolley.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zozx.okvolley.HttpError;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login();
    }

    private void login() {
        ServerApi.login("zozx", "666666", new BaseHttpListener<LoginResponseModel>() {
            @Override
            public void onRequestStart() {
                // do anything you want.
            }

            @Override
            protected void onHandleModel(LoginResponseModel model) {
                // do anything you want.
            }

            @Override
            public void onError(HttpError error) {
                super.onError(error);
                // do anything you want.
            }
        });
    }
}
