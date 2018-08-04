package com.fangsf.easyjoke.viewpagerIndicator.trackView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by fangsf on 2018/8/5.
 * Useful:
 */
public class IndicatorContainerView extends FrameLayout {

    // 将所有的view 添加到一个 容器中
    private LinearLayout mContainerView;
    // 底部指示器的view
    private View mBottomIndicatorView;
    // 一个条目的宽度
    private int mItemWidth;

    private int mInitLeftMargin = 0;

    private FrameLayout.LayoutParams mLayoutParams;

    public IndicatorContainerView(@NonNull Context context) {
        this(context, null);
    }

    public IndicatorContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContainerView = new LinearLayout(context);
        addView(mContainerView);

    }

    /**
     * 获取 容器中的 view
     *
     * @param index
     * @return
     */
    public View getItemView(int index) {
        return mContainerView.getChildAt(index);
    }


    /**
     * 往容器中 添加view
     *
     * @param itemView
     */
    public void addItemView(View itemView) {
        mContainerView.addView(itemView);
    }

    /**
     * 底部 横线 指示器
     */
    public void addBottomIndicatorView(View bottomIndicatorView, int itemWidth) {
        if (bottomIndicatorView == null) {
            return;
        }
        this.mBottomIndicatorView = bottomIndicatorView;
        this.mItemWidth = itemWidth;

        addView(bottomIndicatorView);

        // 添加到底部位置,并指定item的width
        mLayoutParams = (LayoutParams) bottomIndicatorView.getLayoutParams();
        mLayoutParams.gravity = Gravity.BOTTOM;

        // 指定底部指示器的 width, 读取设置的宽度,
        int indicatorWidth = mLayoutParams.width;
        if (indicatorWidth == LayoutParams.MATCH_PARENT ||
                indicatorWidth > mItemWidth) {
            indicatorWidth = mItemWidth;
        }

        mLayoutParams.width = indicatorWidth;

        //底部指示器, 宽度比较小的时候, 设置在中间显示
        mInitLeftMargin = (mItemWidth - indicatorWidth) / 2;
        mLayoutParams.leftMargin = mInitLeftMargin;

        bottomIndicatorView.setLayoutParams(mLayoutParams);

    }

    /**
     * 时刻滚动底部指示器, 改变 leftMargin
     */
    public void scrollBottomIndicatorView(int position, float positionOffset) {
        if (mBottomIndicatorView == null) {
            return;
        }

        int leftMargin = (int) ((position + positionOffset) * mItemWidth);
        mLayoutParams.leftMargin = leftMargin + mInitLeftMargin;
        mBottomIndicatorView.setLayoutParams(mLayoutParams);

    }

    /**
     * 滚动底部指示器, 改变 leftMargin  (只在点击时候的触发)
     */
    public void scrollBottomIndicatorView(int position) {
        if (mBottomIndicatorView == null) {
            return;
        }
        // 最终要 移动的 距离
        int finalLeftMargin = (int) ((position) * mItemWidth) + mInitLeftMargin;

        // 获取当前的位置的 leftMargin
        int currentLeftMargin = mLayoutParams.leftMargin;

        // 移动距离
        int distance = finalLeftMargin - currentLeftMargin;

        // 开启动画, 一直改变 leftMargin 的值
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(currentLeftMargin,
                finalLeftMargin).setDuration((long) (Math.abs(distance) * 0.14f));

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mLayoutParams.leftMargin = (int) value;
                mBottomIndicatorView.setLayoutParams(mLayoutParams);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();

    }
}
