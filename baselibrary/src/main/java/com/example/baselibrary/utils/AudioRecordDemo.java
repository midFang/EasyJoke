package com.example.baselibrary.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by Femto-iMac-003 on 2018/5/6.
 */

public class AudioRecordDemo {
    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    Object mLock;
    private double mVolume;
    private VolumeChangeListener mListener;

    public AudioRecordDemo() {
        mLock = new Object();
    }

    public void getNoiseLevel() {
        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (mAudioRecord == null) {
            Log.e("sound", "mAudioRecord初始化失败");
        }
        isGetVoiceRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioRecord.startRecording();
                short[] buffer = new short[BUFFER_SIZE];
                while (isGetVoiceRun) {
                    //r是实际读取的数据长度，一般而言r会小于buffersize
                    int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                    long v = 0;
                    // 将 buffer 内容取出，进行平方和运算
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    // 平方和除以数据总长度，得到音量大小。
                    double mean = v / (double) r;
                    mVolume = 10 * Math.log10(mean);
                    Log.i(TAG, "分贝值:" + mVolume);
                    if (mListener != null) {
                        mListener.changeing(mVolume);
                    }
                    if (mVolume > 80) {
                        Log.d(TAG, "恭喜您获得一元优惠券");
                    }

                    // 大概一秒十次
                    synchronized (mLock) {
                        try {
                            mLock.wait(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (mAudioRecord != null) {
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                }

            }
        }).start();
    }

    public void release() {

        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            isGetVoiceRun = false;
        }

    }

    public interface VolumeChangeListener {
        void changeing(double volume);

    }

    public void setVolumeListener(VolumeChangeListener listener) {
        this.mListener = listener;
    }

    public double getVolume() {
        return mVolume;
    }
}
