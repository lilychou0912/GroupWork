package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;


public class LongPicture extends MainActivity{

    private RecyclerView pRecyclerView;
    private PictureAdapter pAdapter;
    private List<ScheduleItem> sItem;
    private DBhelper dBhelper = new DBhelper();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long_picture);
        pRecyclerView = findViewById(R.id.pic_recyclerview);
        sItem = dBhelper.getScheduleItme(1);
        initRecycle();
        }
    private void initRecycle() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        pAdapter = new PictureAdapter(this, sItem);
        pRecyclerView.setAdapter(pAdapter);
        pRecyclerView.setHasFixedSize(true);

    }

}

