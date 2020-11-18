package com.shreehariji.servenotifire.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.receiver.ServerAutoCheckerReceiver;

public class ServerAutoCheckerScheduler {
    public static void InitiateAllAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ServerDAO serverDAO = new ServerDAO(context);
        serverDAO.open();
        Cursor cursor = serverDAO.selectAllEnabled();
        while (cursor.moveToNext()) {
            Server server = ServerDAO.cursorToServer(cursor);
            Intent intent = new Intent(context, ServerAutoCheckerReceiver.class);
            intent.putExtra("SERVER_ID", server.getId());
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, (long) (server.getCheckInterval() * 60 * 1000), PendingIntent.getBroadcast(context, server.getId(), intent, 0));
            Log.i("Alarm created (boot)", server.getAddress());
        }
        serverDAO.close();
    }

    public static void AddAlarm(Context context, Server server) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ServerAutoCheckerReceiver.class);
        intent.putExtra("SERVER_ID", server.getId());
        int interval = server.getCheckInterval() * 60 * 1000;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, (long) interval, (long) interval, PendingIntent.getBroadcast(context, server.getId(), intent, 0));
        Log.i("Alarm added", server.getAddress());
    }

    public static void UpdateAlarm(Context context, Server server) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ServerAutoCheckerReceiver.class);
        intent.putExtra("SERVER_ID", server.getId());
        int interval = server.getCheckInterval() * 60 * 1000;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, (long) interval, (long) interval, PendingIntent.getBroadcast(context, server.getId().intValue(), intent, 268435456));
        Log.i("Alarm updated", server.getAddress());
    }

    public static void DeleteAlarm(Context context, Integer server_id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ServerAutoCheckerReceiver.class);
        intent.putExtra("SERVER_ID", server_id);
        alarmManager.cancel(PendingIntent.getBroadcast(context, server_id, intent, 268435456));
        Log.i("Alarm deleted", server_id.toString());
    }
}
