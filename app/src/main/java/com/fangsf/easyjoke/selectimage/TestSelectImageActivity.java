package com.fangsf.easyjoke.selectimage;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.framelibrary.base.BaseActivity;
import com.fangsf.easyjoke.R;
import com.fangsf.easyjoke.activity.TestActivity;

import java.util.ArrayList;

public class TestSelectImageActivity extends BaseActivity {

    private ArrayList<String> mImageList;
    private static final int SELECT_IMAGE_RESULT = 0x0011;

    @Override
    protected int bindLayout() {
        return R.layout.activity_test_select_image;
    }

    @Override
    protected void init() {
        mImageList = new ArrayList<>();
    }


    public void onClick(View view) {

//        Intent intent = new Intent(this, SelectImageActivity.class);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MAX_COUNT, 9);
//        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, SelectImageActivity.MODE_MULTI);
//        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, true);
//        intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
//        startActivityForResult(intent, SELECT_IMAGE_RESULT);

        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);


        // 替换上面的写法
//        ImageSelector.create().count(9).multi().origin(mImageList).showCamera(true)
//                .start(this, SELECT_IMAGE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_RESULT && data != null) {
                mImageList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST);
                Toast.makeText(this, "" + mImageList.size(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
