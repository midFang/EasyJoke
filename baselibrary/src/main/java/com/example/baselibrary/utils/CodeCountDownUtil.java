package com.example.baselibrary.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by fangsf on 2018/6/26.
 * Useful:
 */
public class CodeCountDownUtil {

    private CountDownTimer mTimer;

    private Context mContext;

    public CodeCountDownUtil(Context context) {
        mContext = context;
    }

    public void startCountDownTimer(final TextView view, final int textColor, final int finishColor) {
        mTimer = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              //  view.setTextColor(textColor);
                view.setEnabled(false);
                view.setClickable(false);
                view.setText("" + millisUntilFinished / 1000 + "后重新获取");
            }

            @Override
            public void onFinish() {
             //   view.setTextColor(finishColor);
                view.setText("获取验证码");
                view.setEnabled(true);
                view.setClickable(true);
            }
        }.start();

    }

}
