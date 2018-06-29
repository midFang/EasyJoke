package com.example.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by fangsf on 2018/6/29.
 * Useful:  findViewById()  的辅助类
 */
public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int resId) {
        return mActivity != null ? mActivity.findViewById(resId) : mView.findViewById(resId);
    }
}
