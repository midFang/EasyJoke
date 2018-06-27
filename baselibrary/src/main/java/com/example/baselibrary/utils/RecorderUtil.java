package com.example.baselibrary.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

/**
 * Created by Femto-iMac-003 on 2018/5/2.
 */

public class RecorderUtil {

    MediaRecorder mMediaRecorder;
    MediaPlayer mediaPlayer;
    Context context;
    File mRecAudioFile;
    File mRecAudioPath;
    String strTempFile = "recaudio_";
    private static MediaRecorder m = null;

    public RecorderUtil(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    private MediaRecorder getInstance() {
        if (m == null) {
            m = new MediaRecorder();
        }
        return m;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void startRecord() {
        try {
            if (!initRecAudioPath()) {
                return;
            }

            if (mMediaRecorder != null) {
                return;
            }
            try {

                mMediaRecorder = getInstance();

                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                mRecAudioFile = File.createTempFile(strTempFile, ".amr",
                        mRecAudioPath);

                // mRecAudioFile = new File(mRecAudioPath, strTempFile + ".amr");

            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {

        try {

            mMediaRecorder = getInstance();
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;  // 必须把两个都销毁
            m = null;  // 必须把两个都销毁
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playMusic(String path) {
        /*
         * Intent intent = new Intent();
         * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         * intent.setAction(android.content.Intent.ACTION_VIEW);
         * intent.setDataAndType(Uri.fromFile(mRecAudioFile), "audio");
         * context.startActivity(intent);
         */

//        if (mMediaRecorder != null) {
//            return;
//        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * 创建文件夹
     *
     * @return
     */
    private boolean initRecAudioPath() {
        if (sdcardIsValid()) {
            String path = Environment.getExternalStorageDirectory().toString()
                    + File.separator + "record";
            mRecAudioPath = new File(path);
            if (!mRecAudioPath.exists()) {
                mRecAudioPath.mkdirs();
            }
        } else {
            mRecAudioPath = null;
        }
        return mRecAudioPath != null;
    }

    private boolean sdcardIsValid() {
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {

        }
        return false;
    }
}
