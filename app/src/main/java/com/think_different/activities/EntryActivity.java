package com.think_different.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.think_different.Constants;
import com.think_different.R;
import com.think_different.network.Login;
import com.think_different.utility.DebugLog;

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
     * 初始化文件夹
     */

    private void initApp() {

        // 初始化App文件夹
        File appDir = new File(Constants.SDCard+Constants.DIR);
        if(!appDir.exists()){
            appDir.mkdir();
        }

        File AvatarDir = new File( Constants.SDCard+Constants.DIR+Constants.DIR_AVATAR);
        DebugLog.e(Constants.SDCard+Constants.DIR + Constants.DIR_AVATAR);
        if(!AvatarDir.exists()){
            AvatarDir.mkdir();
            DebugLog.e("创建用户头像文件夹成功" + "   " + AvatarDir.getAbsolutePath());
        }

        Constants.scale = getResources().getDisplayMetrics().density;
    }


}
