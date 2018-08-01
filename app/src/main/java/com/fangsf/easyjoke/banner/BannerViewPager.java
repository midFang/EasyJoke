package com.fangsf.easyjoke.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public class BannerViewPager extends ViewPager {

    private BannerAdapter mBannerAdapter;

    private BannerScroll mScroll;

    // 自动滚动的标记
    private final int AUTO_ROLL_WHAT = 0x0011;

    // 自动滚动的时间
    private final int AUTO_ROLL_TIME = 2500;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        initScroll(context);
    }

    // 反射获取 Scroller类, 修改viewPager 中的 动画滚动时间
    private void initScroll(Context context) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");

            field.setAccessible(true);
            mScroll = new BannerScroll(context);
            // 将自定义的scroll 注入到当前类中
            field.set(this, mScroll);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 自动滚动
            setCurrentItem(getCurrentItem() + 1);

            // 开始滚动
            startRoll();
        }
    };

    /**
     * 设置 滚动动画的时间
     *
     * @param scrollDurationTime
     */
    public void setScrollDurationTime(int scrollDurationTime) {
        mScroll.setScrollDurationTime(scrollDurationTime);
    }


    /**
     * 开始滚动 banner
     */
    public void startRoll() {
        mHandler.removeMessages(AUTO_ROLL_WHAT);

        mHandler.sendEmptyMessageDelayed(AUTO_ROLL_WHAT, AUTO_ROLL_TIME);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 当activity 销毁的时候会回调这个方法, 销毁hanlder
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    public void setAdapter(BannerAdapter adapter) {
        // 设置自定义的 BannerAdapter
        this.mBannerAdapter = adapter;

        // 设置 自定义的adapter
        setAdapter(new BannerPagerAdapter());
        // 默认选中 中间数的最大的位置,  初始化的时候可以 往左边滑
     //   setCurrentItem(Integer.MAX_VALUE / 2);
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // 获取adapter 设计模式, 设置的view
            // position 是从0 到 integer.MAX_VALUE 的值, 会导致数组越界
            View instantiateItemView = mBannerAdapter.getView(position % mBannerAdapter.getCount());
            container.addView(instantiateItemView);


            return instantiateItemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁view
            container.removeView((View) object);
            object = null;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
