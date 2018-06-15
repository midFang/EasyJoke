package com.fangsf.easyjoke.net;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public abstract class HttpCallBack<T> implements IHttpCallBack {

    /**
     * 处理清楚之前的操作
     *
     * @param context
     * @param params
     */
    public void onPreExecute(Context context, Map<String, Object> params) {


        onPreExecute(); // todo
    }

    private void onPreExecute() {

    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        T t = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this)); // 获取泛型类上面的信息

        onSuccess(t);  // 回调

    }

    @Override
    public void onError(Exception e) {

    }

    public abstract void onSuccess(T t);


}
