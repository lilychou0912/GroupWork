package com.example.myapplication.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.Adapter.GridViewAdapter;
import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.Bean.ScheduleItem;
import com.example.myapplication.MainConstant;
import com.example.myapplication.PictureSelectorConfig;
import com.example.myapplication.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class ScheduleEditActivity extends Activity implements View.OnClickListener,TimePicker.OnTimeChangedListener{
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
    private String sId;
    private String objId;
    private String discription;
    private int Syear, Smonth, Sday, Eyear, Emonth, Eday, Shour, Smintue, Ehour, Emintue;
    private StringBuffer Scal, Ecal;
    private String starttime, endtime;
    private int flag;
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
        //Bmob初始化
        Bmob.initialize(this, "1768baa1a8670ebb020369f5672e443f" );
        //
        initGridView();
        //初始化（更改情况）
        initData();
        //按下确认键
        okListener();
        //按下取消键
        cancelListener();
        //初始化时间选择器
        initTime();
        //初始化图片信息
        initPic();


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
                                      if (tagText.getText().toString().equals("观光")) {
                                          color = "#59dbe0";
                                      } else if (tagText.getText().toString().equals("购物")) {
                                          color = "#f18965";
                                      } else if (tagText.getText().toString().equals("饮食")) {
                                          color = "#9cc17b";
                                      } else if (tagText.getText().toString().equals("交通")) {
                                          color = "#ffc24f";
                                      } else if (tagText.getText().toString().equals("住宿")) {
                                          color = "#bb9af0";
                                      } else {
                                          color = "#ffe47a";
                                      }
                                      //新建行程事件
                                      String S = sdf.format(Stime.getTime());
                                      String E = sdf.format(Etime.getTime());
                                      Log.d("S", S);
                                      Log.d("s", E);
                                      if (category != null && Stime != null && Etime != null) {
                                          if (oldname == null) {
                                              BmobQuery<Schedule> query = new BmobQuery<Schedule>();
                                              query.getObject(sId, new QueryListener<Schedule>() {
                                                  @Override
                                                  public void done(Schedule schedule, BmobException e) {
                                                      if (e == null) {
                                                          ScheduleItem item = new ScheduleItem();
                                                          item.setIcategory(category);
                                                          item.setIname(getEventTitle(Stime));
                                                          item.setsNote_pointer(schedule);
                                                          item.setIcolor(color);
                                                          item.setIstarttime(S);
                                                          item.setIendtime(E);
                                                          item.setIdiscription(discription);
                                                          item.setIimgUrl(mPicList);
                                                          item.save(new SaveListener<String>() {
                                                              @Override
                                                              public void done(String objectId,BmobException e) {
                                                                  if(e==null){
                                                                      Log.i("bmob","创建成功啦~");
                                                                      finish();

                                                                  }else{
                                                                      Log.i("bmob","失败："+e.getMessage());
                                                                  }
                                                              }

                                                          });
                                                      } else {
                                                          Toast.makeText(ScheduleEditActivity.this, "新建失败啦", Toast.LENGTH_SHORT).show();

                                                      }
                                                  }
                                              });
                                          } else {
                                              ScheduleItem item = new ScheduleItem();
                                              item.setIcategory(category);
                                              item.setIname(getEventTitle(Stime));
                                              item.setIcolor(color);
                                              item.setIstarttime(S);
                                              item.setIendtime(E);
                                              item.setIdiscription(discription);
                                              item.setIimgUrl(mPicList);
                                              item.update(objId, new UpdateListener() {
                                                  @Override
                                                  public void done(BmobException e) {
                                                      if (e == null) {
                                                          Log.i("bmob", "更改成功啦~");
                                                          finish();

                                                      } else {
                                                          Log.i("bmob", "失败：" + e.getMessage());
                                                      }
                                                  }

                                              });
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
     * 打开相册或者照相机选择凭证图片，最多4张
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
                //PicUpload(mPicList);//把图片上传到云端
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }
    //将图片上传到云端
    private void PicUpload(ArrayList<String> PicList) {
        String[] picPath = (String[])mPicList.toArray(new String[mPicList.size()]);
        BmobFile.uploadBatch(picPath, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == picPath.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    Toast.makeText(ScheduleEditActivity.this, "图片上传成功啦", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Toast.makeText(ScheduleEditActivity.this,"错误码" + statuscode + ",错误描述：" + errormsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
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
        sId = intent.getStringExtra("sId_extra");
        objId = intent.getStringExtra("objId_extra");

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

    //初始化图片信息
    private void initPic() {
        BmobQuery<ScheduleItem> query = new BmobQuery<ScheduleItem>();
        if (objId != null) {
            query.getObject(objId, new QueryListener<ScheduleItem>() {
                @Override
                public void done(ScheduleItem item, BmobException e) {
                    if (e == null) {
                        if (item.getIimgUrl() != null) {
                            mPicList = item.getIimgUrl();
                            initGridView();
                        }
                        //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
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

        Syear = Stime.get(Calendar.YEAR);
        Smonth = Stime.get(Calendar.MONTH) + 1;
        Sday = Stime.get(Calendar.DAY_OF_MONTH);
        Shour = Stime.get(Calendar.HOUR_OF_DAY);
        Smintue = Stime.get(Calendar.MINUTE);

        Eyear = Etime.get(Calendar.YEAR);
        Emonth = Etime.get(Calendar.MONTH) + 1;
        Eday = Etime.get(Calendar.DAY_OF_MONTH);
        Ehour = Etime.get(Calendar.HOUR_OF_DAY);
        Emintue = Etime.get(Calendar.HOUR_OF_DAY);

        Scal = new StringBuffer(String.valueOf(Shour));
        Scal.append(":").append(String.valueOf(Smintue));
        //start.setText(Scal);
        Ecal = new StringBuffer(String.valueOf(Ehour));
        Ecal.append(":").append(String.valueOf(Emintue));
        //end.setText(Ecal);
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
        if(flag == 1){
            this.Shour = hourOfDay;
            this.Smintue = minute;
        }
        if(flag == -1){
            this.Ehour = hourOfDay;
            this.Emintue = minute;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_time_picker: {
                flag = 1;
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
                        if(((Shour/10)<1) && ((Smintue/10)<1)){
                            start.setText(Scal.append("0").append(String.valueOf(Shour)).append(":").append("0").append(String.valueOf(Smintue)));
                            dialog.dismiss();
                        }
                        else if (((Shour/10)<1) && ((Smintue/10)>=1)){
                            start.setText(Scal.append("0").append(String.valueOf(Shour)).append(":").append(String.valueOf(Smintue)));
                            dialog.dismiss();
                        }
                             else if(((Shour/10)>=1) && ((Smintue/10)<1)){
                            start.setText(Scal.append(String.valueOf(Shour)).append(":").append("0").append(String.valueOf(Smintue)));
                            dialog.dismiss();
                        }
                        else {
                            start.setText(Scal.append(String.valueOf(Shour)).append(":").append(String.valueOf(Smintue)));
                            dialog.dismiss();
                        }

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
                flag = -1;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Ehour > Shour){
                            if (Ecal.length() > 0) { //清除上次记录的时间
                                Ecal.delete(0, Ecal.length());
                            }
                            //更改时间
                            Etime.set(Calendar.HOUR_OF_DAY, Ehour);
                            Etime.set(Calendar.MINUTE, Emintue);
                            Etime.set(Calendar.SECOND,0);
                            Log.d("endafter", Etime.toString());
                            if(((Ehour/10)<1) && ((Emintue/10)<1)){
                                end.setText(Ecal.append("0").append(String.valueOf(Ehour)).append(":").append("0").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else if (((Ehour/10)<1) && ((Emintue/10)>=1)){
                                end.setText(Ecal.append("0").append(String.valueOf(Ehour)).append(":").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else if(((Ehour/10)>=1) && ((Emintue/10)<1)){
                                end.setText(Ecal.append(String.valueOf(Ehour)).append(":").append("0").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else {
                                end.setText(Ecal.append(String.valueOf(Ehour)).append(":").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                        }
                        else if (Ehour == Shour && Emintue > Smintue){
                            if (Ecal.length() > 0) { //清除上次记录的时间
                                Ecal.delete(0, Ecal.length());
                            }
                            //更改时间
                            Etime.set(Calendar.HOUR_OF_DAY, Ehour);
                            Etime.set(Calendar.MINUTE, Emintue);
                            Etime.set(Calendar.SECOND,0);
                            Log.d("endafter", Etime.toString());
                            if(((Ehour/10)<1) && ((Emintue/10)<1)){
                                end.setText(Ecal.append("0").append(String.valueOf(Ehour)).append(":").append("0").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else if (((Ehour/10)<1) && ((Emintue/10)>=1)){
                                end.setText(Ecal.append("0").append(String.valueOf(Ehour)).append(":").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else if(((Ehour/10)>=1) && ((Emintue/10)<1)){
                                end.setText(Ecal.append(String.valueOf(Ehour)).append(":").append("0").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                            else {
                                end.setText(Ecal.append(String.valueOf(Ehour)).append(":").append(String.valueOf(Emintue)));
                                dialog.dismiss();
                            }
                        }
                        else {
                            if (Ecal.length() > 0) { //清除上次记录的时间
                                Ecal.delete(0, Ecal.length());
                            }
                        }

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

