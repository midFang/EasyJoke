package com.example.baselibrary.commonDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by fangsf on 2018/2/24.
 * Useful:
 */

class AlertController {

    private BaseAlertDialog mDialog;
    private Window mWindow;
    private  DialogViewHelper mViewHelper;

    public BaseAlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public AlertController(BaseAlertDialog dialog, Window window) {
        mDialog = dialog;
        mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    // 设置 文本
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    // 设置 点击事件
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }


    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;

        // 点击外部是否能够隐藏
        public boolean mCancelable = true;

        public DialogInterface.OnCancelListener mOnCancelListener;

        public DialogInterface.OnDismissListener mOnDismissListener;

        // 按键的监听
        public DialogInterface.OnKeyListener mOnKeyListener;

        public View mView;

        public int mViewLayoutResId;

        // 存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        // 存放资源的监听
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 动画
        public int mAnimation = 0;

        // 位置
        public int mGravity = Gravity.CENTER;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;

        }


        public void apply(AlertController alert) {


            // 设置参数

            // 设置布局
            DialogViewHelper viewHelper = null;
            if (mView != null) {
                viewHelper = new DialogViewHelper();
            }



            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置setContentView()");
            }

            // ** 给dialog  设置布局
            alert.getDialog().setContentView(viewHelper.getContentView());

            alert.setViewHelper(viewHelper);

            // 设置文本
            int textSize = mTextArray.size();
            for (int i = 0; i < textSize; i++) {
                alert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 设置点击
            int clickSize = mClickArray.size();
            for (int i = 0; i < clickSize; i++) {
                alert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }


            // 配置自定义的效果  全屏, 从底部弹出  默认动画
            Window window = alert.mWindow;

            window.setGravity(mGravity);
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;

            window.setAttributes(params);


        }
    }


}
