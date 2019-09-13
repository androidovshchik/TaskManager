/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.pendingReceiverFor
import org.jetbrains.anko.intentFor

class TasksWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, ids: IntArray) = context.run {
        for (id in ids) {
            appWidgetManager.updateAppWidget(id, RemoteViews(packageName, R.layout.widget_tasks).apply {
                setOnClickPendingIntent(R.id.widget_refresh, pendingReceiverFor(intentFor<TasksWidget>().apply {
                    action = ACTION_REFRESH
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                }))
                setRemoteAdapter(R.id.widget_list, intentFor<TasksWidgetService>())
            })
            appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_list)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_REFRESH -> {
                val id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                AppWidgetManager.getInstance(context)
                    .notifyAppWidgetViewDataChanged(id, R.id.widget_list)
            }
        }
    }

    companion object {

        private const val ACTION_REFRESH = "action_refresh"
    }
}