package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class AboutUsActivity extends AppCompatActivity {

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        back = findViewById(R.id.title_back);

        //按下确认键
        initListener();
    }

    //返回键监听
    private void initListener() {
        //对返回按钮的响应，跳转至个人中心页面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
