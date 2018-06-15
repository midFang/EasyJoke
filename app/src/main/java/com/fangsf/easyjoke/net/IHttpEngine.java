package com.fangsf.easyjoke.net;

import android.content.Context;

import java.util.Map;

/**
 * Created by fangsfmac on 2018/6/15.
 * http 网络引擎的规范
 */

public interface IHttpEngine {


    // get
    void get(Context context, String url, Map<String, Object> params, IHttpCallBack callBack);

    // post
    void post(Context context, String url, Map<String, Object> params, IHttpCallBack callBack);

    // 添加头部证书
    void addHeaders(String key, Object value);

    // https 证书校验

    // 上传

    // 下载


}
