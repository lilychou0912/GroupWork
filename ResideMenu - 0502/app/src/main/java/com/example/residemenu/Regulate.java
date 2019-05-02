package com.example.residemenu;

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



public class Regulate extends AppCompatActivity {

    private TextView quit;
    private TextView delete;
    private Button back;
    private EditText call;
    private Button submit;

    private String phone;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String[] data ={"用户名","密码","手机号","签名"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulate);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                Regulate.this,android.R.layout.simple_list_item_1,data
        );
        ListView listView=(ListView) findViewById(R.id.regulate_list_view);
        listView.setAdapter(adapter);

        init();
        initListener();
        //初始化Bmob
        Bmob.initialize(this, "75c88783b55ef9650c609d100ef036b4" );
    }

    private void initListener() {
        //对返回按钮的响应，跳转至个人中心页面
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        //对√的响应
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = call.getText().toString();
                if (!phone.equals("")){
                    editor.putString("cellphone", phone);
                    editor.apply();
                    Toast.makeText(Regulate.this, "修改成功~", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Regulate.this, "不能为空~", Toast.LENGTH_SHORT).show();
                }
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
                us.setObjectId(RegisterActivity.obgID);
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

        submit = (Button) findViewById(R.id.regulate_title_submit);
        back = (Button) findViewById(R.id.regulate_title_back);
        quit = (TextView) findViewById(R.id.txt_quit);
        delete = (TextView) findViewById(R.id.txt_delete);

    }


}
