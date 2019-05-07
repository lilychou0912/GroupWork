package com.example.myapplication.Bean;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String signature;
    private String reEnterPassword;
    private String sex;
    private String age;
    private String location;
    private String favorite;
    private String image;


    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature= signature;
    }

    public String getReEnterPassword() {return this.reEnterPassword;}
    public void setReEnterPassword(String reEnterPassword) {this.reEnterPassword = reEnterPassword;}

    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex= sex;
    }

    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age= age;
    }

    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location= location;
    }

    public String getFavorite() {
        return this.favorite;
    }
    public void setFavorite(String favorite) {
        this.favorite= favorite;
    }

    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image= image;
    }


}
