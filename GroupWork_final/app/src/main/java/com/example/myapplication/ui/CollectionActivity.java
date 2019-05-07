package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapter.MyAdapter;
import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.utils.GradScrollView;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.utils.MyListview;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class CollectionActivity extends AppCompatActivity{
    private ImageView backGroundImg;
    private GradScrollView scrollView;
    private RelativeLayout spaceTopChange;
    private int height;
    private List<Schedule> list;
    private MyListview lv;
    private SwipeRefreshLayout refresh;
    private AlertDialog al;

    private String headUrl = "";

    private BmobUser user;
    private EditText SearchText;
    private Button btn_search;
    private String target;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_collection);
        intiView();
        intiData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intentToArticle(i);
            }
        });
    }
    private void intiView(){
        btn_search = findViewById(R.id.search);
        SearchText = findViewById(R.id.edit_search);

        lv = (MyListview) findViewById(R.id.collection_lv);
        user = BmobUser.getCurrentUser(MyUser.class);
        scrollView = (GradScrollView) findViewById(R.id.scrollview);
        spaceTopChange = (RelativeLayout) findViewById(R.id.spaceTopChange);
        list = new ArrayList<>();
        adapter = new MyAdapter(CollectionActivity.this, list);
        lv.setAdapter(adapter);
    }
    private void intentToArticle(int i){
        Intent intent = new Intent(CollectionActivity.this,ArticleActivity.class);

        intent.putExtra("time", list.get(i).getCreatedAt());




        //如果帖子没有图片就做处理 传入空

        intent.putExtra("obj", list.get(i).getObjectId());
        //  intent.putExtra("urlList", (Parcelable) list.get(i).getHeadImgUrl());

        startActivity(intent);

    }
    private void intiData() {


        list.clear();
        String bql = "select include Schedule,* from Collection where user= pointer('User', user.getObjectId())";
        new BmobQuery<Schedule>().doSQLQuery(bql, new SQLQueryListener<Schedule>() {

            @Override
            public void done(BmobQueryResult<Schedule>result, BmobException e) {
                if (e == null) {
                    List<Schedule> lists = (List<Schedule>) result.getResults();
                    if (lists != null && lists.size() > 0) {
                        list = lists;

                        adapter.addSchedule(list);

                        adapter.notifyDataSetChanged();
                        al.dismiss();

                    } else {
                        Log.i("smile", "查询成功，无数据返回");
                    }
                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });

    }



}















