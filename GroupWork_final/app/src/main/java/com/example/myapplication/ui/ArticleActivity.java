package com.example.myapplication.ui;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.Bean.Schedule;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Bean.Collection;

import java.text.SimpleDateFormat;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ArticleActivity extends AppCompatActivity{
    private MyUser user;
    private String obj;
    private ImageView collection,article;
    private Schedule post = new Schedule();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article);
        init();
        initListener();

    }
    void init() {
        collection=findViewById(R.id.toCollection);
        article=findViewById(R.id.article);
        obj = getIntent().getStringExtra("obj");


        post.setObjectId(obj);


        user = BmobUser.getCurrentUser(MyUser.class);
        //user=BmobUser.getCurrentUser(User.class);
        String path= post.getImgUrl();
        Bitmap bm = BitmapFactory.decodeFile(path);
        article.setImageBitmap(bm);



    }
    void initListener(){
        collection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                final Collection collection=new Collection();
                collection.setPost(post);
                collection.setUser(user);
                collection.setTime(getTime());

                collection.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {


                            toast("收藏成功");

                        } else {
                            toast(e.toString());

                        }
                    }
                });



            }
        });
    }
    private void toast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh点");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }


}
