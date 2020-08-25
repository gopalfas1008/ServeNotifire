package com.shreehariji.servenotifire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LogDAO extends DAOBase {
    public static final String KEY = "log_id";
    public static final String LOG_TABLE_NAME = "log";
    public static final String LOG_TEXT = "log_text";
    public static final String LOG_TIMESTAMP = "log_timestamp";
    public static final String QUERY_LOG_CREATE_TIMESTAMP_INDEX = "CREATE INDEX index_timestamp ON log (log_timestamp);";
    public static final String QUERY_LOG_TABLE_CREATE = "CREATE TABLE log (log_id INTEGER PRIMARY KEY,log_text TEXT,log_timestamp INTEGER);";

    public LogDAO(Context pContext) {
        super(pContext);
    }

    public long add(String logText) {
        ContentValues values = new ContentValues();
        values.put(LOG_TEXT, logText);
        values.put(LOG_TIMESTAMP, Long.valueOf(System.currentTimeMillis() / 1000));
        return this.mDb.insert(LOG_TABLE_NAME, null, values);
    }

    public void deleteOld(Integer log_depth) {
        this.mDb.delete(LOG_TABLE_NAME, "log_timestamp < ?", new String[]{String.valueOf(Long.valueOf(System.currentTimeMillis() / 1000).longValue() - ((long) (log_depth.intValue() * 3600)))});
    }

    public void deleteAll() {
        this.mDb.delete(LOG_TABLE_NAME, null, null);
    }

    public Cursor selectAll() {
        return this.mDb.rawQuery("select log_id as _id, log_text,log_timestamp from log order by log_timestamp desc", null);
    }
}
