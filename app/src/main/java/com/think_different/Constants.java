package com.think_different;

import android.content.Context;
import android.os.Environment;

import com.think_different.javabean.User;

/**
 * Created by oceancx on 15/3/3.
 */
public class Constants {


    public static int ACTIVITY_REQCD_HOME = 1;
    public static String SHARED_PREFERENCE_NAME = "think_different";
    public static String DIR = "/Think_Different";
    public static String DIR_USER = "/User";
    public static String DIR_AVATAR = "/Avatar";
    public static String SDCard = Environment.getExternalStorageDirectory().getPath();



    public static float scale = 0.0f;
    public static Context appContext;
    public static String uid;

    // 当前用户的id_str
    public static User user;


}
