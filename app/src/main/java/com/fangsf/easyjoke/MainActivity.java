package com.fangsf.easyjoke;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.example.baselibrary.commonDialog.BaseAlertDialog;
import com.example.baselibrary.fixbug.FixBugManager;
import com.example.baselibrary.ioc.OnClick;
import com.example.baselibrary.ioc.ViewById;
import com.example.baselibrary.ioc.ViewUtils;
import com.example.framelibrary.db.DaoSupport;
import com.example.framelibrary.db.DaoSupportFactory;
import com.example.framelibrary.db.IDaoSupport;
import com.fangsf.easyjoke.bean.Person;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewById(R.id.button)
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.bind(this);

        // initaPatch(); //阿里 热修复

        mButton.setText("viewbyid");

        //  customFixBug();

        // int i = 2 / 0;
        mButton.setOnClickListener(this);

        IDaoSupport daoSupport = DaoSupportFactory.getFactory().getDao(Person.class);
        daoSupport.insert(new Person("22", 71));

    }

    private void customFixBug() {

        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");

        if (fixFile.exists()) {
            // 存在 加载差分包
            try {
                FixBugManager fixBugManager = new FixBugManager(MainActivity.this);
                fixBugManager.fixBug(fixFile.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    @OnClick(R.id.button)
    public void testClick(View view) {
        // int i = 2 / 0;
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

    @Override
    public void onClick(View v) {
        int i = 2 / 0;
        Toast.makeText(this, "bug修复测试", Toast.LENGTH_SHORT).show();
    }
}
