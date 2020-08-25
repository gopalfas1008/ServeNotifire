package com.shreehariji.servenotifire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class WidgetDAO extends DAOBase {
    public static final String KEY = "widget_id";
    public static final String QUERY_WIDGET_TABLE_CREATE = "CREATE TABLE widget (widget_id INTEGER PRIMARY KEY,server_id INTEGER);";
    public static final String SERVER_ID = "server_id";
    public static final String WIDGET_TABLE_NAME = "widget";

    public WidgetDAO(Context pContext) {
        super(pContext);
    }

    public long addWidget(Integer widgetId, Integer serverId) {
        ContentValues values = new ContentValues();
        values.put(KEY, widgetId);
        values.put("server_id", serverId);
        return this.mDb.insert(WIDGET_TABLE_NAME, null, values);
    }

    public void deleteWidget(Integer widgetId) {
        this.mDb.delete(WIDGET_TABLE_NAME, "widget_id = ?", new String[]{String.valueOf(widgetId)});
    }

    public void deleteServer(Integer serverId) {
        this.mDb.delete(WIDGET_TABLE_NAME, "server_id = ?", new String[]{String.valueOf(serverId)});
    }

    public Integer getServerOfWidget(Integer widgetId) {
        Cursor c = this.mDb.rawQuery("select server_id from widget where widget_id=?", new String[]{String.valueOf(widgetId)});
        c.moveToFirst();
        Integer serverId = Integer.valueOf(c.getInt(0));
        c.close();
        return serverId;
    }

    public Cursor getWidgetsOfServer(Integer serverId) {
        return this.mDb.rawQuery("select widget_id from widget where server_id=?", new String[]{String.valueOf(serverId)});
    }
}
