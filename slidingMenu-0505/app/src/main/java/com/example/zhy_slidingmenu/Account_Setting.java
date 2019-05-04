package com.example.zhy_slidingmenu;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;



public class Account_Setting extends AppCompatActivity {

    private TextView quit;
    private TextView delete;
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
                editor.putBoolean("isLogin", false);
                editor.apply();

                Intent intent = new Intent(Regulate.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //对"删除账户"的响应，跳转至登录页面
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyUser us = new MyUser();
                us.setObjectId(LoginActivity.obgID);
                us.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                editor.clear();
                editor.putBoolean("isLogin", false);
                editor.apply();

                Intent intent = new Intent(Regulate.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private  void init(){
        pref = getSharedPreferences("LoginData", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        back = (Button) findViewById(R.id.regulate_title_back);
        quit = (TextView) findViewById(R.id.txt_quit);
        delete = (TextView) findViewById(R.id.txt_delete);

    }


}