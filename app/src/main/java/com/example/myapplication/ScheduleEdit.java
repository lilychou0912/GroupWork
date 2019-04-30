package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptC;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guojunustb.library.MonthLoader;
import com.guojunustb.library.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.NoteEditActivity.Send;
import static com.example.myapplication.NoteEditActivity.Sstart;

public class ScheduleEdit extends Activity {
    private View mView;
    private ListPopupWindow listPopupWindow;
    private EditText tagText, mText;
    private TextView ok, cancel, start, end;
    private String oldname;
    private String Sstarttime, Sendtime;
    private String oldStartTime, oldEndTime;
    private String oldCategory;
    private String oldDiscription;
    private String oldColor;
    private String category;
    private String color = "#ffffff";
    private int choice;
    private int flag = 1;
    private String discription;
    private DBhelper dBhelper = new DBhelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule);
        /*日程类型*/
        tagText = (EditText) findViewById(R.id.tag_edit);
        /*输入的文字*/
        mText = (EditText) findViewById(R.id.text_edit);
        ok = (TextView) findViewById(R.id.se_ok);
        cancel = (TextView) findViewById(R.id.se_cancel);
        start = (TextView) findViewById(R.id.start_time_picker);
        end = (TextView) findViewById(R.id.end_time_picker);

        //初始化（更改情况）
        initData();
        //按下确认键
        okListener();
        //按下取消键
        cancelListener();


        tagText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //对edit 进行焦点监听
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showListPopulWindow(); //调用显示PopuWindow 函数
                }
            }
        });
    }

    private void showListPopulWindow() {
        final String[] list = {"观光", "购物", "饮食", "交通", "住宿"};//要填充的数据
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(tagText);//以哪个控件为基准，在该处以tagText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tagText.setText(list[i]);//把选择的选项内容展示在EditText上
                choice = i;
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    //确认键监听
    private void okListener() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的信息
                getInput();
                //根据类型填充颜色
                if(choice == 0){
                    color = "#59dbe0";
                }
                else if(choice == 1){
                    color = "#f18965";
                }
                else if(choice == 2){
                    color = "#9cc17b";
                }
                else if(choice == 3){
                    color = "#ffc24f";
                }
                else if(choice == 4){
                    color = "#bb9af0";
                }
                else {
                    color = "#ffe47a";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //新建行程事件
                if (!discription.equals("") && category != null && Sstart!=null && Send!=null){
                    if(oldname == null){
                        String s = sdf.format(Sstart.getTime());
                        String e = sdf.format(Send.getTime());
                        if(dBhelper.addScheduleItem(getEventTitle(Sstart), category, s, e, discription, null, color) != null){
                            flag = 1;
                            finish();
                        }
                    }else {
                        if (dBhelper.updateScheduleItme(oldname, category, start.getText().toString(), end.getText().toString(), discription, null, oldColor) != null) {
                            flag = -1;
                            finish();
                        }
                    }

                }
            }
        });
    }

    //返回键监听
    private void cancelListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除为未创建成功的安排
                if(flag == 1) {
                    dBhelper.deleteScheduleItme(getEventTitle(Sstart));
                }
                //返回日程日历界面
                finish();
            }
        });
    }

    //获取用户输入内容
    private void getInput(){
        get_category();
        get_discription();
        get_startTime();
        get_endTime();
    }

    //获取事件类型
    private void get_category(){
        category = tagText.getText().toString();
    }
    //获取事件描述
    private void get_discription(){
        discription = mText.getText().toString();
    }
    //获取事件开始时间
    private void get_startTime(){ Sstarttime = start.getText().toString(); }
    //获取事件结束时间
    private void get_endTime() {
        Sendtime = end.getText().toString();
    }

    //设置初始数据
    private void initData() {
        Intent intent = getIntent();
        oldStartTime = intent.getStringExtra("startTime_extra");
        oldEndTime = intent.getStringExtra("endTime_extra");
        oldCategory  = intent.getStringExtra("category_extra");
        oldDiscription = intent.getStringExtra("discription_extra");
        oldname = intent.getStringExtra("name_extra");
        oldColor = intent.getStringExtra("color_extra");

        if(oldStartTime != null){
            try {
                tagText.setText(oldCategory);
                mText.setText(oldDiscription);
                start.setText(oldStartTime);
                end.setText(oldEndTime);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    //为每个行程事件生成名字
    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }
}

