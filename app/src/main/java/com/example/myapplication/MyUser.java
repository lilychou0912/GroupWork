package com.example.myapplication;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String signature;
    private String reEnterPassword;


    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature= signature;
    }
    public String getReEnterPassword() {return this.reEnterPassword;}
    public void setReEnterPassword(String reEnterPassword) {this.reEnterPassword = reEnterPassword;}


}
