package com.example.framelibrary.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.framelibrary.R;
import com.example.framelibrary.base.BaseActivity;

/**
 * Created by fangsf on 2018/7/16.
 * Useful:
 */
public abstract class BaseSkinActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // 拦截到view 的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {

                if (name.equals("Button")) {
                    Button button = new Button(BaseSkinActivity.this);
                    button.setText("拦截");
                    button.setBackgroundColor(Color.RED);
                    return button;
                }

                return null;
            }
        });
        super.onCreate(savedInstanceState);
    }
}
