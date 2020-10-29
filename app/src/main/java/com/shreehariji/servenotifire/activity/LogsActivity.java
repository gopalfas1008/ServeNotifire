package com.shreehariji.servenotifire.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.adapter.LogsCursorAdapter;
import com.shreehariji.servenotifire.data.LogDAO;

public class LogsActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    int server_id=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_logs);

//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_logs));
        setupActionBar();
        server_id= getIntent().getIntExtra("server_id",0);
        loadLogs(server_id);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logs, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            case R.id.action_logs_refresh /*2131624099*/:
                loadLogs(server_id);
                break;
            case R.id.action_logs_clear /*2131624100*/:
                LogDAO logDAO = new LogDAO(getApplicationContext());
                logDAO.open();
                logDAO.deleteAll();
                logDAO.close();
                loadLogs(server_id);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLogs(int server) {
        LogDAO logDAO = new LogDAO(getApplicationContext());
        logDAO.open();
        ((ListView) findViewById(R.id.log_list)).setAdapter(new LogsCursorAdapter(this, logDAO.selectAll(server)));
        logDAO.close();
    }
}
