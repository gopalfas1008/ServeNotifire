package com.shreehariji.servenotifire.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.activity.ServerListActivity;
import com.shreehariji.servenotifire.data.LogDAO;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.data.WidgetDAO;
import com.shreehariji.servenotifire.widget.OneServerWidgetProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerChecker extends AsyncTask<Void, Void, Void> {
    private String connectionInfo;
    private Context context;
    private Boolean isManualCheck;
    private Server server;
    private WakeLock wakeLock;

    public ServerChecker(Integer _server_id, Context _context, WakeLock _wakelock) {
        this(_server_id, _context, Boolean.valueOf(false), _wakelock);
    }

    public ServerChecker(Integer _server_id, Context _context) {
        this(_server_id, _context, Boolean.valueOf(true), null);
    }

    private ServerChecker(Integer _server_id, Context _context, Boolean _isManualCheck, WakeLock _wakelock) {
        this.connectionInfo = "";
        this.context = _context;
        this.isManualCheck = _isManualCheck;
        this.wakeLock = _wakelock;
        ServerDAO serverDAO = new ServerDAO(this.context);
        serverDAO.open();
        this.server = serverDAO.select(_server_id);
        serverDAO.close();
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(Void... params) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        NetworkInfo activeNetwork = ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            this.server.setCheckStatus(3);
        } else {
            if (activeNetwork.isRoaming()) {
                if (!prefs.getBoolean("allow_roaming", this.context.getResources().getBoolean(R.bool.default_allow_roaming))) {
                    this.server.setCheckStatus(3);
                }
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(this.server.getAddress()).openConnection();
                connection.setConnectTimeout(Integer.parseInt(prefs.getString("timeout", String.valueOf(this.context.getResources().getInteger(R.integer.default_timeout)))) * 1000);
                connection.connect();

                this.connectionInfo = connection.getResponseCode() + " " + connection.getResponseMessage();
                String responseString="";
                if(server.getAddress().contains("mms")) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        ArrayList<String> response = new ArrayList<>();
                        String responseLine = null;
//                String responseLine =  br.lines().collect(Collectors.joining("\n"));
                        while ((responseLine = br.readLine()) != null) {
                            responseString +=responseLine;
                        }
//                        responseString = TextUtils.join("\n", response);
                    } catch (Exception e) {
                        responseString = e.getMessage();
                    }
                    if (connection.getResponseCode() < 500) {
                        if(!responseString.equals("0")) {
                            this.server.setCheckStatus(0);
                        }else {
                            this.server.setCheckStatus(1);
                        }
                    } else {
                            this.server.setCheckStatus(0);
                    }
                }else {
                    if (connection.getResponseCode() < 500) {
                        this.server.setCheckStatus(1);
                    } else {
                        this.server.setCheckStatus(0);
                    }
                }
                Log.i("Server check code", this.server.getAddress() + " =" + connection.getResponseCode() + " Res=" + responseString);


                connection.disconnect();
            } catch (Exception e) {
                Log.i("Server check error", e.toString());
                this.connectionInfo = e.getMessage();
                this.server.setCheckStatus(0);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
        this.server.setCheckStatus(2);
        this.server.setCheckTimestamp(System.currentTimeMillis() / 1000);
        updateServerData();
        updateUI();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.server.setCheckTimestamp(Long.valueOf(System.currentTimeMillis() / 1000));
        if (this.isManualCheck.booleanValue()) {
            manualCheck();
        } else {
            automaticCheck();
        }
    }

    private void manualCheck() {
        updateServerData();
        updateUI();
    }

    private void automaticCheck() {
        switch (this.server.getCheckStatus().intValue()) {
            case 0:
                this.server.setCheckFailCount(this.server.getCheckFailCount().intValue() + 1);
                if (this.server.getCheckFailCount() >= this.server.getCheckFailThreshold().intValue()) {
                    notifyOfflineServer();
                    break;
                }
                break;
            case 1:
                this.server.setCheckFailCount(Integer.valueOf(0));
                break;
        }
        updateServerData();
        updateUI();
        if (Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(0)}).contains(this.server.getCheckStatus())) {
            writeLog();
        }
        this.wakeLock.release();
    }

    private void notifyOfflineServer() {
        String contentText;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        if (this.server.getCheckFailCount().intValue() == 1) {
            contentText = this.context.getString(R.string.receiver_server_check_failed1, new Object[]{this.server.getName()});
        } else {
            contentText = this.context.getString(R.string.receiver_server_check_failed2, new Object[]{this.server.getName(), this.server.getCheckFailCount()});
        }
        Intent notificationIntent = new Intent(this.context, ServerListActivity.class);
        notificationIntent.setFlags(603979776);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, notificationIntent, 0);
        if (Boolean.valueOf(prefs.getBoolean("enable_notifications", Boolean.valueOf(this.context.getResources().getBoolean(R.bool.default_enable_notifications)).booleanValue())).booleanValue()) {
            NotificationManager manager = ((NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE));

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer = MediaPlayer.create(context, R.raw.smb_gameover);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            Notification.Builder mBuilder = new Notification.Builder(this.context).setSmallIcon(R.drawable.notify).setAutoCancel(true).setContentTitle(this.context.getString(R.string.app_name)).setContentText(contentText).setContentIntent(pendingIntent);
            String notifications_ringtone = prefs.getString("notifications_ringtone", this.context.getResources().getString(R.string.default_notifications_ringtone));
            if (!notifications_ringtone.equals("")) {
//                mBuilder.setSound(Uri.parse(notifications_ringtone));
//                mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.context.getPackageName() + "/" + R.raw.smb_gameover));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "Server_Test";

                String CHANNEL_NAME = "Notification";
                // I would suggest that you use IMPORTANCE_DEFAULT instead of IMPORTANCE_HIGH
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.setLightColor(Color.BLUE);
                channel.enableLights(true);
//                channel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.context.getPackageName() + "/" + R.raw.smb_gameover),
//                        new AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                                .build());
                //channel.canShowBadge();
                // Did you mean to set the property to enable Show Badge?
                channel.setShowBadge(true);
                manager.createNotificationChannel(channel);
            }
            if (Boolean.valueOf(prefs.getBoolean("notifications_vibrate", Boolean.valueOf(this.context.getResources().getBoolean(R.bool.default_notifications_vibrate)).booleanValue())).booleanValue()) {
                Log.i("Notification vibration", "ok");
                mBuilder.setVibrate(new long[]{0, 2000});
            }

            manager.notify(this.server.getId(), mBuilder.build());
        }
    }

    private void updateServerData() {
        ServerDAO serverDAO = new ServerDAO(this.context);
        serverDAO.open();
        serverDAO.update(this.server);
        serverDAO.close();
    }

    private void updateUI() {
        updateWidgets();
        this.context.sendBroadcast(new Intent("com.shreehariji.servenotifire.UPDATE_SERVER_LIST"));
    }

    private void updateWidgets() {
        WidgetDAO widgetDAO = new WidgetDAO(this.context);
        widgetDAO.open();
        Cursor cursor = widgetDAO.getWidgetsOfServer(this.server.getId());
        int[] ids = new int[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            ids[i] = cursor.getInt(0);
            i++;
        }
        widgetDAO.close();
        if (i > 0) {
            Intent updateIntent = new Intent(this.context, OneServerWidgetProvider.class);
            updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            updateIntent.putExtra("appWidgetIds", ids);
            this.context.sendBroadcast(updateIntent);
        }
    }

    private void writeLog() {
        Integer logs_depth = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this.context).getString("logs_depth", String.valueOf(this.context.getResources().getInteger(R.integer.default_logs_depth))));
        if (logs_depth > 0) {
            LogDAO logDAO = new LogDAO(this.context);
            logDAO.open();
            StringBuilder append = new StringBuilder().append("<font color='").append(this.server.getCheckStatus() == 1 ? "#00C407" : "#FF0000").append("'>");
            Context context2 = this.context;
            String[] strArr = new String[3];
            strArr[0] = this.server.getName();
            strArr[1] = this.server.getAddress();
            strArr[2] = this.context.getString(this.server.getCheckStatus() == 1 ? R.string.receiver_server_check_log_online : R.string.receiver_server_check_log_offline) + " (" + this.connectionInfo + ")";
            logDAO.add(this.server.getId(),append.append(context2.getString(R.string.receiver_server_check_log, strArr)).append("</font>").toString());
            logDAO.deleteOld(logs_depth);
            logDAO.close();
        }
    }
}
