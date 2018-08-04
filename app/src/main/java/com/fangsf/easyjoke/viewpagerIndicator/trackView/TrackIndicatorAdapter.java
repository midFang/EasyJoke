package com.fangsf.easyjoke.viewpagerIndicator.trackView;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fangsf on 2018/8/4.
 * Useful:
 */
public abstract class TrackIndicatorAdapter<T extends View> {


    public abstract int getCount();

    public abstract T getView(int position, ViewGroup viewGroup);

    // 选中的时候, 高亮当前 选中
    public void getSelected(T view) {

    }

    // 其他没有选中的, 恢复默认,
    public void getUnSelected(T view) {

    }


    // 设置 底部的指示器view
    public View getBottomIndicatorView() {
        return null;
    }

}
