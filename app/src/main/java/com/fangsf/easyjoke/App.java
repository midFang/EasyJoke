package com.fangsf.easyjoke;

import android.app.Application;

import com.fangsf.easyjoke.net.HttpUtils;
import com.fangsf.easyjoke.net.OkHttpEngine;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // 初始化网络引擎
        HttpUtils.init(new OkHttpEngine());

    }
}
