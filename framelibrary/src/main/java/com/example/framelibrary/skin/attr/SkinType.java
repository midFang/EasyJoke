package com.example.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framelibrary.skin.SkinManager;
import com.example.framelibrary.skin.SkinResource;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:  根据类型 换肤
 */
public enum SkinType {

    // textColor 布局中属性的名字
    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            ColorStateList color = getResource().getColorByResName(resName);
            if (color != null) {
                // 可以兼容button , button 就是 继承textview的
                TextView textView = (TextView) view;
                textView.setTextColor(color);
            }
        }
    }, BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            // background 可能设置的是资源, 还可以是直接设置的颜色
            Drawable drawable = getResource().getDrawableByResName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setBackgroundDrawable(drawable);

                return;
            }

            ColorStateList color = getResource().getColorByResName(resName);
            if (color != null) {
                view.setBackgroundColor(color.getDefaultColor());
            }

        }
    }, SRC("src") {
        @Override
        public void skin(View view, String resName) {
            Drawable drawable = getResource().getDrawableByResName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        }
    };

    private static SkinResource getResource() {
        return SkinManager.getInstance().getSkinResource();
    }

    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }
}
