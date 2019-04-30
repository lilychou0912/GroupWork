package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ScheduleEdit extends Activity implements View.OnClickListener,TimePicker.OnTimeChangedListener{
    private View mView;
    private EditText tagText;
    private EditText mText;

    /*******************************/
    private TextView StvTime, EtvTime;
    private int Syear, Smonth, Sday, Eyear, Emonth, Eday, Shour, Ssecond, Ehour, Esecond;
    private StringBuffer Scal, Ecal;
    private String starttime, endtime;
    /*******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule);

        /*日程类型*/
        tagText = (EditText) findViewById(R.id.tag_edit);
        /*输入的文字*/
        mText = (EditText) findViewById(R.id.tag_edit);

        /************/
        //改变开始结束时间
        StvTime = (TextView)findViewById(R.id.start_time_picker);
        EtvTime = (TextView)findViewById(R.id.end_time_picker);
        TextView start = (TextView)findViewById(R.id.start_time_picker);
        TextView end = (TextView)findViewById(R.id.end_time_picker);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        /***********/

        //对edit 进行焦点监听
        tagText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
    }

    //@Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showListPopulWindow(); //调用显示PopuWindow 函数
        }
    }

    private void showListPopulWindow() {
        final String[] list = {"观光", "购物", "饮食","交通","住宿"};//要填充的数据
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(tagText);//以哪个控件为基准，在该处以tagText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tagText.setText(list[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });

        listPopupWindow.show();//把ListPopWindow展示出来
    }

/*******************************/
    /**
     * 获取默认的时间
     */
    private void initTime() {
        //需要前一页的数据更改
        Calendar Scalendar = Calendar.getInstance();
        Calendar Ecalendar = Calendar.getInstance();

        Syear = Scalendar.get(Calendar.YEAR);
        Smonth = Scalendar.get(Calendar.MONTH) + 1;
        Sday = Scalendar.get(Calendar.DAY_OF_MONTH);
        Shour = Scalendar.get(Calendar.HOUR_OF_DAY);
        Ssecond = Scalendar.get(Calendar.MINUTE);

        Eyear = Ecalendar.get(Calendar.YEAR);
        Emonth = Ecalendar.get(Calendar.MONTH) + 1;
        Eday = Ecalendar.get(Calendar.DAY_OF_MONTH);
        Ehour = Ecalendar.get(Calendar.HOUR_OF_DAY);
        Esecond = Ecalendar.get(Calendar.MINUTE);

        Scal = new StringBuffer(String.valueOf(Shour));
        Scal.append(":").append(String.valueOf(Ssecond));
        Ecal = new StringBuffer(String.valueOf(Ehour));
        Ecal.append(":").append(String.valueOf(Esecond));
    }
    /**
     * 时间改变的监听事件
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.Shour = hourOfDay;
        this.Ssecond = minute;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_time_picker: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Scal.length() > 0) { //清除上次记录的时间
                            Scal.delete(0, Scal.length());
                        }
                        StvTime.setText(Scal.append(String.valueOf(Shour)).append(":").append(String.valueOf(Ssecond)));
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
                View dialogView = View.inflate(this, R.layout.dialog_time, null);
                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);

                dialog.setTitle("设置事件开始时间");
                dialog.setView(dialogView);
                dialog.show();
                //初始化时间监听事件
                //timePicker.init();
            }
            break;
            case R.id.end_time_picker: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Ecal.length() > 0) { //清除上次记录的时间
                            Ecal.delete(0, Ecal.length());
                        }
                        EtvTime.setText(Ecal.append(String.valueOf(Ehour)).append(":").append(String.valueOf(Esecond)));
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
                View dialogView = View.inflate(this, R.layout.dialog_time, null);
                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);

                dialog.setTitle("设置事件结束时间");
                dialog.setView(dialogView);
                dialog.show();
                //初始化时间监听事件
                //timePicker.init(Ehour, Esecond, this);
            }
            break;
        }
    }
    //获取用户输入内容
    private void getInput(){
        get_starttime();
        get_endtime();
    }

    //获取行程开始时间
    private void get_starttime(){
        starttime = StvTime.getText().toString();}
    //获取行程截止时间
    private void get_endtime(){
        endtime = EtvTime.getText().toString();
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

/*******************************/

}
