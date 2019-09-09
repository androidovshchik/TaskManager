/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService

class TasksWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) =
        CardRemoteViewFactory(
            this, intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        )
}