package com.example.myapplication.Bean;


import cn.bmob.v3.BmobObject;

public class Collections  extends BmobObject {
    private  String url;
    private MyUser user;
    private String time;
    private String title;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }


}
