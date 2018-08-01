package com.fangsf.easyjoke.banner;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangsf.easyjoke.R;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public class BannerView extends RelativeLayout {

    private BannerViewPager mBannerViewPager;
    // 广告横幅文字
    private TextView mBannerDesc;
    // banner 指示器
    private LinearLayout mBannerDotIndicator;

    // 指示器底部的view
    private View mBottomView;

    //设置 指示器的drawable, 这里也可以直接自定义CircleView
    private Drawable mIndicatorFocusDrawable;
    private Drawable mIndicatorNormalDrawable;

    private BannerAdapter mBannerAdapter;
    private Context mContext;

    // 当前 指示器选中的位置
    private int mCurrentPosition = 0;

    private int mDotGravity;

    private int mDotSize = 8;

    private int mDotDistance = 7;

    private int mBottomColor = Color.parseColor("#88000000");
    private float mWidthProportion;

    private float mHeightProportion;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        // 添加到当前view上
        inflate(context, R.layout.ui_banner_layout, this);

        initViews();

        initAttribute(attrs);

    }

    private void initViews() {
        mBannerViewPager = findViewById(R.id.banner_view_pager);
        mBannerDesc = findViewById(R.id.banner_desc);
        mBannerDotIndicator = findViewById(R.id.banner_indicator);
        mBottomView = findViewById(R.id.banner_bottom);
    }

    /**
     * 8.初始化自定义属性
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        // 获取点的位置
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity, mDotGravity);
        // 获取点的颜色（默认、选中）
        mIndicatorFocusDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mIndicatorFocusDrawable == null) {
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mIndicatorNormalDrawable == null) {
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        // 获取点的大小和距离
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));
        // 获取底部的颜色
        mBottomColor = array.getColor(R.styleable.BannerView_bottomColor, mBottomColor);
        // 获取宽高比例 ->
        mWidthProportion = array.getFloat(R.styleable.BannerView_withProportion, mWidthProportion);
        mHeightProportion = array.getFloat(R.styleable.BannerView_heightProportion, mHeightProportion);

        array.recycle();
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        mBannerViewPager.setAdapter(adapter);


        initBottomDotIndicator();

        mBannerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                pageSelect(position);

            }
        });

        // 默认选中第一个 位置
        String title = mBannerAdapter.getTitle(0);
        mBannerDesc.setText(title);

        if (mWidthProportion == 0 || mHeightProportion == 0) {
            return;
        }
        int width = getMeasuredWidth();
        // 高度动态适配  -> imageHeight / imageWidth = height / width
        int height = (int) ((mHeightProportion * width) / mWidthProportion);
        getLayoutParams().height = height;
    }

    /**
     * 设置选中的文字
     */
    private void pageSelect(int position) { // 从 0 -> Integer.MAX_VALUE

        // 上一次的设置 默认颜色
        BannerIndicator oldIndicator = (BannerIndicator) mBannerDotIndicator.getChildAt(mCurrentPosition);
        oldIndicator.setDrawable(mIndicatorNormalDrawable);

        // 当前的设置为高亮
        this.mCurrentPosition = position % mBannerAdapter.getCount();
        BannerIndicator newIndicator = (BannerIndicator) mBannerDotIndicator.getChildAt(mCurrentPosition);
        newIndicator.setDrawable(mIndicatorFocusDrawable);

        String title = mBannerAdapter.getTitle(position % mBannerAdapter.getCount());
        mBannerDesc.setText(title);
    }

    /**
     * 初始化点 指示器
     */
    private void initBottomDotIndicator() {

        mBottomView.setBackgroundColor(mBottomColor);

        mBannerDotIndicator.setGravity(getGravity(mDotGravity));
        //    mBannerDotIndicator.setPadding(0, 0, dip2px(12), 0);


        int count = mBannerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            // 画指示器
            BannerIndicator bannerIndicator = new BannerIndicator(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            params.leftMargin = mDotDistance;
            bannerIndicator.setLayoutParams(params);

            // 选中的位置 高亮
            if (i == 0) {
                bannerIndicator.setDrawable(mIndicatorFocusDrawable);
            } else {
                bannerIndicator.setDrawable(mIndicatorNormalDrawable);
            }

            mBannerDotIndicator.addView(bannerIndicator);

        }

    }

    private int getGravity(int dotGravity) {
        int gravity = Gravity.RIGHT;
        switch (dotGravity) {
            case 0:
                gravity = Gravity.CENTER;
                break;

            case 1:
                gravity = Gravity.RIGHT;
                break;

            case -1:
                gravity = Gravity.LEFT;
                break;

            default:
                break;
        }
        return gravity;
    }


    private int dip2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void startRoll() {
        mBannerViewPager.startRoll();
    }


}
