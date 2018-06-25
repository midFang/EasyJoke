package com.example.baselibrary.net;

import android.content.Context;

import java.util.Map;

/**
 * Created by fangsfmac on 2018/6/15.
 * 网络请求的回调
 */

public interface IHttpCallBack {

    void onPreExecute(Context context, Map<String, Object> params);

    void onSuccess(String result);

    void onError(Exception e);

    public IHttpCallBack DEFAULT_CALLBACK = new IHttpCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onError(Exception e) {

        }
    };

}
