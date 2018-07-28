package com.example.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.example.framelibrary.skin.attr.SkinView;
import com.example.framelibrary.skin.callback.SkinChangeListener;
import com.example.framelibrary.skin.config.PreSkinUtil;
import com.example.framelibrary.skin.config.SkinConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:  皮肤的资源管理类
 */
public class SkinManager {


    // 所有activity 需要切换的view
    private Map<SkinChangeListener, List<SkinView>> mSkinViews = new HashMap<>();

    private SkinResource mSkinResource;

    private SkinManager() {
    }

    private Context mContext;

    private static SkinManager mInstance;

    static {
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();

        //判断换肤的apk是否存在
        String saveSkinPath = PreSkinUtil.getInstance(mContext).getSkinPath();
        File file = new File(saveSkinPath);
        if (!file.exists()) {
            return;
        }

        // 获取skinPath 路径下的包名
        String packageName = context.getPackageManager().getPackageArchiveInfo(saveSkinPath,
                PackageManager.GET_ACTIVITIES).packageName;

        // 如果不是一个apk, 也不加载
        if (TextUtils.isEmpty(packageName)) {
            PreSkinUtil.getInstance(mContext).clearSkinInfo();
            return;
        }

        // 初始化 SkinResource
        mSkinResource = new SkinResource(mContext, saveSkinPath);

    }

    /**
     * 加载资源皮肤
     *
     * @param skinPath 需要加载的皮肤资源包
     */
    public int loadSkin(String skinPath) {
        if (mContext == null) {
            throw new RuntimeException("SkinManager  context not init !!!");
        }

        // 先判断文件是否存在
        File skinFile = new File(skinPath);
        if (!skinFile.exists()) {
            return SkinConfig.SKIN_FILE_NOEXISTS;
        }

        String saveSkinPath = PreSkinUtil.getInstance(mContext).getSkinPath();
        if (skinPath.equals(saveSkinPath)) {
            return SkinConfig.SKIN_NOTHING_TODO;
        }

        // 初始化 资源加载类 SkinResource
        mSkinResource = new SkinResource(mContext, skinPath);

        changeSkin();

        // 保存换肤的状态, 下次进入的时候, 自动换肤
        saveChangeSkinStatus(skinPath);

        return SkinConfig.SKIN_LOAD_SUCCESS;
    }

    /**
     * 改变皮肤
     */
    private void changeSkin() {
        Set<SkinChangeListener> activitySet = mSkinViews.keySet();
        for (SkinChangeListener key : activitySet) {

            // 已经打开的activity 的view 也要换肤
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                // 换肤
                skinView.skin();
            }

            // 回掉监听
            key.changeSkin(mSkinResource);
        }
    }

    /**
     * 保存换肤的状态, 就是保存换肤apk的路径
     *
     * @param skinPath
     */
    private void saveChangeSkinStatus(String skinPath) {
        PreSkinUtil.getInstance(mContext).saveSkinPath(skinPath);
    }

    public SkinResource getSkinResource() {
        return mSkinResource;
    }


    /**
     * 获取默认的皮肤, 就是加载自己apk的资源
     *
     * @return
     */
    public int restoreDefault() {

        // 判断当前的皮肤状态
        String saveSkinPath = PreSkinUtil.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(saveSkinPath)) {
            // 代表已经是 默认的皮肤状态了, 不需要再切换皮肤了
            return SkinConfig.SKIN_NOTHING_TODO;
        }

        String skinPath = mContext.getPackageResourcePath();
        if (!TextUtils.isEmpty(skinPath)) {
            // 重新加载资源
            mSkinResource = new SkinResource(mContext, skinPath);
            changeSkin();

            // 将换肤的状态信息清除
            PreSkinUtil.getInstance(mContext).clearSkinInfo();
        }

        return SkinConfig.SKIN_LOAD_SUCCESS;
    }

    public List<SkinView> getSkinViews(SkinChangeListener skinChangeListener) {
        List<SkinView> skinViews = mSkinViews.get(skinChangeListener);
        return skinViews;
    }

    /**
     * 存储activity 所有需要换肤的view
     */
    public void register(SkinChangeListener listener, List<SkinView> skinViews) {
        mSkinViews.put(listener, skinViews);
    }

    /**
     * 检测是否需要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        String skinPath = PreSkinUtil.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(skinPath)) {
            // 换肤
            skinView.skin();
        }
    }

    /**
     * 防止内存泄漏, 移除引用
     * @param skinChangeListener
     */
    public void unRegister(SkinChangeListener skinChangeListener) {

        mSkinViews.remove(skinChangeListener);
    }
}
