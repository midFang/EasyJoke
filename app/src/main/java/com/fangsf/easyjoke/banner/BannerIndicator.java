package com.fangsf.easyjoke.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fangsf on 2018/8/1.
 * Useful:
 */
public class BannerIndicator extends View {

    // 指示器的 drawable
    private Drawable mIndicatorDrawable;

    public BannerIndicator(Context context) {
        this(context, null);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIndicatorDrawable != null) {

            Bitmap bitmap = drawableToBitmap(mIndicatorDrawable);

            Bitmap circleBitmap = getCircleBitmap(bitmap);

            canvas.drawBitmap(circleBitmap, 0, 0, null);
        }


    }

    /**
     * 获取圆形的 circlebitmap
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        if (bitmap != null) {

            Canvas canvas = new Canvas(circleBitmap);

            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setDither(true);
            circlePaint.setFilterBitmap(true);

            // 在画布上画一个圆
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
                    getMeasuredWidth() / 2, circlePaint);

            // 取 圆 和 原来 bitmap 形状的交集, 画笔的状态
            circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // 把原来的 bitmap 画在圆上, 取交际
            canvas.drawBitmap(bitmap, 0, 0, circlePaint);

            // 销毁bitmap
            bitmap.recycle();
            bitmap = null;

        }
        return circleBitmap;
    }

    /**
     * drawable 转换成 bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable != null) {

            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            // 创建一个空的bitmap
            Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);

            // 创建一个画布
            Canvas canvas = new Canvas(bitmap);

            // 将drawable 画在 画布上
            drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        return null;
    }

    /**
     * 设置指示器的drawable
     *
     * @param indicatorFocusDrawable
     */
    public void setDrawable(Drawable indicatorFocusDrawable) {
        this.mIndicatorDrawable = indicatorFocusDrawable;
        invalidate();
    }
}
