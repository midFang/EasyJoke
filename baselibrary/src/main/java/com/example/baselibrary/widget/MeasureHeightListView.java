package com.example.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by fangsf on 2018/6/25.
 * Useful: 解决scrollView  和 listview 重用优化, ( listView 重新计算子view的高度)
 */
public class MeasureHeightListView extends ListView {


    public MeasureHeightListView(Context context) {
        this(context, null);
    }

    public MeasureHeightListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 重新计算高度, 右移两位, 带数据值位
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
