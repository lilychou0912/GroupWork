package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.R;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ShareActivity extends Activity {
    private EditText mTag;
    private TextView mTitle;
    private TextView mDuration;
    private TextView mCancel;
    private TextView mOk;
    private String ssId;
    private String longPicPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mTag = findViewById(R.id.sa_edit);
        mTitle = findViewById(R.id.sa_title);
        mDuration = findViewById(R.id.sa_duration);
        mCancel = findViewById(R.id.sa_cancel);
        mOk = findViewById(R.id.sa_ok);

        Intent intent = getIntent();
        ssId = intent.getStringExtra("sId_extra");
        longPicPath = intent.getStringExtra("longPicPath_extra");
        initData();
        bottomListener();
        // 添加动画
        //pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //saveBitmap(getRelativeLayoutBitmap(pictureView));
    }

    //初始化行程事件
    private void initData() {
        BmobQuery<Schedule> query = new BmobQuery<Schedule>();
        query.getObject(ssId, new QueryListener<Schedule>() {
            @Override
            public void done(Schedule schedule, BmobException e) {
                mTitle.setText(schedule.getName());
                mDuration.setText(schedule.getstarttime()+"~"+schedule.getendtime());
            }
        });
    }

    private void bottomListener(){
        //给SaveBtn绑定监听事件
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给SaveBtn添加点击响应事件
               finish();

            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Schedule> query = new BmobQuery<Schedule>();
                query.getObject(ssId, new QueryListener<Schedule>() {
                    @Override
                    public void done(Schedule schedule, BmobException e) {
                        // 给SaveBtn添加点击响应事件
                        final BmobFile bmobFile = new BmobFile(new File(longPicPath));
                        //Bmob这个上传文件的貌似不成功..........................
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //得到上传的图片地址
                                    schedule.setImgUrl(bmobFile.getFileUrl());
                                    schedule.setTag(mTag.getText().toString());
                                    //更新图片地址
                                    schedule.update(ssId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(ShareActivity.this, "分享成功啦~", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
