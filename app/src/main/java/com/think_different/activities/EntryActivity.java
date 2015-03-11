package com.think_different.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.gson.Gson;
import com.think_different.Constants;
import com.think_different.R;
import com.think_different.adapter.WeiBoCursorAdapter;
import com.think_different.dao.WeiboDataBase;
import com.think_different.dao.WeiboProvider;
import com.think_different.javabean.User;
import com.think_different.network.Login;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;
import com.think_different.utility.Utility;

import java.io.File;

/**
 * Created by oceancx on 15/3/6.
 */
public class EntryActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);

        initApp();
        if(Login.isLoged(this)){
            //如果登陆过了 就跳转到微博的首页
            Intent i  = new Intent(this,HomePageActivity.class);
            startActivity(i);
            finish();
        }else {
            //如果没有登陆过,就进行登陆
                Intent i  = new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
        }


    }

    /**
     * 各种全局变量初始化
     */

    private void initApp() {

        // 初始化App文件夹
        File appDir = new File(Constants.SDCard+Constants.DIR);
        if(!appDir.exists()){
            appDir.mkdir();
            DebugLog.e("创建App文件夹成功" + "   " + appDir.getAbsolutePath());
        }

        File avatarDir = new File( Constants.SDCard+Constants.DIR+Constants.DIR_AVATAR);
        if(!avatarDir.exists()){
            avatarDir.mkdir();
            DebugLog.e("创建用户头像文件夹成功" + "   " + avatarDir.getAbsolutePath());
        }

        //根据设备大小初始化scale值,用于dip2px
        Constants.scale = getResources().getDisplayMetrics().density;
        Constants.appContext = getApplicationContext();

        //初始化Constants的user变量
        Preferences preferences= Preferences.getInstance(this);
        if( !preferences.getUserIdstr().equals("")){
            //说明登陆过了 这就要把数据库中的user赋值为Constants
            Cursor c= getContentResolver().query(WeiboProvider.USERS_CONTENT_URI,
                    new String[]{WeiboDataBase.COL_USER_JSON},
                    WeiboDataBase.COL_USER_IDSTR + " = " + preferences.getUserIdstr(),
                    null, null
            );

            if( c.moveToNext()){
                Constants.user = new Gson().fromJson( c.getString(0) , User.class);
                DebugLog.e( Constants.user.getName());
                File f= new File(Utility.getCurUserDir());
                if( !f.exists()){
                    f.mkdirs();
                }
                Constants.uid = Constants.user.getIdstr();
            }


        }


    }


}
