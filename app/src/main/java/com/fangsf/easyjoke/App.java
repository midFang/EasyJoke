package com.fangsf.easyjoke;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import com.example.baselibrary.exception.CrashAppHandler;
import com.example.baselibrary.exception.CrashAppLog;
import com.example.baselibrary.net.HttpUtils;
import com.example.baselibrary.net.OkHttpEngine;

import java.io.File;

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
        CrashAppHandler.getInstance().init(this);
        CrashAppHandler.getInstance().setCrashLogListener(new CrashAppHandler.OnCrashLogListener() {
            @Override
            public void logInfo(final File folder, File file) {
                // 可以尝试删除错误信息
            }
        });
    }
}
