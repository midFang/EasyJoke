package com.example.baselibrary.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Femto-iMac-003 on 2018/5/29.
 */

public class AppManager {

    private String TAG = this.getClass().getSimpleName();

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }

        return instance;
    }

    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                Log.i(TAG, " finishAllActivity: " + activityStack.get(i));
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }



    public void exitApp() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static long exitTime = 0;

    /**
     * 作用如：2s内双击两次返回则退出程序
     *
     * @return 是否退出程序
     */
    public static boolean exitBy2Click() {
        return exitBy2Click(2000);
    }

    /**
     * 在某个时间段内双击两次
     *
     * @param space 两次点击最大时间间隔
     * @return 是否退出
     */
    public static boolean exitBy2Click(int space) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }

}

