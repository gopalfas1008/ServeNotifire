package com.shreehariji.servenotifire.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.activity.LogsActivity;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import java.util.Date;

public class ServerListCursorAdapter extends ResourceCursorAdapter {
    public static final int LAYOUT_ID = 2130968606;

    public ServerListCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.activity_server_list_list_entry, cursor);
    }

    public View newView(Context _context, Cursor cur, ViewGroup parent) {
        return ((LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_server_list_list_entry, parent, false);
    }

    public void bindView(View view, final Context context, Cursor cursor) {
        final Server server = ServerDAO.cursorToServer(cursor);
        ((TextView) view.findViewById(R.id.server_list_id)).setText(server.getId().toString());
        TextView txtServerName = view.findViewById(R.id.server_list_name);
        txtServerName.setText(server.getName());
        txtServerName.setTextColor(server.getDisabled() ? -7829368 : ViewCompat.MEASURED_STATE_MASK);
        TextView txtServerAddress = view.findViewById(R.id.server_list_address);
        txtServerAddress.setText(server.getAddress());
        txtServerAddress.setTextColor(server.getDisabled() ? -7829368 : ViewCompat.MEASURED_STATE_MASK);
        ((TextView) view.findViewById(R.id.server_list_disabled)).setText(server.getDisabled().toString());
        ((ImageView) view.findViewById(R.id.server_list_picture)).setColorFilter(server.getDisabled() ? -7829368 : ViewCompat.MEASURED_STATE_MASK);
        ImageView imgServerCheckResult = view.findViewById(R.id.server_list_check_result);
        ImageView ic_log = view.findViewById(R.id.ic_log);
        ic_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LogsActivity.class).putExtra("server_id",server.getId()));
            }
        });
        switch (server.getCheckStatus()) {
            case 0:
                imgServerCheckResult.setImageResource(R.drawable.ic_close_24dp);
                break;
            case 1:
                imgServerCheckResult.setImageResource(R.drawable.ic_check_24dp);
                break;
            case 2:
                imgServerCheckResult.setImageResource(R.drawable.ic_autorenew_24dp);
                break;
            default:
                imgServerCheckResult.setImageResource(R.drawable.question);
                break;
        }
        TextView txtServerCheckTimestamp = view.findViewById(R.id.server_list_check_timestamp);
        Date date = new Date(server.getCheckTimestamp().longValue() * 1000);
        txtServerCheckTimestamp.setText(DateFormat.getDateFormat(context).format(date) + "\n" + DateFormat.getTimeFormat(context).format(date));
        txtServerCheckTimestamp.setTextColor(server.getDisabled().booleanValue() ? -7829368 : ViewCompat.MEASURED_STATE_MASK);
    }
}
