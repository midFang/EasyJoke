package com.example.baselibrary.ioc;

import android.app.Activity;
import android.view.View;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fangsf on 2018/6/29.
 * Useful:
 */
public class ViewUtils {

    public static void bind(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void bind(View view) {
        inject(new ViewFinder(view), view);
    }

    // 适配fragment Object
    public static void bind(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    // 兼容上面的上面3个方法, 注入属性和注入方法
    private static void inject(ViewFinder finder, Object object) {
        injectField(finder, object);

        injectEvent(finder, object);
    }

    /**
     * 注入属性
     */
    private static void injectField(ViewFinder finder, Object object) {
        //1 获取类所有的属性
        Class<?> clazz = object.getClass();

        //2 获取类注解上面的所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) { // 所有的属性
            // 获取属性上面的 注解的value值
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                int value = viewById.value();
                //3, findviewById 找到view
                View view = finder.findViewById(value);

                //4, 反射注入属性
                field.setAccessible(true); // 提升反射权限,private 也可以访问
                try {
                    field.set(object, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 注入事件
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        //1, 找到类的信息
        Class<?> clazz = object.getClass();
        // 2.获取类上面的所有的方法
        Method[] clazzMethods = clazz.getDeclaredMethods();

        for (Method method : clazzMethods) {
            // 获取方法上的注解值
            OnClick annotation = method.getAnnotation(OnClick.class);
            if (annotation != null) {
                int[] value = annotation.value();
                for (int i : value) {
                    View view = finder.findViewById(i);
                    if (view != null) {
                        view.setOnClickListener(new DeclaredOnClickListener(method, object)); //模仿view的onClick() 的写法
                    }
                }
            }
        }
    }

    /**
     * 参考view 的 OnClick(View view) 的源码是怎么做处理的
     */
    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Method mMethod;
        private Object mObject;

        public DeclaredOnClickListener(Method method, Object object) {
            this.mMethod = method;
            this.mObject = object;
        }

        @Override
        public void onClick(View v) {
            // 点击会调用这个方法

            //反射注入方法
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject, v); //onClick(View v)
            } catch (Exception e) {
                try {
                    // 方法注入
                    mMethod.invoke(mObject, new Object()); //兼容onClick()
                    // mMethod.invoke(mObject, null); //兼容onClick()
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

}
