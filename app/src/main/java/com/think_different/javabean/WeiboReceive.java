package com.think_different.javabean;

import java.util.ArrayList;

/**
 * Created by oceancx on 15/3/4.
 */
public class WeiboReceive {


    //微博内容数组
    private ArrayList<Statuse> statuses;

    //广告
    private ArrayList<Ad> ads;

    //前一个游标
    private int previous_cursor;
    private long next_cursor;

    //总微博数
    private int total_number;

    public ArrayList<Statuse> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Statuse> statuses) {
        this.statuses = statuses;
    }

    public ArrayList<Ad> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Ad> ads) {
        this.ads = ads;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
