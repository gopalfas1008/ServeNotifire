package com.shreehariji.servenotifire.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.tools.ServerAutoCheckerScheduler;

@SuppressLint("WrongConstant")
public class ServerEditorActivity extends AppCompatActivity {
    private static final String DEFAULT_ADDRESS = "http://";
    private static final Integer DEFAULT_CHECK_INTERVAL = Integer.valueOf(15);
    private static final Integer DEFAULT_CHECK_NUMBER_LIMIT = Integer.valueOf(2);
    private static final String DEFAULT_NAME = "";

    public static class ACTIONS {
        public static final String ADD = "ADD";
        public static final String UPDATE = "UPDATE";
    }

    public static class INTENT_KEYS {
        public static final String ACTION = "ACTION";
        public static final String SERVER_ID = "SERVER_ID";
    }

    /* access modifiers changed from: protected */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        Server server = null;
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_server_editor);
        setupActionBar();
        Button btnValidate = (Button) findViewById(R.id.btnAdd);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Integer server_check_interval;
                Integer server_check_fail_threshold;
                String server_name = ((EditText) findViewById(R.id.txtName)).getText().toString().trim();
                if (server_name.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.activity_server_editor_server_name_error, 1).show();
                    return;
                }
                String server_address = ((EditText) findViewById(R.id.txtAddress)).getText().toString().trim();
                if (server_address.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.activity_server_editor_server_address_error, 1).show();
                    return;
                }
                try {
                    server_check_interval = Integer.valueOf(Integer.parseInt(((EditText) findViewById(R.id.txtCheckInterval)).getText().toString()));
                } catch (Exception e) {
                    server_check_interval = Integer.valueOf(0);
                }
                if (server_check_interval.intValue() <= 0) {
                    Toast.makeText(getApplicationContext(), R.string.activity_server_editor_server_check_interval_error, 1).show();
                    return;
                }
                try {
                    server_check_fail_threshold = Integer.valueOf(Integer.parseInt(((EditText) findViewById(R.id.txtCheckFailThreshold)).getText().toString()));
                } catch (Exception e2) {
                    server_check_fail_threshold = Integer.valueOf(0);
                }
                if (server_check_fail_threshold.intValue() <= 0) {
                    Toast.makeText(getApplicationContext(), R.string.activity_server_editor_server_check_fail_threshold, 1).show();
                    return;
                }
                Server inputServer = new Server(Integer.valueOf(((EditText) findViewById(R.id.txtId)).getText().toString()), server_name, server_address, server_check_interval, server_check_fail_threshold);
                ServerDAO serverDao = new ServerDAO(getApplicationContext());
                serverDao.open();
                Intent intent = getIntent();
                String stringExtra = intent.getStringExtra(INTENT_KEYS.ACTION);
                char c = 65535;
                switch (stringExtra.hashCode()) {
                    case -1785516855:
                        if (stringExtra.equals(ACTIONS.UPDATE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 64641:
                        if (stringExtra.equals(ACTIONS.ADD)) {
                            c = 0;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        inputServer.setId(Integer.valueOf((int) serverDao.add(inputServer)));
                        intent.putExtra("SERVER_ID", inputServer.getId());
                        ServerAutoCheckerScheduler.AddAlarm(getApplicationContext(), inputServer);
                        Toast.makeText(getApplicationContext(), getString(R.string.activity_server_editor_toast_add, new Object[]{inputServer.getName()}), 1).show();
                        break;
                    case 1:
                        inputServer.setCheckFailCount(Integer.valueOf(0));
                        serverDao.update(inputServer);
                        if (!inputServer.getDisabled().booleanValue()) {
                            ServerAutoCheckerScheduler.UpdateAlarm(getApplicationContext(), inputServer);
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.activity_server_editor_toast_update, new Object[]{inputServer.getName()}), 1).show();
                        break;
                }
                serverDao.close();
                setResult(-1, intent);
                finish();
            }
        });
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(INTENT_KEYS.ACTION);
        if (stringExtra.equals(ACTIONS.UPDATE)) {
            z = true;
            setTitle(R.string.activity_server_editor_update_server);
            btnValidate.setText(R.string.activity_server_editor_update_server);
            ServerDAO serverDAO = new ServerDAO(this);
            serverDAO.open();
            server = serverDAO.select(Integer.valueOf(intent.getIntExtra("SERVER_ID", -1)));
            serverDAO.close();
        }
        if (stringExtra.equals(ACTIONS.ADD)) {
            setTitle(R.string.activity_server_editor_add_server);
            btnValidate.setText(R.string.activity_server_editor_add_server);
            server = new Server(Integer.valueOf(1), "", DEFAULT_ADDRESS, DEFAULT_CHECK_INTERVAL, DEFAULT_CHECK_NUMBER_LIMIT);
            z = false;
        }

        ((EditText) findViewById(R.id.txtId)).setText(server.getId().toString());
        ((EditText) findViewById(R.id.txtName)).setText(server.getName());
        ((EditText) findViewById(R.id.txtAddress)).setText(server.getAddress());
        ((EditText) findViewById(R.id.txtCheckInterval)).setText(server.getCheckInterval().toString());
        ((EditText) findViewById(R.id.txtCheckFailThreshold)).setText(server.getCheckFailThreshold().toString());
        ((Button) findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(0);
                finish();
            }
        });
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }
}
