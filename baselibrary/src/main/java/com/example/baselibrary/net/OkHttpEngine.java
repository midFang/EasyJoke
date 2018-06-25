package com.example.baselibrary.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fangsfmac on 2018/6/15.
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, final IHttpCallBack httpCallBack) {

        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);

        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onError(e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();
                // 当然有的时候还需要不同的些许处理
                HttpUtils.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallBack.onSuccess(resultJson);
                    }
                });
            }
        });

    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final IHttpCallBack httpCallBack) {

        RequestBody requestBody = appendBody(params);

        Request mPostRequest = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(mPostRequest).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        executeError(httpCallBack, e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resultJson = response.body().string();

                        executeSuccessMethod(httpCallBack, resultJson);
                    }
                }
        );
    }

    @Override
    public void addHeaders(String key, Object value) {
        // todo 添加头部
    }


    /**
     * 组装post 请求参数
     *
     * @param params
     * @return
     */
    private RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        addParams(builder, params);

        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);

                // 判断是否是file 类型
                if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(
                            MediaType.parse(guessMimeType(file.getAbsolutePath())), file
                    ));
                } else if (value instanceof List) { // 是列表(文件)的参数
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName()
                                    , RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + ""); //是普通的参数
                }
            }
        }

    }

    /**
     * 执行成功的方法
     **/
    private void executeSuccessMethod(final IHttpCallBack httpCallBack, final String resultJson) {
        try {
            HttpUtils.handler.post(new Runnable() {
                @Override
                public void run() {
                    httpCallBack.onSuccess(resultJson);
                }
            });
        } catch (Exception e) {
            executeError(httpCallBack, e);
            e.printStackTrace();
        }
    }

    /**
     * 执行失败的方法
     */
    private void executeError(final IHttpCallBack httpCallBack, final Exception e) {

        HttpUtils.handler.post(new Runnable() {
            @Override
            public void run() {
                httpCallBack.onError(e);
            }
        });
    }

    /**
     * 猜测文件的类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
