package com.fangsf.easyjoke;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.example.framelibrary.base.BaseSkinActivity;

import java.io.File;
import java.lang.reflect.Method;

public class ChangeSkinActivity extends BaseSkinActivity {


    @Override
    protected int bindLayout() {
        return R.layout.activity_change_skin;
    }

    @Override
    protected void init() {


    }

    public void onClick(View view) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            // assetManager 最重要的是要添加 path 路径
            Method addPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addPathMethod.setAccessible(true);

            // 注入
            addPathMethod.invoke(assetManager, Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "red.skin");

            // 获取内存中已经在运行的 对象
            Resources superResource = getResources();

            Resources resources = new Resources(assetManager,
                    superResource.getDisplayMetrics(), superResource.getConfiguration());

            // 获取资源的id
            int id = resources.getIdentifier("my_word", "mipmap", "com.example.fangsf.testscreen");

            Drawable drawable = resources.getDrawable(id);
            ((ImageView) findViewById(R.id.iv_girl)).setImageDrawable(drawable);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
