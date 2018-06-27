package com.example.baselibrary.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Femto-iMac-003 on 2018/5/7.
 */

public class MediaHelper {

    private static MediaPlayer mPlayer;

    // 音频是否正在播放
    private static boolean isPause = true;

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener listener) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        } else {
            mPlayer.reset();
        }
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(listener);
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mPlayer.reset();
                return false;
            }
        });
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException("读取文件异常：" + e.getMessage());
        }
        mPlayer.start();
        isPause = false;
    }

    public static void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 歌曲是否暂停
     *
     * @return
     */
    public static boolean getIsPause() {
        return isPause;
    }

    /**
     * 播放完成,设置暂停状态
     */
    public static void setPlayComplete() {
        isPause = true;
    }


    public static int getDuration() {
        if (mPlayer != null) {
            mPlayer.getDuration();
        }
        return 0;
    }

    public static int getCurrentDuration() {
        if (mPlayer != null) {
            mPlayer.getCurrentPosition();
        }
        return mPlayer.getCurrentPosition();
    }


    // 继续
    public static void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
