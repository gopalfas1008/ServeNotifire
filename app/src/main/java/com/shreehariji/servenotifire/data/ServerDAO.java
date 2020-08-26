package com.shreehariji.servenotifire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ServerDAO extends DAOBase {
    public static final String ADDRESS = "server_address";
    public static final String CHECK_FAIL_COUNT = "server_check_fail_count";
    public static final String CHECK_FAIL_THRESHOLD = "server_check_fail_threshold";
    public static final String CHECK_INTERVAL = "server_check_interval";
    public static final String CHECK_STATUS = "server_check_status";
    public static final String CHECK_TIMESTAMP = "server_check_timestamp";
    public static final String DISABLED = "server_disabled";
    public static final String KEY = "server_id";
    public static final String KEY_SELECT = "_id";
    public static final String NAME = "server_name";
    public static final String QUERY_SERVER_TABLE_CREATE = "CREATE TABLE server (server_id INTEGER PRIMARY KEY AUTOINCREMENT,server_name VARCHAR(255),server_address TEXT,server_check_interval INTEGER,server_check_fail_threshold INTEGER,server_check_fail_count INTEGER,server_check_status INTEGER,server_check_timestamp INTEGER,server_disabled BOOLEAN);";
    public static final String SERVER_TABLE_NAME = "server";

    public ServerDAO(Context pContext) {
        super(pContext);
    }

    public long add(Server s) {
        ContentValues values = new ContentValues();
        values.put(NAME, s.getName());
        values.put(ADDRESS, s.getAddress());
        values.put(CHECK_INTERVAL, s.getCheckInterval());
        values.put(CHECK_FAIL_THRESHOLD, s.getCheckFailThreshold());
        values.put(CHECK_FAIL_COUNT, s.getCheckFailCount());
        values.put(CHECK_STATUS, s.getCheckStatus());
        values.put(CHECK_TIMESTAMP, s.getCheckTimestamp());
        values.put(DISABLED, s.getDisabled().booleanValue() ? "1" : "0");
        return this.mDb.insert(SERVER_TABLE_NAME, null, values);
    }

    public void delete(long id) {
        this.mDb.delete(SERVER_TABLE_NAME, "server_id = ?", new String[]{String.valueOf(id)});
    }

    public void update(Server s) {
        ContentValues values = new ContentValues();
        values.put(NAME, s.getName());
        values.put(ADDRESS, s.getAddress());
        values.put(CHECK_INTERVAL, s.getCheckInterval());
        values.put(CHECK_FAIL_THRESHOLD, s.getCheckFailThreshold());
        values.put(CHECK_FAIL_COUNT, s.getCheckFailCount());
        values.put(CHECK_STATUS, s.getCheckStatus());
        values.put(CHECK_TIMESTAMP, s.getCheckTimestamp());
        values.put(DISABLED, s.getDisabled().booleanValue() ? "1" : "0");
        this.mDb.update(SERVER_TABLE_NAME, values, "server_id = ?", new String[]{String.valueOf(s.getId())});
    }

    public Server select(Integer id) {
        Cursor c = this.mDb.rawQuery("select server_id,server_name,server_address,server_check_interval,server_check_fail_threshold,server_check_fail_count,server_check_status,server_check_timestamp,server_disabled from server where server_id=?", new String[]{String.valueOf(id)});
        c.moveToFirst();
        Server s = cursorToServer(c);
        c.close();
        return s;
    }

    public Cursor selectAll() {
        return this.mDb.rawQuery("select server_id AS _id,server_name,server_address,server_check_interval,server_check_fail_threshold,server_check_fail_count,server_check_status,server_check_timestamp,server_disabled from server", null);
    }

    public Cursor selectAllEnabled() {
        return this.mDb.rawQuery("select server_id AS _id,server_name,server_address,server_check_interval,server_check_fail_threshold,server_check_fail_count,server_check_status,server_check_timestamp,server_disabled from server where server_disabled=0", null);
    }

    public static Server cursorToServer(Cursor cursor) {
        boolean z = true;
        Integer valueOf = cursor.getInt(0);
        String string = cursor.getString(1);
        String string2 = cursor.getString(2);
        Integer valueOf2 = cursor.getInt(3);
        Integer valueOf3 = cursor.getInt(4);
        Integer valueOf4 = cursor.getInt(5);
        Integer valueOf5 = cursor.getInt(6);
        Long valueOf6 = cursor.getLong(7);
        if (cursor.getInt(8) != 1) {
            z = false;
        }
        return new Server(valueOf, string, string2, valueOf2, valueOf3, valueOf4, valueOf5, valueOf6, Boolean.valueOf(z));
    }
}
