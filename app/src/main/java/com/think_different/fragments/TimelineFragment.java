package com.think_different.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costum.android.widget.LoadMoreListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.melnykov.fab.FloatingActionButton;
import com.think_different.R;
import com.think_different.adapter.WeiboContentAdapter;
import com.think_different.customviews.SendWeiboView;
import com.think_different.dao.WeiboDataBase;
import com.think_different.javabean.Statuse;
import com.think_different.javabean.WeiboReceive;
import com.think_different.network.HttpUtility;
import com.think_different.network.WeiBoAPI;
import com.think_different.preferences.Preferences;
import com.think_different.utility.DebugLog;
import com.think_different.utility.WeiboParameters;

/**
 * Created by oceancx on 15/3/4.
 */
public class TimelineFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // 用来发送HTTP请求
    private AsyncHttpClient client;

    //发送微博的自定义视图
    private SendWeiboView mSendWeiboView;

    private LoadMoreListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeiboContentAdapter weiboAdapter;

    private SQLiteDatabase db;
    private WeiboDataBase db_helper;

    private RecyclerView mRecyclerView;

    private View mRootView;


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
//        mListView = (LoadMoreListView) getView().findViewById(R.id.main_lv);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        weiboAdapter = new WeiboContentAdapter(getActivity());
        mRecyclerView.setAdapter(weiboAdapter);


        //mListView.addHeaderView(mSendWeiboView);


        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);


        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                /**
//                 * 这里添加刷新的功能 下拉更新 从服务器取数据 得到一个时间区间 然后把这个区间入棧 然后用这个区间的时间段 去更新list
//                 * 同时改变这个区间的值
//                 */
//                WeiboParameters parameters = new WeiboParameters();
//                parameters.put("access_token", "2.00USFdDDU2U6gD026529ffc08LVsjB");
//                String json = null;
//
//                try {
//                    json = HttpUtility.doRequest(WeiBoAPI.GET_HOME_TIMELINE, parameters, HttpUtility.GET);
//                    Gson gson = new Gson();
//                    WeiboReceive weibo= gson.fromJson(json, WeiboReceive.class);
//                    for(Statuse statuse  : weibo.getStatuses()){
//                        DebugLog.e(statuse.getText());
//                    }
//                    DebugLog.e(weibo.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//           //     DebugLog.e(json);
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//
//                }
//                refreshableView.finishRefreshing();
//            }
//        }

        //            , 0);
        // 底部加载设置监听器
//        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                LoadDataTask task = new LoadDataTask();
//                task.execute();
//
//            }
//        });
        db_helper=new WeiboDataBase(getActivity());

        db=db_helper.getWritableDatabase();
    }


    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        }, 5000);

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


                    db.beginTransaction();
                    try {
                        for (Statuse statuse : weibo.getStatuses()) {
                            Gson gson1 = new Gson();
                            DebugLog.e(i + "   " + gson1.toJson(statuse));
                            ContentValues cv = new ContentValues();
                            cv.put(WeiboDataBase.COL_STATUS_TEXT, gson1.toJson(statuse));
                            db.insert(WeiboDataBase.TABLE_STATUS, null, cv);
                        }

                        DebugLog.e("插入成功一次");
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                        db.close();
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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    mSwipeRefreshLayout.setRefreshing(false);
                    weiboAdapter.restart();
                    weiboAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };
}
