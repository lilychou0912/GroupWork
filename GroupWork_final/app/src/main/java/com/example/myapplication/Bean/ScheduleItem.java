package com.example.myapplication.Bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

public class ScheduleItem extends BmobObject {
    private Schedule note_pointer;
    private String iname;
    private String istarttime;
    private String iendtime;
    private ArrayList<String> iimgUrl;
    private String idiscription;
    private String icategory;
    private String icolor;

    public Schedule getNote_pointer() {return note_pointer;}

    public void setsNote_pointer(Schedule schedule) {this.note_pointer = schedule; }

    public String getIname(){return iname;}

    public void setIname(String name){this.iname = name;}

    public String getIstarttime() { return istarttime; }

    public void setIstarttime(String starttime) { this.istarttime = starttime; }

    public String getIendtime() { return iendtime; }

    public void setIendtime(String endtime) { this.iendtime = endtime; }

    public ArrayList<String> getIimgUrl() { return iimgUrl; }

    public void setIimgUrl(ArrayList<String>  imgUrl) { this.iimgUrl = imgUrl; }

    public String getIcategory() { return icategory; }

    public void setIcategory(String category) { this.icategory = category; }

    public String getIdiscription() { return idiscription; }

    public void setIdiscription(String discription) { this.idiscription = discription; }

    public String getIcolor() { return icolor; }

    public void setIcolor(String color) { this.icolor = color; }
}
