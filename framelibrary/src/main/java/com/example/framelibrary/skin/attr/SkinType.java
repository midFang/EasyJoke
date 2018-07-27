package com.example.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:  根据类型 换肤
 */
public enum SkinType {

    TEXT_COLOR("text_color") {
        @Override
        public void skin(View view, String resName) {

        }
    }, BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {

        }
    }, SRC("src") {
        @Override
        public void skin(View view, String resName) {

        }
    };

    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }
}
