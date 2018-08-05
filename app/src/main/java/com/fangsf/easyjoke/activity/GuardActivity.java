package com.fangsf.easyjoke.activity;

import android.content.Intent;
import android.os.Build;

import com.example.framelibrary.base.BaseSkinActivity;
import com.fangsf.easyjoke.R;
import com.fangsf.easyjoke.service.GuardService;
import com.fangsf.easyjoke.service.JobWakeUpService;
import com.fangsf.easyjoke.service.MessageService;

/**
 * 保活activity 实例
 */
public class GuardActivity extends BaseSkinActivity {


    @Override
    protected int bindLayout() {
        return R.layout.activity_change_skin;
    }

    @Override
    protected void init() {
        startService(new Intent(this, MessageService.class));
        startService(new Intent(this, GuardService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startService(new Intent(this, JobWakeUpService.class));
        }
    }
}
