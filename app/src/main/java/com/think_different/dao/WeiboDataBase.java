package com.think_different.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oceancx on 15/3/4.
 */
public class WeiboDataBase extends SQLiteOpenHelper{

    private static final String DB_NAME="weibo.db";

    private static final int DB_VERSION=3;

    public static final String TABLE_STATUS="status";
    public static final String COL_STATUS_TEXT ="json";

    public static final String STRING_CREATE=
            "CREATE TABLE "+TABLE_STATUS+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_STATUS_TEXT+" TEXT);";


    public WeiboDataBase(Context context) {
        this(context, null,null,0);
    }

    public WeiboDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL(STRING_CREATE);

        //可以在此处加载数据库初始值
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_STATUS);
        onCreate(db);
    }




}
