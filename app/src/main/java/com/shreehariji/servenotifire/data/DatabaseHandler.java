package com.shreehariji.servenotifire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServerDAO.QUERY_SERVER_TABLE_CREATE);
        db.execSQL(WidgetDAO.QUERY_WIDGET_TABLE_CREATE);
        db.execSQL(LogDAO.QUERY_LOG_TABLE_CREATE);
        db.execSQL(LogDAO.QUERY_LOG_CREATE_TIMESTAMP_INDEX);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
