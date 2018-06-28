package com.fangsf.easyjoke;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibrary.exception.CrashAppHandler;
import com.example.baselibrary.net.HttpUtils;
import com.example.framelibrary.net.DialogCallBack;
import com.fangsf.easyjoke.bean.GankIoBean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.tvMes);

        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");

        if (fixFile.exists()) {
            // 存在 加载差分包
            try {
                App.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        // int i = 2 / 0;
//
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 2 / 2;
                Toast.makeText(MainActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });



    }

}
