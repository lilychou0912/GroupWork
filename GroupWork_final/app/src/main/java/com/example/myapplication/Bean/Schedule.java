package com.example.myapplication.Bean;

import com.example.myapplication.Bean.MyUser;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Schedule extends BmobObject {
    private MyUser author;
    private String name;
    private String starttime;
    private String endtime;
    private String imgUrl;
    private boolean isfinished = false;
    private String title,tag;

    public MyUser getAuthor(){
        return author;
    }

    public void setAuthor(MyUser user){
        this.author = user;
    }
    public String getTag() { return tag; }

    public void setTag(String tag) {
        this.tag= tag;
    }
    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getstarttime() { return starttime; }

    public void setstarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getendtime() {
        return endtime;
    }

    public void setendtime(String endtime) {
        this.endtime = endtime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getIsfinished(){
        return isfinished;
    }

    public void setIsfinished(boolean isfinished){
        this.isfinished = isfinished;
    }
}
