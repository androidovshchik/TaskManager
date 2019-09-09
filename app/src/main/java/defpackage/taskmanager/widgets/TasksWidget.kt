/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import defpackage.taskmanager.R

class TasksWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            updateWidget(context, appWidgetManager, id)
        }
    }

    companion object {

        @JvmStatic
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetID: Int) {
            val widgetView = RemoteViews(context.packageName, R.layout.widget_tasks).apply {

            }
            appWidgetManager.updateAppWidget(widgetID, widgetView)
        }
    }
}