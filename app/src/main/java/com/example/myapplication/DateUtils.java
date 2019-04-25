package com.example.myapplication;

/**
 * 描述：
 * 时间：2019/4/22
 * 作者：ztn
 */
public class DateUtils {

    public static String getJR(int month, int day) {
        if (month==1&&day==1) {
            return "元旦";
        } else if (month==4&&day==5) {
            return "清明节";
        } else if (month==5&&day==1) {
            return "劳动节";
        } else if (month==10 && day==1) {
            return "国庆";
        }
        return "";
    }
}
