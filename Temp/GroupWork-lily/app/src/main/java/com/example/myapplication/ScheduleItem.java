package com.example.myapplication;

import android.graphics.Color;

import com.guojunustb.library.WeekViewEvent;

import org.litepal.crud.LitePalSupport;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class ScheduleItem extends LitePalSupport {
    private int sId;
    private String iname;
    private String istarttime;
    private String iendtime;
    private String iimgUrl;
    private String idiscription;
    private String icategory;
    private String icolor;

    public int getsId() {return sId;}

    public void setsId(int id) {this.sId = id; }

    public String getIname(){return iname;}

    public void setIname(String name){this.iname = name;}

    public String getIstarttime() { return istarttime; }

    public void setIstarttime(String starttime) { this.istarttime = starttime; }

    public String getIendtime() { return iendtime; }

    public void setIendtime(String endtime) { this.iendtime = endtime; }

    public String getIimgUrl() { return iimgUrl; }

    public void setIimgUrl(String imgUrl) { this.iimgUrl = imgUrl; }

    public String getIcategory() { return icategory; }

    public void setIcategory(String category) { this.icategory = category; }

    public String getIdiscription() { return idiscription; }

    public void setIdiscription(String discription) { this.idiscription = discription; }

    public String getIcolor() { return icolor; }

    public void setIcolor(String color) { this.icolor = color; }
}
