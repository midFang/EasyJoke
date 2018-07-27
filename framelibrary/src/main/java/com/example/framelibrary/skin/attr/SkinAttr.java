package com.example.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:
 */

public class SkinAttr {

    private String mResName;

    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View view) {
        mSkinType.skin(view, mResName);
    }
}
