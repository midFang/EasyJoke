package com.fangsf.easyjoke.banner;

import android.view.View;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public abstract class BannerAdapter {

    public abstract View getView(int position, View convertView);

    public abstract int getCount();

    public String getTitle(int position) {
        return "";
    }
}
