package com.shreehariji.servenotifire.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.shreehariji.servenotifire.R;
import java.util.Date;

public class LogsCursorAdapter extends ResourceCursorAdapter {
    public static final int LAYOUT_ID = 2130968602;

    public LogsCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.activity_logs_list_entry, cursor);
    }

    public View newView(Context _context, Cursor cur, ViewGroup parent) {
        return ((LayoutInflater) _context.getSystemService("layout_inflater")).inflate(R.layout.activity_logs_list_entry, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        Date date = new Date(cursor.getLong(2) * 1000);
        ((TextView) view.findViewById(R.id.logs_list_timestamp)).setText(DateFormat.getDateFormat(context).format(date) + " " + DateFormat.getTimeFormat(context).format(date));
        ((TextView) view.findViewById(R.id.logs_list_text)).setText(Html.fromHtml(cursor.getString(1)));
    }
}
