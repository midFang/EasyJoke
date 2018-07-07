package com.example.baselibrary.commonDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.baselibrary.R;


/**
 * Created by fangsf on 2018/2/24.
 * Useful:
 */

public class BaseAlertDialog extends Dialog {

    private AlertController mAlert;

    public BaseAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        // 存储参数
        mAlert = new AlertController(this, getWindow());
    }

    // 设置 文本
    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }

    // 设置 点击事件
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    /**
     * 构建参数
     */
    public static class Builder {

        private final AlertController.AlertParams P;


        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public BaseAlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final BaseAlertDialog dialog = new BaseAlertDialog(P.mContext, P.mThemeResId);

            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }


        public BaseAlertDialog show() {
            final BaseAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutResId) {
            P.mView = null;
            P.mViewLayoutResId = layoutResId;
            return this;
        }

        //配置一些万能的参数
        // 设置全屏显示
        public Builder setFullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        // 设置 从底部弹出
        public Builder setFormBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimation = R.style.dialog_form_bottom_anim;
            }

            P.mGravity = Gravity.BOTTOM;
            return this;
        }


        // 设置宽高
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        //设置默认缩放动画
        public Builder addDefaulrAnim() {
            P.mAnimation = R.style.dialog_scale_anim;
            return this;
        }

        //设置动画
        public Builder setAnim(int styleAnim) {
            P.mAnimation = styleAnim;
            return this;
        }


        //存放
        public Builder setText(int viewId, CharSequence charSequence) {
            P.mTextArray.put(viewId, charSequence);

            return this;
        }

        //存放
        public Builder setClicklistener(int viewId, View.OnClickListener listener) {

            P.mClickArray.put(viewId, listener);

            return this;
        }

        public Builder setCancelable(boolean isCancel) {
            P.mCancelable = isCancel;
            return this;
        }
    }


}
