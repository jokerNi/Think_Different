package com.think_different.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by oceancx on 15/3/11.
 * Helper基本类
 */
public abstract class WeiboBaseHelper {
    private Context mContext;

    public WeiboBaseHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract Uri getContentUri();

    public void notifyChange() {
        mContext.getContentResolver().notifyChange(getContentUri(), null);
    }

    protected final Cursor query(Uri uri, String[] projection,
                                 String selection, String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection,
                selectionArgs, sortOrder);
    }

    protected final Cursor query(String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection,
                selection, selectionArgs, sortOrder);
    }

    protected final Uri insert(ContentValues values) {
        Uri uri = mContext.getContentResolver().insert(getContentUri(), values);
        // mContext.getContentResolver().notify();
        notifyChange();


        return uri;
    }

    protected int bulkInsert(ContentValues[] values) {
        int row = mContext.getContentResolver().bulkInsert(getContentUri(),
                values);

        //	notifyChange();

        return row;
    }

    protected int update(ContentValues values, String where, String[] whereArgs) {
        return mContext.getContentResolver().update(getContentUri(), values,
                where, whereArgs);
    }

    protected int delete(Uri uri, String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(getContentUri(), selection,
                selectionArgs);
    }

    protected Cursor getList(String[] projection, String selection,
                             String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection,
                selection, selectionArgs, sortOrder);
    }

    public CursorLoader getCursorLoader(Context context) {
        return getCursorLoader(context, null, null, null, null);
    }

    protected final CursorLoader getCursorLoader(Context context,
                                                 String[] projection, String selection, String[] selectionArgs,
                                                 String sortOrder) {
        return new CursorLoader(context, getContentUri(), projection,
                selection, selectionArgs, sortOrder);
    }


}
