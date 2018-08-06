package com.fangsf.easyjoke.selectimage;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangsf on 2018/8/6.
 * Useful:  图片选择器, 传递参数的 封装类
 */
public class ImageSelector {

    private static ImageSelector mImageSelector;

    // 照片多选的模式
    public static final int MODE_MULTI = 0x0011;
    // 照片的多选模式
    public static final int MODE_SINGLE = 0x0012;
    // 是否显示相机的extra_key
    public static final String EXTRA_SHOW_CAMERA = SelectImageActivity.EXTRA_SHOW_CAMERA;
    public static boolean mIsShowCamera;

    // 照片最多选择的个数
    public static final String EXTRA_SELECT_MAX_COUNT = SelectImageActivity.EXTRA_SELECT_MAX_COUNT;
    // 照片的 集合
    public static final String EXTRA_DEFAULT_SELECTED_LIST = SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST;
    // 照片选择的模式
    public static final String EXTRA_SELECT_MODE = SelectImageActivity.EXTRA_SELECT_MODE;
    // 返回选择图片列表的 extra_key
    public static final String EXTRA_RESULT = SelectImageActivity.EXTRA_RESULT;

    private int mMaxCount;

    private int mMode;

    private ArrayList mImageResultList;

    public static ImageSelector create() {
        mImageSelector = new ImageSelector();
        return mImageSelector;
    }

    /**
     * 最多选择的图片的个数
     */
    public ImageSelector count(int maxCount) {
        this.mMaxCount = maxCount;
        return this;
    }

    /**
     * 是否支持多选模式
     * @return
     */
    public ImageSelector multi() {
        this.mMode = MODE_MULTI;
        return this;
    }

    /**
     * 是否是单选模式
     * @return
     */
    public ImageSelector single() {
        this.mMode = MODE_SINGLE;
        return this;
    }

    /**
     * 传递的图片数据
     */
    public ImageSelector origin(ArrayList imageList) {
        this.mImageResultList = imageList;
        return this;
    }

    /**
     * 是否 需要 拍照
     */
    public ImageSelector showCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
        return this;
    }

    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addExeraParamsByIntent(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    private void addExeraParamsByIntent(Intent intent) {
        intent.putExtra(EXTRA_SELECT_MAX_COUNT, mMaxCount);
        intent.putExtra(EXTRA_SELECT_MODE, mMode);
        intent.putExtra(EXTRA_SHOW_CAMERA, mIsShowCamera);
        if (mImageResultList != null) {
            intent.putStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST, mImageResultList);
        }
    }

    public void start(Activity activity) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addExeraParamsByIntent(intent);
        activity.startActivity(intent);
    }

}
