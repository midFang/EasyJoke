package com.example.baselibrary.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by fangsf on 2018/6/27.
 * Useful:  解决webview 中,需要停止播放音乐
 */
public class GlobalAudioUtil {

    private static final String TAG = "GlobalAudioUtil";

    public static void pauseMusic(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                Log.i(TAG, "onAudioFocusChange:  获取了焦点");

            }
        };


        // 在onPause方法中执行了如下代码（把播放音乐的焦点拿走）：
        int i = 0;
        do {
            int result = audioManager.requestAudioFocus(listener
                    , AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                break;
            }
            i++;
        } while (i < 10);
    }


    public static void focusMusic() {
        // 在onResume方法中执行如下代码（释放拿走的焦点）：
//        if (audioManager!= null) {
//            audioManager.abandonAudioFocus(listener);
//            audioManager = null;
//        }
    }


}
