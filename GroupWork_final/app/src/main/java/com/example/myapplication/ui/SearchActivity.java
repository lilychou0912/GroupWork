package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.myapplication.Adapter.MyAdapter;
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
import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.utils.MyListview;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends AppCompatActivity{
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

        setContentView(R.layout.layout_search);
        intiView();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
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

            lv = (MyListview) findViewById(R.id.lv);
            user = BmobUser.getCurrentUser(MyUser.class);
            scrollView = (GradScrollView) findViewById(R.id.scrollview);
            spaceTopChange = (RelativeLayout) findViewById(R.id.spaceTopChange);
            list = new ArrayList<>();
            adapter = new MyAdapter(SearchActivity.this, list);
            lv.setAdapter(adapter);
        }
    private void intentToArticle(int i){
        Intent intent = new Intent(SearchActivity.this,ArticleActivity.class);


        intent.putExtra("time", list.get(i).getCreatedAt());


        intent.putExtra("obj", list.get(i).getObjectId());
        //  intent.putExtra("urlList", (Parcelable) list.get(i).getHeadImgUrl());

        startActivity(intent);

    }



    private void search() {
        target = SearchText.getText().toString();

        list.clear();
        BmobQuery<Schedule> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(20);
        query.addWhereEqualTo("tag",target);
        query.findObjects(new FindListener<Schedule>() {
            @Override
            public void done(List<Schedule> lists, BmobException e) {
                if (e == null) {

                    list = lists;

                    adapter.addSchedule(list);

                    adapter.notifyDataSetChanged();
                    al.dismiss();

                }
            }
        });






    }
}

