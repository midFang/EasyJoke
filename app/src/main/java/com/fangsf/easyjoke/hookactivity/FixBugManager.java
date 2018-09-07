package com.fangsf.easyjoke.hookactivity;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by fangsf on 2018/7/6.
 * Useful: 类的加载机制,修复dex中加载的element  (需要注意: 类的加载的顺序)
 */
public class FixBugManager {
    private static final String TAG = "FixBugManager";

    private Context mContext;

    // 系统可以访问的 odex 文件路径
    private File mDexDir;

    public FixBugManager(Context context) {
        this.mContext = context;
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }


    public void fixBug(String fixDexPath) throws Exception {

        // 2, 获取修复好了 dex 文件, dexElement
        File fixDexFile = new File(fixDexPath);
        if (!fixDexFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }

        // 创建文件
        File destFile = new File(mDexDir, fixDexFile.getName());

        if (destFile.exists()) { // 已经修复过了

            Log.i(TAG, "fixBug: 已经修复过了");
            destFile.delete();
//            return;

        }

        // copy 文件
        copyFile(fixDexFile, destFile);


        //开始修复, Classloader 读取 fixDex路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDesFiles(fixDexFiles);
    }

    private void injectDexElement(ClassLoader classLoader, Object dexElement) throws Exception {
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);

        Object pathList = pathListField.get(classLoader); // todo .get 获取的是什么

        // 获取pathList中的dexElement
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);


        // 注入属性
        dexElementsField.set(pathList, dexElement);
    }


    /**
     * 合并数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 获取 dexElement
     *
     * @param classLoader
     * @return
     */
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {

        // 获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);

        Object pathList = pathListField.get(classLoader);

        // 获取pathList中的dexElement
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        Object dexElement = dexElementsField.get(pathList);

        return dexElement;
    }

    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 修复已经下载好的dex包
     */
    public void loadFixDex() throws Exception {

        File[] files = mDexDir.listFiles();

        List<File> fixDexFiles = new ArrayList<>();

        for (File file : files) {
            if (file.getName().endsWith(".dex")) {
                fixDexFiles.add(file);
            }
        }

        //修复
        fixDesFiles(fixDexFiles);
    }

    /**
     * 加载修复的dex包, 解析成element, 注入
     * @param fixDexFiles
     * @throws Exception
     */
    private void fixDesFiles(List<File> fixDexFiles) throws Exception {
        //1, 获取当前正在运行的classLoader,可以获取到正在运行的element
        ClassLoader applicationClassLoader = mContext.getClassLoader();

        //通过反射获取正在运行的dexElement
        Object applicationDexElement = getDexElementByClassLoader(applicationClassLoader);

        // 解压路径
        File librarySearchPath = new File(mDexDir, "odex");
        if (!librarySearchPath.exists()) {
            librarySearchPath.mkdirs();
        }

        for (File dexFile : fixDexFiles) {

            // fixDex, 解析成classLoader
            ClassLoader fixLoader = new BaseDexClassLoader(
                    dexFile.getAbsolutePath(),
                    librarySearchPath,
                    null, applicationClassLoader
            );

            Object fixDexElements = getDexElementByClassLoader(fixLoader);

            // 3, 将修复好的 dexElement 插入到 原来的 dexElement 前面
            applicationDexElement = combineArray(fixDexElements, applicationDexElement);
        }

        // 注入 applicationDexElement
        injectDexElement(applicationClassLoader, applicationDexElement);
    }
}
