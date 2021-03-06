package com.think_different.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.think_different.Constants;
import com.think_different.activities.HomePageActivity;
import com.think_different.utility.DebugLog;

/**
 * Created by oceancx on 15/3/11.
 */
public class WeiboProvider extends ContentProvider {


    public static final Object DBLock = new Object();

    public static final String AUTHORITY = "com.think_different.app.provider";

    public static final String SCHEME = "content://";

    public static final String PATH_STATUSES = "/statuses";
    public static final String PATH_USERS = "/users";

    public static final Uri STATUSES_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
            + PATH_STATUSES);
    public static final Uri USERS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_USERS);

    private static final int STATUSES = 1;
    private static final int USERS = 10;
    /*
     * MIME type definitions
     */
    public static final String STATUSES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.think_different.app.statuses";
    public static final String USERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.think_different.app.users";

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "statuses", STATUSES);
        sUriMatcher.addURI(AUTHORITY, "users", USERS);
    }

    private static WeiboDataBase mDBHelper;

    public static WeiboDataBase getDBHelper( ) {
        if (mDBHelper == null) {
            mDBHelper =  WeiboDataBase.getInstance(Constants.appContext);
        }
        return mDBHelper;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case STATUSES:
                return STATUSES_CONTENT_TYPE;
            case USERS:
                return USERS_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    private static String matchTable(Uri uri) {
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case STATUSES:
                table = WeiboDataBase.TABLE_STATUS;
                break;
            case USERS:
                table = WeiboDataBase.TABLE_USER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        synchronized (DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);

            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, // The database to  queryFromDB
                    projection, // The columns to return from the queryFromDB
                    selection, // The columns for the where clause
                    selectionArgs, // The values for the where clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    sortOrder // The sort order
            );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws SQLException {
        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                // getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            db.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    db.insertWithOnConflict(table, BaseColumns._ID,
                            contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                return values.length;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count = 0;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }


}
