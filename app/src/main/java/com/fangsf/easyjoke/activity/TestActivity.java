package com.fangsf.easyjoke.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fangsf.easyjoke.R;

/**
 * Created by fangsf on 2018/8/10.
 * Useful:  测试, 没有在清单文件中注册的活动
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_skin);
    }
}
