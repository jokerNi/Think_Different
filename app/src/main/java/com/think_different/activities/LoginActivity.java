package com.think_different.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DebugUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.think_different.Constants;
import com.think_different.R;
import com.think_different.dao.WeiboDataBase;
import com.think_different.javabean.User;
import com.think_different.network.WeiBoAPI;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;
import com.think_different.utility.Utility;

import org.apache.http.Header;

import java.io.File;
import java.io.UnsupportedEncodingException;

import static com.think_different.utility.Utility.MakeToast;


/**
 * 仿新浪微博客户端
 */

public class LoginActivity extends ActionBarActivity {

    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DebugLog.e("进入LoginActivity");
        setContentView(R.layout.login_activity);

        preferences = Preferences.getInstance(this);

        WebView webView = (WebView) findViewById(R.id.weibo_login);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WeiboWebViewClient());

        //  https://api.weibo.com/oauth2/authorize?client_id=3375079460&redirect_uri=https://api.weibo.com/oauth2/default.html
        webView.loadUrl(WeiBoAPI.OAUTH2_AUTHORIZE + "client_id=" + WeiBoAPI.APP_KEY + "&redirect_uri=" + WeiBoAPI.REDIRECT_URL + "&display=mobile");
    }


    private class WeiboWebViewClient extends WebViewClient {

        AsyncHttpClient client = new AsyncHttpClient();

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.startsWith("https://api.weibo.com/oauth2/default.html")) {
                //说明已经获取到了Code
                DebugLog.e(url);

                //分割出Code
                final String code = ((url.split("\\?"))[1]).substring(5);

                //用Code来换取Access_Token
                final RequestParams params = new RequestParams();
                params.put("client_id", WeiBoAPI.APP_KEY);
                params.put("client_secret", WeiBoAPI.APP_SECRET);
                params.put("grant_type", "authorization_code");
                params.put("code", code);
                params.put("redirect_uri", "https://api.weibo.com/oauth2/default.html");

                client.post(LoginActivity.this, "https://api.weibo.com/oauth2/access_token", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String str = new String(responseBody, "utf-8");
                            DebugLog.e("获取AcessToken");

                            //获取到AcessToken
                            String access_token = str.split(":")[1].split(",")[0];

                            // 此处应该 继续获取用户信息,然后创建用户文件夹
                            preferences.storeAcessToken(access_token, 0L, 0L, 0L);
                            RequestParams params = new RequestParams();
                            params.put("access_token", preferences.getAcessToken());
                            client.get("https://api.weibo.com/2/account/get_uid.json", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    //成功获取用户Uid
                                    try {
                                        String uid_str = new String(responseBody, "utf-8");
                                        //{"uid":2802042662}
                                        DebugLog.e(uid_str);
                                        String uid = uid_str.substring(7, uid_str.length() - 1);
                                        DebugLog.e(uid);
                                        Constants.uid = uid;
                                        File f = new File(Utility.getCurUserDir());
                                        if (!f.exists()) {
                                            f.mkdirs();
                                            DebugLog.e("用户文件夹创建成功");
                                        }

                                        RequestParams params = new RequestParams();
                                        params.put("access_token", preferences.getAcessToken());
                                        params.put("uid",  Constants.uid);
                                        client.get(WeiBoAPI.BASE_URL + WeiBoAPI.URL_USERS_SHOW, params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                try {
                                                    String str = new String(responseBody, "utf-8");
                                                    DebugLog.e("获取用户信息成功" + str);

                                                    Constants.user= new Gson().fromJson(str,User.class);
                                                    String id_str=Constants.user.getIdstr();
                                                    //将id_str加入到数据库中
                                                    ContentValues cv= new ContentValues();
                                                    cv.put(WeiboDataBase.COL_USER_IDSTR,id_str);
                                                    cv.put(WeiboDataBase.COL_USER_JSON,str);

                                                    WeiboDataBase helper=WeiboDataBase.getInstance(LoginActivity.this);
                                                    SQLiteDatabase db=helper.getWritableDatabase();
                                                    db.insert(WeiboDataBase.TABLE_USER,null,cv);
                                                    DebugLog.e("用户信息插入成功");
                                                    db.close();
                                                    //到这里才算用户登录成功
                                                    preferences.setLogged(true);
                                                    preferences.setUserIdstr(id_str);
                                                    Intent i = new Intent(LoginActivity.this, HomePageActivity.class);

                                                    startActivity(i);
                                                    finish();
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });


                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //  finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }

                });
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //DebugLog.e(url);

        }


    }
}
