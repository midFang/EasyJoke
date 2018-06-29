package com.fangsf.easyjoke;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.baselibrary.ioc.OnClick;
import com.example.baselibrary.ioc.ViewById;
import com.example.baselibrary.ioc.ViewUtils;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.button)
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.bind(this);

        ButterKnife.bind(this);

        initaPatch(); //阿里 热修复

        mButton.setText("viewbyid");

        // int i = 2 / 0;
    }

    @OnClick(R.id.button)
    public void testClick(View view) {
        Toast.makeText(this, "viewByid", Toast.LENGTH_SHORT).show();
    }

    private void initaPatch() {
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
    }

}
