package com.fangsf.easyjoke;

import android.app.Application;

import com.example.baselibrary.exception.ExceptionCrashHandler;
import com.example.baselibrary.net.HttpUtils;
import com.example.baselibrary.net.OkHttpEngine;
import com.google.gson.Gson;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // 初始化网络引擎
        HttpUtils.init(new OkHttpEngine());

        // 全局异常的捕捉类
        ExceptionCrashHandler.getInstance().init(this);
    }
}
