package com.fangsf.easyjoke.viewpagerIndicator.trackView;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.midi.MidiDevice;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.fangsf.easyjoke.R;

/**
 * Created by fangsf on 2018/8/4.
 * Useful:  横向滑动的scrollView, 使用adapter 设计模式, 用户自定义数据适配
 */
public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private static final String TAG = "TrackIndicatorView";

    // 是否执行 滚动
    private boolean mIsExcuteScroll = false;

    // 父容器view,
    private IndicatorContainerView mParentContainerView;

    // viewPager 切换的时候,是否 滑动切换
    private boolean mSmoothScroll = true;

    // 自定义的 适配器adapter
    private TrackIndicatorAdapter mAdapter;

    // 默认显示多少个条目
    private int mVisibleNum;

    //每一个 item宽度
    private int mItemWidth;

    //当前滑动的viewpager
    private ViewPager mViewPager;

    // 当前选中的位置
    private int mCurrentPosition = 0;


    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);

        mParentContainerView = new IndicatorContainerView(context);
        addView(mParentContainerView);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);

        // 默认 显示多少个itemView
        mVisibleNum = array.getInt(R.styleable.TrackIndicatorView_visibleNum, mVisibleNum);

        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            // 指定每一个item的宽度
            mItemWidth = getItemWidth();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View childAtItemView = mParentContainerView.getItemView(i);
                ViewGroup.LayoutParams layoutParams = childAtItemView.getLayoutParams();
                layoutParams.width = mItemWidth;

                // 在 Android 7.0 以下设置params 无效,需要重新设置layoutParams()
                childAtItemView.setLayoutParams(layoutParams);
            }

            // 设置底部指示器的view
            mParentContainerView.addBottomIndicatorView(mAdapter.getBottomIndicatorView(), mItemWidth);
        }
    }

    private int getItemWidth() {

        int parentWidth = getWidth();
        if (mVisibleNum != 0) {
            return parentWidth / mVisibleNum; // 显示多少个条目
        }

        int itemWidth = 0;
        int maxWidth = 0;
        int allWidth = 0;

        //没有设置 需要显示多少个条目, 取条目中最大宽度作为所有条目的宽度,动态适配
        for (int i = 0; i < mAdapter.getCount(); i++) {
            int currentWidth = mParentContainerView.getItemView(i).getWidth();
            maxWidth = Math.max(currentWidth, maxWidth);
        }

        itemWidth = maxWidth; // 取最大的宽度

        allWidth = mAdapter.getCount() * itemWidth; // 最大宽度的width
        if (allWidth < parentWidth) {
            //所有的 width 小于屏幕宽度
            itemWidth = parentWidth / mAdapter.getCount();
        }

        return itemWidth;
    }

    public void setAdapter(TrackIndicatorAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter not null ");
        }

        this.mAdapter = adapter;

        // 将itemview 添加到容器中
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View itemView = mAdapter.getView(i, mParentContainerView);
            mParentContainerView.addItemView(itemView);


            if (mViewPager != null) {
                switchItemViewClick(itemView, i);
            }
        }

        // 设置默认选中
        mAdapter.getSelected(mParentContainerView.getItemView(0));
    }

    /**
     * 设置每个条目的 view 的事件
     *
     * @param itemView
     * @param position
     */
    private void switchItemViewClick(View itemView, final int position) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // msotthScroll 为false viewPager 的addOnPageChangeListener .onPageScrolled方法不会调用, 解决bug,
                mViewPager.setCurrentItem(position, mSmoothScroll);

                // 移动 indicatorView
                smoothScrollIndicatorView(position);

                // 移动 bottomIndicatorView
                mParentContainerView.scrollBottomIndicatorView(position);
            }
        });
    }

    private void smoothScrollIndicatorView(int position) {

        // 总共滚动的指示器
        float totalScroll = (position) * mItemWidth;
        // 保持在屏幕中间的 偏移量
        int offsetScroll = (getWidth() - mItemWidth) / 2;

        // 最终要滚动的距离
        final int finalScroll = (int) (totalScroll - offsetScroll);
        // 开始滚动
        smoothScrollTo(finalScroll, 0);
    }

    public void setAdapter(TrackIndicatorAdapter adapter, ViewPager viewPager) {
        setAdapter(adapter, viewPager, true);
    }

    public void setAdapter(TrackIndicatorAdapter adapter, ViewPager viewPager, boolean smoothScroll) {
        this.mSmoothScroll = smoothScroll;
        if (viewPager == null) {
            throw new NullPointerException("viewPager not null ");
        }
        this.mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(this);

        setAdapter(adapter);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled:  -> " + position + "  positionOffset  -> " + positionOffset);

        // 在点击条目的时候,不触发这个 方法

        if (mIsExcuteScroll) {
            scrollCurrentIndicator(position, positionOffset);
            // 滚动底部指示器
            mParentContainerView.scrollBottomIndicatorView(position, positionOffset);
        }

    }

    /**
     * 不断滚动当前的指示器
     *
     * @param position
     * @param positionOffset
     */
    private void scrollCurrentIndicator(int position, float positionOffset) {

        // 总共滚动的指示器
        float totalScroll = (position + positionOffset) * mItemWidth;
        // 保持在屏幕中间的 偏移量
        int offsetScroll = (getWidth() - mItemWidth) / 2;

        // 最终要滚动的距离
        final int finalScroll = (int) (totalScroll - offsetScroll);
        // 开始滚动
        scrollTo(finalScroll, 0);
    }

    @Override
    public void onPageSelected(int position) {

        // 恢复之前没有选中的状态
        mAdapter.getUnSelected(mParentContainerView.getItemView(mCurrentPosition));

        this.mCurrentPosition = position;
        mAdapter.getSelected(mParentContainerView.getItemView(mCurrentPosition));

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged: " + state);

        if (state == 0) { // 空闲
            mIsExcuteScroll = false;
        }
        if (state == 1) { // 正在滑动
            mIsExcuteScroll = true;
        }
    }
}
