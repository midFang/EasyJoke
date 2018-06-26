package com.fangsf.easyjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibrary.net.HttpUtils;
import com.example.framelibrary.net.DialogCallBack;
import com.fangsf.easyjoke.bean.GankIoBean;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tvMes);

        int i = 2 / 0;

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 2 / 0;
                Toast.makeText(MainActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
