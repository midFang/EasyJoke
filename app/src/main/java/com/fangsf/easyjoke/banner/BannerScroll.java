package com.fangsf.easyjoke.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public class BannerScroll extends Scroller {

    private int mScrollDurationTime = 1500;

    public BannerScroll(Context context) {
        super(context);
    }

    public BannerScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroll(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    /**
     * 设置 滚动动画的时间
     *
     * @param scrollDurationTime
     */
    public void setScrollDurationTime(int scrollDurationTime) {
        mScrollDurationTime = scrollDurationTime;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // 修改默认的时间 mScrollDurationTime
        super.startScroll(startX, startY, dx, dy, mScrollDurationTime);
    }
}
