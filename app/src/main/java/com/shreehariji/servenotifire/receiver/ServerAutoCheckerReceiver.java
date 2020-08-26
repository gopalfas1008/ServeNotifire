package com.shreehariji.servenotifire.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.shreehariji.servenotifire.tools.ServerChecker;

public class ServerAutoCheckerReceiver extends BroadcastReceiver {
    private Context context;

    public static class INTENT_KEYS {
        public static final String SERVER_ID = "SERVER_ID";
    }

    public void onReceive(Context _context, Intent intent) {
        this.context = _context;
        Integer server_check_id = intent.getIntExtra("SERVER_ID", -1);
        Log.i("Alarm triggered", server_check_id.toString());
        WakeLock wakeLock = ((PowerManager) this.context.getSystemService("power")).newWakeLock(1, "ServerCheckInProgress");
        wakeLock.acquire();
        new ServerChecker(server_check_id, this.context, wakeLock).execute();
    }
}
