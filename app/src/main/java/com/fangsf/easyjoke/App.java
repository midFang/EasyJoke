package com.fangsf.easyjoke;

import android.app.Application;
import android.os.Environment;

import com.alipay.euler.andfix.patch.PatchManager;
import com.blankj.utilcode.util.AppUtils;
import com.example.baselibrary.exception.CrashAppHandler;
import com.example.baselibrary.fixbug.FixBugManager;
import com.example.baselibrary.net.HttpUtils;
import com.example.baselibrary.net.OkHttpEngine;
import com.example.framelibrary.skin.SkinManager;

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

        SkinManager.getInstance().init(this);

        // 全局异常的捕捉类
        CrashAppHandler.getInstance().init(this);
        CrashAppHandler.getInstance().setCrashLogListener(new CrashAppHandler.OnCrashLogListener() {
            @Override
            public void logInfo(final File folder, File file) {
                // 可以上传删除错误信息
            }
        });

//        // 阿里 热修复的使用
//        mPatchManager = new PatchManager(this);
//        mPatchManager.init(AppUtils.getAppVersionName("com.fangsf.easyjoke"));
//
//        // 加载之前的apatch 差分包, 有可能不只有一个差分包, 所以加载之前的差分包
//        mPatchManager.loadPatch();

//        FixBugManager fixBugManager = new FixBugManager(this);
//        try {
//            fixBugManager.loadFixDex();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
