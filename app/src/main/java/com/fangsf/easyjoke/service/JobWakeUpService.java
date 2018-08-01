package com.fangsf.easyjoke.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

/**
 * Created by fangsf on 2018/7/29.
 * Useful:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private static final String TAG = "JobWakeUpService";
    private final int JobWakeUp = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 开启一个任务,定时轮询检查服务有没有被停止, 停止了就重启服务  (在android7.0 以上就用不了了 )
//        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(JOB_SCHEDULER_SERVICE);
//        JobInfo.Builder jobInfo = new JobInfo.Builder(JobWakeUp,
//                new ComponentName(this, JobWakeUpService.class));
//
//        jobInfo.setPeriodic(5000);  //每 5秒轮询一次
//
//        jobScheduler.schedule(jobInfo.build());


        // Android 7.0 以上的方式, 直接继承service 就可以了
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long triggerAtTime = SystemClock.elapsedRealtime() + 2000; // 2秒后执行

        checkServiceAlive();

        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                new Intent(this, JobWakeUpService.class), 0);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);


        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        // 判断服务是否被停止了
        checkServiceAlive();


        return false;
    }

    private void checkServiceAlive() {
        boolean serviceAlive = getServiceAlive(MessageService.class.getName());
        Log.i(TAG, "onStartJob: 111 " + serviceAlive);
        if (!serviceAlive) {
            startService(new Intent(this, MessageService.class));
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断某个服务是否在运行
     *
     * @param serviceName
     * @return
     */
    private boolean getServiceAlive(String serviceName) {
        boolean isWork = false;

        ActivityManager myAM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = myAM.getRunningServices(100);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            String mName = runningServices.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }

        return isWork;
    }

}
