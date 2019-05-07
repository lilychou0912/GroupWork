package com.example.myapplication.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


public class AccountSettingActivity extends AppCompatActivity {

    private TextView quit;
    private Button back;



    private String phone;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__setting);

        init();
        initListener();
        //初始化Bmob
        Bmob.initialize(this, "1768baa1a8670ebb020369f5672e443f" );
    }

    private void initListener() {
        //对返回按钮的响应，跳转至个人中心页面
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });


        //对"退出登录"的响应，跳转至登录页面
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BmobUser.logOut();
                Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private  void init(){
        back = (Button) findViewById(R.id.regulate_title_back);
        quit = (TextView) findViewById(R.id.txt_quit);
    }


}