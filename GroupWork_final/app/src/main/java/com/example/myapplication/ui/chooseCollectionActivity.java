package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

public class chooseCollectionActivity extends AppCompatActivity{
    private Button collection_1,collection_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose);
        collection_1=findViewById(R.id.collection_1);
        collection_2=findViewById(R.id.collection_2);
        collection_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToCollection();
            }
        });
        collection_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToCollections();
            }
        });



    }
    private void intentToCollection(){
        Intent intent = new Intent(chooseCollectionActivity.this,CollectionActivity.class);
        //启动
        startActivity(intent);

    }
    private void intentToCollections(){
        Intent intent = new Intent(chooseCollectionActivity.this,CollectionActivitys.class);
        //启动
        startActivity(intent);

    }
}
