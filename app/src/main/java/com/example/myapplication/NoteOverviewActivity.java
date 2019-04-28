package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteOverviewActivity extends Activity implements View.OnClickListener,DatePicker.OnDateChangedListener{
    private PopupWindow mPopWindow;
    private PopupWindow DatePopWindow;
    private LinearLayout llDate;
    private EditText nameText;
    private TextView StvDate, EtvDate;
    private int Syear, Smonth, Sday, Eyear, Emonth, Eday;
    private StringBuffer Sdate, Edate;
    private int flag;
    private RecyclerView mRecyclerView;
    private List<Schedule> Sche;
    private ScheduleAdapter myAdapter;
    private DBhelper dBhelper = new DBhelper();
    private String name;
    private String starttime;
    private String endtime;
    private String oldname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_overview);
        ImageButton MapBtn = (ImageButton) findViewById(R.id.button_map);
        ImageButton SearchBtn = (ImageButton) findViewById(R.id.button_search);
        ImageButton CollectBtn = (ImageButton) findViewById(R.id.button_collect);
        FloatingActionButton AddNoteBtn = (FloatingActionButton) findViewById(R.id.add_note);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //初始化界面，载入已建立行程
        initSchedule();
        //初始化RecyclerView适配器
        initRecycle();
        //初始化日历时间为当前日期
        initDateTime();

        //对“地图”按钮的响应
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt添加点击响应事件
                Intent intent =new Intent(NoteOverviewActivity.this,MainActivity.class);
                //启动
                startActivity(intent);
            }
        });
        //对“搜索”按钮的响应
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt添加点击响应事件
                Intent intent =new Intent(NoteOverviewActivity.this,SearchActivity.class);
                //启动
                startActivity(intent);
            }
        });
        //对“收藏”按钮的响应
        CollectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给bnt添加点击响应事件
                Intent intent =new Intent(NoteOverviewActivity.this,CollectActivity.class);
                //启动
                startActivity(intent);
            }
        });
        //新建行程事件
        AddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }
     private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(NoteOverviewActivity.this).inflate(R.layout.popwindow_select_time, null);
        mPopWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(1f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        //设置各个控件的点击响应
        TextView ok = (TextView)contentView.findViewById(R.id.time_pick_ok);
        TextView cancel = (TextView)contentView.findViewById(R.id.time_pick_cancel);
        TextView start = (TextView)contentView.findViewById(R.id.start_date_picker);
        StvDate = (TextView)contentView.findViewById(R.id.start_date_picker);
        EtvDate = (TextView)contentView.findViewById(R.id.end_date_picker);
        nameText = contentView.findViewById(R.id.title_edit);
        TextView end = (TextView)contentView.findViewById(R.id.end_date_picker);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(NoteOverviewActivity.this).inflate(R.layout.note_overview, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        Syear = Eyear = calendar.get(Calendar.YEAR);
        Smonth = Emonth = calendar.get(Calendar.MONTH) + 1;
        Sday = Eday = calendar.get(Calendar.DAY_OF_MONTH);
        Sdate = new StringBuffer(String.valueOf(Syear));
        Sdate.append("年").append(String.valueOf(Smonth)).append("月").append(Sday).append("日");
        Edate = new StringBuffer(String.valueOf(Eyear));
        Edate.append("年").append(String.valueOf(Emonth)).append("月").append(Eday).append("日");
    }
    /**
     * 日期改变的监听事件
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.Syear = year;
        this.Smonth = monthOfYear+1;
        this.Sday = dayOfMonth;
        this.Eyear = year;
        this.Emonth = monthOfYear+1;
        this.Eday = dayOfMonth;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.time_pick_ok: {
                getInput();
                //添加行程
                if (!name.equals("") && starttime != null && endtime != null) {
                    if (oldname == null) {
                        if (dBhelper.addSchedule(name, starttime,endtime) != null) {
                            Sche.clear();
                            Sche.addAll(dBhelper.getSchedule());
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        if (dBhelper.updateSchedule(oldname, name, starttime,endtime)) {
                            Sche.clear();
                            Sche.addAll(dBhelper.getSchedule());
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                }
                mPopWindow.dismiss();
            }
            break;
            case R.id.time_pick_cancel: {
                Sche.clear();
                Sche.addAll(dBhelper.getSchedule());
                myAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
            break;
            case R.id.start_date_picker: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Sdate.length() > 0) { //清除上次记录的日期
                            Sdate.delete(0, Sdate.length());
                        }
                        StvDate.setText(Sdate.append(String.valueOf(Syear)).append("-").append(String.valueOf(Smonth)).append("-").append(Sday));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置行程开始日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(Syear, Smonth - 1, Sday, this);
            }
            break;
            case R.id.end_date_picker: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Edate.length() > 0) { //清除上次记录的日期
                            Edate.delete(0, Edate.length());
                        }
                        EtvDate.setText(Edate.append(String.valueOf(Eyear)).append("-").append(String.valueOf(Emonth)).append("-").append(Eday));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置行程结束日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(Eyear, Emonth - 1, Eday, this);
            }
            break;
        }
    }
    //获取用户输入内容
    private void getInput(){
        get_name();
        get_starttime();
        get_endtime();
    }

    //获取行程名称
    private void get_name(){
        name = nameText.getText().toString();
    }
    //获取行程开始时间
    private void get_starttime(){ starttime = StvDate.getText().toString();}
    //获取行程截止时间
    private void get_endtime(){
        endtime = EtvDate.getText().toString();
    }
    /**
     * 设置背景透明度
     * @param f
     */
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    private void initRecycle() {
        //纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 获取数据，向适配器传数据，绑定适配器
        myAdapter = new ScheduleAdapter(this,Sche);
        mRecyclerView.setAdapter( myAdapter);
        mRecyclerView.setHasFixedSize(true);
        // 添加动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    //初始化行程
    private void initSchedule(){
        Sche = dBhelper.getSchedule();
    }

}
