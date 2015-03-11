package com.think_different.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.costum.android.widget.LoadMoreListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.melnykov.fab.FloatingActionButton;
import com.think_different.Constants;
import com.think_different.R;
import com.think_different.activities.SendWeibo;
import com.think_different.adapter.WeiBoCursorAdapter;
import com.think_different.adapter.WeiboAdapter;
import com.think_different.adapter.WeiboContentAdapter;
import com.think_different.customviews.SendWeiboView;
import com.think_different.dao.StatusHelper;
import com.think_different.dao.WeiboBaseHelper;
import com.think_different.dao.WeiboDataBase;
import com.think_different.dao.WeiboProvider;
import com.think_different.javabean.Statuse;
import com.think_different.javabean.User;
import com.think_different.javabean.WeiboReceive;
import com.think_different.network.HttpUtility;
import com.think_different.network.WeiBoAPI;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;
import com.think_different.utility.TimeUtility;
import com.think_different.utility.WeiboParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oceancx on 15/3/4.
 */
public class TimelineFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {

    // 用来发送HTTP请求
    private AsyncHttpClient client;

    //发送微博的自定义视图
    private SendWeiboView mSendWeiboView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeiboAdapter weiboAdapter;
    private WeiBoCursorAdapter weiBoCursorAdapter;


    private ListView mListView;
    private FloatingActionButton mFab;

    private View mRootView;

    private final int REQCODE_SENDWEIBO = 0x1;
    private final int LOADER_TIMELINE = 0x1;

    private StatusHelper statusHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null)
            mRootView = inflater.inflate(R.layout.timeline_fragment, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        client = new AsyncHttpClient();

        // mSendWeiboView = (SendWeiboView) LayoutInflater.from(getActivity()).inflate(R.layout.sendweibo_view, null).findViewById(R.id.SendWeiboView);

        mListView = (ListView) mRootView.findViewById(R.id.lv);

        weiboAdapter = new WeiboAdapter(getActivity());

        mListView.setDividerHeight(0);
        //mListView.addHeaderView(mSendWeiboView);
        weiBoCursorAdapter = new WeiBoCursorAdapter(getActivity(), null, true);
        mListView.setAdapter(weiBoCursorAdapter);


        mFab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        mFab.attachToListView(mListView);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SendWeibo.class);
                startActivityForResult(i, REQCODE_SENDWEIBO);
            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        statusHelper = new StatusHelper(getActivity());

        getLoaderManager().initLoader(LOADER_TIMELINE, null, this);

        // 初步调用Loader成功
//        Cursor c= getActivity().getContentResolver().query(WeiboProvider.STATUSES_CONTENT_URI,new String[]{WeiboDataBase.COL_STATUS_JSON},
//                "_id>0",null,null);
//        while (c.moveToNext()){
//            DebugLog.e(c.getString(0));
//        }

    }


    // 下拉刷新暂时做到这里
    public void onRefresh() {
        /*每刷新一次 就从服务器那里取出前20条微博
        * 新开启一个线程 在里面取数据 等数据取到后 用handler发送消息 通知执行完毕 取消刷新
        * */
        new Thread() {
            @Override
            public void run() {
                WeiboParameters parameters = new WeiboParameters();
                parameters.put("access_token", Preferences.getInstance(getActivity()).getAcessToken());

                String json = null;

                try {
                    json = HttpUtility.doRequest(WeiBoAPI.GET_HOME_TIMELINE, parameters, HttpUtility.GET);
                    Gson gson = new Gson();
                    int i = 0;
                    WeiboReceive weibo = gson.fromJson(json, WeiboReceive.class);

                    ArrayList<Statuse> statuses = weibo.getStatuses();

                    for (Statuse statuse : weibo.getStatuses()) {

                        // 查询是否有此id 和 curUser的微博
                        Cursor c = statusHelper.cQuery(new String[]{WeiboDataBase.COL_STATUS_IDSTR},
                                WeiboDataBase.COL_STATUS_IDSTR + " = ? and " + WeiboDataBase.COL_STATUS_OWNER + " = ?",
                                new String[]{statuse.getIdstr(), Constants.user.getIdstr()},
                                null
                        );
                        if (c.moveToNext()) {
                            c.close();
                        } else {
                            Gson gson1 = new Gson();
                            //DebugLog.e(i + "   " + gson1.toJson(statuse));
                            ContentValues cv = new ContentValues();
                            cv.put(WeiboDataBase.COL_STATUS_JSON, gson1.toJson(statuse));
                            cv.put(WeiboDataBase.COL_STATUS_OWNER, Constants.user.getIdstr());
                            cv.put(WeiboDataBase.COL_STATUS_CREATE_AT, TimeUtility.toLong(statuse.getCreated_at()));
                            cv.put(WeiboDataBase.COL_STATUS_IDSTR,statuse.getIdstr());
                            TimeUtility.toLong(statuse.getCreated_at());
                            // 插入的时候 要先判断 是否已经有了  如果有,就不插入
                            //statusHelper
                            statusHelper.cvInsert(cv);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x11);
            }
        }.start();
    }

//    private class LoadDataTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if (isCancelled()) {
//                return null;
//            }
//            try {
//  try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            mListView.onLoadMoreComplete();
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQCODE_SENDWEIBO:
                DebugLog.e("微博发送成功");
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x11:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }

        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 创建Loader

        return new CursorLoader(Constants.appContext,
                // URI
                WeiboProvider.STATUSES_CONTENT_URI,
                // Projection
                new String[]{BaseColumns._ID, WeiboDataBase.COL_STATUS_JSON},
                // Selection 找到微博的作者
                null,
                null,
                WeiboDataBase.COL_STATUS_CREATE_AT+" DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        weiBoCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        weiBoCursorAdapter.changeCursor(null);
    }
}
