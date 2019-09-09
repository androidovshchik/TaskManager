/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import defpackage.taskmanager.R
import org.jetbrains.anko.intentFor

class TasksWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) = context.run {
        for (id in appWidgetIds) {
            appWidgetManager.updateAppWidget(id, RemoteViews(packageName, R.layout.widget_tasks).apply {
                setRemoteAdapter(R.id.widget_list, intentFor<TasksWidgetService>())
            })
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_list)
        }
    }
}