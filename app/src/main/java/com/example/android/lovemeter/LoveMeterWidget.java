package com.example.android.lovemeter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.android.lovemeter.data.ResultsContract;

/**
 * Implementation of App Widget functionality.
 */
public class LoveMeterWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Cursor cursor = context.getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI, null, null, null, null);
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.love_meter_widget);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                views.setTextViewText(R.id.tvWidget,
                        cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_TESTER_NAME))
                                + cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_TEST_NAME))
                                + cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_RESULT ))+" %"+"\n");
            }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

