package com.fangsf.easyjoke.hookactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fangsf on 2018/8/10.
 * Useful:
 */
public class HookStartActivityUtil {
    private static final String TAG = "HookStartActivityUtil";
    private final Context mContext;
    private final Class mSafeActivityClass;
    // 原来 intent 的key
    private String EXTRA_ORIGIN_INTENT = "extra_origin_intent";

    public HookStartActivityUtil(Context context, Class safeActivityClass) {
        this.mContext = context;
        this.mSafeActivityClass = safeActivityClass;
    }

    /**
     * 3 hook 启动activity
     */
    public void hookHandlerLauncherActivity() throws Exception {
        // 获取activityThread 实例
        Class<?> atClass = Class.forName("android.app.ActivityThread");
        Field scatField = atClass.getDeclaredField("sCurrentActivityThread");
        scatField.setAccessible(true);
        Object sCurrentActivityThread = scatField.get(null);

        // 获取activityThread 中的 mH
        Field mhField = atClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        Object mHandler = mhField.get(sCurrentActivityThread);

        // 给handler 设置callBack 回调, 再sendMessage
        Class<?> handlerClass = Class.forName("android.os.Handler");
        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mHandler, new handlerCallBack());

    }

    private class handlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 100) { // 源码中的 LAUNCH_ACTIVITY
                handleLaunchActivity(msg);
            }

            return false;
        }

        private void handleLaunchActivity(Message msg) {
            try {
                // 是一个 activityRecorder
                Object record = msg.obj;
                Field intentField = record.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                // 获取过了安检的intent
                Intent safeIntent = (Intent) intentField.get(record);
                // 获取绑定了的 原来的intent -> 没有在清单文件注册的activity的intent
                Intent originIntent = safeIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                //重新设置回去
                if (originIntent != null) {
                    intentField.set(record, originIntent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void hookStartActivity() throws Exception {

        // 获取 方法的执行者, 也就是反射那个类在真正执行的startActivity   -> 需要获取的类是  Singleton 中的 IActivityManager 实例
        //Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>()
        Class<?> amnClass = Class.forName("android.app.ActivityManagerNative");
        Field gDefaultField = amnClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null); // 是static 的 , 直接获取null就可以了

        // 获取 gDefault 中的实例, 真正在startActivity 的实例
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object iamInstance = mInstanceField.get(gDefault); // IActivityManager 执行者实例


        // 1, 代理启动的 startActivity 类
        // 反射android.app.IActivityManager 类
        Class<?> iamClass = Class.forName("android.app.IActivityManager");
        iamInstance = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(),
                new Class[]{iamClass}, //执行者(接口), 动态代理, 系统正在执行的 startActivity 类, 也就是做一些 替换的作用 (系统startActivity的类就是android.app.IActivityManager, 代理这个类, 自己做一些手脚)
                // InvocationHandler 必须 要有一个执行者, 谁去执行
                new StartActivityInvocationHandler(iamInstance));


        // 1.1 需要重新指定为自己设置的 实例
        mInstanceField.set(gDefault, iamInstance);
    }

    private class StartActivityInvocationHandler implements InvocationHandler {

        // 方法的执行者
        private final Object mObject;

        public StartActivityInvocationHandler(Object object) {
            this.mObject = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG, "invoke: " + method.getName());

            // 2, 创建 一个 安全的 activity 过 安全检测
            if (method.getName().equals("startActivity")) {
                // 获取 原来的的intent
                Intent originIntent = (Intent) args[2];

                //创建 一个 安全过安检的intent
                Intent safeIntent = new Intent(mContext, mSafeActivityClass);
                args[2] = safeIntent;

                // 绑定原来的intent  -> (后面过了安检后 真正启动的activity 就 替换这个intent)
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT, originIntent);
            }

            // 执行方法
            return method.invoke(mObject, args);
        }
    }

}
