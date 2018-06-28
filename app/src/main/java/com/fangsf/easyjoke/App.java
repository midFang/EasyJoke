package com.fangsf.easyjoke;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import com.alipay.euler.andfix.AndFixManager;
import com.alipay.euler.andfix.patch.PatchManager;
import com.example.baselibrary.exception.CrashAppHandler;
import com.example.baselibrary.exception.CrashAppLog;
import com.example.baselibrary.net.HttpUtils;
import com.example.baselibrary.net.OkHttpEngine;
import com.example.baselibrary.utils.utilCode.ApkUtil;

import java.io.File;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public class App extends Application {

    public static PatchManager mPatchManager;

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
                // 可以上传删除错误信息
            }
        });

        // 阿里 热修复的使用
        mPatchManager = new PatchManager(this);
        mPatchManager.init(ApkUtil.getAppVersionName(this));

        // 加载之前的apatch 差分包, 有可能不只有一个差分包, 所以加载之前的差分包
        mPatchManager.loadPatch();

    }
}
