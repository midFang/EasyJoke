package com.fangsf.easyjoke.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.fangsf.easyjoke.GuardAidl;

/**
 *  接受消失的service
 */
public class MessageService extends Service {
    private static final String TAG = "MessageService";

    private final int MessageServiceId = 1;

    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.i(TAG, "onStartCommand:  等待接受消息");
                    SystemClock.sleep(2060);
                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提高进程的优先级
        startForeground(MessageServiceId, new Notification());


        // 和MessageService服务进行绑定
        bindService(new Intent(this, GuardService.class)
                , mServiceConnection, Context.BIND_IMPORTANT);


        return START_STICKY; //服务被停止后, 会再次尝试重新启动
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GuardAidl.Stub() {
        };
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 服务连接
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 服务断开后, 再次开启 对方的服务
            startService(new Intent(MessageService.this, GuardService.class));
            bindService(new Intent(MessageService.this, GuardService.class)
                    , mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

}
