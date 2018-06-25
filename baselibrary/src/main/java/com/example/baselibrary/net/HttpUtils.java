package com.example.baselibrary.net;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public class HttpUtils {

    public static Handler handler;

    private Context mContext;

    private Map<String, Object> mParams;

    private String mUrl;

    // 网络引擎
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    // get请求标识
    private final int GET_REQUEST = 0x0011;
    // post请求标识
    private final int POST_REQUEST = 0x0022;
    // 请求的方式
    private int mRequestMethod = GET_REQUEST;

    // 是否开启缓存
    private boolean isCache = false;

    /**
     * 初始化网络引擎
     *
     * @param okHttpEngine
     */
    public static void init(OkHttpEngine okHttpEngine) {
        mHttpEngine = okHttpEngine;
    }

    /**
     * 切换网络引擎框架
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        this.mHttpEngine = httpEngine;
        return this;
    }

    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new HashMap<>();
        handler = new Handler();
    }

    /**
     * 添加头部
     */
    public HttpUtils addHeaders(String key, Object value) {
        mHttpEngine.addHeaders(key, value);

        return this;
    }

    /**
     * 链式调用开始的位置
     *
     * @param context
     * @return
     */
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }


    public HttpUtils get() {
        mRequestMethod = GET_REQUEST;
        return this;
    }

    public HttpUtils post() {
        mRequestMethod = POST_REQUEST;
        return this;
    }

    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }


    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public void execute() {
        execute(null);
    }

    public void execute(IHttpCallBack callBack) {
        if (TextUtils.isEmpty(mUrl)) {
            throw new NullPointerException("网络访问路径不能为空");
        }

        if (callBack == null) {
            callBack = IHttpCallBack.DEFAULT_CALLBACK;  // 实现了一个 默认的callback
        }

        // 实现一个 onPreExecute(), 可以实现, 添加一些 固定的参数类型
        callBack.onPreExecute(mContext, mParams);


        if (mRequestMethod == GET_REQUEST) {
            mHttpEngine.get(mContext, mUrl, mParams, callBack);
        }

        if (mRequestMethod == POST_REQUEST) {
            mHttpEngine.post(mContext, mUrl, mParams, callBack);
        }
    }

    /**
     * 解析一个类上面的 class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
