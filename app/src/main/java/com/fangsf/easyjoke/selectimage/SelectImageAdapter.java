package com.fangsf.easyjoke.selectimage;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangsf.easyjoke.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangsf on 2018/8/6.
 * Useful:  图像相册选择
 */
public class SelectImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    // 图片信息
    private ArrayList<String> mResultImageList;
    // 最多能选择多少张图片
    private int mImageMaxCount;

    public SelectImageAdapter(int layoutResId, @Nullable List<String> data,
                              ArrayList<String> resultImageList, int imageMaxCount) {
        super(layoutResId, data);
        this.mResultImageList = resultImageList;   // 选中 图片数据的集合
        this.mImageMaxCount = imageMaxCount;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView ivPreView = helper.getView(R.id.iv_preview);
        ImageView ivCheckBox = helper.getView(R.id.iv_check_box);
        ImageView ivTackPhoto = helper.getView(R.id.iv_take_photo);
        TextView tvTackPhoto = helper.getView(R.id.tv_take_photo);

        if (TextUtils.isEmpty(item)) {
            // 为空的时候显示拍照
            ivTackPhoto.setVisibility(View.VISIBLE);
            ivPreView.setVisibility(View.INVISIBLE);
            ivCheckBox.setVisibility(View.INVISIBLE);
            tvTackPhoto.setVisibility(View.VISIBLE);

        } else {
            ivTackPhoto.setVisibility(View.INVISIBLE);
            ivPreView.setVisibility(View.VISIBLE);
            tvTackPhoto.setVisibility(View.INVISIBLE);
            ivCheckBox.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(item).centerCrop().dontAnimate().into(ivPreView);

            if (mResultImageList.contains(item)) {
                // 选中的
                ivCheckBox.setSelected(true);
            } else {
                ivCheckBox.setSelected(false);
            }

            ivPreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 判断是否在集合中, 不在集合中,就设置为选中, 在集合中,则取消选中
                    if (!mResultImageList.contains(item)) {
                        if (mImageMaxCount <= mResultImageList.size()) {
                            Toast.makeText(mContext, "只能选择" + mResultImageList.size() + "张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mResultImageList.add(item);
                    } else {
                        mResultImageList.remove(item);
                    }

                    if (mImageListener != null) {
                        mImageListener.selectedImage();
                    }

                    notifyItemChanged(helper.getLayoutPosition());

                }
            });

        }
    }

    // 选择图片的监听回调
    private SelectImageListener mImageListener;

    public void setOnSelectImageListener(SelectImageListener listener) {
        this.mImageListener = listener;
    }


}
