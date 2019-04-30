package com.sourcey.materiallogindemo;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String signature;
    private String reEnterPassword;
    private String email;


    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature= signature;
    }
    public String getReEnterPassword() {return this.reEnterPassword;}
    public void setReEnterPassword(String reEnterPassword) {this.reEnterPassword = reEnterPassword;}
    public String getEmail() {return this.email;}
    public void setEmail() {this.email = email;}


}
