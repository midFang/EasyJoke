package com.example.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:
 */
public class SkinView {

    // 当前的view
    private View mView;

    // 需要换肤的view的属性
    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mAttrs = skinAttrs;
    }

    public void skin() {
        for (SkinAttr attr : mAttrs) {
            // 注入到属性中
            attr.skin(mView);
        }
    }
}
