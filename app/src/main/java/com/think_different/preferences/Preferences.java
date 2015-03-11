package com.think_different.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.think_different.Constants;

/**
 * Created by oceancx on 15/3/3.
 */
public class Preferences {
    private static final String IS_LOGGED = "is_logged";
    private static Preferences preference=null;

    private Context mContext;
    private SharedPreferences sharedPreferences=null;

    private static String ACESS_TOKEN = "access_token";

    private static String EXPRIRES_IN = "expires_in";
    private boolean logged;

    private Preferences(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context) {
        if (preference == null) {
            preference = new Preferences(context);
        }
        return preference;
    }


    public void storeString(String name, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public void storeAcessToken( String access_token,Long remind_in,Long expires_in,Long uid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACESS_TOKEN, access_token);
        editor.putLong(EXPRIRES_IN,expires_in);

        editor.commit();
    }

    public String getAcessToken() {
        if(sharedPreferences==null ||sharedPreferences.getString(ACESS_TOKEN, null)==null){
            return null;
        }

        String token=sharedPreferences.getString(ACESS_TOKEN, null).substring(1,sharedPreferences.getString(ACESS_TOKEN, null).length()-1);

        return token;
    }

    public void setLogged(boolean logged){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED, logged);

        editor.commit();
    }


    public boolean isLogged() {

        return sharedPreferences.getBoolean(IS_LOGGED,false);
    }

    public void setUserIdstr(String idstr){

        sharedPreferences.edit().putString("idstr",idstr).commit();

    }

    public String getUserIdstr(){
        return sharedPreferences.getString("idstr","");
    }
}
