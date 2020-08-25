package com.shreehariji.servenotifire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected static final String NOM = "ServerOfflineNotifier.db";
    protected static final int VERSION = 1;
    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, 1);
    }

    public SQLiteDatabase open() {
        this.mDb = this.mHandler.getWritableDatabase();
        return this.mDb;
    }

    public void close() {
        this.mDb.close();
    }

    public SQLiteDatabase getDb() {
        return this.mDb;
    }

    public static void resetAllTables(Context context) {
        SQLiteDatabase _mDb = new DatabaseHandler(context, NOM, null, 1).getWritableDatabase();
        _mDb.execSQL("DROP TABLE IF EXISTS server");
        _mDb.execSQL(ServerDAO.QUERY_SERVER_TABLE_CREATE);
        _mDb.execSQL("DROP TABLE IF EXISTS widget");
        _mDb.execSQL(WidgetDAO.QUERY_WIDGET_TABLE_CREATE);
        _mDb.execSQL("DROP TABLE IF EXISTS log");
        _mDb.execSQL(LogDAO.QUERY_LOG_TABLE_CREATE);
    }
}
