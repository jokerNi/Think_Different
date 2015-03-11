package com.think_different.dao;

import android.content.Context;
import android.net.Uri;

/**
 * Created by oceancx on 15/3/11.
 */
public class UserHelper extends WeiboBaseHelper {


    public UserHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return WeiboProvider.STATUSES_CONTENT_URI;
    }
}
