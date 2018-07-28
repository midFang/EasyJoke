package com.example.framelibrary.skin.config;

import android.content.Context;

/**
 * Created by fangsf on 2018/7/28.
 * Useful:
 */
public class PreSkinUtil {
    private static PreSkinUtil mInstance;
    private Context mContext;

    private PreSkinUtil(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public static PreSkinUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PreSkinUtil.class) {
                if (mInstance == null) {
                    mInstance = new PreSkinUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存皮肤路径
     *
     * @param skinPath
     * @return
     */
    public boolean saveSkinPath(String skinPath) {
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH, skinPath).commit();
    }

    public String getSkinPath() {
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH, "");
    }

    /**
     * 清除保存的换肤 apk 路径
     */
    public boolean clearSkinInfo() {
        return saveSkinPath("");
    }
}
