package com.example.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import com.example.framelibrary.R;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:  皮肤资源类
 */
public class SkinResource {

    // 获取资源
    private Resources mResources;

    private String mPackageName;


    public SkinResource(Context context, String skinPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            // assetManager 最重要的是要添加 path 路径
            Method addPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addPathMethod.setAccessible(true);

            // addAssetPath 注入皮肤包
            addPathMethod.invoke(assetManager, skinPath);

            // 获取内存中已经在运行的 对象
            Resources superResource = context.getResources();

            mResources = new Resources(assetManager,
                    superResource.getDisplayMetrics(), superResource.getConfiguration());

            // 获取skinPath 路径下的包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过设置资源的名称获取drawable drawable="名称"
     */
    public Drawable getDrawableByResName(String resName) {
        int resId;
        try {
            resId = mResources.getIdentifier(resName, "mipmap", mPackageName);
            return mResources.getDrawable(resId);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resId = mResources.getIdentifier(resName, "drawable", mPackageName);
                return mResources.getDrawable(resId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 通过设置资源的名称获取颜色 color="名称"
     */
    public ColorStateList getColorByResName(String resName) {
        try {
            int resId = mResources.getIdentifier(resName, "color", mPackageName);
            return mResources.getColorStateList(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
