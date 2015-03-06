package com.think_different.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.think_different.R;
import com.think_different.fragments.TimelineFragment;
import com.think_different.preferences.Preferences;

/**
 * Created by oceancx on 15/3/3.
 * <p/>
 * 此Activity为MainActivity
 */
public class HomePageActivity extends ActionBarActivity {
    /**
     * 这个类用于首页显示,如果用户还没有登陆,就提示用户进行登陆
     * <p/>
     * 有一个DrawerLayout 用于导航
     * 内容视图 包含一个FAB,一个SwipeToRefresh,RecylcerView+CardView
     */
    private Preferences preferences;

    private String[] mDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private ViewPager viewPager;
    private WeiboPagerAdapter mWeiboPagerAdapter;


    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preferences = Preferences.getInstance(this);

        setContentView(R.layout.homepage_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.platte_text_icon) );
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItemTitles = getResources().getStringArray(R.array.drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.title, mDrawerItemTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mWeiboPagerAdapter = new WeiboPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mWeiboPagerAdapter);

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
                mDrawerLayout.closeDrawer(mDrawerList);
            }
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
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new TimelineFragment();
                    break;
                default:
                    fragment = new TimelineFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
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


        return super.onOptionsItemSelected(item);
    }


}
