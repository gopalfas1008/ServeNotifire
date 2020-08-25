package com.shreehariji.servenotifire.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

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
    public void onCreate(android.os.Bundle r13) {
        /*
            r12 = this;
            r11 = 2131165223(0x7f070027, float:1.7944657E38)
            r5 = 2131165211(0x7f07001b, float:1.7944633E38)
            r3 = 1
            r2 = -1
            super.onCreate(r13)
            r1 = 2130968603(0x7f04001b, float:1.7545864E38)
            r12.setContentView(r1)
            r12.setupActionBar()
            r1 = 2131624057(0x7f0e0079, float:1.8875283E38)
            android.view.View r7 = r12.findViewById(r1)
            android.widget.Button r7 = (android.widget.Button) r7
            com.shreehariji.servenotifire.activity.ServerEditorActivity$1 r1 = new com.shreehariji.servenotifire.activity.ServerEditorActivity$1
            r1.<init>()
            r7.setOnClickListener(r1)
            android.content.Intent r8 = r12.getIntent()
            java.lang.String r1 = "ACTION"
            java.lang.String r1 = r8.getStringExtra(r1)
            int r4 = r1.hashCode()
            switch(r4) {
                case -1785516855: goto L_0x0045;
                case 64641: goto L_0x003b;
                default: goto L_0x0036;
            }
        L_0x0036:
            r1 = r2
        L_0x0037:
            switch(r1) {
                case 0: goto L_0x004f;
                case 1: goto L_0x00d5;
                default: goto L_0x003a;
            }
        L_0x003a:
            return
        L_0x003b:
            java.lang.String r4 = "ADD"
            boolean r1 = r1.equals(r4)
            if (r1 == 0) goto L_0x0036
            r1 = 0
            goto L_0x0037
        L_0x0045:
            java.lang.String r4 = "UPDATE"
            boolean r1 = r1.equals(r4)
            if (r1 == 0) goto L_0x0036
            r1 = r3
            goto L_0x0037
        L_0x004f:
            r12.setTitle(r5)
            r7.setText(r5)
            com.shreehariji.servenotifire.data.Server r0 = new com.shreehariji.servenotifire.data.Server
            java.lang.Integer r1 = java.lang.Integer.valueOf(r3)
            java.lang.String r2 = ""
            java.lang.String r3 = "http://"
            java.lang.Integer r4 = DEFAULT_CHECK_INTERVAL
            java.lang.Integer r5 = DEFAULT_CHECK_NUMBER_LIMIT
            r0.<init>(r1, r2, r3, r4, r5)
        L_0x0066:
            r1 = 2131624047(0x7f0e006f, float:1.8875263E38)
            android.view.View r1 = r12.findViewById(r1)
            android.widget.EditText r1 = (android.widget.EditText) r1
            java.lang.Integer r2 = r0.getId()
            java.lang.String r2 = r2.toString()
            r1.setText(r2)
            r1 = 2131624050(0x7f0e0072, float:1.8875269E38)
            android.view.View r1 = r12.findViewById(r1)
            android.widget.EditText r1 = (android.widget.EditText) r1
            java.lang.String r2 = r0.getName()
            r1.setText(r2)
            r1 = 2131624052(0x7f0e0074, float:1.8875273E38)
            android.view.View r1 = r12.findViewById(r1)
            android.widget.EditText r1 = (android.widget.EditText) r1
            java.lang.String r2 = r0.getAddress()
            r1.setText(r2)
            r1 = 2131624054(0x7f0e0076, float:1.8875277E38)
            android.view.View r1 = r12.findViewById(r1)
            android.widget.EditText r1 = (android.widget.EditText) r1
            java.lang.Integer r2 = r0.getCheckInterval()
            java.lang.String r2 = r2.toString()
            r1.setText(r2)
            r1 = 2131624056(0x7f0e0078, float:1.887528E38)
            android.view.View r1 = r12.findViewById(r1)
            android.widget.EditText r1 = (android.widget.EditText) r1
            java.lang.Integer r2 = r0.getCheckFailThreshold()
            java.lang.String r2 = r2.toString()
            r1.setText(r2)
            r1 = 2131624058(0x7f0e007a, float:1.8875285E38)
            android.view.View r6 = r12.findViewById(r1)
            android.widget.Button r6 = (android.widget.Button) r6
            com.shreehariji.servenotifire.activity.ServerEditorActivity$2 r1 = new com.shreehariji.servenotifire.activity.ServerEditorActivity$2
            r1.<init>()
            r6.setOnClickListener(r1)
            goto L_0x003a
        L_0x00d5:
            r12.setTitle(r11)
            r7.setText(r11)
            com.shreehariji.servenotifire.data.ServerDAO r9 = new com.shreehariji.servenotifire.data.ServerDAO
            r9.<init>(r12)
            r9.open()
            java.lang.String r1 = "SERVER_ID"
            int r1 = r8.getIntExtra(r1, r2)
            java.lang.Integer r10 = java.lang.Integer.valueOf(r1)
            com.shreehariji.servenotifire.data.Server r0 = r9.select(r10)
            r9.close()
            goto L_0x0066
        */
        super.onCreate(r13);
        throw new UnsupportedOperationException("Method not decompiled: com.shreehariji.servenotifire.activity.ServerEditorActivity.onCreate(android.os.Bundle):void");
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
