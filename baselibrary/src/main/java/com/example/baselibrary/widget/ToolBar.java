package com.example.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baselibrary.R;

/**
 * Created by fangsf on 2018/7/2.
 * Useful:
 */
public class ToolBar extends Toolbar {

    private String mLeftText, mRightText, mCenterText;
    private int mLeftIcon, mRightIcon;
    private int mDefaultTextSize;
    private int mTextColor = Color.DKGRAY;
    private boolean mIsFinshActivity = true;

    private ImageView mIvBack, mIvRight;
    private TextView mTvTitle, mTvRight;

    public ToolBar(Context context) {
        this(context, null);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
        mLeftIcon = array.getInt(R.styleable.ToolBar_leftIcon, mLeftIcon);
        mLeftText = array.getString(R.styleable.ToolBar_leftText);
        mCenterText = array.getString(R.styleable.ToolBar_centerText);
        mRightIcon = array.getInt(R.styleable.ToolBar_rightIcon, mRightIcon);
        mRightText = array.getString(R.styleable.ToolBar_rightText);
        mIsFinshActivity = array.getBoolean(R.styleable.ToolBar_finishActivity, true);
        mDefaultTextSize = array.getDimensionPixelSize(R.styleable.ToolBar_defaultTextSize, 15);
        mTextColor = array.getColor(R.styleable.ToolBar_defaultTextColor, mTextColor);

        array.recycle();

        setData();
    }

    private void setData() {
        mIvBack.setImageResource(R.drawable.ic_back);
        mIvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });

        if (mCenterText != null) {
            mTvTitle.setVisibility(VISIBLE);
            mTvTitle.setTextColor(mTextColor);
            //mDefaultTextSize 获取的是像素值, 而setTextSize(value) 获取是sp的值
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
            mTvTitle.setText(mCenterText);
        }

        if (mRightIcon != 0) {
            mIvRight.setVisibility(VISIBLE);
            mIvRight.setImageResource(mRightIcon);
        }

        if (mRightText != null) {
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setTextColor(mTextColor);
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
            mTvRight.setText(mRightText);
        }

    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.title_bar, null, false);

        mIvBack = (ImageView) view.findViewById(R.id.ivBack);
        mIvRight = (ImageView) view.findViewById(R.id.ivRight);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mTvRight = (TextView) view.findViewById(R.id.tvRight);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        addView(view, lp);
    }
}
