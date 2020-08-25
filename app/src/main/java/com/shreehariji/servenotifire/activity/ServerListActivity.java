package com.shreehariji.servenotifire.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.adapter.ServerListCursorAdapter;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.data.WidgetDAO;
import com.shreehariji.servenotifire.tools.ServerAutoCheckerScheduler;
import com.shreehariji.servenotifire.tools.ServerChecker;

@SuppressLint("WrongConstant")
public class ServerListActivity extends AppCompatActivity {
    BroadcastReceiver serverListReceiver;

    private static class CONTEXT_MENU_OPTIONS {
        public static final int CHECK = 1;
        public static final int DELETE = 3;
        public static final int DISABLE = 4;
        public static final int EDIT = 2;

        private CONTEXT_MENU_OPTIONS() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_server_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((FloatingActionButton) findViewById(R.id.addServer)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ServerListActivity.this.getApplicationContext(), ServerEditorActivity.class);
                intent.putExtra(ServerEditorActivity.INTENT_KEYS.ACTION, ServerEditorActivity.ACTIONS.ADD);
                ServerListActivity.this.startActivityForResult(intent, 1);
            }
        });
        loadServerList();
        registerUpdateReceiver();
        disableBatteryOptimization();
    }

    private void registerUpdateReceiver() {
        this.serverListReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.i("ServerListReceiver", "loadServerList()");
                ServerListActivity.this.loadServerList();
            }
        };
        registerReceiver(this.serverListReceiver, new IntentFilter("com.com.shreehariji.servenotifire.UPDATE_SERVER_LIST"));
    }

    public void onDestroy() {
        unregisterReceiver(this.serverListReceiver);
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings /*2131624101*/:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_show_logs /*2131624102*/:
                startActivity(new Intent(this, LogsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1 && intent != null) {
            new ServerChecker(Integer.valueOf(intent.getIntExtra("SERVER_ID", -1)), this).execute(new Void[0]);
        }
    }

    public void loadServerList() {
        ServerDAO serverDAO = new ServerDAO(this);
        serverDAO.open();
        Cursor cursor = serverDAO.selectAll();
        ListView serverListView = (ListView) findViewById(R.id.server_list);
        serverListView.setAdapter(new ServerListCursorAdapter(this, cursor));
        serverDAO.close();
        registerForContextMenu(serverListView);
        serverListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.showContextMenu();
            }
        });
        findViewById(R.id.lblEmptyList).setVisibility(cursor.getCount() > 0 ? 8 : 0);
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        TextView serverDisabledView = (TextView) info.targetView.findViewById(R.id.server_list_disabled);
        menu.setHeaderTitle(((TextView) info.targetView.findViewById(R.id.server_list_name)).getText());
        menu.add(1, 1, 1, R.string.activity_server_list_check_now);
        menu.add(1, 2, 2, R.string.activity_server_list_edit);
        menu.add(1, 3, 3, R.string.activity_server_list_delete);
        menu.add(1, 4, 4, Boolean.parseBoolean(serverDisabledView.getText().toString()) ? R.string.activity_server_list_enable : R.string.activity_server_list_disable);
    }

    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        Integer clickedServerId = Integer.valueOf(((TextView) ((AdapterContextMenuInfo) item.getMenuInfo()).targetView.findViewById(R.id.server_list_id)).getText().toString());
        switch (item.getItemId()) {
            case 1:
                new ServerChecker(clickedServerId, this).execute(new Void[0]);
                return true;
            case 2:
                Intent intent = new Intent(this, ServerEditorActivity.class);
                intent.putExtra(ServerEditorActivity.INTENT_KEYS.ACTION, ServerEditorActivity.ACTIONS.UPDATE);
                intent.putExtra("SERVER_ID", clickedServerId);
                startActivityForResult(intent, 1);
                return true;
            case 3:
                ServerDAO serverDAO_delete = new ServerDAO(this);
                serverDAO_delete.open();
                Server server_delete = serverDAO_delete.select(clickedServerId);
                serverDAO_delete.delete((long) clickedServerId.intValue());
                serverDAO_delete.close();
                ServerAutoCheckerScheduler.DeleteAlarm(getApplicationContext(), clickedServerId);
                WidgetDAO widgetDAO = new WidgetDAO(this);
                widgetDAO.open();
                widgetDAO.deleteServer(clickedServerId);
                widgetDAO.close();
                Toast.makeText(this, getString(R.string.activity_server_editor_toast_delete, new Object[]{server_delete.getName()}), 1).show();
                loadServerList();
                return true;
            case 4:
                ServerDAO serverDAO_disable = new ServerDAO(this);
                serverDAO_disable.open();
                Server server_disable = serverDAO_disable.select(clickedServerId);
                server_disable.setDisabled(Boolean.valueOf(!server_disable.getDisabled().booleanValue()));
                serverDAO_disable.update(server_disable);
                serverDAO_disable.close();
                if (server_disable.getDisabled().booleanValue()) {
                    ServerAutoCheckerScheduler.DeleteAlarm(getApplicationContext(), server_disable.getId());
                } else {
                    ServerAutoCheckerScheduler.AddAlarm(getApplicationContext(), server_disable);
                }
                loadServerList();
                return true;
            default:
                return false;
        }
    }


    private void disableBatteryOptimization() {
        if (VERSION.SDK_INT >= 23) {
            String pkg = getPackageName();
            if (!((PowerManager) getSystemService(PowerManager.class)).isIgnoringBatteryOptimizations(pkg)) {
                Intent i = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                i.setData(Uri.parse("package:" + pkg));
                startActivity(i);
            }
        }
    }
}
