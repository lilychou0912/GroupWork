package com.example.myapplication;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Schedule extends LitePalSupport {
    private int sId = 0;
    private String name;
    private String starttime;
    private String endtime;
    private String imgUrl;
    private boolean isfinished = false;

    public int getSId(){return sId;}

    public void setSId(int id) {this.sId = id; }

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
