package com.example.residemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu_Setting extends AppCompatActivity {

    private Button back;
    private TextView username;
    private TextView signature;

    private String name;
    private String sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__setting);
    }

    init();

    //按下确认键
    backListener();

}
