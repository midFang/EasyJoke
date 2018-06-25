package com.example.framelibrary.net;

import android.content.Context;


import com.example.baselibrary.net.HttpUtils;
import com.example.baselibrary.net.IHttpCallBack;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public abstract class JsonCallBack<T> implements IHttpCallBack {

    /**
     * 网络请求前的  操作
     *
     * @param context
     * @param params
     */
    public void onPreExecute(Context context, Map<String, Object> params) {
        // todo , 请求之前 params 每个请求中可以添加固定参数, 例如deviceID等等, 也可以处理token

        onPreExecute();
    }

    public void onPreExecute() {

    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        T t = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this)); // 获取泛型类上面的信息

        onSuccess(t);  // 回调

    }

    @Override
    public void onError(Exception e) {


        //todo 可以 自定义Exception , 处理token 失效

    }

    public abstract void onSuccess(T t);


}
