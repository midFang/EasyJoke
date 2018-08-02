package com.fangsf.easyjoke.banner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public class BannerViewPager extends ViewPager {

    private static final String TAG = "BannerViewPager";
    // 当前的 activity
    private final Activity mActivity;

    private BannerAdapter mBannerAdapter;

    private BannerScroll mScroll;

    // 性能优化,缓存view
    private List<View> mConvertView;

    // 自动滚动的标记
    private final int AUTO_ROLL_WHAT = 0x0011;

    // 自动滚动的时间
    private final int AUTO_ROLL_TIME = 2500;

    // 是否按下了
    private boolean mIsPress;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mActivity = (Activity) context;
        this.mConvertView = new ArrayList<>();

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
            Log.i(TAG, "handleMessage:  startRoll -> " + mIsPress);
            // 自动滚动

            if (!mIsPress) {
                setCurrentItem(getCurrentItem() + 1);
            }

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
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    public void setAdapter(BannerAdapter adapter) {
        // 设置自定义的 BannerAdapter
        this.mBannerAdapter = adapter;

        // 设置 自定义的adapter
        setAdapter(new BannerPagerAdapter());
        // 默认选中 中间数的最大的位置,  初始化的时候可以 往左边滑
        //   setCurrentItem(Integer.MAX_VALUE / 2);


        //注册activity 生命周期回调
        mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // 获取adapter 设计模式, 设置的view
            // position 是从0 到 integer.MAX_VALUE 的值, 会导致数组越界
            View instantiateItemView = mBannerAdapter.getView(position % mBannerAdapter.getCount(),
                    getConvertView());
            container.addView(instantiateItemView);

            return instantiateItemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mConvertView.add((View) object); // 复用view
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

    /**
     * 管理activity 生命周期方法的回调, 性能优化
     */
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallback() {

        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);

            // 所有activity 的生命周期的回调, 只需要管理当前的
            if (mActivity == activity) {
                mHandler.sendEmptyMessageDelayed(AUTO_ROLL_WHAT, AUTO_ROLL_TIME);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            // 所有activity 的生命周期的回调, 只需要管理当前的
            if (mActivity == activity) {
                mHandler.removeMessages(AUTO_ROLL_WHAT);
            }
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                mIsPress = true; // 按下的时候停止滚动
                break;


            case MotionEvent.ACTION_UP:
                mIsPress = false; // 抬起的时候继续滚动
                break;


            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取复用的view
     *
     * @return
     */
    private View getConvertView() {

        for (int i = 0; i < mConvertView.size(); i++) {
            // 复用 销毁的view, 当界面滑动过快的时候, view 还没有从parentview中移除, 会报错,
            // 必须从原来的界面中移除后,才能够再次复用
            // mConvertView.get(i).getParent() == null 从parentView 中移除了, 直接返回 复用原来创建的view 就可以了
            if (mConvertView.get(i).getParent() == null) {
                return mConvertView.get(i);
            }
        }

        return null;  // 返回到setAdapter, 重新创建一个view
    }

}
