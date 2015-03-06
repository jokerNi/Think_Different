package com.think_different.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.think_different.R;
import com.think_different.Weibo;
import com.think_different.activedroid.Item;
import com.think_different.dao.WeiboDataBase;
import com.think_different.javabean.Statuse;
import com.think_different.utility.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oceancx on 15/3/6.
 */
public class WeiboContentAdapter  extends RecyclerView.Adapter<WeiboContentAdapter.ViewHolder> {

    private Context mContext;

    private List<Statuse> Statuses;
    private WeiboDataBase weiboDataBase;
    private SQLiteDatabase db;


    public WeiboContentAdapter(Context context)
    {
        mContext =context;
        init();
    }

    private void init() {
        Statuses=new ArrayList<Statuse>();
        weiboDataBase=new WeiboDataBase(mContext);
        db=weiboDataBase.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from "+ WeiboDataBase.TABLE_STATUS ,null);
        while (cursor.moveToNext()){
            String statusJson = cursor.getString(1);
            Gson gson=new Gson();
            Statuse st= gson.fromJson(statusJson,Statuse.class);
            Statuses.add(st);
        }
    }

    public void restart(){
        Statuses.clear();
        Cursor cursor =db.rawQuery("select * from "+ WeiboDataBase.TABLE_STATUS ,null);
        while (cursor.moveToNext()){
            String statusJson = cursor.getString(1);
            Gson gson=new Gson();
            Statuse st= gson.fromJson(statusJson,Statuse.class);
            Statuses.add(st);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.weibo_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Statuse statues=Statuses.get(position);
        holder.vName.setText(statues.getUser().getName());
        holder.vContent.setText(statues.getText());
    }

    @Override
    public int getItemCount() {
        return Statuses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            vName= (TextView) itemView.findViewById(R.id.weibo_name);
            vContent=(TextView)itemView.findViewById(R.id.weibo_content);
        }



        protected TextView vName;
        protected TextView vContent;
    }

}
