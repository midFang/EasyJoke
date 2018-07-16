package com.example.baselibrary.utils;

/**
 * Created by Femto-iMac-003 on 2018/4/28.
 */

public class TabIndicaterWidthUtil {

//    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
//        Class<?> tabLayout = tabs.getClass();
//        Field tabStrip = null;
//        try {
//            tabStrip = tabLayout.getDeclaredField("mTabStrip");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//        tabStrip.setAccessible(true);
//        LinearLayout ll_tab = null;
//        try {
//            ll_tab = (LinearLayout) tabStrip.get(tabs);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        int left = (int) (getDisplayMetrics(context).density * leftDip);
//        int right = (int) (getDisplayMetrics(context).density * rightDip);
//
//        for (int i = 0; i < ll_tab.getChildCount(); i++) {
//            View child = ll_tab.getChildAt(i);
//            child.setPadding(0, 0, 0, 0);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//            params.leftMargin = left;
//            params.rightMargin = right;
//            child.setLayoutParams(params);
//            child.invalidate();
//        }
//    }
//    public static DisplayMetrics getDisplayMetrics(Context context) {
//        DisplayMetrics metric = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
//        return metric;
//    }
//
//    public static float getPXfromDP(float value, Context context) {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
//                context.getResources().getDisplayMetrics());
//    }
}
