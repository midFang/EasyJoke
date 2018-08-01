package com.fangsf.easyjoke;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fangsf.easyjoke.banner.BannerAdapter;
import com.fangsf.easyjoke.banner.BannerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * banner view
 */
public class BannerViewActivity extends AppCompatActivity {


    @BindView(R.id.banner_view1)
    BannerView mBannerView;
    private ArrayList<BannerBean> mBannerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_view);
        ButterKnife.bind(this);

        mBannerList = new ArrayList<>();

        mBannerList.add(new BannerBean(R.mipmap.my_word, "亏他俩了么1"));
        mBannerList.add(new BannerBean(R.mipmap.my_word2, "邻距离2"));

        init();
    }

    protected void init() {
        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {

                BannerBean bannerBean = mBannerList.get(position);

                ImageView imageView = new ImageView(BannerViewActivity.this);
                imageView.setBackgroundColor(Color.DKGRAY);
                Glide.with(BannerViewActivity.this)
                        .load(bannerBean.getResId()).centerCrop()
                        .into(imageView);

                return imageView;
            }

            @Override
            public int getCount() {
                return mBannerList.size();
            }

            @Override
            public String getTitle(int position) {
                return mBannerList.get(position).getTitle();
            }
        });


        mBannerView.startRoll();
    }

    class BannerBean {
        int resId;
        String title;

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public BannerBean(int resId, String title) {
            this.resId = resId;
            this.title = title;
        }
    }

}
