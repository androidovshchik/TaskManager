/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Task
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class TasksWidgetService : RemoteViewsService(), KodeinAware {

    override val kodein by kodein()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = Factory()

    inner class Factory : RemoteViewsFactory {

        private val tasks = arrayListOf<Task>()

        override fun onCreate() {
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
            tasks.add(Task())
        }

        override fun getViewAt(position: Int): RemoteViews {
            val item = tasks[position]
            return RemoteViews(packageName, R.layout.item_task).apply {
                setTextViewText(R.id.tasks_item_info, "2000-01-01 00:0$position")
                setTextViewText(R.id.tasks_item_title, "Задача $position. Длинное описание")
            }
        }

        override fun onDataSetChanged() {}

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = tasks.size

        override fun hasStableIds() = true

        override fun onDestroy() {}
    }
}