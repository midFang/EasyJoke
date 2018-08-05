package com.fangsf.easyjoke.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangsf.easyjoke.R;

import java.util.List;

/**
 * Created by fangsf on 2018/8/6.
 * Useful:  图像相册选择
 */
public class SelectImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public SelectImageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivPreView = helper.getView(R.id.iv_preview);
        ImageView ivCheckBox = helper.getView(R.id.iv_check_box);
        ImageView ivTackPhoto = helper.getView(R.id.iv_take_photo);

        if (TextUtils.isEmpty(item)) {
            // 为空的时候显示拍照
            ivTackPhoto.setVisibility(View.VISIBLE);
            ivPreView.setVisibility(View.INVISIBLE);
            ivCheckBox.setVisibility(View.INVISIBLE);
        } else {
            ivTackPhoto.setVisibility(View.INVISIBLE);
            ivPreView.setVisibility(View.VISIBLE);
            ivCheckBox.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext).load(item).centerCrop().into(ivPreView);

    }
}
