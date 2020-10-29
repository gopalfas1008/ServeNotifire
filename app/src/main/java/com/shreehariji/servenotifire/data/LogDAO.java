package com.shreehariji.servenotifire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LogDAO extends DAOBase {
    public static final String KEY = "log_id";
    public static final String LOG_TABLE_NAME = "log";
    public static final String SERVER_ID = "server_id";
    public static final String LOG_TEXT = "log_text";
    public static final String LOG_TIMESTAMP = "log_timestamp";
    public static final String QUERY_LOG_CREATE_TIMESTAMP_INDEX = "CREATE INDEX index_timestamp ON log (log_timestamp);";
    public static final String QUERY_LOG_TABLE_CREATE = "CREATE TABLE log (log_id INTEGER PRIMARY KEY,server_id INTEGER,log_text TEXT,log_timestamp INTEGER);";

    public LogDAO(Context pContext) {
        super(pContext);
    }

    public long add(int serverid,String logText) {
        ContentValues values = new ContentValues();
        values.put(SERVER_ID, serverid);
        values.put(LOG_TEXT, logText);
        values.put(LOG_TIMESTAMP, System.currentTimeMillis() / 1000);
        return this.mDb.insert(LOG_TABLE_NAME, null, values);
    }

    public void deleteOld(Integer log_depth) {
        this.mDb.delete(LOG_TABLE_NAME, "log_timestamp < ?", new String[]{String.valueOf(System.currentTimeMillis() / 1000 - ((long) (log_depth * 3600)))});
    }

    public void deleteAll() {
        this.mDb.delete(LOG_TABLE_NAME, null, null);
    }

    public Cursor selectAll(int server_id) {
        if(server_id==0) {
            return this.mDb.rawQuery("select log_id as _id, log_text,log_timestamp from log order by log_timestamp desc", null);
        }else {
            return this.mDb.rawQuery("select log_id as _id, log_text,log_timestamp from log where server_id="+server_id+"  order by log_timestamp desc", null);
        }

    }
}
