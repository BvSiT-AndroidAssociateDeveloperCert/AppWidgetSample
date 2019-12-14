package com.example.appwidgetsample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.widget.RemoteViews;

import com.example.appwidgetsample.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile =
            "com.example.appwidgetsample";
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        // Instruct the widget manager to update the widget

        views.setTextViewText(R.id.appwidget_id,String.valueOf(appWidgetId));

        SharedPreferences prefs = context.getSharedPreferences(mSharedPrefFile,0);
        int count = prefs.getInt(COUNT_KEY+appWidgetId,0);
        count++;
        prefs.edit().putInt(COUNT_KEY+appWidgetId,count).apply();

        String dateString =
                DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL).format(new Date());

        views.setTextViewText(R.id.appwidget_update,
                context.getResources().getString(
                        R.string.date_count_format,count,dateString));

        Intent intent = new Intent(context,NewAppWidget.class);
        //intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray);

        PendingIntent p = PendingIntent.getBroadcast(context,appWidgetId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.button_update,p);

        //BvS This line AFTER setting the update intent!!
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

