package com.shreehariji.servenotifire.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import com.shreehariji.servenotifire.R;
import com.shreehariji.servenotifire.activity.ServerListActivity;
import com.shreehariji.servenotifire.data.Server;
import com.shreehariji.servenotifire.data.ServerDAO;
import com.shreehariji.servenotifire.data.WidgetDAO;
import com.shreehariji.servenotifire.tools.ServerChecker;
import java.util.Date;

public class OneServerWidgetProvider extends AppWidgetProvider {

    public static class CLICK_ACTIONS {
        public static final int MANUAL_CHECK = 1;
        public static final int OPEN_SERVER_LIST = 0;
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Server server;
        Integer image_res;
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.one_server_widget);
        for (int appWidgetId : appWidgetIds) {
            Log.i("Updating widget", Integer.valueOf(appWidgetId).toString());
            try {
                WidgetDAO widgetDAO = new WidgetDAO(context);
                widgetDAO.open();
                Integer serverId = widgetDAO.getServerOfWidget(appWidgetId);
                widgetDAO.close();
                ServerDAO serverDAO = new ServerDAO(context);
                serverDAO.open();
                server = serverDAO.select(serverId);
                serverDAO.close();
            } catch (Exception e) {
                server = null;
            }
            if (server == null) {
                views.setTextViewText(R.id.widgetTxtServerName, "");
                views.setImageViewResource(R.id.widgetImgServerCheckStatus, R.drawable.question);
                views.setTextViewText(R.id.widgetTxtServerCheckTimestamp, context.getString(R.string.widget_unknown_server));
            } else {
                views.setTextViewText(R.id.widgetTxtServerName, server.getName());
                switch (server.getCheckStatus().intValue()) {
                    case 0:
                        image_res = R.drawable.ic_close_24dp;
                        break;
                    case 1:
                        image_res = Integer.valueOf(R.drawable.ic_check_24dp);
                        break;
                    case 2:
                        image_res = Integer.valueOf(R.drawable.ic_autorenew_24dp);
                        break;
                    default:
                        image_res = Integer.valueOf(R.drawable.question);
                        break;
                }
                views.setImageViewResource(R.id.widgetImgServerCheckStatus, image_res.intValue());
                Date date = new Date(server.getCheckTimestamp().longValue() * 1000);
                views.setTextViewText(R.id.widgetTxtServerCheckTimestamp, DateFormat.getDateFormat(context).format(date) + "\n" + DateFormat.getTimeFormat(context).format(date));
                Intent clickIntent = new Intent(context, OneServerWidgetProvider.class);
                clickIntent.putExtra("server_id", server.getId());
                views.setOnClickPendingIntent(R.id.widgetOneServer, PendingIntent.getBroadcast(context, appWidgetId, clickIntent, 268435456));
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        WidgetDAO widgetDAO = new WidgetDAO(context);
        widgetDAO.open();
        for (int appWidgetId : appWidgetIds) {
            Log.i("Deleting widget", Integer.valueOf(appWidgetId).toString());
            widgetDAO.deleteWidget(Integer.valueOf(appWidgetId));
        }
        widgetDAO.close();
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Integer server_id_check = Integer.valueOf(intent.getIntExtra("server_id", 0));
        Log.i("Widget clicked", String.valueOf(server_id_check));
        if (server_id_check.intValue() != 0) {
            switch (Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("widget_action", String.valueOf(context.getResources().getInteger(R.integer.default_widget_action)))).intValue()) {
                case 0:
                    Intent clickIntent = new Intent(context, ServerListActivity.class);
                    clickIntent.setFlags(268435456);
                    context.startActivity(clickIntent);
                    return;
                case 1:
                    new ServerChecker(server_id_check, context).execute(new Void[0]);
                    return;
                default:
                    return;
            }
        }
    }
}
