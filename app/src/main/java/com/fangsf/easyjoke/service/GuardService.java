package com.fangsf.easyjoke.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.fangsf.easyjoke.GuardAidl;

/**
 * 保活的service,
 */
public class GuardService extends Service {


    public GuardService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 和MessageService服务进行绑定
        bindService(new Intent(this, MessageService.class)
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
            // 服务断开后, 再次开启 对方的服务,再次进行绑定
            startService(new Intent(GuardService.this, MessageService.class));
            bindService(new Intent(GuardService.this, MessageService.class)
                    , mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

}
