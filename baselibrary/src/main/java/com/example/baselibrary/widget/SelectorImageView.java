package com.example.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.example.baselibrary.R;


/**
 * Created by fangsf on 2018/3/15.
 * Useful:
 */

public class SelectorImageView extends ImageView implements Checkable {

    private boolean isChecked;
    private Drawable mSelectorDrawable;
    private Drawable mDrawable;

    public SelectorImageView(Context context) {
        this(context, null);
    }

    public SelectorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**获取默认属性src的Drawable并用成员变量保存*/
        mDrawable = getDrawable();
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectorImageView);
        /**获取自定义属性selector_src的Drawable并用成员变量保存*/
        Drawable d = a.getDrawable(R.styleable.SelectorImageView_selector_src);
        mSelectorDrawable = d;
        /**获取自定义属性checked的值并用成员变量保存*/
        isChecked = a.getBoolean(R.styleable.SelectorImageView_checked, false);
        setChecked(isChecked);
        if (d != null && isChecked) {
            /**如果在布局中设置了selector_src与checked = true，我们就要设置ImageView的图片为mSelectorDrawable */
            setImageDrawable(d);
        }
        a.recycle();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        /**此处依据是否选中来设置不同的图片*/
        if (isChecked()) {
            setImageDrawable(mSelectorDrawable);
        } else {
            setImageDrawable(mDrawable);
        }
    }

    public void toggle(boolean checked) {
        /**外部通过调用此方法传入checked参数，然后把值传入给setChecked（）方法改变当前的选中状态*/
        setChecked(checked);
        toggle();
    }

}
