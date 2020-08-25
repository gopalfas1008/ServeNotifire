package com.shreehariji.servenotifire.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.adapter.ServerListCursorAdapter;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.data.WidgetDAO;
import com.shreehariji.servenotifire.widget.OneServerWidgetProvider;

public class ServerSelectActivity extends AppCompatActivity {
    int mAppWidgetId = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(0);
        setContentView((int) R.layout.activity_server_select);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mAppWidgetId = extras.getInt("appWidgetId", 0);
        }
        if (this.mAppWidgetId == 0) {
            finish();
            return;
        }
        ServerDAO serverDAO = new ServerDAO(this);
        serverDAO.open();
        Cursor cursor = serverDAO.selectAll();
        if (cursor.getCount() > 0) {
            findViewById(R.id.lblEmptyList).setVisibility(8);
            ListView serverListView = (ListView) findViewById(R.id.server_list);
            serverListView.setAdapter(new ServerListCursorAdapter(this, cursor));
            serverListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Integer server_id = Integer.valueOf(((TextView) view.findViewById(R.id.server_list_id)).getText().toString());
                    WidgetDAO widgetDAO = new WidgetDAO(ServerSelectActivity.this.getApplicationContext());
                    widgetDAO.open();
                    widgetDAO.addWidget(Integer.valueOf(ServerSelectActivity.this.mAppWidgetId), server_id);
                    widgetDAO.close();
                    Intent updateIntent = new Intent(ServerSelectActivity.this.getApplicationContext(), OneServerWidgetProvider.class);
                    updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    updateIntent.putExtra("appWidgetIds", new int[]{ServerSelectActivity.this.mAppWidgetId});
                    ServerSelectActivity.this.sendBroadcast(updateIntent);
                    Intent resultValue = new Intent();
                    resultValue.putExtra("appWidgetId", ServerSelectActivity.this.mAppWidgetId);
                    ServerSelectActivity.this.setResult(-1, resultValue);
                    ServerSelectActivity.this.finish();
                }
            });
        }
        serverDAO.close();
    }
}
