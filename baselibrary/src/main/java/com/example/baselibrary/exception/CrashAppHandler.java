package com.example.baselibrary.exception;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by fangsf on 2018/5/1.
 * Useful:
 */

public class CrashAppHandler extends CrashAppLog {
    public static CrashAppHandler mCrashAppHandler = null;


    private CrashAppHandler() {
    }


    public static CrashAppHandler getInstance() {

        if (mCrashAppHandler == null) {
            synchronized (CrashAppHandler.class) {
                if (mCrashAppHandler == null) {
                    mCrashAppHandler = new CrashAppHandler();
                }
            }
        }

        return mCrashAppHandler;

    }

    @Override
    public void initParams(CrashAppLog crashAppLog) {

        if (crashAppLog != null) {

            crashAppLog.setCAHCE_CRASH_LOG(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crashLog");
            crashAppLog.setLIMIT_LOG_COUNT(1);  // 只能有一个文件
        }
    }

    /**
     * 可上传服务器
     *
     * @param folder 文件路径
     * @param file   文件
     */
    @Override
    protected void sendCrashLogToServer(File folder, File file) {
        Log.e("*********", "文件夹:" + folder.getAbsolutePath() + " - " + file.getAbsolutePath() + "");

        //给外部调用,外部是否删除服务器
        if (mListener != null) {
            mListener.logInfo(folder, file);
        }
    }

    public interface OnCrashLogListener {
        void logInfo(File folder, File file);
    }

    private OnCrashLogListener mListener;

    public void setCrashLogListener(OnCrashLogListener listener) {
        mListener = listener;
    }
}
