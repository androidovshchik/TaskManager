/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import defpackage.taskmanager.EXTRA_STATUS
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.R
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.extensions.pendingReceiverFor
import defpackage.taskmanager.receivers.ActionReceiver
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TasksWidgetService : RemoteViewsService(), KodeinAware {

    override val kodein by kodein()

    val dbManager: DbManager by instance()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = Factory()

    inner class Factory : RemoteViewsFactory {

        private val tasks = arrayListOf<Task>()

        override fun onCreate() {}

        override fun getViewAt(position: Int): RemoteViews {
            val item = tasks[position]
            return RemoteViews(packageName, R.layout.item_task).apply {
                setTextViewText(R.id.tasks_item_info, item.getPossibleTime())
                setTextViewText(R.id.tasks_item_title, item.title)
                setOnClickPendingIntent(
                    R.id.tasks_item_complete, pendingReceiverFor<ActionReceiver>(
                        EXTRA_TASK to item.id,
                        EXTRA_STATUS to Record.STATUS_COMPLETED
                    )
                )
                setOnClickPendingIntent(
                    R.id.tasks_item_defer, pendingReceiverFor<ActionReceiver>(
                        EXTRA_TASK to item.id,
                        EXTRA_STATUS to Record.STATUS_DEFERRED
                    )
                )
                setOnClickPendingIntent(
                    R.id.tasks_item_cancel, pendingReceiverFor<ActionReceiver>(
                        EXTRA_TASK to item.id,
                        EXTRA_STATUS to Record.STATUS_CANCELLED
                    )
                )
            }
        }

        override fun onDataSetChanged() {
            dbManager.io.if (dbManager.io.isLocked) {

            }
            dbManager.getAllTasks()
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = tasks[position].id

        override fun getCount(): Int = tasks.size

        override fun hasStableIds() = true

        override fun onDestroy() {}
    }
}