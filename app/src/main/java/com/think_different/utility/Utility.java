package com.think_different.utility;

import android.content.Context;
import android.widget.Toast;

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
}
