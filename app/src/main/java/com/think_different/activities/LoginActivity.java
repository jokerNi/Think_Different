package com.think_different.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.think_different.R;
import com.think_different.network.WeiBoAPI;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

import static com.think_different.utility.Utility.MakeToast;


/**
 * 仿新浪微博客户端
 *
 *
 *
 */

public class LoginActivity extends ActionBarActivity {

    private Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_PROGRESS);
        DebugLog.e("进入LoginActivity");
        setContentView(R.layout.login_activity);
        preferences=Preferences.getInstance(this);
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
                String code = ((url.split("\\?"))[1]).substring(5);

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
                            DebugLog.e(str);

                            //获取到AcessToken
                            String access_token = str.split(":")[1].split(",")[0];

                            // 此处应该 继续获取用户信息,然后创建用户文件夹


                            preferences.storeAcessToken(access_token, 0L, 0L, 0L);


                            Intent i =new Intent(LoginActivity.this,HomePageActivity.class);
                            startActivity(i);
                            finish();

                            //发送发微博请求
//                            RequestParams params2 = new RequestParams();
//                            access_token = access_token.substring(1);
//                            access_token = access_token.substring(0, access_token.length() - 1);
//
//                            Log.e("ta", "access_token=" + access_token);
//                            params2.put("access_token", access_token);
//                            params2.put("status", "来自于ThinkDifferent客户端");
//
//                            client.post(EntryActivity.this, "https://api.weibo.com/2/statuses/update.json", params2, new AsyncHttpResponseHandler() {
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                    try {
//                                        MakeToast(new String(responseBody, "utf-8"), EntryActivity.this);
//                                        Log.e("ya", new String(responseBody, "utf-8"));
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                    try {
//                                        MakeToast(new String(responseBody, "utf-8"), EntryActivity.this);
//                                        Log.e("ya", new String(responseBody, "utf-8"));
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //  finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        try {
////                            MakeToast(new String(responseBody, "utf-8"), EntryActivity.this);
//                         //   Log.e("ya", new String(responseBody, "utf-8"));
//
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
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
