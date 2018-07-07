package com.example.baselibrary.commonDialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by fangsf on 2018/2/24.
 * Useful:
 */

public class DialogViewHelper {

    private View mContentView;

    // 减少findviewByid 的次数
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context context, int lyoutResId) {

        this();

        mContentView = LayoutInflater.from(context).inflate(lyoutResId, null);

    }

    public DialogViewHelper() {


        mViews = new SparseArray<>();

    }

    // 设置 文本
    public void setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    // 设置 点击事件
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public <T extends View> T getView(int viewId) {

        WeakReference<View> viewWeakReference = mViews.get(viewId);

        // 防止内存泄漏
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);

            // 弱引用将viewId 存储起来
            if (view != null) {
                mViews.put(viewId, new WeakReference<View>(view));
            }
        }

        return (T) view;
    }

    public View getContentView() {
        return this.mContentView;
    }


}
