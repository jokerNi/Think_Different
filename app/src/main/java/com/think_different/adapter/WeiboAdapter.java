package com.think_different.adapter;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.think_different.Constants;
import com.think_different.R;
import com.think_different.dao.WeiboDataBase;
import com.think_different.fragments.TimelineFragment;
import com.think_different.javabean.Statuse;
import com.think_different.utility.DebugLog;
import com.think_different.utility.ImageLoader;
import com.think_different.utility.TimeUtility;
import com.think_different.utility.Utility;

import org.apache.http.Header;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by oceancx on 15/3/7.
 */
public class WeiboAdapter extends BaseAdapter {


    private Context mContext;

    private List<Statuse> Statuses;
    private WeiboDataBase weiboDataBase;
    private SQLiteDatabase db;
    private ImageLoader mImageLoader;


    public WeiboAdapter(Context context)
    {
        mContext =context;
        init();
    }

    private void init() {
        mImageLoader=ImageLoader.getInstance();

        Statuses=new ArrayList<Statuse>();
        weiboDataBase=new WeiboDataBase(mContext);
        db=weiboDataBase.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from "+ WeiboDataBase.TABLE_STATUS ,null);
        while (cursor.moveToNext()){
            String statusJson = cursor.getString(2);
            Gson gson=new Gson();
            Statuse st= gson.fromJson(statusJson,Statuse.class);
            Statuses.add(st);
        }
    }

    public void restart(){
        Statuses.clear();
        Cursor cursor =db.rawQuery("select * from "+ WeiboDataBase.TABLE_STATUS ,null);
        while (cursor.moveToNext()){
            String statusJson = cursor.getString(2);
            Gson gson=new Gson();
            Statuse st= gson.fromJson(statusJson,Statuse.class);
            Statuses.add(st);
        }

    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        Statuse statues=Statuses.get(position);
        holder.vName.setText(statues.getUser().getName());
        holder.vContent.setText(statues.getText());
        setAvatar(holder,statues);
        holder.vDate.setText(TimeUtility.Description(statues.getCreated_at()));
        holder.vFrom.setText(statues.getSource());
        holder.vAttitudes.setText(statues.getAttitudes_count());
        holder.vRetweets.setText(statues.getReposts_count());
        holder.vCommites.setText(statues.getComments_count());
        if(statues.getRetweeted_status()!=null ){
            holder.oLayout.setVisibility(View.VISIBLE);
            holder.oContent.setText(statues.getRetweeted_status().getText());
        }else {
            holder.oLayout.setVisibility(View.GONE);
        }
    }


    public class ViewHolder  {

        public ViewHolder(View v) {
            vName = (TextView) v.findViewById(R.id.weibo_name);
            vContent = (TextView) v.findViewById(R.id.weibo_content);
            vAvatar = (CircleImageView) v.findViewById(R.id.weibo_avatar);
            vDate = (TextView) v.findViewById(R.id.weibo_date);
            vFrom = (TextView) v.findViewById(R.id.weibo_from);
            vAttitudes = (TextView) v.findViewById(R.id.weibo_attitudes);
            vRetweets = (TextView) v.findViewById(R.id.weibo_retweet);
            vCommites = (TextView) v.findViewById(R.id.weibo_comments);
            oLayout = (RelativeLayout) v.findViewById(R.id.weibo_origin);
            oContent = (TextView) v.findViewById(R.id.weibo_orig_content);
        }
        protected TextView vName;
        protected TextView vContent;
        protected CircleImageView vAvatar;
        protected TextView vDate;
        protected TextView vFrom;
        protected TextView vAttitudes;
        protected TextView vRetweets;
        protected TextView vCommites;
        protected RelativeLayout oLayout;
        protected TextView oContent;

    }



    private void setAvatar(ViewHolder holder, final Statuse statuse) {
        /**
         * 首先根据图片地址找在SD卡中寻找图片
         * 如果找不到 就从网上下载
         */
        final String idstr= statuse.getUser().getIdstr(); //用用户的idstr当做文件名字保存文件
        Bitmap bitmap= mImageLoader.getBitmapFromCache(idstr);
        if( bitmap != null){
            holder.vAvatar.setImageBitmap(bitmap);
        }else {
            final File f = new File(Utility.getAvatarDir()+ "/"+idstr+".jpg");
            if(!f.exists()){
                // 如果用户头像图片不存在 ,那么就从网下下载头像
                DebugLog.e(statuse.getUser().getAvatar_large());
                DownLoadAvatar(holder, statuse.getUser().getAvatar_large(),idstr);
//                new Thread(){
//                    @Override
//                    public void run() {
//                        downloadImage(statuse.getUser().getAvatar_large(),idstr);
//                    }
//                }.start();
            }else {
                // 先把文件从file中解析出来 然后加入到缓存 然后在进行设置
                DebugLog.e("f.abs path:"+ f.getAbsolutePath());
                bitmap= mImageLoader.decodeBitmapFromFile(f.getAbsolutePath(),Utility.dip2px(36), Utility.dip2px(36) );

                if(bitmap !=  null ) {
                    mImageLoader.addToImageCache(idstr, bitmap);
                    holder.vAvatar.setImageBitmap(bitmap);
                }else {
                    DebugLog.e("我是空得");
                }
            }
        }
    }

    private void DownLoadAvatar(ViewHolder holder, String url, final String idstr) {
        /**
         * 外开一个线程下载图片
         */
        File f=new File(Utility.getAvatarDir() + "/" + idstr+".jpg");
        if( !f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return;
        }


        AsyncHttpClient client= new AsyncHttpClient();

        client.get(url, null, new FileAsyncHttpResponseHandler(f) {

            @Override
            public void onSuccess(int arg0, Header[] arg1, File arg2) {
                DebugLog.e("下载单张图片ok");
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2,
                                  File arg3) {

            }
        });

    }

    private void downloadImage(String imageUrl,String path) {
        DebugLog.e(imageUrl+"       "+path);

        HttpURLConnection con = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        File imageFile = null;

        try {
            URL url = new URL(imageUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setReadTimeout(15 * 1000);
            con.setDoInput(true);
            con.setDoOutput(true);
            bis = new BufferedInputStream(con.getInputStream());
            imageFile = new File(Utility.getAvatarDir() +"/"+path+"123.jpg");
            fos = new FileOutputStream(imageFile);
            bos = new BufferedOutputStream(fos);
            byte[] b = new byte[1024];
            int length;
            while ((length = bis.read(b)) != -1) {
                bos.write(b, 0, length);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if (imageFile != null) {
//            Bitmap bitmap = mImageLoader.decodeBitmapFromFile(
//                    imageFile.getPath(), Utility.dip2px(36), Utility.dip2px(36));
//            if (bitmap != null) {
//                mImageLoader.addToImageCache(imageUrl, bitmap);
//            }
//        }
    }

    @Override
    public int getCount() {
        return Statuses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
       if(convertView == null){
           convertView = LayoutInflater.
                   from(parent.getContext()).
                   inflate(R.layout.weibo_view, parent, false);
           holder= new ViewHolder(convertView);
           convertView.setTag(holder);
       }else
       {
           holder= (ViewHolder) convertView.getTag();
       }
        onBindViewHolder(holder,position);
        return convertView;
    }
}
