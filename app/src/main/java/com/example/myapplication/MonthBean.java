package com.example.myapplication;

/**
 * 描述：
 * 时间：2019/4/22
 * 作者：ztn
 */
public class MonthBean {
    private int year;         //该月份 属于哪一年
    private int month;        // 该月 是哪一个月份

    public MonthBean(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
