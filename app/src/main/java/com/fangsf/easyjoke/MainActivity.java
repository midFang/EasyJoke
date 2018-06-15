package com.fangsf.easyjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.fangsf.easyjoke.bean.GankIoBean;
import com.fangsf.easyjoke.net.HttpCallBack;
import com.fangsf.easyjoke.net.HttpUtils;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tvMes);

        HttpUtils.with(this).url("http://gank.io/api/history/content/2/1")
                .get().execute(new HttpCallBack<GankIoBean>() {

            public void onSuccess(GankIoBean o) {
                mTextView.setText("" + o.getResults().get(0).getContent());
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
