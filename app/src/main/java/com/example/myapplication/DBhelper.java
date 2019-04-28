package com.example.myapplication;

import android.provider.ContactsContract;
import android.renderscript.ScriptC;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class DBhelper {

    public DBhelper(){
        LitePal.getDatabase();
    }

    //新建行程
    public Schedule addSchedule(String name, String starttime, String endtime){
        //不可重名，判断是否有相同name的行程
        if (LitePal.where("name = ?", name).find(Schedule.class).size() != 0){
            return null;
        }

        Schedule Sche = new Schedule();
        Sche.setName(name);
        Sche.setstarttime(starttime);
        Sche.setendtime(endtime);
        Sche.setIsfinished(false);
        Sche.save();

        return Sche;
    }

    //更新（修改）行程安排
    //点入修改界面时，先使oldname=此时的名字，再跳转页面
    public boolean updateSchedule(String oldname,String name, String starttime, String endtime){
        //检测重名
        if (oldname.equals(name)){
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
    public void deleteSchedule(String oldname){
        LitePal.deleteAll(Schedule.class, "name = ?", oldname);
    }

    //获取行程列表
    public List<Schedule> getSchedule(){
        List<Schedule> Sche = LitePal.order("name").order("starttime").order("endtime").order("isfinished").find(Schedule.class);
        return Sche;
    }

    //查找行程
    public Schedule findSche(String name){
        List<Schedule> Sche = LitePal.where("name = ?", name).find(Schedule.class);
        Schedule sche = Sche.get(0);

        return sche;
    }

    //行程完成 id = -1
    public void ScheFinished(String name, boolean isFinished){
        Schedule sche = findSche(name);
        if (isFinished){
            sche.setIsfinished(true);
        }
        else{
            sche.setToDefault("isfinished");
        }
        sche.updateAll("name = ?", name);
    }
    

}

