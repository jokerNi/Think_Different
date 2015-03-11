package com.think_different.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.think_different.Constants;
import com.think_different.R;
import com.think_different.dao.WeiboDataBase;
import com.think_different.dao.WeiboProvider;
import com.think_different.fragments.TimelineFragment;
import com.think_different.javabean.Statuse;
import com.think_different.network.WeiBoAPI;
import com.think_different.network.WeiboClient;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;
import com.think_different.utility.ImageLoader;
import com.think_different.utility.Utility;

import org.apache.http.Header;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by oceancx on 15/3/3.
 * <p/>
 * 此Activity为MainActivity
 */
public class HomePageActivity extends ActionBarActivity {
    /**
     * 这个类用于首页显示,如果用户还没有登陆,就提示用户进行登陆
     * 有一个DrawerLayout 用于导航
     * 内容视图 包含一个FAB,一个SwipeToRefresh,RecylcerView+CardView
     */
    private Preferences preferences;

    private String[] mDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private CircleImageView side_face;
    private TextView side_name;
    private TextView side_intro;


    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private ViewPager viewPager;
    private WeiboPagerAdapter mWeiboPagerAdapter;
    private LinearLayout drawerView;

    private Toolbar mToolbar;
    private Object DBlock=new Object();
    private Bitmap sideface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preferences = Preferences.getInstance(this);

        setContentView(R.layout.homepage_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.platte_text_icon));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        side_face = (CircleImageView) findViewById(R.id.side_face);
        side_name = (TextView)findViewById(R.id.side_name);
        side_intro = (TextView) findViewById(R.id.side_intro);

        setSideWidgets();

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_name);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        drawerView = (LinearLayout) findViewById(R.id.drawer_view);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItemTitles = getResources().getStringArray(R.array.drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.title, mDrawerItemTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mWeiboPagerAdapter = new WeiboPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mWeiboPagerAdapter);


    }

    private void setSideWidgets() {
        //为三个边栏控件设置内容 side_face name intro
        side_name.setText(Constants.user.getName());
        side_intro.setText(Constants.user.getDescription());
        if( getSideface()!=null){
            side_face.setImageBitmap(getSideface());
        }

    }

    public Bitmap getSideface() {
        // 先从文件中找用户头像图片 如果没找到,就下载
        Bitmap bitmap;
        File f =new File(Utility.facePath());
        if( !f.exists()){
            //下载一个
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(Constants.user.getAvatar_large(),new FileAsyncHttpResponseHandler(f) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    DebugLog.e("side face 图片下载成功");
                    side_face.setImageBitmap(getSideface());
                }
            });
            return null;
        }else {
            // 从File中解析出来Bitmap
           DebugLog.e(Utility.dip2px(90)+"  " +Utility.dip2px(90));
           bitmap=  ImageLoader.getInstance().decodeBitmapFromFile(f.getAbsolutePath(),Utility.dip2px(90),Utility.dip2px(90));
        }

        return bitmap;
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            // 创建一个新的fragment 以及 具体说明 这个fragment的作用
            if (position < 3) {
                viewPager.setCurrentItem(position);

            } else if (position == 3) {
                // 退出登录 要好好做
                RequestParams params=new RequestParams();
                params.put("access_token",Preferences.getInstance(HomePageActivity.this).getAcessToken());
                WeiboClient.get(WeiBoAPI.BASE_OAUTH2 +WeiBoAPI.URL_REVOKEOAUTH2, params,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //
                        DebugLog.e("注销登录成功");
                        Utility.MakeToast("请重新登录",HomePageActivity.this);
                        Preferences.getInstance(HomePageActivity.this).setLogged(false);

                        Intent i =new Intent(HomePageActivity.this,EntryActivity.class);
                        startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }

            mDrawerLayout.closeDrawer(drawerView);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private class WeiboPagerAdapter extends FragmentStatePagerAdapter {

        public WeiboPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // 根据不同的Postion创建出不同的Fragment
            switch (position) {
                case 0: {
                    Fragment fragment = new TimelineFragment();
                    return fragment;
                }
                default: {
                    Fragment fragment = new TimelineFragment();
                    return fragment;
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        if( mDrawerLayout.isDrawerOpen(drawerView)){
            mDrawerLayout.closeDrawer(drawerView);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        switch (item.getItemId()) {
            case R.id.action_settings:
                // 新增一条微博试试
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
