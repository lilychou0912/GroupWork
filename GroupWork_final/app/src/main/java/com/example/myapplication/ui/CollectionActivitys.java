package com.example.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.example.myapplication.Adapter.MyAdapterC;

import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Bean.MyUser;
import com.example.myapplication.Bean.Collections;
import com.example.myapplication.utils.MyListview;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.myapplication.utils.info_token;

import org.json.JSONException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class CollectionActivitys extends AppCompatActivity {

    WebView webView = new WebView(this);



    private List<Collections> list;
    private MyListview lv;
    private AlertDialog al;

    private String headUrl;
    private BmobUser user;
    private EditText Text;
    private Button btn_collection;
    private MyAdapterC adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_collections);
        intiView();
        intiData();
        btn_collection.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    collectionFromWeb();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intentToUrl(i);
            }
        });
    }
    private void intiView(){
        btn_collection= findViewById(R.id.search_collection);
        Text = findViewById(R.id.edit_collection);

        lv = (MyListview) findViewById(R.id.collections);
        user = BmobUser.getCurrentUser(MyUser.class);

        list = new ArrayList<>();
        adapter = new MyAdapterC(CollectionActivitys.this, list);
        lv.setAdapter(adapter);
    }
    private void intiData() {


        list.clear();
        BmobQuery<Collections> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(20);

        query.findObjects(new FindListener<Collections>() {
            @Override
            public void done(List<Collections> lists, BmobException e) {
                if (e == null) {

                    list = lists;

                    adapter.addCollections(list);

                    adapter.notifyDataSetChanged();
                    al.dismiss();

                }
            }
        });
    }
    private void collectionFromWeb()throws ParseException, IOException, JSONException {
        String textUrl=Text.getText().toString();
        info_token token=new info_token();
        String article_title=token.getTitle(textUrl) ;
        final Collections collections=new Collections();
        collections.setUrl(textUrl);
        collections.setTitle(article_title);
        collections.setUser(BmobUser.getCurrentUser(MyUser.class));
        collections.setTime(getTime());

        collections.save(new SaveListener<String>() {
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




        public String getTime() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            return formatter.format(curDate);

        }
    private void intentToUrl(int i){
        webView.loadUrl(list.get(i).getUrl());


    }
    private void toast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

}
