package com.think_different.network;

import android.app.Activity;
import android.content.Context;

import com.think_different.preferences.Preferences;

/**
 * Created by oceancx on 15/3/6.
 */
public class Login {
    public static boolean isLoged(Context context) {
        return Preferences.getInstance(context).getAcessToken()!=null;
    }

    public static boolean Log(Activity activity) {

        return true;
    }
}
