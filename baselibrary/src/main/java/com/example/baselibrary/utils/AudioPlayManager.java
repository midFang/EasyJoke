package com.example.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;


import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayManager implements OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "AudioPlayManager";

    private static AudioPlayManager mAudioPlayManager;

    private MediaPlayer mPlayer;

    private ExecutorService worker;

    private AudioManager.OnAudioFocusChangeListener focusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int change) {
            Log.d(TAG, "focus change: " + change);
            switch (change) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    pause();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    resume();
                    break;
            }
        }
    };

    public interface PlayListener {
        void onAudioStart();

        void onComplete();

        void onError();
    }

    private ConcurrentLinkedQueue<PlayListener> mPlayListenerLists;

    enum State {
        PLAYING, PAUSE, STOPED, ERROR
    }

    private State mState = State.STOPED;
    private AudioManager mAudioManager;

    /**
     * 构造方法
     */
    public AudioPlayManager(Context context) {
        mPlayer = new MediaPlayer();

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayListenerLists = new ConcurrentLinkedQueue<PlayListener>();

        worker = Executors.newSingleThreadExecutor();
        mAudioManager = (AudioManager) context.getSystemService(Activity.AUDIO_SERVICE);
    }

    /**
     * get AudioPlayManager Instance
     *
     * @param context
     * @return
     */
    public static synchronized AudioPlayManager getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null!");
        }
        if (mAudioPlayManager == null) {
            mAudioPlayManager = new AudioPlayManager(context);
        }
        return mAudioPlayManager;
    }

    /**
     * 打开并播放一段音频
     *
     * @param filePath 音频路径
     */
    public void openAndPlay(final String filePath) {

        worker.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mPlayer.reset();
                    mPlayer.setDataSource(filePath);
                    mPlayer.prepareAsync();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    onStateChange(State.STOPED);
                } catch (Exception e) {
                    e.printStackTrace();
                    onStateChange(State.ERROR);
                    dispatchErrorListenner();
                }
            }
        });
    }

    /**
     * 暂停播放
     */
    public void pause() {
        Log.i(TAG, "pause media, current state: " + mState);
        if (mState == State.PLAYING) {
            worker.execute(new Runnable() {
                @Override
                public void run() {
                    onStateChange(State.PAUSE);
                    mPlayer.pause();
                }
            });
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        Log.i(TAG, "resume media, current state: " + mState);
        if (mState == State.PAUSE) {
            worker.execute(new Runnable() {
                @Override
                public void run() {
                    onStateChange(State.PLAYING);
                    mPlayer.start();
                }
            });
        }

    }

    /**
     * 停止播放
     */
    public void stop() {
        Log.i(TAG, "stop media, current state: " + mState);
        try {
            if (mState != null) {
                worker.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "stop play1111");
                        if (mState == State.STOPED) {
                            return;
                        }
                        onStateChange(State.STOPED);
                        mPlayer.stop();
                        mPlayer.reset();
                        mAudioManager.abandonAudioFocus(focusListener);
                        dispatchCompleteListener();
                    }
                });
            } else {
                Log.i(TAG, "mState is null");
            }
        } catch (IllegalStateException e) {
            onStateChange(State.STOPED);
            e.printStackTrace();
        } catch (Exception e) {
            onStateChange(State.STOPED);
            e.printStackTrace();
        }
    }

    private void onStateChange(State state) {
        this.mState = state;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion");
        mAudioManager.abandonAudioFocus(focusListener);
        onStateChange(State.STOPED);
        dispatchCompleteListener();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onPrepared");
        mAudioManager.requestAudioFocus(focusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (mPlayer != null) {
            mPlayer.start();
            onStateChange(State.PLAYING);
            dispatchStartListener();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        if (i == 1) {
            onStateChange(State.ERROR);
            dispatchErrorListenner();
        }
        return true;
    }

    /**
     * 注册播放状态监听器
     *
     * @param playListener 播放状态监听器
     */
    public void registerListener(PlayListener playListener) {
        if (playListener != null) {
            if (!mPlayListenerLists.contains(playListener)) {
                mPlayListenerLists.add(playListener);
            }
        }
    }

    /**
     * 注册播放状态监听器
     *
     * @param playListener 播放状态监听器
     */
    public void unRegisterListener(PlayListener playListener) {
        if (playListener != null) {
            if (mPlayListenerLists.contains(playListener)) {
                mPlayListenerLists.remove(playListener);
            }
        }
    }


    private void dispatchErrorListenner() {
        Iterator<PlayListener> iter = mPlayListenerLists.iterator();
        if (iter.hasNext()) {
            PlayListener listener = iter.next();
            if (listener != null) {
                listener.onError();
            }
        }
    }

    private void dispatchCompleteListener() {
        Iterator<PlayListener> iter = mPlayListenerLists.iterator();
        while (iter.hasNext()) {
            PlayListener listener = iter.next();
            if (listener != null) {
                listener.onComplete();
            }
        }
    }

    private void dispatchStartListener() {
        Iterator<PlayListener> iter = mPlayListenerLists.iterator();
        while (iter.hasNext()) {
            PlayListener listener = iter.next();
            if (listener != null) {
                listener.onAudioStart();
            }
        }
    }
}