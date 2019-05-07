package com.example.myapplication.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_signature) EditText _signatureText;
    @BindView(R.id.input_number) EditText _numberText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    public static String obgID;

    Handler Handler = new Handler();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                final MyUser users = BmobUser.getCurrentUser(MyUser.class);
                if(users != null) {
                    Intent intent = new Intent(SignupActivity.this, SlidingActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignupActivity.this, "网络较差，页面加载失败，请重新登录", Toast.LENGTH_LONG).show();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _signatureText.getText().toString();
        String number = _numberText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String signature = _signatureText.getText().toString();
        String number = _numberText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("用户名至少为3个字符");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (signature.isEmpty()) {
            _signatureText.setError("个性签名不能为空");
            valid = false;
        } else {
            _signatureText.setError(null);
        }

        if(number.isEmpty()) {
            _numberText.setError("手机号不能为空");
            valid=false;
        }else {
            if(number.length() != 11){
                _numberText.setError("请输入正确的手机号");
                valid=false;
            }
            _numberText.setError(null);
        }


        if (password.isEmpty()) {
            _passwordText.setError("密码不能为空");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (password.length() < 4 || password.length() > 10) {
            _passwordText.setError("密码长度应为4到10个字符");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("两次输入的密码不一致");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }



        final MyUser bu = new MyUser();
        bu.setUsername(name);
        bu.setMobilePhoneNumber(number);
        bu.setPassword(password);
        bu.setSignature(signature);
        bu.setReEnterPassword(reEnterPassword);

        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "注册成功啦~", Toast.LENGTH_SHORT).show();
                    obgID = bu.getObjectId();
                } else {
                    Toast.makeText(SignupActivity.this, "注册失败咯~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return valid;
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