package com.example.framelibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getSimpleName();

    protected View mContextView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContextView = LayoutInflater.from(this).inflate(bindLayout(), null, false);
        setContentView(mContextView);
        ButterKnife.bind(this);

        init();
    }
    protected abstract int bindLayout();

    protected abstract void init();

    protected void startActivity(Class clz) {
        startActivity(new Intent(this, clz));
    }
}
