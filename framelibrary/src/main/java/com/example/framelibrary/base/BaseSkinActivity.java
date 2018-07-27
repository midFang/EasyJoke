package com.example.framelibrary.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.example.framelibrary.skin.attr.SkinAttr;
import com.example.framelibrary.skin.attr.SkinView;
import com.example.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.example.framelibrary.skin.support.SkinAttrSupport;

import java.util.List;

/**
 * Created by fangsf on 2018/7/16.
 * Useful:
 */
public abstract class BaseSkinActivity extends BaseActivity {

    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // 拦截到view 的创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.setFactory(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        View view = createView(parent, name, context, attrs);
        Log.i(TAG, " onCreateView: " + view);
        if (view != null) {
            // 一个activity 对应多个 skinView
            // 获取view 中的 属性, textColor, src, background
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);

            // 统一交给SkinManager管理
            managerSkinView(skinView);
        }



        return view;
    }

    private void managerSkinView(SkinView skinView) {

    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                true /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }


}
