package com.example.framelibrary.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by fangsfmac on 2018/6/16.
 */

public abstract class DialogCallBack<T> extends JsonCallBack<T> {

    private Context mContext;
    private ProgressDialog dialog;

    protected DialogCallBack(Context context) {
        mContext = context;

        initDialog(context);
    }

    private void initDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);

        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onError(Exception e) {
        super.onError(e);

        //网络请求错误关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
}
