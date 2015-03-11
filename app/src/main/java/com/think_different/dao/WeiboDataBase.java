package com.think_different.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.think_different.Constants;
import com.think_different.preferences.Preferences;

/**
 * Created by oceancx on 15/3/4.
 */
public class WeiboDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "weibo.db";

    private static final int DB_VERSION = 6;

    public static final String TABLE_STATUS = "status";
    public static final String COL_STATUS_JSON = "status_json";
    public static final String COL_STATUS_IDSTR = "status_idstr";
    //一个登录用户 对应一堆微博
    public static final String COL_STATUS_OWNER = "status_owner";
    //创建时间
    public static final String COL_STATUS_CREATE_AT = "status_create_at";

    public static final String TABLE_USER = "user";
    public static final String COL_USER_IDSTR = "user_idstr";
    public static final String COL_USER_JSON = "user_json";


//    public static final String TABLE_ASSOCIATION="association";
//    public static final String COL_ASSOCIATION_USER="association_user";
//    public static final String COL_ASSOCIATION_STATUS="association_status";

    private static SQLiteDatabase db;
    private static WeiboDataBase DBhelper;

    // 微博表
    public static final String TABLE_STATUS_CREATE =
            "CREATE TABLE " + TABLE_STATUS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_STATUS_IDSTR + " TEXT,"
                    + COL_STATUS_JSON + " TEXT ,"
                    + COL_STATUS_OWNER + " TEXT,"
                    + COL_STATUS_CREATE_AT + " LONG);";


    // 用户表
    public static final String TABLE_USER_CREATE =
            "CREATE TABLE " + TABLE_USER + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_USER_IDSTR + " TEXT,"
                    + COL_USER_JSON + " TEXT );";


    public WeiboDataBase(Context context) {
        this(context, null, null, 0);
    }

    public WeiboDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL(TABLE_STATUS_CREATE);
        db.execSQL(TABLE_USER_CREATE);

        //可以在此处加载数据库初始值
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public static WeiboDataBase getInstance(Context context) {
        if (DBhelper == null) {
            DBhelper = new WeiboDataBase(context);
        }
        return DBhelper;
    }

    public static SQLiteDatabase getDb() {
        return DBhelper.getWritableDatabase();
    }

    public static void closeDb() {
        DBhelper.close();
    }


}
