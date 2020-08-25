package com.shreehariji.servenotifire.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.shreehariji.servenotifire.tools.ServerAutoCheckerScheduler;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ServerAutoCheckerScheduler.InitiateAllAlarms(context);
        }
    }
}
