package com.example.myapplication;

import android.provider.ContactsContract;
import android.renderscript.ScriptC;
import android.util.Log;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class DBhelper {

    static int sId = 0;

    public DBhelper() {
        LitePal.getDatabase();
    }

    //新建行程
    public Schedule addSchedule(String name, String starttime, String endtime) {
        //不可重名，判断是否有相同name的行程
        if (LitePal.where("name = ?", name).find(Schedule.class).size() != 0) {
            return null;
        }

        Schedule Sche = new Schedule();
        Sche.setSId(++sId);
        Sche.setName(name);
        Sche.setstarttime(starttime);
        Sche.setendtime(endtime);
        Sche.setIsfinished(false);
        Sche.save();

        return Sche;
    }

    //更新（修改）行程安排
    //点入修改界面时，先使oldname=此时的名字，再跳转页面
    public boolean updateSchedule(String oldname, String name, String starttime, String endtime) {
        //检测重名
        if (oldname.equals(name)) {
            return false;
        }

        Schedule Sche = new Schedule();
        Sche.setName(name);
        Sche.setstarttime(starttime);
        Sche.setendtime(endtime);
        Sche.setIsfinished(false);
        Sche.save();
        Sche.updateAll("name = ?", oldname);
        return true;
    }

    //删除行程
    public void deleteSchedule(String oldname) {
        LitePal.deleteAll(Schedule.class, "name = ?", oldname);
    }

    //获取行程列表
    public List<Schedule> getSchedule() {
        List<Schedule> Sche = LitePal.order("name").order("starttime").order("endtime").order("isfinished").find(Schedule.class);
        return Sche;
    }

    //查找行程
    public Schedule findSche(String name) {
        List<Schedule> Sche = LitePal.where("name = ?", name).find(Schedule.class);
        Schedule sche = Sche.get(0);

        return sche;
    }

    //行程完成 id = -1
    public void ScheFinished(String name, boolean isFinished) {
        Schedule sche = findSche(name);
        if (isFinished) {
            sche.setIsfinished(true);
        } else {
            sche.setToDefault("isfinished");
        }
        sche.updateAll("name = ?", name);
    }

    //新建行程事件
    public ScheduleItem addScheduleItem(String name, String category, String starttime, String endtime, String discription, String imgUrl, String color, int id) {

        ScheduleItem Sitem = new ScheduleItem();
        Sitem.setsId(id);
        Sitem.setIname(name);
        Sitem.setIcategory(category);
        Sitem.setIstarttime(starttime);
        Sitem.setIendtime(endtime);
        Sitem.setIdiscription(discription);
        Sitem.setIimgUrl(imgUrl);
        Sitem.setIcolor(color);
        Sitem.saveThrows();

        Sitem.save();

        return Sitem;
    }

    //更新（修改）行程事件
    public ScheduleItem updateScheduleItme(String oldname, String name, String category, String starttime, String endtime, String discription, String imgUrl, String color, int id) {
        ///结束时间必须大于开始时间
        Calendar Scalendar = Calendar.getInstance();
        Calendar Ecalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date Sdate = sdf.parse(starttime);
            Date Edate = sdf.parse(endtime);
            Scalendar.setTime(Sdate);
            Ecalendar.setTime(Edate);
        } catch (ParseException e) {
            e.printStackTrace();
            if (Ecalendar.compareTo(Scalendar) != 1) {
                return null;
            }
        }

        ScheduleItem sItem = findItem(oldname, id);
        sItem.setsId(id);
        sItem.setIname(name);
        sItem.setIcategory(category);
        sItem.setIstarttime(starttime);
        sItem.setIendtime(endtime);
        sItem.setIdiscription(discription);
        sItem.setIimgUrl(imgUrl);
        sItem.setIcolor(color);
        sItem.updateAll("iname = ?", oldname);
        return sItem;
    }

    //删除行程事件
    public void deleteScheduleItme(String name, int id) {
        LitePal.deleteAll(ScheduleItem.class, "iname = ?", name);
    }

    //查找行程事件
    public ScheduleItem findItem(String name, int id){
        List<ScheduleItem> ItemList = LitePal.where("iname = ?", name).find(ScheduleItem.class);
        ScheduleItem item = ItemList.get(0);

        return item;
    }

    //获取行程事件列表
    public List<ScheduleItem> getScheduleItme(int id) {
        String sId = String.valueOf(id);
        List<ScheduleItem> itmeList = LitePal.where("sId = ?", sId).find(ScheduleItem.class);

        return itmeList;
    }

}