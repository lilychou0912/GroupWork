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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guojunustb.library.MonthLoader;
import com.guojunustb.library.WeekViewEvent;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.NoteEditActivity.Send;
import static com.example.myapplication.NoteEditActivity.Sstart;

public class ScheduleEdit extends Activity implements View.OnClickListener,TimePicker.OnTimeChangedListener{
    private View mView;
    private ListPopupWindow listPopupWindow;
    private EditText tagText, mText;
    private TextView ok, cancel, start, end;
    private String oldname;
    private String Sstarttime, Sendtime;
    private Calendar Stime, Etime;
    private String oldStartTime, oldEndTime;
    private String oldCategory;
    private String oldDiscription;
    private String oldColor;
    private String category;
    private String color = "#ffffff";
    private int sId;
    private int choice;
    private int flag = 1;
    private String discription;
    private int Syear, Smonth, Sday, Eyear, Emonth, Eday, Shour, Smintue, Ehour, Emintue;
    private StringBuffer Scal, Ecal;
    private String starttime, endtime;
    private DBhelper dBhelper = new DBhelper();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /*****************************************/
    private static final String TAG = "MainActivity";
    private Context mContext;
    private GridView gridView;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    /*****************************************/

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
        start.setOnClickListener(this);
        end.setOnClickListener(this);

        mContext = this;
        gridView = (GridView) findViewById(R.id.gridView);
        initGridView();

        //初始化（更改情况）
        initData();
        //按下确认键
        okListener();
        //按下取消键
        cancelListener();
        //初始化时间选择器
        initTime();


        tagText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showListPopulWindow(); //调用显示PopuWindow 函数
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
                if(tagText.getText().toString().equals("观光")){
                    color = "#59dbe0";
                }
                else if(tagText.getText().toString().equals("购物")){
                    color = "#f18965";
                }
                else if(tagText.getText().toString().equals("饮食")){
                    color = "#9cc17b";
                }
                else if(tagText.getText().toString().equals("交通")){
                    color = "#ffc24f";
                }
                else if(tagText.getText().toString().equals("住宿")){
                    color = "#bb9af0";
                }
                else {
                    color = "#ffe47a";
                }
                //新建行程事件
                String s = sdf.format(Stime.getTime());
                String e = sdf.format(Etime.getTime());
                Log.d("S",s);
                Log.d("s",e);
                if (category != null && Stime!=null && Etime!=null){
                    if(oldname == null){
                        if(dBhelper.addScheduleItem(getEventTitle(Stime), category, s, e, discription, null, color, sId) != null){
                            flag = 1;
                            finish();
                        }
                    }else {
                        if (dBhelper.updateScheduleItme(oldname, getEventTitle(Stime), category, s, e, discription, null, color, sId) != null) {
                            flag = -1;
                            finish();
                        }else {
                            Log.d("error","这次新建出现了一些问题，请再试一次哟~");
                        }
                    }

                }
            }
        });
    }

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过4张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加4张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(mContext, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }


    //返回键监听
    private void cancelListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        oldCategory = intent.getStringExtra("category_extra");
        oldDiscription = intent.getStringExtra("discription_extra");
        oldname = intent.getStringExtra("name_extra");
        oldColor = intent.getStringExtra("color_extra");
        sId = intent.getIntExtra("sId_extra",-1);

        if (oldStartTime != null) {
            try {
                tagText.setText(oldCategory);
                mText.setText(oldDiscription);
                start.setText(oldStartTime.substring(11, 16));
                end.setText(oldEndTime.substring(11, 16));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            Date Sdate = sdf.parse(oldStartTime);
            Stime = Calendar.getInstance();
            Stime.setTime(Sdate);
            Log.d("init", Stime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date Edate = sdf.parse(oldEndTime);
            Etime = Calendar.getInstance();
            Etime.setTime(Edate);
            Log.d("init", Etime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    //为每个行程事件生成名字
    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

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
        Shour = Scalendar.get(Calendar.HOUR);
        Smintue = Scalendar.get(Calendar.MINUTE);

        Eyear = Ecalendar.get(Calendar.YEAR);
        Emonth = Ecalendar.get(Calendar.MONTH) + 1;
        Eday = Ecalendar.get(Calendar.DAY_OF_MONTH);
        Ehour = Ecalendar.get(Calendar.HOUR);
        Emintue = Ecalendar.get(Calendar.MINUTE);

        Scal = new StringBuffer(String.valueOf(Shour));
        Scal.append(":").append(String.valueOf(Smintue));
        Ecal = new StringBuffer(String.valueOf(Ehour));
        Ecal.append(":").append(String.valueOf(Emintue));
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
        this.Smintue = minute;
        this.Ehour = hourOfDay;
        this.Emintue = minute;
        Log.d("S",String.valueOf(Shour));
        Log.d("s",String.valueOf(Smintue));
        Log.d("E",String.valueOf(Ehour));
        Log.d("e",String.valueOf(Emintue));
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
                        //更改时间
                        Stime.set(Calendar.HOUR_OF_DAY, Shour);
                        Stime.set(Calendar.MINUTE, Smintue);
                        Stime.set(Calendar.SECOND,0);
                        Log.d("startafter", Stime.toString());
                        start.setText(Scal.append(String.valueOf(Shour)).append(":").append(String.valueOf(Smintue)));
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
                timePicker.setCurrentHour(Shour);
                timePicker.setCurrentMinute(Smintue);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);

                dialog.setTitle("设置事件开始时间");
                dialog.setView(dialogView);
                dialog.show();

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
                        //更改时间
                        Etime.set(Calendar.HOUR_OF_DAY, Ehour);
                        Etime.set(Calendar.MINUTE, Emintue);
                        Etime.set(Calendar.SECOND,0);
                        Log.d("endafter", Etime.toString());
                        end.setText(Ecal.append(String.valueOf(Ehour)).append(":").append(String.valueOf(Emintue)));
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
                timePicker.setCurrentHour(Ehour);
                timePicker.setCurrentMinute(Emintue);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);

                dialog.setTitle("设置事件结束时间");
                dialog.setView(dialogView);
                dialog.show();
            }
            break;
        }
    }

}

