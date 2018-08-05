package com.fangsf.easyjoke.activity;

import android.content.Intent;
import android.view.View;

import com.example.framelibrary.base.BaseActivity;
import com.fangsf.easyjoke.R;

import java.util.ArrayList;

public class TestSelectImageActivity extends BaseActivity {

    private ArrayList<String> mImageList;

    @Override
    protected int bindLayout() {
        return R.layout.activity_test_select_image;
    }

    @Override
    protected void init() {
        mImageList = new ArrayList<>();
    }


    public void onClick(View view) {
        Intent intent = new Intent(this, SelectImageActivity.class);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MAX_COUNT, 9);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, SelectImageActivity.MODE_MULTI);
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA,  true);
        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
        startActivity(intent);
    }
}
