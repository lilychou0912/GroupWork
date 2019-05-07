package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @BindView(R.id.input_number) EditText _numberText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    private String number;
    private String password;

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();

        LoginListener();

        RegisterListener();
    }

    //对登录按钮的响应
    private void LoginListener(){
        //对"登录按钮的响应，跳转至待办页面
        _loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final MyUser user = new MyUser();
                user.setUsername(_numberText.getText().toString());
                user.setPassword(_passwordText.getText().toString());

                user.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser bmobUser, BmobException e) {
                        if (e == null) {
                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
                            Toast.makeText(LoginActivity.this, "登录成功~", Toast.LENGTH_SHORT).show();
                            //editor.putBoolean("isLogin", true);
                            //editor.apply();
                            Intent intent = new Intent(LoginActivity.this, SlidingActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });
    }

    private void RegisterListener() {

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private  void init(){
        _loginButton = (Button)findViewById(R.id.btn_login);
        _numberText = (EditText)findViewById(R.id.input_number);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        pref = getSharedPreferences("LoginData", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        number = pref.getString("number", "");
        password = pref.getString("password", "");

        if (!number.equals("")) {
            _numberText.setText(number);
            _passwordText.setText(password);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        number = pref.getString("number", "");
        password = pref.getString("password", "");
        if (!number.equals("")) {
            _numberText.setText(number);
            _passwordText.setText(password);
        }
    }

    //设置返回按钮：不应该退出程序---而是返回桌面
    //复写onKeyDown事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}













