package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class LongPicture extends MainActivity{

    private RecyclerView pRecyclerView;
    private PictureAdapter pAdapter;
    private List<ScheduleItem> sItem;
    private DBhelper dBhelper = new DBhelper();

    /*************************************************************/
    private Context mContext;
    private GridView gridView;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    /*************************************************************/


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long_picture);
        pRecyclerView = findViewById(R.id.pic_recyclerview);
        sItem = dBhelper.getScheduleItme(1);
        initRecycle();
    }

    //初始化item
    private void initRecycle() {
        initGridView(sItem.getIimgUrl());
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        pAdapter = new PictureAdapter(this, sItem);
        pRecyclerView.setAdapter(pAdapter);
        pRecyclerView.setHasFixedSize(true);

    }

    //初始化item里的图片组件
    private void initGridView (String[] pictures) {
        View contentView = LayoutInflater.from(LongPicture.this).inflate(R.layout.picture_item, null);
        GridView imgGridVeiw = (GridView) findViewById(R.id.gridView);
        //在这里判断图片的数量，根据图片的数量改变GridView的列数，图片的大小要在Adapter里设置才有效
        if (pictures.length>=3) imgGridVeiw.setNumColumns(3);
        if (pictures.length==1) imgGridVeiw.setNumColumns(1);
        //绑定自定义的adapter
        PictureComponentAdapter imgaAdapter = new PictureComponentAdapter(this,pictures);
        imgGridVeiw.setAdapter(imgaAdapter);
    }
}

