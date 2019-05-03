package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.*;


public class LoginActivity extends AppCompatActivity {

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
            public void onClick(View v){

                String user = _numberText.getText().toString();
                String passw = _passwordText.getText().toString();

                MyUser users = BmobUser.getCurrentUser(MyUser.class);

                if (users.getMobilePhoneNumber().equals(user) && users.getReEnterPassword().equals(passw)) {
                    Toast.makeText(LoginActivity.this, "登录成功~", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("isLogin", true);
                    editor.apply();


                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "登录名或密码有误哦~", Toast.LENGTH_SHORT).show();
                }
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
}













