package com.example.myapplication.Bean;


import cn.bmob.v3.BmobObject;

public class Collection  extends BmobObject {
    private Schedule post;
    private MyUser user;
    private String time;

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

    public Schedule getPost() {
        return post;
    }

    public void setPost(Schedule post) {
        this.post = post;
    }

}
