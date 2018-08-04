package com.fangsf.easyjoke.viewpagerIndicator;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.framelibrary.base.BaseActivity;
import com.fangsf.easyjoke.MainActivity;
import com.fangsf.easyjoke.R;
import com.fangsf.easyjoke.viewpagerIndicator.trackView.TrackIndicatorAdapter;
import com.fangsf.easyjoke.viewpagerIndicator.trackView.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ColorTrackTextViewActivity extends BaseActivity {

    @BindView(R.id.llIndicator)
    TrackIndicatorView mIndicatorContainer;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private String[] items = {"直播", "推荐", "趣视频", "视频", "图片", "段子", "精华", "音乐", "其他",
            "精华", "同城", "游戏", "直播", "收藏"};

    private List<ColorTrackTextView> mIndicatorList;

    @Override
    protected int bindLayout() {
        return R.layout.activity_color_track_text_view;
    }

    @Override
    protected void init() {
        mIndicatorList = new ArrayList<>();

        initIndicator();

        initViewPager();
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset > 0) {

                    // 左边的文字
                    ColorTrackTextView left = mIndicatorList.get(position);
                    left.setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT_RIGHT);
                    left.setCurrentProgress(1 - positionOffset);


                    //右边的文字
                    try {
                        ColorTrackTextView right = mIndicatorList.get(position + 1);
                        right.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT_LEFT);
                        right.setCurrentProgress(positionOffset);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "onPageScrollStateChanged: ");
            }
        });

    }


    /**
     * 初始化可变色的指示器
     */
    private void initIndicator() {


//        for (int i = 0; i < items.length; i++) {
//            // 动态添加颜色跟踪的TextView
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.weight = 1;
//            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(this);
//            // 设置两种颜色
//            colorTrackTextView.setOriginColor(Color.BLACK);
//            colorTrackTextView.setChangeColor(Color.RED);
//            colorTrackTextView.setText(items[i]);
//            colorTrackTextView.setLayoutParams(params);
//            // 把新的加入LinearLayout容器
//            mIndicatorContainer.addView(colorTrackTextView);
//            // 加入集合
//            mIndicatorList.add(colorTrackTextView);
//        }

        mIndicatorContainer.setAdapter(new TrackIndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public void getSelected(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT_LEFT);
                view.setCurrentProgress(1);
            }

            @Override
            public void getUnSelected(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT_LEFT);
                view.setCurrentProgress(0);
            }

            @Override
            public View getBottomIndicatorView() {
                View view = new View(ColorTrackTextViewActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(88, 6));
                view.setBackgroundColor(Color.RED);
                return view;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup viewGroup) {


                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(ColorTrackTextViewActivity.this);
                colorTrackTextView.setGravity(Gravity.CENTER);
                colorTrackTextView.setPadding(10, 10, 10, 25);
                colorTrackTextView.setText(items[position]);
                colorTrackTextView.setTextSize(20);
                // 加入集合
                mIndicatorList.add(colorTrackTextView);


                return colorTrackTextView;
            }
        }, mViewPager, false);


    }

}
