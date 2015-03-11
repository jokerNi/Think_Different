package com.think_different.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.think_different.R;
import com.think_different.preferences.Preferences;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * Created by oceancx on 15/3/9.
 */
public class SendWeibo extends ActionBarActivity {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity);

        mEditText = (EditText) findViewById(R.id.edit_weibo);
        findViewById(R.id.bt_sendweibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 发送请求  成功了 就返回
                 * 
                 */
                String weibo_content= mEditText.getText().toString();
                AsyncHttpClient client=new AsyncHttpClient();

                //发送发微博请求
                RequestParams params = new RequestParams();

                params.put("access_token", Preferences.getInstance(SendWeibo.this).getAcessToken());
                params.put("status", weibo_content);

                client.post(SendWeibo.this, "https://api.weibo.com/2/statuses/update.json", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });



            }
        });
    }
}
