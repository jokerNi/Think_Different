package com.think_different.utility;

import android.content.Context;
import android.widget.Toast;

import com.think_different.Constants;
import com.think_different.preferences.Preferences;

/**
 * Created by oceancx on 15/3/3.
 */
public class Utility {
    public static void MakeToast(String str,Context context){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }

    public static boolean isLoged() {
        return false;
    }

    public static String getAvatarDir() {
        return Constants.SDCard + Constants.DIR + Constants.DIR_AVATAR;
    }


    public static int dip2px( float dpValue) {
        //Log.e(TAG, "cale :" + scale);
        return (int) (dpValue * Constants.scale + 0.5f);

    }


    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / Constants.scale + 0.5f);
    }




}
