package com.fangsf.easyjoke.hookactivity;

import android.content.Context;

/**
 * Created by fangsf on 2018/8/15.
 * Useful:
 */
public class PluginManager  {

    public static  void install(Context context, String apkPath) {

        // 把apk中的class 加载到 applicationClassLoader中
        FixBugManager fixBugManager = new FixBugManager(context);
        try {
            fixBugManager.fixBug(apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
