/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import defpackage.taskmanager.*
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.EventTask
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.pendingReceiverFor
import defpackage.taskmanager.receivers.ActionReceiver
import defpackage.taskmanager.screens.tasks.TasksActivity
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TasksWidgetService : RemoteViewsService(), KodeinAware {

    override val kodein by kodein()

    val dbManager: DbManager by instance()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = Factory()

    private inner class Factory : RemoteViewsFactory {

        private val items = arrayListOf<EventTask>()

        override fun onCreate() {}

        override fun getViewAt(position: Int): RemoteViews {
            val item = items[position]
            val visibility = if (item.event.willBeInFuture) View.GONE else View.VISIBLE
            return RemoteViews(packageName, R.layout.item_task).apply {
                setTextViewText(R.id.tasks_item_info, SIMPLE_DATETIME.format(item.event.nextTime))
                setTextViewText(R.id.tasks_item_title, item.title)
                setOnClickPendingIntent(
                    R.id.task_widget_container, pendingActivityFor<TasksActivity>(
                        EXTRA_ID to item.event.task
                    )
                )
                setOnClickPendingIntent(
                    R.id.tasks_item_complete, pendingReceiverFor<ActionReceiver>(
                        EXTRA_ID to item.event.task,
                        EXTRA_ACTION to ACTION_COMPLETED
                    )
                )
                setViewVisibility(R.id.tasks_item_defer, visibility)
                setOnClickPendingIntent(
                    R.id.tasks_item_defer, pendingReceiverFor<ActionReceiver>(
                        EXTRA_ID to item.event.task,
                        EXTRA_ACTION to ACTION_DEFERRED
                    )
                )
                setViewVisibility(R.id.tasks_item_cancel, visibility)
                setOnClickPendingIntent(
                    R.id.tasks_item_cancel, pendingReceiverFor<ActionReceiver>(
                        EXTRA_ID to item.event.task,
                        EXTRA_ACTION to ACTION_CANCELLED
                    )
                )
            }
        }

        override fun onDataSetChanged() {
            runBlocking {
                items.clear()
                items.addAll(dbManager.safeExecute {
                    getActualEvents(0)
                })
            }
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = items[position].event.task

        override fun getCount(): Int = items.size

        override fun hasStableIds() = true

        override fun onDestroy() {
            items.clear()
        }
    }
}