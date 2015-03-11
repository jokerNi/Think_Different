package com.think_different.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by oceancx on 15/3/11.
 */
public class StatusHelper extends WeiboBaseHelper {

    public StatusHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return WeiboProvider.STATUSES_CONTENT_URI;
    }


    public void cvInsert(ContentValues cv){
         insert(cv);
    }

    public void delete(){

    }

    public Cursor cQuery(String[] projection, String selection,
                      String[] selectionArgs, String sortOrder){
        return query(projection,selection,selectionArgs,sortOrder);
    }



}
