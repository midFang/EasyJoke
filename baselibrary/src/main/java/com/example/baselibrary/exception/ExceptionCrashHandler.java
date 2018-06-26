package com.example.baselibrary.exception;

import android.content.Context;
import android.util.Log;

/**
 * Created by fangsf on 2018/6/26.
 * Useful: 捕捉全局异常类
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "ExceptionCrashHandler";

    private static ExceptionCrashHandler mInstance;
    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private ExceptionCrashHandler() {
    }

    public static ExceptionCrashHandler getInstance() {

        if (mInstance == null) {
            // 单例模式, 解决多线程的问题, 多线程的情况下,可能会创建多个
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context;

        // 1. 获取系统默认的异常处理类, 需要先获取
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        //  2. 使用当前的类为异常处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 捕捉全局的异常

        Log.i(TAG, "uncaughtException: 有bug");
        // 让系统默认方式 做处理
        mDefaultExceptionHandler.uncaughtException(t, e);
    }


}
