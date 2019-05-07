package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class SplashActivity extends Activity {


    /**
     * 闪屏页
     */

    private LinearLayout rlSplash;
    Handler mHandler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rlSplash = (LinearLayout) findViewById(R.id.rl_splash);
        startAnim();
        Bmob.initialize(this, "1768baa1a8670ebb020369f5672e443f" );

        if (BmobUser.isLogin()) {
            final MyUser userInfo =  BmobUser.getCurrentUser(MyUser.class);
            // 允许用户使用应用
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, SlidingActivity.class));
                }
            }, 2000);
            Toast.makeText(SplashActivity.this, "检测到用户信息，自动登陆中……", Toast.LENGTH_LONG).show();
        }
        else {
            //缓存用户对象为空时， 可打开用户登录界面…
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }, 2000);
        }

    }

    /**
     * 启动动画
     */
    private void startAnim() {
        // 渐变动画,从完全透明到完全不透明
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        // 持续时间 2 秒
        alpha.setDuration(2000);
        // 动画结束后，保持动画状态
        alpha.setFillAfter(true);
        // 设置动画监听器
        alpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画结束时回调此方法
            @Override
            public void onAnimationEnd(Animation animation) {

             }
         });
        // 启动动画
        rlSplash.startAnimation(alpha);
     }

}


