package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guojunustb.library.DateTimeInterpreter;
import com.guojunustb.library.MonthLoader;
import com.guojunustb.library.WeekView;
import com.guojunustb.library.WeekViewEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NoteEditActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekView.EmptyViewClickListener, WeekView.AddEventClickListener, WeekView.DropListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_WEEK_VIEW = 3;
    private String Sstarttime, Sendtime;
    private int sId;
    public static Calendar Sstart, Send;
    private Calendar Scalendar, Ecalendar;
    private int mWeekViewType = TYPE_WEEK_VIEW;
    private WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<>();;
    private List<WeekViewEvent> matchedEvents;
    /*********/
    private RelativeLayout pictureView;
    private View contentView;
    private RecyclerView pRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PictureAdapter pAdapter;
    private List<WeekViewEvent> pEvent;

    FileOutputStream out;
    String bitmapName = "longpicture.jpg";
    File file;
    String TravelFilePath;
    /*********/
    private List<ScheduleItem> sItem;
    private DBhelper dBhelper = new DBhelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);
        /************************************************************************/
        contentView = LayoutInflater.from(NoteEditActivity.this).inflate(R.layout.long_picture, null);
        pRecyclerView = contentView.findViewById(R.id.pic_recyclerview);
        /************************************************************************/
        TextView draggableView = (TextView) findViewById(R.id.draggable_view);
        draggableView.setOnLongClickListener(new DragTapListener());
        sId = (int) getIntent().getSerializableExtra("sId_extra");
        Sstarttime = (String) getIntent().getSerializableExtra("starttime_extra");
        Sendtime = (String) getIntent().getSerializableExtra("endtime_extra");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date Sdate = null;
        try {
            Sdate = sdf.parse(Sstarttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar Scalendar = Calendar.getInstance();
        Scalendar.setTime(Sdate);

        Date Edate = null;
        try {
            Edate = sdf.parse(Sendtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar Ecalendar = Calendar.getInstance();
        Ecalendar.setTime(Edate);

        //设置日期范围
        mWeekView.setMinDate(Scalendar);
        mWeekView.setMaxDate(Ecalendar);

        draggableView.setOnLongClickListener(new DragTapListener());

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set EmptyView Click Listener
        mWeekView.setEmptyViewClickListener(this);

        // Set AddEvent Click Listener
        mWeekView.setAddEventClickListener(this);

        // Set Drag and Drop Listener
        mWeekView.setDropListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        //初始化events
        events.clear();
        //初始化界面，载入已建立行程事件
        initScheduleItem();
        //初始化Recyclerview
        initRecycle();
        //设置初始化显示天数
        long diffDays = (mWeekView.getMaxDate().getTimeInMillis() - mWeekView.getMinDate().getTimeInMillis()) / (1000 * 60 * 60 * 24);
        if (diffDays < 4) {
            mWeekView.setNumberOfVisibleDays(Integer.parseInt(String.valueOf(diffDays)) + 1);
        } else {
            mWeekView.setNumberOfVisibleDays(4);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        events.clear();
        initScheduleItem();
        mWeekView.notifyDatasetChanged();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final class DragTapListener implements View.OnLongClickListener {
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_day_view);
        switch (id) {
            case R.id.action_publish:

                return true;
            case R.id.action_pic://生成长图
                if (sItem != null) {
                    //setContentView(R.layout.long_picture);
                    layoutManager = new LinearLayoutManager(this);
                    pRecyclerView.setLayoutManager(layoutManager);
                    // 获取数据，向适配器传数据，绑定适配器
                    pAdapter = new PictureAdapter(this, sItem);
                    pRecyclerView.setAdapter(pAdapter);
                    pRecyclerView.setHasFixedSize(true);
                    pAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(NoteEditActivity.this, LongPicture.class);
                    //启动
                    startActivity(intent);
                    // 添加动画
                    //pRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    pictureView = contentView.findViewById(R.id.long_pic);
                    saveBitmap(getRelativeLayoutBitmap(pictureView));
                }
                return true;
            case R.id.action_draft:

                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    long diffDays = (mWeekView.getMaxDate().getTimeInMillis() - mWeekView.getMinDate().getTimeInMillis()) / (1000 * 60 * 60 * 24);
                    if (diffDays < 4) {
                        mWeekView.setNumberOfVisibleDays(Integer.parseInt(String.valueOf(diffDays)) + 1);
                    } else {
                        mWeekView.setNumberOfVisibleDays(4);
                    }

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                String strMinutes = String.format("%02d", minutes);
                if (hour > 11) {
                    return (hour - 12) + ":" + strMinutes + " PM";
                } else {
                    if (hour == 0) {
                        return "12:" + strMinutes + " AM";
                    } else {
                        return hour + ":" + strMinutes + " AM";
                    }
                }
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Intent intent = new Intent(this, ScheduleEdit.class);
        ScheduleItem item = dBhelper.findItem(getEventTitle(event.getStartTime()), sId);
        intent.putExtra("sId_extra", sId);
        intent.putExtra("name_extra", item.getIname());
        intent.putExtra("startTime_extra", item.getIstarttime());
        intent.putExtra("endTime_extra", item.getIendtime());
        intent.putExtra("category_extra", item.getIcategory());
        intent.putExtra("discription_extra", item.getIdiscription());
        intent.putExtra("color_extra", item.getIcolor());
        startActivity(intent);

    }
    /****/
    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("提示");
        alertDialogBuilder
                .setMessage("你确定要删除这个行程安排吗？");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                events.remove(event);
                dBhelper.deleteScheduleItme(getEventTitle(event.getStartTime()), sId);
                events.clear();
                initScheduleItem();
                mWeekView.notifyDatasetChanged();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    /****/
    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void onEmptyViewClicked(Calendar date) {
        Toast.makeText(this, "再点一次就可以创建行程安排哟~", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddEventClicked(Calendar startTime, Calendar endTime) {
        String name = null;
        Sstart = (Calendar) startTime.clone();
        Send = (Calendar) endTime.clone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = sdf.format(startTime.getTime());
        String e = sdf.format(endTime.getTime());
            //启动
            Intent intent = new Intent(NoteEditActivity.this, ScheduleEdit.class);
            intent.putExtra("sId_extra", sId);
            intent.putExtra("name_extra", name);
            intent.putExtra("startTime_extra", s);
            intent.putExtra("endTime_extra", e);
            startActivity(intent);
    }

    @Override
    public void onDrop(View view, Calendar date) {
        Toast.makeText(this, "View dropped to " + date.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        showEvents(newMonth, newYear);
        return events;
    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     **/
    /**
   private boolean eventMatches(WeekViewEvent event, int year, int month) {
       return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
   }
**/
    //SHOW EVENTS
    private void showEvents(int month, int year) {
        int idset = 0;
        int c = 0;

        matchedEvents = new ArrayList<>();

        for (int i = 0; i < sItem.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date Sdate = sdf.parse(sItem.get(i).getIstarttime());
                Scalendar = Calendar.getInstance();
                Scalendar.setTime(Sdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Date Edate = sdf.parse(sItem.get(i).getIendtime());
                Ecalendar = Calendar.getInstance();
                Ecalendar.setTime(Edate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String name = sItem.get(i).getIcategory() + '\n' + sItem.get(i).getIstarttime().substring(11,16) + "-" + sItem.get(i).getIendtime().substring(11,16);
            int Colour = 0;

            try{
                Colour = Color.parseColor(sItem.get(i).getIcolor());
            }catch(IllegalArgumentException e1){
                dBhelper.deleteScheduleItme(getEventTitle(Scalendar), sId);
                Toast.makeText(this,"这次新建出现了一些问题，请再试一次哟~", Toast.LENGTH_LONG).show();
            }
            WeekViewEvent event = new WeekViewEvent(idset++, name, Scalendar, Ecalendar);

            event.setColor(Colour);

            if (!events.contains(event)) {
                Log.d("Event: ", event.getName());
                events.add(event);
            }
            /**
            for (WeekViewEvent we : events) {
                if (eventMatches(we, year, month)) {
                    if (!matchedEvents.contains(we)) {
                        Log.d("we: ", we.getName());
                        matchedEvents.add(we);
                    }
                }
            }
            **/
            Scalendar = null;
            Ecalendar = null;
            event = null;

        }
        mWeekView.notifyDatasetChanged();

    }

    private void initRecycle() {
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        pRecyclerView.setLayoutManager(layoutManager);
        pAdapter = new PictureAdapter(this, sItem);
        pRecyclerView.setAdapter(pAdapter);
        pRecyclerView.setHasFixedSize(true);

    }

    public static Bitmap getRelativeLayoutBitmap(RelativeLayout rLayout) {
        int h = 0;
        // 获取RelativeLayout实际高度
        for (int i = 0; i < rLayout.getChildCount(); i++) {
            rLayout.getChildAt(i).measure(0, 0);
            h += rLayout.getChildAt(i).getMeasuredHeight();
        }
        rLayout.measure(0, 0);
        // 创建相应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(rLayout.getMeasuredWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        rLayout.draw(canvas);
        if (bitmap == null) {
            Log.d("空","dijduthfdiuhgfiuko");
        }
        return bitmap;
    }

    /*生成长图的方法*/
    public static Bitmap getViewDrawingCacheBitmap(View view) {
        view = view.getRootView();
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);

        if (bm == null) {
            Log.d("df","dijduthfdiuhgfiuko");
        }
        return bm;
    }

    //保存获取的bitmap图片
    public void saveBitmap(Bitmap bmp) {

        try { // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/travel1U/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            file = new File(sdCardDir, bitmapName);// 在SDcard的目录下创建图片文,以当前时间为其命名
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            Log.e("saveBitMap", "saveBitmap: 图片保存到" + Environment.getExternalStorageDirectory() + "/travel1U/" + "00");
            TravelFilePath = Environment.getExternalStorageDirectory() + "/travel1U/" + "00";
//            showShare(QQFilePath);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(HahItemActivity.this,"保存已经至"+Environment.getExternalStorageDirectory()+"/CoolImage/"+"目录文件夹下", Toast.LENGTH_SHORT).show();
    }

    //初始化行程事件
    private void initScheduleItem(){
        sItem = dBhelper.getScheduleItme(sId);
        //Log.d("sss",String.valueOf(sId));
    }
    }