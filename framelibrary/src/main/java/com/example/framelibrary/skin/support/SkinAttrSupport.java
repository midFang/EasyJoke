package com.example.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.framelibrary.skin.attr.SkinAttr;
import com.example.framelibrary.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangsf on 2018/7/27.
 * Useful:  属性解析的支持类
 */
public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";
    /**
     * 获取view的属性
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {

        List<SkinAttr> attrList = new ArrayList<>();

        // 一共有多少属性
        int attributeCount = attrs.getAttributeCount();
        for (int index = 0; index < attributeCount; index++) {
            // 获取资源的名字, 和资源的value值,存储在skinAttr中
            String attrName = attrs.getAttributeName(index);  // src background layout_width
            String attrValue = attrs.getAttributeValue(index);  // #fff, @int
            Log.i(TAG, "getSkinAttrs: " + attrName + " attrValue -> " + attrValue);

            SkinType skinType = getSkinType(attrName);
            if (skinType != null) {
                String resName = getSkinResName(context,attrValue);
                if (TextUtils.isEmpty(resName)) {
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                attrList.add(skinAttr);
            }
        }


        return attrList;
    }

    /**
     * 通过资源的值, 拿名称
     *
     * @param context
     * @param attrValue
     * @return
     */
    private static String getSkinResName(Context context, String attrValue) {
        if (attrValue.startsWith("@")) {
            attrValue =   attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);

            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
             // 存在这个type 就返回
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }
}
