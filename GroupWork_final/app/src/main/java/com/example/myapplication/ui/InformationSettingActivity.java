package com.example.myapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class InformationSettingActivity extends Activity {

    private Button back;
    private TextView username;
    private TextView signature;
    private TextView sexuality;
    private TextView age;
    private TextView location;

    private String name;
    private String sign;
    private String sex;
    private String year;
    private String city;

    public static String obgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_setting);
        init();
        //按下返回加确认键
        backListener();
        //Bmob初始化
        Bmob.initialize(this, "1768baa1a8670ebb020369f5672e443f" );
    }

    private void init() {
        username = (EditText) findViewById(R.id.in_name);
        signature = (EditText) findViewById(R.id.in_sign);
        sexuality = (EditText) findViewById(R.id.in_sex);
        age = (EditText) findViewById(R.id.in_age);
        location = (EditText) findViewById(R.id.in_city);

        final MyUser user =  BmobUser.getCurrentUser(MyUser.class);
        name = user.getUsername();
        sign = user.getSignature();
        sex = user.getSex();
        year = user.getAge();
        city = user.getLocation();

        if (!name.equals("")) {
            username.setText(name);
            signature.setText(sign);
            sexuality.setText(sex);
            age.setText(year);
            location.setText(city);

        } else {
            username.setText("未登录");
            signature.setText("");
            sexuality.setText("");
            age.setText("");
            location.setText("");
        }
    }

    //返回键监听
    private void backListener(){
        back = (Button)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                name = username.getText().toString();
                sign = signature.getText().toString();
                sex = sexuality.getText().toString();
                year = age.getText().toString();
                city = location.getText().toString();

                final MyUser infor =  BmobUser.getCurrentUser(MyUser.class);
                infor.setUsername(name);
                infor.setSignature(sign);
                infor.setSex(sex);
                infor.setAge(year);
                infor.setLocation(city);

                infor.update(infor.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(InformationSettingActivity.this, "修改成功咯", Toast.LENGTH_SHORT).show();
                            obgID = infor.getObjectId();
                        } else {
                            Toast.makeText(InformationSettingActivity.this, "修改失败咯", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //返回
                finish();
            }
        });
    }

}
